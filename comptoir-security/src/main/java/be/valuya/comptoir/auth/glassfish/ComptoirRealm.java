package be.valuya.comptoir.auth.glassfish;

import be.valuya.comptoir.auth.dao.AuthenticationDao;
import be.valuya.comptoir.auth.dao.ComptoirAuthenticationDao;
import be.valuya.comptoir.auth.domain.ComptoirPrincipal;
import be.valuya.comptoir.auth.domain.EmployeePrincipal;
import com.sun.enterprise.security.BaseRealm;
import com.sun.enterprise.security.auth.realm.BadRealmException;
import com.sun.enterprise.security.auth.realm.IASRealm;
import com.sun.enterprise.security.auth.realm.InvalidOperationException;
import com.sun.enterprise.security.auth.realm.NoSuchRealmException;
import com.sun.enterprise.security.auth.realm.NoSuchUserException;

import javax.jms.IllegalStateException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;
import java.util.logging.Logger;

/**
 * Created by cghislai on 12/02/17.
 */
public class ComptoirRealm extends BaseRealm {
    private final static Logger LOG = Logger.getLogger(ComptoirRealm.class.getCanonicalName());
    public static final String AUTH_TYPE = "comptoir";
    private ComptoirRealmOptions options;
    private ComptoirAuthenticationDao authenticationDao;

    @Override
    protected void init(Properties props) throws BadRealmException, NoSuchRealmException {
        super.init(props);
        try {
            this.checkJaasContext(props);
            this.options = new ComptoirRealmOptions(props);
            this.authenticationDao = new AuthenticationDao();
            this.authenticationDao.setConfig(options);
        } catch (IllegalStateException e) {
            throw new BadRealmException(e);
        }
    }


    public ComptoirPrincipal authenticateUserLoginPassword(String login, char[] password) {
        if (login == null || password == null) {
            return null;
        }
        EmployeePrincipal authenticatedEmployee = this.authenticationDao.findEmployeeWithLogin(login)
                .filter(id -> this.authenticationDao.checkEmployeePassword(id, password))
                .map(id -> this.authenticationDao.fetchEmployeeInfo(id))
                .orElse(null);
        return authenticatedEmployee;
    }

    public ComptoirPrincipal authenticateUserToken(String token) {
        if (token == null) {
            return null;
        }
        EmployeePrincipal authenticatedEmployee = this.authenticationDao.findAuthWithToken(token)
                .map(authId -> this.authenticationDao.findEmployeeIdForAuthId(authId))
                .map(id -> this.authenticationDao.fetchEmployeeInfo(id))
                .orElse(null);
        return authenticatedEmployee;
    }


    public String[] fetchGroups(String userName) {
        return this.authenticationDao.findEmployeeWithLogin(userName)
                .map(this.authenticationDao::fetchEmployeeInfo)
                .map(this::getEmployeeGroups)
                .orElse(new String[0]);
    }

    public String[] getEmployeeGroups(EmployeePrincipal employeePrincipal) {
        List<String> groups = new ArrayList<>();
        groups.add("employee");
        if (employeePrincipal.isActive()) {
            groups.add("active");
        }
        return groups.toArray(new String[0]);
    }

    @Override
    public String getAuthType() {
        return AUTH_TYPE;
    }

    @Override
    public Enumeration getGroupNames(String username) throws InvalidOperationException, NoSuchUserException {
        System.out.println("Get group name");
        return Collections.enumeration(Arrays.asList(this.fetchGroups(username)));
    }

    private void checkJaasContext(Properties props) {
        String jaasContextProperty = props.getProperty(IASRealm.JAAS_CONTEXT_PARAM);
        if (jaasContextProperty == null) {
            throw new java.lang.IllegalStateException("Missing jaas-context");
        }
        this.setProperty(IASRealm.JAAS_CONTEXT_PARAM, jaasContextProperty);
    }

}
