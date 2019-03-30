package radnor;

import robocode.AdvancedRobot;
import robocode.ScannedRobotEvent;

public class DoctorBob
  extends AdvancedRobot
{
  static final double STOPDECOMPILING = 0.002755783029464731D;
  static double a;
  static double b,c,d;
  
  public void run()
  {
    setAdjustGunForRobotTurn(true);
    setAdjustRadarForGunTurn(true);
    b = 190.0D;
    for (;;)
    {
      turnRadarRightRadians(Double.POSITIVE_INFINITY);
    }
  }
  
  public void onScannedRobot(ScannedRobotEvent e)
  {
    ;
    ;
    setTurnRightRadians(Math.cos((c = e.getBearingRadians()) - 0.002755783029464731D * b * ((d = e.getDistance()) < 200.0D ? -1 : 1)));
    if (getDistanceRemaining() == 0.0D) {
      setAhead((b = -b) * (0.4D + Math.random()));
    }
    if (d < 1000.0D / Math.sqrt(getOthers()))
    {
      setTurnRadarRightRadians(5 * Math.sin(c += getHeadingRadians() - getRadarHeadingRadians()));
      setTurnGunRightRadians(Math.sin(c - getGunHeadingRadians() + Math.asin(Math.sin(e.getHeadingRadians() - c) * (c = e.getVelocity()) / (20.0D - 3 * (d = Math.min(3, Math.max(1.0D, 400.0D / d)))))));
      if ((a = 0.9D * a + 0.1D * c) * c >= 0.0D) {
        setFire(d);
      }
    }
    clearAllEvents();
  }
}
