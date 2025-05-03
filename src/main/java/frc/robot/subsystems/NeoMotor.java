package frc.robot.subsystems;

import static frc.robot.utilities.Util.logf;

import com.revrobotics.RelativeEncoder;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.SparkMax;

import com.revrobotics.spark.config.ClosedLoopConfig; // added to configure PID
import com.revrobotics.spark.ClosedLoopSlot; // added to configure PID Slot
import com.revrobotics.spark.SparkBase;
import com.revrobotics.spark.SparkClosedLoopController;

import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.subsystems.PID;

public class NeoMotor extends SubsystemBase {
  private SparkMax m_motor;
  private RelativeEncoder m_encoder;
  private XboxController driveHID;
  private ClosedLoopConfig m_pidConfig;

  // configuring PID slot
  public static final ClosedLoopSlot PID_SLOT = ClosedLoopSlot.kSlot0;

  public NeoMotor(XboxController driveHID, PID pid) {
    int motorID = 20; // Replace with the actual CAN ID of your NEO
    m_motor = new SparkMax(motorID, MotorType.kBrushless);
    this.driveHID = driveHID;

    // Factory reset, just in case
    // m_motor.restoreFactoryDefaults();

    // Get the encoder object
    m_encoder = m_motor.getEncoder();

    // Initialize ClosedLoopConfig for PID controller + give value
    m_pidConfig = new ClosedLoopConfig();

    // Configure PID Gains from values from PID subsystem
    m_pidConfig.pidf(pid.kP, pid.kI, pid.kD, pid.kFF, PID_SLOT);

    // Sets motor to linear range
    m_pidConfig.positionWrappingEnabled(false);

    // Set velocity feedforward gain (optional)

    // Optional: Set the idle mode (kCoast or kBrake)
    // m_motor.setIdleMode(CANSparkMax.IdleMode.kCoast);
  }



  @Override
  public void periodic() {
    double value = driveHID.getLeftTriggerAxis();
    logf("NEO Value:%.2f\n", value);
    m_motor.set(value);

    // PID Control
    double targetPosition = value * 100.0; 
    // ^^ Need help with this.. how to get the value for the target postion?

    // Retrieves closedloopcontroller from the spark class
    SparkClosedLoopController controller = m_motor.getClosedLoopController();
    controller.setReference(targetPosition, SparkBase.ControlType.kPosition, PID_SLOT);
  }
}
