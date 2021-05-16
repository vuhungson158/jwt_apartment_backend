package com.hung91hn.apartment;

import com.twilio.Twilio;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class App {

	public static void main(String[] args) {
		SpringApplication.run(App.class, args);
		Twilio.init("AC9310581c98840f32f932f5e1e54cdb19", "27206d3c1abeed198f720fa7cc8059cc");
	}

}
