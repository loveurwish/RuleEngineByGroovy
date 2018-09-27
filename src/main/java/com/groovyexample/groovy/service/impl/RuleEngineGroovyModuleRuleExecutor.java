package com.groovyexample.groovy.service.impl;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import com.groovyexample.groovy.service.RuleEngineGroovyExecutor;
import com.groovyexample.groovy.service.EngineGroovyModuleRule;
import com.groovyexample.groovy.service.GroovyScriptTemplate;
import com.groovyexample.utils.MD5Utils;
import groovy.lang.GroovyClassLoader;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class RuleEngineGroovyModuleRuleExecutor implements
  RuleEngineGroovyExecutor<EngineGroovyModuleRule> {

  private Map<String, Class<EngineGroovyModuleRule>> nameAndClass = Maps.newConcurrentMap();

  private Map<String, String> nameAndMd5 = Maps.newConcurrentMap();

  @Autowired
  private GroovyScriptTemplate groovyScriptTemplate;

  @Override
  public EngineGroovyModuleRule getInstance(String name) {
    try {
      Class<EngineGroovyModuleRule> aClass = nameAndClass.get(name);
      if (aClass == null) {
        throw new IllegalArgumentException(String.format("script:%s not load", name));
      }
      return aClass.newInstance();
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
  @SuppressWarnings("unchecked")
  @Override
  public void praseAndCache(String name, String script) {
    try {
      Preconditions.checkNotNull(name);
      Preconditions.checkNotNull(script);
      String scriptBuilder = groovyScriptTemplate.getScript("ScriptTemplate.groovy_template");
      String scriptClassName = RuleEngineGroovyModuleRuleExecutor.class.getSimpleName() + "_" + name;
      String fullScript = String.format(scriptBuilder, scriptClassName, script);

      String oldMd5 = nameAndMd5.get(name);
      String newMd5 = MD5Utils.getStringMD5(fullScript);
      if (oldMd5 != null && oldMd5.equals(newMd5)) {
        return;
      }

      GroovyClassLoader classLoader = new GroovyClassLoader();
      Class aClass = classLoader.parseClass(fullScript);
      log.info("collection-engine load script:{} finish", name);
      nameAndClass.put(name, aClass);
      nameAndMd5.put(name, newMd5);
    } catch (Exception e){
      throw new RuntimeException(e);
    }
  }


}
