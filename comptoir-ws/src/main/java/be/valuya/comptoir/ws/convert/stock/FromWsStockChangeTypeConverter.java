package be.valuya.comptoir.ws.convert.stock;

import be.valuya.comptoir.model.stock.StockChangeType;
import be.valuya.comptoir.ws.rest.api.domain.stock.WsStockChangeType;

import javax.enterprise.context.ApplicationScoped;
import java.util.Objects;

@ApplicationScoped
public class FromWsStockChangeTypeConverter {

    public StockChangeType fromWsStockChangeType(WsStockChangeType stockChangeType) {
        switch (stockChangeType) {

            case INITIAL:
                return StockChangeType.INITIAL;
            case SALE:
                return StockChangeType.SALE;
            case TRANSFER:
                return StockChangeType.TRANSFER;
            case ADJUSTMENT:
                return StockChangeType.ADJUSTMENT;
            default:
                throw new IllegalArgumentException(Objects.toString(stockChangeType));
        }
    }
}
