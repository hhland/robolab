/*    */ package dmh.robocode.gunner.aiming;
/*    */ 
/*    */ import dmh.robocode.data.Location;
/*    */ import dmh.robocode.data.RadarObservation;
/*    */ import dmh.robocode.enemy.EnemyRobot;
/*    */ import dmh.robocode.gunner.simulator.EnemySimulator;
/*    */ import dmh.robocode.gunner.simulator.SteadyChangeEnemySimulator;
/*    */ import dmh.robocode.robot.CommandBasedRobot;
/*    */ import dmh.robocode.utils.Geometry;
/*    */ import java.awt.Color;
/*    */ 
/*    */ public class AimByInterpolatingStraightAhead extends CalibratedAimingStrategyUsingEnemySimulator
/*    */ {
/*    */   public AimByInterpolatingStraightAhead(CommandBasedRobot myRobot, EnemyRobot enemy, boolean isLearningAllowed)
/*    */   {
/* 16 */     super(myRobot, enemy, isLearningAllowed);
/*    */   }
/*    */ 
/*    */   public Color getDebugColour()
/*    */   {
/* 21 */     return Color.CYAN;
/*    */   }
/*    */ 
/*    */   public double getEstimatedSuccessOfShotUsingRules(double bulletSpeed)
/*    */   {
/* 26 */     return getDefaultEstimatedSuccess(bulletSpeed);
/*    */   }
/*    */ 
/*    */   public Location getTargetForShot(double bulletSpeed)
/*    */   {
/* 31 */     int period = 5;
/*    */ 
/* 33 */     RadarObservation oldObservation = getEnemy().getObservationAtTime(getMyRobot().getTime() - period);
/*    */ 
/* 35 */     if (oldObservation != null) {
/* 36 */       Location oldLocation = oldObservation.getLocation();
/* 37 */       Location currentLocation = getEnemy().getLatestRadarObservation().getLocation();
/* 38 */       double velocity = Geometry.getDistanceBetweenLocations(oldLocation, currentLocation) / period;
/* 39 */       double bearing = Geometry.getBearingBetweenLocations(oldLocation, currentLocation);
/*    */ 
/* 41 */       EnemySimulator simulator = new SteadyChangeEnemySimulator(currentLocation, velocity, bearing, 0.0D, 0.0D);
/* 42 */       return getSimulatorIntercept(simulator, bulletSpeed);
/*    */     }
/* 44 */     return null;
/*    */   }
/*    */ }

/* Location:           E:\naval-robocode\sample\dmh.robocode.robot.BlackDeathMk7_7.0.jar
 * Qualified Name:     dmh.robocode.gunner.aiming.AimByInterpolatingStraightAhead
 * JD-Core Version:    0.6.2
 */