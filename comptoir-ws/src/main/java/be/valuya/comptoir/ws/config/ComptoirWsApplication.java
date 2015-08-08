package be.valuya.comptoir.ws.config;

import be.valuya.comptoir.ws.ComptoirWsRoot;
import javax.ws.rs.ApplicationPath;
import org.glassfish.jersey.server.ResourceConfig;

/**
 *
 * @author Yannick Majoros <yannick@valuya.be>
 */
@ApplicationPath("/")
public class ComptoirWsApplication extends ResourceConfig {

    public ComptoirWsApplication() {
        packages(true, ComptoirWsRoot.class.getPackage().getName());
    }

}
