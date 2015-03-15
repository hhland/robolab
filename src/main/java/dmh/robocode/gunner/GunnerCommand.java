package dmh.robocode.gunner;

import dmh.robocode.enemy.EnemyRobot;
import dmh.robocode.gunner.aiming.AimingStrategy;
import java.awt.Graphics2D;

public abstract interface GunnerCommand
{
  public abstract boolean isDone();

  public abstract double getRightTurn();

  public abstract double getFire();

  public abstract EnemyRobot getEnemyTarget();

  public abstract AimingStrategy getAimingStrategy();

  public abstract void executed();

  public abstract void hitEnemy(EnemyRobot paramEnemyRobot);

  public abstract void paint(Graphics2D paramGraphics2D);
}

/* Location:           E:\naval-robocode\sample\dmh.robocode.robot.BlackDeathMk7_7.0.jar
 * Qualified Name:     dmh.robocode.gunner.GunnerCommand
 * JD-Core Version:    0.6.2
 */