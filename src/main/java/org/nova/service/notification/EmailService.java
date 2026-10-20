package org.nova.service.notification;

public interface EmailService {

    boolean sendEmail(String to, String subject, String htmlContent);

    boolean sendEmailWithAttachment(String to, String subject, String htmlContent, String filename, byte[] attachmentBytes);

    void sendCredentials(String email, String username, String temporaryPassword);

    void sendMemberCarnet(String email, String username, String fullName, byte[] pdfBytes);

    void sendInvoiceReceipt(String email, String customerName, Long invoiceId, String amount, String planName);

    void sendMembershipNotification(String email, String customerName, String planName, String startDate, String dueDate, String status);
}
