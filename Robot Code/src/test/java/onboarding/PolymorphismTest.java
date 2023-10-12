package onboarding;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import frc.onboarding.Coder;
import frc.onboarding.Person;
import frc.robot.RobotContainer;

public class PolymorphismTest {
    
    @BeforeEach // this method will run before each test
    void setup() {
        
    }
    

    @AfterEach // this method will run after each test
    void shutdown() throws Exception {
    
    }
    @Test
    void testCoderIsPerson() {
        Person coder = new Coder(0,0);
        assertNotNull(coder, "Person can be instantiated as a coder.");
    }

    @Test
    void testCoderIsPerson() {
        Person coder = new Coder(0,0);
        assertNotNull(coder, "Person can be instantiated as a coder.");
    }
}