package it.ciroppina.ejb.entity;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.OneToMany;
import javax.persistence.Table;

//@Entity
@Table(name = "entita")
public class Entita implements Serializable{

	
	private static final long serialVersionUID = -6293667065560381678L;
	
//	@Id
//	@SequenceGenerator(name = "ENTITA_GENERATOR", sequenceName = "seq_entita", allocationSize = 1, initialValue = 1)
//	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ENTITA_GENERATOR")
//	@Column(name = "id")
//	private Long id;
	
	@Column(name = "descrizione")
	private String descrizione;
	
	@Column (name = "occorrenze")
	private int occorrenze;
	
	@Column(name = "qualificatore_principale")
	private String principalQualifier;
	
	@Column(name = "qualificatore_secondario")
	private String secondaryQualifier;
	
	@OneToMany
	private List<Offset> listaOffset;

	public Entita(String descrizione, int occorrenze,
			String principalQualifier, String secondaryQualifier,
			List<Offset> listaOffset) {
		super();
//		this.id = id;
		this.descrizione = descrizione;
		this.occorrenze = occorrenze;
		this.principalQualifier = principalQualifier;
		this.secondaryQualifier = secondaryQualifier;
		this.listaOffset = listaOffset;
	}
	
	public Entita() {
		super();
	}

	// GETTER & SETTER
	
	public String getDescrizione() {
		return descrizione;
	}

	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}

	public int getOccorrenze() {
		return occorrenze;
	}

	public void setOccorrenze(int occorrenze) {
		this.occorrenze = occorrenze;
	}

	public String getPrincipalQualifier() {
		return principalQualifier;
	}

	public void setPrincipalQualifier(String principalQualifier) {
		this.principalQualifier = principalQualifier;
	}

	public String getSecondaryQualifier() {
		return secondaryQualifier;
	}

	public void setSecondaryQualifier(String secondaryQualifier) {
		this.secondaryQualifier = secondaryQualifier;
	}

	public List<Offset> getListaOffset() {
		return listaOffset;
	}

	public void setListaOffset(List<Offset> listaOffset) {
		this.listaOffset = listaOffset;
	}

//	public Long getId() {
//		return id;
//	}

}
