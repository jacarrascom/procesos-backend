package pe.cpebs.procesos.service.impl;

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
import org.springframework.stereotype.Service;

import pe.cpebs.procesos.model.entities.CronogramaEtapa;
import pe.cpebs.procesos.model.entities.Entidad;
import pe.cpebs.procesos.model.entities.Estado;
import pe.cpebs.procesos.model.entities.IDProcesoCronograma;
import pe.cpebs.procesos.model.entities.IDProcesoItem;
import pe.cpebs.procesos.model.entities.Moneda;
import pe.cpebs.procesos.model.entities.Objeto;
import pe.cpebs.procesos.model.entities.Proceso;
import pe.cpebs.procesos.model.entities.ProcesoCronograma;
import pe.cpebs.procesos.model.entities.ProcesoItem;
import pe.cpebs.procesos.model.entities.Proveedor;
import pe.cpebs.procesos.model.repository.jpa.CronogramaEtapaRepository;
import pe.cpebs.procesos.model.repository.jpa.EntidadRepository;
import pe.cpebs.procesos.model.repository.jpa.EstadoRepository;
import pe.cpebs.procesos.model.repository.jpa.MonedaRepository;
import pe.cpebs.procesos.model.repository.jpa.ObjetoRepository;
import pe.cpebs.procesos.model.repository.jpa.ProcesoCronogramaRepository;
import pe.cpebs.procesos.model.repository.jpa.ProcesoItemRepository;
import pe.cpebs.procesos.model.repository.jpa.ProcesoRepository;
import pe.cpebs.procesos.model.repository.jpa.ProveedorRepository;
import pe.cpebs.procesos.service.ProcesoSeaceService;
import pe.cpebs.procesos.util.DateUtil;

