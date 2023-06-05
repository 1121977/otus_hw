package ru.otus.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;
import ru.otus.core.dao.AddressDataSetDao;
import ru.otus.core.dao.ClientDao;
import ru.otus.core.model.AddressDataSet;
import ru.otus.core.model.Client;
import ru.otus.core.model.PhoneData;

import java.util.*;

@Controller
public class ClientController {

    private final ClientDao clientDao;
//    private final AddressDataSetDao addressDataSetDao;

//    public ClientController(ClientDao clientDao, AddressDataSetDao addressDataSetDao){
public ClientController(ClientDao clientDao){
        this.clientDao = clientDao;
//        this.addressDataSetDao = addressDataSetDao;
    }

    @GetMapping({"/", "/client/list"})
    public String clientsListView(Model model) {
        List<Client> clients = clientDao.findAll();
        model.addAttribute("clients", clients);
        return "clientsList.html";
    }

    @GetMapping("/client/create")
    public String clientCreateView(Model model) {
        model.addAttribute("client", new Client());
        model.addAttribute("addressDataSet", new AddressDataSet());
        model.addAttribute("phones", new PhoneData(""));
        return "clientCreate.html";
    }

    @PostMapping("/client/save")
    public RedirectView clientSave(@ModelAttribute Client client, @ModelAttribute AddressDataSet addressDataSet, @RequestParam HashMap<String, String> reqMap) {
        reqMap.entrySet().stream().filter(note -> note.getKey().startsWith("phone"))
                .forEach(note -> client.getPhoneDataSet().add(new PhoneData(note.getValue())));
        client.getPhoneDataSet().stream().forEach(phoneData -> phoneData.setClient(client));
        addressDataSet.setClient(client);
        client.setAddress(addressDataSet);
        clientDao.update(client);
        return new RedirectView("/", true);
    }

    @GetMapping("/client/{clientID}")
    public String clientEditView(Model model, @PathVariable Long clientID) {
        Client client = clientDao.findById(clientID).get();
        model.addAttribute("client", client);
        model.addAttribute("addressDataSet", client.getAddress());
        model.addAttribute("phones", client.getPhoneDataSet());
        return "clientCreate.html";
    }

    @PostMapping("/client/delete")
    public RedirectView clientDelete(@ModelAttribute Client client) {
        clientDao.delete(client);
        return new RedirectView("/", true);
    }

}
