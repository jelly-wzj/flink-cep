package com.roc.stellar.functions;

import com.roc.stellar.exceptions.ParseException;

import java.util.List;

public interface StellarFunction {
    <T> T apply(List<Object> args) throws ParseException;
}