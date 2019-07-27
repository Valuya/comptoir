package be.valuya.comptoir.model.event;

public class SaleRemovedEvent {

    private Long saleId;

    public SaleRemovedEvent(Long saleId) {
        this.saleId = saleId;
    }

    public Long getSaleId() {
        return saleId;
    }

    public void setSaleId(Long saleId) {
        this.saleId = saleId;
    }
}
