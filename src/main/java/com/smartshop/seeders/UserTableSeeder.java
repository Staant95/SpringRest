package com.smartshop.seeders;

import com.github.javafaker.Faker;
import com.smartshop.models.Shoplist;
import com.smartshop.models.User;
import com.smartshop.repositories.ShoplistRepository;
import com.smartshop.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;

@Component
@Transactional
public class UserTableSeeder implements Seeder {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ShoplistRepository shoplistRepository;

    private final Faker faker;

    private final Logger logger = LoggerFactory.getLogger(UserTableSeeder.class);


    public UserTableSeeder() {
        this.faker = new Faker();
    }

    @Override
    public void run() {

        for(int i = 0; i < 3; i++) {

            User u = new User(
                    faker.name().firstName(),
                    faker.name().lastName(),
                    faker.lorem().word() + "@gmail.com",
                    faker.lorem().word());

            logger.info("Email -> " + u.getEmail() + " password -> " + u.getPassword());

            u.setPassword(new BCryptPasswordEncoder().encode(u.getPassword()));
            this.userRepository.save(u);

            // Create and attach a shoplist to every user
            Shoplist s = new Shoplist("Home");

            s.getUsers().add(u);
            this.shoplistRepository.save(s);

        }

        User me = new User("Stas", "Antokhi", "stas@gmail.com", new BCryptPasswordEncoder().encode("secret"));
        this.userRepository.save(me);
        Shoplist s = new Shoplist("Natale");
        s.getUsers().add(me);
        this.shoplistRepository.save(s);


    }

}
