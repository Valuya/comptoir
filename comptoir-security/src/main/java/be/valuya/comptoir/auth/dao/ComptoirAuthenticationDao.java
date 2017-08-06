package be.valuya.comptoir.auth.dao;

import be.valuya.comptoir.auth.domain.EmployeePrincipal;
import be.valuya.comptoir.auth.glassfish.ComptoirRealmOptions;

import java.util.Optional;

/**
 * Created by cghislai on 12/02/17.
 */
public interface ComptoirAuthenticationDao {

    void setConfig(ComptoirRealmOptions realmOptions);

    Optional<Long> findEmployeeWithLogin(String login);

    boolean checkEmployeePassword(long id, char[] password);

    EmployeePrincipal fetchEmployeeInfo(long employeeId);

    Optional<Long> findAuthWithToken(String token);

    Long findEmployeeIdForAuthId(long authId);

    String findEmployeeLogin(long id);

}
