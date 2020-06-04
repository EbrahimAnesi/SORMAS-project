package de.symeda.sormas.ui;


import fish.payara.security.annotations.ClaimsDefinition;
import fish.payara.security.annotations.OpenIdAuthenticationDefinition;

import javax.enterprise.context.ApplicationScoped;
import javax.security.enterprise.authentication.mechanism.http.CustomFormAuthenticationMechanismDefinition;
import javax.security.enterprise.authentication.mechanism.http.LoginToContinue;

//@ApplicationScoped
//@CustomFormAuthenticationMechanismDefinition(loginToContinue = @LoginToContinue(useForwardToLogin = false, loginPage = "/login"))
@ApplicationScoped
@OpenIdAuthenticationDefinition(
        providerURI = "http://localhost:8085/auth/realms/sormas-realm/", // replace 'sormas-realm' with your realm
        clientId = "sormas-ui",                                          // replace with your client id
        clientSecret = "976a224e-cd0b-4dbe-a61b-40cce8bf518a",           // replace with your keycloak client secret
        redirectURI = "http://localhost:8080/sormas-ui/callback",
        scope = {"openid"},
        claimsDefinition = @ClaimsDefinition())
public class ApplicationSecurityConfig {

}
