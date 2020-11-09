package at.termftp.backend.service;

import at.termftp.backend.dao.PersonDao;
import at.termftp.backend.model.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class ConnectionService {
    private final PersonDao personDao;

    @Autowired
    public ConnectionService(@Qualifier("postgres") PersonDao personDao) {
        this.personDao = personDao;
    }

    public Person register(Person person){
        return personDao.insertPerson(person);
    }
}
