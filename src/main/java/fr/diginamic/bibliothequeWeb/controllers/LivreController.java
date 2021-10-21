package fr.diginamic.bibliothequeWeb.controllers;

import fr.diginamic.bibliothequeWeb.entities.Emprunt;
import fr.diginamic.bibliothequeWeb.entities.Livre;
import fr.diginamic.bibliothequeWeb.exceptions.LivreError;
import fr.diginamic.bibliothequeWeb.repository.ClientRepository;
import fr.diginamic.bibliothequeWeb.repository.EmpruntRepository;
import fr.diginamic.bibliothequeWeb.repository.LivreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("/add")
    public String AddLivre(@RequestBody Livre livre) {
        livreRepository.save(livre);
        return "insert : " + livre;
    }

    @PutMapping("/update/{id}")
    public String updateLivre(@PathVariable("id") Integer id, @RequestBody Livre livre) {
        Livre livreToUpdate = livreRepository.findById(id).get();
        livreToUpdate.setTitre(livre.getTitre());
        livreToUpdate.setAuteur(livre.getAuteur());
        livreToUpdate.setEmpruntLivres(livre.getEmpruntLivres());

        livreRepository.save(livre);
        return "update : " + livreToUpdate;
    }

    @GetMapping("/getEmprunt/{id}")
    public String getLivre(@PathVariable("id") Integer id, Model model) throws LivreError {
        Optional<Livre> livre = livreRepository.findById(id);
        if (livre.isEmpty()) {
            throw (new LivreError("livre id :" + id + " non trouvé !"));
        }

        model.addAttribute("livreRepository", livreRepository);
        model.addAttribute("empruntRepository", empruntRepository);
        model.addAttribute("message", "Voici la liste des emprunts pour le livre ");
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
