package dmh.robocode.gunner.simulator;

import dmh.robocode.data.Location;

public abstract interface EnemySimulator
{
  public abstract Location getLocation();

  public abstract void takeOneTurn();
}

/* Location:           E:\naval-robocode\sample\dmh.robocode.robot.BlackDeathMk7_7.0.jar
 * Qualified Name:     dmh.robocode.gunner.simulator.EnemySimulator
 * JD-Core Version:    0.6.2
 */