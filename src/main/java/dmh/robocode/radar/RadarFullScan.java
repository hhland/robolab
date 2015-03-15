/*    */ package dmh.robocode.radar;
/*    */ 
/*    */ import dmh.robocode.data.Location;
/*    */ import dmh.robocode.enemy.EnemySet;
/*    */ import dmh.robocode.robot.CommandBasedRobot;
/*    */ import dmh.robocode.utils.Geometry;
/*    */ import java.awt.Graphics2D;
/*    */ 
/*    */ public class RadarFullScan
/*    */   implements RadarCommand
/*    */ {
/* 11 */   private boolean startedTurn = false;
/*    */   long startingTime;
/*    */   private CommandBasedRobot myRobot;
/*    */   private double scanDirection;
/*    */ 
/*    */   public RadarFullScan(CommandBasedRobot myRobot)
/*    */   {
/* 17 */     this.startingTime = myRobot.getTime();
/* 18 */     this.myRobot = myRobot;
/*    */ 
/* 20 */     setupScanTowardsCentre(myRobot);
/*    */   }
/*    */ 
/*    */   private void setupScanTowardsCentre(CommandBasedRobot myRobot) {
/* 24 */     Location centre = new Location(myRobot.getBattleFieldWidth() / 2.0D, myRobot.getBattleFieldHeight() / 2.0D);
/* 25 */     double headingTowardsCentre = Geometry.getBearingBetweenLocations(myRobot.getLocation(), centre);
/* 26 */     this.scanDirection = (Geometry.getRelativeBearing(myRobot.getRadarHeading(), headingTowardsCentre) >= 0.0D ? 1.0D : -1.0D);
/*    */   }
/*    */ 
/*    */   public boolean isDone()
/*    */   {
/* 32 */     return ((this.startedTurn) && (this.myRobot.getRadarTurnRemaining() == 0.0D)) || (
/* 33 */       this.myRobot.getEnemies().getNumberOfLiveEnemiesSeenSince(this.startingTime) >= this.myRobot.getOthers());
/*    */   }
/*    */ 
/*    */   public double getRightTurn()
/*    */   {
/* 38 */     if (this.startedTurn)
/* 39 */       return this.myRobot.getRadarTurnRemaining();
/* 40 */     return 360.0D * this.scanDirection;
/*    */   }
/*    */ 
/*    */   public void executed()
/*    */   {
/* 45 */     this.startedTurn = true;
/*    */   }
/*    */ 
/*    */   public void paint(Graphics2D g)
/*    */   {
/*    */   }
/*    */ }

/* Location:           E:\naval-robocode\sample\dmh.robocode.robot.BlackDeathMk7_7.0.jar
 * Qualified Name:     dmh.robocode.radar.RadarFullScan
 * JD-Core Version:    0.6.2
 */