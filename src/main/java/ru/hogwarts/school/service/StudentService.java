package ru.hogwarts.school.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.hogwarts.school.exception.NotFoundException;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.StudentRepository;

import java.util.*;

@Service
public class StudentService {

    private final StudentRepository studentRepository;

    @Autowired
    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public Collection<Student> getAll() {
        return studentRepository.findAll();
    }

    public Student getById(Long id) {
        return studentRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Student with " + id + " id not found"));
    }

    public Student create(Student student) {
        return studentRepository.save(student);
    }

    public Student update(Long id, Student student) {
        if (!studentRepository.existsById(id)) {
            throw new NotFoundException("Student with " + id + " id not found");
        }
        student.setId(id);
        return studentRepository.save(student);
    }

    public Student delete(Long id) {
        Student removed = getById(id);
        if (removed == null) {
            throw new NotFoundException("Student with " + id + " id not found");
        }
        studentRepository.delete(removed);
        return removed;
    }

    public Collection<Student> getByAge(int age) {
        return studentRepository.findAllByAge(age);
    }
}
//ForgottenPullRequest...