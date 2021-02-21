package com.jelly.test.java;

import com.roc.stellar.dsl.Stellar;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.reflections.Reflections;

public class StellarTest {

    @Test
    public void obtainStellarFunctions() {
        Reflections reflections = new Reflections("com.roc.stellar");
        Class<?> stellarFunctionClass = reflections.getTypesAnnotatedWith(Stellar.class).stream().filter(stellarAnnotatedClass ->
                stellarAnnotatedClass.getAnnotation(Stellar.class).name().equals("DEFAULT_FLINK_FN")).findFirst().get();

        System.out.println(stellarFunctionClass);
    }

    @Test
    public void test(){
        System.out.println(StringUtils.substringBefore("WORD_COUNT_FUN", "("));
    }
}
