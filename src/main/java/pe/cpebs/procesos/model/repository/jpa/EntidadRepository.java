package pe.cpebs.procesos.model.repository.jpa;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import pe.cpebs.procesos.model.entities.Entidad;

@Repository
public interface EntidadRepository extends CrudRepository<Entidad, Long> {
	
	Entidad findByDescEntidad(String name);

}
