/*    */ package dmh.robocode.navigator;
/*    */ 
/*    */ import dmh.robocode.data.Location;
/*    */ import dmh.robocode.enemy.EnemyRobot;
/*    */ import dmh.robocode.navigator.target.EnemyTargetAlgorithm;
/*    */ import dmh.robocode.simulate.SimulateableRobot;
/*    */ import java.awt.Graphics2D;
/*    */ 
/*    */ public class NavigateToEnemy
/*    */   implements NavigatorCommand
/*    */ {
/*    */   private Location targetLocation;
/*    */   private NavigateToLocation subNavigator;
/*    */   private EnemyRobot enemy;
/*    */   private EnemyTargetAlgorithm targetAlgorithm;
/*    */ 
/*    */   public NavigateToEnemy(EnemyRobot enemy, SimulateableRobot myRobot, EnemyTargetAlgorithm targetAlgorithm)
/*    */   {
/* 18 */     this.enemy = enemy;
/* 19 */     this.targetAlgorithm = targetAlgorithm;
/*    */ 
/* 21 */     this.targetLocation = targetAlgorithm.getRevisedTargetLocation();
/*    */ 
/* 23 */     this.subNavigator = new NavigateToLocation(this.targetLocation, myRobot);
/*    */   }
/*    */ 
/*    */   public void setWiggleFactor(int wiggleFactor)
/*    */   {
/* 30 */     this.subNavigator.setWiggleFactor(wiggleFactor);
/*    */   }
/*    */ 
/*    */   public int getWiggleFactor()
/*    */   {
/* 36 */     return this.subNavigator.getWiggleFactor();
/*    */   }
/*    */ 
/*    */   public void setWiggleExpiry(long wiggleExpiry)
/*    */   {
/* 41 */     this.subNavigator.setWiggleExpiry(wiggleExpiry);
/*    */   }
/*    */ 
/*    */   public long getWiggleExpiry()
/*    */   {
/* 46 */     return this.subNavigator.getWiggleExpiry();
/*    */   }
/*    */ 
/*    */   public boolean isDone()
/*    */   {
/* 51 */     return !this.enemy.isAlive();
/*    */   }
/*    */ 
/*    */   public double getRightTurn()
/*    */   {
/* 56 */     return this.subNavigator.getRightTurn();
/*    */   }
/*    */ 
/*    */   public double getAhead()
/*    */   {
/* 61 */     return this.subNavigator.getAhead();
/*    */   }
/*    */ 
/*    */   public double getVelocity()
/*    */   {
/* 66 */     return this.subNavigator.getVelocity();
/*    */   }
/*    */ 
/*    */   public EnemyRobot getEnemy() {
/* 70 */     return this.enemy;
/*    */   }
/*    */ 
/*    */   public void executed()
/*    */   {
/* 75 */     this.subNavigator.executed();
/*    */ 
/* 77 */     if (this.enemy.getLatestRadarObservation() != null) {
/* 78 */       this.targetLocation = this.targetAlgorithm.getRevisedTargetLocation();
/* 79 */       this.subNavigator.setTarget(this.targetLocation);
/*    */     }
/*    */   }
/*    */ 
/*    */   public void reverseDirection()
/*    */   {
/* 85 */     this.subNavigator.reverseDirection();
/*    */   }
/*    */ 
/*    */   public void paint(Graphics2D g)
/*    */   {
/* 90 */     this.subNavigator.paint(g);
/*    */   }
/*    */ }

/* Location:           E:\naval-robocode\sample\dmh.robocode.robot.BlackDeathMk7_7.0.jar
 * Qualified Name:     dmh.robocode.navigator.NavigateToEnemy
 * JD-Core Version:    0.6.2
 */