package ru.hogwarts.school.controller;

import tools.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.hogwarts.school.exception.NotFoundException;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.service.StudentService;

import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(StudentController.class)
class StudentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private StudentService studentService;

    private Student student;
    private Faculty faculty;

    @BeforeEach
    void setUp() {
        faculty = new Faculty(1L, "Hufflepuff", "purple");

        student = new Student(1L, "Pups", 16);
        student.setFaculty(faculty);
    }

    @Test
    void testGetAll() throws Exception {
        when(studentService.getAll()).thenReturn(List.of(student));

        mockMvc.perform(get("/student"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("Pups"))
                .andExpect(jsonPath("$[0].age").value(16));

        verify(studentService, times(1)).getAll();
    }

    @Test
    void testGetById() throws Exception {
        when(studentService.getById(1L)).thenReturn(student);

        mockMvc.perform(get("/student/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Pups"))
                .andExpect(jsonPath("$.age").value(16));

        verify(studentService).getById(1L);
    }

    @Test
    void testGetByIdNotFound() throws Exception {
        when(studentService.getById(99L))
                .thenThrow(new NotFoundException("Student with 99 id not found"));

        mockMvc.perform(get("/student/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testCreate() throws Exception {
        Student toCreate = new Student(null, "Ron", 16);
        Student created = new Student(2L, "Ron", 16);

        when(studentService.create(any(Student.class))).thenReturn(created);

        mockMvc.perform(post("/student")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(toCreate)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(2))
                .andExpect(jsonPath("$.name").value("Ron"));

        verify(studentService).create(any(Student.class));
    }

    @Test
    void testUpdate() throws Exception {
        Student updated = new Student(1L, "Ron Updated", 16);

        when(studentService.update(eq(1L), any(Student.class))).thenReturn(updated);

        mockMvc.perform(put("/student/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updated)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Ron Updated"));

        verify(studentService).update(eq(1L), any(Student.class));
    }

    @Test
    void testDelete() throws Exception {
        when(studentService.delete(1L)).thenReturn(student);

        mockMvc.perform(delete("/student/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Pups"));

        verify(studentService).delete(1L);
    }

    @Test
    void testGetByAge() throws Exception {
        when(studentService.getByAge(16)).thenReturn(List.of(student));

        mockMvc.perform(get("/student/age").param("age", "16"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].age").value(16));

        verify(studentService).getByAge(16);
    }

    @Test
    void testGetByAgeBetween() throws Exception {
        when(studentService.getByAgeBetween(10, 20)).thenReturn(List.of(student));

        mockMvc.perform(get("/student/age/between")
                        .param("min", "10")
                        .param("max", "20"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Pups"))
                .andExpect(jsonPath("$[0].age").value(16));

        verify(studentService).getByAgeBetween(10, 20);
    }

    @Test
    void testGetFaculty() throws Exception {
        when(studentService.getFaculty(1L)).thenReturn(faculty);

        mockMvc.perform(get("/student/1/faculty"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Hufflepuff"))
                .andExpect(jsonPath("$.color").value("purple"));

        verify(studentService).getFaculty(1L);
    }
}