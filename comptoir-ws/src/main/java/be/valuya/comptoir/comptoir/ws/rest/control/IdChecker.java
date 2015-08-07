package be.valuya.comptoir.comptoir.ws.rest.control;

import be.valuya.comptoir.api.domain.company.WithId;
import javax.enterprise.context.ApplicationScoped;

/**
 *
 * @author Yannick Majoros <yannick@valuya.be>
 */
@ApplicationScoped
public class IdChecker {

    public boolean checkId(long expectedId, WithId withId) {
        Long id = withId.getId();
        return id == expectedId;
    }

}
