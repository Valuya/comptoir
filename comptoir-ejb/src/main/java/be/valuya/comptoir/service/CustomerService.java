package be.valuya.comptoir.service;

import be.valuya.comptoir.model.thirdparty.Customer;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Yannick Majoros <yannick@valuya.be>
 */
@Stateless
public class CustomerService {

    @PersistenceContext
    private EntityManager entityManager;

    public Customer findCustomerById(Long id) {
        return entityManager.find(Customer.class, id);
    }

}
