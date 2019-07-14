package be.valuya.comptoir.ws.rest.control;

import be.valuya.comptoir.api.domain.commercial.WsPos;
import be.valuya.comptoir.api.domain.commercial.WsPosRef;
import be.valuya.comptoir.api.domain.search.WsPosSearch;
import be.valuya.comptoir.model.commercial.Pos;
import be.valuya.comptoir.model.search.PosSearch;
import be.valuya.comptoir.service.PosService;
import be.valuya.comptoir.util.pagination.Pagination;
import be.valuya.comptoir.util.pagination.PosColumn;
import be.valuya.comptoir.ws.api.PosResourceApi;
import be.valuya.comptoir.ws.api.parameters.PaginationParams;
import be.valuya.comptoir.ws.convert.RestPaginationUtil;
import be.valuya.comptoir.ws.convert.commercial.FromWsPosConverter;
import be.valuya.comptoir.ws.convert.commercial.ToWsPosConverter;
import be.valuya.comptoir.ws.convert.search.FromWsPosSearchConverter;
import be.valuya.comptoir.ws.rest.validation.EmployeeAccessChecker;
import be.valuya.comptoir.ws.rest.validation.IdChecker;
import be.valuya.comptoir.security.ComptoirRoles;

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
public class PosResource implements PosResourceApi {

    @EJB
    private PosService posService;
    @Inject
    private FromWsPosConverter fromWsPosConverter;
    @Inject
    private FromWsPosSearchConverter fromWsPosSearchConverter;
    @Inject
    private ToWsPosConverter toWsPosConverter;
    @Inject
    private IdChecker idChecker;
    @Context
    private UriInfo uriInfo;
    @Inject
    private RestPaginationUtil restPaginationUtil;
    @Context
    private HttpServletResponse response;
    @Inject
    private EmployeeAccessChecker accessChecker;

    public WsPosRef createPos(WsPos wsPos) {
        Pos pos = fromWsPosConverter.convert(wsPos);
        accessChecker.checkOwnCompany(pos);
        Pos savedPos = posService.savePos(pos);

        WsPosRef posRef = toWsPosConverter.reference(savedPos);

        return posRef;
    }

    public WsPosRef savePos(long id, WsPos wsPos) {
        idChecker.checkId(id, wsPos);
        Pos pos = fromWsPosConverter.convert(wsPos);
        accessChecker.checkOwnCompany(pos);
        Pos savedPos = posService.savePos(pos);

        WsPosRef posRef = toWsPosConverter.reference(savedPos);

        return posRef;
    }

    public WsPos getPos(long id) {
        Pos pos = posService.findPosById(id);
        accessChecker.checkOwnCompany(pos);

        WsPos wsPos = toWsPosConverter.convert(pos);

        return wsPos;
    }

    public List<WsPos> findPosList(PaginationParams paginationParams, WsPosSearch wsPosSearch) {
        Pagination<Pos, PosColumn> pagination = restPaginationUtil.extractPagination(uriInfo, PosColumn::valueOf);
        PosSearch posSearch = fromWsPosSearchConverter.convert(wsPosSearch);
        accessChecker.checkOwnCompany(posSearch);
        List<Pos> posList = posService.findPosList(posSearch, pagination);

        List<WsPos> wsPosList = posList.stream()
                .map(toWsPosConverter::convert)
                .collect(Collectors.toList());

        restPaginationUtil.addResultCount(response, pagination);
        return wsPosList;
    }

}
