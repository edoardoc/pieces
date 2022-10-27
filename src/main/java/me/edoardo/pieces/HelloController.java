package me.edoardo.pieces;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

	@GetMapping("/")
	public String index() {
		MerkleSingleton singleton = MerkleSingleton.getInstance();
		return "Greetings from Spring Boot! ---> ";
	}

}