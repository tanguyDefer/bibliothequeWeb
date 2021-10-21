package fr.diginamic.bibliothequeWeb.repository;

import fr.diginamic.bibliothequeWeb.entities.Client;
import fr.diginamic.bibliothequeWeb.entities.Emprunt;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;


public interface ClientRepository extends CrudRepository<Client, Integer> {

    @Query("select e from Emprunt e where e.clientE.id = :id")
    public Iterable<Emprunt> findByEmprunt(int id);

}
