package org.nova.service.user;

import org.nova.common.PageResponse;
import org.nova.domain.model.user.User;
import org.nova.web.dto.request.user.CreateUserRequest;
import org.nova.web.dto.request.user.UpdateUserRequest;
import org.nova.web.dto.response.user.CreateUserResponse;
import org.nova.web.dto.response.user.UserResponse;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserService {

    List<User> findAll();

    PageResponse<UserResponse> findPaged(int page, int size);

    PageResponse<UserResponse> findPaged(int page, int size, String sortBy, String sortDirection);

    Optional<User> findById(UUID id);

    User create(User user);

    CreateUserResponse createInternalUser(CreateUserRequest request);

    User update(UUID id, User user);

    User update(UUID id, UpdateUserRequest request);

    void delete(UUID id);
}