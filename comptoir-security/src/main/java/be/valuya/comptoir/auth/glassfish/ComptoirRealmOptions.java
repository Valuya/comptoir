package be.valuya.comptoir.auth.glassfish;

import javax.jms.IllegalStateException;
import java.util.Properties;

/**
 * Created by cghislai on 12/02/17.
 */
public class ComptoirRealmOptions {
    public static final String PARAM_DATASOURCE_JNDI = "datasource-jndi";
    public static final String PARAM_DEBUG = "debug";

    private String dataSourceJndi;
    private boolean debug;

    ComptoirRealmOptions(Properties properties) throws IllegalStateException {
        this.dataSourceJndi = properties.getProperty(PARAM_DATASOURCE_JNDI);
        this.debug = properties.getProperty(PARAM_DEBUG, "false").equals("true");

        if (this.dataSourceJndi == null) {
            throw new IllegalStateException("Invalid options");
        }
    }

    public String getDataSourceJndi() {
        return dataSourceJndi;
    }

    public void setDataSourceJndi(String dataSourceJndi) {
        this.dataSourceJndi = dataSourceJndi;
    }

    public boolean isDebug() {
        return debug;
    }

    public void setDebug(boolean debug) {
        this.debug = debug;
    }
}
