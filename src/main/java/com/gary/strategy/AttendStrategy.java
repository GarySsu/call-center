package com.gary.strategy;

import com.gary.employee.Employee;

import java.util.Collection;

/**
 * Models different strategies on which is the next Employee available to work
 */
public interface AttendStrategy {

    /**
     * Finds next available employee
     *
     * @param employeeList
     * @return available employee, or null if all employees are busy
     */
    Employee findEmployee(Collection<Employee> employeeList);

}
