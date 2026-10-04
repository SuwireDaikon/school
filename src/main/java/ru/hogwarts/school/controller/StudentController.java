package ru.hogwarts.school.controller;

import org.springframework.web.bind.annotation.*;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.service.StudentService;

import java.util.Collection;

@RestController
@RequestMapping("/student")
public class StudentController {
    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping
    public Collection<Student> getAll() {
        return studentService.getAll();
    }

    @GetMapping("/{id}")
    public Student getById(@PathVariable long id) {
        return studentService.getById(id);
    }

    @PostMapping
    public Student create(@RequestBody Student student) {
        return  studentService.create(student);
    }

    @PutMapping("/{id}")
    public Student update(@PathVariable Long id, @RequestBody Student student) {
        return studentService.update(id, student);
    }

    @DeleteMapping("/{id}")
    public Student delete(@PathVariable Long id) {
        return studentService.delete(id);
    }

    @GetMapping("/age/{age}")
    public Collection<Student> getByAge(@PathVariable int age) {
        return studentService.getByAge(age);
    }
}
//ForgottenPullRequest...