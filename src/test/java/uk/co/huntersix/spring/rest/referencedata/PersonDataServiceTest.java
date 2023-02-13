package uk.co.huntersix.spring.rest.referencedata;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import uk.co.huntersix.spring.rest.exception.InvalidArgumentException;
import uk.co.huntersix.spring.rest.exception.RecordExistException;
import uk.co.huntersix.spring.rest.exception.RecordNotFoundException;
import uk.co.huntersix.spring.rest.model.Person;

import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class PersonDataServiceTest {
    private PersonDataService service = new PersonDataService();

    @Test
    public void testFindPersonPositive() {
        Person person = service.findPerson("Smith", "Mary");
        assertEquals("Mary", person.getFirstName());
        assertEquals("Smith", person.getLastName());
    }

    @Test
    public void testFindPersonCaseSensitive() {
        Person person = service.findPerson("SMIth", "MarY");
        assertEquals("Mary", person.getFirstName());
        assertEquals("Smith", person.getLastName());
    }

    @Test(expected = RecordNotFoundException.class)
    public void testFindPersonRecordNotFoundExp() {
        service.findPerson("Gates", "Bill");
    }

    @Test(expected = RecordNotFoundException.class)
    public void testFindPersonBySurname() {
        service.findPersonBySurname("Gates");
    }

    @Test
    public void testFindPersonBySurnamePositive() {
        service.addPerson("Smith", "test");
        List<Person> list = service.findPersonBySurname("Smith");
        assertFalse(list.isEmpty());
        assertEquals(list.size(), 2);
    }

    @Test
    public void testAddPersonPositive() {

        Person person = service.addPerson("Ujjinkopp", "Gururaj");
        assertEquals(person.getFirstName(), "Gururaj");
        assertEquals(person.getLastName(), "Ujjinkopp");
    }

    @Test(expected = RecordExistException.class)
    public void testAddPersonNegative() {
        service.addPerson("Smith", "Mary");
    }

    @Test(expected = InvalidArgumentException.class)
    public void testAddPersonWithInvalidArgument() {
        service.addPerson("", "");
    }

    @Test(expected = InvalidArgumentException.class)
    public void testAddPersonWithInvalidArgumentNull() {
        service.addPerson(null, "");
    }
}
