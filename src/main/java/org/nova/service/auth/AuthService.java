package org.nova.service.auth;

import org.nova.web.dto.request.auth.LoginRequest;
import org.nova.web.dto.response.auth.AuthResponse;

public interface AuthService {

    AuthResponse login(LoginRequest request);
}
