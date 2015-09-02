package be.valuya.comptoir.ws.convert.commercial;

import be.valuya.comptoir.api.domain.commercial.WsItemPicture;
import be.valuya.comptoir.api.domain.commercial.WsItemPictureRef;
import be.valuya.comptoir.model.commercial.ItemPicture;
import be.valuya.comptoir.service.StockService;
import javax.ejb.EJB;
import javax.enterprise.context.ApplicationScoped;

/**
 *
 * @author Yannick Majoros <yannick@valuya.be>
 */
@ApplicationScoped
public class FromWsItemPictureConverter {

    @EJB
    private StockService stockService;

    public ItemPicture convert(WsItemPicture wsItemPicture) {
        if (wsItemPicture == null) {
            return null;
        }
        Long id = wsItemPicture.getId();
        String contentType = wsItemPicture.getContentType();
        byte[] data = wsItemPicture.getData();

        ItemPicture itemPicture = new ItemPicture();
        itemPicture.setId(id);
        itemPicture.setContentType(contentType);
        itemPicture.setData(data);

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
