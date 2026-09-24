package org.nova.service.notification;

public interface CredentialNotificationService {

    void notifyCredentials(String email, String username, String temporaryPassword);
}
