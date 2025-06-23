package org.ecommerce.project.controllers;

import jakarta.validation.Valid;
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
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

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

        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authentication);
        SecurityContextHolder.setContext(context);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        ResponseCookie jwtCookie = jwtUtils.generateJwtCookie(userDetails);

        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        LoginResponse response = new LoginResponse(userDetails.getId(), userDetails.getUsername(), roles);

        return ResponseEntity.status(HttpStatus.OK).header(HttpHeaders.SET_COOKIE, jwtCookie.toString()).body(response);
    }

    @GetMapping("/me/username")
    public String currentUsername(Authentication authentication) {
        return authentication != null ? authentication.getName() : "NULL";
    }

    @GetMapping("/me/user")
    public ResponseEntity<LoginResponse> currentUserDetails(Authentication authentication) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        LoginResponse response = new LoginResponse(userDetails.getId(), userDetails.getUsername(), roles);

        return ResponseEntity.status(HttpStatus.OK).body(response);
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
//        public Set<Role> mapRolesOptimized(Set<String> strRoles) {
//    if (strRoles == null || strRoles.isEmpty()) {
//        // Lógica para role padrão continua a mesma
//        return Set.of(roleRepository.findByRoleName(RoleName.ROLE_USER)
//                .orElseThrow(() -> new ResourceNotFoundException("Role", "name", RoleName.ROLE_USER.name())));
//    }
//
//    // 1. Converte a lista de Strings para uma lista de Enums
//    Set<RoleName> roleNames = strRoles.stream()
//            .map(roleStr -> RoleName.valueOf("ROLE_" + roleStr.toUpperCase()))
//            .collect(Collectors.toSet());
//
//    // 2. Faz UMA ÚNICA consulta ao banco para buscar todas as roles
//    Set<Role> foundRoles = roleRepository.findAllByRoleNameIn(roleNames);
//
//    // 3. (Opcional, mas recomendado) Verifica se todas as roles pedidas foram encontradas
//    if (foundRoles.size() != roleNames.size()) {
//        // Lançar uma exceção aqui se alguma role não foi encontrada no banco
//        throw new ResourceNotFoundException("Role", "name", "Uma ou mais roles especificadas não foram encontradas no banco de dados.");
//    }
//
//    return foundRoles;
//    }

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
