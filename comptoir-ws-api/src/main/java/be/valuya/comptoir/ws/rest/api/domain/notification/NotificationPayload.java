package be.valuya.comptoir.ws.rest.api.domain.notification;


import javax.annotation.Nullable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement()
@XmlAccessorType(XmlAccessType.FIELD)
public class NotificationPayload {

    @Nullable
    private NotificationOptions notification;

    @Nullable
    public NotificationOptions getNotification() {
        return notification;
    }

    public void setNotification(@Nullable NotificationOptions notification) {
        this.notification = notification;
    }

}
