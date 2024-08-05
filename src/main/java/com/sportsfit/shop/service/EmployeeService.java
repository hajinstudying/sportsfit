package com.sportsfit.shop.service;

import com.sportsfit.shop.vo.EmployeeCommonDto;
import com.sportsfit.shop.vo.Employees;

import java.util.List;


public interface EmployeeService {

	List<EmployeeCommonDto> getEmployeesList(EmployeeCommonDto dto);
	EmployeeCommonDto getEmployees(int employeeId);
	int register(Employees emp);

}