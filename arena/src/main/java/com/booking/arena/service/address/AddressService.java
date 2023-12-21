package com.booking.arena.service.address;



import com.booking.arena.dto.address.AddressDto;
import com.booking.arena.entity.address.Address;

import java.util.Optional;

public interface AddressService {
    Optional<Address> createAddress(AddressDto addressDto);
    Optional<Address> updateAddress(Long id, AddressDto addressDto);
    Optional<Address> getAddressById(Long id);
    void deleteAddressById(Long id);
}
