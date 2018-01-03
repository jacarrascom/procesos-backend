package pe.cpebs.procesos.model.repository.jpa;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import pe.cpebs.procesos.model.entities.Moneda;

@Repository
public interface MonedaRepository extends CrudRepository<Moneda, Long>{

	Moneda findByDescMoneda(String name);
}
