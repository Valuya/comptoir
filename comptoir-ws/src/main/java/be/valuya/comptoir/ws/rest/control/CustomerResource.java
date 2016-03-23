package be.valuya.comptoir.ws.rest.control;

import be.valuya.comptoir.api.domain.search.WsCustomerSearch;
import be.valuya.comptoir.api.domain.thirdparty.WsCustomer;
import be.valuya.comptoir.api.domain.thirdparty.WsCustomerRef;
import be.valuya.comptoir.model.search.CustomerSearch;
import be.valuya.comptoir.model.thirdparty.Customer;
import be.valuya.comptoir.service.CustomerService;
import be.valuya.comptoir.ws.convert.search.FromWsCustomerSearchConverter;
import be.valuya.comptoir.ws.convert.thirdparty.FromWsCustomerConverter;
import be.valuya.comptoir.ws.convert.thirdparty.ToWsCustomerConverter;
import be.valuya.comptoir.ws.rest.validation.IdChecker;
import be.valuya.comptoir.ws.rest.validation.NoId;

import javax.ejb.EJB;
import javax.inject.Inject;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;
import java.util.stream.Collectors;

/**
 *
 * @author Yannick Majoros <yannick@valuya.be>
 */
@Path("/customer")
@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public class CustomerResource {

    @EJB
    private CustomerService customerService;
    @Inject
    private FromWsCustomerConverter fromWsCustomerConverter;
    @Inject
    private ToWsCustomerConverter toWsCustomerConverter;
    @Inject
    private IdChecker idChecker;
    @Inject
    private FromWsCustomerSearchConverter fromWsCustomerSearchConverter;

    @POST
    public WsCustomerRef createCustomer(@NoId WsCustomer wsCustomer) {
        Customer customer = fromWsCustomerConverter.convert(wsCustomer);
        Customer savedCustomer = customerService.saveCustomer(customer);

        WsCustomerRef customerRef = toWsCustomerConverter.reference(savedCustomer);

        return customerRef;
    }

    @Path("{id}")
    @Valid
    @PUT
    public WsCustomerRef saveCustomer(@PathParam("id") long id, @Valid WsCustomer wsCustomer) {
        idChecker.checkId(id, wsCustomer);

        Customer existingCustomer = customerService.findCustomerById(id);
        Customer customer = fromWsCustomerConverter.patch(existingCustomer, wsCustomer);

        Customer savedCustomer = customerService.saveCustomer(customer);
        WsCustomerRef customerRef = toWsCustomerConverter.reference(savedCustomer);

        return customerRef;
    }

    @Path("{id}")
    @Valid
    @GET
    public WsCustomer getCustomer(@PathParam("id") long id) {
        Customer customer = customerService.findCustomerById(id);

        WsCustomer wsCustomer = toWsCustomerConverter.convert(customer);

        return wsCustomer;
    }

    @Valid
    @POST
    @Path("/search")
    public List<WsCustomer> findCustomers(@Valid WsCustomerSearch wsCustomerSearch) {
        CustomerSearch customerSearch = fromWsCustomerSearchConverter.convert(wsCustomerSearch);
        List<Customer> customers = customerService.findCustomers(customerSearch);

        List<WsCustomer> wsCustomers = customers.stream()
                .map(toWsCustomerConverter::convert)
                .collect(Collectors.toList());

        return wsCustomers;
    }

}
