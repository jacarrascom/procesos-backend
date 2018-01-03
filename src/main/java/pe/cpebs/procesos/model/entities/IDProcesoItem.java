package pe.cpebs.procesos.model.entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.Getter;

@Embeddable
public class IDProcesoItem implements Serializable {

	private static final long serialVersionUID = 1L;

	@Getter
	@Column(name = "idconvocatoria")
	private Long idConvocatoria;
 
	@Getter
    @Column(name = "nroitem")
    private Integer nroitem;
    
    public IDProcesoItem() {
    }
 
    public IDProcesoItem(Long idConvocatoria, Integer nroitem) {
        this.idConvocatoria = idConvocatoria;
        this.nroitem = nroitem;
    }
}
