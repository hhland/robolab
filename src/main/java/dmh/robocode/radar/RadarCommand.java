package dmh.robocode.radar;

import java.awt.Graphics2D;

public abstract interface RadarCommand
{
  public abstract boolean isDone();

  public abstract double getRightTurn();

  public abstract void executed();

  public abstract void paint(Graphics2D paramGraphics2D);
}

/* Location:           E:\naval-robocode\sample\dmh.robocode.robot.BlackDeathMk7_7.0.jar
 * Qualified Name:     dmh.robocode.radar.RadarCommand
 * JD-Core Version:    0.6.2
 */