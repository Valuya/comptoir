/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package be.valuya.comptoir.ws.convert.stock;

import be.valuya.comptoir.api.domain.commercial.WsItemVariantRef;
import be.valuya.comptoir.api.domain.commercial.WsSaleRef;
import be.valuya.comptoir.api.domain.stock.WsItemStock;
import be.valuya.comptoir.api.domain.stock.WsItemStockRef;
import be.valuya.comptoir.api.domain.stock.WsStockRef;
import be.valuya.comptoir.model.commercial.ItemVariant;
import be.valuya.comptoir.model.commercial.Sale;
import be.valuya.comptoir.model.stock.ItemStock;
import be.valuya.comptoir.model.stock.Stock;
import be.valuya.comptoir.model.stock.StockChangeType;
import be.valuya.comptoir.service.StockService;
import be.valuya.comptoir.ws.convert.commercial.FromWsItemVariantConverter;
import be.valuya.comptoir.ws.convert.commercial.FromWsSaleConverter;

import javax.annotation.Nonnull;
import javax.ejb.EJB;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.math.BigDecimal;
import java.time.ZonedDateTime;

/**
 *
 * @author cghislai
 */
@ApplicationScoped
public class FromWsItemVariantStockConverter {

    @EJB
    private StockService stockService;
    @Inject
    private FromWsStockConverter fromWsStockConverter;
    @Inject
    private FromWsItemVariantConverter fromWsItemVariantConverter;
    @Inject
    private FromWsSaleConverter fromWsSaleConverter;

    public ItemStock convert(WsItemStock wsItemStock) {
        if (wsItemStock == null) {
            return null;
        }
        ItemStock itemStock = new ItemStock();
        return patch(itemStock, wsItemStock);
    }

    public ItemStock patch(@Nonnull ItemStock itemStock, @Nonnull WsItemStock wsItemStock) {

        String comment = wsItemStock.getComment();
        ZonedDateTime endDateTime = wsItemStock.getEndDateTime();
        Long id = wsItemStock.getId();
        WsItemVariantRef itemVariantRef = wsItemStock.getItemVariantRef();
        BigDecimal quantity = wsItemStock.getQuantity();
        ZonedDateTime startDateTime = wsItemStock.getStartDateTime();
        WsStockRef stockRef = wsItemStock.getStockRef();
        WsItemStockRef previousItemStockRef = wsItemStock.getPreviousItemStockRef();
        StockChangeType stockChangeType = wsItemStock.getStockChangeType();
        WsSaleRef stockChangeSaleRef = wsItemStock.getStockChangeSaleRef();

        ItemVariant itemVariant = fromWsItemVariantConverter.find(itemVariantRef);
        Stock stock = fromWsStockConverter.find(stockRef);
        ItemStock previouseItemStock = find(previousItemStockRef);
        Sale stockChangeSale = fromWsSaleConverter.find(stockChangeSaleRef);

        itemStock.setComment(comment);
        itemStock.setEndDateTime(endDateTime);
        itemStock.setId(id);
        itemStock.setItemVariant(itemVariant);
        itemStock.setQuantity(quantity);
        itemStock.setStartDateTime(startDateTime);
        itemStock.setStock(stock);
        itemStock.setPreviousItemStock(previouseItemStock);
        itemStock.setStockChangeSale(stockChangeSale);
        itemStock.setStockChangeType(stockChangeType);

        return  itemStock;
    }

    public ItemStock find(WsItemStockRef wsItemStockRef) {
        ItemStock stockById = stockService.findItemStockById(wsItemStockRef.getId());
        return stockById;
    }
}
