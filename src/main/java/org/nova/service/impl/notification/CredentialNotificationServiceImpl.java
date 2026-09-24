package org.nova.service.impl.notification;

import jakarta.enterprise.context.ApplicationScoped;
import org.nova.service.notification.CredentialNotificationService;

@ApplicationScoped
public class CredentialNotificationServiceImpl implements CredentialNotificationService {

    @Override
    public void notifyCredentials(String email, String username, String temporaryPassword) {
    }
}
