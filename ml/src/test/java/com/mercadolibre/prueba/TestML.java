package com.mercadolibre.prueba;

import java.math.BigDecimal;
import java.util.Arrays;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import com.mercadolibre.prueba.controller.ProspectController;
import com.mercadolibre.prueba.entidades.DNAMessage;
import com.mercadolibre.prueba.entidades.Human;
import com.mercadolibre.prueba.entidades.Statistic;
import com.mercadolibre.prueba.errores.MainServiceException;
import com.mercadolibre.prueba.errores.ProcessorException;
import com.mercadolibre.prueba.errores.ValidatorException;
import com.mercadolibre.prueba.processors.ToUppercaseProcessor;
import com.mercadolibre.prueba.service.ProspectService;
import com.mercadolibre.prueba.validators.DNAAllowedTypesValidator;
import com.mercadolibre.prueba.validators.MinLengthValidator;
import com.mercadolibre.prueba.validators.SequenceLengthValidator;


@RunWith(SpringRunner.class)
@SpringBootTest

public class TestML {

	@Autowired
	ProspectService prospectService;
	@Autowired
	ProspectController prospectController;
	@Autowired
	SequenceLengthValidator sequenceLengthValidator;
	@Autowired
	MinLengthValidator minLengthValidator;
	@Autowired
	ToUppercaseProcessor toUppercaseProcessor;
	@Autowired
	DNAAllowedTypesValidator dNAAllowedTypesValidator;
	@Autowired
	Human human;
	@Autowired
	Statistic statistic;


	

	
	@Test
	public void MutantTest() {
		// ADN Mutante
		String[] dna = { "ATGCGA", "CAGTGC", "TTATGT", "AGAAGG", "CCCCTA", "TCACTG" };
		human.setDna(dna);
		try {
			Assertions.assertEquals(true, prospectService.mainService(human));
		} catch (MainServiceException e) {
			e.printStackTrace();
			Assertions.fail();

		}
	}

	@Test
	public void ExtendedDNAMutantTest() {
		// ADN Mutante de cadena mas larga
		String[] dnaA = { "TGAAGTCACCTA","AAAAGTAGAAAA","TGAAGTCACCTA","TCACTGAGAAGG","CACCTAAGAAGG","AGAAGGTCACTG","AGAAGGTCACTG","TGAAGTAGAAGG","TGAAGTCACCTA","TCACTGAGAAGG","CACCTAAGAAGG","AGAAGGTCACTG" };
		String[] dnaC = { "TCCCCGAGCCCC","TGAAGTAGAAGG","TGAAGTCACCTA","TCACTGAGAAGG","CACCTAAGAAGG","AGAAGGTCACTG","AGAAGGTCACTG","TGAAGTAGAAGG","TGAAGTCACCTA","TCACTGAGAAGG","CACCTAAGAAGG","AGAAGGTCACTG" };
		String[] dnaT = { "TCACTGAGAAGG","TGAAGTAGAAGG","TGAAGTCACCTA","TTTTCCTTTTTT","CACCTAAGAAGG","AGAAGGTCACTG","AGAAGGTCACTG","TGAAGTAGAAGG","TGAAGTCACCTA","TCACTGAGAAGG","CACCTAAGAAGG","AGAAGGTCACTG" };
		String[] dnaG = { "GGGGCCTTGGGG","TGAAGTAGAAGG","TGAAGTCACCTA","TCACTGAGAAGG","CACCTAAGAAGG","AGAAGGTCACTG","AGAAGGTCACTG","TGAAGTAGAAGG","TGAAGTCACCTA","TCACTGAGAAGG","CACCTAAGAAGG","AGAAGGTCACTG" };
		
		try {
			human.setDna(dnaA);
			Assertions.assertEquals(true, prospectService.mainService(human));
			human.setDna(dnaC);
			Assertions.assertEquals(true, prospectService.mainService(human));
			human.setDna(dnaT);
			Assertions.assertEquals(true, prospectService.mainService(human));
			human.setDna(dnaG);
			Assertions.assertEquals(true, prospectService.mainService(human));
		} catch (MainServiceException e) {
			e.printStackTrace();
			Assertions.fail();

		}
	}
	
