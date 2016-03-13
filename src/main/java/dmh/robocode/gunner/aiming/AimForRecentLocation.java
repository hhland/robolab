/*    */ package dmh.robocode.gunner.aiming;
/*    */ 
/*    */ import dmh.robocode.data.Location;
/*    */ import dmh.robocode.data.RadarObservation;
/*    */ import dmh.robocode.enemy.EnemyRobot;
/*    */ import dmh.robocode.robot.CommandBasedRobot;
/*    */ import dmh.robocode.utils.Geometry;
/*    */ import java.awt.Color;
/*    */ import java.util.List;
/*    */ 
/*    */ public class AimForRecentLocation extends CalibratedAimingStrategy
/*    */ {
/*    */   public AimForRecentLocation(CommandBasedRobot myRobot, EnemyRobot enemy, boolean isLearningAllowed)
/*    */   {
/* 15 */     super(myRobot, enemy, isLearningAllowed);
/*    */   }
/*    */ 
/*    */   public Color getDebugColour()
/*    */   {
/* 20 */     return Color.YELLOW;
/*    */   }
/*    */ 
/*    */   public double getEstimatedSuccessOfShotUsingRules(double bulletSpeed)
/*    */   {
/* 26 */     Location target = getTargetForShot(bulletSpeed);
/* 27 */     if (target == null) {
/* 28 */       return 0.0D;
/*    */     }
/* 30 */     int timeRange = Math.max(20, getApproximateTimeUntilHitTarget(target, bulletSpeed));
/* 31 */     List<RadarObservation> observations = getEnemy().getObservationsSince(getMyRobot().getTime() - timeRange);
/* 32 */     double targetDelta = Math.min(getMyRobot().getHeight(), getMyRobot().getWidth()) / 2.0D;
/* 33 */     double hits = 0.0D;
/* 34 */     for (RadarObservation observation : observations) {
/* 35 */       if (observation.getLocation().isSameAs(target, targetDelta)) {
/* 36 */         hits += scoreForHitting(timeRange, getMyRobot().getTime() - observation.getTimeSeen());
/*    */       }
/*    */     }
/* 39 */     return hits * 100.0D / (timeRange + 1);
/*    */   }
/*    */ 
/*    */   public Location getTargetForShot(double bulletSpeed)
/*    */   {
/* 45 */     RadarObservation latestObservation = getEnemy().getLatestRadarObservation();
/*    */ 
/* 47 */     if (latestObservation != null) {
/* 48 */       return latestObservation.getLocation();
/*    */     }
/* 50 */     return null;
/*    */   }
/*    */ 
/*    */   private double scoreForHitting(int timeRange, long ageOfObservation)
/*    */   {
/* 56 */     if (ageOfObservation >= timeRange / 3)
/* 57 */       return 1.7D;
/* 58 */     if (ageOfObservation >= timeRange / 3 * 2) {
/* 59 */       return 1.0D;
/*    */     }
/* 61 */     return 0.3D;
/*    */   }
/*    */ 
/*    */   private int getApproximateTimeUntilHitTarget(Location target, double bulletSpeed)
/*    */   {
/* 66 */     double distance = Geometry.getDistanceBetweenLocations(getMyRobot().getLocation(), target);
/* 67 */     return (int)(distance / bulletSpeed);
/*    */   }
/*    */ }

/* Location:           E:\naval-robocode\sample\dmh.robocode.robot.BlackDeathMk7_7.0.jar
 * Qualified Name:     dmh.robocode.gunner.aiming.AimForRecentLocation
 * JD-Core Version:    0.6.2
 */