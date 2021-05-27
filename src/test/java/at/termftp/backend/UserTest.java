package at.termftp.backend;

import at.termftp.backend.model.User;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@AutoConfigureMockMvc
public class UserTest{

    @Autowired
    private MockMvc mockMvc;


    private final ObjectMapper mapper = new ObjectMapper();

    private static final String[] USER_NAMES = {
            "u", "u1", "username1", "111", "---", "äöü", "´ß098655431123456789"
    };

    private static final List<String> accessTokens = new ArrayList<>();


    @Test
    @Order(1)
    public void testRegister() throws Exception{


        for (int i = 0; i < USER_NAMES.length; i++) {
            User user = new User();
            user.setUserID(UUID.randomUUID());
            user.setUsername(USER_NAMES[i]);
            user.setPassword(USER_NAMES[i]);
            user.setEmail("user-"+i+"@gmail.com");

            // do register
            register(user);
        }
    }


    public void register(User user) throws Exception {

        String body = "{\n" +
                "    \"username\" : \"%s\",\n" +
                "    \"password\" : \"%s\",\n" +
                "    \"email\" : \"%s\"\n" +
                "}";
        body = String.format(body, user.getUsername(), user.getPassword(), user.getEmail());
        mockMvc.perform(MockMvcRequestBuilders
                .post("/api/v1/register")
                .content(body)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .characterEncoding(StandardCharsets.UTF_8.name()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.username").value(user.getUsername()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.email").value(user.getEmail()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.verified").value(user.isVerified()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.userID").isNotEmpty());
    }

    @Test
    @Order(2)
    public void testLogin() throws Exception {
        String body = "{ \"username\" : \"%s\", \"password\" : \"%s\" }";
        loginMany(body);
    }

    public void loginMany(String body) throws Exception {

        for(String user : USER_NAMES){
            String requestBody = String.format(body, user, user);
            String at = login(requestBody);
            System.out.println(at);
            accessTokens.add(at);
        }
    }

    public String login(String requestBody) throws Exception {
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders
                .post("/api/v1/login")
                .content(requestBody)
                .header("PC-Name", "private-pc")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .characterEncoding(StandardCharsets.UTF_8.name()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(200))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("AccessToken"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.accessTokenID.token").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.validUntil").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.pcName").isNotEmpty())
                .andReturn();

        JsonNode node = mapper.readTree(result.getResponse().getContentAsString());
        return node.get("data").get("accessTokenID").get("token").asText();
    }


    @Test
    @Order(3)
    public void testDeleteUser() throws Exception {
        for(String at : accessTokens){
            mockMvc.perform(MockMvcRequestBuilders
                    .delete("/api/v1/deleteUser")
                    .header("Access-Token", at)
                    .characterEncoding(StandardCharsets.UTF_8.name()))
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Deleted User"))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.data").value(1));
        }
    }
}
