package be.valuya.comptoir.ws.rest.api.domain.event;

public enum WsComptoirEvent {

    SALE_UPDATE(WsSaleUpdateEvent.class),
    SALE_ITEMS(WsSaleItemsUpdateEvent.class),
    SALE_PAYMENT_ENTRIES(WsSalePaymentEntriesUpdateEvent.class),
    ;
    private Class<? extends WsComptoirServerEvent> eventClass;

    WsComptoirEvent(Class<? extends WsComptoirServerEvent> eventClass) {

        this.eventClass = eventClass;
    }

    public Class<? extends WsComptoirServerEvent> getEventClass() {
        return eventClass;
    }
}