@Service
public class ProcesoSeaceServiceImpl implements ProcesoSeaceService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ProcesoSeaceServiceImpl.class);
	
	@Autowired ProcesoRepository procesoRepository;
	@Autowired EntidadRepository entidadRepository;
	@Autowired ObjetoRepository objetoRepository;
	@Autowired MonedaRepository monedaRepository;
	@Autowired CronogramaEtapaRepository cronogramaEtapaRepository;	
	@Autowired EstadoRepository estadoRepository;	
	@Autowired ProcesoCronogramaRepository procesoCronogramaRepository;	
	@Autowired ProcesoItemRepository procesoItemRepository;	
	@Autowired ProveedorRepository proveedorRepository;
	
	Document 				doc;
		
	Entidad  				entidad;
	Objeto   				objeto;	
	Moneda   				moneda;
	Estado					estado;
	CronogramaEtapa 		cronogramaEtapa;
	Proveedor				proveedor;
	Proceso  				proceso;
	ProcesoCronograma 		cronograma;	
	ProcesoItem 			item;
		
	String 	 				campo, valor, descMoneda;
	BigDecimal 				valorRef;
	Elements 				cols, span;
	List<ProcesoCronograma> lista = new ArrayList<>();
	List<ProcesoItem>       items = new ArrayList<>();

	@Override
	@Transactional
	public void getDataProcesoSEACE(Long idconvocatoria) {				
		try {
			if(verificaAccesoDataProcesoSEACE(idconvocatoria)) {				
				getDataInformacionGeneralProcesoSEACE(doc, idconvocatoria);      //1. Información General
				getDataInformacionGeneralEntidadProcesoSEACE(doc);               //2. Información general de la Entidad
				getDataDescripcionObjetoCompletoProcesoSEACE(doc);               //3. Objeto del Proceso Completo
				getDataInformacionGeneralProcedimientoProcesoSEACE(doc);         //4. Información General del Procedimiento
				getDataInformacionCronogramaProcesoSEACE(doc, idconvocatoria);   //5. Cronograma
				getDataInformacionItemsProcesoSEACE(doc, idconvocatoria);		 //8. Items
				if (proceso != null) { 				
					procesoRepository.save(proceso);
					procesoCronogramaRepository.save(lista);
					procesoItemRepository.save(items);
				}
			} else {
				LOGGER.info("Acceso NO OK");
			}			
		} catch (Exception e) {
			LOGGER.error("Error procesando información", e);
			e.printStackTrace();			
		}		
	}

	@Override
	public Boolean verificaAccesoDataProcesoSEACE(Long idconvocatoria) {
		Boolean acceso = false;		
		try {
			doc = Jsoup.connect("http://prodapp2.seace.gob.pe/seacebus-uiwd-pub/fichaSeleccion/fichaSeleccion.xhtml?idSistema=3&ongei=1&idConvocatoria="+ idconvocatoria).get();			

			//recorro la tabla HTML(j_idt22) para leer el campo clave: idconvocatoria
			for (Element table : doc.select("table[id=tbFicha:j_idt803]")) { //j_idt22
				for (Element row : table.select("tr")) {
					cols = row.select("td");
					campo = cols.get(0).text().trim();
					valor = cols.get(1).text().trim();
					switch (campo) {					
						case "Identificador Convocatoria:":
							if(!valor.isEmpty()) acceso = true;						
							break;
						default:
							break;
					}
				}
			}
		} catch (IOException e) {
			LOGGER.error("Error procesando información", e);
			e.printStackTrace();			
		}
		return acceso;		
	}	
	
	@Override
	public void getDataInformacionGeneralProcesoSEACE(Document doc, Long idconvocatoria) {
		proceso = procesoRepository.findByIdConvocatoria(idconvocatoria);
		if (proceso == null) {
			proceso = new Proceso();
		}
		
		for (Element table : doc.select("table[id=tbFicha:j_idt803]")) { //j_idt22
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
	}

	@Override
	public void getDataInformacionGeneralEntidadProcesoSEACE(Document doc) {
		for (Element table : doc.select("table[id=tbFicha:j_idt844]")) { //j_idt63
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
	}
	
	@Override
	public void getDataDescripcionObjetoCompletoProcesoSEACE(Document doc) {		
		valor = doc.select("div[id=tbFicha:dialogDescObj]").text();
		proceso.setDescripcion(valor.substring(24).trim());		
	}
	
	@Override
	public void getDataInformacionGeneralProcedimientoProcesoSEACE(Document doc) {		
		for (Element table : doc.select("table[id=tbFicha:j_idt868]")) { //j_idt87
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
							
							valorRef = new BigDecimal(valor);								
							proceso.setValorReferencial(valorRef);
							
							moneda = monedaRepository.findByDescMoneda(descMoneda); 
							if (moneda == null) {
								moneda = new Moneda(descMoneda);		
								monedaRepository.save(moneda);
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
	}

	@Override
	public void getDataInformacionCronogramaProcesoSEACE(Document doc, Long idconvocatoria) {
		int nro = 0;
		String etapa, inicio, fin;
		for (Element table : doc.select("tbody[id=tbFicha:dtCronograma_data]")) {
			for (Element row : table.select("tr")) {
				cols = row.select("td");
				span = cols.select("span");
				cronograma = new ProcesoCronograma();
				if (cols.size() > 0) {
					etapa = cols.get(0).ownText().toString();
					inicio = cols.get(1).text();
					fin = cols.get(2).text();
					cronograma.setId(new IDProcesoCronograma(idconvocatoria, ++nro));
					cronogramaEtapa = cronogramaEtapaRepository.findByDescEtapaCronograma(etapa); 
					if (cronogramaEtapa == null) {
						cronogramaEtapa = new CronogramaEtapa(etapa);	
						//cronogramaEtapaRepository.save(cronogramaEtapa);
					}
					cronograma.setEtapa(cronogramaEtapa);							
					cronograma.setInicio(DateUtil.stringToCalendar(inicio, inicio.length() > 10 ? "dd/MM/yyyy HH:mm" : "dd/MM/yyyy"));
					cronograma.setFin(DateUtil.stringToCalendar(fin, fin.length() > 10 ? "dd/MM/yyyy HH:mm" : "dd/MM/yyyy"));
				}
				if (span.text().length() > 0) {
					cronograma.setObservaciones(span.text());
				} else {
					cronograma.setObservaciones("");
				}
				lista.add(cronograma);
			}
		}	
	}
	
	@Override
	public void getDataInformacionItemsProcesoSEACE(Document doc, Long idconvocatoria) {
		int nroitem = 0;
		BigDecimal cantidad, monto;
		String descEstado;
		for (Element table : doc.select("table[class=ui-datagrid-data]")) {
			for (Element row : table.select("tr")) {
				Elements cols = row.select("td");						
				for (Element tables : cols.select("table")) {
					for (Element rowi : tables.select("tr")) {					
						Elements colsi = rowi.select("td");
						
						if (colsi.size() == 1) {
							valor = colsi.get(0).text().trim();
							if(valor.length() > 0 && !valor.equalsIgnoreCase("No se encontraron Datos")) {
								item = new ProcesoItem();
								nroitem = Integer.parseInt(valor.substring(0, valor.indexOf("-")).trim());
								item.setId(new IDProcesoItem(idconvocatoria, nroitem));
								item.setDescripcion(valor.substring(valor.indexOf("-")+2));
							} else if(valor.length() > 0 && valor.equalsIgnoreCase("No se encontraron Datos")) {
								items.add(item);	
							}							
						} else if ( colsi.size() == 8) {
							if(colsi.get(0).text().equalsIgnoreCase("Codigo CUBSO:")) {								
								if(colsi.get(1).text().length()>0) item.setCodigoCUBSO(Long.parseLong(colsi.get(1).text()));							
							}
							if(colsi.get(0).text().equalsIgnoreCase("Denominación del Bien o Servicio Común")) item.setDescripcionBienComun(colsi.get(1).text());

							if(colsi.get(2).text().equalsIgnoreCase("Cantidad:")) {
								valor = colsi.get(3).text().trim();
								cantidad = new BigDecimal(valor.substring(0,valor.indexOf("-")).trim());
								item.setCantidad(cantidad);
								item.setUnidad(valor.substring(valor.indexOf("-")+2).trim());
							}
							
							if(colsi.get(4).text().equalsIgnoreCase("Reserva Para MYPE:")) {
								item.setReservaMype(colsi.get(5).text().trim());
							} else {
								valor = colsi.get(4).text().trim();
								valor = valor.replaceAll(",",""); // quita las comas
								monto = new BigDecimal(valor.substring(0, valor.indexOf(" ")).trim());
								item.setValorReferencial(monto);
								
								descMoneda = valor.substring(valor.indexOf(" ")+1).trim();
								moneda = monedaRepository.findByDescMoneda(descMoneda); 
								if (moneda == null) {
									moneda = new Moneda(descMoneda);	
									monedaRepository.save(moneda);
								}
								item.setMoneda(moneda);
							}
							
							if(colsi.get(6).text().equalsIgnoreCase("Paquete:")) item.setPaquete(colsi.get(7).text().trim());
							if(colsi.get(6).text().equalsIgnoreCase("Estado:")) {
								descEstado = colsi.get(7).text().trim();
								estado = estadoRepository.findByDescEstado(descEstado); 
								if (estado == null) {
									estado = new Estado(descEstado);
									estadoRepository.save(estado);
								}
								item.setEstado(estado);
							}							
						} else if ( colsi.size() == 6) {
							if(colsi.get(4).text().length()>0) { //Datos del Proveedor adjudicado								
								String ruc, razonsocial;
								valor = colsi.get(0).text().trim();
								ruc = valor.substring(0, valor.indexOf("-")).trim();
								razonsocial = valor.substring(valor.indexOf("-")+2);
								proveedor = proveedorRepository.findByRazonSocial(razonsocial); 
								if (proveedor == null) {
									if(ruc.equalsIgnoreCase("CONSORCIO")) {
										proveedor = new Proveedor(razonsocial, true);
									} else {
										proveedor = new Proveedor(ruc, razonsocial);
									}
									proveedorRepository.save(proveedor);
								}
								item.setProveedor(proveedor);
								
								item.setMype(colsi.get(1).text().trim());
								item.setPromocionSelva(colsi.get(2).text().trim());
								item.setBonificacionColindante(colsi.get(3).text().trim());
								
								valor = colsi.get(4).text().trim();
								valor = valor.replaceAll(",",""); // quita las comas
								cantidad = new BigDecimal(valor);
								item.setCantidadAdjudicada(cantidad);
								
								valor = colsi.get(5).text().trim();
								valor = valor.replaceAll(",",""); // quita las comas
								monto = new BigDecimal(valor);
								item.setMontoAdjudicado(monto);								
							}
							items.add(item);							
						}						
					}
				}
			}
		}
	}
}
