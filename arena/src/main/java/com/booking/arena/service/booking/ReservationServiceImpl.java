package com.booking.arena.service.booking;

import com.booking.arena.dto.booking.ReservationArenaDto;
import com.booking.arena.entity.arena.ArenaEntity;
import com.booking.arena.entity.booking.BookingUser;
import com.booking.arena.entity.booking.ReservationArena;
import com.booking.arena.exception.ResourceNotFoundException;
import com.booking.arena.repository.arena.ArenaRepository;
import com.booking.arena.repository.booking.ReservationArenaRepository;
import com.booking.arena.repository.user.UserRepository;
import com.booking.arena.utils.ConvertEntityToDto;
import com.booking.arena.utils.SecurityUtils;
import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class ReservationServiceImpl implements ReservationService{
    private final ReservationArenaRepository reservationArenaRepository;
    private final ArenaRepository arenaRepository;
    private final UserRepository userRepository;


    @Override
    public Optional<ReservationArenaDto> getById(Long id) {
        ReservationArena reservationArena = reservationArenaRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Not found reservation arena with id: " + id)
        );
        return Optional.of((ConvertEntityToDto.reservationArenaToDto(reservationArena)));
    }

    @Override
    public List<ReservationArenaDto> getAll() {
        List<ReservationArena> reservations = reservationArenaRepository.findAll();
        return reservations.stream().map(ConvertEntityToDto::reservationArenaToDto).toList();
    }

    @Override
    public Optional<ReservationArenaDto> create(ReservationArenaDto reservationArenaDto) {
        ArenaEntity arena = arenaRepository.findById(reservationArenaDto.getArenaId()).orElseThrow(
                () -> new ResourceNotFoundException("Not found arena with id: " + reservationArenaDto.getArenaId())
        );
        try {
            ReservationArena reservationArena = ReservationArena.builder()
                    .createdAt(Instant.now())
                    .arena(arena)
                    .description(reservationArenaDto.getDescription())
                    .costumer(reservationArenaDto.getCostumer())
                    .totalPrice((int) (arena.getArenaInfo().getPrice() * (
                            reservationArenaDto.getBookingTo().getEpochSecond() -
                                    reservationArenaDto.getBookingFrom().getEpochSecond()) / 3600))
                    .build();
            if (getByReservationByTime(arena, reservationArenaDto.getBookingFrom(), reservationArenaDto.getBookingTo())) {
                reservationArena.setBookingFrom(reservationArenaDto.getBookingFrom());
                reservationArena.setBookingTo(reservationArenaDto.getBookingTo());
            } else {
                throw new ResourceNotFoundException("Invalid, not free time ");
            }
            BookingUser bookingUser = new BookingUser();
            bookingUser.setBooking(reservationArena);
            if (SecurityUtils.getCurrentUserId() != null) {
                bookingUser.setUser(userRepository.findById(SecurityUtils.getCurrentUserId()).orElseThrow());
                bookingUser.setConsumer(SecurityUtils.getCurrentUsername());
            }
            reservationArena.setCostumer(reservationArenaDto.getCostumer());
            bookingUser.setConsumer(reservationArenaDto.getCostumer());
            reservationArenaRepository.save(reservationArena);
            log.debug("Created reservation arena with name: {}"
                    , reservationArena.getArena().getName());
            return Optional.of(ConvertEntityToDto.reservationArenaToDto(reservationArena));
        } catch (Exception e) {
            throw new ResourceNotFoundException("Invalid reservation details\n" + e.getMessage());
        }
    }

    @Override
    public Optional<ReservationArenaDto> update(Long id, ReservationArenaDto reservationArenaDto) {
        ReservationArena reservationArena = reservationArenaRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Not found reservation arena with id: " + id)
        );
        ArenaEntity arena = arenaRepository.findById(reservationArenaDto.getArenaId()).orElseThrow(
                () -> new ResourceNotFoundException("Not found arena with id: " + reservationArenaDto.getArenaId())
        );
        try {
            reservationArena.setArena(arena);
            reservationArena.setDescription(reservationArenaDto.getDescription());
            reservationArena.setCostumer(reservationArenaDto.getCostumer());
            reservationArena.setTotalPrice((int) (reservationArena.getArena().getArenaInfo().getPrice() * (
                    reservationArenaDto.getBookingTo().getEpochSecond()
                            - reservationArenaDto.getBookingFrom().getEpochSecond()) / 3600));
            if (getByReservationByTime(arena, reservationArenaDto.getBookingFrom(), reservationArenaDto.getBookingTo())) {
                reservationArena.setBookingFrom(reservationArenaDto.getBookingFrom());
                reservationArena.setBookingTo(reservationArenaDto.getBookingTo());
            } else {
                throw new ResourceNotFoundException("Invalid, not free time");
            }
            reservationArenaRepository.save(reservationArena);
            log.debug("Updated reservation arena with id: {}, from user by username: {}"
                    , reservationArena.getId(), SecurityUtils.getCurrentUsername());
            return Optional.of(ConvertEntityToDto.reservationArenaToDto(reservationArena));
        } catch (Exception e) {
            throw new ResourceNotFoundException("Invalid reservation details\n" + e.getMessage());
        }
    }

    @Override
    public void delete(Long id) {
        if (reservationArenaRepository.findById(id).isPresent()) {
            reservationArenaRepository.deleteById(id);
            log.info("Reservation arena deleted by id: {}", id);
        } else {
            throw new ResourceNotFoundException("Not found reservation arena with id: " + id);
        }
    }

    private boolean getByReservationByTime(ArenaEntity arena, Instant from, Instant to) {
        return arena.getReservationArena().stream().noneMatch(a -> a.getBookingFrom().isBefore(to) && a.getBookingTo().isAfter(from));
    }
}
