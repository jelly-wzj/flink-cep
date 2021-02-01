package com.roc.util;

import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.api.common.typeinfo.Types;

public final class TypeInformationUtils {
    private TypeInformationUtils() {
    }

    /**
     * 获取数据类型
     *
     * @param javaType
     * @return
     */
    public static TypeInformation[] getTypeInformation(String[] javaType) {
        if (null == javaType) {
            throw new IllegalArgumentException("there is no supported");
        }
        int len = javaType.length;
        TypeInformation[] typeInformations = new TypeInformation[len];
        for (int i = 0; i < len; i++) {
            switch (javaType[i].toUpperCase()) {
                case "STRING":
                    typeInformations[i] = Types.STRING;
                    break;
                case "LONG":
                    typeInformations[i] = Types.LONG;
                    break;
                case "INTEGER":
                    typeInformations[i] = Types.INT;
                    break;
                case "DOUBLE":
                    typeInformations[i] = Types.DOUBLE;
                    break;
                case "CHAR":
                    typeInformations[i] = Types.CHAR;
                    break;
                case "BOOLEAN":
                    typeInformations[i] = Types.BOOLEAN;
                    break;
                case "FLOAT":
                    typeInformations[i] = Types.FLOAT;
                    break;
                default:
                    throw new IllegalArgumentException("the " + javaType[i].toUpperCase() + "is no supported");
            }
        }
        return typeInformations;
    }
}
