package com.dto;

public class AplayDto {
	public String type; // 0会员费，1为押金，2为租金
	public String amount;
	public String billCheckId; // 如果是支付租金，则需要此字段，不是租金，忽略此字段
}
