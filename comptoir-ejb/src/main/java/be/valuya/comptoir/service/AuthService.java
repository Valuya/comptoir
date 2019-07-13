package be.valuya.comptoir.service;

import be.valuya.comptoir.model.auth.Auth;
import be.valuya.comptoir.model.auth.Auth_;
import be.valuya.comptoir.model.thirdparty.Employee;
import be.valuya.comptoir.security.ComptoirPrincipal;
import be.valuya.comptoir.security.ComptoirRoles;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.security.enterprise.identitystore.CredentialValidationResult;
import javax.security.enterprise.identitystore.Pbkdf2PasswordHash;
import javax.ws.rs.NotAuthorizedException;

/**
 * @author Yannick Majoros <yannick@valuya.be>
 */
@Stateless
public class AuthService {

    @PersistenceContext
    private EntityManager entityManager;
    @Inject
    private Pbkdf2PasswordHash pbkdf2Hash;


    public CredentialValidationResult validateAuth(Employee employee, String passwordHash) {
        String expectedPasswordHash = employee.getPasswordHash();
        // TODO: salt
        if (expectedPasswordHash == null || passwordHash == null || !passwordHash.equals(expectedPasswordHash)) {
            return CredentialValidationResult.INVALID_RESULT;
        }
        Auth employeeAuth = createEmployeeAuth(employee);
        return createAuthCredentialValidationResult(employeeAuth);
    }


    public CredentialValidationResult validateTokenAuth(String token) {
        return findAuthFromToken(token)
                .filter(this::isValidAuth)
                .map(this::createAuthCredentialValidationResult)
                .orElse(CredentialValidationResult.INVALID_RESULT);
    }


    public Auth refreshAuth(String refreshToken) {
        String token = createToken();

        Auth auth = findAuthFromRefreshToken(refreshToken);
        auth.setToken(token);

        setAuthExpiration(auth);

        return auth;
    }


    public String hashPassword(String password) {
        try {
            // FIXME: use stronger hash
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.reset();
            messageDigest.update(password.getBytes(StandardCharsets.UTF_8));
            byte[] digest = messageDigest.digest();
            BigInteger bigInteger = new BigInteger(1, digest);
            String hashedPassword = bigInteger.toString(16);

            // Now we need to zero pad it if you actually want the full 32 chars.
            while (hashedPassword.length() < 32) {
                hashedPassword = "0" + hashedPassword;
            }

            return hashedPassword;
        } catch (NoSuchAlgorithmException exception) {
            throw new AssertionError(exception);
        }
    }

    private CredentialValidationResult createAuthCredentialValidationResult(Auth auth) {
        Set<String> groups = getEmployeeGroups(auth.getEmployee());
        ComptoirPrincipal comptoirPrincipal = new ComptoirPrincipal(auth);
        return new CredentialValidationResult(comptoirPrincipal, groups);
    }

    private Set<String> getEmployeeGroups(Employee employee) {
        Set<String> groups = new HashSet<>();
        groups.add(ComptoirRoles.EMPLOYEE);
        if (employee.isActive()) {
            groups.add(ComptoirRoles.ACTIVE);
        }
        return groups;
    }

    private boolean isValidAuth(Auth auth) {
        ZonedDateTime nowDateTime = ZonedDateTime.now();
        ZonedDateTime expirationDateTime = auth.getExpirationDateTime();
        return expirationDateTime != null && expirationDateTime.isAfter(nowDateTime);
    }

    private Optional<Auth> findAuthFromToken(String token) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Auth> query = criteriaBuilder.createQuery(Auth.class);
        Root<Auth> authRoot = query.from(Auth.class);

        Path<String> tokenPath = authRoot.get(Auth_.token);
        Predicate tokenPredicate = criteriaBuilder.equal(tokenPath, token);

        query.where(tokenPredicate);

        TypedQuery<Auth> typedQuery = entityManager.createQuery(query);
        try {
            Auth auth = typedQuery.getSingleResult();

            return Optional.of(auth);
        } catch (NoResultException noResultException) {
            return Optional.empty();
        }
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

    private Auth createEmployeeAuth(Employee employee) {
        String token = createToken();
        String refreshToken = createToken();

        Auth auth = new Auth();
        auth.setEmployee(employee);
        auth.setToken(token);
        auth.setRefreshToken(refreshToken);

        setAuthExpiration(auth);

        return entityManager.merge(auth);
    }


    private String createToken() {
        return UUID.randomUUID().toString();
    }

    private void setAuthExpiration(Auth auth) {
        ZonedDateTime nowDateTime = ZonedDateTime.now();
        ZonedDateTime expirationDateTime = nowDateTime.plusMinutes(30);
        auth.setExpirationDateTime(expirationDateTime);
    }

}
