package com.wudgaby.platform.flowable.helper.cmd;

/**
 * @ClassName : CustomerElCmd
 * @Author :  WudGaby
 * @Version :  1.0
 * @Date : 2020/2/29 15:44
 * @Desc :
 */

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.flowable.common.engine.api.FlowableException;
import org.flowable.common.engine.api.delegate.Expression;
import org.flowable.common.engine.impl.el.ExpressionManager;
import org.flowable.common.engine.impl.interceptor.Command;
import org.flowable.common.engine.impl.interceptor.CommandContext;
import org.flowable.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.flowable.engine.impl.persistence.entity.ExecutionEntity;
import org.flowable.engine.impl.persistence.entity.ExecutionEntityImpl;
import org.flowable.engine.impl.util.CommandContextUtil;
import org.flowable.variable.api.history.HistoricVariableInstance;

import java.util.List;

public class CustomerElCmd<T> implements Command<T> {
    private List<HistoricVariableInstance> historicVariableInstanceList;
    private String exp;
    public CustomerElCmd(String exp, List<HistoricVariableInstance> historicVariableInstanceList) {
        this.exp = exp;
        this.historicVariableInstanceList = historicVariableInstanceList;
    }

    @Override
    public T execute(CommandContext commandContext) {
        ProcessEngineConfigurationImpl processEngineConfiguration = CommandContextUtil.getProcessEngineConfiguration(commandContext);
        ExpressionManager expressionManager = processEngineConfiguration.getExpressionManager();

        if(StringUtils.isBlank(exp)){
            return (T)exp;
        }

        Expression expr = expressionManager.createExpression(exp);
        ExecutionEntity variableScope = ExecutionEntityImpl.createWithEmptyRelationshipCollections();

        if(CollectionUtils.isNotEmpty(historicVariableInstanceList)){
            for (HistoricVariableInstance variableInstance : historicVariableInstanceList) {
                variableScope.setVariable(variableInstance.getVariableName(), variableInstance.getValue());
            }
        }
        try{
            return (T) expr.getValue(variableScope);
        }catch (FlowableException fe) {
            fe.printStackTrace();
        }
        return null;
    }
}
