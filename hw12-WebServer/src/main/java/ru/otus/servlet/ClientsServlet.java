package ru.otus.servlet;

import ru.otus.core.model.AddressDataSet;
import ru.otus.core.model.Client;
import ru.otus.core.model.PhoneData;
import ru.otus.core.service.DBServiceClient;
import ru.otus.dao.UserDao;
import ru.otus.services.TemplateProcessor;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ClientsServlet extends HttpServlet {

    private static final String CLIENTS_PAGE_TEMPLATE = "clients.html";
    private static final String TEMPLATE_ALL_CLIENTS = "allClients";

    private final DBServiceClient dbServiceClient;
    private final TemplateProcessor templateProcessor;

    public ClientsServlet(TemplateProcessor templateProcessor, DBServiceClient dbServiceClient) {
        this.templateProcessor = templateProcessor;
        this.dbServiceClient = dbServiceClient;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse response) throws IOException {
        Map<String, Object> paramsMap = new HashMap<>();
//        dbServiceClient.findRandomUser().ifPresent(randomUser -> paramsMap.put(TEMPLATE_ALL_CLIENTS, randomUser));
        paramsMap.put(TEMPLATE_ALL_CLIENTS, dbServiceClient.findAll().toString());
/*        List<Client> clientList = new ArrayList<>();
        Client client = new Client("Ivan");
        AddressDataSet someAdddressData = new AddressDataSet("Some Address");
        client.setAddress(someAdddressData);
        client.addPhoneData(new PhoneData("88005005005"));
        clientList.add(client);
        paramsMap.put(TEMPLATE_ALL_CLIENTS, clientList.toString());*/
        response.setContentType("text/html");
        response.getWriter().println(templateProcessor.getPage(CLIENTS_PAGE_TEMPLATE, paramsMap));
    }

}
