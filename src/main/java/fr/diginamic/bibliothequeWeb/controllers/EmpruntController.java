package fr.diginamic.bibliothequeWeb.controllers;

import fr.diginamic.bibliothequeWeb.entities.Emprunt;
import fr.diginamic.bibliothequeWeb.entities.Livre;
import fr.diginamic.bibliothequeWeb.exceptions.EmpruntError;
import fr.diginamic.bibliothequeWeb.repository.ClientRepository;
import fr.diginamic.bibliothequeWeb.repository.EmpruntRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/emprunt")
public class EmpruntController {
    @Autowired
    EmpruntRepository empruntRepository;

    @Autowired
    ClientRepository clientRepository;

    public EmpruntController() {
        // TODO Auto-generated constructor stub
    }

    @GetMapping
    public String welcome() {
        return "Welcome REST CONTROLLER : EMPRUNTS";
    }

    @GetMapping("/all")
    public List<Emprunt> empruntAll() {

        return (List<Emprunt>) empruntRepository.findAll();
    }

    @GetMapping("/one/{id}")
    public Emprunt empruntOne(@PathVariable("id") Integer pid) throws EmpruntError {
        Optional<Emprunt> e = empruntRepository.findById(pid);
        if (e.isEmpty()) {
            throw (new EmpruntError("Emprunt id :" + pid + " non trouvé !"));
        }
        System.err.println(empruntRepository.findByLivre(e.get()));
        return e.get();
    }


    @PutMapping("update/{id}")
    public String empruntUpdateOne(@PathVariable("id") Integer id,
                                   @Valid @RequestBody Emprunt pe) {
        Emprunt e = (Emprunt) empruntRepository.findById(id).get();

        empruntRepository.save(pe);
        return "Update Ok : " + e.getId();
    }

    @PostMapping("create")
    public String empruntCreate(@Valid @RequestBody Emprunt pe) {
        //Emprunt e = er.findById(id).get();

        empruntRepository.save(pe);//save insert ou update selon l'id !
        return "Create Ok : " + pe.getId();
    }

    @ExceptionHandler(value = {EmpruntError.class})
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public String errorEmpruntException(EmpruntError e) {
        //"Emprunt id :"+pid+" non trouvé !"
        String message = "Soucis sur le controlleur : " + this.getClass().getSimpleName() + ":" + e.getMessage();

        return message;
    }
}
