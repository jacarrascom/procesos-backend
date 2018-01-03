package pe.cpebs.procesos.model.repository.jpa;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import pe.cpebs.procesos.model.entities.Objeto;

@Repository
public interface ObjetoRepository extends CrudRepository<Objeto, Long>{

	Objeto findByDescObjeto(String name);
}
