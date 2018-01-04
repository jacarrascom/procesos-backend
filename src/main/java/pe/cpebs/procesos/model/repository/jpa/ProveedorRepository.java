package pe.cpebs.procesos.model.repository.jpa;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import pe.cpebs.procesos.model.entities.Proveedor;

@Repository
public interface ProveedorRepository extends CrudRepository<Proveedor, Long>{

	Proveedor findByRazonSocial(String name);
	
}
