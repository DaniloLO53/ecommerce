package org.ecommerce.project.controllers;

import org.ecommerce.project.payloads.DTOs.AddressDTO;
import org.ecommerce.project.security.services.UserDetailsImpl;
import org.ecommerce.project.services.AddressService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class AddressController {
    private final AddressService addressService;

    public AddressController(AddressService addressService) {
        this.addressService = addressService;
    }

    @PostMapping("/addresses")
    public ResponseEntity<AddressDTO> addAddress(Authentication authentication, @RequestBody AddressDTO addressDTO) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        Long userId = userDetails.getId();

        return ResponseEntity.status(HttpStatus.CREATED).body(addressService.addAddress(userId, addressDTO));
    }

    @GetMapping("/addresses")
    public ResponseEntity<List<AddressDTO>> getAllAddresses() {
        return ResponseEntity.status(HttpStatus.OK).body(addressService.getAllAddresses());
    }

    @GetMapping("/addresses/{addressId}")
    public ResponseEntity<AddressDTO> getAddressById(@PathVariable Long addressId) {
        return ResponseEntity.status(HttpStatus.OK).body(addressService.getAddressById(addressId));
    }

    @GetMapping("/users/addresses")
    public ResponseEntity<List<AddressDTO>> getAddressesByUser(Authentication authentication) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        Long userId = userDetails.getId();

        return ResponseEntity.status(HttpStatus.OK).body(addressService.getAddressesByUser(userId));
    }
}
