package be.valuya.comptoir.ws.rest.api.domain.event;

public enum WsComptoirEvent {

    SALE_UPDATE("sale-update", WsSaleUpdateEvent.class),
    SALE_ITEMS("sale-items", WsSaleItemsUpdateEvent.class),
    ;
    private Class<? extends WsComptoirServerEvent> eventClass;

    WsComptoirEvent(String name, Class<? extends WsComptoirServerEvent> eventClass) {

        this.eventClass = eventClass;
    }

    public Class<? extends WsComptoirServerEvent> getEventClass() {
        return eventClass;
    }
}
