package org.netbeans.rest.application.config;

import java.util.Set;
import javax.ws.rs.core.Application;

/**
 *
 * @author Yannick Majoros <yannick@valuya.be>
 */
@javax.ws.rs.ApplicationPath("webresources")
public class ApplicationConfig extends Application {

    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> resources = new java.util.HashSet<>();
        addRestResourceClasses(resources);
        return resources;
    }

    /**
     * Do not modify addRestResourceClasses() method.
     * It is automatically populated with
     * all resources defined in the project.
     * If required, comment out calling this method in getClasses().
     */
    private void addRestResourceClasses(Set<Class<?>> resources) {
        resources.add(be.valuya.comptoir.comptoir.ws.rest.control.AccountResource.class);
        resources.add(be.valuya.comptoir.comptoir.ws.rest.control.CompanyResource.class);
        resources.add(be.valuya.comptoir.comptoir.ws.rest.control.ItemResource.class);
        resources.add(org.glassfish.jersey.server.wadl.internal.WadlResource.class);
    }

}
