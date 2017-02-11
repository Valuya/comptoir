package be.valuya.comptoir.ws.config;

import be.valuya.comptoir.ws.ComptoirWsRoot;
import be.valuya.wsfilter.CrossOriginResourceSharingRequestFilter;
import be.valuya.wsfilter.CrossOriginResourceSharingResponseFilter;
import org.glassfish.jersey.server.ResourceConfig;

import javax.ws.rs.ApplicationPath;

/**
 * @author Yannick Majoros <yannick@valuya.be>
 */
@ApplicationPath("/")
public class ComptoirWsApplication extends ResourceConfig {

    public ComptoirWsApplication() {
        packages(true, ComptoirWsRoot.class.getPackage().getName());
        register(CrossOriginResourceSharingRequestFilter.class);
        register(CrossOriginResourceSharingResponseFilter.class);
    }

}
