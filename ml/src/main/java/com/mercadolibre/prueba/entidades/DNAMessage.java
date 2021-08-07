package com.mercadolibre.prueba.entidades;

import java.util.Arrays;

public class DNAMessage{

    private String[] dna;

    public String[] getDna() {
        return dna;
    }

    public void setDna(String[] dna) {
        this.dna = dna;
    }

	@Override
	public boolean equals(Object obj) {
		DNAMessage other = (DNAMessage) obj;
		if (!Arrays.equals(dna, other.dna))
			return false;
		return true;
	}
    

}
