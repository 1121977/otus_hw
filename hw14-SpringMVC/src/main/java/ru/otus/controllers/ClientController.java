package ru.otus.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.view.RedirectView;
import ru.otus.core.model.Client;
import ru.otus.core.model.PhoneData;
import ru.otus.core.service.DBServiceClient;

import java.util.List;

@Controller
public class ClientController {

    private final DBServiceClient clientService;

    public ClientController(DBServiceClient clientService) {
        this.clientService = clientService;
    }

    @GetMapping({"/", "/client/list"})
    public String clientsListView(Model model) {
        List<Client> clients = clientService.findAll();
        model.addAttribute("clients", clients);
        return "clientsList.html";
    }

    @GetMapping("/client/create")
    public String clientCreateView(Model model) {
        model.addAttribute("client", new Client());
        return "clientCreate.html";
    }

    @GetMapping("/phoneNumber/create")
    public String phoneNumberCreateView(Model model) {
        model.addAttribute("phoneNumber", new PhoneData());
        return "phoneNumberCreate.html";
    }


    @PostMapping("/client/save")
    public RedirectView clientSave(@ModelAttribute Client client) {
        clientService.saveClient(client);
        return new RedirectView("/", true);
    }

    @PostMapping("/phoneNumber/save")
    public RedirectView phoneNumberSave(@ModelAttribute PhoneData phoneData) {
//        clientService.saveClient(client);
        return new RedirectView("/", true);
    }


}
