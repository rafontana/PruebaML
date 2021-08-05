/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mercadolibre.prueba.validators;

import org.springframework.stereotype.Component;

import com.mercadolibre.prueba.entidades.Human;
import com.mercadolibre.prueba.errores.ValidatorException;
import com.mercadolibre.prueba.utils.MutantConst;


/**
 *
 * @author rafon
 */
@Component
public class MinLengthValidator extends AbstractValidator {

    

    @Override
    public boolean validate(Human human) throws ValidatorException{
        super.validate(human);
       	return human.getDna().length >= MutantConst.MIN_SEQUENCE_LENGTH;
    }

}
