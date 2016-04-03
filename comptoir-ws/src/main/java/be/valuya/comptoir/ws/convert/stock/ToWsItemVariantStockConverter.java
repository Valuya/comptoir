/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package be.valuya.comptoir.ws.convert.stock;

import be.valuya.comptoir.api.domain.commercial.WsItemVariantRef;
import be.valuya.comptoir.api.domain.commercial.WsItemVariantSaleRef;
import be.valuya.comptoir.api.domain.stock.WsItemStock;
import be.valuya.comptoir.api.domain.stock.WsItemStockRef;
import be.valuya.comptoir.api.domain.stock.WsStockRef;
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

    public WsItemStock convert(ItemStock itemStock) {
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

        WsItemStock wsItemStock = new WsItemStock();
        wsItemStock.setId(id);
        wsItemStock.setStartDateTime(startDateTime);
        wsItemStock.setComment(comment);
        wsItemStock.setEndDateTime(endDateTime);
        wsItemStock.setQuantity(quantity);
        wsItemStock.setItemVariantRef(itemVariantRef);
        wsItemStock.setStockRef(stockRef);
        wsItemStock.setStockChangeType(stockChangeType);
        wsItemStock.setOrderPosition(orderPosition);

        if (previousItemStock != null) {
            WsItemStockRef previousItemStockref = reference(previousItemStock);
            wsItemStock.setPreviousItemStockRef(previousItemStockref);
        }
        if (stockChangeSale != null) {
            WsItemVariantSaleRef itemVariantSaleRef = toWsItemVariantSaleConverter.reference(stockChangeSale);
            wsItemStock.setStockChangeVariantSaleRef(itemVariantSaleRef);
        }
        return wsItemStock;
    }

    public WsItemStockRef reference(ItemStock itemStock) {
        Long id = itemStock.getId();
        WsItemStockRef wsItemStockRef = new WsItemStockRef(id);
        return wsItemStockRef;
    }
}