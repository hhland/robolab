package dmh.robocode.gunner.aiming;

import dmh.robocode.data.Location;
import dmh.robocode.robot.CommandBasedRobot;
import java.awt.Color;

public abstract interface AimingStrategy
{
  public abstract Color getDebugColour();

  public abstract double getEstimatedSuccessOfShot(String paramString, double paramDouble);

  public abstract Location getTargetForShot(double paramDouble);

  public abstract void simulateShot(String paramString, double paramDouble);

  public abstract void notifyShotJustFired();

  public abstract void processTurn();

  public abstract void processEndOfRound(CommandBasedRobot paramCommandBasedRobot);

  public abstract void debugDisplayStats();
}

/* Location:           E:\naval-robocode\sample\dmh.robocode.robot.BlackDeathMk7_7.0.jar
 * Qualified Name:     dmh.robocode.gunner.aiming.AimingStrategy
 * JD-Core Version:    0.6.2
 */