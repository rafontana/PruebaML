/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mercadolibre.prueba.entidades;

import java.io.Serializable;
import java.util.Arrays;

import javax.validation.constraints.NotNull;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.stereotype.Component;



@Component
@Document(collection ="Human")
public class Human implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
//	private ObjectId objectId;
	@Id
	@NotNull
	private String id;
    private String[] dna;
    private boolean mutant;

    /**
     * @return the dna
     */
    public String[] getDna() {
        return dna;
    }

    /**
     * @param dna the dna to set
     */
    public void setDna(String[] dna) {
        this.dna = dna;
    }

    /**
     * @return the mutant
     */
    public boolean isMutant() {
        return mutant;
    }

    /**
     * @param mutant the mutant to set
     */
    public void setMutant(boolean mutant) {
        this.mutant = mutant;
    }
    
    
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Override
    public int hashCode() {
        int hash = 5;
        hash = 13 * hash + Arrays.deepHashCode(this.dna);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Human other = (Human) obj;
        if (!Arrays.deepEquals(this.dna, other.dna)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Human{" + "dna=" + dna + ", mutant=" + mutant +'}';
    }
    
    public String hexIdGenerator() {
    	return Integer.toHexString(hashCode()).toString();
    }

}
