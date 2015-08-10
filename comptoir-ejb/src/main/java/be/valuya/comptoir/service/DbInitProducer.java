package be.valuya.comptoir.service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.sql.DataSource;
import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.LiquibaseException;
import liquibase.resource.ClassLoaderResourceAccessor;

/**
 *
 * A Simple CDI Producer to configure the CDI Liquibase integration
 *
 * @author Yannick Majoros <yannick@valuya.be>
 */
@Singleton
@Startup
public class DbInitProducer {

    private static final Logger LOG = Logger.getLogger(DbInitProducer.class.getName());

    @Resource(lookup = "jdbc/comptoir")
    private DataSource dataSource;

    @PostConstruct
    public void run() {
        try {
            LOG.info("Starting liquibase...");
            Connection connection = dataSource.getConnection();
            JdbcConnection jdbcConnection = new JdbcConnection(connection);
            Database database = DatabaseFactory.getInstance().findCorrectDatabaseImplementation(jdbcConnection);
            ClassLoaderResourceAccessor classLoaderResourceAccessor = new ClassLoaderResourceAccessor();

            Liquibase liquibase = new Liquibase("db.changelog.xml", classLoaderResourceAccessor, database);
            liquibase.update("");
            LOG.info("Liquibase done.");
        } catch (SQLException | LiquibaseException exception) {
            throw new RuntimeException(exception);
        }
    }

}
