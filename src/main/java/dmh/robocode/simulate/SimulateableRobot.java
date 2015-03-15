package dmh.robocode.simulate;

import dmh.robocode.data.Location;

public abstract interface SimulateableRobot
{
  public abstract double getVelocity();

  public abstract double getHeading();

  public abstract Location getLocation();

  public abstract long getTime();
}

/* Location:           E:\naval-robocode\sample\dmh.robocode.robot.BlackDeathMk7_7.0.jar
 * Qualified Name:     dmh.robocode.simulate.SimulateableRobot
 * JD-Core Version:    0.6.2
 */