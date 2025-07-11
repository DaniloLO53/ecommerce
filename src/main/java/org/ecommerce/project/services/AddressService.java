package org.ecommerce.project.services;

import org.ecommerce.project.payloads.DTOs.AddressDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface AddressService {
    AddressDTO addAddress(Long userId, AddressDTO addressDTO);
    List<AddressDTO> getAllAddresses();
    AddressDTO getAddressById(Long addressId);
    List<AddressDTO> getAddressesByUser(Long userId);
    AddressDTO updateAddress(Long userId, Long addressId, AddressDTO addressDTO);
    String deleteUserAddress(Long userId, Long addressId);
}
