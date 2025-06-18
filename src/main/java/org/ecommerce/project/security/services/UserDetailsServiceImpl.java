package org.ecommerce.project.security.services;

import jakarta.transaction.Transactional;
import org.ecommerce.project.models.User;
import org.ecommerce.project.repositories.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserRepository userRepository;

    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @Transactional // enables rollback (db changes only if operation is successful)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> optionalUser = userRepository.findByUserName(username);

        if (optionalUser.isPresent()) {
            return UserDetailsImpl.build(optionalUser.get());
        }

        throw new UsernameNotFoundException("Username not found");
    }
}
