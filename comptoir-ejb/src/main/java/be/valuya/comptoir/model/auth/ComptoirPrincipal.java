package be.valuya.comptoir.model.auth;

import java.security.Principal;

/**
 * Created by cghislai on 12/02/17.
 */
public class ComptoirPrincipal implements Principal {
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "ComptoirPrincipal{" +
                getClass().getSimpleName() + " " +
                "name='" + name + '\'' +
                '}';
    }
}
