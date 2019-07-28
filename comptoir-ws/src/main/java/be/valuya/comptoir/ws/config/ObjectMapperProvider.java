package be.valuya.comptoir.ws.config;

import com.fasterxml.jackson.databind.ObjectMapper;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;

@ApplicationScoped
public class ObjectMapperProvider {

    @Produces
    private ObjectMapper createObjectMapper() {
        return new ObjectMapper();
    }
}
