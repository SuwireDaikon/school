package ru.hogwarts.school.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.hogwarts.school.exception.NotFoundException;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.StudentRepository;


import java.util.*;

@Service
public class StudentService {

    private static final Logger logger = LoggerFactory.getLogger(StudentService.class);

    private final StudentRepository studentRepository;

    @Autowired
    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public Collection<Student> getAll() {
        logger.info("Was invoked method for get all students");
        return studentRepository.findAll();
    }

    public Student getById(Long id) {
        logger.info("Was invoked method for get students by id");
        return studentRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("There is no student with such id: {} to get ", id);
                    return new NotFoundException("Student with " + id + " id not found");
                });
    }

    public Student create(Student student) {
        logger.info("Was invoked method for create student");
        logger.debug("Creating student with name: {}", student.getName());
        return studentRepository.save(student);
    }

    public Student update(Long id, Student student) {
        logger.info("Was invoked method for update student");
        if (!studentRepository.existsById(id)) {
            logger.error("There is no student with such id: {} to update ", id);
            throw new NotFoundException("Student with " + id + " id not found");
        }
        logger.warn("Overwriting student with id: {}", id);
        student.setId(id);
        return studentRepository.save(student);
    }

    public Student delete(Long id) {
        logger.info("Was invoked method for delete student");
        logger.warn("Deleting student with id: {}", id);
        Student removed = getById(id);
        if (removed == null) {
            logger.error("There is no student with such id: {} to delete ", id);
            throw new NotFoundException("Student with " + id + " id not found");
        }
        studentRepository.delete(removed);
        logger.info("Student with id: {} was removed", id);
        return removed;
    }

    public Collection<Student> getByAge(int age) {
        logger.info("Was invoked method for get students by age");
        return studentRepository.findAllByAge(age);
    }

    public Collection<Student> getByAgeBetween(int min, int max) {
        logger.info("Was invoked method for get students by age between min and max");
        return studentRepository.findByAgeBetween(min, max);
    }

    public Faculty getFaculty(Long id) {
        logger.info("Was invoked method for get a faculty by student id");
        Faculty faculty = getById(id).getFaculty();
        if (faculty == null) {
            logger.error("Student with id: {} has no faculty", id);
            throw new NotFoundException("Student with " + id + " id has no faculty");
        }
        return faculty;
    }

    public long getStudentsCount() {
        logger.info("Was invoked method for get students count");
        return studentRepository.countAllStudents();
    }

    public double getAverageAge() {
        logger.info("Was invoked method for get average age of students");
        Double averageAge = studentRepository.findAverageAge();
        if (averageAge == null) {
            logger.debug("Average age is null, returning 0.0");
            return 0.0;
        }
        return averageAge;
    }

    public List<Student> getLastFiveStudents() {
        logger.info("Was invoked method for get last five students");
        return studentRepository.findLastFiveStudents();
    }

}
//This toaster is... broken.