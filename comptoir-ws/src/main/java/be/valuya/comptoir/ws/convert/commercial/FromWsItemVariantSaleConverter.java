package be.valuya.comptoir.ws.convert.commercial;

import be.valuya.comptoir.ws.rest.api.domain.commercial.WsItemVariantRef;
import be.valuya.comptoir.ws.rest.api.domain.commercial.WsItemVariantSale;
import be.valuya.comptoir.ws.rest.api.domain.commercial.WsItemVariantSaleRef;
import be.valuya.comptoir.ws.rest.api.domain.commercial.WsSaleRef;
import be.valuya.comptoir.ws.rest.api.domain.lang.WsLocaleText;
import be.valuya.comptoir.ws.rest.api.domain.stock.WsStockRef;
import be.valuya.comptoir.model.commercial.ItemVariant;
import be.valuya.comptoir.model.commercial.ItemVariantSale;
import be.valuya.comptoir.model.commercial.Price;
import be.valuya.comptoir.model.commercial.Sale;
import be.valuya.comptoir.model.lang.LocaleText;
import be.valuya.comptoir.model.stock.Stock;
import be.valuya.comptoir.service.SaleService;
import be.valuya.comptoir.ws.convert.stock.FromWsStockConverter;
import be.valuya.comptoir.ws.convert.text.FromWsLocaleTextConverter;

import javax.ejb.EJB;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.List;

/**
 * @author Yannick Majoros <yannick@valuya.be>
 */
@ApplicationScoped
public class FromWsItemVariantSaleConverter {

    @Inject
    private FromWsLocaleTextConverter fromWsLocaleTextConverter;
    @Inject
    private FromWsSaleConverter fromWsSaleConverter;
    @Inject
    private FromWsItemVariantConverter fromWsItemConverter;
    @Inject
    private FromWsStockConverter fromWsStockConverter;
    @EJB
    private SaleService saleService;

    public ItemVariantSale convert(WsItemVariantSale wsItemSale) {
        if (wsItemSale == null) {
            return null;
        }
        ItemVariantSale itemSale = new ItemVariantSale();
        itemSale.setQuantity(BigDecimal.ONE);

        return patch(itemSale, wsItemSale);
    }

    public ItemVariantSale patch(ItemVariantSale itemSale, WsItemVariantSale wsItemSale) {
        Long id = wsItemSale.getId();

        ZonedDateTime dateTime = wsItemSale.getDateTime();

        List<WsLocaleText> wsComment = wsItemSale.getComment();
        LocaleText comment = fromWsLocaleTextConverter.convert(wsComment);

        WsItemVariantRef itemVariantRef = wsItemSale.getItemVariantRef();
        ItemVariant itemVariant = fromWsItemConverter.find(itemVariantRef);

        WsSaleRef saleRef = wsItemSale.getSaleRef();
        Sale sale = fromWsSaleConverter.find(saleRef);

        WsStockRef stockRef = wsItemSale.getStockRef();
        Stock stock = fromWsStockConverter.find(stockRef);

        Boolean includeCustomerLoyalty = wsItemSale.getIncludeCustomerLoyalty();
        Boolean includeCustomerDiscount = wsItemSale.getIncludeCustomerDiscount();

        itemSale.setId(id);
        itemSale.setItemVariant(itemVariant);
        itemSale.setSale(sale);
        itemSale.setComment(comment);
        itemSale.setDateTime(dateTime);
        itemSale.setStock(stock);
        itemSale.setIncludeCustomerLoyalty(includeCustomerLoyalty);
        itemSale.setIncludeCustomerDiscount(includeCustomerDiscount);

        return itemSale;
    }

    public ItemVariantSale find(WsItemVariantSaleRef itemSaleRef) {
        if (itemSaleRef == null) {
            return null;
        }
        Long id = itemSaleRef.getId();
        return saleService.findItemSaleById(id);
    }

}
