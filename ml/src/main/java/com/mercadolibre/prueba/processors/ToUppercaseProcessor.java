/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mercadolibre.prueba.processors;

import com.mercadolibre.prueba.entidades.Human;
import com.mercadolibre.prueba.errores.ProcessorException;
import com.mercadolibre.prueba.errores.ValidatorException;
import com.mercadolibre.prueba.interfaces.Processable;
import com.mercadolibre.prueba.validators.AbstractValidator;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

/**
 *
 * @author rafon
 */
@Component
public class ToUppercaseProcessor extends AbstractValidator implements Processable{

    @Override
    public boolean process(Human human)throws ProcessorException {
        try {
            this.validate(human);
            List<String> listaDNA = Arrays.stream(human.getDna()).parallel().map(x -> x.toUpperCase()).collect(Collectors.toList());
            String[] modifiedDNA = listaDNA.toArray(new String[listaDNA.size()]);
            human.setDna(modifiedDNA);
            return true;
        } catch (ValidatorException ex) {
            throw new ProcessorException(ex.getMessage());
        }
    }
    
}
