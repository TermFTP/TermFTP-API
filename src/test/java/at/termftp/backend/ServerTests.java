package at.termftp.backend;


import at.termftp.backend.model.User;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.nio.charset.StandardCharsets;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@AutoConfigureMockMvc
public class ServerTests{

    @Autowired
    private MockMvc mockMvc;

    private User user;

    private static final String[] USER = {
            "user_007",
            "qzfoxcmxgyflrozdyilppooyzrghixpc"
    };

    public ServerTests() {
        this.user = new User(
                "user_007",
                "user_007@gmail.com",
                "a_pw"
        );
    }

    @Test
    @Order(1)
    public void testCreateServer() throws Exception {
        String body = "{\n" +
                "    \"ip\" : \"127.0.0.6\",\n" +
                "    \"ftpPort\" : \"21\",\n" +
                "    \"sshPort\" : \"22\",\n" +
                "    \"name\" : \"local2\",\n" +
                "    \"username\" : \"admin\",\n" +
                "    \"password\" : \"admin\",\n" +
                "    \"ftpType\" : \"FTP\"\n" +
                "}";
        System.out.println(mockMvc);
        mockMvc.perform(MockMvcRequestBuilders
                .post("/api/v1/createServer")
                .content(body)
                .header("Access-Token", USER[1])
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .characterEncoding(StandardCharsets.UTF_8.name()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.serverID").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.ip").value("127.0.0.6"));
    }

    @Test
    @Order(2)
    public void testCreateInvalidServer() throws Exception {
        String body = "{\n" +
                "    \"ip\" : \"127.0.0.6\",\n" +
                "    \"ftpPort\" : \"21\",\n" +
                "    \"sshPort\" : \"22\",\n" +
                "    \"name\" : \"local2\",\n" +
                "    \"username\" : \"admin\",\n" +
                "    \"password\" : \"admin\",\n" +
                "    \"ftpType\" : \"a   very very very custom ftptype that may not exist\"\n" + // <-- invalid FTP-Type
                "}";

        System.out.println(mockMvc);
        mockMvc.perform(MockMvcRequestBuilders
                .post("/api/v1/createServer")
                .content(body)
                .header("Access-Token", USER[1])
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .characterEncoding(StandardCharsets.UTF_8.name()))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(400))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Bad Request"));
    }
}
