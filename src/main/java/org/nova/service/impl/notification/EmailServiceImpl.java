package org.nova.service.impl.notification;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.jboss.logging.Logger;
import org.nova.service.notification.EmailService;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.List;
import java.util.Map;

@ApplicationScoped
public class EmailServiceImpl implements EmailService {

    private static final Logger LOG = Logger.getLogger(EmailServiceImpl.class);

    private final String apiKey;
    private final String fromEmail;
    private final ObjectMapper objectMapper;
    private final HttpClient httpClient;

    @Inject
    public EmailServiceImpl(
            @ConfigProperty(name = "resend.api-key", defaultValue = "") String apiKey,
            @ConfigProperty(name = "resend.from", defaultValue = "Nova Transform <soporte@solairy.app>") String fromEmail,
            ObjectMapper objectMapper
    ) {
        this.apiKey = apiKey != null ? apiKey.trim() : "";
        this.fromEmail = fromEmail != null ? fromEmail.trim() : "Nova Transform <soporte@solairy.app>";
        this.objectMapper = objectMapper;
        this.httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(10))
                .build();
    }

    @Override
    public boolean sendEmail(String to, String subject, String htmlContent) {
        if (apiKey.isBlank()) {
            LOG.warnf("Resend API Key no configurada. Omitiendo envio a: %s", to);
            return false;
        }

        try {
            Map<String, Object> payload = Map.of(
                    "from", fromEmail,
                    "to", List.of(to),
                    "subject", subject,
                    "html", htmlContent
            );

            String requestBody = objectMapper.writeValueAsString(payload);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://api.resend.com/emails"))
                    .header("Authorization", "Bearer " + apiKey)
                    .header("Content-Type", "application/json")
                    .timeout(Duration.ofSeconds(15))
                    .POST(HttpRequest.BodyPublishers.ofString(requestBody, StandardCharsets.UTF_8))
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() >= 200 && response.statusCode() < 300) {
                LOG.infof("Correo enviado exitosamente via Resend a: %s", to);
                return true;
            } else {
                LOG.errorf("Error al enviar correo via Resend a %s. Status: %d, Response: %s",
                        to, response.statusCode(), response.body());
                return false;
            }
        } catch (Exception e) {
            LOG.errorf("Excepcion al enviar correo via Resend a %s: %s", to, e.getMessage());
            return false;
        }
    }

    @Override
    public boolean sendEmailWithAttachment(String to, String subject, String htmlContent, String filename, byte[] attachmentBytes) {
        if (apiKey.isBlank()) {
            LOG.warnf("Resend API Key no configurada. Omitiendo envio a: %s", to);
            return false;
        }

        try {
            String base64Content = java.util.Base64.getEncoder().encodeToString(attachmentBytes);
            Map<String, Object> attachment = Map.of(
                    "filename", filename,
                    "content", base64Content
            );

            Map<String, Object> payload = Map.of(
                    "from", fromEmail,
                    "to", List.of(to),
                    "subject", subject,
                    "html", htmlContent,
                    "attachments", List.of(attachment)
            );

            String requestBody = objectMapper.writeValueAsString(payload);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://api.resend.com/emails"))
                    .header("Authorization", "Bearer " + apiKey)
                    .header("Content-Type", "application/json")
                    .timeout(Duration.ofSeconds(20))
                    .POST(HttpRequest.BodyPublishers.ofString(requestBody, StandardCharsets.UTF_8))
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() >= 200 && response.statusCode() < 300) {
                LOG.infof("Correo con adjunto enviado exitosamente via Resend a: %s", to);
                return true;
            } else {
                LOG.errorf("Error al enviar correo con adjunto via Resend a %s. Status: %d, Response: %s",
                        to, response.statusCode(), response.body());
                return false;
            }
        } catch (Exception e) {
            LOG.errorf("Excepcion al enviar correo con adjunto via Resend a %s: %s", to, e.getMessage());
            return false;
        }
    }

    @Override
    public void sendCredentials(String email, String username, String temporaryPassword) {
        String html = String.format("""
            <div style="font-family: Arial, sans-serif; max-width: 600px; margin: 0 auto; padding: 24px; border: 1px solid #e2e8f0; border-radius: 8px;">
                <h2 style="color: #0f172a; text-align: center;">Bienvenido a Nova Transform</h2>
                <p style="font-size: 15px; color: #334155;">Hola <strong>%s</strong>,</p>
                <p style="font-size: 14px; color: #475569;">Tu cuenta ha sido creada exitosamente. A continuacion encuentras tus credenciales de acceso:</p>
                <div style="background-color: #f8fafc; padding: 16px; border-radius: 6px; margin: 20px 0; border-left: 4px solid #2563eb;">
                    <p style="margin: 6px 0; font-size: 14px;"><strong>Nombre de Usuario:</strong> <span style="font-family: monospace; font-size: 15px; color: #0284c7; font-weight: bold;">@%s</span></p>
                    <p style="margin: 6px 0; font-size: 14px;"><strong>Correo Electronico:</strong> %s</p>
                    <p style="margin: 6px 0; font-size: 14px;"><strong>Contrasena temporal:</strong> <code style="background: #e2e8f0; padding: 4px 8px; border-radius: 4px; font-weight: bold;">%s</code></p>
                </div>
                <p style="font-size: 13px; color: #475569;"><em>Nota: Puedes iniciar sesion ingresando tanto tu nombre de usuario (@%s) como tu correo electronico (%s).</em></p>
                <p style="font-size: 13px; color: #64748b;">Te sugerimos cambiar tu contrasena al iniciar sesion por primera vez.</p>
                <hr style="border: none; border-top: 1px solid #e2e8f0; margin: 24px 0;" />
                <p style="font-size: 12px; color: #94a3b8; text-align: center;">Nova Transform &copy; 2026. Todos los derechos reservados.</p>
            </div>
            """, username, username, email, temporaryPassword, username, email);

        sendEmail(email, "Tus credenciales de acceso - Nova Transform", html);
    }

    @Override
    public void sendMemberCarnet(String email, String username, String fullName, byte[] pdfBytes) {
        String html = String.format("""
            <div style="font-family: Arial, sans-serif; max-width: 600px; margin: 0 auto; padding: 24px; border: 1px solid #e2e8f0; border-radius: 8px;">
                <h2 style="color: #0f172a; text-align: center;">Carnet Digital de Acceso - Nova Transform</h2>
                <p style="font-size: 15px; color: #334155;">Hola <strong>%s</strong>,</p>
                <p style="font-size: 14px; color: #475569;">Adjunto a este correo encuentras tu carnet digital oficial con codigo QR para el control de acceso en nuestras sedes.</p>
                <div style="background-color: #f8fafc; padding: 16px; border-radius: 6px; margin: 20px 0; border-left: 4px solid #10b981;">
                    <p style="margin: 6px 0; font-size: 14px;"><strong>Socio:</strong> %s</p>
                    <p style="margin: 6px 0; font-size: 14px;"><strong>Nombre de Usuario:</strong> <span style="font-family: monospace; font-size: 15px; color: #0284c7; font-weight: bold;">@%s</span></p>
                    <p style="margin: 6px 0; font-size: 14px;"><strong>Correo Electronico:</strong> %s</p>
                </div>
                <p style="font-size: 13px; color: #64748b;">Recuerda que tambien puedes presentar tu codigo QR directamente desde tu dispositivo movil.</p>
                <hr style="border: none; border-top: 1px solid #e2e8f0; margin: 24px 0;" />
                <p style="font-size: 12px; color: #94a3b8; text-align: center;">Nova Transform &copy; 2026. Todos los derechos reservados.</p>
            </div>
            """, fullName, fullName, username, email);

        sendEmailWithAttachment(email, "Tu Carnet Digital con Codigo QR - Nova Transform", html, "carnet_" + username + ".pdf", pdfBytes);
    }

    @Override
    public void sendInvoiceReceipt(String email, String customerName, Long invoiceId, String amount, String planName) {
        String html = String.format("""
            <div style="font-family: Arial, sans-serif; max-width: 600px; margin: 0 auto; padding: 24px; border: 1px solid #e2e8f0; border-radius: 8px;">
                <h2 style="color: #0f172a; text-align: center;">Comprobante de Pago #%06d</h2>
                <p style="font-size: 15px; color: #334155;">Estimado/a <strong>%s</strong>,</p>
                <p style="font-size: 14px; color: #475569;">Hemos registrado exitosamente tu pago. A continuacion el resumen del movimiento:</p>
                <div style="background-color: #f8fafc; padding: 16px; border-radius: 6px; margin: 20px 0; border-left: 4px solid #16a34a;">
                    <p style="margin: 6px 0; font-size: 14px;"><strong>Factura ID:</strong> #%06d</p>
                    <p style="margin: 6px 0; font-size: 14px;"><strong>Concepto / Plan:</strong> %s</p>
                    <p style="margin: 6px 0; font-size: 14px;"><strong>Monto pagado:</strong> %s</p>
                    <p style="margin: 6px 0; font-size: 14px;"><strong>Estado:</strong> PAGADO</p>
                </div>
                <p style="font-size: 13px; color: #64748b;">Puedes descargar tu comprobante oficial en formato PDF desde la aplicacion.</p>
                <hr style="border: none; border-top: 1px solid #e2e8f0; margin: 24px 0;" />
                <p style="font-size: 12px; color: #94a3b8; text-align: center;">Nova Transform &copy; 2026</p>
            </div>
            """, invoiceId != null ? invoiceId : 0L, customerName, invoiceId != null ? invoiceId : 0L, planName, amount);

        sendEmail(email, "Comprobante de Pago #" + invoiceId + " - Nova Transform", html);
    }

    @Override
    public void sendMembershipNotification(String email, String customerName, String planName, String startDate, String dueDate, String status) {
        String html = String.format("""
            <div style="font-family: Arial, sans-serif; max-width: 600px; margin: 0 auto; padding: 24px; border: 1px solid #e2e8f0; border-radius: 8px;">
                <h2 style="color: #0f172a; text-align: center;">Actualizacion de Membresia</h2>
                <p style="font-size: 15px; color: #334155;">Hola <strong>%s</strong>,</p>
                <p style="font-size: 14px; color: #475569;">Te informamos sobre el estado actual de tu membresia en Nova Transform:</p>
                <div style="background-color: #f8fafc; padding: 16px; border-radius: 6px; margin: 20px 0; border-left: 4px solid #3b82f6;">
                    <p style="margin: 6px 0; font-size: 14px;"><strong>Plan:</strong> %s</p>
                    <p style="margin: 6px 0; font-size: 14px;"><strong>Estado:</strong> %s</p>
                    <p style="margin: 6px 0; font-size: 14px;"><strong>Fecha inicio:</strong> %s</p>
                    <p style="margin: 6px 0; font-size: 14px;"><strong>Fecha vencimiento:</strong> %s</p>
                </div>
                <hr style="border: none; border-top: 1px solid #e2e8f0; margin: 24px 0;" />
                <p style="font-size: 12px; color: #94a3b8; text-align: center;">Nova Transform &copy; 2026</p>
            </div>
            """, customerName, planName, status, startDate, dueDate);

        sendEmail(email, "Estado de Membresia: " + status + " - Nova Transform", html);
    }
}
