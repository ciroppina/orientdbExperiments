package it.ciroppina.ejb.entity;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.OneToMany;
import javax.persistence.Table;


//@Entity
@Table(schema="TiapTest", name="documento")
public class Documento implements Serializable{

	private static final long serialVersionUID = 6062356867250024646L;

//	@Id
////	@SequenceGenerator(name = "DOCUMENTO_GENERATOR", sequenceName = "seq_documento", allocationSize = 1, initialValue = 1)
////	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "DOCUMENTO_GENERATOR")
//	@Column(name = "id")
//	private Long id;
	
	@Column(name = "nome_documento")
	private String nomeDocumento;
	
	//obbligatorio
	@Column(name = "fascicolo")
	private String fascicolo;
	
	//obbligatorio
	@Column(name = "rg")
	private String rg;
	
	//enumeration : indagine; dibattimento
	@Column(name = "fase")
	private String fase;
	
	@OneToMany
	private List<Entita> listaEntita;
	
	
	public Documento() {}
	
	public Documento(String nomeDocumento, String fascicolo,
			String rg, String fase, List<Entita> listaEntita) {
		super();
//		this.id = id;
		this.nomeDocumento = nomeDocumento;
		this.fascicolo = fascicolo;
		this.rg = rg;
		this.fase = fase;
		this.listaEntita = listaEntita;
	}

	
	
	// GETTER & SETTER
	
//	public Long getId() {
//		return id;
//	}

	public String getNomeDocumento() {
		return nomeDocumento;
	}

	public void setNomeDocumento(String nomeDocumento) {
		this.nomeDocumento = nomeDocumento;
	}

	public String getFascicolo() {
		return fascicolo;
	}

	public void setFascicolo(String fascicolo) {
		this.fascicolo = fascicolo;
	}

	public String getRg() {
		return rg;
	}

	public void setRg(String rg) {
		this.rg = rg;
	}

	public String getFase() {
		return fase;
	}

	public void setFase(String fase) {
		this.fase = fase;
	}

	public List<Entita> getListaEntita() {
		return listaEntita;
	}

	public void setListaEntita(List<Entita> listaEntita) {
		this.listaEntita = listaEntita;
	}

}
