package com.valpuestajorge.conecta4.shared.patch_compare;

import com.valpuestajorge.conecta4.errors.NotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;

@Slf4j
@Service
@AllArgsConstructor
public class PatchCompare implements PatchComparePort {

    private final ApplicationContext applicationContext;
    private static final DateTimeFormatter DATE_FORMATTER_TIME_ZONE = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
    private static final DateTimeFormatter DATE_FORMATTER_SIMPLE = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static final DateTimeFormatter DATE_FORMATTER_WITH_MILISECONDS = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
    private final MessageSource messageSource;

    /**
     * Method to ease patch ...
     *
     * @param input: Objeto que contiene los campos por los que filtrar.
     * @param toChange: Objeto que quieres cambiar con los datos del Map<String,Object>.
     * @return It returns an object you have to cast to your handling input
     * @throws ParseException error when parsing the patch elements
     */
    public <T> T getPatchCompare(Map<String, Object> input, T toChange) throws ParseException, ClassNotFoundException, NotFoundException {
        Set<String> keys = input.keySet();
        for (String key : keys) {
            Field field = ReflectionUtils.findField(toChange.getClass(), key);
            if (field != null) {
                updateField(input, toChange, key, field);
            } else {
                log.warn("Incorrect field " + key);
            }
        }
        return toChange;
    }

    @Override
    public <I> Map<String, Object> getMapFromInput(I inputValue) throws IllegalAccessException {
        Map<String, Object> map = new HashMap<>();
        Class<?> clazz = inputValue.getClass();
        for (Field field : clazz.getDeclaredFields()) {
            ReflectionUtils.makeAccessible(field);
            if (!Objects.isNull(field.get(inputValue))) {
                map.put(field.getName(), field.get(inputValue));
            }
            field.setAccessible(false);
        }

        return map;
    }

    private <T> void updateField(Map<String, Object> input, T toChange, String key, Field field) throws ParseException, ClassNotFoundException, NotFoundException {
        ReflectionUtils.makeAccessible(field);
        Object value = input.get(key);
        if (value == null || value.getClass() == field.getType()) {
            ReflectionUtils.setField(field, toChange, value);
        } else if (field.getType().equals(BigDecimal.class) || field.getType().equals(Float.class)) {
            updateNumberField(toChange, field, value);
        } else if (value instanceof String && LocalDateTime.class.isAssignableFrom(field.getType())) {
            mapLocalDateTime(field, value, toChange);
        } else if (value instanceof HashMap<?, ?> && field.getType().getSimpleName().contains("Map")) {
            ReflectionUtils.setField(field, toChange, value);
        } else if (field.getType().isEnum()) {
            mapEnum(field, value, toChange);
        } else if (value.getClass().getSimpleName().contains("Map")) {
            mapMap(field, value, toChange);
        }
    }

    private static <T> void updateNumberField(T toChange, Field field, Object value) {
        if ((value.getClass().equals(Double.class) || value.getClass().equals(Float.class) || value.getClass().equals(Integer.class)) && field.getType().equals(BigDecimal.class)) {
            ReflectionUtils.setField(field, toChange, BigDecimal.valueOf(Double.parseDouble(value.toString())));
        } else if ((value.getClass().equals(BigDecimal.class) || value.getClass().equals(Integer.class)) && field.getType().equals(Float.class)) {
            ReflectionUtils.setField(field, toChange, Float.valueOf(value.toString()));
        }
    }

    private Object getRepository(Class<?> aClass) throws ClassNotFoundException {
        String shortName = aClass.getSimpleName();
        String className = aClass.getTypeName();
        String packageName = className.substring(0, className.indexOf(shortName));
        packageName = packageName.substring(0, packageName.indexOf("entity.business"));
        String repoClassName = packageName + "repository.";
        shortName += "Repository";
        repoClassName += shortName;
        return applicationContext.getBean(Class.forName(repoClassName));
    }

    private <T> void mapMap(Field field, Object value, T toChange) throws ParseException, ClassNotFoundException {

        try {
            Class<?> innerClass = field.getType();
            if (!isPrimitive(innerClass)) {

                var instance = ReflectionUtils.getField(field, toChange);
                if (instance == null) {

                    Constructor<?> ctor = ReflectionUtils.accessibleConstructor(innerClass);
                    instance = ctor.newInstance();
                }

                Map<String, Object> mapObj = (Map<String, Object>) value;

                var patched = getPatchCompare(mapObj, instance);

                ReflectionUtils.setField(field, toChange, patched);
            }
        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException |
                 NotFoundException e) {
            throw new ClassNotFoundException();
        }
    }

    private <T> void mapEnum(Field field, Object value, T toChange) {

        Class enumClass = field.getType();
        Enum<?> e = Enum.valueOf(enumClass, (String) value);
        ReflectionUtils.setField(field, toChange, e);
    }

    private <T> void mapLocalDateTime(Field field, Object value, T toChange) throws ParseException {

        LocalDateTime date;
        String valueString = value.toString();
        DateTimeFormatter dateTimeFormatter;
        if (valueString.contains("T") && valueString.contains("Z")) {
            dateTimeFormatter = DATE_FORMATTER_TIME_ZONE;
        } else if (!valueString.contains("T") && !valueString.contains("Z")) {
            if (valueString.length() < 20) {
                dateTimeFormatter = DATE_FORMATTER_SIMPLE;
            } else {
                dateTimeFormatter = DATE_FORMATTER_WITH_MILISECONDS;
            }
        } else {
            dateTimeFormatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
        }
        try {
            date = LocalDateTime.parse(valueString, dateTimeFormatter);
        } catch (DateTimeParseException ex) {
            throw new ParseException("The string " + valueString + " is not a valid Date format. ", ex.getErrorIndex());
        }
        ReflectionUtils.setField(field, toChange, date);
    }

    private boolean isPrimitive(Class c) {

        return c.isPrimitive() || getPrimitives().contains(c.getSimpleName()) || c.isEnum();
    }

    private List<String> getPrimitives() {

        List<String> nombres = new ArrayList<>();

        nombres.add("Boolean");
        nombres.add("Character");
        nombres.add("Short");
        nombres.add("Integer");
        nombres.add("Long");
        nombres.add("Float");
        nombres.add("Double");
        nombres.add("String");
        nombres.add("Date");
        nombres.add("Timestamp");

        return nombres;
    }

}
