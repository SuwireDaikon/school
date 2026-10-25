package ru.hogwarts.school.controller;


import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.resttestclient.TestRestTemplate;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureTestRestTemplate;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.FacultyRepository;
import ru.hogwarts.school.repository.StudentRepository;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestRestTemplate
class FacultyControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private FacultyRepository facultyRepository;

    private Student student;
    private Faculty faculty;

    @BeforeEach
    void setUp() {
        faculty = new Faculty();
        faculty.setName("Ravenclaw");
        faculty.setColor("blue");
        faculty = facultyRepository.save(faculty);

        student = new Student();
        student.setName("Owl");
        student.setAge(10);
        student.setFaculty(faculty);
        student = studentRepository.save(student);
    }

    @AfterEach
    void tearDown() {
        studentRepository.deleteAll();
        facultyRepository.deleteAll();
    }

    private String url(String path) {
        return "http://localhost:" + port + path;
    }

    @Test
    void testGetAll() {
        ResponseEntity<List<Faculty>> response = testRestTemplate.exchange(url("/faculty"), HttpMethod.GET, null, new ParameterizedTypeReference<>(){});
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assert response.getBody() != null;
    }

    @Test
    void testGetById() {
        ResponseEntity<Faculty> response = testRestTemplate.getForEntity(url("/faculty/" + faculty.getId()), Faculty.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assert response.getBody() != null;
        assertThat(response.getBody().getName()).isEqualTo("Ravenclaw");
    }

    @Test
    void testCreate() {
        Faculty newFaculty = new Faculty();
        newFaculty.setName("Slytherin");
        newFaculty.setColor("green");
        ResponseEntity<Faculty> response = testRestTemplate.postForEntity(url("/faculty"), newFaculty, Faculty.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assert response.getBody() != null;
        assertThat(response.getBody().getId()).isNotNull();
        assertThat(response.getBody().getName()).isEqualTo("Slytherin");
    }

    @Test
    void testUpdate() {
        faculty.setName("Slytherin Updated");
        faculty.setColor("DarkerGreen");

        HttpEntity<Faculty> entity = new HttpEntity<>(faculty);
        ResponseEntity<Faculty> response = testRestTemplate.exchange(url("/faculty/" + faculty.getId()), HttpMethod.PUT, entity, Faculty.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assert response.getBody() != null;
        assertThat(response.getBody().getName()).isEqualTo("Slytherin Updated");
        assertThat(response.getBody().getColor()).isEqualTo("DarkerGreen");
    }

    @Test
    void testDelete() {
        studentRepository.deleteAll();
        ResponseEntity<Faculty> response = testRestTemplate.exchange(url("/faculty/" + faculty.getId()), HttpMethod.DELETE, null, Faculty.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assert response.getBody() != null;
        assertThat(facultyRepository.findById(faculty.getId())).isEmpty();
    }

    @Test
    void testGetByColor() {
        ResponseEntity<List<Faculty>> response = testRestTemplate.exchange(url("/faculty/color/blue"), HttpMethod.GET, null, new ParameterizedTypeReference<>(){});
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assert response.getBody() != null;
        assertThat(response.getBody().get(0).getColor()).isEqualTo("blue");
    }

    @Test
    void testSearch() {
        ResponseEntity<List<Faculty>> response = testRestTemplate.exchange(url("/faculty/search/Raven"), HttpMethod.GET, null, new ParameterizedTypeReference<>(){});
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assert response.getBody() != null;
        assertThat(response.getBody().get(0).getName().toLowerCase()).contains("raven");
    }

    @Test
    void testGetStudents() {
        ResponseEntity<List<Student>> response = testRestTemplate.exchange(url("/faculty/" + faculty.getId() + "/students"), HttpMethod.GET, null, new ParameterizedTypeReference<>(){});
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assert response.getBody() != null;
        assertThat(response.getBody().get(0).getName()).isEqualTo("Owl");
    }

}
