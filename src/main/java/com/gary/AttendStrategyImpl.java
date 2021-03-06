package com.gary;

import com.gary.dice.Dice;
import com.gary.dice.DiceStatus;
import com.gary.employee.Employee;
import com.gary.employee.EmployeeStatus;
import com.gary.employee.EmployeeType;
import com.gary.strategy.AttendStrategy;
import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class AttendStrategyImpl implements AttendStrategy {

    private static final Logger logger = LoggerFactory.getLogger(AttendStrategyImpl.class);

    @Override
    public Employee findEmployee(Collection<Employee> employeeList) {
        Validate.notNull(employeeList);
        List<Employee> availableEmployees = employeeList.stream().filter(e -> e.getEmployeeState() == EmployeeStatus.AVAILABLE).collect(Collectors.toList());
        logger.info("Available Employee: " + availableEmployees.size());
        Optional<Employee> employee = availableEmployees.stream().filter(e -> e.getEmployeeType() == EmployeeType.FRESHER).findAny();
        if (!employee.isPresent()) {
            logger.info("No available fresher found");

            DiceStrategyImpl diceStrategy
                     = new DiceStrategyImpl();
            String result =  diceStrategy.roll(new Dice(),new Dice());
            if(result.equals(DiceStatus.WIN.toString())){
                logger.info("Roll the dice and win");
                employee = availableEmployees.stream().filter(e -> e.getEmployeeType() == EmployeeType.TECHNICAL).findAny();
                if (!employee.isPresent()) {
                    logger.info("No available technical found");
                    employee = availableEmployees.stream().filter(e -> e.getEmployeeType() == EmployeeType.PRODUCT_MANAGER).findAny();
                    if (!employee.isPresent()) {
                        logger.info("No available product manger found");
                    }
                }
                logger.info("Employee of type " + employee.get().getEmployeeType() + " found");
            }

            logger.info("Roll the dice and lose");
            logger.info("Employee of type not found");
        }
        return employee.get();
    }

}
