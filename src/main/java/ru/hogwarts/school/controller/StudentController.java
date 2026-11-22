package ru.hogwarts.school.controller;

import org.springframework.web.bind.annotation.*;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.service.StudentService;

import java.util.Collection;
import java.util.List;

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

    @GetMapping("/age")
    public Collection<Student> getByAge(@RequestParam int age) {
        return studentService.getByAge(age);
    }

    @GetMapping("/age/between")
    public Collection<Student> getByAgeBetween(@RequestParam int min, @RequestParam int max) {
        return studentService.getByAgeBetween(min, max);
    }

    @GetMapping("/{id}/faculty")
    public Faculty getFaculty(@PathVariable Long id) {
        return studentService.getFaculty(id);
    }

    @GetMapping("/count")
    public long getStudentsCount() {
        return studentService.getStudentsCount();
    }

    @GetMapping("/average_age")
    public double getAverageAge() {
        return studentService.getAverageAge();
    }
    @GetMapping("/last_five")
    public List<Student> getLastFiveStudents() {
        return studentService.getLastFiveStudents();
    }

    @GetMapping("/students/print-parallel")
    public void printParallel() {
        List<Student> students = studentService.getAll().stream().limit(6).toList();

        System.out.println(students.getFirst().getName());
        System.out.println(students.get(1).getName());
        Thread thread1 = new Thread(() -> {
            System.out.println(students.get(2).getName());
            System.out.println(students.get(3).getName());
        });
        Thread thread2 = new Thread(() -> {
            System.out.println(students.get(4).getName());
            System.out.println(students.get(5).getName());
        });
        thread1.start();
        thread2.start();
    }

    @GetMapping("/students/print-synchronized")
    public void printSync() {
        List<Student> students = studentService.getAll().stream().limit(6).toList();

        printNameSync(students.getFirst().getName());
        printNameSync(students.get(1).getName());
        Thread thread1 = new Thread(() -> {
            printNameSync(students.get(2).getName());
            printNameSync(students.get(3).getName());
        });
        Thread thread2 = new Thread(() -> {
            printNameSync(students.get(4).getName());
            printNameSync(students.get(5).getName());
        });
        thread1.start();
        thread2.start();
    }

    private synchronized void printNameSync(String name) {
        System.out.println(name);
    }

}
//(Not enough minerals.)x3 You must construct additional pylons! <Nyon>