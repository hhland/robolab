/*    */ package dmh.robocode.navigator.target;
/*    */ 
/*    */ import dmh.robocode.data.BattleConstants;
/*    */ import dmh.robocode.data.Location;
/*    */ import dmh.robocode.data.RadarObservation;
/*    */ import dmh.robocode.enemy.EnemyRobot;
/*    */ import dmh.robocode.simulate.SimulateableRobot;
/*    */ import dmh.robocode.utils.Geometry;
/*    */ 
/*    */ public class EnemyTargetDefault
/*    */   implements EnemyTargetAlgorithm
/*    */ {
/*    */   private EnemyRobot enemy;
/*    */   private double safeTargetDistance;
/*    */   private SimulateableRobot myRobot;
/*    */ 
/*    */   public EnemyTargetDefault(EnemyRobot enemy, SimulateableRobot myRobot, double safeTargetDistance)
/*    */   {
/* 17 */     this.enemy = enemy;
/* 18 */     this.myRobot = myRobot;
/* 19 */     this.safeTargetDistance = safeTargetDistance;
/*    */   }
/*    */ 
/*    */   public Location getRevisedTargetLocation()
/*    */   {
/* 24 */     Location enemyLocation = this.enemy.getLatestRadarObservation().getLocation();
/* 25 */     double enemyBearing = Geometry.getBearingBetweenLocations(this.myRobot.getLocation(), enemyLocation);
/* 26 */     double enemyDistance = Geometry.getDistanceBetweenLocations(this.myRobot.getLocation(), enemyLocation);
/*    */ 
/* 28 */     if (enemyDistance > this.safeTargetDistance) {
/* 29 */       double offset = (4.0D * this.safeTargetDistance - enemyDistance) / 3.0D;
/*    */ 
/* 31 */       Location leftOption = Geometry.getLocationAtBearing(enemyLocation, enemyBearing - 90.0D, offset);
/* 32 */       Location rightOption = Geometry.getLocationAtBearing(enemyLocation, enemyBearing + 90.0D, offset);
/*    */ 
/* 34 */       return getFurthestFromEdges(leftOption, rightOption);
/*    */     }
/* 36 */     Location leftOption = Geometry.getLocationAtBearing(this.myRobot.getLocation(), enemyBearing - 90.0D, this.safeTargetDistance * 1.1D);
/* 37 */     Location rightOption = Geometry.getLocationAtBearing(this.myRobot.getLocation(), enemyBearing + 90.0D, this.safeTargetDistance * 1.1D);
/*    */ 
/* 39 */     return getFurthestFromEdges(leftOption, rightOption);
/*    */   }
/*    */ 
/*    */   private Location getFurthestFromEdges(Location firstOption, Location secondOption)
/*    */   {
/* 45 */     if (firstOption.getHowCloseToEdgeOfBattlefield(BattleConstants.getInstance().getBattlefieldHeight(), BattleConstants.getInstance().getBattlefieldWidth()) > 
/* 45 */       secondOption.getHowCloseToEdgeOfBattlefield(BattleConstants.getInstance().getBattlefieldHeight(), BattleConstants.getInstance().getBattlefieldWidth())) {
/* 46 */       return firstOption;
/*    */     }
/* 48 */     return secondOption;
/*    */   }
/*    */ }

/* Location:           E:\naval-robocode\sample\dmh.robocode.robot.BlackDeathMk7_7.0.jar
 * Qualified Name:     dmh.robocode.navigator.target.EnemyTargetDefault
 * JD-Core Version:    0.6.2
 */