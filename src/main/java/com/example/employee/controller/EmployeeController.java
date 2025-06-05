package com.example.employee.controller;

import com.example.employee.model.Employee;
import com.example.employee.repository.EmployeeRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/employees")
@CrossOrigin(origins = "http://localhost:3000") // allow React frontend to call backend
public class EmployeeController {

    @Autowired
    private EmployeeRepository repository;

    // Get all employees
    @GetMapping
    public List<Employee> getAllEmployees() {
        return repository.findAll();
    }

    // Add new employee
    @PostMapping
    public Employee addEmployee(@RequestBody Employee employee) {
        return repository.save(employee);
    }

    // Update employee by id
    @PutMapping("/{id}")
    public Employee updateEmployee(@PathVariable Long id, @RequestBody Employee employee) {
        return repository.findById(id).map(emp -> {
            emp.setName(employee.getName());
            emp.setEmail(employee.getEmail());
            emp.setDepartment(employee.getDepartment());
            emp.setSalary(employee.getSalary());
            return repository.save(emp);
        }).orElseThrow(() -> new RuntimeException("Employee not found with id " + id));
    }

    // Delete employee by id
    @DeleteMapping("/{id}")
    public void deleteEmployee(@PathVariable Long id) {
        repository.deleteById(id);
    }

    // Search employees by name or department
    @GetMapping("/search")
    public List<Employee> searchEmployees(@RequestParam String keyword) {
        List<Employee> byName = repository.findByNameContainingIgnoreCase(keyword);
        List<Employee> byDept = repository.findByDepartmentContainingIgnoreCase(keyword);
        byDept.forEach(e -> {
            if (!byName.contains(e)) byName.add(e);
        });
        return byName;
    }
}
