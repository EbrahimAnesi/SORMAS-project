package de.symeda.sormas.ui.login;

import com.vaadin.server.Page;
import com.vaadin.server.RequestHandler;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinResponse;
import com.vaadin.server.VaadinServletResponse;
import com.vaadin.server.VaadinSession;

import java.io.IOException;

public class LoginHandler implements RequestHandler {
    @Override
    public boolean handleRequest(VaadinSession vaadinSession, VaadinRequest vaadinRequest,
                                 VaadinResponse vaadinResponse) throws IOException {

        VaadinSession.getCurrent().setAttribute("principal", vaadinRequest.getUserPrincipal());

        ((VaadinServletResponse) vaadinResponse).getHttpServletResponse().
                sendRedirect(Page.getCurrent().getLocation().toString());

        return true;
    }
}
