package com.top.mini.happy.down.util;

import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import java.lang.reflect.Method;

/**
 * Created by xugang on 17/9/5.
 */
public class SpelUtil {
    public static final ParameterNameDiscoverer parameterNameDiscoverer = new LocalVariableTableParameterNameDiscoverer();
    public static String getKey(Method method, Object[] args, String spel){
        EvaluationContext context = new StandardEvaluationContext();
        String[] parameterNames = parameterNameDiscoverer.getParameterNames(method);
        Object[] parameterValues = args;
        for (int i = 0; i < parameterNames.length; i++) {
            context.setVariable(parameterNames[i], parameterValues[i]);
        }
        ExpressionParser parser = new SpelExpressionParser();
        return parser.parseExpression(spel).getValue(context, String.class);
    }

    public static String getKey(Object obj, String spel){
        EvaluationContext context = new StandardEvaluationContext(obj);
        ExpressionParser parser = new SpelExpressionParser();
        return parser.parseExpression(spel).getValue(context, String.class);
    }
}
