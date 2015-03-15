/*    */ package dmh.robocode.radar;
/*    */ 
/*    */ import dmh.robocode.data.RadarObservation;
/*    */ import dmh.robocode.enemy.EnemyRobot;
/*    */ import dmh.robocode.robot.CommandBasedRobot;
/*    */ import dmh.robocode.utils.Geometry;
/*    */ import java.awt.Color;
/*    */ import java.awt.Graphics2D;
/*    */ 
/*    */ public class RadarNarrowLock
/*    */   implements RadarCommand
/*    */ {
/*    */   private EnemyRobot enemy;
/*    */   private CommandBasedRobot myRobot;
/* 15 */   private boolean lockedOn = false;
/*    */   private double previousSeenTime;
/*    */   private double scanDirection;
/*    */ 
/*    */   public RadarNarrowLock(EnemyRobot enemy, CommandBasedRobot myRobot)
/*    */   {
/* 20 */     this.enemy = enemy;
/* 21 */     this.myRobot = myRobot;
/*    */ 
/* 23 */     calculateInitialScanDirection();
/*    */   }
/*    */ 
/*    */   private void calculateInitialScanDirection()
/*    */   {
/* 30 */     this.scanDirection = (Geometry.getRelativeBearing(this.myRobot.getRadarHeading(), getEnemyBearing()) >= 0.0D ? 1.0D : -1.0D);
/*    */   }
/*    */ 
/*    */   private double getEnemyBearing() {
/* 34 */     return Geometry.getBearingBetweenLocations(this.myRobot.getLocation(), this.enemy.getLatestRadarObservation().getLocation());
/*    */   }
/*    */ 
/*    */   public boolean isDone()
/*    */   {
/* 39 */     return !this.enemy.isAlive();
/*    */   }
/*    */ 
/*    */   public double getRightTurn()
/*    */   {
/* 44 */     if (this.lockedOn) {
/* 45 */       double targetRadarBearing = getEnemyBearing() + 22.5D * this.scanDirection;
/* 46 */       return Geometry.getRelativeBearing(this.myRobot.getRadarHeading(), targetRadarBearing);
/*    */     }
/* 48 */     return this.scanDirection * 45.0D;
/*    */   }
/*    */ 
/*    */   public void executed()
/*    */   {
/* 55 */     boolean wasLockedOn = this.lockedOn;
/*    */ 
/* 57 */     areWeStillLockedOn();
/*    */ 
/* 59 */     if (this.lockedOn)
/* 60 */       this.scanDirection *= -1.0D;
/* 61 */     else if (wasLockedOn)
/* 62 */       calculateInitialScanDirection();
/*    */   }
/*    */ 
/*    */   private void areWeStillLockedOn()
/*    */   {
/* 71 */     if (this.enemy.getLatestRadarObservation() != null) {
/* 72 */       long timeLastSeen = this.enemy.getLatestRadarObservation().getTimeSeen();
/* 73 */       this.lockedOn = ((this.previousSeenTime < timeLastSeen) && (this.myRobot.getTime() - timeLastSeen <= 2L));
/*    */     } else {
/* 75 */       this.lockedOn = false;
/*    */     }
/*    */   }
/*    */ 
/*    */   public void paint(Graphics2D g)
/*    */   {
/* 81 */     this.enemy.drawCircle(g, Color.RED, 30);
/*    */   }
/*    */ }

/* Location:           E:\naval-robocode\sample\dmh.robocode.robot.BlackDeathMk7_7.0.jar
 * Qualified Name:     dmh.robocode.radar.RadarNarrowLock
 * JD-Core Version:    0.6.2
 */