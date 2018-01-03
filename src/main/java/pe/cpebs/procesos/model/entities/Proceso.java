package pe.cpebs.procesos.model.entities;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Calendar;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.Data;

@Entity
@Table(name = "proceso")
@Data
public class Proceso implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id")
	private Long Id;

	//cascade= { CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.REMOVE, CascadeType.DETACH }	
	@ManyToOne(fetch = FetchType.LAZY, cascade= {CascadeType.PERSIST} )
	@JoinColumn(name = "entidad_id", nullable = true)
	private Entidad entidad;
	
	@Column(name = "publicacion", nullable = true)
	@Temporal(TemporalType.TIMESTAMP)
	private Calendar fechaPublicacion;

	@Column(name = "nomenclatura", nullable = true, length = 200)
	private String Nomenclatura;

	@Column(name = "reiniciado", nullable = true, length = 200)
	private String Reiniciado;

	@ManyToOne(fetch = FetchType.LAZY, cascade= {CascadeType.PERSIST} ) 
	@JoinColumn(name = "objeto_id", nullable = true)
	private Objeto objeto;

	@Column(name = "descripcion", nullable = true, length = 1500)
	private String Descripcion;

	@Column(name = "snip", nullable = true, length = 100)
	private String Snip;

	@ManyToOne(fetch = FetchType.LAZY, cascade= {CascadeType.PERSIST} ) 
	@JoinColumn(name = "moneda_id", nullable = true)
	private Moneda moneda;

	@Column(name = "valorreferencial", nullable = true, precision=18, scale=2)
	private BigDecimal valorReferencial;

	@Column(name = "version", nullable = true)
	private Integer versionSeace;

	@Column(name = "idconvocatoria", nullable = true)
	private Long idConvocatoria;

	@Column(name = "procesado", nullable = true)
	private Boolean Procesado = false;

	@Column(name = "numconvocatoria", nullable = true)
	private Integer numConvocatoria;

	@Column(name = "tipocompra", nullable = true, length = 150)
	private String tipoCompra;

	@Column(name = "normativa", nullable = true, length = 150)
	private String Normativa;
	
}
