package de.cimt;

import de.cimt.entity.Employee;
import de.cimt.repository.EmployeeRepository;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class CheckLeaveBalance implements JavaDelegate {
    private final EmployeeRepository employeeRepository;
    @Autowired
    public CheckLeaveBalance(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }
    @Override
    public void execute(DelegateExecution execution) throws Exception {
        // Access process variables
        String email = (String) execution.getVariable("email");
        Optional<Employee> employee = employeeRepository.findById(email);
        if (employee.isPresent()) {
            execution.setVariable("leave_balance", employee.get().getLeaveBalance());
            System.out.println(employee.get());
        } else {
            System.out.println("User not found");
        }

        // Perform your business logic here
        // ...

        // Set new process variables
    }
}
