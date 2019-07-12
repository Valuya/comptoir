package be.valuya.comptoir.service;

import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.LiquibaseException;
import liquibase.resource.ClassLoaderResourceAccessor;
import liquibase.resource.CompositeResourceAccessor;
import liquibase.resource.FileSystemResourceAccessor;
import liquibase.resource.ResourceAccessor;

import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Logger;

@Stateless
@TransactionManagement(TransactionManagementType.BEAN)
public class LiquibaseService {

    @Resource(lookup = "java:/jdbc/comptoir")
    private DataSource dataSource;

    public void run(String changelogFilePath) {
        try (Connection connection = dataSource.getConnection();) {
            JdbcConnection jdbcConnection = new JdbcConnection(connection);
            Database database = DatabaseFactory.getInstance().findCorrectDatabaseImplementation(jdbcConnection);

            ClassLoader classLoader = this.getClass().getClassLoader();
            ResourceAccessor classLoaderResourceAccessor = new ClassLoaderResourceAccessor(classLoader);
            ResourceAccessor fileSystemResourceAccessor = new FileSystemResourceAccessor();
            CompositeResourceAccessor resourceAccessor = new CompositeResourceAccessor(classLoaderResourceAccessor, fileSystemResourceAccessor);

            Liquibase liquibase = new Liquibase(changelogFilePath, resourceAccessor, database);
            liquibase.update("");
        } catch (SQLException | LiquibaseException exception) {
            throw new RuntimeException(exception);
        }
    }
}
