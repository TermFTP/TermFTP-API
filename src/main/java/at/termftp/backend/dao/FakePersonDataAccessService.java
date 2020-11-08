package at.termftp.backend.dao;

import at.termftp.backend.model.Person;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


@Repository("fakeDao")
public class FakePersonDataAccessService implements PersonDao{

    private static List<Person> personList = new ArrayList<>();

    @Override
    public int insertPerson(UUID id, Person person) {
        System.out.println("inserting person");
        personList.add(new Person(id, person.getFirstName(), person.getLastName()));
        return 1;
    }

    @Override
    public List<Person> selectAllPeople() {
        return personList;
    }
}
