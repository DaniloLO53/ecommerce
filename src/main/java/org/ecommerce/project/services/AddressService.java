package org.ecommerce.project.services;

import org.ecommerce.project.payloads.DTOs.AddressDTO;
import org.springframework.stereotype.Service;

@Service
public interface AddressService {
    AddressDTO addAddress(Long userId, AddressDTO addressDTO);
}
