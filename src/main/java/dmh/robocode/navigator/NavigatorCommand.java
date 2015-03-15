package dmh.robocode.navigator;

import java.awt.Graphics2D;

public abstract interface NavigatorCommand
{
  public abstract boolean isDone();

  public abstract double getRightTurn();

  public abstract double getAhead();

  public abstract double getVelocity();

  public abstract void executed();

  public abstract void setWiggleFactor(int paramInt);

  public abstract int getWiggleFactor();

  public abstract void setWiggleExpiry(long paramLong);

  public abstract long getWiggleExpiry();

  public abstract void reverseDirection();

  public abstract void paint(Graphics2D paramGraphics2D);
}

/* Location:           E:\naval-robocode\sample\dmh.robocode.robot.BlackDeathMk7_7.0.jar
 * Qualified Name:     dmh.robocode.navigator.NavigatorCommand
 * JD-Core Version:    0.6.2
 */