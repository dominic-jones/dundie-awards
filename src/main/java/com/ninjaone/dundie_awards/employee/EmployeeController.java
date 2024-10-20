package com.ninjaone.dundie_awards.employee;

import com.ninjaone.dundie_awards.activity.ActivityRepository;
import com.ninjaone.dundie_awards.activity.MessageBroker;
import com.ninjaone.dundie_awards.organization.AwardsCache;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;

@RestController
@RequestMapping("/employees")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private ActivityRepository activityRepository;

    @Autowired
    private MessageBroker messageBroker;

    @Autowired
    private AwardsCache awardsCache;

    @GetMapping
    public List<EmployeeRecord> getAllEmployees() {
        return employeeService.getAllEmployees();
    }

    @PostMapping
    @ResponseStatus(CREATED)
    public EmployeeRecord createEmployee(@RequestBody Employee employee) {
        return employeeService.createEmployee(employee);
    }

    @GetMapping("/{id}")
    public EmployeeRecord getEmployeeById(@PathVariable long id) {
        return employeeService.getEmployee(id);
    }

    @PutMapping("/{id}")
    public @NotNull EmployeeRecord updateEmployee(@PathVariable long id,
                                                  @RequestBody @NotNull UpdateEmployeeRequest request) {
        return employeeService.updateEmployee(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(NO_CONTENT)
    public void deleteEmployee(@PathVariable long id) {
        employeeService.deleteEmployee(id);
    }
}
