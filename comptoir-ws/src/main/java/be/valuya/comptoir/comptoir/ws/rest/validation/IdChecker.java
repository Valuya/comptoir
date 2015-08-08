package be.valuya.comptoir.comptoir.ws.rest.validation;

import be.valuya.comptoir.api.domain.company.WithId;
import javax.enterprise.context.ApplicationScoped;

/**
 *
 * @author Yannick Majoros <yannick@valuya.be>
 */
@ApplicationScoped
public class IdChecker {

    public boolean checkId(WithId givenRef, WithId withId) {
        long givenId = givenRef.getId();
        return checkId(givenId, withId);
    }

    public boolean checkId(long expectedId, WithId withId) {
        Long id = withId.getId();
        return id == expectedId;
    }

}
