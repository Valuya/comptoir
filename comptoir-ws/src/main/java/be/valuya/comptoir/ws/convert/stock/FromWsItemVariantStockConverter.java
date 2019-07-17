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
import be.valuya.comptoir.service.StockService;
import be.valuya.comptoir.ws.convert.commercial.FromWsItemVariantConverter;
import be.valuya.comptoir.ws.convert.commercial.FromWsItemVariantSaleConverter;

import javax.annotation.Nonnull;
import javax.ejb.EJB;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.math.BigDecimal;
import java.time.ZonedDateTime;

/**
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
    private FromWsItemVariantSaleConverter fromWsItemVariantSaleConverter;
    @Inject
    private FromWsStockChangeTypeConverter fromWsStockChangeTypeConverter;

    public ItemStock convert(WsItemVariantStock wsItemVariantStock) {
        if (wsItemVariantStock == null) {
            return null;
        }
        ItemStock itemStock = new ItemStock();
        return patch(itemStock, wsItemVariantStock);
    }

    public ItemStock patch(@Nonnull ItemStock itemStock, @Nonnull WsItemVariantStock wsItemVariantStock) {

        String comment = wsItemVariantStock.getComment();
        ZonedDateTime endDateTime = wsItemVariantStock.getEndDateTime();
        Long id = wsItemVariantStock.getId();
        WsItemVariantRef itemVariantRef = wsItemVariantStock.getItemVariantRef();
        BigDecimal quantity = wsItemVariantStock.getQuantity();
        ZonedDateTime startDateTime = wsItemVariantStock.getStartDateTime();
        WsStockRef stockRef = wsItemVariantStock.getStockRef();
        WsItemVariantStockRef previousItemStockRef = wsItemVariantStock.getPreviousItemStockRef();
        WsStockChangeType wsStockChangeType = wsItemVariantStock.getStockChangeType();
        WsItemVariantSaleRef stockChangeSaleRef = wsItemVariantStock.getStockChangeVariantSaleRef();
        Integer orderPosition = wsItemVariantStock.getOrderPosition();

        ItemVariant itemVariant = fromWsItemVariantConverter.find(itemVariantRef);
        Stock stock = fromWsStockConverter.find(stockRef);
        StockChangeType stockChangeType = fromWsStockChangeTypeConverter.fromWsStockChangeType(wsStockChangeType);

        itemStock.setComment(comment);
        itemStock.setEndDateTime(endDateTime);
        itemStock.setId(id);
        itemStock.setItemVariant(itemVariant);
        itemStock.setQuantity(quantity);
        itemStock.setStartDateTime(startDateTime);
        itemStock.setStock(stock);
        itemStock.setStockChangeType(stockChangeType);
        itemStock.setOrderPosition(orderPosition);

        if (previousItemStockRef != null) {
            ItemStock previousItemStock = find(previousItemStockRef);
            itemStock.setPreviousItemStock(previousItemStock);
        }
        if (stockChangeSaleRef != null) {
            ItemVariantSale stockChangeSale = fromWsItemVariantSaleConverter.find(stockChangeSaleRef);
            itemStock.setStockChangeVariantSale(stockChangeSale);
        }
        return itemStock;
    }

    public ItemStock find(WsItemVariantStockRef wsItemVariantStockRef) {
        ItemStock stockById = stockService.findItemStockById(wsItemVariantStockRef.getId());
        return stockById;
    }
}
