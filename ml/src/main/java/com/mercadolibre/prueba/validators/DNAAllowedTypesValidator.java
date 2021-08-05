/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mercadolibre.prueba.validators;

import com.mercadolibre.prueba.entidades.Human;
import com.mercadolibre.prueba.errores.ValidatorException;
import com.mercadolibre.prueba.utils.MutantConst;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

/**
 *
 * @author rafon
 * 
 */
@Component
public class DNAAllowedTypesValidator extends AbstractValidator {

    @Override
    public boolean validate(Human human) throws ValidatorException {
        super.validate(human);
        Pattern pattern = Pattern.compile(MutantConst.REGEX_PATTERN);
        List<String> matching = Arrays.stream(human.getDna()).parallel()
                .filter(pattern.asPredicate())
                .collect(Collectors.toList());
        	return matching.size() == human.getDna().length;

    }

}
