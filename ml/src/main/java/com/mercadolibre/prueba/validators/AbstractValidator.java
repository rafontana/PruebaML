/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mercadolibre.prueba.validators;

import com.mercadolibre.prueba.entidades.Human;
import com.mercadolibre.prueba.errores.ValidatorException;
import com.mercadolibre.prueba.interfaces.Validable;

/**
 *
 * @author rafon
 */
public class AbstractValidator implements Validable{

    @Override
    public boolean validate(Human human) throws ValidatorException {  
          if (human == null) {
            throw new ValidatorException("Missing Human");
        }else if (human.getDna() == null) {
            throw new ValidatorException("Missing DNA chain");
        }else
          { 
            return true;  
          }
          
    }
    
}
