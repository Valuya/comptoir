package be.valuya.comptoir.ws.rest.control;

import be.valuya.comptoir.model.search.AttributeSearch;
import be.valuya.comptoir.util.pagination.AttributeValueColumn;
import be.valuya.comptoir.util.pagination.Pagination;
import be.valuya.comptoir.ws.rest.api.domain.commercial.WsAttributeValue;
import be.valuya.comptoir.ws.rest.api.domain.commercial.WsAttributeValueRef;
import be.valuya.comptoir.model.commercial.AttributeValue;
import be.valuya.comptoir.ws.rest.api.domain.commercial.WsAttributeValueSearchResult;
import be.valuya.comptoir.ws.rest.api.domain.search.WsAttributeSearch;
import be.valuya.comptoir.ws.rest.api.util.ComptoirRoles;
import be.valuya.comptoir.service.StockService;
import be.valuya.comptoir.ws.convert.RestPaginationUtil;
import be.valuya.comptoir.ws.convert.commercial.FromWsAttributeValueConverter;
import be.valuya.comptoir.ws.convert.commercial.ToWsAttributeValueConverter;
import be.valuya.comptoir.ws.convert.search.FromWsAttributeSearchConverter;
import be.valuya.comptoir.ws.rest.api.AttributeValueResourceApi;
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
public class AttributeValueResource implements AttributeValueResourceApi {

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
    @Inject
    private EmployeeAccessChecker employeeAccessChecker;
    @Context
    private HttpServletResponse response;
    @Context
    private UriInfo uriInfo;
    @Inject
    private RestPaginationUtil restPaginationUtil;

    public WsAttributeValueRef createAttributeValue(WsAttributeValue wsAttributeValue) {
        AttributeValue attributeValue = fromWsAttributeValueConverter.convert(wsAttributeValue);
        employeeAccessChecker.checkOwnCompany(attributeValue.getAttributeDefinition());
        AttributeValue savedAttributeValue = stockService.saveAttributeValue(attributeValue);

        WsAttributeValueRef attributeValueRef = toWsAttributeValueConverter.reference(savedAttributeValue);

        return attributeValueRef;
    }

    public WsAttributeValueRef updateAttributeValue(long id, WsAttributeValue wsAttributeValue) {
        idChecker.checkId(id, wsAttributeValue);
        AttributeValue attributeValue = fromWsAttributeValueConverter.convert(wsAttributeValue);
        employeeAccessChecker.checkOwnCompany(attributeValue.getAttributeDefinition());
        AttributeValue savedAttributeValue = stockService.saveAttributeValue(attributeValue);

        WsAttributeValueRef attributeValueRef = toWsAttributeValueConverter.reference(savedAttributeValue);

        return attributeValueRef;
    }

    public WsAttributeValue getAttributeValue(long id) {
        AttributeValue attributeValue = stockService.findAttributeValueById(id);
        employeeAccessChecker.checkOwnCompany(attributeValue.getAttributeDefinition());

        WsAttributeValue wsAttributeValue = toWsAttributeValueConverter.convert(attributeValue);

        return wsAttributeValue;
    }

}
