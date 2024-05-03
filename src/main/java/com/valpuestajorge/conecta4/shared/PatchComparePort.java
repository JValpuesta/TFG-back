package com.valpuestajorge.conecta4.shared;


import com.valpuestajorge.conecta4.errors.NotFoundException;

import java.text.ParseException;
import java.util.Map;

public interface PatchComparePort {

    /**
     * Devuelve la clase modificada, comparando un Map y una clase
     *
     * @param input: Objeto que contiene los campos por los que filtrar.
     * @param toChange: Objeto que quieres cambiar con los datos del Map<String,Object>.
     * @return Objeto con las normas para paginar.
     */
    <T> T getPatchCompare(Map<String, Object> input, T toChange) throws ParseException, ClassNotFoundException, NotFoundException;

    <I> Map<String, Object> getMapFromInput(I inputValue) throws IllegalAccessException;

}
