package it.ciroppina.ejb.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Table;

//@Entity
@Table(name = "offset")
public class Offset implements Serializable {

	private static final long serialVersionUID = -1806955944360013502L;

//	@Id
//	@SequenceGenerator(name = "OFFSET_GENERATOR", sequenceName = "seq_offset", allocationSize = 1, initialValue = 1)
//	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "OFFSET_GENERATOR")
//	@Column(name = "id")
//	private Long id;
	
	@Column (name = "start")
	private String start;
	
	@Column (name = "end")
	private String end;

	public Offset(Long id, String start, String end) {
		super();
//		this.id = id;
		this.start = start;
		this.end = end;
	}
	
	
	
	// GETTER & SETTER
	
	public Offset() {
		super();
	}

	public String getStart() {
		return start;
	}

	public void setStart(String start) {
		this.start = start;
	}

	public String getEnd() {
		return end;
	}

	public void setEnd(String end) {
		this.end = end;
	}

//	public Long getId() {
//		return id;
//	}
	
}
