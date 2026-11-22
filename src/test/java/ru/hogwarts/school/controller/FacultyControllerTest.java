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
import ru.hogwarts.school.service.FacultyService;

import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(FacultyController.class)
class FacultyControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private FacultyService facultyService;

    private Faculty faculty;
    private Student student;

    @BeforeEach
    void setUp() {
        faculty = new Faculty(1L, "Ravenclaw", "blue");

        student = new Student(1L, "Owl", 10);
        student.setFaculty(faculty);
    }

    @Test
    void testGetAll() throws Exception {
        when(facultyService.getAll()).thenReturn(List.of(faculty));

        mockMvc.perform(get("/faculty"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("Ravenclaw"))
                .andExpect(jsonPath("$[0].color").value("blue"));

        verify(facultyService).getAll();
    }

    @Test
    void testGetById() throws Exception {
        when(facultyService.getById(1L)).thenReturn(faculty);

        mockMvc.perform(get("/faculty/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Ravenclaw"))
                .andExpect(jsonPath("$.color").value("blue"));

        verify(facultyService).getById(1L);
    }

    @Test
    void testGetByIdNotFound() throws Exception {
        when(facultyService.getById(99L))
                .thenThrow(new NotFoundException("Faculty with 99 id not found"));

        mockMvc.perform(get("/faculty/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testCreate() throws Exception {
        Faculty toCreate = new Faculty(null, "Slytherin", "green");
        Faculty created = new Faculty(2L, "Slytherin", "green");

        when(facultyService.create(any(Faculty.class))).thenReturn(created);

        mockMvc.perform(post("/faculty")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(toCreate)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(2))
                .andExpect(jsonPath("$.name").value("Slytherin"))
                .andExpect(jsonPath("$.color").value("green"));

        verify(facultyService).create(any(Faculty.class));
    }

    @Test
    void testUpdate() throws Exception {
        Faculty updated = new Faculty(1L, "Slytherin Updated", "DarkerGreen");

        when(facultyService.update(eq(1L), any(Faculty.class))).thenReturn(updated);

        mockMvc.perform(put("/faculty/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updated)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Slytherin Updated"))
                .andExpect(jsonPath("$.color").value("DarkerGreen"));

        verify(facultyService).update(eq(1L), any(Faculty.class));
    }

    @Test
    void testDelete() throws Exception {
        when(facultyService.delete(1L)).thenReturn(faculty);

        mockMvc.perform(delete("/faculty/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Ravenclaw"));

        verify(facultyService).delete(1L);
    }

    @Test
    void testGetByColor() throws Exception {
        when(facultyService.getByColor("blue")).thenReturn(List.of(faculty));

        mockMvc.perform(get("/faculty/color/blue"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].color").value("blue"));

        verify(facultyService).getByColor("blue");
    }

    @Test
    void testSearch() throws Exception {
        when(facultyService.getByNameOrColorIgnoreCaseBoth("Raven"))
                .thenReturn(List.of(faculty));

        mockMvc.perform(get("/faculty/search/Raven"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Ravenclaw"));

        verify(facultyService).getByNameOrColorIgnoreCaseBoth("Raven");
    }

    @Test
    void testGetStudents() throws Exception {
        when(facultyService.getStudents(1L)).thenReturn(List.of(student));

        mockMvc.perform(get("/faculty/1/students"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("Owl"))
                .andExpect(jsonPath("$[0].age").value(10));

        verify(facultyService).getStudents(1L);
    }
}