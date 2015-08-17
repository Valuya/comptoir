package be.valuya.comptoir.service;

import be.valuya.comptoir.model.auth.Auth;
import be.valuya.comptoir.model.auth.Auth_;
import be.valuya.comptoir.model.thirdparty.Employee;
import java.time.ZonedDateTime;
import java.util.UUID;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.ws.rs.NotAuthorizedException;

/**
 *
 * @author Yannick Majoros <yannick@valuya.be>
 */
@Stateless
public class AuthService {

    @PersistenceContext
    private EntityManager entityManager;

    private static String createToken() {
        return UUID.randomUUID().toString();
    }

    public Auth login(Employee employee, String passwordHash) {
        checkAuth(employee, passwordHash);

        String token = createToken();
        String refreshToken = createToken();

        Auth auth = new Auth();
        auth.setEmployee(employee);
        auth.setToken(token);
        auth.setRefreshToken(refreshToken);

        setAuthExpiration(auth);

        return entityManager.merge(auth);
    }

    private void setAuthExpiration(Auth auth) {
        ZonedDateTime nowDateTime = ZonedDateTime.now();
        ZonedDateTime expirationDateTime = nowDateTime.plusMinutes(30);
        auth.setExpirationDateTime(expirationDateTime);
    }

    public void checkAuth(Employee employee, String passwordHash) throws NotAuthorizedException {
        String expectedPasswordHash = employee.getPasswordHash();
        // TODO: salt
        if (expectedPasswordHash == null || passwordHash == null || !passwordHash.equals(expectedPasswordHash)) {
            throw new NotAuthorizedException("Invalid login");
        }
    }

    public Auth checkAuth(String token) {
        Auth auth = findAuthFromToken(token);

        checkAuthExpiration(auth);

        return auth;
    }

    private void checkAuthExpiration(Auth auth) throws NotAuthorizedException {
        ZonedDateTime nowDateTime = ZonedDateTime.now();
        ZonedDateTime expirationDateTime = auth.getExpirationDateTime();
        if (expirationDateTime == null || expirationDateTime.isBefore(nowDateTime)) {
            throw new NotAuthorizedException("Authentication token has expired.");
        }
    }

    private Auth findAuthFromToken(String token) throws NotAuthorizedException {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Auth> query = criteriaBuilder.createQuery(Auth.class);
        Root<Auth> authRoot = query.from(Auth.class);

        Path<String> tokenPath = authRoot.get(Auth_.token);
        Predicate tokenPredicate = criteriaBuilder.equal(tokenPath, token);

        query.where(tokenPredicate);

        TypedQuery<Auth> typedQuery = entityManager.createQuery(query);
        try {
            Auth auth = typedQuery.getSingleResult();

            return auth;
        } catch (NoResultException noResultException) {
            throw new NotAuthorizedException("Refresh token not found");
        }
    }

    public Auth refreshAuth(String refreshToken) {
        String token = createToken();

        Auth auth = findAuthFromRefreshToken(refreshToken);
        auth.setToken(token);

        setAuthExpiration(auth);

        return auth;
    }

    private Auth findAuthFromRefreshToken(String refreshToken) throws NotAuthorizedException {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Auth> query = criteriaBuilder.createQuery(Auth.class);
        Root<Auth> authRoot = query.from(Auth.class);

        Path<String> refreshTokenPath = authRoot.get(Auth_.refreshToken);
        Predicate refreshTokenPredicate = criteriaBuilder.equal(refreshTokenPath, refreshToken);

        query.where(refreshTokenPredicate);

        TypedQuery<Auth> typedQuery = entityManager.createQuery(query);
        try {
            Auth auth = typedQuery.getSingleResult();
            return auth;
        } catch (NoResultException noResultException) {
            throw new NotAuthorizedException("Refresh token not found");
        }
    }

}
