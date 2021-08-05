package com.mercadolibre.prueba.controller;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.mercadolibre.prueba.entidades.Statistic;
import com.mercadolibre.prueba.errores.MainServiceException;
import com.mercadolibre.prueba.entidades.DNAMessage;
import com.mercadolibre.prueba.entidades.Human;
import com.mercadolibre.prueba.repository.HumanRepository;
import com.mercadolibre.prueba.service.ProspectService;

@RestController
public class ProspectController {

	@Autowired
	private Human human;
	@Autowired
	private ProspectService prospectService;

	@Autowired
	private HumanRepository humanRepository;

	@Autowired
	private Statistic statistic;

	private boolean isMutant;

	@RequestMapping(value = "/mutant/", method = RequestMethod.POST)
	public ResponseEntity<String> prospectRequest(@RequestBody DNAMessage dna) {
		{
			ResponseEntity<String> responseEntity;

			try {

				human.setMutant(false);
				human.setDna(dna.getDna());
				isMutant = prospectService.mainService(human);
				human.setMutant(isMutant);
				human.setId(human.hexIdGenerator());
				humanRepository.save(human);

				if (isMutant) {
					responseEntity = new ResponseEntity<>(HttpStatus.FORBIDDEN);
					return responseEntity;
				} else {
					responseEntity = new ResponseEntity<>(HttpStatus.OK);
					return responseEntity;
				}
			} catch (MainServiceException ex) {
				Logger.getLogger(ProspectController.class.getName()).log(Level.SEVERE, null, ex);
				responseEntity = new ResponseEntity<>(HttpStatus.BAD_REQUEST);
				return responseEntity;
			}

		}

	}

	@GetMapping("/stats")
	public Statistic stats() {
		Long mutants = humanRepository.countByMutant(true);
		Long humans = humanRepository.countByMutant(false);
		statistic.setCount_mutant_dna(mutants);
		statistic.setCount_human_dna(humans);
		statistic.calculateRatio();
		return statistic;
	}

}
