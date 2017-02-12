package be.valuya.comptoir.ws.rest.control;

import be.valuya.comptoir.api.domain.search.WsEmployeeSearch;
import be.valuya.comptoir.api.domain.thirdparty.WsEmployee;
import be.valuya.comptoir.api.domain.thirdparty.WsEmployeeRef;
import be.valuya.comptoir.model.search.EmployeeSearch;
import be.valuya.comptoir.model.thirdparty.Employee;
import be.valuya.comptoir.service.EmployeeService;
import be.valuya.comptoir.ws.convert.search.FromWsEmployeeSearchConverter;
import be.valuya.comptoir.ws.convert.thirdparty.FromWsEmployeeConverter;
import be.valuya.comptoir.ws.convert.thirdparty.ToWsEmployeeConverter;
import be.valuya.comptoir.ws.rest.validation.IdChecker;
import be.valuya.comptoir.ws.rest.validation.NoId;
import be.valuya.comptoir.ws.security.Roles;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.inject.Inject;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Yannick Majoros <yannick@valuya.be>
 */
@Path("/employee")
@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
public class EmployeeResource {

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

    @POST
    @PermitAll
    public WsEmployeeRef createEmployee(@NoId WsEmployee wsEmployee) {
        Employee employee = fromWsEmployeeConverter.convert(wsEmployee);
        Employee savedEmployee = employeeService.saveEmployee(employee);

        WsEmployeeRef employeeRef = toWsEmployeeConverter.reference(savedEmployee);

        return employeeRef;
    }


    @Path("{id}")
    @Valid
    @GET
    public WsEmployee getEmployee(@PathParam("id") long id) {
        Employee employee = employeeService.findEmployeeById(id);

        WsEmployee wsEmployee = toWsEmployeeConverter.convert(employee);

        return wsEmployee;
    }

    @Path("{id}")
    @Valid
    @PUT
    @RolesAllowed({Roles.EMPLOYEE})
    public WsEmployeeRef saveEmployee(@PathParam("id") long id, @Valid WsEmployee wsEmployee) {
        idChecker.checkId(id, wsEmployee);

        Employee existingEmployee = employeeService.findEmployeeById(id);
        Employee employee = fromWsEmployeeConverter.patch(existingEmployee, wsEmployee);

        Employee savedEmployee = employeeService.saveEmployee(employee);
        WsEmployeeRef employeeRef = toWsEmployeeConverter.reference(savedEmployee);

        return employeeRef;
    }

    @Valid
    @POST
    @Path("/search")
    @RolesAllowed({Roles.EMPLOYEE})
    public List<WsEmployee> findEmployees(@Valid WsEmployeeSearch wsEmployeeSearch) {
        EmployeeSearch employeeSearch = fromWsEmployeeSearchConverter.convert(wsEmployeeSearch);
        List<Employee> employees = employeeService.findEmployees(employeeSearch);

        List<WsEmployee> wsEmployees = employees.stream()
                .map(toWsEmployeeConverter::convert)
                .collect(Collectors.toList());

        return wsEmployees;
    }

    @Path("/{employeeId}/password/{password}")
    @PUT
    @RolesAllowed({Roles.EMPLOYEE})
    public void setPassword(@PathParam("employeeId") long employeeId, @PathParam("password") String password) {
        Employee employee = employeeService.findEmployeeById(employeeId);
        employeeService.setPassword(employee, password);
    }

}
