package uk.co.huntersix.spring.rest.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uk.co.huntersix.spring.rest.model.Person;
import uk.co.huntersix.spring.rest.exception.InvalidArgumentException;
import uk.co.huntersix.spring.rest.referencedata.PersonDataService;
import uk.co.huntersix.spring.rest.exception.RecordExistException;
import uk.co.huntersix.spring.rest.exception.RecordNotFoundException;

import java.util.List;

@RestController
public class PersonController {
    private final PersonDataService personDataService;

    public PersonController(@Autowired PersonDataService personDataService) {
        this.personDataService = personDataService;
    }

    @GetMapping("/person/{lastName}/{firstName}")
    public ResponseEntity<?> findPerson(@PathVariable(value = "lastName") String lastName,
                                        @PathVariable(value = "firstName") String firstName) {
        try {
            Person person = personDataService.findPerson(lastName, firstName);
            return new ResponseEntity<>(person, HttpStatus.OK);
        } catch (RecordNotFoundException exp) {
            return new ResponseEntity<>(exp.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/person/{lastName}")
    public ResponseEntity<?> personBySurname(@PathVariable(value = "lastName") String lastName) {
        try {
            List<Person> list = personDataService.findPersonBySurname(lastName);
            return new ResponseEntity<>(list, HttpStatus.OK);
        } catch (RecordNotFoundException exp) {
            return new ResponseEntity<>(exp.getMessage(), HttpStatus.NOT_FOUND);
        }
    }


    @PostMapping("/person/{lastName}/{firstName}")
    public ResponseEntity<?> add(@PathVariable(value = "lastName") String lastName,
                                 @PathVariable(value = "firstName") String firstName) {
        try {
            Person resp = personDataService.addPerson(lastName, firstName);
            ;
            return new ResponseEntity<>(resp, HttpStatus.CREATED);
        } catch (RecordExistException exp) {
            return new ResponseEntity<String>(exp.getMessage(), HttpStatus.CONFLICT);
        } catch (InvalidArgumentException exp) {
            return new ResponseEntity<String>(exp.getMessage(), HttpStatus.BAD_REQUEST);
        }

    }

}