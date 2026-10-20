package org.nova.service.impl.notification;

import jakarta.enterprise.context.ApplicationScoped;
import lombok.RequiredArgsConstructor;
import org.nova.service.notification.CredentialNotificationService;
import org.nova.service.notification.EmailService;

@ApplicationScoped
@RequiredArgsConstructor
public class CredentialNotificationServiceImpl implements CredentialNotificationService {

    private final EmailService emailService;

    @Override
    public void notifyCredentials(String email, String username, String temporaryPassword) {
        emailService.sendCredentials(email, username, temporaryPassword);
    }
}
