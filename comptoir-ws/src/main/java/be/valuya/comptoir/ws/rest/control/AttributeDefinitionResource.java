package be.valuya.comptoir.ws.rest.control;

import be.valuya.comptoir.api.domain.commercial.WsAttributeDefinition;
import be.valuya.comptoir.api.domain.commercial.WsAttributeDefinitionRef;
import be.valuya.comptoir.api.domain.search.WsAttributeSearch;
import be.valuya.comptoir.model.commercial.AttributeDefinition;
import be.valuya.comptoir.model.search.AttributeSearch;
import be.valuya.comptoir.service.StockService;
import be.valuya.comptoir.util.pagination.AttributeDefinitionColumn;
import be.valuya.comptoir.util.pagination.Pagination;
import be.valuya.comptoir.ws.convert.commercial.FromWsAttributeDefinitionConverter;
import be.valuya.comptoir.ws.convert.commercial.ToWsAttributeDefinitionConverter;
import be.valuya.comptoir.ws.convert.search.FromWsAttributeSearchConverter;
import be.valuya.comptoir.ws.rest.validation.EmployeeAccessChecker;
import be.valuya.comptoir.ws.rest.validation.IdChecker;
import be.valuya.comptoir.ws.rest.validation.NoId;
import be.valuya.comptoir.security.ComptoirRoles;

import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;
import java.util.List;
import java.util.stream.Collectors;

/**
 *
 * @author Yannick Majoros <yannick@valuya.be>
 */
@Path("/attribute/definition")
@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
@Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
@RolesAllowed({ComptoirRoles.EMPLOYEE})
public class AttributeDefinitionResource {

    @EJB
    private StockService stockService;
    @Inject
    private FromWsAttributeDefinitionConverter fromWsAttributeDefinitionConverter;
    @Inject
    private FromWsAttributeSearchConverter fromWsAttributeSearchConverter;
    @Inject
    private ToWsAttributeDefinitionConverter toWsAttributeDefinitionConverter;
    @Inject
    private IdChecker idChecker;
    @Inject
    private EmployeeAccessChecker employeeAccessChecker;
    @Context
    private HttpServletResponse response;
    @Context
    private UriInfo uriInfo;
    @Inject
    private RestPaginationUtil restPaginationUtil;

    @POST
    @Valid
    public WsAttributeDefinitionRef createAttributeDefinition(@NoId @Valid WsAttributeDefinition wsAttributeDefinition) {
        AttributeDefinition attributeDefinition = fromWsAttributeDefinitionConverter.convert(wsAttributeDefinition);
        employeeAccessChecker.checkOwnCompany(attributeDefinition);
        AttributeDefinition savedAttributeDefinition = stockService.saveAttributeDefinition(attributeDefinition);

        WsAttributeDefinitionRef attributeDefinitionRef = toWsAttributeDefinitionConverter.reference(savedAttributeDefinition);

        return attributeDefinitionRef;
    }

    @Path("{id}")
    @PUT
    @Valid
    public WsAttributeDefinitionRef updateAttributeDefinition(@PathParam("id") long id, @Valid WsAttributeDefinition wsAttributeDefinition) {
        idChecker.checkId(id, wsAttributeDefinition);
        AttributeDefinition attributeDefinition = fromWsAttributeDefinitionConverter.convert(wsAttributeDefinition);
        employeeAccessChecker.checkOwnCompany(attributeDefinition);
        AttributeDefinition savedAttributeDefinition = stockService.saveAttributeDefinition(attributeDefinition);

        WsAttributeDefinitionRef attributeDefinitionRef = toWsAttributeDefinitionConverter.reference(savedAttributeDefinition);

        return attributeDefinitionRef;
    }

    @Path("{id}")
    @GET
    @Valid
    public WsAttributeDefinition getAttributeDefinition(@PathParam("id") long id) {
        AttributeDefinition attributeDefinition = stockService.findAttributeDefinitionById(id);
        employeeAccessChecker.checkOwnCompany(attributeDefinition);

        WsAttributeDefinition wsAttributeDefinition = toWsAttributeDefinitionConverter.convert(attributeDefinition);

        return wsAttributeDefinition;
    }

    @POST
    @Path("search")
    @Valid
    public List<WsAttributeDefinition> findAttributeDefinitions(@Valid WsAttributeSearch wsAttributeSearch) {
        Pagination<AttributeDefinition, AttributeDefinitionColumn> pagination = restPaginationUtil.extractPagination(uriInfo, AttributeDefinitionColumn::valueOf);

        AttributeSearch attributeSearch = fromWsAttributeSearchConverter.convert(wsAttributeSearch);
        employeeAccessChecker.checkOwnCompany(attributeSearch);

        List<AttributeDefinition> attributeDefinitions = stockService.findAttributeDefinitions(attributeSearch, pagination);

        List<WsAttributeDefinition> wsAttributeDefinitions = attributeDefinitions.stream()
                .map(toWsAttributeDefinitionConverter::convert)
                .collect(Collectors.toList());

        restPaginationUtil.addResultCount(response, pagination);

        return wsAttributeDefinitions;
    }

}
