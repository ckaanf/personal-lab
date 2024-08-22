package com.freely.nyodomain.service;

import org.springframework.stereotype.Service;

import com.freely.nyocore.core.Employee;
import com.freely.nyocore.core.User;
import com.freely.nyocore.repository.EmployeeRepository;
import com.freely.nyocore.repository.UserRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {
	private final UserRepository userRepository;
	private final EmployeeRepository employeeRepository;

	@Transactional
	public void testUser() {
		Employee employee = employeeRepository.save(new Employee("test"));

		User user = userRepository.findById(employee.getId()).orElseThrow();
		user.setName("안될리가");
	}
}
