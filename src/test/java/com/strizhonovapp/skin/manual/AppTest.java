package com.strizhonovapp.skin.manual;

import com.strizhonovapp.skin.service.AppRunner;
import com.strizhonovapp.skin.testutil.SpringManualTests;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@SpringManualTests(classes = IntegrationConfiguration.class)
class AppTest {

    @Test
    void run(@Autowired AppRunner appRunner) {
        appRunner.run();
    }

}