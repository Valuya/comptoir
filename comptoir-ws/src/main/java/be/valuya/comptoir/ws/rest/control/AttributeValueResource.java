package be.valuya.comptoir.ws.rest.control;

import be.valuya.comptoir.api.domain.commercial.WsAttributeValue;
import be.valuya.comptoir.api.domain.commercial.WsAttributeValueRef;
import be.valuya.comptoir.model.commercial.AttributeValue;
import be.valuya.comptoir.service.StockService;
import be.valuya.comptoir.ws.convert.commercial.FromWsAttributeValueConverter;
import be.valuya.comptoir.ws.convert.commercial.ToWsAttributeValueConverter;
import be.valuya.comptoir.ws.convert.search.FromWsAttributeSearchConverter;
import be.valuya.comptoir.ws.rest.validation.IdChecker;
import be.valuya.comptoir.ws.rest.validation.NoId;
import javax.ejb.EJB;
import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;

/**
 *
 * @author Yannick Majoros <yannick@valuya.be>
 */
@Path("/attribute/value")
@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
@Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
public class AttributeValueResource {

    @EJB
    private StockService stockService;
    @Inject
    private FromWsAttributeValueConverter fromWsAttributeValueConverter;
    @Inject
    private FromWsAttributeSearchConverter fromWsAttributeSearchConverter;
    @Inject
    private ToWsAttributeValueConverter toWsAttributeValueConverter;
    @Inject
    private IdChecker idChecker;
    @Context
    private HttpServletResponse response;
    @Context
    private UriInfo uriInfo;
    @Inject
    private RestPaginationUtil restPaginationUtil;

    @POST
    @Valid
    public WsAttributeValueRef createAttributeValue(@NoId @Valid WsAttributeValue wsAttributeValue) {
        AttributeValue attributeValue = fromWsAttributeValueConverter.convert(wsAttributeValue);
        AttributeValue savedAttributeValue = stockService.saveAttributeValue(attributeValue);

        WsAttributeValueRef attributeValueRef = toWsAttributeValueConverter.reference(savedAttributeValue);

        return attributeValueRef;
    }

    @Path("{id}")
    @PUT
    @Valid
    public WsAttributeValueRef updateAttributeValue(@PathParam("id") long id, @Valid WsAttributeValue wsAttributeValue) {
        idChecker.checkId(id, wsAttributeValue);
        AttributeValue attributeValue = fromWsAttributeValueConverter.convert(wsAttributeValue);
        AttributeValue savedAttributeValue = stockService.saveAttributeValue(attributeValue);

        WsAttributeValueRef attributeValueRef = toWsAttributeValueConverter.reference(savedAttributeValue);

        return attributeValueRef;
    }

    @Path("{id}")
    @GET
    @Valid
    public WsAttributeValue getAttributeValue(@PathParam("id") long id) {
        AttributeValue attributeValue = stockService.findAttributeValueById(id);

        WsAttributeValue wsAttributeValue = toWsAttributeValueConverter.convert(attributeValue);

        return wsAttributeValue;
    }

}
