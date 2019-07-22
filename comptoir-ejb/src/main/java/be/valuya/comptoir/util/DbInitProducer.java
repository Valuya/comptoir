package be.valuya.comptoir.util;


import be.valuya.comptoir.service.LiquibaseService;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;

/**
 * A Simple CDI Producer to configure the CDI Liquibase integration
 *
 * @author Yannick Majoros <yannick@valuya.be>
 */
@Singleton
@Startup
public class DbInitProducer {

    @Inject
    private LiquibaseService liquibaseService;


    @PostConstruct
    public void init() {
        liquibaseService.run("db/liquibase/main.xml");
    }


}
