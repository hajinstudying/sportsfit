package com.sportsfit.shop.service;

import java.util.List;


public interface EmployeeService {

	List<EmployeeCommonDto> getEmployeesList(EmployeeCommonDto dto);
	EmployeeCommonDto getEmployees(int employeeId);
	int register(Employees emp);

}