package fr.diginamic.bibliothequeWeb.entities;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.Date;


@Entity
@Table(name="EMPRUNT")
public class Emprunt {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;
	
	@Temporal(TemporalType.DATE)
	@Column(name = "date_debut")
	private Date datedebut;

	@NotNull
	@Temporal(TemporalType.DATE)
	@Column(name = "date_fin")
	private Date datefin;

	@Min(2)
	@Max(100)
	private int delai;

	@ManyToOne
	@JoinColumn(name="ID_CLIENT")
	@NotNull
	private Client clientE;

	public Emprunt() {
		super();
		// TODO Auto-generated constructor stub
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Date getDatedebut() {
		return datedebut;
	}

	public void setDatedebut(Date datedebut) {
		this.datedebut = datedebut;
	}

	public Date getDatefin() {
		return datefin;
	}

	public void setDatefin(Date datefin) {
		this.datefin = datefin;
	}

	public int getDelai() {
		return delai;
	}

	public void setDelai(int delai) {
		this.delai = delai;
	}

	public Client getClientE() {
		return clientE;
	}

	public void setClientE(Client clientE) {
		this.clientE = clientE;
	}

	@Override
	public String toString() {
		return "Emprunt [id=" + id + ", datedebut=" + datedebut + ", datefin=" + datefin + ", delai=" + delai
				+ ", clientE=" + clientE + "]";
	}

}
