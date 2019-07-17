/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package be.valuya.comptoir.ws.convert.stock;

import be.valuya.comptoir.ws.rest.api.domain.commercial.WsItemVariantRef;
import be.valuya.comptoir.ws.rest.api.domain.commercial.WsItemVariantSaleRef;
import be.valuya.comptoir.ws.rest.api.domain.stock.WsItemVariantStock;
import be.valuya.comptoir.ws.rest.api.domain.stock.WsItemVariantStockRef;
import be.valuya.comptoir.ws.rest.api.domain.stock.WsStockChangeType;
import be.valuya.comptoir.ws.rest.api.domain.stock.WsStockRef;
import be.valuya.comptoir.model.commercial.ItemVariant;
import be.valuya.comptoir.model.commercial.ItemVariantSale;
import be.valuya.comptoir.model.stock.ItemStock;
import be.valuya.comptoir.model.stock.Stock;
import be.valuya.comptoir.model.stock.StockChangeType;
import be.valuya.comptoir.ws.convert.commercial.ToWsItemVariantConverter;
import be.valuya.comptoir.ws.convert.commercial.ToWsItemVariantSaleConverter;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.math.BigDecimal;
import java.time.ZonedDateTime;

/**
 * @author cghislai
 */
@ApplicationScoped
public class ToWsItemVariantStockConverter {

    @Inject
    private ToWsItemVariantConverter toWsItemVariantConverter;
    @Inject
    private ToWsItemVariantSaleConverter toWsItemVariantSaleConverter;
    @Inject
    private ToWsStockConverter toWsStockConverter;
    @Inject
    private ToWsStockChangeTypeConverter toWsStockChangeTypeConverter;

    public WsItemVariantStock convert(ItemStock itemStock) {
        Long id = itemStock.getId();
        String comment = itemStock.getComment();
        ZonedDateTime endDateTime = itemStock.getEndDateTime();
        ItemVariant itemVariant = itemStock.getItemVariant();
        BigDecimal quantity = itemStock.getQuantity();
        ZonedDateTime startDateTime = itemStock.getStartDateTime();
        Stock stock = itemStock.getStock();
        ItemStock previousItemStock = itemStock.getPreviousItemStock();
        ItemVariantSale stockChangeSale = itemStock.getStockChangeVariantSale();
        StockChangeType stockChangeType = itemStock.getStockChangeType();
        Integer orderPosition = itemStock.getOrderPosition();

        WsItemVariantRef itemVariantRef = toWsItemVariantConverter.reference(itemVariant);
        WsStockRef stockRef = toWsStockConverter.reference(stock);
        WsStockChangeType wsStockChangeType = toWsStockChangeTypeConverter.toWsStockChangeType(stockChangeType);

        WsItemVariantStock wsItemVariantStock = new WsItemVariantStock();
        wsItemVariantStock.setId(id);
        wsItemVariantStock.setStartDateTime(startDateTime);
        wsItemVariantStock.setComment(comment);
        wsItemVariantStock.setEndDateTime(endDateTime);
        wsItemVariantStock.setQuantity(quantity);
        wsItemVariantStock.setItemVariantRef(itemVariantRef);
        wsItemVariantStock.setStockRef(stockRef);
        wsItemVariantStock.setStockChangeType(wsStockChangeType);
        wsItemVariantStock.setOrderPosition(orderPosition);

        if (previousItemStock != null) {
            WsItemVariantStockRef previousItemStockref = reference(previousItemStock);
            wsItemVariantStock.setPreviousItemStockRef(previousItemStockref);
        }
        if (stockChangeSale != null) {
            WsItemVariantSaleRef itemVariantSaleRef = toWsItemVariantSaleConverter.reference(stockChangeSale);
            wsItemVariantStock.setStockChangeVariantSaleRef(itemVariantSaleRef);
        }
        return wsItemVariantStock;
    }

    public WsItemVariantStockRef reference(ItemStock itemStock) {
        Long id = itemStock.getId();
        WsItemVariantStockRef wsItemVariantStockRef = new WsItemVariantStockRef(id);
        return wsItemVariantStockRef;
    }
}
