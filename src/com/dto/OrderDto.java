package com.dto;

import java.util.ArrayList;
import java.util.List;

public class OrderDto {
	public String allMoney;
	public String detailLocation;
	public String city;
	public String endTime;
	public String leaseType;
	public String province;
	public String needInvoices;
	public String rentType;
	public String startTime;
	public String rentDuraion;		//SUNZHE, 2017-03-26
	public String totalDeposit;		//SUNZHE, 2017-03-26
	public String totalInsurance;	//SUNZHE, 2017-03-26
	public InOrderDto rentSideInfo;
	public List<OrderDeviceDto> subOrders = new ArrayList<OrderDeviceDto>();
}
