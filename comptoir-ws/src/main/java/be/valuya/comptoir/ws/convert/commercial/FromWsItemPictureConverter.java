package be.valuya.comptoir.ws.convert.commercial;

import be.valuya.comptoir.api.domain.commercial.WsItemPicture;
import be.valuya.comptoir.api.domain.commercial.WsItemPictureRef;
import be.valuya.comptoir.api.domain.commercial.WsItemVariantRef;
import be.valuya.comptoir.model.commercial.ItemPicture;
import be.valuya.comptoir.model.commercial.ItemVariant;
import be.valuya.comptoir.service.StockService;
import javax.ejb.EJB;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

/**
 *
 * @author Yannick Majoros <yannick@valuya.be>
 */
@ApplicationScoped
public class FromWsItemPictureConverter {

    @EJB
    private StockService stockService;
    @Inject
    private FromWsItemConverter fromWsItemConverter;

    public ItemPicture convert(WsItemPicture wsItemPicture) {
        if (wsItemPicture == null) {
            return null;
        }
        Long id = wsItemPicture.getId();
        String contentType = wsItemPicture.getContentType();
        byte[] data = wsItemPicture.getData();

        WsItemVariantRef itemVariantRef = wsItemPicture.getItemVariantRef();
        ItemVariant itemVariant = fromWsItemConverter.find(itemVariantRef);

        ItemPicture itemPicture = new ItemPicture();
        itemPicture.setId(id);
        itemPicture.setContentType(contentType);
        itemPicture.setData(data);
        itemPicture.setItemVariant(itemVariant);

        return itemPicture;
    }

    public ItemPicture find(WsItemPictureRef itemPictureRef) {
        if (itemPictureRef == null) {
            return null;
        }
        Long id = itemPictureRef.getId();
        return stockService.findItemPictureById(id);
    }

}
