package org.ecommerce.project.services;

import jakarta.transaction.Transactional;
import org.ecommerce.project.exceptions.ResourceNotFoundException;
import org.ecommerce.project.models.Address;
import org.ecommerce.project.models.User;
import org.ecommerce.project.payloads.DTOs.AddressDTO;
import org.ecommerce.project.repositories.AddressRepository;
import org.ecommerce.project.repositories.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AddressServiceImpl implements AddressService {
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    public AddressServiceImpl(UserRepository userRepository, ModelMapper modelMapper) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    @Transactional
    public AddressDTO addAddress(Long userId, AddressDTO addressDTO) {
        Address address = modelMapper.map(addressDTO, Address.class);
        Optional<User> optionalUser = userRepository.findById(userId);

        if (optionalUser.isPresent()) {
            User user = optionalUser.get();

            user.getAddresses().add(address);
            address.getUsers().add(user);

            userRepository.save(user); // we save user instead of address because we defined user as the relationship owner at User model

            return modelMapper.map(user.getAddresses().getLast(), AddressDTO.class);
        }

        throw new ResourceNotFoundException("User", "id", userId);
    }
}
