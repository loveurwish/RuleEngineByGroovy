package com.groovyexample.groovy.service;

import com.groovyexample.groovy.bean.StrategyScriptEntity;
import java.util.List;

public interface StrategyScriptDataSouce {

  /**
   * 根据策略id查询对应表达式片段信息
   */
  StrategyScriptEntity queryByStrategyId(Integer strategyId);


  List<StrategyScriptEntity> queryByStrategyIds(List<Integer> strategyIds );
  /**
   * 保存策略的执行片段
   */
  boolean saveScript(StrategyScriptEntity strategyScriptEntity);

  /**
   * 查询所有的表达式片段
   */
  List<StrategyScriptEntity> queryAll();
}
