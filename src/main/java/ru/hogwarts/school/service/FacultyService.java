package ru.hogwarts.school.service;

import org.springframework.stereotype.Service;
import ru.hogwarts.school.exception.NotFoundException;
import ru.hogwarts.school.model.Faculty;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class FacultyService {

    private final Map<Long, Faculty> faculties = new HashMap<>();
    private long nextId = 1;

    public Collection<Faculty> getAll() {
        return List.copyOf(faculties.values());
    }

    public Faculty getById(Long id) {
        Faculty faculty = faculties.get(id);

        if (faculty == null) {
            throw new NotFoundException("Faculty with " + id + " id not found");
        }

        return faculty;
    }

    public Faculty create(Faculty faculty) {
        long id = nextId++;
        faculty.setId(id);
        faculties.put(id, faculty);
        return faculty;
    }

    public Faculty update(Long id, Faculty faculty) {
        if (!faculties.containsKey(id)) {
            throw new NotFoundException("Faculty with " + id + " id not found");
        }
        faculty.setId(id);
        faculties.put(id, faculty);
        return faculty;
    }

    public Faculty delete(Long id) {
        Faculty removed = faculties.remove(id);
        if (removed == null) {
            throw new NotFoundException("Faculty with " + id + " id not found");
        }
        return removed;
    }

    public Collection<Faculty> getByColor(String color) {
        return faculties.values().stream()
                .filter(faculty -> faculty.getColor() != null && faculty.getColor().equalsIgnoreCase(color))
                .collect(Collectors.toList());
    }

}
