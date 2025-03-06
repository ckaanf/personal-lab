package com.freely.nyocore.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.freely.nyocore.core.Employee;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
}