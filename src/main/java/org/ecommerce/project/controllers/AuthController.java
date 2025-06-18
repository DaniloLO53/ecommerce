package org.ecommerce.project.controllers;

import jakarta.validation.Valid;
import org.ecommerce.project.exceptions.APIBadRequestException;
import org.ecommerce.project.exceptions.APIConflictException;
import org.ecommerce.project.exceptions.ResourceNotFoundException;
import org.ecommerce.project.models.Role;
import org.ecommerce.project.models.RoleName;
import org.ecommerce.project.models.User;
import org.ecommerce.project.payloads.responses.APIResponse;
import org.ecommerce.project.payloads.responses.LoginRequest;
import org.ecommerce.project.payloads.responses.LoginResponse;
import org.ecommerce.project.payloads.responses.SignupRequest;
import org.ecommerce.project.repositories.RoleRepository;
import org.ecommerce.project.repositories.UserRepository;
import org.ecommerce.project.security.jwt.JwtUtils;
import org.ecommerce.project.security.services.UserDetailsImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final JwtUtils jwtUtils;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    public AuthController(JwtUtils jwtUtils,
                          PasswordEncoder passwordEncoder,
                          AuthenticationManager authenticationManager,
                          UserRepository userRepository,
                          RoleRepository roleRepository) {
        this.jwtUtils = jwtUtils;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest) {
        Authentication authentication;
        try {
            authentication = authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
        } catch (AuthenticationException exception) {
            Map<String, Object> map = new HashMap<>();
            map.put("message", "Bad credentials");
            map.put("status", false);
            return new ResponseEntity<Object>(map, HttpStatus.NOT_FOUND);
        }

        SecurityContextHolder.getContext().setAuthentication(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        String jwtToken = jwtUtils.generateTokenFromUserName(userDetails);

        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        LoginResponse response = new LoginResponse(userDetails.getId(), userDetails.getUsername(), roles, jwtToken);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@RequestBody @Valid SignupRequest signupRequest) {
        String username = signupRequest.getUsername();
        String email = signupRequest.getEmail();
        String password = signupRequest.getPassword();

        Set<String> strRoles = signupRequest.getRoles();
        Set<Role> roles = new HashSet<>();


        Boolean usernameAlreadyExists = userRepository.existsByUserName(signupRequest.getUsername());
        Boolean emailAlreadyExists = userRepository.existsByEmail(signupRequest.getEmail());

        if (usernameAlreadyExists || emailAlreadyExists) {
            throw new APIConflictException("Username or email already exists");
        }

        // TODO: optimize to make only one request to db and change switch-case to a streams solution (convert strings set to enums set)
        if (strRoles == null || strRoles.isEmpty()) {
            Role userRole = roleRepository
                    .findByRoleName(RoleName.ROLE_USER)
                    .orElseThrow(() -> new ResourceNotFoundException("Role", "name", RoleName.ROLE_USER.name()));
            roles.add(userRole);
        } else {
            strRoles.forEach(role -> {
                switch (role) {
                    case "admin":
                        Role dbRoleAdmin = roleRepository
                                .findByRoleName(RoleName.ROLE_ADMIN)
                                .orElseThrow(() -> new ResourceNotFoundException("Role", "name", RoleName.ROLE_USER.name()));

                        roles.add(dbRoleAdmin);
                        break;
                    case "seller":
                        Role dbRoleSeller = roleRepository
                                .findByRoleName(RoleName.ROLE_SELLER)
                                .orElseThrow(() -> new ResourceNotFoundException("Role", "name", RoleName.ROLE_SELLER.name()));

                        roles.add(dbRoleSeller);
                        break;
                    default:
                        Role dbRoleUser = roleRepository
                                .findByRoleName(RoleName.ROLE_USER)
                                .orElseThrow(() -> new ResourceNotFoundException("Role", "name", RoleName.ROLE_USER.name()));

                        roles.add(dbRoleUser);
                }
            });
        }

        User user = new User(username, email, passwordEncoder.encode(password));
        user.setRoles(roles);
        userRepository.save(user);

        return ResponseEntity.status(HttpStatus.OK).body(new APIResponse("User has been created successfully"));
    }
}
