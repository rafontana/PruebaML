package com.mercadolibre.prueba.entidades;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.springframework.stereotype.Component;

@Component
public class Statistic {
	
    
	private Long count_mutant_dna;
    
	private Long count_human_dna;
    
	private BigDecimal ratio;
	

	public Long getCount_mutant_dna() {
		return count_mutant_dna;
	}



	public void setCount_mutant_dna(Long count_mutant_dna) {
		this.count_mutant_dna = count_mutant_dna;
	}



	public Long getCount_human_dna() {
		return count_human_dna;
	}



	public void setCount_human_dna(Long count_human_dna) {
		this.count_human_dna = count_human_dna;
	}



	public BigDecimal getRatio() {
		return ratio;
	}

	public void setRatio(BigDecimal ratio) {
		this.ratio = ratio;
	}

	public void calculateRatio() {		
		BigDecimal mutants = new BigDecimal(count_mutant_dna);
		BigDecimal humans = new BigDecimal(count_human_dna);
	    ratio = (humans.compareTo(BigDecimal.ZERO) != 0) ? mutants.divide(humans, 1,RoundingMode.HALF_DOWN) : new BigDecimal("0.0");
	
	}
}
