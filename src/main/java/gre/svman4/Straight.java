/*    */ package gre.svman4;
/*    */ 
/*    */ import java.awt.Color;
/*    */ import java.awt.geom.Rectangle2D;
/*    */ import robocode.AdvancedRobot;
/*    */ 
/*    */ public class Straight extends OneVsOneMovementStrategy
/*    */ {
/*    */   public Straight(RobotState me, Rectangle2D movingArea, EnemyState target)
/*    */   {
/* 14 */     super(me, movingArea, target);
/*    */   }
/*    */ 
/*    */   public void run(AdvancedRobot robot) {
/* 18 */     this.movingAngle = 0.0D;
/* 19 */     if (this.mineRobot.distance(this.target) < 22.0D) {
/* 20 */       this.movingAngle = 3.141592653589793D;
/*    */     }
/* 22 */     else if (this.mineRobot.distance(this.target) < 50.0D)
/* 23 */       this.movingAngle = (this.mineRobot.getAngleTo(this.target) - 1.570796326794897D);
/*    */     else {
/* 25 */       this.movingAngle = robot.getHeadingRadians();
/*    */     }
/*    */ 
/* 28 */     this.movingAngle = wallSmoothing(this.mineRobot, this.movingAngle, 1);
/* 29 */     goTo(this.movingAngle, 100.0D, robot);
/*    */   }
/*    */ 
/*    */   public String getName()
/*    */   {
/* 38 */     return "Straight";
/*    */   }
/*    */ 
/*    */   public Color getColor() {
/* 42 */     return Color.MAGENTA;
/*    */   }
/*    */ }

/* Location:           E:\naval-robocode\sample\gre.svman4.Morfeas_1.4.jar
 * Qualified Name:     gre.svman4.Straight
 * JD-Core Version:    0.6.2
 */