/*    */ package dmh.robocode.gunner.aiming;
/*    */ 
/*    */ import dmh.robocode.data.BattleConstants;
/*    */ import dmh.robocode.data.Location;
/*    */ import dmh.robocode.data.RadarObservation;
/*    */ import dmh.robocode.enemy.EnemyRobot;
/*    */ import dmh.robocode.robot.CommandBasedRobot;
/*    */ import dmh.robocode.utils.Geometry;
/*    */ import java.awt.Color;
/*    */ 
/*    */ public class AimBySprayingAtLongDistance extends CalibratedAimingStrategy
/*    */ {
/*    */   private static final double MIN_SHOOTING_DISTANCE = 250.0D;
/* 14 */   private double sprayAngle = 90.0D;
/* 15 */   private double sprayDistance = BattleConstants.getInstance().getRobotWidth();
/*    */ 
/*    */   public AimBySprayingAtLongDistance(CommandBasedRobot myRobot, EnemyRobot enemy, boolean isLearningAllowed) {
/* 18 */     super(myRobot, enemy, isLearningAllowed);
/*    */   }
/*    */ 
/*    */   public Color getDebugColour()
/*    */   {
/* 23 */     return Color.PINK;
/*    */   }
/*    */ 
/*    */   public double getEstimatedSuccessOfShotUsingRules(double bulletSpeed)
/*    */   {
/* 28 */     return Math.min(100.0D, getDefaultEstimatedSuccess(bulletSpeed));
/*    */   }
/*    */ 
/*    */   public void notifyShotJustFired()
/*    */   {
/* 33 */     this.sprayAngle = (-this.sprayAngle);
/*    */   }
/*    */ 
/*    */   public Location getTargetForShot(double bulletSpeed)
/*    */   {
/* 39 */     Location currentEnemyLocation = getEnemy().getLatestRadarObservation().getLocation();
/* 40 */     double distance = Geometry.getDistanceBetweenLocations(currentEnemyLocation, getMyRobot().getLocation());
/*    */ 
/* 42 */     if (distance > 250.0D) {
/* 43 */       double bearingTowardsUs = Geometry.getBearingBetweenLocations(currentEnemyLocation, getMyRobot().getLocation());
/* 44 */       return Geometry.getLocationAtBearing(currentEnemyLocation, bearingTowardsUs + this.sprayAngle, this.sprayDistance);
/*    */     }
/* 46 */     return null;
/*    */   }
/*    */ }

/* Location:           E:\naval-robocode\sample\dmh.robocode.robot.BlackDeathMk7_7.0.jar
 * Qualified Name:     dmh.robocode.gunner.aiming.AimBySprayingAtLongDistance
 * JD-Core Version:    0.6.2
 */