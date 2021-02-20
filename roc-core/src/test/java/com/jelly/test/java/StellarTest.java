package com.jelly.test.java;

import com.roc.stellar.annotation.Stellar;
import org.junit.Test;
import org.reflections.Reflections;

import java.util.Set;

public class StellarTest {

    @Test
    public void obtainStellarFunctions() {
        Reflections reflections = new Reflections("com.roc.stellar");
        Class<?> stellarFunctionClass = reflections.getTypesAnnotatedWith(Stellar.class).stream().filter(stellarAnnotatedClass ->
                stellarAnnotatedClass.getAnnotation(Stellar.class).name().equals("DEFAULT_FLINK_FN")).findFirst().get();

        System.out.println(stellarFunctionClass);
    }
}
