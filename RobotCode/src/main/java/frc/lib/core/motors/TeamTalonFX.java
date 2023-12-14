package frc.lib.core.motors;

import com.ctre.phoenix.ErrorCode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.BaseTalonConfiguration;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;

import frc.lib.core.PidParameters;

/**
 * A wrapper class for Motors that helps to consistently and easily perform the following functions:
 * -Keep current and max speeds -Get and Reset encoder values -Lots and lots of SmartDashboard
 * information
 */
public class TeamTalonFX extends WPI_TalonFX implements ITeamTalon {

  private double lastTelemetryUpdate = 0;

  protected final String smartDashboardPrefix;

  protected int numEStops = 0;

  protected double maxSpeed = Double.MAX_VALUE;

  protected PidParameters pidProfiles[] = new PidParameters[4];

  public TeamTalonFX(String smartDashboardPrefix, int deviceNumber) {
    super(deviceNumber);
    this.smartDashboardPrefix = smartDashboardPrefix;
    // assuming quadencoder
    this.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder);
  }

  public long getCurrentEncoderValue() {
    // This should be configurable
    return (long) getSensorCollection().getIntegratedSensorPosition();
  }

  public void set(double power, String reason) {
    super.set(power);
    //Logs.info("Set power to " + power + " REASON: " + reason);
  }

  public void resetEncoder() {
    getSensorCollection().setIntegratedSensorPosition(0, 0);
  }

  public double getLastTelemetryUpdate() {
    return lastTelemetryUpdate;
  }

  public void setLastTelemetryUpdate(double val) {
    lastTelemetryUpdate = val;
  }

  public String getSmartDashboardPrefix() {
    return smartDashboardPrefix;
  }

  public int getNumEStops() {
    return numEStops;
  }

  public void setNumEStops(int val) {
    numEStops = val;
  }

  public double getMaxSpeed() {
    return maxSpeed;
  }

  public void setMaxSpeed(double val) {
    maxSpeed = val;
  }

  public PidParameters[] getPidProfiles() {
    return pidProfiles;
  }

  // Public wrapper for protected method (which aren't allowed in interfaces)
  // Use this for configurations which can be shared between SRX and FX
  // Otherwise down cast and use configAllSettings(TalonFXConfiguration allConfigs)
  // if using config settings only available for TalonFX
  public ErrorCode configBaseAllSettings(BaseTalonConfiguration allConfigs) {
    return configAllSettings(allConfigs);
  }

  
}