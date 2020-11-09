package at.termftp.backend.dao;

import at.termftp.backend.model.Person;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


@Repository("fakeDao")
public class FakePersonDataAccessService implements PersonDao{

    private static List<Person> personList = new ArrayList<>();

    @Override
    public Person insertPerson(UUID id, Person person) {
        System.out.println("inserting person");
        Person p = new Person(id, person.getFirstName(), person.getLastName(), person.getEmail());
        personList.add(p);
        return p;
    }

    @Override
    public List<Person> selectAllPeople() {
        return personList;
    }

    @Override
    public Optional<Person> selectPersonById(UUID id) {
        return personList.stream()
                .filter(person -> person.getId().equals(id))
                .findFirst();
    }
}
