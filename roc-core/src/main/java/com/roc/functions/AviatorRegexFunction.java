package com.roc.functions;

import com.googlecode.aviator.AviatorEvaluator;
import io.siddhi.core.config.SiddhiQueryContext;
import io.siddhi.core.executor.ExpressionExecutor;
import io.siddhi.core.executor.function.FunctionExecutor;
import io.siddhi.core.util.config.ConfigReader;
import io.siddhi.core.util.snapshot.state.State;
import io.siddhi.core.util.snapshot.state.StateFactory;
import io.siddhi.query.api.definition.Attribute;

import java.util.HashMap;

/**
 * @author jelly
 * <p>
 * 根据正则提取字段内容
 */
public class AviatorRegexFunction extends FunctionExecutor {

    @Override
    protected StateFactory init(ExpressionExecutor[] expressionExecutors, ConfigReader configReader, SiddhiQueryContext siddhiQueryContext) {
        return null;
    }

    /**
     * 规定 data[1] 为 正则内容
     */
    @Override
    protected Object execute(Object[] objects, State state) {
        if (null != objects && objects.length == 2) {
            return AviatorEvaluator.execute("REG=~/" + objects[1] + "/ ? $1 : '' ", new HashMap<String, Object>() {{
                put("REG", objects[0]);
            }});
        }
        return null;
    }

    @Override
    protected Object execute(Object o, State state) {
        return AviatorEvaluator.execute(o.toString());
    }

    @Override
    public Attribute.Type getReturnType() {
        return Attribute.Type.OBJECT;
    }
}