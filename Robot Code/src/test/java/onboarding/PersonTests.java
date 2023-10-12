package onboarding;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import frc.robot.RobotContainer;

public class PersonTests {

  @BeforeEach // this method will run before each test
  void setup() {
    
  }
  

  @AfterEach // this method will run after each test
  void shutdown() throws Exception {
  
  }

  @Test
  void testInstantiatedObjectIsNotNull() {
    Person person = null;
    assertNotNull(person);
  }
  
  @Test
  void testInstantiatedObjectHasAttributes() {
    Person person = null;
    assertNotNull(person.heightInInches);
    assertNotNull(person.weightInPounds);
  }

  @Test
  void testComputeBMI() {
    // bmi = weight / height^2
    Person person = null;
    assertEquals(0, person.calculateBMI());
    person = null;
    assertEquals(10, person.calculateBMI());
    person  = null;
    assertEquals(40, person.calculateBMI());
  }

  @Test
  void testComputeBMIThrowsInvalidValueException() {
    Person person  = null;
    IllegalArgumentException thrown = assertThrows(
           IllegalArgumentException.class,
           () -> person.calculateBMI()
    );

    assertTrue(thrown.getMessage().contains("Weight cannot be negative."));
  }
}
