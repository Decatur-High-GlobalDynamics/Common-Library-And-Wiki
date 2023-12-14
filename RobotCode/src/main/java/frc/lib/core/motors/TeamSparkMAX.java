package frc.lib.core.motors;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.lib.core.PidParameters;
import frc.lib.core.util.TeamUtils;

import com.revrobotics.CANSparkMax;

import com.revrobotics.RelativeEncoder;

import com.revrobotics.REVLibError;
import com.revrobotics.SparkMaxPIDController;

public class TeamSparkMAX extends CANSparkMax {
  public static double telemetryUpdateInterval_secs = 0.0;
  private double lastTelemetryUpdate = 0;

  protected final String smartDashboardPrefix;

  protected int numEStops = 0;

  protected double maxSpeed = Double.MAX_VALUE;

  protected double smartMotionLoopTarget;

  public SparkMaxPIDController canPidController;

  public RelativeEncoder canEncoder;

  protected PidParameters pidProfiles[] = new PidParameters[4];

  private CANSparkMax.ControlType ctrlType = null;

  public TeamSparkMAX(String smartDashboardPrefix, int deviceID) {
    super(deviceID, MotorType.kBrushless); // Neos are brushless
    this.smartDashboardPrefix = smartDashboardPrefix;
    canPidController = getPIDController();
    canEncoder = getEncoder();
  }

  private static boolean isPidControlMode(CANSparkMax.ControlType mode) {

    // kDutyCycle, kVelocity, kVoltage, kPosition, kSmartMotion, kCurrent, kSmartVelocity

    // Are all possible values. If one of these are not part of PID, add case for them and return
    // false.
    if (mode == null) {
      return true;
    }
    switch (mode) {
      case kCurrent:
        return false;
      default:
        return true;
    }
  }

  public void noteEmergencyStop() {
    numEStops++;
  }

  public double getCurrentEncoderValue() {
    // This should be configurable
    return canEncoder.getPosition();
  }

  public void resetEncoder() {
    canEncoder.setPosition(0.0);
  }

  public boolean
      isRunningPidControlMode() { // Dunno if this is safe, but its the easiest way to get around
    // problems with the PidParameters.
    return isPidControlMode(ctrlType);
  }

  public void periodic() {
    double now = TeamUtils.getCurrentTime();

    if ((now - lastTelemetryUpdate) < telemetryUpdateInterval_secs) {
      return;
    }

    lastTelemetryUpdate = now;

    double currentEncoderValue = getCurrentEncoderValue();
    double currentSpeed = canEncoder.getVelocity();

    SmartDashboard.putNumber(smartDashboardPrefix + ".power", super.get());

    if (maxSpeed == Double.MAX_VALUE || currentSpeed > maxSpeed) maxSpeed = currentSpeed;

    // SmartDashboard.putNumber(smartDashboardPrefix + ".PowerPercent", getMotorOutputPercent());

    SmartDashboard.putNumber(smartDashboardPrefix + ".Position-ticks", currentEncoderValue);

    SmartDashboard.putNumber(smartDashboardPrefix + ".speedPer100ms", currentSpeed);
    SmartDashboard.putNumber(smartDashboardPrefix + ".speedPerSec", currentSpeed * 10);

    SmartDashboard.putNumber(smartDashboardPrefix + ".maxSpeedPer100ms", maxSpeed);
    SmartDashboard.putNumber(smartDashboardPrefix + ".maxSpeedPerSec", maxSpeed * 10);

    // SmartDashboard.putString(smartDashboardPrefix + "Mode", getControlMode().toString());
    SmartDashboard.putNumber(smartDashboardPrefix + "EmergencyStops", numEStops);

    SmartDashboard.putNumber(smartDashboardPrefix + ".error", getVelocityError());
    /*
    switch (getControlMode()) {
      case Position:
      case Velocity:
        SmartDashboard.putNumber(
            smartDashboardPrefix + "Target",
            getClosedLoopTarget(0)); // 0 is the primary closed-loop
        SmartDashboard.putNumber(smartDashboardPrefix + "Error", getClosedLoopError(0));
        break;
      default:
        // Fill in Zeros when we're not in a mode that is using it
        SmartDashboard.putNumber(smartDashboardPrefix + "Target", 0);
        SmartDashboard.putNumber(smartDashboardPrefix + "Error", 0);
    }*/

    SmartDashboard.putNumber(smartDashboardPrefix + ".setpoint", getClosedLoopTarget());
  }

  public double getClosedLoopTarget() {
    return this.smartMotionLoopTarget;
  }

  public double setClosedLoopTarget(double value) {
    this.smartMotionLoopTarget = value;
    return this.smartMotionLoopTarget;
  }

  public REVLibError setSmartMotionVelocity(double speed, String reason) {
    setClosedLoopTarget(speed);
    ctrlType = CANSparkMax.ControlType.kSmartVelocity;
    REVLibError errors = this.canPidController.setReference(Math.abs(speed), CANSparkMax.ControlType.kSmartVelocity);
    //Logs.severe("Set smart motion velocity to: " + speed + " on motor: " + smartDashboardPrefix + " for reason: " + reason);
    return errors;
  }

  public double getVelocityError() {
    /* if (getControlMode() != CANSparkMax.ControlType.kSmartVelocity) {
      return 0;
    } */
    double currentSpeed = canEncoder.getVelocity();
    return getClosedLoopTarget() - currentSpeed;
  }

  public void configureWithPidParameters(PidParameters pidParameters, int pidSlotIndex) {
    pidProfiles[pidSlotIndex] = pidParameters;

    canPidController.setFF(pidParameters.kF, pidSlotIndex); // Feed-forward
    canPidController.setP(pidParameters.kP, pidSlotIndex);
    canPidController.setI(pidParameters.kI, pidSlotIndex);
    canPidController.setD(pidParameters.kD, pidSlotIndex);
    canPidController.setOutputRange(-pidParameters.kPeakOutput, pidParameters.kPeakOutput);

    canPidController.setSmartMotionMaxVelocity(pidParameters.maxVel, pidSlotIndex);
    canPidController.setSmartMotionMinOutputVelocity(0, pidSlotIndex);
    canPidController.setSmartMotionMaxAccel(pidParameters.maxAcc, pidSlotIndex);
    canPidController.setSmartMotionAllowedClosedLoopError(
        pidParameters.errorTolerance, pidSlotIndex);
  }

  public void set(double power, String reason) {
    super.set(power);
    //Logs.info("Set power to: " + power + " on motor: " + smartDashboardPrefix + " for reason: " + reason);
  }
}