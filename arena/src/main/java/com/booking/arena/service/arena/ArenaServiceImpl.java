package com.booking.arena.service.arena;

import com.booking.arena.dto.arena.ArenaDto;
import com.booking.arena.dto.arena.ArenaFiltersDto;
import com.booking.arena.entity.arena.ArenaEntity;
import com.booking.arena.entity.arena.ArenaInfo;
import com.booking.arena.entity.user.UserEntity;
import com.booking.arena.exception.ResourceNotFoundException;
import com.booking.arena.repository.arena.ArenaRepository;
import com.booking.arena.repository.user.UserRepository;
import com.booking.arena.service.filesistem.ImageService;
import com.booking.arena.utils.ConvertEntityToDto;
import com.booking.arena.utils.DeserializeJson;
import com.booking.arena.utils.SecurityUtils;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;


import java.awt.geom.Point2D;
import java.time.Instant;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional
public class ArenaServiceImpl implements ArenaService{
    private final ArenaRepository arenaRepository;
    private final ArenaInfoService arenaInfoService;
    private final UserRepository userRepository;
    private final ImageService imageService;

    @Override
    public Optional<ArenaDto> getById(Long id) {
        ArenaEntity arena = arenaRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("Not found arena with id: " + id));
        return Optional.of(ConvertEntityToDto.arenaToDto(arena));
    }

    @Override
    public List<ArenaDto> getAll() {
        List<ArenaEntity> arenas = arenaRepository.findAll();
        return arenas.stream().map(ConvertEntityToDto::arenaToDto).toList();
    }

    @Override
    public List<ArenaDto> getByFilter(ArenaFiltersDto filters) {
        Set<ArenaEntity> arenas;
        if (filters.getCity() != null) {
            arenas = getByCity(arenaRepository.findAll(), filters.getCity());
        } else {
            arenas = new HashSet<>(arenaRepository.findAll());
        }

        if (filters.getFrom() != null && filters.getTo() != null) {
            arenas = getByReservationByTime(arenas, filters.getFrom(), filters.getTo());
        }
        if (filters.getLongitude() != null && filters.getLatitude() != null) {
            arenas = getSortedByDistance(arenas, filters.getLongitude()
            , filters.getLatitude());
        }
        return arenas.stream().map(ConvertEntityToDto::arenaToDto).toList();
    }

    @SneakyThrows
    private void uploadImage(ArenaEntity arena, ArenaDto object) {
        if (object.getImageFile() == null) {
            return;
        }
        Optional.ofNullable(object.getImageFile()).filter(Predicate.not(MultipartFile::isEmpty))
                .ifPresent(image -> arena.setImage(image.getOriginalFilename()));
        imageService.upload(arena.getImage(), object.getImageFile().getInputStream());
    }

    @Override
    public Optional<ArenaDto> create(String arenaDtoFromStr, MultipartFile file) {
        try {
            ArenaDto arenaDto = DeserializeJson.get(arenaDtoFromStr, ArenaDto.class);
            arenaDto.setImageFile(file);
            ArenaInfo arenaInfo = arenaInfoService.create(arenaDto.getArenaInfo()).orElseThrow();
            UserEntity user = userRepository.findById(Objects.requireNonNull(SecurityUtils.getCurrentUserId())).orElseThrow(
                    () -> new ResourceNotFoundException("User is Not valid")
            );
            ArenaEntity arena = ArenaEntity.builder()
                    .arenaInfo(arenaInfo)
                    .name(arenaDto.getName())
                    .description(arenaDto.getDescription())
                    .user(user)
                    .status(true)
                    .build();
            uploadImage(arena, arenaDto);
            arenaInfo.setArena(arena);
            arenaRepository.save(arena);
            log.info("Create arena with name: {}, from user by username: {}", arena.getName(), user.getUsername());
            return Optional.of(ConvertEntityToDto.arenaToDto(arena));
        } catch (Exception e) {
            throw new ResourceNotFoundException("Invalid input data\n" + e.getMessage());
        }
    }

    @Override
    public Optional<ArenaDto> update(Long id, String arenaDtoFromStr, MultipartFile file) {
        ArenaEntity arena = arenaRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("Not found arena with id: " + id));
        try {
            ArenaDto arenaDto = DeserializeJson.get(arenaDtoFromStr, ArenaDto.class);
            arenaDto.setImageFile(file);
            arena.setName(arenaDto.getName());
            arena.setDescription(arenaDto.getDescription());
            try {
                ArenaInfo arenaInfo = arenaInfoService.update(arena.getArenaInfo().getId(), arenaDto.getArenaInfo()).orElseThrow();
                arena.setArenaInfo(arenaInfo);
            } catch (Exception e) {
                throw new ResourceNotFoundException("OW" + e.getMessage());
            }
            uploadImage(arena, arenaDto);
            arenaRepository.save(arena);
            log.debug("Updated arena with name: {}", arena.getName());
            return Optional.of(ConvertEntityToDto.arenaToDto(arena));
        } catch (Exception e) {
            throw new ResourceNotFoundException("Invalid input data");
        }
    }

    @Override
    public void delete(Long id) {
        ArenaEntity arena = arenaRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("Not found arena with id: " + id));
        arenaRepository.deleteById(arena.getId());
        log.debug("Deleted arena with id: {}, from user by username: {}", arena.getId(), SecurityUtils.getCurrentUsername());
    }

    private Set<ArenaEntity> getByCity(List<ArenaEntity> all, String city) {
        Set<ArenaEntity> filtered = new HashSet<>();
        for (ArenaEntity arena : all) {
            if (arena.getArenaInfo().getAddress().getCity().getName().equals(city)) {
                filtered.add(arena);
            }
        }
        return filtered;
    }

    private Set<ArenaEntity> getByReservationByTime(Set<ArenaEntity> arenas, Instant from, Instant to) {
        return arenas.stream()
                .filter(arena -> arena.getReservationArena().stream()
                        .noneMatch(r -> r.getBookingFrom().isBefore(to) && r.getBookingTo().isAfter(from)))
                .collect(Collectors.toSet());
    }


    private Set<ArenaEntity> getSortedByDistance(Set<ArenaEntity> filtered, Double lng, Double lat) {
        Map<ArenaEntity, Double> distance = new HashMap<>();
        for (ArenaEntity arena : filtered) {
            distance.put(arena, Point2D.distance(arena.getArenaInfo().getAddress().getLongitude()
                    , arena.getArenaInfo().getAddress().getLatitude(), lng, lat));
        }
        return distance.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }
}
