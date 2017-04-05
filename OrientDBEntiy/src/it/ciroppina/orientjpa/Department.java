package it.ciroppina.orientjpa;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Table;

/**
 * non-Entity Implementation class for table: Department
 *
 */
//@Entity
@Table(name="Department")
public class Department implements Serializable {

	@Column(name="name")
	private String name;   
	
	private static final long serialVersionUID = 1L;

	
	/*******************************
	 * Constructor, setters, getters
	 */
	public Department() {
		super();
	}
	public String getName() {
		return this.name;
	}
	public void setName(String name) {
		this.name = name;
	}
}
