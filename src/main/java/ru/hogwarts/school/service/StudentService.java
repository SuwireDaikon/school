package ru.hogwarts.school.service;

import org.springframework.stereotype.Service;
import ru.hogwarts.school.exception.NotFoundException;
import ru.hogwarts.school.model.Student;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class StudentService {

    private final Map<Long, Student> students = new HashMap<>();
    private long nextId = 1;

    public Collection<Student> getAll() {
        return List.copyOf(students.values());
    }

    public Student getById(Long id) {
        Student student = students.get(id);

        if (student == null) {
            throw new NotFoundException("Student with " + id + " id not found");
        }

        return student;
    }

    public Student create(Student student) {
        long id = nextId++;
        student.setId(id);
        students.put(id, student);
        return student;
    }

    public Student update(Long id, Student student) {
        if (!students.containsKey(id)) {
            throw new NotFoundException("Student with " + id + " id not found");
        }
        student.setId(id);
        students.put(id, student);
        return student;
    }

    public Student delete(Long id) {
        Student removed = students.remove(id);
        if (removed == null) {
            throw new NotFoundException("Student with " + id + " id not found");
        }
        return removed;
    }

    public Collection<Student> getByAge(int age) {
        return students.values().stream()
                .filter(student -> student.getAge() == age)
                .collect(Collectors.toList());
    }
}
