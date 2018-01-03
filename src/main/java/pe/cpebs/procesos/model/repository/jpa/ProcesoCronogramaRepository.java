package pe.cpebs.procesos.model.repository.jpa;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import pe.cpebs.procesos.model.entities.ProcesoCronograma;

@Repository
public interface ProcesoCronogramaRepository extends CrudRepository<ProcesoCronograma, Long> {

}
