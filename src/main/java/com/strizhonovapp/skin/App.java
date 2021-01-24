package com.strizhonovapp.skin;

import com.strizhonovapp.skin.service.AppRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.PropertySource;

@EnableCaching
@SpringBootApplication
@PropertySource("classpath:credentials.properties")
public class App {

    public static void main(String... args) {
        SpringApplication.run(App.class, args)
                .getBean(AppRunner.class)
                .run();
    }

}
