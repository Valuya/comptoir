package be.valuya.comptoir.ws.rest.control;


import be.valuya.comptoir.ws.config.WebNotificationsConfigProvider;
import be.valuya.comptoir.ws.rest.api.WebNotificationApi;
import be.valuya.comptoir.ws.rest.api.domain.event.WsSaleUpdateEvent;
import be.valuya.comptoir.ws.rest.api.domain.notification.NotificationPayload;
import be.valuya.comptoir.ws.rest.api.domain.notification.NotificationOptions;
import be.valuya.comptoir.ws.rest.api.domain.notification.WebNotificationSubscriptionRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import nl.martijndwars.webpush.Notification;
import nl.martijndwars.webpush.PushService;
import org.apache.http.HttpResponse;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.jose4j.lang.JoseException;

import javax.annotation.PostConstruct;
import javax.annotation.security.PermitAll;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.InternalServerErrorException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.security.GeneralSecurityException;
import java.security.Security;
import java.util.concurrent.ExecutionException;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@ApplicationScoped
@PermitAll
public class WebNotificationsResource implements WebNotificationApi {

    @Inject
    private ObjectMapper mapper;
    @Inject
    private WebNotificationsConfigProvider webNotificationsConfigProvider;
    @Inject
    private Logger logger;

    private PushService pushService;

    @PostConstruct
    public void init() {
        Security.addProvider(new BouncyCastleProvider());
        try {
            String privateKey = webNotificationsConfigProvider.getPrivateKey()
                    .orElseThrow(() -> new RuntimeException("No VAPID private key configured"));
            String publicKey = webNotificationsConfigProvider.getPublicKey()
                    .orElseThrow(() -> new RuntimeException("No VAPID public key configured"));
            String subject = webNotificationsConfigProvider.getSubject();
            pushService = new PushService(publicKey, privateKey, subject);
        } catch (GeneralSecurityException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void subscriptionWebNotifications(WebNotificationSubscriptionRequest subscription) {
        String p256dh = subscription.getKeys().get("p256dh");
        String auth = subscription.getKeys().get("auth");

        NotificationOptions notificationOptions = new NotificationOptions();
        notificationOptions.setTitle("Welcome to comptoir");
        notificationOptions.setBody("We welcome you to comptoir");
        notificationOptions.setData("testdata");
        NotificationPayload payload = new NotificationPayload();
        payload.setNotification(notificationOptions);
        payload.setServerEvent(new WsSaleUpdateEvent());
        String jsonPayload = writeJsonString(payload);

        String endpoint = subscription.getEndpoint();
        try {
            Notification testNotification = new Notification(endpoint, p256dh, auth, jsonPayload);
            HttpResponse response = pushService.send(testNotification);

            String reasonPhrase = response.getStatusLine().getReasonPhrase();
            InputStream responseContent = response.getEntity().getContent();
            try (InputStreamReader streamReader = new InputStreamReader(responseContent)) {
                BufferedReader bufferedReader = new BufferedReader(streamReader);
                String responseString = bufferedReader.lines().collect(Collectors.joining("\n"));
                logger.info("Push notification response: " + reasonPhrase + " - " + responseString);
            }
        } catch (ExecutionException | InterruptedException | GeneralSecurityException | JoseException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    private String writeJsonString(Object data) {
        try {
            StringWriter stringWriter = new StringWriter();
            mapper.writeValue(stringWriter, data);
            return stringWriter.toString();
        } catch (IOException e) {
            throw new InternalServerErrorException(e);
        }
    }
}
