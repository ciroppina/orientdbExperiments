package it.ciroppina.orientjpa;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * non-Entity Implementation class for table: Company
 *
 */
//@Entity
@Table(name="Company")
public class Company implements Serializable {

	@Column(name="Name")
	private String name;   
	
	@OneToMany
	private List<Department> listaDept;
	
	private static final long serialVersionUID = 1L;

	
	/*******************************
	 * Constructor, setters, getters
	 */
	public Company() {
		super();
		this.listaDept = new ArrayList<Department>();
	}   
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}
	public List<Department> getListaDept() {
		return listaDept;
	}
	public void setListaDept(List<Department> aList) {
		this.listaDept = aList == null ? this.listaDept : aList;
	}   
   
}
