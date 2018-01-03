package pe.cpebs.procesos.model.entities;

import java.io.Serializable;
import java.util.Calendar;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.Data;

@Entity
@Table(name = "proceso_cronograma")
@Data
public class ProcesoCronograma implements Serializable{
	private static final long serialVersionUID = 1L;

	@EmbeddedId
    private IDProcesoCronograma id;
	
	@ManyToOne(fetch = FetchType.LAZY, cascade= {CascadeType.ALL} )
	@JoinColumn(name = "etapa_id", nullable = true)
	private CronogramaEtapa etapa;
		
	@Column(name = "inicio", nullable = true)
	@Temporal(TemporalType.TIMESTAMP)
	private Calendar inicio;
	
	@Column(name = "fin", nullable = true)
	@Temporal(TemporalType.TIMESTAMP)
	private Calendar fin;
	
	@Column(name = "observaciones", nullable = true, length = 500)
	private String observaciones;

	@Column(name = "modificado", nullable = true)
	private Boolean modificado = false;
	
	@ManyToOne
    @JoinColumn(name = "idconvocatoria", insertable = false, updatable = false, referencedColumnName="idconvocatoria")
    private Proceso proceso;

}
