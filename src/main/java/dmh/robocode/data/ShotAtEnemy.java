/*    */ package dmh.robocode.data;
/*    */ 
/*    */ import dmh.robocode.enemy.EnemyRobot;
/*    */ import dmh.robocode.gunner.aiming.AimingStrategy;
/*    */ import dmh.robocode.robot.CommandBasedRobot;
/*    */ import dmh.robocode.utils.ConvertStatistic;
/*    */ import robocode.util.Utils;
/*    */ 
/*    */ public class ShotAtEnemy extends Shot
/*    */ {
/*    */   private ShotStatus status;
/*    */   private byte enemyVelocityTimesTen;
/*    */   private short headingDifferential;
/*    */ 
/*    */   public ShotAtEnemy(CommandBasedRobot myRobot, EnemyRobot enemyRobot, double bulletPower, AimingStrategy aimingStrategy)
/*    */   {
/* 50 */     super(myRobot, enemyRobot, bulletPower);
/*    */ 
/* 52 */     RadarObservation enemyObservation = enemyRobot.getLatestRadarObservation();
/*    */ 
/* 54 */     if (enemyObservation == null) {
/* 55 */       this.enemyVelocityTimesTen = 0;
/* 56 */       this.headingDifferential = 0;
/*    */     } else {
/* 58 */       this.enemyVelocityTimesTen = ConvertStatistic.makeByte(enemyObservation.getVelocity() * 10.0D);
/* 59 */       this.headingDifferential = getDifferential(myRobot.getHeading(), enemyObservation.getHeading());
/*    */     }
/*    */ 
/* 62 */     this.status = ShotStatus.IN_PROGRESS;
/*    */   }
/*    */ 
/*    */   private short getDifferential(double myHeading, double enemyHeading) {
/* 66 */     double diff = Utils.normalRelativeAngleDegrees(enemyHeading - myHeading);
/* 67 */     return ConvertStatistic.makeShort(diff);
/*    */   }
/*    */ 
/*    */   public void setStatus(ShotStatus newStatus) {
/* 71 */     this.status = newStatus;
/*    */   }
/*    */ 
/*    */   public ShotStatus getStatus() {
/* 75 */     return this.status;
/*    */   }
/*    */ 
/*    */   public double getEnemyVelocity() {
/* 79 */     return this.enemyVelocityTimesTen / 10.0D;
/*    */   }
/*    */ 
/*    */   public short getHeadingDifferential() {
/* 83 */     return this.headingDifferential;
/*    */   }
/*    */ 
/*    */   public static enum ShotStatus
/*    */   {
/* 42 */     IN_PROGRESS, HIT_TARGET, HIT_OTHER, HIT_BULLET, MISS, ALREADY_DEAD, END_ROUND;
/*    */   }
/*    */ }

/* Location:           E:\naval-robocode\sample\dmh.robocode.robot.BlackDeathMk7_7.0.jar
 * Qualified Name:     dmh.robocode.data.ShotAtEnemy
 * JD-Core Version:    0.6.2
 */