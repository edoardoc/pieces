package me.edoardo.pieces;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application {


	public static void main(String[] args) throws IOException, NoSuchAlgorithmException {
		// args[0].toString()
		MerkleSingleton.init(args[0].toString());
		MerkleSingleton singleton = MerkleSingleton.getInstance();
		SpringApplication.run(Application.class, args);
	}

}