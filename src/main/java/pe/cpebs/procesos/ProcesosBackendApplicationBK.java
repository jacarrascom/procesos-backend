package pe.cpebs.procesos;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.context.annotation.Bean;

import pe.cpebs.procesos.model.entities.CronogramaEtapa;
import pe.cpebs.procesos.model.entities.Entidad;
import pe.cpebs.procesos.model.entities.IDProcesoCronograma;
import pe.cpebs.procesos.model.entities.Moneda;
import pe.cpebs.procesos.model.entities.Objeto;
import pe.cpebs.procesos.model.entities.Proceso;
import pe.cpebs.procesos.model.entities.ProcesoCronograma;
import pe.cpebs.procesos.model.repository.jpa.CronogramaEtapaRepository;
import pe.cpebs.procesos.model.repository.jpa.EntidadRepository;
import pe.cpebs.procesos.model.repository.jpa.MonedaRepository;
import pe.cpebs.procesos.model.repository.jpa.ObjetoRepository;
import pe.cpebs.procesos.model.repository.jpa.ProcesoCronogramaRepository;
import pe.cpebs.procesos.model.repository.jpa.ProcesoRepository;
import pe.cpebs.procesos.service.ProcesoSeaceService;
import pe.cpebs.procesos.util.DateUtil;

public class ProcesosBackendApplicationBK {
	private static final Logger LOGGER = LoggerFactory.getLogger(ProcesosBackendApplication.class);

	@Autowired
	EntidadRepository entidadRepository;

	@Autowired
	ProcesoRepository procesoRepository;

	@Autowired
	ObjetoRepository objetoRepository;

	@Autowired
	MonedaRepository monedaRepository;
	
	@Autowired
	CronogramaEtapaRepository cronogramaEtapaRepository;
	
	@Autowired
	ProcesoCronogramaRepository procesoCronogramaRepository;
	
	@Autowired
	ProcesoSeaceService procesoSeaceService;

	public static void main(String[] args) {
		SpringApplication.run(ProcesosBackendApplication.class, args);
	}

	@Bean
	public CommandLineRunner run() throws Exception {
		return args -> {
			// saveData();
			//leerDataSEACE(384688L); //347368L,384688L
			//procesoSeaceService.getDataProcesoSEACE(384688L);
		};
	}

	@Transactional
	private void saveData() {
		Entidad entidad1 = new Entidad("MINSA");
		Entidad entidad2 = new Entidad("ESSALUD");

		Objeto objeto1 = new Objeto("Bienes");
		Objeto objeto2 = new Objeto("Servicios");

		Moneda moneda1 = new Moneda("Soles");
		// Proceso proceso1 = new Proceso(entidad1, Calendar.getInstance());
		// Proceso proceso2 = new Proceso(entidad2, Calendar.getInstance());
		//Proceso proceso1 = new Proceso(entidad1, 347368L);
		//Proceso proceso2 = new Proceso(entidad2, 350895L);

		entidadRepository.save(entidad1);
		entidadRepository.save(entidad2);

		objetoRepository.save(objeto1);
		objetoRepository.save(objeto2);

		monedaRepository.save(moneda1);

		//procesoRepository.save(proceso1);
		//procesoRepository.save(proceso2);
	}

