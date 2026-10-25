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
class StudentControllerTest {

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
        faculty.setName("Hufflepuff");
        faculty.setColor("purple");
        faculty = facultyRepository.save(faculty);

        student = new Student();
        student.setName("Pups");
        student.setAge(16);
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
        ResponseEntity<List<Student>> response = testRestTemplate.exchange(url("/student"), HttpMethod.GET, null, new ParameterizedTypeReference<>(){});
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assert response.getBody() != null;
    }

    @Test
    void testGetById() {
        ResponseEntity<Student> response = testRestTemplate.getForEntity(url("/student/" + student.getId()), Student.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assert response.getBody() != null;
        assertThat(response.getBody().getName()).isEqualTo("Pups");
    }

    @Test
    void testCreate() {
        Student newStudent = new Student();
        newStudent.setName("Ron");
        newStudent.setAge(16);
        ResponseEntity<Student> response = testRestTemplate.postForEntity(url("/student"), newStudent, Student.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assert response.getBody() != null;
        assertThat(response.getBody().getId()).isNotNull();
        assertThat(response.getBody().getName()).isEqualTo("Ron");
    }

    @Test
    void testUpdate() {
        student.setName("Ron Updated");
        student.setAge(16);

        HttpEntity<Student> entity = new HttpEntity<>(student);
        ResponseEntity<Student> response = testRestTemplate.exchange(url("/student/" + student.getId()), HttpMethod.PUT, entity, Student.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assert response.getBody() != null;
        assertThat(response.getBody().getName()).isEqualTo("Ron Updated");
        assertThat(response.getBody().getAge()).isEqualTo(16);
    }

    @Test
    void testDelete() {
        ResponseEntity<Student> response = testRestTemplate.exchange(url("/student/" + student.getId()), HttpMethod.DELETE, null, Student.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assert response.getBody() != null;
        assertThat(studentRepository.findById(student.getId())).isEmpty();
    }

    @Test
    void testGetByAge() {
        ResponseEntity<List<Student>> response = testRestTemplate.exchange(url("/student/age?age=16"), HttpMethod.GET, null, new ParameterizedTypeReference<>(){});
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assert response.getBody() != null;
        assertThat(response.getBody().get(0).getAge()).isEqualTo(16);
    }

    @Test
    void testGetByAgeBetween() {
        ResponseEntity<List<Student>> response = testRestTemplate.exchange(url("/student/age/between?min=10&max=20"), HttpMethod.GET, null, new ParameterizedTypeReference<>(){});
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assert response.getBody() != null;
    }

    @Test
    void testGetFaculty() {
        ResponseEntity<Faculty> response = testRestTemplate.getForEntity(url("/student/" + student.getId() + "/faculty"), Faculty.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assert response.getBody() != null;
        assertThat(response.getBody().getName()).isEqualTo("Hufflepuff");
    }

}
