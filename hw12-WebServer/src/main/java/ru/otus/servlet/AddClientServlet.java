package ru.otus.servlet;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import ru.otus.core.model.Client;
import ru.otus.core.model.PhoneData;
import ru.otus.core.service.DBServiceClient;
import ru.otus.services.TemplateProcessor;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static jakarta.servlet.http.HttpServletResponse.SC_UNAUTHORIZED;

public class AddClientServlet extends HttpServlet {
    private static final String ADD_CLIENT_TEMPLATE = "addClient.html";
    private static final String TEMPLATE_ADD_CLIENT = "addClient";
    private final DBServiceClient dbServiceClient;
    private final TemplateProcessor templateProcessor;
    private static final String PARAM_NAME = "name";
    private static final String PARAM_PHONE = "phone";
    private static final String ADD_CLIENT_PAGE_TEMPLATE = "addClient.html";

    public AddClientServlet(TemplateProcessor templateProcessor, DBServiceClient dbServiceClient) {
        this.templateProcessor = templateProcessor;
        this.dbServiceClient = dbServiceClient;
    }
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse response) throws IOException {
        Map<String, Object> paramsMap = new HashMap<>();
        response.setContentType("text/html");
//        response.getWriter().println("Hello, world!");
        response.getWriter().println(templateProcessor.getPage(ADD_CLIENT_TEMPLATE, paramsMap));
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {

        String name = request.getParameter(PARAM_NAME);
        String phoneNumber = request.getParameter(PARAM_PHONE);
        Client newClient = new Client(name);
        PhoneData phoneData = new PhoneData(phoneNumber);
        newClient.addPhoneData(phoneData);
        dbServiceClient.saveClient(newClient);
        response.sendRedirect("/clients");

/*        if (userAuthService.authenticate(name, password)) {
            HttpSession session = request.getSession();
            session.setMaxInactiveInterval(MAX_INACTIVE_INTERVAL);
            response.sendRedirect("/users");
        } else {
            response.setStatus(SC_UNAUTHORIZED);
        }*/

    }

}
