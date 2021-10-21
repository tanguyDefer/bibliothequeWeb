package fr.diginamic.bibliothequeWeb.entities;

import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name="LIVRE")
public class Livre {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;
	
	@NotNull
	@Length(min=4, max=50)
	@Column(name = "AUTEUR", length = 50,nullable = false)
	private String auteur;
	
	@NotNull
	@Length(min=4, max=255)
	@Column(name = "TITRE", length = 255,nullable = false)
	private String titre;

	@ManyToMany
	@JoinTable(name="COMPO",
	joinColumns= @JoinColumn(name="ID_LIV", referencedColumnName="ID"),
	inverseJoinColumns= @JoinColumn(name="ID_EMP", referencedColumnName="ID")
	)
	private Set<Emprunt> empruntLivres;
	

	public Livre() {
		super();
		// TODO Auto-generated constructor stub
		empruntLivres = new HashSet<Emprunt>();
	}

	public Livre(String auteur, String titre) {
		super();
		this.auteur = auteur;
		this.titre = titre;
	}

	@Override
	public String toString() {
		return "Livre [id=" + id + ", auteur=" + auteur + ", titre=" + titre + "]";
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getAuteur() {
		return auteur;
	}

	public void setAuteur(String auteur) {
		this.auteur = auteur;
	}

	public String getTitre() {
		return titre;
	}

	public void setTitre(String titre) {
		this.titre = titre;
	}

	public Set<Emprunt> getEmpruntLivres() {
		return empruntLivres;
	}

	public void setEmpruntLivres(Set<Emprunt> empruntLivres) {
		this.empruntLivres = empruntLivres;
	}

}
