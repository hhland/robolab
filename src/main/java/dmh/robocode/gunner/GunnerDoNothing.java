/*    */ package dmh.robocode.gunner;
/*    */ 
/*    */ import dmh.robocode.enemy.EnemyRobot;
/*    */ import dmh.robocode.gunner.aiming.AimingStrategy;
/*    */ import java.awt.Graphics2D;
/*    */ 
/*    */ public class GunnerDoNothing
/*    */   implements GunnerCommand
/*    */ {
/*    */   public boolean isDone()
/*    */   {
/* 15 */     return true;
/*    */   }
/*    */ 
/*    */   public double getRightTurn()
/*    */   {
/* 20 */     return 0.0D;
/*    */   }
/*    */ 
/*    */   public double getFire()
/*    */   {
/* 25 */     return 0.0D;
/*    */   }
/*    */ 
/*    */   public void executed()
/*    */   {
/*    */   }
/*    */ 
/*    */   public EnemyRobot getEnemyTarget()
/*    */   {
/* 34 */     return null;
/*    */   }
/*    */ 
/*    */   public AimingStrategy getAimingStrategy()
/*    */   {
/* 39 */     return null;
/*    */   }
/*    */ 
/*    */   public void hitEnemy(EnemyRobot enemy)
/*    */   {
/*    */   }
/*    */ 
/*    */   public void paint(Graphics2D g)
/*    */   {
/*    */   }
/*    */ }

/* Location:           E:\naval-robocode\sample\dmh.robocode.robot.BlackDeathMk7_7.0.jar
 * Qualified Name:     dmh.robocode.gunner.GunnerDoNothing
 * JD-Core Version:    0.6.2
 */