package pe.cpebs.procesos.model.repository.jpa;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import pe.cpebs.procesos.model.entities.CronogramaEtapa;

@Repository
public interface CronogramaEtapaRepository extends CrudRepository<CronogramaEtapa, Long> {
	
	CronogramaEtapa findByDescEtapaCronograma(String descEtapaCronograma);
	
}
