package be.valuya.comptoir.ws.convert;

import be.valuya.comptoir.api.domain.company.WsCompanyRef;
import be.valuya.comptoir.ws.convert.company.WsCompanyParameterProvider;

import javax.ws.rs.ext.ParamConverter;
import javax.ws.rs.ext.ParamConverterProvider;
import javax.ws.rs.ext.Provider;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

/**
 * @author Yannick Majoros <yannick@valuya.be>
 */
@Provider
public class ComptoirWsParamConverterProvider implements ParamConverterProvider {

    @Override
    public <T> ParamConverter<T> getConverter(Class<T> rawType, Type genericType, Annotation[] annotations) {
        if (rawType.isAssignableFrom(WsCompanyRef.class)) {
            return (ParamConverter<T>) new WsCompanyParameterProvider();
        }
        return null;
    }

}
