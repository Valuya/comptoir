package be.valuya.comptoir.util;

import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;
import java.lang.reflect.Member;
import java.util.logging.Logger;

public class LoggerProducer {

    @Produces
    public Logger produceLogger(InjectionPoint injectionPoint) {
        Member member = injectionPoint.getMember();
        Class<?> declaringClass = member.getDeclaringClass();
        String className = declaringClass.getName();
        return Logger.getLogger(className);
    }
}
