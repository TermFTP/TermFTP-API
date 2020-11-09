package at.termftp.backend.dao;

import at.termftp.backend.model.Person;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


/**
 * Database things
 */
@Repository("postgres")
public class PersonDataAccessService implements PersonDao{


    @Override
    public Person insertPerson(UUID id, Person person) {
        return null; // todo
    }

    @Override
    public List<Person> selectAllPeople() {
        return null;
    }

    @Override
    public Optional<Person> selectPersonById(UUID id) {
        return Optional.empty();
    }
}
