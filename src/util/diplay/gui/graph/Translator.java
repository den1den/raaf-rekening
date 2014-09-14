/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util.diplay.gui.graph;

import java.util.Date;

/**
 *
 * @author Dennis
 */
abstract class Translator<T extends Object> {

    abstract double translate(T object);

    static Translator<?> getDefault(Class<?> clazz) {
        if (clazz.equals(Date.class)) {
            return dateTranslator();
        } else if (clazz.equals(Integer.class)) {
            return intTranslator();
        } else if (clazz.equals(Double.class)) {
            return doubleTranslator();
        } else {
            new UnsupportedOperationException("No translator for: " + clazz).printStackTrace();
            return objectTranslator();
        }
    }

    static public Translator<Date> dateTranslator() {
        return new Translator<Date>() {

            @Override
            double translate(Date object) {
                return object.getTime();
            }
        };
    }

    static public Translator<Integer> intTranslator() {
        return new Translator<Integer>() {

            @Override
            double translate(Integer object) {
                return object;
            }
        };
    }

    static public Translator<Double> doubleTranslator() {
        return new Translator<Double>() {

            @Override
            double translate(Double object) {
                return object;
            }
        };
    }

    private static Translator<?> objectTranslator() {
        return new Translator<Object>() {

            @Override
            double translate(Object object) {
                return object.hashCode();
            }
        };
    }
}
