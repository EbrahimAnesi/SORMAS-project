package de.symeda.sormas.ui;


import fish.payara.security.openid.api.OpenIdContext;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/callback")
public class KeycloakCallbackServlet extends HttpServlet {

//    @Inject
    OpenIdContext context;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//        System.out.println(context.getCallerName());
//        System.out.println(context.getCallerGroups());
//        System.out.println(context.getSubject());
//        System.out.println(context.getAccessToken());
//        System.out.println(context.getIdentityToken());
//        System.out.println(context.getClaimsJson());

        System.out.println(request.getParameter("code"));

        response.addCookie(request.getCookies()[0]);
        response.sendRedirect("http://localhost:8080/sormas-ui");
    }
}
