package com.groovyexample.groovy.bean;

import java.io.Serializable;
/**
 * Script variable
 */
public class ScriptVariable implements Serializable {

  private static final long serialVersionUID = 8432423423082665246L;

  /**
   * 变量名称
   */
  private String name;

  /**
   * 变量类型
   */
  private VariableType type;

  public VariableType getType() {
    return type;
  }

  public void setType(VariableType type) {
    this.type = type;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  @Override
  public String toString() {
    final StringBuilder sb = new StringBuilder("ScriptVariable{");
    sb.append("name='").append(name).append('\'');
    sb.append(", type='").append(type).append('\'');
    sb.append('}');
    return sb.toString();
  }
}