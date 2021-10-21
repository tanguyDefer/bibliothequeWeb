package fr.diginamic.bibliothequeWeb.controllers;

import fr.diginamic.bibliothequeWeb.entities.Client;
import fr.diginamic.bibliothequeWeb.entities.Emprunt;
import fr.diginamic.bibliothequeWeb.entities.Livre;
import fr.diginamic.bibliothequeWeb.exceptions.ClientError;
import fr.diginamic.bibliothequeWeb.exceptions.LivreError;
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
@RequestMapping("/livre")
public class LivreController {
    @Autowired
    LivreRepository livreRepository;

    @Autowired
    ClientRepository clientRepository;

    @Autowired
    EmpruntRepository empruntRepository;

    public LivreController() {
        // TODO Auto-generated constructor stub
    }

    @GetMapping
    public String welcome() {
        return "Welcome REST CONTROLLER : LIVRES";
    }


    @GetMapping("/all")
    public String findALl(Model model) {
        model.addAttribute("livres", livreRepository.findAll());
        model.addAttribute("titre", "Liste des livres");
        return "livres/livresList";

    }

    @GetMapping("/one/{id}")
    public Livre livreOne(@PathVariable("id") Integer id) throws LivreError {
        Optional<Livre> livre = livreRepository.findById(id);
        if (livre.isEmpty()) {
            throw (new LivreError("Livre id : " + id + " non trouvé !"));
        }
        return livre.get();
    }

    @GetMapping("/add")
    public String add(Model model) {
        model.addAttribute("livreForm", new Livre());
        model.addAttribute("titre", "Formulaire d'ajout d'un livre");
        return "livres/addLivre";
    }

    @PostMapping("/add")
    public String addLivre(@Valid @ModelAttribute("livreForm") Livre livreForm) {
        livreRepository.save(livreForm);
        return "redirect:/livre/all";
    }

    @GetMapping("/update/{id}")
    public String updateLivre(Model model, @PathVariable("id") Integer id) {
        model.addAttribute("livreForm", new Livre());
        model.addAttribute("titre", "Modification de la fiche livre");
        model.addAttribute("livre", livreRepository.findById(id).get());
        return "livres/updateLivre";
    }


    @PostMapping("/update/{id}")
    public String updateClient(@PathVariable("id") Integer id,
                               @Valid @ModelAttribute("livreForm") Livre livreForm) throws LivreError {
        Optional<Livre> c = livreRepository.findById(id);
        if (c.isEmpty()) {
            throw (new LivreError("Livre id :" + id + " non trouvé : impossible pour faire un update"));
        }
        livreRepository.save(livreForm);
        return "redirect:/livre/all";
    }

    @GetMapping("/getEmprunt/{id}")
    public String getLivre(@PathVariable("id") Integer id, Model model) throws LivreError {
        Optional<Livre> livre = livreRepository.findById(id);
        if (livre.isEmpty()) {
            throw (new LivreError("livre id :" + id + " non trouvé !"));
        }

        model.addAttribute("emprunts", livre.get().getEmpruntLivres());
        model.addAttribute("livreRepository", livreRepository);
        model.addAttribute("empruntRepository", empruntRepository);
        model.addAttribute("message", "Voici la liste des emprunts pour le livre : ");
        model.addAttribute("livre", livre.get());
        return "emprunts/displayEmpruntsByLivre";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable("id") Integer id) throws Exception {
        Optional<Livre> livre = livreRepository.findById(id);
        if (livre.isEmpty()) {
            throw (new Exception("Livre id : " + id + " non trouvé !"));
        }
        List<Emprunt> empruntList = (List<Emprunt>) clientRepository.findByEmprunt(id);
        for(Emprunt emprunt : empruntList) {
            Iterable<Livre> livreList = empruntRepository.findByLivre(emprunt);
            for(Livre livre1 : livreList) {
                livre1.getEmpruntLivres().clear();
            }
            empruntRepository.deleteById(emprunt.getId());
        }
        livreRepository.deleteById(id);
        return "redirect:/livre/all";
    }

    @ExceptionHandler(value = {LivreError.class})
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public String errorLivreExeption(LivreError e) {
        return "RESTLIVRE : Soucis sur le controller Livre : " + e.getMessage();
    }

}
