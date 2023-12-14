package frc.lib.core.motors;

import com.ctre.phoenix.ErrorCode;
import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.IMotorControllerEnhanced;
import com.ctre.phoenix.motorcontrol.can.BaseTalonConfiguration;

import edu.wpi.first.wpilibj.motorcontrol.MotorController;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.lib.core.PidParameters;
import frc.lib.core.util.TeamUtils;

public interface ITeamTalon extends MotorController, IMotorControllerEnhanced {
  public static boolean isPidControlMode(ControlMode mode) {
    switch (mode) {
      case Current:
        return false;
      case Disabled:
        return false;
      case Follower:
        return false;
      default:
        return true;
    }
  }

  public int getNumEStops();

  public void setNumEStops(int val);

  public double getLastTelemetryUpdate();

  public void setLastTelemetryUpdate(double val);

  public double getMaxSpeed();

  public void setMaxSpeed(double val);

  public String getSmartDashboardPrefix();

  public PidParameters[] getPidProfiles();

  public void set(double power, String reason);

  public static double telemetryUpdateInterval_secs = 0.0;

  public default boolean isRunningPidControlMode() {
    return ITeamTalon.isPidControlMode(getControlMode());
  }

  public default void noteEmergencyStop() {
    setNumEStops(getNumEStops() + 1);
  }

  public long getCurrentEncoderValue();

  public void resetEncoder();

  public default void periodic() {
    double now = TeamUtils.getCurrentTime();

    if ((now - getLastTelemetryUpdate()) < telemetryUpdateInterval_secs) {
      return;
    }

    setLastTelemetryUpdate(now);

    long currentEncoderValue = getCurrentEncoderValue();
    double currentSpeed = getSelectedSensorVelocity(0);

    if (getMaxSpeed() == Double.MAX_VALUE || currentSpeed > getMaxSpeed())
      setMaxSpeed(currentSpeed);

    if (isRunningPidControlMode()) {
      SmartDashboard.putBoolean(getSmartDashboardPrefix() + ".PID", true);
    } else {
      SmartDashboard.putBoolean(getSmartDashboardPrefix() + ".PID", false);
    }

    if (getControlMode() == ControlMode.Velocity) {
      double currentError = getVelocityError();
      SmartDashboard.putNumber(getSmartDashboardPrefix() + ".VelocityError", currentError);
    } else {
      SmartDashboard.putNumber(getSmartDashboardPrefix() + ".VelocityError", 0);
    }

    SmartDashboard.putNumber(getSmartDashboardPrefix() + ".PowerPercent", getMotorOutputPercent());

    SmartDashboard.putNumber(getSmartDashboardPrefix() + ".Position-ticks", currentEncoderValue);

    SmartDashboard.putNumber(getSmartDashboardPrefix() + ".speedPer100ms", currentSpeed);
    SmartDashboard.putNumber(getSmartDashboardPrefix() + ".speedPerSec", currentSpeed * 10);

    SmartDashboard.putNumber(getSmartDashboardPrefix() + ".maxSpeedPer100ms", getMaxSpeed());
    SmartDashboard.putNumber(getSmartDashboardPrefix() + ".maxSpeedPerSec", getMaxSpeed() * 10);

    SmartDashboard.putString(getSmartDashboardPrefix() + "Mode", getControlMode().toString());
    SmartDashboard.putNumber(getSmartDashboardPrefix() + "EmergencyStops", getNumEStops());

    SmartDashboard.putNumber(getSmartDashboardPrefix() + "Target", getClosedLoopTarget(0));
    SmartDashboard.putNumber(getSmartDashboardPrefix() + "Error", getClosedLoopError(0));

    /*switch (getControlMode()) {
      case Position:
      case Velocity:
        SmartDashboard.putNumber(getSmartDashboardPrefix() + "Target", getClosedLoopTarget(0));
        SmartDashboard.putNumber(getSmartDashboardPrefix() + "Error", getClosedLoopError(0));
        break;
      default:
        // Fill in Zeros when we're not in a mode that is using it
        SmartDashboard.putNumber(getSmartDashboardPrefix() + "Target", 0);
        SmartDashboard.putNumber(getSmartDashboardPrefix() + "Error", 0);
    }*/
  }
  

  public default double getVelocityError() {
    if (getControlMode() != ControlMode.Velocity) {
      return 0;
    }
    double currentSpeed = getSelectedSensorVelocity(0);
    return (getClosedLoopTarget(0) - currentSpeed);
  }

  public default void configureWithPidParameters(PidParameters pidParameters, int pidSlotIndex) {
    getPidProfiles()[pidSlotIndex] = pidParameters;

    config_kF(pidSlotIndex, pidParameters.kF, 30);
    config_kP(pidSlotIndex, pidParameters.kP, 30);
    config_kI(pidSlotIndex, pidParameters.kI, 30);
    config_kD(pidSlotIndex, pidParameters.kD, 30);
    configPeakOutputForward(pidParameters.kPeakOutput, 30);
    configPeakOutputReverse(-pidParameters.kPeakOutput, 30);
    configAllowableClosedloopError(pidSlotIndex, pidParameters.errorTolerance, 30);
  }

  public ErrorCode config_kP(int slotIdx, double value, int timeoutMs);

  public ErrorCode config_kP(int slotIdx, double value);

  public ErrorCode config_kI(int slotIdx, double value, int timeoutMs);

  public ErrorCode config_kI(int slotIdx, double value);

  public ErrorCode config_kD(int slotIdx, double value, int timeoutMs);

  public ErrorCode config_kD(int slotIdx, double value);

  public ErrorCode config_kF(int slotIdx, double value, int timeoutMs);

  public ErrorCode config_kF(int slotIdx, double value);

  public ErrorCode configPeakOutputForward(double percentOut, int timeoutMs);

  public ErrorCode configPeakOutputForward(double percentOut);

  public ErrorCode configPeakOutputReverse(double percentOut, int timeoutMs);

  public ErrorCode configPeakOutputReverse(double percentOut);

  // Public wrapper for protected method (which aren't allowed in interfaces)
  public ErrorCode configBaseAllSettings(BaseTalonConfiguration allConfigs);
}