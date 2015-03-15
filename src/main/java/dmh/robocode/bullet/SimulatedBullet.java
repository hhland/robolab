/*    */ package dmh.robocode.bullet;
/*    */ 
/*    */ import dmh.robocode.data.BattleConstants;
/*    */ import dmh.robocode.data.Location;
/*    */ import dmh.robocode.data.RadarObservation;
/*    */ import dmh.robocode.enemy.EnemyRobot;
/*    */ import dmh.robocode.utils.Geometry;
/*    */ 
/*    */ public class SimulatedBullet
/*    */ {
/* 13 */   private BulletResult result = BulletResult.IN_PROGRESS;
/*    */   private EnemyRobot enemy;
/*    */   private Location startLocation;
/*    */   private long startTime;
/*    */   private double bearing;
/*    */   private double velocity;
/*    */   private double robotRadius;
/*    */ 
/*    */   public SimulatedBullet(EnemyRobot enemy, Location startLocation, long startTime, double bearing, double velocity)
/*    */   {
/* 22 */     this.enemy = enemy;
/* 23 */     this.startLocation = startLocation;
/* 24 */     this.startTime = startTime;
/* 25 */     this.bearing = bearing;
/* 26 */     this.velocity = velocity;
/* 27 */     this.robotRadius = (Math.min(BattleConstants.getInstance().getRobotHeight(), BattleConstants.getInstance().getRobotWidth()) / 2.0D);
/*    */   }
/*    */ 
/*    */   public BulletResult getResult() {
/* 31 */     return this.result;
/*    */   }
/*    */ 
/*    */   public void processAtTime(long time) {
/* 35 */     if (this.result == BulletResult.IN_PROGRESS) {
/* 36 */       double enemyLocationTolerance = Math.abs(time - this.enemy.getLatestRadarObservation().getTimeSeen()) * 8.0D;
/* 37 */       if (enemyLocationTolerance <= this.robotRadius) {
/* 38 */         Location myLocation = Geometry.getLocationAtBearing(this.startLocation, this.bearing, (time - this.startTime) * this.velocity);
/* 39 */         double distanceToEnemy = Geometry.getDistanceBetweenLocations(myLocation, this.enemy.getLatestRadarObservation().getLocation());
/* 40 */         if (!this.enemy.isAlive())
/* 41 */           this.result = BulletResult.DEAD;
/* 42 */         else if (isLocationOutOfBattle(myLocation))
/* 43 */           this.result = BulletResult.MISS;
/* 44 */         else if (distanceToEnemy <= this.robotRadius - enemyLocationTolerance)
/* 45 */           this.result = BulletResult.HIT;
/*    */       }
/*    */     }
/*    */   }
/*    */ 
/*    */   private boolean isLocationOutOfBattle(Location myLocation)
/*    */   {
/* 52 */     return (myLocation.getX() < 0.0D) || 
/* 53 */       (myLocation.getY() < 0.0D) || 
/* 54 */       (myLocation.getX() > BattleConstants.getInstance().getBattlefieldWidth()) || (
/* 55 */       myLocation.getY() > BattleConstants.getInstance().getBattlefieldHeight());
/*    */   }
/*    */ 
/*    */   public static enum BulletResult
/*    */   {
/* 11 */     IN_PROGRESS, HIT, MISS, DEAD;
/*    */   }
/*    */ }

/* Location:           E:\naval-robocode\sample\dmh.robocode.robot.BlackDeathMk7_7.0.jar
 * Qualified Name:     dmh.robocode.bullet.SimulatedBullet
 * JD-Core Version:    0.6.2
 */