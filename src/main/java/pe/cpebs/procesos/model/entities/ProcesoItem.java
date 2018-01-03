package pe.cpebs.procesos.model.entities;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Table(name = "proceso_item")
@Data
public class ProcesoItem implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@EmbeddedId
    private IDProcesoItem id;

	@Column(name = "codigocubso")
	private Long codigoCUBSO;
	
	@Column(name = "descripcion", nullable = true, length = 1500)
	private String descripcion;

	@Column(name = "unidad", nullable = true, length = 100)
	private String unidad;
	
	@Column(name = "cantidad", nullable = true, precision=18, scale=2)
	private BigDecimal cantidad;
	
	@Column(name = "reservamype", nullable = true, length = 20)
	private String reservaMype;
	
	@Column(name = "paquete", nullable = true, length = 20)
	private String paquete;
	
	@Column(name = "descripcionbiencomun", nullable = true, length = 1500)
	private String descripcionBienComun;
	
	@Column(name = "valorreferencial", nullable = true, precision=18, scale=2)
	private BigDecimal valorReferencial;
	
	@ManyToOne(fetch = FetchType.LAZY, cascade= {CascadeType.ALL} ) 
	@JoinColumn(name = "moneda_id", nullable = true)
	private Moneda moneda;
	
	@ManyToOne(fetch = FetchType.LAZY, cascade= {CascadeType.ALL} ) 
	@JoinColumn(name = "estado_id", nullable = true)
	private Estado estado;
	
	@ManyToOne
    @JoinColumn(name = "idconvocatoria", insertable = false, updatable = false, referencedColumnName="idconvocatoria")
    private Proceso proceso;
	
}
