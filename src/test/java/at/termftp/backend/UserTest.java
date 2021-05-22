package at.termftp.backend;

import at.termftp.backend.model.User;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UserTest extends BackendApplicationTests{



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


    private void register(User user) throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                .post("/api/v1/register")
                .content(mapper.writeValueAsString(user))
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

        for(String user : USER_NAMES){
            String requestBody = String.format(body, user, user);

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
            String at = node.get("data").get("accessTokenID").get("token").asText();
            System.out.println(at);
            accessTokens.add(at);
        }
    }

    @Test
    @Order(3)
    void testDeleteUser() throws Exception {
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
