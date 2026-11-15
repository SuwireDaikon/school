package ru.hogwarts.school.service;

import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.hogwarts.school.exception.NotFoundException;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.FacultyRepository;

import java.util.*;

@Service
public class FacultyService {

    private static final Logger logger = LoggerFactory.getLogger(FacultyService.class);

    private final FacultyRepository facultyRepository;

    @Autowired
    public FacultyService(FacultyRepository facultyRepository) {
        this.facultyRepository = facultyRepository;
    }

    public Collection<Faculty> getAll() {
        logger.info("Was invoked method for get all faculties");
        return facultyRepository.findAll();
    }

    public Faculty getById(Long id) {
        logger.info("Was invoked method for get faculty by id {} ", id);
        return facultyRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Faculty with such id: {} not found ", id);
                    return new NotFoundException("Faculty with " + id + " id not found");});
    }

    public Faculty create(Faculty faculty) {
        logger.info("Was invoked method to create faculty");
        return facultyRepository.save(faculty);
    }

    public Faculty update(Long id, Faculty faculty) {
        logger.info("Was invoked method for update faculty");
        if (!facultyRepository.existsById(id)) {
            logger.error("Faculty with id: {} not found", id);
            throw new NotFoundException("Faculty with " + id + " id not found");
        }
        faculty.setId(id);
        return facultyRepository.save(faculty);
    }

    public Faculty delete(Long id) {
        logger.info("Was invoked method for delete the faculty");
        Faculty removed = getById(id);
        if (removed == null) {
            logger.error("Faculty with id: {} does not exist", id);
            throw new NotFoundException("Faculty with " + id + " id not found");
        }
        facultyRepository.delete(removed);
        return removed;
    }

    public Collection<Faculty> getByColor(String color) {
        logger.info("Was invoked method for get faculty by color");
        return facultyRepository.findAllByColorIgnoreCase(color);
    }

    public Collection<Faculty> getByNameOrColorIgnoreCaseBoth(String request) {
        logger.info("Was invoked method for get faculty by name or color with case ignore");
        return facultyRepository.findByNameContainingIgnoreCaseOrColorContainingIgnoreCase(request, request);
    }

    @Transactional
    public Collection<Student> getStudents(Long id) {
        logger.info("Was invoked method for get students by faculty id");
        return getById(id).getStudents();
    }

}
