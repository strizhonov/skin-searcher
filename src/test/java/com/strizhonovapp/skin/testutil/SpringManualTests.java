package com.strizhonovapp.skin.testutil;

import com.strizhonovapp.skin.App;
import org.junit.jupiter.api.Tag;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@ActiveProfiles("test")
@Tag("manual")
@SpringBootTest
@Import(SpringManualTests.LazyConfiguration.class)
public @interface SpringManualTests {

    Class<?>[] classes() default {};

    @Configuration
    @ComponentScan(lazyInit = true, basePackageClasses = App.class)
    class LazyConfiguration {

    }

}
