package be.valuya.comptoir.security;

import be.valuya.comptoir.model.auth.Auth;

import javax.security.enterprise.CallerPrincipal;

public class ComptoirPrincipal extends CallerPrincipal {

    private Auth auth;

    public ComptoirPrincipal(Auth auth) {
        super(auth.getEmployee().getLogin());
        this.auth = auth;
    }

    public Auth getAuth() {
        return auth;
    }
}
