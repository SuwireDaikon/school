package ru.hogwarts.school.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.hogwarts.school.model.Faculty;

import java.util.List;

public interface FacultyRepository extends JpaRepository<Faculty, Long> {
    List<Faculty> findAllByColorIgnoreCase(String color);

    List<Faculty> findAllByNameOrColorAllIgnoreCase(String name, String color);
}
//За моего отца!