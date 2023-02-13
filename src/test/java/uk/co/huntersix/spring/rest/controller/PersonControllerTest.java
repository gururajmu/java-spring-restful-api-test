package uk.co.huntersix.spring.rest.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import uk.co.huntersix.spring.rest.exception.RecordExistException;
import uk.co.huntersix.spring.rest.model.Person;
import uk.co.huntersix.spring.rest.referencedata.PersonDataService;
import uk.co.huntersix.spring.rest.exception.RecordNotFoundException;

import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@WebMvcTest(PersonController.class)
public class PersonControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PersonDataService personDataService;

    @Test
    public void testShouldReturnPersonFromService() throws Exception {
        when(personDataService.findPerson(any(), any())).thenReturn(new Person("Mary", "Smith"));
        this.mockMvc.perform(get("/person/smith/Mary"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("id").exists())
                .andExpect(jsonPath("firstName").value("Mary"))
                .andExpect(jsonPath("lastName").value("Smith"));
    }


    @Test
    public void testFindPersonReturnsRecordNotFoundExp() throws Exception {
        when(personDataService.findPerson(any(), any())).thenThrow(new RecordNotFoundException("Record Not Found"));
        this.mockMvc.perform(get("/person/test1/test2"))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    public void testFindPersonBySurname() throws Exception {
        List<Person> personList = new ArrayList<>();
        personList.add(new Person("elon", "mask"));
        when(personDataService.findPersonBySurname(any())).thenReturn(personList);
        this.mockMvc.perform(get("/person/mask"))
                .andDo(print())
                .andExpect(status().isOk()).andExpect(jsonPath("$[0].lastName").value("mask"));
    }

    @Test
    public void testFindAllPersonBySurname() throws Exception {
        List<Person> personList = new ArrayList<>();
        personList.add(new Person("Bill", "Gates"));
        personList.add(new Person("Rory", "Gates"));
        when(personDataService.findPersonBySurname(any())).thenReturn(personList);
        this.mockMvc.perform(get("/person/Gates"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].firstName").value("Bill"))
                .andExpect(jsonPath("$[0].lastName").value("Gates"))
                .andExpect(jsonPath("$[1].firstName").value("Rory"))
                .andExpect(jsonPath("$[1].lastName").value("Gates"));
    }

    @Test
    public void testFindPersonBySurnameReturnsRecordNotFound() throws Exception {
        when(personDataService.findPersonBySurname(any())).thenThrow(new RecordNotFoundException(("Record Not Found")));
        this.mockMvc.perform(get("/person/Guru"))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$").value("Record Not Found"));
    }

    @Test
    public void testAddPersonReturnsPerson() throws Exception {
        Person person = new Person("Gururaj", "Ujjinkopp");
        when(personDataService.addPerson(any(), any())).thenReturn(person);
        this.mockMvc.perform(post("/person/Ujjinkopp/Gururaj"))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("id").exists())
                .andExpect(jsonPath("$.firstName").value("Gururaj"))
                .andExpect(jsonPath("$.lastName").value("Ujjinkopp"));

    }

    @Test
    public void testAddPersonReturnsRecordExistException() throws Exception {
        when(personDataService.addPerson(any(), any())).thenThrow(new RecordExistException("Record Exist"));
        this.mockMvc.perform(post("/person/Ujjinkopp/Gururaj"))
                .andDo(print())
                .andExpect(status().isConflict());
    }
}