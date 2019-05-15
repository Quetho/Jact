import org.springframework.boot.*;
import org.springframework.boot.autoconfigure.*;
import org.springframework.web.bind.annotation.*;

@RestController
@EnableAutoConfiguration
public class Example {

	@RequestMapping("/")
	String home() {
		return "<a href=\"http://google.com\">tst</a>";
	}
	

	public static void main(String[] args) {
		SpringApplication.run(Example.class, args); 
	}
	
}