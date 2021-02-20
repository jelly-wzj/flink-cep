package com.roc.stellar.business;

import com.roc.stellar.annotation.Stellar;
import com.roc.stellar.exceptions.ParseException;
import com.roc.stellar.functions.StellarFunction;

import java.util.List;

public class FlinkStellarFunctions {

    @Stellar(name = "DEFAULT_FLINK_FN")
    public static class DefaultFlinkFunction implements StellarFunction {

        @Override
        public <T> T apply(List<Object> args) throws ParseException {
            return null;
        }
    }

}
