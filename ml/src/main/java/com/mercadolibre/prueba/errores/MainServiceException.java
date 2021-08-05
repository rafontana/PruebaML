package com.mercadolibre.prueba.errores;

public class MainServiceException extends Throwable{

	 private String message;
	 
	    public MainServiceException(){
	        super();
	    }
	    
	    public MainServiceException(String message){
	        super();
	        this.message=message;
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
