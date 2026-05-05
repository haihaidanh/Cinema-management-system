package com.example.qlrp;

import com.example.qlrp.entity.Customer;
import com.example.qlrp.repository.CustomerRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Optional;

@SpringBootApplication
public class QlrpApplication {

	public static void main(String[] args) {
		SpringApplication.run(QlrpApplication.class, args);
	}

	@Bean
	public CommandLineRunner initData(CustomerRepository customerRepository) {
		return args -> {
			Optional<Customer> adminOpt = customerRepository.findByEmail("admin@cinema.com");
			if (adminOpt.isEmpty()) {
				Customer admin = new Customer();
				admin.setName("Administrator");
				admin.setEmail("admin@cinema.com");
				admin.setPassword("admin"); // Plain text, cần mã hóa nếu có Security
				customerRepository.save(admin);
				System.out.println("Tạo tài khoản admin thành công: admin@cinema.com / admin");
			}
		};
	}
}
