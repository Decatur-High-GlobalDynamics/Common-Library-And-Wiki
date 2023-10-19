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
    public String job;
    public Boolean hasSpecialty;
    
    public Person(double heightInInches, double weightInPounds) {
        this.heightInInches = heightInInches;
        this.weightInPounds = weightInPounds;
        job = "Unknown";
        hasSpecialty = Boolean.FALSE;
      }

    public double calculateBMI(){
        if(weightInPounds < 0) {
            throw new IllegalArgumentException("Weight cannot be negative.");
        }
        return weightInPounds / (heightInInches*heightInInches);
    }

    public String getToWork() {
        if (this.job == "Unknown") {
            return "I don't know what to do yet.";
        } else if (this.job == "Robot building") {
        return "Starting to build";
        } else if (this.job == "Robot coding") {
        return "Starting to code";
        } else if (this.job == "Robot designing") {
        return "Starting to design";
        }  else {
            throw new IllegalArgumentException("No work available for job: " + this.job)
        }             
    }
    
}