	@Test
	public void notMutantTest() {
		//ADN No Mutante
		String[] dna = { "ATCGTA", "CATTGC", "TGAAGT", "AGAAGG", "CACCTA", "TCACTG" };
		human.setDna(dna);
		try {
			Assertions.assertEquals(false, prospectService.mainService(human));
		} catch (MainServiceException e) {
			e.printStackTrace();
			Assertions.fail();

		}
	}

	@Test
	public void mainServiceExceptionTest() {
		//ADN Roto Bubbling hacia MainServiceException
		String[] dna = { "ATCGTA", "CATTGC", "TGAAGT", "AGAAGG", "CACCTA", "TCA" };
		human.setDna(dna);
		try {
			 prospectService.mainService(human);
		} catch (MainServiceException e) {
			Assertions.assertEquals("Unequal dna dimension chain", e.getMessage());

		}
	}
	


	@Test
	public void sequenceLengthValidatorExceptionTest() {
		// Cadena ADN No válida - Valida largo de cada secuencia sean iguales
		String[] dna = { "ATCGT", "CATTGC", "TGAAGT", "AGAAGG", "CACCTA", "TCACTG" };
		human.setDna(dna);
		try {
			sequenceLengthValidator.validate(human);
			} catch (ValidatorException e) {
			Assertions.assertEquals("Exception in Main Service: Unequal dna dimension chain", e.getMessage());
		} 
	}

	@Test
	public void minLengthValidatorExceptionTest() {
		// Cadena ADN No válida - Valida cantidad de secuencias sean iguales a largo de cadena
		String[] dna = { "ATGCGA", "CAGTGC", "TTATGT"};
		human.setDna(dna);
		try {
			minLengthValidator.validate(human);
			} catch (ValidatorException e) {
			Assertions.assertEquals("Exception in Main Service: Short dna chain", e.getMessage());

		}
	}


	@Test
	public void dNAAllowedTypesValidatorExceptionTest() {
		// Cadena ADN No válida - Valida cantidad de secuencias sean iguales a largo de cadena
		String[] dna = { "XXXGTA", "CATTGC", "TGAAGT", "AGAAGG", "CACCTA", "TCACTG"};
		human.setDna(dna);
		try {
			dNAAllowedTypesValidator.validate(human);
			} catch (ValidatorException e) {
			Assertions.assertEquals("Exception in Main Service: Not A C T G Types", e.getMessage());

		}
	}	
	
	@Test
	public void toUppercaseProcessorTest() {
		// Cadena ADN No válida - Procesa a mayúsculas
		String[] dna = {"atgcga","cagtgc","ttatgt","agaagg","ccccta","tcactt"};
		String[] dnaExpected = {"ATGCGA","CAGTGC","TTATGT","AGAAGG","CCCCTA","TCACTT"};
		human.setDna(dna);
		try {
			toUppercaseProcessor.process(human);
			System.out.println(dnaExpected);
			System.out.println(human.getDna());
			Assertions.assertTrue(Arrays.equals(dnaExpected, human.getDna()));
		} catch (ProcessorException e) {
			e.printStackTrace();
			Assertions.fail();
		}
	} 
		
		@Test
		public void toUppercaseProcessorWithoutDNAExceptionTest() {
			//Sin DNA
			human.setDna(null);
			try {
				toUppercaseProcessor.process(human);
			} catch (ProcessorException e) {
				Assertions.assertEquals("Missing DNA chain", e.getMessage());
			}

	}	

		@Test
		public void toUppercaseProcessorWithoutHumanExceptionTest() {
			//Sin Human
			try {
				toUppercaseProcessor.process(null);
			} catch (ProcessorException e) {
				Assertions.assertEquals("Missing Human", e.getMessage());
			}
	
		}
		
