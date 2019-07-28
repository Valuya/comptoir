package be.valuya.comptoir.ws.config;

import org.eclipse.microprofile.config.inject.ConfigProperty;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.Optional;

@ApplicationScoped
public class WebNotificationsConfigProvider {

    @Inject
    @ConfigProperty(name = "be.valuya.comptoir.webNotifications.subject", defaultValue = "comptoir")
    private String subject;
    @Inject
    @ConfigProperty(name = "be.valuya.comptoir.webNotifications.privateKey")
    private Optional<String> privateKey;
    @Inject
    @ConfigProperty(name = "be.valuya.comptoir.webNotifications.publicKey")
    private Optional<String> publicKey;

    public String getSubject() {
        return subject;
    }

    public Optional<String> getPrivateKey() {
        return privateKey;
    }

    public Optional<String> getPublicKey() {
        return publicKey;
    }
}
