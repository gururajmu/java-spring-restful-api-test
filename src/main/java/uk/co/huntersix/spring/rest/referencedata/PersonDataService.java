package uk.co.huntersix.spring.rest.referencedata;

import org.springframework.stereotype.Service;
import uk.co.huntersix.spring.rest.exception.InvalidArgumentException;
import uk.co.huntersix.spring.rest.exception.RecordExistException;
import uk.co.huntersix.spring.rest.exception.RecordNotFoundException;
import uk.co.huntersix.spring.rest.model.Person;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PersonDataService {
    public static List<Person> PERSON_DATA = Arrays.asList(
        new Person("Mary", "Smith"),
        new Person("Brian", "Archer"),
        new Person("Collin", "Brown")
    );

    public Person findPerson(String lastName, String firstName) {
        List<Person> list = PERSON_DATA.stream()
            .filter(p -> p.getFirstName().equalsIgnoreCase(firstName)
                && p.getLastName().equalsIgnoreCase(lastName))
            .collect(Collectors.toList());

        if(!list.isEmpty())
            return list.get(0);
        else
            throw new RecordNotFoundException("Person not Found");
    }

    public Person addPerson(String lastName, String firstName) {
        if (null == lastName || ("").equals(lastName) || null == firstName  ||  ("").equals(firstName)) {
            throw new InvalidArgumentException("Invalid Input. Please verify the request");
        }
        try {
            findPerson(lastName, firstName);
            throw new RecordExistException("Person record already exist, Please try with other input");
        } catch (RecordNotFoundException ex) {
            return addRecord(lastName, firstName);
        }
    }

    private Person addRecord(String lastName, String firstName ){
        Person person = new Person(firstName, lastName);
        List<Person> tmp = new ArrayList<>(PERSON_DATA);
        tmp.add(person);
        PERSON_DATA = new ArrayList<>(tmp);
        return person;
    }

    public List<Person> findPersonBySurname(String lastName) throws RecordNotFoundException {
        List<Person> personList = PERSON_DATA.stream()
                .filter(p -> p.getLastName().equalsIgnoreCase(lastName)).collect(Collectors.toList());
        if (personList.isEmpty()) {
            throw new RecordNotFoundException("Person not found with given last name");
        }
        return personList;
    }
}
