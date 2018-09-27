package com.groovyexample.groovy.bean;

public enum VariableType {

  STRING(0,"String类型"),

  INT(1,"int类型");

  private final int value ;
  private final String desc ;

  VariableType(int value ,String desc){
    this.value = value ;
    this.desc = desc;
  }

  public int getValue() {
    return value;
  }

  public String getDesc() {
    return desc;
  }
}
