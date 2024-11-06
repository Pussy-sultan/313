package ru.kata.spring.boot_security.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.service.RoleServiceImp;
import ru.kata.spring.boot_security.demo.service.UserService;
import ru.kata.spring.boot_security.demo.service.UserServiceImp;

import java.util.HashSet;
import java.util.Set;

@SpringBootApplication
public class SpringBootSecurityDemoApplication {

	public static void main(String[] args) {
		ConfigurableApplicationContext context = SpringApplication.run(SpringBootSecurityDemoApplication.class, args);
		initApplicationDefault(context);
	}

	public static void initApplicationDefault(ConfigurableApplicationContext context) {
		UserService userService = context.getBean(UserServiceImp.class);
		RoleService roleService = context.getBean(RoleServiceImp.class);
		BCryptPasswordEncoder bCryptPasswordEncoder = context.getBean(BCryptPasswordEncoder.class);

		roleService.saveIfExists("ROLE_ADMIN");
		roleService.saveIfExists("ROLE_USER");

		User user = userService.getByEmail("admin@mail.ru");
		if (user == null) {
			Set<Role> roleList = roleService.findAll();
			User newUser = new User(
					"admin",
					"admin@mail.ru",
					"admin"
			);
			newUser.setRoles(roleList);
			userService.save(newUser);
		}

		User demoUser1 = userService.getByEmail("user1@mail.ru");
		if (demoUser1 == null) {
			Set<Role> roleList = new HashSet<>();
			roleList.add(roleService.findByName("ROLE_USER"));
			User newUser = new User(
					"User1",
					"user1@mail.ru",
					"user"
			);
			newUser.setRoles(roleList);
			userService.save(newUser);
		}

		User demoUser2 = userService.getByEmail("user2@mail.ru");
		if (demoUser2 == null) {
			Set<Role> roleList = new HashSet<>();
			roleList.add(roleService.findByName("ROLE_USER"));
			User newUser = new User(
					"User2",
					"user2@mail.ru",
					"user"
			);
			newUser.setRoles(roleList);
			userService.save(newUser);
		}
	}
}
