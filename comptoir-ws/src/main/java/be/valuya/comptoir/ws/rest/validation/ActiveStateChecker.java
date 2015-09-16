/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package be.valuya.comptoir.ws.rest.validation;

import be.valuya.comptoir.model.common.Activable;
import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.BadRequestException;

/**
 *
 * @author cghislai
 */
@ApplicationScoped
public class ActiveStateChecker {

    public void checkState(Activable activable, boolean expectedActriveState) {
        boolean active = activable.isActive();
        if (active != expectedActriveState) {
            throw new BadRequestException("Unexpected active state");
        }
    }

}
