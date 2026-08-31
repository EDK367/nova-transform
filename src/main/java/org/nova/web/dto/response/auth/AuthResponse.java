package org.nova.web.dto.response.auth;

import lombok.*;
import org.nova.web.dto.response.user.UserResponse;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthResponse {

    private String accessToken;
    private String tokenType;
    private Long expiresIn;
    private UserResponse user;
}
