package be.valuya.comptoir.ws.rest.api.domain.event;

import org.eclipse.microprofile.openapi.annotations.media.Schema;

import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "ComptoirServerEvent")
@XmlAccessorType(XmlAccessType.FIELD)
@Schema(description = "A generic event", name = "WsComptoirServerEvent")
public abstract class WsComptoirServerEvent {

    @NotNull
    private WsComptoirEvent eventType;

    public WsComptoirServerEvent() {
    }

    protected WsComptoirServerEvent(@NotNull WsComptoirEvent eventType) {
        this.eventType = eventType;
    }

    public WsComptoirEvent getEventType() {
        return eventType;
    }

    public void setEventType(WsComptoirEvent eventType) {
        this.eventType = eventType;
    }
}
