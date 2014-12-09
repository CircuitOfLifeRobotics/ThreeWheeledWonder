/*----------------------------------------------------------------------------*/
/* Copyright (c) FIRST 2008. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package com.team3925.threewheeledwonder;


import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Relay;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class ThreeWheeledWonder extends IterativeRobot {
    
    Victor left1, left2, left3, right1, right2, right3;
    Joystick xbox;
    Solenoid solenoid1, solenoid2;
    Relay compressorController;
    boolean highGear;
    boolean compressorPower = false;
    
    boolean currentButton;
    boolean lastButton = false;
    
    boolean currentButtonRelay;
    boolean lastButtonRelay = false;
    
    
    
    /**
     * This function is run when the robot is first started up and should be
     * used for any initialization code.
     */
    public void robotInit() {

        left1 = new Victor(1);
        left2 = new Victor(2);
        left3 = new Victor(3);
        right1 = new Victor(4);
        right2 = new Victor(5);
        right3 = new Victor(6);
        
        xbox = new Joystick(1);
        
        solenoid1 = new Solenoid(1);
        solenoid2 = new Solenoid(2);
        
        compressorController = new Relay(1);
        
    }

    /**
     * This function is called periodically during autonomous
     */
    public void autonomousPeriodic() {

    }

    /**
     * This function is called periodically during operator control
     */
    public void teleopPeriodic() {
        
        
//      Soleniod control (turn on and turn off)
        currentButton = xbox.getRawButton(XboxButtonMap.A);
        
        if (currentButton != lastButton && currentButton) {
            highGear = !highGear;
        }
        lastButton = currentButton;
        
        solenoid1.set(highGear);
        solenoid2.set(!highGear);
        
        
//      Compressor control (turn on and turn off)
//        currentButtonRelay = xbox.getRawButton(XboxButtonMap.X);
//        
//        if (currentButtonRelay != lastButtonRelay && currentButtonRelay) {
//            compressorPower = !compressorPower;
//        }
//        lastButtonRelay = currentButtonRelay;
        
        compressorPower = xbox.getRawButton(XboxButtonMap.X);
        
        compressorController.set(compressorPower ? Relay.Value.kForward : Relay.Value.kOff);
        
        
        //arcade drive algorithm copied from RobotDrive
        double leftMotorSpeed;
        double rightMotorSpeed;
        
        double moveValue = xbox.getRawAxis(XboxButtonMap.LY_AXIS);
        double rotateValue = xbox.getRawAxis(XboxButtonMap.RX_AXIS);
        
        if (moveValue > 0.0) {
            if (rotateValue > 0.0) {
                leftMotorSpeed = moveValue - rotateValue;
                rightMotorSpeed = Math.max(moveValue, rotateValue);
            } else {
                leftMotorSpeed = Math.max(moveValue, -rotateValue);
                rightMotorSpeed = moveValue + rotateValue;
            }
        } else {
            if (rotateValue > 0.0) {
                leftMotorSpeed = -Math.max(-moveValue, rotateValue);
                rightMotorSpeed = moveValue + rotateValue;
            } else {
                leftMotorSpeed = moveValue - rotateValue;
                rightMotorSpeed = -Math.max(-moveValue, -rotateValue);
            }
        }
        
        left1.set(leftMotorSpeed);
        left2.set(leftMotorSpeed);
        left3.set(leftMotorSpeed);
        right1.set(rightMotorSpeed);
        right2.set(rightMotorSpeed);
        right3.set(rightMotorSpeed);
        
        SmartDashboard.putBoolean("HighGear", highGear);
        SmartDashboard.putBoolean("Compressor", compressorPower);
        SmartDashboard.putNumber("LeftMotorSpeed", leftMotorSpeed);
        SmartDashboard.putNumber("RightMotorSpeed", rightMotorSpeed);
        
    }
    
    /**
     * This function is called periodically during test mode
     */
    public void testPeriodic() {
    
    }
    
}
