package com.jelly.flink.functions;

import com.googlecode.aviator.AviatorEvaluator;
import org.wso2.siddhi.core.config.SiddhiAppContext;
import org.wso2.siddhi.core.executor.ExpressionExecutor;
import org.wso2.siddhi.core.executor.function.FunctionExecutor;
import org.wso2.siddhi.core.util.config.ConfigReader;
import org.wso2.siddhi.query.api.definition.Attribute;

import java.util.HashMap;
import java.util.Map;

/**
 * @author jelly
 * <p>
 * 根据正则提取字段内容
 */
public class AviatorRegexFunctionExtension extends FunctionExecutor {

    @Override
    protected void init(ExpressionExecutor[] attributeExpressionExecutors, ConfigReader configReader, SiddhiAppContext siddhiAppContext) {
    }


    /**
     * 规定 data[1] 为 正则内容
     */
    @Override
    protected Object execute(Object[] data) {
        if (null != data && data.length == 2) {
            return AviatorEvaluator.execute("REG=~/" + data[1] + "/ ? $1 : '' ", new HashMap<String, Object>() {{
                put("REG", data[0]);
            }});
        }
        return null;
    }

    @Override
    protected Object execute(Object data) {
        return AviatorEvaluator.execute(data.toString());
    }

    @Override
    public Attribute.Type getReturnType() {
        return Attribute.Type.OBJECT;
    }

    @Override
    public Map<String, Object> currentState() {
        return new HashMap<>();
    }

    @Override
    public void restoreState(Map<String, Object> map) {

    }
}