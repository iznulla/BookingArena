package com.booking.arena.service.arena;

import com.booking.arena.dto.arena.ArenaInfoDto;
import com.booking.arena.entity.address.Address;
import com.booking.arena.entity.arena.ArenaInfo;
import com.booking.arena.exception.ResourceNotFoundException;
import com.booking.arena.repository.arena.ArenaInfoRepository;
import com.booking.arena.service.address.AddressService;
import com.booking.arena.utils.ConvertEntityToDto;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class ArenaInfoServiceImpl implements ArenaInfoService{
    private final ArenaInfoRepository arenaInfoRepository;
    private final AddressService addressService;


    @Override
    public Optional<ArenaInfo> getById(Long id) {
        return Optional.of(arenaInfoRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("Not found arena with id: " + id)));
    }

    @Override
    public Optional<ArenaInfo> create(ArenaInfoDto arenaInfoDto) {
        Address address = addressService.create(arenaInfoDto.getAddress()).orElseThrow(
                () -> new ResourceNotFoundException("Address is Not valid")
        );
        try {
            ArenaInfo arena = ArenaInfo.builder()
                    .createdAt(Instant.now())
                    .phone(arenaInfoDto.getPhone())
                    .price(arenaInfoDto.getPrice())
                    .workedFrom(arenaInfoDto.getWorkedFrom())
                    .workedTo(arenaInfoDto.getWorkedTo())
                    .updatedAt(Instant.now())
                    .address(address)
                    .build();
            arenaInfoRepository.save(arena);
            return Optional.of(arena);
        } catch (Exception e) {
            throw new ResourceNotFoundException("Invalid input data");
        }
    }

    @Override
    public Optional<ArenaInfo> update(Long id, ArenaInfoDto arenaInfoDto) {
        ArenaInfo arenaInfo = arenaInfoRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("Not found arena with id: " + id));
        Address address = addressService.getById(arenaInfo.getAddress().getId()).orElseThrow(
                () -> new ResourceNotFoundException("Not found address with id: " + arenaInfo.getAddress().getId())
        );
        try {
            arenaInfo.setUpdatedAt(Instant.now());
            arenaInfo.setPhone(arenaInfoDto.getPhone());
            arenaInfo.setPrice(arenaInfoDto.getPrice());
            arenaInfo.setWorkedFrom(arenaInfoDto.getWorkedFrom());
            arenaInfo.setWorkedTo(arenaInfoDto.getWorkedTo());
            if (ConvertEntityToDto.addressToDto(address) != arenaInfoDto.getAddress())
                address = addressService.update(address.getId(), arenaInfoDto.getAddress()).orElseThrow();
            arenaInfo.setAddress(address);
            arenaInfoRepository.save(arenaInfo);
            return Optional.of(arenaInfo);
        }
        catch (Exception e){
            throw new ResourceNotFoundException("Invalid input data");
        }
    }
}
