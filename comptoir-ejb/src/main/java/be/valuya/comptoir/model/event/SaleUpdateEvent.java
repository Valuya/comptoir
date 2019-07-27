package be.valuya.comptoir.model.event;

import be.valuya.comptoir.model.commercial.Sale;

public class SaleUpdateEvent {

    private Sale sale;

    public SaleUpdateEvent(Sale sale) {
        this.sale = sale;
    }

    public Sale getSale() {
        return sale;
    }

    public void setSale(Sale sale) {
        this.sale = sale;
    }
}
