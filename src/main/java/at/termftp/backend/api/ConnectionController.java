package at.termftp.backend.api;

import at.termftp.backend.model.Person;
import at.termftp.backend.service.ConnectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("api/v1/connection")
public class ConnectionController {

    private final ConnectionService connectionService;

    @Autowired
    public ConnectionController(ConnectionService connectionService) {
        this.connectionService = connectionService;
    }

    @PostMapping(path = "/register")
    public Object register(@RequestBody Person person){
        return connectionService.register(person);
    }




    public String logIn(UUID id){
        return null;
    }



}
