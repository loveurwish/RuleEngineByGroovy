package com.groovyexample.groovy.service.impl;

import com.groovyexample.groovy.bean.ScriptVariable;
import com.groovyexample.groovy.bean.RuleEngineExecuteContext;
import com.groovyexample.groovy.bean.StrategyScriptEntity;
import com.groovyexample.groovy.service.EngineGroovyModuleRule;
import com.groovyexample.groovy.service.GroovyScriptService;
import com.groovyexample.groovy.service.StrategyScriptDataSouce;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

@Slf4j
@Service
public class GroovyScriptServiceImpl implements GroovyScriptService, InitializingBean {

  @Autowired
  private RuleEngineGroovyModuleRuleExecutor groovyModuleRuleExecutor;
  @Autowired
  private StrategyScriptDataSouce strategyScriptDataSouce;

  @Override
  public void afterPropertiesSet() throws Exception {
    log.info("collection-engine start loading valid script...");
    int count = loadValidScript(true);
    log.info("collection-engine finish load {} script", count);
  }

  @Override
  public Object fragmentEval(RuleEngineExecuteContext context, Integer strategyId) {
    EngineGroovyModuleRule engineGroovyModuleRule = groovyModuleRuleExecutor
      .getInstance("enginesScript_" + strategyId);
    return engineGroovyModuleRule
      .run(context.getData().get("context"), context.getData().get("result"));
  }

  @Override
  public boolean booleanScript(RuleEngineExecuteContext context, Integer strategyId) {
    return (Boolean) this.fragmentEval(context, strategyId);
  }

  @Override
  public void saveVariables(Integer strategyId, String script, List<ScriptVariable> variables,
    String author) {

    StrategyScriptEntity strategyScriptEntity = new StrategyScriptEntity();
    Date date = new Date();
    if (CollectionUtils.isEmpty(variables)) {
      throw new IllegalArgumentException("Add script failed , script variables is empty");
    }
    String variableStr = variables.toString();
    strategyScriptEntity.setScript(script);
    strategyScriptEntity.setStrategyId(strategyId);
    strategyScriptEntity.setUpdateTime(date);
    strategyScriptEntity.setAuthor(author);
    strategyScriptEntity.setCreateTime(date);
    strategyScriptEntity.setVariables(variableStr);
    strategyScriptDataSouce.saveScript(strategyScriptEntity);
  }

  @Override
  public StrategyScriptEntity queryByStrategyId(Integer strategyId) {
    return strategyScriptDataSouce.queryByStrategyId(strategyId);
  }

  @Override
  public List<StrategyScriptEntity> queryByStrategyIds(List<Integer> strategyIds) {
    return strategyScriptDataSouce.queryByStrategyIds(strategyIds);
  }

  @Override
  public boolean scriptTest(String scriptText) {
    try {
      groovyModuleRuleExecutor
        .praseAndCache("enginescript_" + Math.abs(scriptText.hashCode()), scriptText);
      return true;
    } catch (Exception ex) {
      log.error("testing failed", ex);
      throw new RuntimeException("testing failed", ex);
    }
  }

  @Override
  public void loadScript() {
    List<StrategyScriptEntity> list = strategyScriptDataSouce.queryAll();
    list.forEach(item -> {
      String scriptText = item.getScript();
      Integer strategyId = item.getStrategyId();
      groovyModuleRuleExecutor.praseAndCache("enginesScript_" + strategyId, scriptText);
    });
  }

  @Override
  public int loadValidScript(boolean isInit) {
    //TODO add some filter to get right valid scripts
    List<StrategyScriptEntity> scriptList = strategyScriptDataSouce.queryAll().parallelStream()
      .collect(Collectors.toList());

    if (CollectionUtils.isEmpty(scriptList)) {
      log.info("invalid script");
      return 0;
    }
    scriptList.parallelStream().forEach(item -> {
      String scriptText = item.getScript();
      Integer strategyId = item.getStrategyId();
      groovyModuleRuleExecutor.praseAndCache("enginesScript_" + strategyId, scriptText);
    });

    return scriptList.size();
  }

}
