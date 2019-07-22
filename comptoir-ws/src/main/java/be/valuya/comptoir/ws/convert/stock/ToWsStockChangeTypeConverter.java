package be.valuya.comptoir.ws.convert.stock;

import be.valuya.comptoir.model.stock.StockChangeType;
import be.valuya.comptoir.ws.rest.api.domain.stock.WsStockChangeType;

import javax.enterprise.context.ApplicationScoped;
import java.util.Objects;

@ApplicationScoped
public class ToWsStockChangeTypeConverter {

    public WsStockChangeType toWsStockChangeType(StockChangeType stockChangeType) {
        switch (stockChangeType) {

            case INITIAL:
                return WsStockChangeType.INITIAL;
            case SALE:
                return WsStockChangeType.SALE;
            case TRANSFER:
                return WsStockChangeType.TRANSFER;
            case ADJUSTMENT:
                return WsStockChangeType.ADJUSTMENT;
            default:
                throw new IllegalArgumentException(Objects.toString(stockChangeType));
        }
    }
}
