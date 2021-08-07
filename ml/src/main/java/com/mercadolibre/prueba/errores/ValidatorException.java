/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mercadolibre.prueba.errores;

/**
 *
 * @author rafon
 */
public class ValidatorException extends Exception{
   
    private String message;

    public ValidatorException(String message){
        super();
        this.setMessage(message);
    }
    /**
     * @return the message
     */
    @Override
    public String getMessage() {
        return message;
    }

    /**
     * @param message the message to set
     */
    public void setMessage(String message) {
        this.message = message;
    }
    
    
}
