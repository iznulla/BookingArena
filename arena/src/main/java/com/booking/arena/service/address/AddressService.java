package com.booking.arena.service.address;



import com.booking.arena.dto.address.AddressDto;
import com.booking.arena.entity.address.Address;

import java.util.Optional;

public interface AddressService {
    Optional<Address> create(AddressDto addressDto);
    Optional<Address> update(Long id, AddressDto addressDto);
    Optional<Address> getById(Long id);
    void delete(Long id);
}
