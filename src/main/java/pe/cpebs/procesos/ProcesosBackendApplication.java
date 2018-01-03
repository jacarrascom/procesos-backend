package pe.cpebs.procesos;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import pe.cpebs.procesos.service.ProcesoSeaceService;

@SpringBootApplication
public class ProcesosBackendApplication {

	@Autowired
	ProcesoSeaceService procesoSeaceService;

	public static void main(String[] args) {
		SpringApplication.run(ProcesosBackendApplication.class, args);
	}

	@Bean
	public CommandLineRunner run() throws Exception {
		return args -> {			
			//probando casos: 347368L, 384688L, 395748L
			procesoSeaceService.getDataProcesoSEACE(395748L);
			//procesoSeaceService.getDataProcesoSEACE(384688L);
			//procesoSeaceService.getDataProcesoSEACE(347368L); 			
		};
	}
}