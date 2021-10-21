package fr.diginamic.bibliothequeWeb.controllers;

import fr.diginamic.bibliothequeWeb.entities.Client;
import fr.diginamic.bibliothequeWeb.entities.Emprunt;
import fr.diginamic.bibliothequeWeb.entities.Livre;
import fr.diginamic.bibliothequeWeb.exceptions.ClientError;
import fr.diginamic.bibliothequeWeb.repository.ClientRepository;
import fr.diginamic.bibliothequeWeb.repository.EmpruntRepository;
import fr.diginamic.bibliothequeWeb.repository.LivreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/client")
public class ClientController {
    @Autowired
    ClientRepository clientRepository;

    @Autowired
    EmpruntRepository empruntRepository;

    @Autowired
    LivreRepository livreRepository;
    public ClientController() {
        // TODO Auto-generated constructor stub
    }

    @GetMapping
    public String welcome() {
        return "Welcome REST CONTROLLER : CLIENTS";
    }


    @GetMapping("/all")
    public String findAll(Model model) {
        model.addAttribute("clients", clientRepository.findAll());
        model.addAttribute("titre", "Liste des clients");
        return "client/clientsList";
    }

    @GetMapping("/getEmprunt/{id}")
    public String getClient(@PathVariable("id") Integer id, Model model) throws ClientError {
        Optional<Client> client = clientRepository.findById(id);
        if (client.isEmpty()) {
            throw (new ClientError("Client id :" + id + " non trouvé !"));
        }

        model.addAttribute("emprunts", clientRepository.findByEmprunt(client.get().getId()));
        model.addAttribute("livreRepository", livreRepository);
        model.addAttribute("empruntRepository", empruntRepository);
        model.addAttribute("message", "Voici la liste des emprunts pour le client ");
        model.addAttribute("client", client.get());
        return "emprunts/displayEmpruntsByClient";
    }

    @GetMapping("/add")
    public String add(Model model) {
        model.addAttribute("clientForm", new Client());
        model.addAttribute("titre", "Formulaire d'ajout d'un client");
        return "client/addClient";
    }

    @PostMapping("/add")
    public String addClient(@Valid @ModelAttribute("clientForm") Client clientForm) {
        clientRepository.save(clientForm);
        return "redirect:/client/all";
    }

    @GetMapping("/update/{id}")
    public String updateCLient(Model model, @PathVariable("id") Integer id) {
        model.addAttribute("clientForm", new Client());
        model.addAttribute("titre", "Modification de la fiche client");
        model.addAttribute("client", clientRepository.findById(id).get());
        return "client/updateClient";
    }


    @PostMapping("/update/{id}")
    public String updateClient(@PathVariable("id") Integer id,
                                  @Valid @ModelAttribute("clientForm") Client clientForm) throws ClientError {
        Optional<Client> c = clientRepository.findById(id);
        if (c.isEmpty()) {
            throw (new ClientError("Client id :" + id + " non trouvé : impossible pour faire un update"));
        }
        clientRepository.save(clientForm);
        return "redirect:/client/all";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable("id") Integer id) throws Exception {
        Optional<Client> client = clientRepository.findById(id);
        if (client.isEmpty()) {
            throw (new Exception("Client id :" + id + " non trouvé !"));
        }
        List<Emprunt> empruntList = (List<Emprunt>) clientRepository.findByEmprunt(id);
        for(Emprunt emprunt : empruntList) {
            Iterable<Livre> livreList = empruntRepository.findByLivre(emprunt);
            for(Livre livre : livreList) {
                livre.getEmpruntLivres().clear();
            }
            empruntRepository.deleteById(emprunt.getId());
        }
        clientRepository.deleteById(id);
        return "redirect:/client/all";
    }

    @ExceptionHandler(value = {ClientError.class})
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public String errorClientException(ClientError e) {

        return "Soucis sur le controlleur : " + this.getClass().getSimpleName() + ":" + e.getMessage();
    }
}
