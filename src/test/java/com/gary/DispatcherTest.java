package com.gary;

import com.gary.employee.Employee;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class DispatcherTest {

    private static final Logger logger = LoggerFactory.getLogger(DispatcherTest.class);

    private static final int TOTAL_AMOUNT = 10;
    private static final int MIN_DURATION = 5;
    private static final int MAX_DURATION = 10;

    @Test(expected = NullPointerException.class)
    public void testDispatcherCreationWithNullEmployees() {
        new Dispatcher(null);
    }

    @Test(expected = NullPointerException.class)
    public void testDispatcherCreationWithNullStrategy() {
        new Dispatcher(new ArrayList<>(), null);
    }

    @Test
    public void testDispatchCallsToEmployees() throws InterruptedException {
        List<Employee> employeeList = buildEmployeeList();
        Dispatcher dispatcher = new Dispatcher(employeeList);
        dispatcher.start();
        TimeUnit.SECONDS.sleep(1);
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(dispatcher);
        TimeUnit.SECONDS.sleep(1);

        buildCallList().stream().forEach(call -> {
            dispatcher.dispatch(call);
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                fail();
            }
        });

        // close thread
        executorService.awaitTermination(MAX_DURATION * 2, TimeUnit.SECONDS);
        assertEquals(TOTAL_AMOUNT, employeeList.stream().mapToInt(employee -> employee.getAttendedCalls().size()).sum());
    }

    private static List<Employee> buildEmployeeList() {
        Employee fresher1 = Employee.buildFresher();
        Employee fresher2 = Employee.buildFresher();
        Employee fresher3 = Employee.buildFresher();
        Employee fresher4 = Employee.buildFresher();
        Employee fresher5 = Employee.buildFresher();
        Employee fresher6 = Employee.buildFresher();
        Employee technical = Employee.buildTechnical();
        Employee productManager = Employee.buildProductManager();
        return Arrays.asList(fresher1, fresher2, fresher3, fresher4, fresher5, fresher6,
                technical, productManager);
    }

    private static List<Call> buildCallList() {
        return Call.buildListOfRandomCalls(TOTAL_AMOUNT, MIN_DURATION, MAX_DURATION);
    }

}
