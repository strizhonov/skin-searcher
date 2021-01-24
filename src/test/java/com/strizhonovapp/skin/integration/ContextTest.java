package com.strizhonovapp.skin.integration;

import com.strizhonovapp.skin.testutil.SpringIntegrationTests;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringIntegrationTests
public class ContextTest {

    @Test
    void shouldLoadContext(@Autowired ApplicationContext context){
        assertNotNull(context);
    }

}
