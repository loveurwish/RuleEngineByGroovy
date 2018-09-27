package com.groovyexample.groovy.service;


import com.groovyexample.groovy.bean.ScriptVariable;
import com.groovyexample.groovy.bean.RuleEngineExecuteContext;
import com.groovyexample.groovy.bean.StrategyScriptEntity;
import java.util.List;

public interface GroovyScriptService {

  Object  fragmentEval(RuleEngineExecuteContext context,Integer strategyId);

  boolean booleanScript(RuleEngineExecuteContext context,Integer strategyId);
  /**
   * 存储执行脚本片段和变量
   * @param strategyId
   * @param script
   * @param variables
   */
  void saveVariables(Integer strategyId, String script, List<ScriptVariable> variables, String author);

  StrategyScriptEntity queryByStrategyId(Integer strategyId);

  List<StrategyScriptEntity> queryByStrategyIds(List<Integer> strategyIds);

  /**
   * 测试脚本
   * @param scriptText
   * @return
   */
  boolean scriptTest(String scriptText);

  /**
   * 加载数据库所有脚本
   */
  void loadScript();

  /**
   * 加载数据库所有未停用的脚本
   * @param isInit 是否初始化调用
   * @return 成功加载脚本个数
   */
  int loadValidScript(boolean isInit);
}
