package com.sportsfit.shop.service;

import com.sportsfit.shop.repository.EmployeeDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;



@Service
public class EmployeeServiceImpl implements EmployeeService {

	@Autowired
	private EmployeeDao dao;

	public List<EmployeeCommonDto> getEmployeesList(EmployeeCommonDto dto) {
		return dao.getEmployeesList(dto);
	}

	@Override
	public EmployeeCommonDto getEmployees(int employeeId) {
		
		return dao.getEmployees(employeeId);
	}

	@Override
	public int register(Employees emp) {
		return dao.register(emp);
	}

}