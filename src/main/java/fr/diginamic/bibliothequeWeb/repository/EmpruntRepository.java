package fr.diginamic.bibliothequeWeb.repository;

import fr.diginamic.bibliothequeWeb.entities.Emprunt;
import fr.diginamic.bibliothequeWeb.entities.Livre;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface EmpruntRepository extends CrudRepository<Emprunt, Integer> {

    @Query("select l from Livre l where :emp MEMBER OF l.empruntLivres")
    public Iterable<Livre> findByLivre(Emprunt emp);

}
