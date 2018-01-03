package pe.cpebs.procesos.model.entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.Getter;

@Embeddable
public class IDProcesoCronograma implements Serializable {

	private static final long serialVersionUID = 1L;

	@Getter
	@Column(name = "idconvocatoria")
	private Long idConvocatoria;
 
	@Getter
    @Column(name = "nro")
    private Integer nro;
 
    public IDProcesoCronograma() {
    }
 
    public IDProcesoCronograma(Long idConvocatoria, Integer nro) {
        this.idConvocatoria = idConvocatoria;
        this.nro = nro;
    }

}
