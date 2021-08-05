package com.mercadolibre.prueba.utils;

import org.springframework.stereotype.Component;

@Component
public class Status {

	private boolean conditionReached;

	public boolean isConditionReached() {
		return conditionReached;
	}

	public void setConditionReached(boolean conditionReached) {
		this.conditionReached = conditionReached;
	}
	
	
	
}