		@Test
		public void HumanTest() {
			//Completo uso del Humano
			String[] dna1 = {"ATGCGA","CAGTGC","TTATGT","AGAAGG","CCCCTA","TCACTT"};
			String[] dna2 = {"ATGCGA","CAGTGC","TTATGT","AGAAGG","CCCCTA","TCACTT"};
			human.setDna(dna1);
			human.setId(human.hexIdGenerator());
			human.setMutant(true);
			human.toString();
			human.hashCode();
			human.getId();
			human.getDna();
			human.isMutant();
			
			Human expectedHuman = new Human();
			expectedHuman.setDna(dna2);
			expectedHuman.setId(expectedHuman.hexIdGenerator());
			expectedHuman.setMutant(true);	
			//Compara con otro
			Assertions.assertTrue(human.equals(expectedHuman));
			//Compara con si mísmo
			Assertions.assertTrue(human.equals(human));
			//Compara con null
			Assertions.assertFalse(human.equals(null));
			//Compara con ADN roto
			expectedHuman.setDna(null);
			Assertions.assertFalse(human.equals(expectedHuman));
			//Compara class
			Assertions.assertFalse(human.getClass().equals(new String().getClass()));
			//Verifica Id
			human.setId("a");
			Assertions.assertFalse(human.equals(expectedHuman));
			//Verifica Id nulo
			human.setId(null);
			Assertions.assertFalse(human.equals(expectedHuman));
			//Verifica Id nulo del otro
			human.setId(human.hexIdGenerator());
			expectedHuman.setId(null);
			Assertions.assertFalse(human.equals(expectedHuman));
			
		}
		@Test
		public void dNAMessageTest() {
			String[] dna = {"ATGCGA","CAGTGC","TTATGT","AGAAGG","CCCCTA","TCACTT"};
			String[] anotherDna = {"ATTTGA","CAGTGC","TTATGT","AGAAGG","CCCCTA","TCAATT"};
			DNAMessage dNAMessage = new DNAMessage();
			DNAMessage dNAMessage2 = new DNAMessage();
			dNAMessage2.setDna(anotherDna);
			dNAMessage.setDna(dna);
			Assertions.assertTrue(dNAMessage.getDna().equals(dna));
			Assertions.assertTrue(dNAMessage.equals(dNAMessage));
			Assertions.assertFalse(dNAMessage.equals(dNAMessage2));
			
		}

		@Test
		public void statisticTest() {
			statistic.setCount_human_dna(22L);
			statistic.setCount_mutant_dna(44L);
			statistic.setRatio(new BigDecimal(0));
			statistic.calculateRatio();
			statistic.getCount_mutant_dna();
			statistic.getRatio();
			Assertions.assertTrue(statistic.getCount_human_dna().equals(22L));

			
		}
		
		@Test
		public void controllerTest() {
			//DNA Humano
			String[] humanDna = { "ATCGTA", "CATTGC", "TGAAGT", "AGAAGG", "CACCTA", "TCACTG" };
			DNAMessage dNAMessage = new DNAMessage();
			dNAMessage.setDna(humanDna);
			ResponseEntity responseEntity = prospectController.prospectRequest(dNAMessage);
			Assertions.assertEquals(HttpStatus.OK,responseEntity.getStatusCode());
			String[] mutantDna = { "ATGCGA", "CAGTGC", "TTATGT", "AGAAGG", "CCCCTA", "TCACTG" };
			dNAMessage.setDna(mutantDna);
			responseEntity = prospectController.prospectRequest(dNAMessage);
			Assertions.assertEquals(HttpStatus.FORBIDDEN,responseEntity.getStatusCode());
			String[] brokenDna = { "ATGCGA", "CAGTGC", "TTATGT", "AGAAGG", "CCCCTA" };
			dNAMessage.setDna(brokenDna);
			responseEntity = prospectController.prospectRequest(dNAMessage);
			Assertions.assertEquals(HttpStatus.BAD_REQUEST,responseEntity.getStatusCode());
			statistic = prospectController.stats();
			System.out.println(statistic.getCount_human_dna());
			Assertions.assertTrue(statistic!=null);
		}
		

}
