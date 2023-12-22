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
                    .bookingFrom(reservationArenaDto.getBookingFrom())
                    .bookingTo(reservationArenaDto.getBookingTo())
                    .description(reservationArenaDto.getDescription())
                    .costumer(reservationArenaDto.getCostumer())
                    .totalPrice((int) (arena.getArenaInfo().getPrice() * (
                            reservationArenaDto.getBookingTo().getEpochSecond() -
                                    reservationArenaDto.getBookingFrom().getEpochSecond()) / 3600))
                    .build();
            BookingUser bookingUser = new BookingUser();
            bookingUser.setBooking(reservationArena);
            if (SecurityUtils.getCurrentUserId() != null) {
                bookingUser.setUser(userRepository.findById(SecurityUtils.getCurrentUserId()).orElseThrow(
                        () -> new ResourceNotFoundException("Not found user with id: " + SecurityUtils.getCurrentUserId())
                ));
            } else {
                if (reservationArenaDto.getCostumer() == null) {
                    bookingUser.setConsumer(SecurityUtils.getCurrentUsername());
                    bookingUser.setConsumer(SecurityUtils.getCurrentUsername());
                } else {
                    bookingUser.setConsumer(reservationArenaDto.getCostumer());
                }
            }
            reservationArenaRepository.save(reservationArena);
            log.debug("Created reservation arena with id: {}, from user by username: {}"
                    , reservationArena.getId(), SecurityUtils.getCurrentUsername());
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
            reservationArena.setBookingFrom(reservationArenaDto.getBookingFrom());
            reservationArena.setBookingTo(reservationArenaDto.getBookingTo());
            reservationArena.setDescription(reservationArenaDto.getDescription());
            reservationArena.setCostumer(reservationArenaDto.getCostumer());
            reservationArena.setTotalPrice((int) (reservationArena.getArena().getArenaInfo().getPrice() * (
                    reservationArenaDto.getBookingTo().getEpochSecond()
                            - reservationArenaDto.getBookingFrom().getEpochSecond()) / 3600));
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
        }
    }
}
