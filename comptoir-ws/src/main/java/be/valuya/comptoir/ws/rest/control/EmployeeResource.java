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
import be.valuya.comptoir.ws.rest.validation.EmployeeAccessChecker;
import be.valuya.comptoir.ws.rest.validation.IdChecker;
import be.valuya.comptoir.ws.rest.validation.NoId;
import be.valuya.comptoir.security.ComptoirRoles;

import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
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
@RolesAllowed({ComptoirRoles.EMPLOYEE})
@RequestScoped
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
    @Inject
    private EmployeeAccessChecker accessChecker;

    @POST
    public WsEmployeeRef createEmployee(@NoId WsEmployee wsEmployee) {
        Employee employee = fromWsEmployeeConverter.convert(wsEmployee);
        accessChecker.checkSelf(employee); //TODO: limit to some role
        Employee savedEmployee = employeeService.saveEmployee(employee);

        WsEmployeeRef employeeRef = toWsEmployeeConverter.reference(savedEmployee);

        return employeeRef;
    }


    @Path("{id}")
    @Valid
    @GET
    public WsEmployee getEmployee(@PathParam("id") long id) {
        Employee employee = employeeService.findEmployeeById(id);
        accessChecker.checkSelf(employee); //TODO: limit to some role

        WsEmployee wsEmployee = toWsEmployeeConverter.convert(employee);

        return wsEmployee;
    }

    @Path("{id}")
    @Valid
    @PUT
    public WsEmployeeRef saveEmployee(@PathParam("id") long id, @Valid WsEmployee wsEmployee) {
        idChecker.checkId(id, wsEmployee);

        Employee existingEmployee = employeeService.findEmployeeById(id);
        accessChecker.checkSelf(existingEmployee); //TODO: limit to some role
        Employee employee = fromWsEmployeeConverter.patch(existingEmployee, wsEmployee);

        Employee savedEmployee = employeeService.saveEmployee(employee);
        WsEmployeeRef employeeRef = toWsEmployeeConverter.reference(savedEmployee);

        return employeeRef;
    }

    @Valid
    @POST
    @Path("/search")
    public List<WsEmployee> findEmployees(@Valid WsEmployeeSearch wsEmployeeSearch) {
        EmployeeSearch employeeSearch = fromWsEmployeeSearchConverter.convert(wsEmployeeSearch);
        accessChecker.checkOwnCompany(employeeSearch);
        List<Employee> employees = employeeService.findEmployees(employeeSearch);

        List<WsEmployee> wsEmployees = employees.stream()
                .map(toWsEmployeeConverter::convert)
                .collect(Collectors.toList());

        return wsEmployees;
    }

    @Path("/{employeeId}/password/{password}")
    @PUT
    public void setPassword(@PathParam("employeeId") long employeeId, @PathParam("password") String password) {
        Employee employee = employeeService.findEmployeeById(employeeId);
        accessChecker.checkSelf(employee); //TODO: limit to some role
        employeeService.setPassword(employee, password);
    }

}
