package com.gary;

import com.gary.employee.Employee;
import com.gary.strategy.AttendStrategy;
import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Dispatcher implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(Dispatcher.class);
    public static final Integer MAX_THREADS = 10;
    private Boolean active;
    private ExecutorService executorService;
    private ConcurrentLinkedDeque<Employee> employees;
    private ConcurrentLinkedDeque<Call> incomingCalls;
    private AttendStrategy attendStrategy;

    public Dispatcher(List<Employee> employees) {
        this(employees, new AttendStrategyImpl());
    }

    public Dispatcher(List<Employee> employees, AttendStrategy attendStrategy) {
        Validate.notNull(employees);
        Validate.notNull(attendStrategy);
        this.employees = new ConcurrentLinkedDeque(employees);
        this.attendStrategy = attendStrategy;
        this.incomingCalls = new ConcurrentLinkedDeque<>();
        this.executorService = Executors.newFixedThreadPool(MAX_THREADS);
    }

    public synchronized void dispatch(Call call) {
        logger.info("Dispatch new call of " + call.getDurationInSeconds() + " seconds");
        this.incomingCalls.add(call);
    }

    /**
     * Starts the employee threads and allows the dispatcher run method to execute
     */
    public synchronized void start() {
        this.active = true;
        for (Employee employee : this.employees) {
            this.executorService.execute(employee);
        }
    }

    public synchronized Boolean getActive() {
        return active;
    }

    /**
     * This is the method that runs on the thread.
     * If the incoming calls queue is not empty, then it searches for and available employee to take the call.
     * Calls will queue up until some workers becomes available.
     */
    @Override
    public void run() {
        while (getActive()) {
            if (this.incomingCalls.isEmpty()) {
                continue;
            } else {
                Employee employee = this.attendStrategy.findEmployee(this.employees);
                if (employee == null) {
                    continue;
                }
                Call call = this.incomingCalls.poll();
                try {
                    employee.attend(call);
                } catch (Exception e) {
                    logger.error(e.getMessage());
                    this.incomingCalls.addFirst(call);
                }
            }
        }
    }

}
