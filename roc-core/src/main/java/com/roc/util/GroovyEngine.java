package com.roc.util;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.roc.common.bytecode.MD5Utils;
import com.roc.common.text.ConfigUtils;
import groovy.lang.Binding;
import groovy.lang.GroovyClassLoader;
import groovy.lang.GroovyObject;
import groovy.lang.Script;
import groovy.util.GroovyScriptEngine;

import javax.script.*;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * @author : jelly.wang
 * @date : Created in 2021-01-26 下午10:15
 * @description: groovy执行引擎
 */
public final class GroovyEngine {
    private final ScriptEngine scriptEngine = new ScriptEngineManager().getEngineByName("groovy");
    private final GroovyClassLoader groovyClassLoader = new GroovyClassLoader();
    private final Cache<String, Script> GROOVY_SCRIPT_ENGINE_CACHE = CacheBuilder.newBuilder().expireAfterAccess(ConfigUtils.getInt("cache_size", 30), TimeUnit.MINUTES).build();
    private final Cache<String, GroovyObject> GROOVY_CLASS_ENGINE_CACHE = CacheBuilder.newBuilder().expireAfterAccess(ConfigUtils.getInt("cache_size", 30), TimeUnit.MINUTES).build();
    public final static GroovyEngine INSTANCE = new GroovyEngine();

    private GroovyEngine() {
    }

    /**
     * 执行脚本
     *
     * @param script
     * @param params
     * @param <T>
     * @return
     * @throws ScriptException
     */
    public <T> T runScript(String script, Map<String, Object> params) throws ScriptException {
        Bindings bindings = scriptEngine.createBindings();
        if (null != params) bindings.putAll(params);
        return (T) scriptEngine.eval(script, bindings);
    }

    /**
     * 执行函数
     *
     * @param script
     * @param method
     * @param params
     * @param <T>
     * @return
     * @throws ScriptException
     * @throws NoSuchMethodException
     */
    public <T> T runFunction(String script, String method, Object[] params) throws ScriptException, NoSuchMethodException {
        scriptEngine.eval(script);
        return (T) ((Invocable) scriptEngine).invokeFunction(method, params);
    }

    /**
     * 执行groovy文件
     *
     * @param filePath
     * @param fileName
     * @param method
     * @param params
     * @param <T>
     * @return
     * @throws Exception
     */
    public <T> T runFile(String filePath, String fileName, String method, Object[] params) throws Exception {
        Script script = GROOVY_SCRIPT_ENGINE_CACHE.get(fileName, () -> {
            GroovyScriptEngine engine = new GroovyScriptEngine(filePath);
            return engine.createScript(fileName, new Binding());
        });
        return (T) script.invokeMethod(method, params);
    }

    /**
     * 执行groovy类
     *
     * @param clazz
     * @param method
     * @param params
     * @param <T>
     * @return
     * @throws ExecutionException
     */
    public <T> T runClass(String clazz, String method, Object params) throws ExecutionException {
        GroovyObject groovyObject = GROOVY_CLASS_ENGINE_CACHE.get(MD5Utils.getMD5(clazz), () -> {
            Class groovyClass = groovyClassLoader.parseClass(clazz);
            return (GroovyObject) groovyClass.newInstance();
        });
        return (T) groovyObject.invokeMethod(method, params);
    }


    public static void main(String[] args) throws ExecutionException {
        Object test = GroovyEngine.INSTANCE.runClass("package com.jelly.test.groovy\n" +
                "\n" +
                "class Test {\n" +
                "\n" +
                "    def test(){\n" +
                "        return \"aaa\"\n" +
                "    }\n" +
                "}\n", "test", null);

        System.out.println(test);
    }
}
