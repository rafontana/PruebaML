/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mercadolibre.prueba.interfaces;

import com.mercadolibre.prueba.entidades.Human;
import com.mercadolibre.prueba.errores.ProcessorException;

/**
 *
 * @author rafon
 */
public interface Processable {
    public boolean process(Human human)throws ProcessorException;
}
