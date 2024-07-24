package de.cimt.service;

import de.cimt.entity.Employee;
import de.cimt.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CheckLeaveBalance implements JavaDelegate {
    private final EmployeeRepository employeeRepository;
    @Override
    public void execute(DelegateExecution execution) throws Exception {

        String email = (String) execution.getVariable("email");
        Optional<Employee> employee = employeeRepository.findById(email);
        if (employee.isPresent()) {
            execution.setVariable("leave_balance", employee.get().getLeaveBalance());
        } else {
            System.out.println("User not found");
        }

    }
}
