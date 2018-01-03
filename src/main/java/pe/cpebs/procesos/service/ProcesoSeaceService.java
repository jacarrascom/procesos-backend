package pe.cpebs.procesos.service;

import org.jsoup.nodes.Document;

public interface ProcesoSeaceService {
		
	void getDataProcesoSEACE(Long idconvocatoria);
	
	public Boolean verificaAccesoDataProcesoSEACE(Long idconvocatoria);
	
	void getDataInformacionGeneralProcesoSEACE(Document doc, Long idconvocatoria);
	
	void getDataInformacionGeneralEntidadProcesoSEACE(Document doc);
	
	void getDataDescripcionObjetoCompletoProcesoSEACE(Document doc);
	
	void getDataInformacionGeneralProcedimientoProcesoSEACE(Document doc);
	
	void getDataInformacionCronogramaProcesoSEACE(Document doc, Long idconvocatoria);
	
	void getDataInformacionItemsProcesoSEACE(Document doc, Long idconvocatoria);
	
}
