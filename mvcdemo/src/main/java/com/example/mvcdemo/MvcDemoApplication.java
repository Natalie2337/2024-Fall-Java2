package com.example.mvcdemo;

import com.example.mvcdemo.model.Student;
import com.example.mvcdemo.repository.StudentRepository;
import com.example.mvcdemo.service.StudentService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.List;

@SpringBootApplication
public class MvcDemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(MvcDemoApplication.class, args);
	}

	@Bean
	public CommandLineRunner commandLineRunner(StudentService service){
        return args -> {
            service.addStudents();
        };
    }

}
