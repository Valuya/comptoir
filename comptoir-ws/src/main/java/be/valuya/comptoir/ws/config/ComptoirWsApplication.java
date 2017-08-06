package be.valuya.comptoir.ws.config;

import be.valuya.comptoir.ws.ComptoirWsRoot;
import be.valuya.wsfilter.CrossOriginResourceSharingResponseFilter;
import be.valuya.wsfilter.WsExceptionMapper;
import org.glassfish.jersey.message.filtering.SecurityEntityFilteringFeature;
import org.glassfish.jersey.server.ResourceConfig;

import javax.ws.rs.ApplicationPath;

/**
 * @author Yannick Majoros <yannick@valuya.be>
 */
@ApplicationPath("/")
public class ComptoirWsApplication extends ResourceConfig {

    public ComptoirWsApplication() {
        packages(true, ComptoirWsRoot.class.getPackage().getName());
        register(CrossOriginResourceSharingResponseFilter.class);
        register(WsExceptionMapper.class);
        register(SecurityEntityFilteringFeature.class);
    }

}
