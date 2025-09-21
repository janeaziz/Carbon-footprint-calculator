package group10.backendco2;

import org.junit.jupiter.api.Test;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.SpringBootTest;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;




@SpringBootTest
class Backendco2ApplicationTest {

    @Test
    void contextLoads() {
        // This test will fail if the application context cannot start
    }

    @Test
    void mainMethodRunsWithoutException() {
        assertDoesNotThrow(() -> Backendco2Application.main(new String[]{}));
    }
}