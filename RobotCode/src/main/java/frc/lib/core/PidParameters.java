package frc.lib.core;

import java.util.Objects;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.lib.core.motors.*;
import frc.lib.core.util.TeamUtils;

public class PidParameters
{
	public static double telemetryUpdateInterval_secs = 0.5;

	private double lastTelemetryUpdate = 0;

	public double kP, kI, kD, kF, kIZone, kPeakOutput, maxAcc, maxVel;
	public int errorTolerance;

	public PidParameters( // Talon SRX
			double kP, double kI, double kD, double kF, double kIZone, double kPeakOutput,
			int errorTolerance)
	{
		this.kP = kP;
		this.kI = kI;
		this.kD = kD;
		this.kF = kF;
		this.kIZone = kIZone;
		this.kPeakOutput = kPeakOutput;
		this.errorTolerance = errorTolerance;
	}

	public PidParameters( // Spark MAX
			double kP, double kI, double kD, double kF, double kIZone, double kPeakOutput,
			double maxVel, double maxAcc, int errorTolerance)
	{
		this.kP = kP;
		this.kI = kI;
		this.kD = kD;
		this.kF = kF;
		this.kIZone = kIZone;
		this.kPeakOutput = kPeakOutput;
		this.errorTolerance = errorTolerance;
		this.maxVel = maxVel;
		this.maxAcc = maxAcc;
	}

	@Override
	public PidParameters clone()
	{
		return new PidParameters(kP, kI, kD, kF, kIZone, kPeakOutput, errorTolerance);
	}

	@Override
	public boolean equals(Object obj)
	{
		if (obj == null)
			return false;
		if (!(obj instanceof PidParameters))
			return false;

		PidParameters otherPP = (PidParameters) obj;

		return otherPP.kP == kP && otherPP.kI == kI && otherPP.kD == kD && otherPP.kF == kF
				&& otherPP.kIZone == kIZone && otherPP.kPeakOutput == kPeakOutput
				&& otherPP.errorTolerance == errorTolerance;
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(kP, kI, kD, kF, kIZone, kPeakOutput, errorTolerance);
	}

	/** Use these parameters on a motor */
	public void configureMotorWithPidParameters(ITeamTalon motor, int pidSlotIndex)
	{
		motor.configureWithPidParameters(this, pidSlotIndex);
	}

	public void configureMotorWithPidParameters(TeamSparkMAX motor, int pidSlotIndex)
	{
		motor.configureWithPidParameters(this, pidSlotIndex);
	}

	public void periodic(String prefix, ITeamTalon motor, int pidSlotIndex)
	{
		double now = TeamUtils.getCurrentTime();

		if ((now - lastTelemetryUpdate) < telemetryUpdateInterval_secs)
		{
			return;
		}

		lastTelemetryUpdate = now;

		// We update the motor immediately when the the motor is in a PID-controlled mode
		boolean updateMotor = motor.isRunningPidControlMode();

		double new_kF = SmartDashboard.getNumber(prefix + ".PID.kF", kF);
		if (new_kF != kF)
		{
			kF = new_kF;
			if (updateMotor)
				motor.config_kF(pidSlotIndex, kF);
		}
		SmartDashboard.putNumber(prefix + ".PID.kF", kF);

		double new_kP = SmartDashboard.getNumber(prefix + ".PID.kP", kP);
		if (new_kP != kP)
		{
			kP = new_kP;
			if (updateMotor)
				motor.config_kP(pidSlotIndex, kP);
		}
		SmartDashboard.putNumber(prefix + ".PID.kP", kP);

		double new_kI = SmartDashboard.getNumber(prefix + ".PID.kI", kI);
		if (new_kI != kI)
		{
			kI = new_kI;
			if (updateMotor)
				motor.config_kI(pidSlotIndex, kI);
		}
		SmartDashboard.putNumber(prefix + ".PID.kI", kI);

		double new_kD = SmartDashboard.getNumber(prefix + ".PID.kD", kD);
		if (new_kD != kD)
		{
			kD = new_kD;
			if (updateMotor)
				motor.config_kD(pidSlotIndex, kD);
		}
		SmartDashboard.putNumber(prefix + ".PID.kD", kD);

		double new_kPeakOutput = SmartDashboard.getNumber(prefix + ".kPeakOutput", kPeakOutput);
		if (new_kPeakOutput != kPeakOutput)
		{
			kPeakOutput = new_kPeakOutput;
			if (updateMotor)
				motor.configPeakOutputForward(kPeakOutput);
			if (updateMotor)
				motor.configPeakOutputReverse(-kPeakOutput);
		}
		SmartDashboard.putNumber(prefix + ".kPeakOutput", kPeakOutput);

		int new_errorTolerance = (int) SmartDashboard.getNumber(prefix + ".errorTolerance",
				errorTolerance);
		if (new_errorTolerance != errorTolerance)
		{
			errorTolerance = new_errorTolerance;
			if (updateMotor)
				motor.configAllowableClosedloopError(pidSlotIndex, errorTolerance, 30);
		}
		SmartDashboard.putNumber(prefix + ".errorTolerance", errorTolerance);
	}

