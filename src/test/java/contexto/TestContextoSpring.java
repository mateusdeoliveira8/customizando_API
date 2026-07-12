package contexto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import com.springone.app.SpringoneApplication;

@ActiveProfiles("dev") /* Perfil de desenvolvimento */
@AutoConfigureMockMvc(addFilters = false) /*COnfiguraão automatica do spring*/
//@TestPropertySource(locations = "classpath:application-dev.properties")
@SpringBootTest(classes = SpringoneApplication.class)
@Transactional
public class TestContextoSpring {
	
	
	@Test
	@DisplayName("Teste inicial de funcionamento")
	public void teste () {
		System.out.println("Teste funcionando....");
	}

}
