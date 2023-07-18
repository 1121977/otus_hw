package ru.otus.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;
import ru.otus.core.dao.ClientDao;
import ru.otus.core.model.AddressDataSet;
import ru.otus.core.model.Client;
import ru.otus.core.model.PhoneData;
import ru.otus.core.model.Role;
import java.util.*;
@Controller
public class ClientController {

    private final ClientDao clientDao;

    public ClientController(ClientDao clientDao) {
        this.clientDao = clientDao;
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
        model.addAttribute("role", new Role(""));
        return "clientCreate.html";
    }

    @PostMapping("/client/save")
    public RedirectView clientSave(@ModelAttribute Client clientFromForm, @ModelAttribute AddressDataSet addressDataSet, @ModelAttribute Role role, @RequestParam HashMap<String, String> reqMap) {
        Client client;
        if (clientFromForm.getId() == 0) {
            client = clientFromForm;
            reqMap.entrySet().stream().filter(note -> note.getKey().startsWith("phone"))
                    .forEach(note -> client.getPhoneDataSet().add(new PhoneData(note.getValue())));
            client.getPhoneDataSet().stream().forEach(phoneData -> phoneData.setClient(client));

        } else {
            Set<PhoneData> removablePhoneDataSet = new HashSet<>();
            client = clientDao.findClientById(clientFromForm.getId()).get();
            client.getPhoneDataSet().stream()
                    .filter(phoneData -> reqMap.entrySet().stream().filter(entry -> entry.getKey().startsWith("phone"))
                            .noneMatch(entry -> phoneData.equals(new PhoneData(entry.getValue()))))
                    .forEach(phoneData -> removablePhoneDataSet.add(phoneData));
            for(PhoneData phoneData:removablePhoneDataSet)
                client.getPhoneDataSet().remove(phoneData);
            reqMap.entrySet().stream().filter(entry -> entry.getKey().startsWith("phone"))
                    .filter(entry -> client.getPhoneDataSet().stream().noneMatch(phoneData -> phoneData.equals(new PhoneData(entry.getValue()))))
                    .forEach(entry -> client.getPhoneDataSet().add(new PhoneData(entry.getValue(), client)));
            if (!clientFromForm.getName().equals(client.getName()))
                client.setName(clientFromForm.getName());
            if(!clientFromForm.getPassword().equals(client.getPassword()))
                client.setPassword(clientFromForm.getPassword());
        }
        addressDataSet.setClient(client);
        client.setAddress(addressDataSet);
        clientDao.insertOrUpdate(client);
        return new RedirectView("/", true);
    }

    @GetMapping("/client/{clientID}")
    public String clientEditView(Model model, @PathVariable Long clientID) {
        Client client = null;
        try {
            client = clientDao.findClientById(clientID).get();
            model.addAttribute("client", client);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            model.addAttribute("phones", client.getPhoneDataSet());
            model.addAttribute("addressDataSet", client.getAddress());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "clientCreate.html";
    }

    @PostMapping("/client/delete")
    public RedirectView clientDelete(@ModelAttribute Client client) {
        clientDao.delete(client);
        return new RedirectView("/", true);
    }

    @PostMapping("/client/search")
    public String clientSearch(Model model, @RequestParam HashMap<String, String> reqMap){
        String name = reqMap.get("clientName");
        model.addAttribute("clients", clientDao.findClientsByNameRegExp(name));
        return "/clientSearchResult.html";
    }

}
