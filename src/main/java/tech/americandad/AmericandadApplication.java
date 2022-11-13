package tech.americandad;

import java.io.File;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import tech.americandad.constants.FileConstants;

@SpringBootApplication
public class AmericandadApplication {

	public static void main(String[] args) {
		SpringApplication.run(AmericandadApplication.class, args);

		new File(FileConstants.USER_FOLDER).mkdirs();
	}

	@Bean
	public BCryptPasswordEncoder bCryptPasswordEncoder(){
		return new BCryptPasswordEncoder();
	}

}
