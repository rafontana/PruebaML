package com.mercadolibre.prueba.repository;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.CrossOrigin;

import com.mercadolibre.prueba.entidades.Human;

@Repository
@CrossOrigin(value={})
public interface HumanRepository extends MongoRepository<Human,Serializable>{
	
//	@RestResource(path="mutant", rel="mutant")
//	public List<Human> findByMutant(@Param("mutant")boolean mutant);
	
	@RestResource(path="mutant", rel="mutant")
	public Long countByMutant(@Param("mutant")boolean mutant);

}
