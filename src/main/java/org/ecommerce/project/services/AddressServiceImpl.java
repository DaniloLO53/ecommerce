package org.ecommerce.project.services;

import jakarta.transaction.Transactional;
import org.ecommerce.project.exceptions.APIForbiddenException;
import org.ecommerce.project.exceptions.ResourceNotFoundException;
import org.ecommerce.project.models.Address;
import org.ecommerce.project.models.User;
import org.ecommerce.project.payloads.DTOs.AddressDTO;
import org.ecommerce.project.repositories.AddressRepository;
import org.ecommerce.project.repositories.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AddressServiceImpl implements AddressService {
    private final AddressRepository addressRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    public AddressServiceImpl(AddressRepository addressRepository, UserRepository userRepository, ModelMapper modelMapper) {
        this.addressRepository = addressRepository;
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
            address.setUser(user);

            userRepository.save(user); // we save user instead of address because we defined user as the relationship owner at User model

            return modelMapper.map(user.getAddresses().getLast(), AddressDTO.class);
        }

        throw new ResourceNotFoundException("User", "id", userId);
    }

    @Override
    public AddressDTO getAddressById(Long addressId) {
        Optional<Address> optionalAddress = addressRepository.findById(addressId);

        if (optionalAddress.isPresent()) {
            return modelMapper.map(optionalAddress.get(), AddressDTO.class);
        }

        throw new ResourceNotFoundException("Address", "id", addressId);
     }

    @Override
    public List<AddressDTO> getAddressesByUser(Long userId) {
        List<Address> addresses = addressRepository.findAllByUser_Id(userId);

        return addresses.
                stream()
                .map(address -> modelMapper.map(address, AddressDTO.class))
                .toList();
    }

    @Override
    @Transactional
    public AddressDTO updateAddress(Long userId, Long addressId, AddressDTO addressDTO) {
        Address address = addressRepository
                .findById(addressId)
                .orElseThrow(() -> new ResourceNotFoundException("Address", "id", addressId));

        if (!address.getUser().getId().equals(userId)) {
            throw new APIForbiddenException("User should only change own content");
        }

        address.setCountry(addressDTO.getCountry());
        address.setState(addressDTO.getState());
        address.setCity(addressDTO.getCity());
        address.setZipcode(addressDTO.getZipcode());
        address.setStreet(addressDTO.getStreet());
        address.setBuilding(addressDTO.getBuilding());

        Address savedAddress = addressRepository.save(address);
        return modelMapper.map(savedAddress, AddressDTO.class);
    }

    @Override
    public List<AddressDTO> getAllAddresses() {
        List<Address> allAddresses = addressRepository.findAll();

        return allAddresses
                .stream()
                .map(address -> modelMapper.map(address, AddressDTO.class))
                .toList();
    }
}
