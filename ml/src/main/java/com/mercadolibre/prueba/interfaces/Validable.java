
package com.mercadolibre.prueba.interfaces;

import org.springframework.stereotype.Service;

import com.mercadolibre.prueba.entidades.Human;
import com.mercadolibre.prueba.errores.ValidatorException;


public interface Validable{
   public boolean validate(Human human) throws ValidatorException; 
}
