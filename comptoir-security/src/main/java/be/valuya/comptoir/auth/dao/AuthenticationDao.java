package be.valuya.comptoir.auth.dao;

import be.valuya.comptoir.auth.domain.EmployeePrincipal;
import be.valuya.comptoir.auth.glassfish.ComptoirRealmOptions;
import com.sun.enterprise.connectors.ConnectorRuntime;

import javax.naming.NamingException;
import javax.sql.DataSource;
import java.sql.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by cghislai on 12/02/17.
 */
public class AuthenticationDao implements ComptoirAuthenticationDao {
    private final static Logger LOG = Logger.getLogger(AuthenticationDao.class.getCanonicalName());
    private ComptoirRealmOptions options;

    @Override
    public void setConfig(ComptoirRealmOptions realmOptions) {
        this.options = realmOptions;
    }

    @Override
    public Optional<Long> findEmployeeWithLogin(String login) {
        try (Connection connection = this.getConnection()) {
            String sql = "SELECT ID FROM employee WHERE LOGIN = ?";
            this.debugSql(sql);

            PreparedStatement statement = connection.prepareStatement(sql);

            statement.setString(1, login);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                long id = resultSet.getLong(1);
                return Optional.of(id);
            } else {
                return Optional.empty();
            }
        } catch (SQLException e) {
            LOG.log(Level.SEVERE, "Failed to find employee login", e);
            return Optional.empty();
        }
    }


    @Override
    public boolean checkEmployeePassword(long id, char[] password) {
        try (Connection connection = this.getConnection()) {
            String sql = "SELECT password_hash FROM employee WHERE ID = ?";
            this.debugSql(sql);

            PreparedStatement statement = connection.prepareStatement(sql);

            statement.setLong(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                String dbPassword = resultSet.getString(1);
                return Arrays.equals(dbPassword.toCharArray(), password);
            } else {
                LOG.log(Level.SEVERE, "Failed to fetch employee password");
                return false;
            }
        } catch (SQLException e) {
            LOG.log(Level.SEVERE, "Failed to check employee password", e);
            return false;
        }
    }

    @Override
    public EmployeePrincipal fetchEmployeeInfo(long employeeId) {
        boolean employeeActive = this.isEmployeeActive(employeeId);
        long companyId = this.findEmployeeCompany(employeeId);
        String login = this.findEmployeeLogin(employeeId);
        EmployeePrincipal employeePrincipal = new EmployeePrincipal();
        employeePrincipal.setEmployeeId(employeeId);
        employeePrincipal.setActive(employeeActive);
        employeePrincipal.setName(login);
        employeePrincipal.setLogin(login);
        employeePrincipal.setCompanyId(companyId);
        return employeePrincipal;
    }

    @Override
    public Optional<Long> findAuthWithToken(String token) {
        try (Connection connection = this.getConnection()) {
            String sql = "SELECT id FROM auth WHERE token = ? AND expiration > ?";
            this.debugSql(sql);

            PreparedStatement statement = connection.prepareStatement(sql);

            statement.setString(1, token);
            statement.setTimestamp(2, Timestamp.from(Instant.now()));
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                Long authId = resultSet.getLong(1);
                return Optional.of(authId);
            } else {
                LOG.log(Level.SEVERE, "Failed to fetch auth for token");
                return Optional.empty();
            }
        } catch (SQLException e) {
            LOG.log(Level.SEVERE, "Failed to fetch auth for token", e);
            return Optional.empty();
        }
    }

    @Override
    public Long findEmployeeIdForAuthId(long authId) {
        try (Connection connection = this.getConnection()) {
            String sql = "SELECT employee_id FROM auth WHERE id = ?";
            this.debugSql(sql);

            PreparedStatement statement = connection.prepareStatement(sql);

            statement.setLong(1, authId);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                Long employeeId = resultSet.getLong(1);
                return employeeId;
            } else {
                LOG.log(Level.SEVERE, "Failed to fetch employee from auth ");
                return null;
            }
        } catch (SQLException e) {
            LOG.log(Level.SEVERE, "Failed to fetch employee from auth ", e);
            return null;
        }
    }

    @Override
    public String findEmployeeLogin(long id) {
        try (Connection connection = this.getConnection()) {
            String sql = "SELECT LOGIN FROM employee WHERE ID = ?";
            this.debugSql(sql);

            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setLong(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                String login = resultSet.getString(1);
                return login;
            } else {
                LOG.log(Level.SEVERE, "Failed to fetch employee login");
                return null;
            }
        } catch (SQLException e) {
            LOG.log(Level.SEVERE, "Failed to fetch employee login", e);
            return null;
        }
    }

    private boolean isEmployeeActive(long id) {
        try (Connection connection = this.getConnection()) {
            String sql = "SELECT ACTIVE FROM employee WHERE ID = ?";
            this.debugSql(sql);

            PreparedStatement statement = connection.prepareStatement(sql);

            statement.setLong(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                boolean active = resultSet.getBoolean(1);
                return active;
            } else {
                LOG.log(Level.SEVERE, "Failed to check employee is active");
                return false;
            }
        } catch (SQLException e) {
            LOG.log(Level.SEVERE, "Failed to find employee login", e);
            return false;
        }
    }

    private Long findEmployeeCompany(long employeeId) {
        try (Connection connection = this.getConnection()) {
            String sql = "SELECT COMPANY_ID FROM employee WHERE ID = ?";
            this.debugSql(sql);

            PreparedStatement statement = connection.prepareStatement(sql);

            statement.setLong(1, employeeId);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                long companyId = resultSet.getLong(1);
                return companyId;
            } else {
                LOG.log(Level.SEVERE, "Failed to check employee is active");
                return null;
            }
        } catch (SQLException e) {
            LOG.log(Level.SEVERE, "Failed to find employee login", e);
            return null;
        }
    }

    private Connection getConnection() {
        final String dsJndi = options.getDataSourceJndi();
        try {
            final DataSource dataSource = (DataSource) ConnectorRuntime.getRuntime().lookupNonTxResource(dsJndi, false);

            Connection connection = dataSource.getConnection();
            return connection;
        } catch (NamingException exception) {
            throw new IllegalStateException("Cannot find name " + dsJndi);
        } catch (SQLException exception) {
            throw new IllegalStateException("Failed to create a connection to " + dsJndi);
        }
    }

    private void debugSql(String sql) {
        if (this.options.isDebug() && LOG.isLoggable(Level.FINE)) {
            LOG.fine("Running sql: " + sql);
        }
    }
}