	public void periodic(String prefix, TeamSparkMAX motor, int pidSlotIndex)
	{
		double now = TeamUtils.getCurrentTime();

		if ((now - lastTelemetryUpdate) < telemetryUpdateInterval_secs)
		{
			return;
		}

		lastTelemetryUpdate = now;

		// We update the motor immediately when the the motor is in a PID-controlled mode
		boolean updateMotor = motor.isRunningPidControlMode();

		double new_kF = SmartDashboard.getNumber(prefix + ".PID.kF", kF);
		if (new_kF != kF)
		{
			kF = new_kF;
			if (updateMotor)
				motor.canPidController.setFF(kF, pidSlotIndex);
		}
		SmartDashboard.putNumber(prefix + ".PID.kF", kF);

		double new_kP = SmartDashboard.getNumber(prefix + ".PID.kP", kP);
		if (new_kP != kP)
		{
			kP = new_kP;
			if (updateMotor)
				motor.canPidController.setP(kP, pidSlotIndex);
		}
		SmartDashboard.putNumber(prefix + ".PID.kP", kP);

		double new_kI = SmartDashboard.getNumber(prefix + ".PID.kI", kI);
		if (new_kI != kI)
		{
			kI = new_kI;
			if (updateMotor)
				motor.canPidController.setI(kI, pidSlotIndex);
		}
		SmartDashboard.putNumber(prefix + ".PID.kI", kI);

		double new_kD = SmartDashboard.getNumber(prefix + ".PID.kD", kD);
		if (new_kD != kD)
		{
			kD = new_kD;
			if (updateMotor)
				motor.canPidController.setD(kD, pidSlotIndex);
		}
		SmartDashboard.putNumber(prefix + ".PID.kD", kD);

		double new_kPeakOutput = SmartDashboard.getNumber(prefix + ".kPeakOutput", kPeakOutput);
		if (new_kPeakOutput != kPeakOutput)
		{
			kPeakOutput = new_kPeakOutput;
			if (updateMotor)
				motor.canPidController.setOutputRange(-kPeakOutput, kPeakOutput);
		}
		SmartDashboard.putNumber(prefix + ".kPeakOutput", kPeakOutput);

		int new_errorTolerance = (int) SmartDashboard.getNumber(prefix + ".errorTolerance",
				errorTolerance);
		if (new_errorTolerance != errorTolerance)
		{
			errorTolerance = new_errorTolerance;
			if (updateMotor)
				motor.canPidController.setSmartMotionAllowedClosedLoopError(errorTolerance,
						pidSlotIndex);
		}
		SmartDashboard.putNumber(prefix + ".errorTolerance", errorTolerance);

		double new_maxAcc = SmartDashboard.getNumber(prefix + ".maxAcc", maxAcc);
		if (new_maxAcc != maxAcc)
		{
			maxAcc = new_maxAcc;
			if (updateMotor)
				motor.canPidController.setSmartMotionMaxAccel(maxAcc, pidSlotIndex);
		}

		double new_maxVel = SmartDashboard.getNumber(prefix + ".maxAcc", maxVel);
		if (new_maxVel != maxVel)
		{
			maxVel = new_maxVel;
			if (updateMotor)
				motor.canPidController.setSmartMotionMaxVelocity(maxVel, pidSlotIndex);
		}

		SmartDashboard.putNumber(prefix + ".kPeakOutput", kPeakOutput);
	}
}