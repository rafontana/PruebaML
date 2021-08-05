package com.mercadolibre.prueba.validators;

import java.util.Arrays;

import org.springframework.stereotype.Component;

import com.mercadolibre.prueba.entidades.Human;
import com.mercadolibre.prueba.errores.ValidatorException;

@Component
public class SequenceLengthValidator extends AbstractValidator {

    

    @Override
    public boolean validate(Human human) throws ValidatorException{
        super.validate(human);
        int secuencesLength = human.getDna().length;
        Long ret = Arrays.stream(human.getDna()).parallel().filter(x -> x.length()!=secuencesLength).count();
        return ret.intValue()==0;

    }

}
