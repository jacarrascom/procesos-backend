package pe.cpebs.procesos.model.repository.jpa;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import pe.cpebs.procesos.model.entities.Proceso;

@Repository
public interface ProcesoRepository extends CrudRepository<Proceso, Long>{

	Proceso findByIdConvocatoria(Long idconvocatoria);
}
