package org.ecommerce.project.payloads.responses;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
public class LoginResponse {
    private Long id;
    private String username;
    private List<String> roles;
    // we don't need this as we're using cookies based jwt
//    private String jwtToken;

    public LoginResponse(Long id, String username, List<String> roles) {
        this.id = id;
        this.username = username;
        this.roles = roles;
    }
}
