package at.termftp.backend;

import at.termftp.backend.model.User;
import at.termftp.backend.service.UserService;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.introspect.JacksonAnnotationIntrospector;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

@SpringBootTest
@AutoConfigureMockMvc
class BackendApplicationTests {

	@Autowired
	private MockMvc mockMvc;

	private final ObjectMapper mapper = new ObjectMapper();

	@Test
	void contextLoads() {
	}

	@Test
	public void testRegister() throws Exception{
		String[] userNames = {
				"u", "u1", "username1", "111", "---", "äöü", "´ß098655431123456789"
		};
		String[] passwords = {
				"u", "u1", "username1", "111", "---", "äöü", "´ß098655431123456789"
		};

		for (int i = 0; i < userNames.length; i++) {
			User user = new User();
			user.setUserID(UUID.randomUUID());
			user.setUsername(userNames[i]);
			user.setPassword(passwords[i]);
			user.setEmail("user"+i+"@gmail.com");
			register(user);
		}


		User u1 = new User();
		u1.setPassword("a_pw");
		u1.setEmail("u1@gmail.com");
		u1.setUsername("u1");
		u1.setUserID(UUID.randomUUID());

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

}
