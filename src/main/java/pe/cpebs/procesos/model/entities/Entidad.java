package pe.cpebs.procesos.model.entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Table(name = "mae_entidad")
@Data
public class Entidad implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id")
	private Long Id;

	@Column(name = "descripcion", nullable = false, length = 500)
	private String descEntidad;

	@Column(name = "ruc", nullable = true, length = 20)
	private String Ruc;

	@Column(name = "direccion", nullable = true, length = 200)
	private String Direccion;

	@Column(name = "telefono", nullable = true, length = 100)
	private String Telefono;

	@Column(name = "web", nullable = true, length = 100)
	private String Web;

	@Column(name = "email", nullable = true, length = 100)
	private String Email;

	@Column(name = "contacto", nullable = true, length = 100)
	private String Contacto;

	public Entidad() {
	}

	public Entidad(String descEntidad) {
		super();
		this.descEntidad = descEntidad;
	}

	public Entidad(String descEntidad, String ruc, String direccion, String telefono, String web, String email,	String contacto) {
		super();
		this.descEntidad = descEntidad;
		this.Ruc = ruc;
		this.Direccion = direccion;
		this.Telefono = telefono;
		this.Web = web;
		this.Email = email;
		this.Contacto = contacto;
	}
	
}
