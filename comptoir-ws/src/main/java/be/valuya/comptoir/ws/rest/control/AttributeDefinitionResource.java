package be.valuya.comptoir.ws.rest.control;

import be.valuya.comptoir.ws.rest.api.domain.commercial.WsAttributeDefinition;
import be.valuya.comptoir.ws.rest.api.domain.commercial.WsAttributeDefinitionRef;
import be.valuya.comptoir.ws.rest.api.domain.commercial.WsAttributeDefinitionSearchResult;
import be.valuya.comptoir.ws.rest.api.domain.search.WsAttributeSearch;
import be.valuya.comptoir.model.commercial.AttributeDefinition;
import be.valuya.comptoir.model.search.AttributeSearch;
import be.valuya.comptoir.ws.rest.api.util.ComptoirRoles;
import be.valuya.comptoir.service.StockService;
import be.valuya.comptoir.util.pagination.AttributeDefinitionColumn;
import be.valuya.comptoir.util.pagination.Pagination;
import be.valuya.comptoir.ws.convert.RestPaginationUtil;
import be.valuya.comptoir.ws.convert.commercial.FromWsAttributeDefinitionConverter;
import be.valuya.comptoir.ws.convert.commercial.ToWsAttributeDefinitionConverter;
import be.valuya.comptoir.ws.convert.search.FromWsAttributeSearchConverter;
import be.valuya.comptoir.ws.rest.api.AttributeDefinitionResourceApi;
import be.valuya.comptoir.ws.rest.api.util.PaginationParams;
import be.valuya.comptoir.ws.rest.validation.EmployeeAccessChecker;
import be.valuya.comptoir.ws.rest.validation.IdChecker;

import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Yannick Majoros <yannick@valuya.be>
 */


@RolesAllowed({ComptoirRoles.EMPLOYEE})
public class AttributeDefinitionResource implements AttributeDefinitionResourceApi {

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

    public WsAttributeDefinitionRef createAttributeDefinition(WsAttributeDefinition wsAttributeDefinition) {
        AttributeDefinition attributeDefinition = fromWsAttributeDefinitionConverter.convert(wsAttributeDefinition);
        employeeAccessChecker.checkOwnCompany(attributeDefinition);
        AttributeDefinition savedAttributeDefinition = stockService.saveAttributeDefinition(attributeDefinition);

        WsAttributeDefinitionRef attributeDefinitionRef = toWsAttributeDefinitionConverter.reference(savedAttributeDefinition);

        return attributeDefinitionRef;
    }

    public WsAttributeDefinitionRef updateAttributeDefinition(long id, WsAttributeDefinition wsAttributeDefinition) {
        idChecker.checkId(id, wsAttributeDefinition);
        AttributeDefinition attributeDefinition = fromWsAttributeDefinitionConverter.convert(wsAttributeDefinition);
        employeeAccessChecker.checkOwnCompany(attributeDefinition);
        AttributeDefinition savedAttributeDefinition = stockService.saveAttributeDefinition(attributeDefinition);

        WsAttributeDefinitionRef attributeDefinitionRef = toWsAttributeDefinitionConverter.reference(savedAttributeDefinition);

        return attributeDefinitionRef;
    }

    public WsAttributeDefinition getAttributeDefinition(long id) {
        AttributeDefinition attributeDefinition = stockService.findAttributeDefinitionById(id);
        employeeAccessChecker.checkOwnCompany(attributeDefinition);

        WsAttributeDefinition wsAttributeDefinition = toWsAttributeDefinitionConverter.convert(attributeDefinition);

        return wsAttributeDefinition;
    }

    public WsAttributeDefinitionSearchResult findAttributeDefinitions(PaginationParams paginationParams, WsAttributeSearch wsAttributeSearch) {
        Pagination<AttributeDefinition, AttributeDefinitionColumn> pagination = restPaginationUtil.extractPagination(uriInfo, AttributeDefinitionColumn::valueOf);

        AttributeSearch attributeSearch = fromWsAttributeSearchConverter.convert(wsAttributeSearch);
        employeeAccessChecker.checkOwnCompany(attributeSearch);

        List<AttributeDefinition> attributeDefinitions = stockService.findAttributeDefinitions(attributeSearch, pagination);

        List<WsAttributeDefinitionRef> wsAttributeDefinitions = attributeDefinitions.stream()
                .map(toWsAttributeDefinitionConverter::reference)
                .collect(Collectors.toList());

        restPaginationUtil.addResultCount(response, pagination);

        return restPaginationUtil.setResults(new WsAttributeDefinitionSearchResult(), wsAttributeDefinitions, pagination);
    }

}
