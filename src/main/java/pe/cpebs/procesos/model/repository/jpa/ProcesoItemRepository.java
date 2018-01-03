package pe.cpebs.procesos.model.repository.jpa;

import org.springframework.data.repository.CrudRepository;

import pe.cpebs.procesos.model.entities.ProcesoItem;

public interface ProcesoItemRepository extends CrudRepository<ProcesoItem, Long>{

}
