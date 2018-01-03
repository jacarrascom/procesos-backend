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
@Table(name="mae_objeto")
@Data
public class Objeto implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id")
	private Long Id;
	
	@Column(name = "descripcion", nullable = false, length = 200)
	private String descObjeto;

	public Objeto() {}

	public Objeto(String descObjeto) {
		super();
		this.descObjeto = descObjeto;
	}
	
}
