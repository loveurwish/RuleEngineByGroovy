package com.groovyexample.groovy.bean;

import java.util.Map;
import lombok.Data;

@Data
public class RuleEngineExecuteContext {

  /**
   * 业务元id,例如订单id,案件id等
   */
  private String bizId;

  /**
   * 业务id,比如产品id或者产品组id
   */
  private String proId;

  /**
   * 策略组ID,供使用方使用一个产品对应多个策略组的情况下使用(比如进行分流策略时)
   */
  private Integer groupId;

  /**
   * 上下文数据
   */
  private Map<String,Object> data;

  /**
   * 是否是初始节点,用于第一个节点自动迭代
   */
  private boolean init = false;

}
