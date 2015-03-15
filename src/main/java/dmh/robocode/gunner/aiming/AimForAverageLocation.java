/*    */ package dmh.robocode.gunner.aiming;
/*    */ 
/*    */ import dmh.robocode.data.Location;
/*    */ import dmh.robocode.enemy.EnemyRobot;
/*    */ import dmh.robocode.robot.CommandBasedRobot;
/*    */ import java.awt.Color;
/*    */ 
/*    */ public class AimForAverageLocation extends CalibratedAimingStrategy
/*    */ {
/*    */   long timePeriod;
/*    */   int minimumObservations;
/*    */ 
/*    */   public AimForAverageLocation(CommandBasedRobot myRobot, EnemyRobot enemy, boolean isLearningAllowed, long timePeriod, int minimumObservations)
/*    */   {
/* 15 */     super(myRobot, enemy, isLearningAllowed);
/* 16 */     this.timePeriod = timePeriod;
/* 17 */     this.minimumObservations = minimumObservations;
/*    */   }
/*    */ 
/*    */   public Color getDebugColour()
/*    */   {
/* 22 */     return Color.WHITE;
/*    */   }
/*    */ 
/*    */   public double getEstimatedSuccessOfShotUsingRules(double bulletSpeed)
/*    */   {
/* 27 */     return getDefaultEstimatedSuccess(bulletSpeed);
/*    */   }
/*    */ 
/*    */   public Location getTargetForShot(double bulletSpeed)
/*    */   {
/* 32 */     return getEnemy().getAverageLocation();
/*    */   }
/*    */ }

/* Location:           E:\naval-robocode\sample\dmh.robocode.robot.BlackDeathMk7_7.0.jar
 * Qualified Name:     dmh.robocode.gunner.aiming.AimForAverageLocation
 * JD-Core Version:    0.6.2
 */