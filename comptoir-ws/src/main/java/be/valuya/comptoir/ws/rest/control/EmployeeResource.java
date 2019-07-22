package be.valuya.comptoir.ws.rest.control;

import be.valuya.comptoir.util.pagination.Pagination;
import be.valuya.comptoir.ws.convert.RestPaginationUtil;
import be.valuya.comptoir.ws.rest.api.domain.search.WsEmployeeSearch;
import be.valuya.comptoir.ws.rest.api.domain.thirdparty.WsEmployee;
import be.valuya.comptoir.ws.rest.api.domain.thirdparty.WsEmployeeRef;
import be.valuya.comptoir.model.search.EmployeeSearch;
import be.valuya.comptoir.model.thirdparty.Employee;
import be.valuya.comptoir.ws.rest.api.domain.thirdparty.WsEmployeeSearchResult;
import be.valuya.comptoir.ws.rest.api.util.ComptoirRoles;
import be.valuya.comptoir.service.EmployeeService;
import be.valuya.comptoir.ws.convert.search.FromWsEmployeeSearchConverter;
import be.valuya.comptoir.ws.convert.thirdparty.FromWsEmployeeConverter;
import be.valuya.comptoir.ws.convert.thirdparty.ToWsEmployeeConverter;
import be.valuya.comptoir.ws.rest.api.EmployeeResourceApi;
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
public class EmployeeResource implements EmployeeResourceApi {

    @EJB
    private EmployeeService employeeService;
    @Inject
    private FromWsEmployeeConverter fromWsEmployeeConverter;
    @Inject
    private ToWsEmployeeConverter toWsEmployeeConverter;
    @Inject
    private IdChecker idChecker;
    @Inject
    private FromWsEmployeeSearchConverter fromWsEmployeeSearchConverter;
    @Inject
    private EmployeeAccessChecker accessChecker;
    @Inject
    private RestPaginationUtil restPaginationUtil;
    @Context
    private UriInfo uriInfo;

    public WsEmployeeRef createEmployee(WsEmployee wsEmployee) {
        Employee employee = fromWsEmployeeConverter.convert(wsEmployee);
        accessChecker.checkSelf(employee); //TODO: limit to some role
        Employee savedEmployee = employeeService.saveEmployee(employee);

        WsEmployeeRef employeeRef = toWsEmployeeConverter.reference(savedEmployee);

        return employeeRef;
    }


    public WsEmployee getEmployee(long id) {
        Employee employee = employeeService.findEmployeeById(id);
        accessChecker.checkSelf(employee); //TODO: limit to some role

        WsEmployee wsEmployee = toWsEmployeeConverter.convert(employee);

        return wsEmployee;
    }

    public WsEmployeeRef updateEmployee(long id, WsEmployee wsEmployee) {
        idChecker.checkId(id, wsEmployee);

        Employee existingEmployee = employeeService.findEmployeeById(id);
        accessChecker.checkSelf(existingEmployee); //TODO: limit to some role
        Employee employee = fromWsEmployeeConverter.patch(existingEmployee, wsEmployee);

        Employee savedEmployee = employeeService.saveEmployee(employee);
        WsEmployeeRef employeeRef = toWsEmployeeConverter.reference(savedEmployee);

        return employeeRef;
    }

    public WsEmployeeSearchResult findEmployees(WsEmployeeSearch wsEmployeeSearch) {
        Pagination<Object, ?> pagination = restPaginationUtil.extractPagination(uriInfo);
        EmployeeSearch employeeSearch = fromWsEmployeeSearchConverter.convert(wsEmployeeSearch);
        accessChecker.checkOwnCompany(employeeSearch);
        List<Employee> employees = employeeService.findEmployees(employeeSearch);

        List<WsEmployeeRef> wsEmployees = employees.stream()
                .map(toWsEmployeeConverter::reference)
                .collect(Collectors.toList());

        return restPaginationUtil.setResults(new WsEmployeeSearchResult(), wsEmployees, pagination);
    }

    public void setPassword(long employeeId, String password) {
        Employee employee = employeeService.findEmployeeById(employeeId);
        accessChecker.checkSelf(employee); //TODO: limit to some role
        employeeService.setPassword(employee, password);
    }

}