	@Transactional
	private void leerDataSEACE(Long idconvocatoria) {
		Document doc;
		String campo, valor, descMoneda;
		Boolean hayAcceso = false;
		BigDecimal bigDecimal;		
		Elements cols, descObjeto, span;			
		Entidad entidad = null;
		Proceso proceso = null;
		Objeto objeto = null;	
		Moneda moneda = null;
		ProcesoCronograma cronograma = null;
		CronogramaEtapa cronogramaEtapa = null;
		int nro;
		List<ProcesoCronograma> lista = new ArrayList<>();
		try {
			doc = Jsoup.connect("http://prodapp2.seace.gob.pe/seacebus-uiwd-pub/fichaSeleccion/fichaSeleccion.xhtml?idSistema=3&ongei=1&idConvocatoria="+ idconvocatoria).get();

			//Valido acceso a la pagina leyendo campo clave: idconvocatoria
			for (Element table : doc.select("table[id=tbFicha:j_idt22]")) {
				for (Element row : table.select("tr")) {
					cols = row.select("td");
					campo = cols.get(0).text().trim();
					valor = cols.get(1).text().trim();
					switch (campo) {
					case "Identificador Convocatoria:":
						if(!valor.isEmpty()) {
							hayAcceso = true;	
						}						
						break;
					default:
						break;
					}
				}
			}
			
			if (hayAcceso) {				
				LOGGER.info("Acceso correcto");
				proceso = procesoRepository.findByIdConvocatoria(idconvocatoria);
				if (proceso == null) {
					proceso = new Proceso();
				}
				
				//1. Información General
				for (Element table : doc.select("table[id=tbFicha:j_idt22]")) {
					for (Element row : table.select("tr")) {
						cols = row.select("td");
						campo = cols.get(0).text().trim();
						valor = cols.get(1).text().trim();
						switch (campo) {
						case "Nomenclatura:":
							proceso.setNomenclatura(valor);
							break;
						case "N° Convocatoria:":
							proceso.setNumConvocatoria(Integer.parseInt(valor));
							break;
						case "Tipo Compra o Selección:":
							proceso.setTipoCompra(valor);
							break;
						case "Normativa Aplicable:":
							proceso.setNormativa(valor);
							break;
						case "Versión SEACE":
							proceso.setVersionSeace(Integer.parseInt(valor));
							break;
						case "Identificador Convocatoria:":
							proceso.setIdConvocatoria(Long.parseLong(valor));
							break;
						default:
							break;
						}
					}
				}
	
				//2. Información general de la Entidad
				for (Element table : doc.select("table[id=tbFicha:j_idt63]")) {
					for (Element row : table.select("tr")) {
						cols = row.select("td");
						campo = cols.get(0).text().trim();
						valor = cols.get(1).text().trim();
						switch (campo) {
						case "Entidad Convocante:":
							entidad = entidadRepository.findByDescEntidad(valor);
							if (entidad == null) {
								entidad = new Entidad(valor);							
							}
							break;
						case "Direccion Legal:":
							entidad.setDireccion(valor);
							break;
						case "Pagina Web:":
							entidad.setWeb(valor);
							break;
						case "Télefono de la Entidad:":
							entidad.setTelefono(valor);
							break;
						default:
							break;
						}
					}
				}
				proceso.setEntidad(entidad);
				
				//3. Objeto del Proceso Completo
				descObjeto = doc.select("div[id=tbFicha:dialogDescObj]");
				valor = descObjeto.text();
				proceso.setDescripcion(valor.substring(24).trim());
				
				//4. Información General del Procedimiento
				for (Element table : doc.select("table[id=tbFicha:j_idt87]")) {
					for (Element row : table.select("tr")) {
						cols = row.select("td");
						if (cols.size() > 0) {
							campo = cols.get(0).text().trim();
							if(campo.equalsIgnoreCase("Valor Estimado:")) campo="Valor Referencial:";
							valor = cols.get(1).text().trim();
							switch (campo) {
							case "Objeto de Contratación:":
								objeto = objetoRepository.findByDescObjeto(valor);
								if (objeto == null) {
									objeto = new Objeto(valor);							
								}
								proceso.setObjeto(objeto);
								break;
							case "Valor Referencial:":
								valor = valor.replaceAll(",", "");                    // quita las comas
								descMoneda = valor.substring(valor.indexOf(".") + 3); // extrae la moneda
								valor = valor.substring(0, valor.indexOf(".") + 3);   // extrae el valor
								
								bigDecimal = new BigDecimal(valor);								
								proceso.setValorReferencial(bigDecimal);
								
								moneda = monedaRepository.findByDescMoneda(descMoneda); 
								if (moneda == null) {
									moneda = new Moneda(descMoneda);							
								}
								proceso.setMoneda(moneda);
								break;
							case "Fecha y Hora Publicación:":
								proceso.setFechaPublicacion(DateUtil.stringToCalendar(valor, "dd/MM/yyyy HH:mm"));
								break;
							default:
								break;
							}
						}
					}
				}
				
				//5. Cronograma
				nro = 0;
				for (Element table : doc.select("tbody[id=tbFicha:dtCronograma_data]")) {
					for (Element row : table.select("tr")) {
						cols = row.select("td");
						span = cols.select("span");
						cronograma = new ProcesoCronograma();
						if (cols.size() > 0) {
							cronograma.setId(new IDProcesoCronograma(idconvocatoria, ++nro));
							cronogramaEtapa = cronogramaEtapaRepository.findByDescEtapaCronograma(cols.get(0).ownText().toString()); 
							if (cronogramaEtapa == null) {
								cronogramaEtapa = new CronogramaEtapa(cols.get(0).ownText().toString());	
								cronogramaEtapaRepository.save(cronogramaEtapa);
							}
							cronograma.setEtapa(cronogramaEtapa);							
							cronograma.setInicio(DateUtil.stringToCalendar(cols.get(1).text(), cols.get(1).text().length() > 10 ? "dd/MM/yyyy HH:mm" : "dd/MM/yyyy"));
							cronograma.setFin(DateUtil.stringToCalendar(cols.get(2).text(),    cols.get(2).text().length() > 10 ? "dd/MM/yyyy HH:mm" : "dd/MM/yyyy"));
						}
						if (span.text().length() > 0) {
							cronograma.setObservaciones(span.text());
						} else {
							cronograma.setObservaciones("");
						}
						lista.add(cronograma);
					}
				}
				
				if (proceso != null) {					
					procesoRepository.save(proceso);
					procesoCronogramaRepository.save(lista);
				}
			} else {
				LOGGER.info("Acceso NO Ok");
			}		

		} catch (IOException e) {
			LOGGER.error("Error procesando información", e);
			e.printStackTrace();			
		}
	}

}
