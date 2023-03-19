package com.jumpjump.bean;

import lombok.Data;


/**
 * 载具对象
 */
@Data
public class Vehicles {
  private String vehicleName;
  private String type;
  private String image;
  private long kills;
  private double killsPerMinute;
  private long destroyed;  // 摧毁了
  private long timeIn;
}
