package be.valuya.comptoir.ws.rest.control;

import be.valuya.comptoir.api.domain.commercial.WsItemPicture;
import be.valuya.comptoir.api.domain.commercial.WsItemPictureRef;
import be.valuya.comptoir.model.commercial.Item;
import be.valuya.comptoir.model.commercial.ItemPicture;
import be.valuya.comptoir.service.StockService;
import be.valuya.comptoir.ws.convert.commercial.FromWsItemPictureConverter;
import be.valuya.comptoir.ws.convert.commercial.ToWsItemPictureConverter;
import be.valuya.comptoir.ws.rest.validation.IdChecker;
import be.valuya.comptoir.ws.rest.validation.NoId;
import java.util.List;
import java.util.stream.Collectors;
import javax.ejb.EJB;
import javax.inject.Inject;
import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 *
 * @author Yannick Majoros <yannick@valuya.be>
 */
@Path("/item/{itemId}/picture")
@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
@Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
public class ItemPictureResource {

    @EJB
    private StockService stockService;
    @Inject
    private FromWsItemPictureConverter fromWsItemPictureConverter;
    @Inject
    private ToWsItemPictureConverter toWsItemPictureConverter;
    @Inject
    private IdChecker idChecker;
    @PathParam("itemId")
    private Long itemId;

    @POST
    @Valid
    public WsItemPictureRef createItemPicture(@NoId @Valid WsItemPicture wsItemPicture) {
        ItemPicture itemPicture = fromWsItemPictureConverter.convert(wsItemPicture);
        ItemPicture savedItemPicture = stockService.saveItemPicture(itemPicture);
        WsItemPictureRef itemPictureRef = toWsItemPictureConverter.reference(savedItemPicture);
        return itemPictureRef;
    }

    @Path("{id}")
    @PUT
    @Valid
    public WsItemPictureRef updateItemPicture(@PathParam("id") long id, WsItemPicture wsItemPicture) {
        idChecker.checkId(id, wsItemPicture);
        ItemPicture itemPicture = fromWsItemPictureConverter.convert(wsItemPicture);
        ItemPicture savedItemPicture = stockService.saveItemPicture(itemPicture);
        WsItemPictureRef itemPictureRef = toWsItemPictureConverter.reference(savedItemPicture);
        return itemPictureRef;
    }

    @GET
    @Valid
    public List<WsItemPicture> findItemPictures() {
        Item item = stockService.findItemById(itemId);
        List<ItemPicture> itemPictures = stockService.findItemPictures(item);

        List<WsItemPicture> wsItemPictures = itemPictures.stream()
                .map(toWsItemPictureConverter::convert)
                .collect(Collectors.toList());

        return wsItemPictures;
    }

    @Path("{id}")
    @GET
    @Valid
    public WsItemPicture getItemPicture(@PathParam("id") long id) {
        ItemPicture itemPicture = stockService.findItemPictureById(id);

        WsItemPicture wsItemPicture = toWsItemPictureConverter.convert(itemPicture);

        return wsItemPicture;
    }

}
