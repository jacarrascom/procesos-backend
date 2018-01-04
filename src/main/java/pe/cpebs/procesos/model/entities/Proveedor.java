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
@Table(name = "mae_proveedor")
@Data
public class Proveedor implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id")
	private Long Id;

	@Column(name = "ruc", nullable = true, length = 20)
	private String Ruc;
	
	@Column(name = "razonsocial", nullable = false, length = 500)
	private String razonSocial;

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
		
	@Column(name = "consorcio", nullable = true)
	private Boolean Consorcio = false;

	public Proveedor() {
	}

	public Proveedor(String razonSocial, Boolean Consorcio) {
		super();
		this.razonSocial = razonSocial;
		this.Consorcio = Consorcio;
	}
	
	public Proveedor(String Ruc, String razonSocial) {
		super();
		this.Ruc = Ruc;
		this.razonSocial = razonSocial;
	}
	
}
