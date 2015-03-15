/*    */ package gre.svman4;
/*    */ 
/*    */ import java.awt.Color;
/*    */ import java.awt.Graphics2D;
/*    */ import java.io.PrintStream;
/*    */ 
/*    */ abstract class Gun
/*    */ {
/* 26 */   public int gunShootingCounter = 0;
/* 27 */   public int gunSuccessShootingCounter = 0;
/*    */ 
/*    */   public abstract Color getColor();
/*    */ 
/*    */   public abstract double getFiringAngle(RobotState paramRobotState, EnemyState paramEnemyState, double paramDouble);
/*    */ 
/*    */   public abstract String getName();
/*    */ 
/*    */   public abstract void onPaint(Graphics2D paramGraphics2D);
/*    */ 
/* 32 */   public void printStatisticData() { System.out.println("----" + getName() + "----");
/* 33 */     System.out.println("Gun shooting counter..." + this.gunShootingCounter);
/* 34 */     System.out.println("Gun success counter..." + this.gunSuccessShootingCounter);
/*    */   }
/*    */ }

/* Location:           E:\naval-robocode\sample\gre.svman4.Morfeas_1.4.jar
 * Qualified Name:     gre.svman4.Gun
 * JD-Core Version:    0.6.2
 */