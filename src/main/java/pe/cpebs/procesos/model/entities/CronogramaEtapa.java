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
@Table(name = "mae_cronograma_etapa")
@Data
public class CronogramaEtapa implements Serializable{
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id")
	private Long Id;

	@Column(name = "descripcion", nullable = false, length = 250)
	private String descEtapaCronograma;
	
	public CronogramaEtapa() {		
	}
	
	public CronogramaEtapa(String descEtapaCronograma) {
		super();
		this.descEtapaCronograma = descEtapaCronograma;
	}

}
