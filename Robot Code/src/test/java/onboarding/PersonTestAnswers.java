package onboarding;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import frc.robot.RobotContainer;

public class PersonTestAnswers {

  public class Person {

    public double heightInInches;
    public double weightInPounds;
    
    public Person(double heightInInches, double weightInPounds) {
        this.heightInInches = heightInInches;
        this.weightInPounds = weightInPounds;
      }

    public double calculateBMI(){
        if(weightInPounds < 0) {
            throw new IllegalArgumentException("Weight cannot be negative.");
        }
        return weightInPounds / (heightInInches*heightInInches);
    }
    
}


  @BeforeEach // this method will run before each test
  void setup() {
    
  }
  

  @AfterEach // this method will run after each test
  void shutdown() throws Exception {
  
  }

  @Test
  void testInstantiatedObjectIsNotNull() {
    Person person = new Person(0, 0);
    assertNotNull(person);
  }
  
  @Test
  void testInstantiatedObjectHasAttributes() {
    Person person = new Person(0, 0);
    assertNotNull(person.heightInInches);
    assertNotNull(person.weightInPounds);
  }

  @Test
  void testComputeBMI() {
    // bmi = weight / height^2
    Person person = new Person(1000, 0);
    assertEquals(0, person.calculateBMI());
    person = new Person(10, 1000);
    assertEquals(10, person.calculateBMI());
    person = new Person(1, 40);
    assertEquals(40, person.calculateBMI());
  }

  @Test
  void testComputeBMIThrowsInvalidValueException() {
    Person person = new Person(-1, -10);
    IllegalArgumentException thrown = assertThrows(
           IllegalArgumentException.class,
           () -> person.calculateBMI()
    );

    assertTrue(thrown.getMessage().contains("Weight cannot be negative."));
  }
}
