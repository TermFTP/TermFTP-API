package at.termftp.backend;

import at.termftp.backend.model.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import java.nio.charset.StandardCharsets;

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
		User u1 = new User();
		u1.setPassword("a_pw");
		u1.setEmail("u1@gmail.com");
		u1.setUsername("u1");

		System.out.println(mapper.writeValueAsString(u1)); // password is not included in json

		mockMvc.perform(MockMvcRequestBuilders
					.post("/api/v1/register")
					.content("{\n" +
							"    \"username\" : \"u_test\",\n" +
							"    \"password\" : \"a_pw\",\n" +
							"    \"email\" : \"u_test@gmail.com\"\n" +
							"}")
					.contentType(MediaType.APPLICATION_JSON)
					.accept(MediaType.APPLICATION_JSON)
					.characterEncoding(StandardCharsets.UTF_8.name()))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$.data.username").value("u_test"))
				.andExpect(MockMvcResultMatchers.jsonPath("$.data.email").value("u_test@gmail.com"))
				.andExpect(MockMvcResultMatchers.jsonPath("$.data.verified").value("false"))
				.andExpect(MockMvcResultMatchers.jsonPath("$.data.verified").isNotEmpty());

	}

}
