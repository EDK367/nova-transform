package org.nova.service;

import io.quarkus.elytron.security.common.BcryptUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.nova.domain.model.user.Role;
import org.nova.domain.model.user.User;
import org.nova.exception.AuthenticationException;
import org.nova.repository.UserRepository;
import org.nova.service.auth.JwtTokenGenerator;
import org.nova.service.impl.auth.AuthServiceImpl;
import org.nova.web.dto.request.auth.LoginRequest;
import org.nova.web.dto.response.auth.AuthResponse;
import org.nova.web.dto.response.user.UserResponse;
import org.nova.web.mapper.UserMapper;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceUnitTest {

    @Mock
    private UserRepository repository;

    @Mock
    private UserMapper mapper;

    @Mock
    private JwtTokenGenerator tokenGenerator;

    @InjectMocks
    private AuthServiceImpl authService;

    private User sampleUser;
    private final String rawPassword = "SecurePassword123";

    @BeforeEach
    void setUp() {
        Role role = new Role();
        role.setId(1L);
        role.setName("CLIENTE");

        sampleUser = new User();
        sampleUser.setId(UUID.randomUUID());
        sampleUser.setUsername("testuser");
        sampleUser.setEmail("testuser@example.com");
        sampleUser.setPassword(BcryptUtil.bcryptHash(rawPassword));
        sampleUser.setActive(true);
        sampleUser.setRole(role);
    }

    @Test
    void login_WithValidCredentials_ReturnsAuthResponse() {
        LoginRequest request = new LoginRequest("testuser", rawPassword);
        UserResponse userResponse = new UserResponse();
        userResponse.setUsername("testuser");
        userResponse.setEmail("testuser@example.com");

        when(repository.findByUsernameOrEmail("testuser")).thenReturn(Optional.of(sampleUser));
        when(tokenGenerator.generate(sampleUser)).thenReturn("mocked.jwt.token");
        when(mapper.toResponse(sampleUser)).thenReturn(userResponse);

        AuthResponse response = authService.login(request);

        assertNotNull(response);
        assertEquals("mocked.jwt.token", response.getAccessToken());
        assertEquals("Bearer", response.getTokenType());
        assertEquals("testuser", response.getUser().getUsername());
        verify(tokenGenerator, times(1)).generate(sampleUser);
    }

    @Test
    void login_WithNonExistentUser_ThrowsAuthenticationException() {
        LoginRequest request = new LoginRequest("unknownuser", rawPassword);
        when(repository.findByUsernameOrEmail("unknownuser")).thenReturn(Optional.empty());

        assertThrows(AuthenticationException.class, () -> authService.login(request));
        verify(tokenGenerator, never()).generate(any());
    }

    @Test
    void login_WithInactiveUser_ThrowsAuthenticationException() {
        sampleUser.setActive(false);
        LoginRequest request = new LoginRequest("testuser", rawPassword);
        when(repository.findByUsernameOrEmail("testuser")).thenReturn(Optional.of(sampleUser));

        assertThrows(AuthenticationException.class, () -> authService.login(request));
        verify(tokenGenerator, never()).generate(any());
    }

    @Test
    void login_WithInvalidPassword_ThrowsAuthenticationException() {
        LoginRequest request = new LoginRequest("testuser", "WrongPassword999");
        when(repository.findByUsernameOrEmail("testuser")).thenReturn(Optional.of(sampleUser));

        assertThrows(AuthenticationException.class, () -> authService.login(request));
        verify(tokenGenerator, never()).generate(any());
    }

    @Test
    void login_WithWhitespaceIdentifier_TrimsAndFindsUser() {
        LoginRequest request = new LoginRequest("  testuser  ", rawPassword);
        UserResponse userResponse = new UserResponse();
        userResponse.setUsername("testuser");

        when(repository.findByUsernameOrEmail("testuser")).thenReturn(Optional.of(sampleUser));
        when(tokenGenerator.generate(sampleUser)).thenReturn("mocked.jwt.token");
        when(mapper.toResponse(sampleUser)).thenReturn(userResponse);

        AuthResponse response = authService.login(request);

        assertNotNull(response);
        verify(repository, times(1)).findByUsernameOrEmail("testuser");
    }
}
