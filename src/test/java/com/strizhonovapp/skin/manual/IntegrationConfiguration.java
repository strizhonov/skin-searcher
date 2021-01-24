package com.strizhonovapp.skin.manual;

import com.strizhonovapp.skin.service.EntityRetriever;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
class IntegrationConfiguration {

    @Bean("entityRetrieverImpl")
    EntityRetriever testEntityRetriever() {
        return new TestEntityRetriever();
    }

}