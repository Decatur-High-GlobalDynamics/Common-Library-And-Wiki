package frc.onboarding;
import java.lang.Integer;
// Objective: By the end of this lesson, students will be able to:

// Define and create classes in Java.
// Understand the role of constructors in initializing objects.
// Create and use methods within Java classes.
// Write unit tests to validate class functionality.


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
