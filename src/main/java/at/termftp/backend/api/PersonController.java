package at.termftp.backend.api;

import at.termftp.backend.model.Person;
import at.termftp.backend.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RequestMapping("api/v1/person")
@RestController
public class PersonController {
    private final PersonService personService;

    @Autowired
    public PersonController(PersonService personService) {
        this.personService = personService;
    }

    @PostMapping(path = "")
    public String addPerson(@RequestBody Person person){
        System.out.println("adding person");
        personService.addPerson(person);
        return "Success";
    }


    @GetMapping(path = "/test")
    public String someTest(){
        System.out.println("working");
        return "wurking";
    }

    @GetMapping(path = "/getPeople")
    public List<Person> getAllPeople(){
        return personService.getAllPeople();
    }

    @GetMapping(path = "/getPerson/{id}")
    public Person getPersonByID(@PathVariable("id") UUID id){
        return personService.getPersonById(id).orElse(null);
    }
}
