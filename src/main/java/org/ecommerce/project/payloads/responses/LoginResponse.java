package org.ecommerce.project.payloads.responses;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class LoginResponse {
    private Long id;
    private String username;
    private List<String> roles;
    private String jwtToken;
}
