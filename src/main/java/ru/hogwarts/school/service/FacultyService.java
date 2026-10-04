package ru.hogwarts.school.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.hogwarts.school.exception.NotFoundException;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.repository.FacultyRepository;

import java.util.*;

@Service
public class FacultyService {

    private final FacultyRepository facultyRepository;

    @Autowired
    public FacultyService(FacultyRepository facultyRepository) {
        this.facultyRepository = facultyRepository;
    }

    public Collection<Faculty> getAll() {
        return facultyRepository.findAll();
    }

    public Faculty getById(Long id) {
        return facultyRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Faculty with " + id + " id not found"));
    }

    public Faculty create(Faculty faculty) {
        return facultyRepository.save(faculty);
    }

    public Faculty update(Long id, Faculty faculty) {
        if (!facultyRepository.existsById(id)) {
            throw new NotFoundException("Faculty with " + id + " id not found");
        }
        faculty.setId(id);
        return facultyRepository.save(faculty);
    }

    public Faculty delete(Long id) {
        Faculty removed = getById(id);
        if (removed == null) {
            throw new NotFoundException("Faculty with " + id + " id not found");
        }
        facultyRepository.delete(removed);
        return removed;
    }

    public Collection<Faculty> getByColor(String color) {
        return facultyRepository.findAllByColorIgnoreCase(color);
    }
}
