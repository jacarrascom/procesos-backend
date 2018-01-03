package pe.cpebs.procesos.model.repository.jpa;

import org.springframework.data.repository.CrudRepository;

import pe.cpebs.procesos.model.entities.Estado;

public interface EstadoRepository extends CrudRepository<Estado, Long>{

	Estado findByDescEstado(String name);
	
}
