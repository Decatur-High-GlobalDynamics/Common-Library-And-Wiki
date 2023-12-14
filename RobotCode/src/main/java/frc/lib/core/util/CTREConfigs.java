package frc.lib.core.util;

import com.ctre.phoenix.motorcontrol.SupplyCurrentLimitConfiguration;
import com.ctre.phoenix.motorcontrol.can.TalonFXConfiguration;
import com.ctre.phoenix.sensors.AbsoluteSensorRange;
import com.ctre.phoenix.sensors.CANCoderConfiguration;
import com.ctre.phoenix.sensors.SensorInitializationStrategy;
import com.ctre.phoenix.sensors.SensorTimeBase;

import frc.lib.modules.swervedrive.SwerveConstants;

public final class CTREConfigs
{
	public TalonFXConfiguration swerveAngleFXConfig;
	public TalonFXConfiguration swerveDriveFXConfig;
	public CANCoderConfiguration swerveCanCoderConfig;

	public CTREConfigs()
	{
		swerveAngleFXConfig = new TalonFXConfiguration();
		swerveDriveFXConfig = new TalonFXConfiguration();
		swerveCanCoderConfig = new CANCoderConfiguration();

		/* Swerve Angle Motor Configurations */
		SupplyCurrentLimitConfiguration angleSupplyLimit = new SupplyCurrentLimitConfiguration(
				SwerveConstants.ANGLE_ENABLE_CURRENT_LIMIT,
				SwerveConstants.ANGLE_CONTINUOUS_CURRENT_LIMIT,
				SwerveConstants.ANGLE_PEAK_CURRENT_LIMIT,
				SwerveConstants.ANGLE_PEAK_CURRENT_DURATION);

		swerveAngleFXConfig.slot0.kP = SwerveConstants.ANGLE_KP;
		swerveAngleFXConfig.slot0.kI = SwerveConstants.ANGLE_KI;
		swerveAngleFXConfig.slot0.kD = SwerveConstants.ANGLE_KD;
		swerveAngleFXConfig.slot0.kF = SwerveConstants.ANGLE_KF;
		swerveAngleFXConfig.supplyCurrLimit = angleSupplyLimit;

		/* Swerve Drive Motor Configuration */
		SupplyCurrentLimitConfiguration driveSupplyLimit = new SupplyCurrentLimitConfiguration(
				SwerveConstants.DRIVE_ENABLE_CURRENT_LIMIT,
				SwerveConstants.DRIVE_CONTINUOUS_CURRENT_LIMIT,
				SwerveConstants.DRIVE_PEAK_CURRENT_LIMIT,
				SwerveConstants.DRIVE_PEAK_CURRENT_DURATION);

		swerveDriveFXConfig.slot0.kP = SwerveConstants.DRIVE_KP;
		swerveDriveFXConfig.slot0.kI = SwerveConstants.DRIVE_KI;
		swerveDriveFXConfig.slot0.kD = SwerveConstants.DRIVE_KD;
		swerveDriveFXConfig.slot0.kF = SwerveConstants.DRIVE_KF;
		swerveDriveFXConfig.supplyCurrLimit = driveSupplyLimit;
		swerveDriveFXConfig.openloopRamp = SwerveConstants.OPEN_LOOP_RAMP;
		swerveDriveFXConfig.closedloopRamp = SwerveConstants.CLOSED_LOOP_RAMP;

		/* Swerve CANCoder Configuration */
		swerveCanCoderConfig.absoluteSensorRange = AbsoluteSensorRange.Unsigned_0_to_360;
		swerveCanCoderConfig.sensorDirection = SwerveConstants.CANCODER_INVERT;
		swerveCanCoderConfig.initializationStrategy = SensorInitializationStrategy.BootToAbsolutePosition;
		swerveCanCoderConfig.sensorTimeBase = SensorTimeBase.PerSecond;
	}
}