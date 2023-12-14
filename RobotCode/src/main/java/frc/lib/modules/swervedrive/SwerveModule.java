package frc.lib.modules.swervedrive;

import edu.wpi.first.math.controller.SimpleMotorFeedforward;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.kinematics.SwerveModulePosition;
import edu.wpi.first.math.kinematics.SwerveModuleState;
import frc.lib.core.motors.TeamSparkMAX;
import frc.lib.core.util.CANCoderUtil;
import frc.lib.core.util.CANSparkMaxUtil;
import frc.lib.core.util.CTREModuleState;
import frc.lib.core.util.Conversions;
import frc.lib.core.util.CANCoderUtil.CCUsage;
import frc.lib.core.util.CANSparkMaxUtil.Usage;
import frc.robot.Robot;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.DemandType;
import com.ctre.phoenix.motorcontrol.can.TalonFX;
import com.ctre.phoenix.sensors.CANCoder;
import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.SparkMaxPIDController;
import com.revrobotics.CANSparkMax.ControlType;

public class SwerveModule
{
    public int moduleNumber;
    private Rotation2d angleOffset;
    private Rotation2d lastAngle;

    private CANSparkMax mAngleMotor;
    private TalonFX mDriveMotor;

    private RelativeEncoder integratedAngleEncoder;
    private SparkMaxPIDController angleController;
    private CANCoder angleEncoder;

    SimpleMotorFeedforward feedforward = new SimpleMotorFeedforward(SwerveConstants.Swerve.driveKS,
            SwerveConstants.Swerve.driveKV, SwerveConstants.Swerve.driveKA);

    public SwerveModule(int moduleNumber, SwerveModuleConstants moduleConstants)
    {
        this.moduleNumber = moduleNumber;
        this.angleOffset = moduleConstants.angleOffset;

        /* Angle Encoder Config */
        angleEncoder = new CANCoder(moduleConstants.cancoderID);
        configAngleEncoder();

        /* Angle Motor Config */
        mAngleMotor = new TeamSparkMAX("AngleMotor", moduleConstants.angleMotorID);
        integratedAngleEncoder = mAngleMotor.getEncoder();
        angleController = mAngleMotor.getPIDController();
        configAngleMotor();

        /* Drive Motor Config */
        mDriveMotor = new TalonFX(moduleConstants.driveMotorID);
        configDriveMotor();

        lastAngle = getState().angle;
    }

    public void setDesiredState(SwerveModuleState desiredState, boolean isOpenLoop)
    {
        /*
         * This is a custom optimize function, since default WPILib optimize assumes continuous
         * controller which CTRE and Rev onboard is not
         */
        desiredState = CTREModuleState.optimize(desiredState, getState().angle);
        setAngle(desiredState);
        setSpeed(desiredState, isOpenLoop);
    }

    private void setSpeed(SwerveModuleState desiredState, boolean isOpenLoop)
    {
        // if isOpenLoop is false, we convert to a Falcon unit. If true, we set the motor speed
        // using a PercentOutput of motor power
        if (isOpenLoop)
        {
            double percentOutput = desiredState.speedMetersPerSecond
                    / SwerveConstants.Swerve.maxSpeed;
            mDriveMotor.set(ControlMode.PercentOutput, percentOutput);
        }
        else
        {
            double velocity = Conversions.MPSToFalcon(desiredState.speedMetersPerSecond,
                    SwerveConstants.Swerve.wheelCircumference,
                    SwerveConstants.Swerve.driveGearRatio);
            mDriveMotor.set(ControlMode.Velocity, velocity, DemandType.ArbitraryFeedForward,
                    feedforward.calculate(desiredState.speedMetersPerSecond));
        }
    }

    private void setAngle(SwerveModuleState desiredState)
    {
        Rotation2d angle = (Math
                .abs(desiredState.speedMetersPerSecond) <= (SwerveConstants.Swerve.maxSpeed * 0.01))
                        ? lastAngle
                        : desiredState.angle; // Prevent rotating module if speed is less then 1%.
                                              // Prevents Jittering.

        angleController.setReference(angle.getDegrees(), ControlType.kPosition);
        lastAngle = angle;
    }

    private Rotation2d getAngle()
    {
        return Rotation2d.fromDegrees(integratedAngleEncoder.getPosition());
    }

    public Rotation2d getCanCoder()
    {
        return Rotation2d.fromDegrees(angleEncoder.getAbsolutePosition());
    }

    public void resetToAbsolute()
    {
        double absolutePosition = getCanCoder().getDegrees() - angleOffset.getDegrees();
        integratedAngleEncoder.setPosition(absolutePosition);
    }

    private void configAngleEncoder()
    {
        angleEncoder.configFactoryDefault();
        CANCoderUtil.setCANCoderBusUsage(angleEncoder, CCUsage.kAll);
        angleEncoder.configAllSettings(Robot.ctreConfigs.swerveCanCoderConfig);
    }

    private void configAngleMotor()
    {
        mAngleMotor.restoreFactoryDefaults();
        CANSparkMaxUtil.setCANSparkMaxBusUsage(mAngleMotor, Usage.kMinimal);
        mAngleMotor.setSmartCurrentLimit(SwerveConstants.Swerve.angleContinuousCurrentLimit);
        mAngleMotor.setInverted(SwerveConstants.Swerve.angleMotorInvert);
        mAngleMotor.setIdleMode(SwerveConstants.Swerve.angleNeutralMode);
        integratedAngleEncoder
                .setPositionConversionFactor(SwerveConstants.Swerve.angleConversionFactor);
        angleController.setP(SwerveConstants.Swerve.angleKP);
        angleController.setI(SwerveConstants.Swerve.angleKI);
        angleController.setD(SwerveConstants.Swerve.angleKD);
        angleController.setFF(SwerveConstants.Swerve.angleKF);
        mAngleMotor.enableVoltageCompensation(SwerveConstants.Swerve.voltageComp);
        mAngleMotor.burnFlash(); // writes configurations to flash memory so they save if a PDP
                                 // breaker trips
        resetToAbsolute();
    }

    private void configDriveMotor()
    {
        mDriveMotor.configFactoryDefault();
        mDriveMotor.configAllSettings(Robot.ctreConfigs.swerveDriveFXConfig);
        mDriveMotor.setInverted(SwerveConstants.Swerve.driveMotorInvert);
        mDriveMotor.setNeutralMode(SwerveConstants.Swerve.driveNeutralMode);
        mDriveMotor.setSelectedSensorPosition(0);
    }

    public SwerveModuleState getState()
    {
        return new SwerveModuleState(Conversions.falconToMPS(
                mDriveMotor.getSelectedSensorVelocity(), SwerveConstants.Swerve.wheelCircumference,
                SwerveConstants.Swerve.driveGearRatio), getAngle());
    }

    public SwerveModulePosition getPosition()
    {
        return new SwerveModulePosition(Conversions.falconToMeters(
                mDriveMotor.getSelectedSensorPosition(), SwerveConstants.Swerve.wheelCircumference,
                SwerveConstants.Swerve.driveGearRatio), getAngle());
    }
}