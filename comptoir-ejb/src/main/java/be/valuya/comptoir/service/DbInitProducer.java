package be.valuya.comptoir.service;

import java.sql.SQLException;
import javax.annotation.Resource;
import javax.sql.DataSource;
import liquibase.integration.cdi.CDILiquibaseConfig;
import liquibase.integration.cdi.annotations.LiquibaseType;
import liquibase.resource.ClassLoaderResourceAccessor;
import liquibase.resource.ResourceAccessor;

/**
 *
 * A Simple CDI Producer to configure the CDI Liquibase integration
 *
 * @author Yannick Majoros <yannick@valuya.be>
 */
public class DbInitProducer {

    @Resource
    private DataSource myDataSource;

//    @Produces
    @LiquibaseType
    public CDILiquibaseConfig createConfig() {
        CDILiquibaseConfig config = new CDILiquibaseConfig();
        config.setChangeLog("liquibase/parser/core/xml/simpleChangeLog.xml");
        return config;
    }

//    @Produces
    @LiquibaseType
    public DataSource createDataSource() throws SQLException {
        return myDataSource;
    }

//    @Produces
    @LiquibaseType
    public ResourceAccessor create() {
        return new ClassLoaderResourceAccessor(getClass().getClassLoader());
    }

}
