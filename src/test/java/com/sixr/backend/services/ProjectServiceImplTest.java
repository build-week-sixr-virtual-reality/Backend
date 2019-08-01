package com.sixr.backend.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.sixr.backend.BackendApplication;
import com.sixr.backend.exceptions.ResourceNotFoundException;
import com.sixr.backend.models.Project;
import com.sixr.backend.models.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = BackendApplication.class)
public class ProjectServiceImplTest {

    @Autowired
    private ProjectService projectService;

    @Autowired
    private UserService userService;

    @Autowired
    private ObjectMapper mapper;

    @BeforeEach
    public void aasetUp() {
        MockitoAnnotations.initMocks(this);
        mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
    }

    @AfterEach
    public void aatearDown() {
    }

    @Test
    public void afindAll() {
        assertEquals(1,projectService.findAll().size());
    }

    @Test
    public void btestFindAll() {
        assertEquals(1,projectService.findAll().size());
    }

//    @Test
//    public void csave() {
//        User user = userService.findUserById(1);
//        Project test = new Project("Another Demo", "Another Test Project", 50000, user,"brendlorend@gmail.com","5555555555","Starting");
//
//        assertEquals(1,projectService.findAll().size());
//        Project returned = projectService.save(test);
//        assertNotNull(returned);
//        assertEquals(2,projectService.findAll().size());
//
//        Project found = projectService.findProjectById(returned.getProjectid());
//        assertEquals(found.getName(),returned.getName());
//    }

    @Test
    public void dfindProjectById() {
        assertEquals("Demo Project", projectService.findProjectById(1).getName());
    }

    @Test(expected = ResourceNotFoundException.class)
    public void eDeleteFail(){
        projectService.delete(1000);
        assertEquals(1,projectService.findAll().size());
    }

//    @Test
//    public void eDeleteFound(){
//        projectService.delete(1);
//        assertEquals(1,projectService.findAll().size());
//    }
}