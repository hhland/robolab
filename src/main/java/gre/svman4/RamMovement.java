/*    */ package gre.svman4;
/*    */ 
/*    */ import java.awt.Color;
/*    */ import java.awt.Graphics2D;
/*    */ import robocode.AdvancedRobot;
/*    */ 
/*    */ public class RamMovement extends Movement
/*    */ {
/*    */   private RobotState target;
/*    */ 
/*    */   public RamMovement(RobotState me, RobotState target)
/*    */   {
/* 21 */     super(me);
/* 22 */     setTarget(target);
/*    */   }
/*    */   public void setTarget(RobotState tar) {
/* 25 */     this.target = tar;
/*    */   }
/*    */   public void run(AdvancedRobot robot) {
/* 28 */     this.nextPosition.setLocation(this.target);
/* 29 */     goTo(this.nextPosition, robot);
/*    */   }
/*    */ 
/*    */   public String getName()
/*    */   {
/* 34 */     return "Ram";
/*    */   }
/*    */ 
/*    */   public void onPaint(Graphics2D g) {
/* 38 */     g.setColor(Color.RED);
/* 39 */     g.drawLine((int)this.mineRobot.x, (int)this.mineRobot.y, (int)this.nextPosition.x, 
/* 40 */       (int)this.nextPosition.y);
/*    */   }
/*    */ 
/*    */   public Color getColor()
/*    */   {
/* 47 */     return Color.BLACK;
/*    */   }
/*    */ }

/* Location:           E:\naval-robocode\sample\gre.svman4.Morfeas_1.4.jar
 * Qualified Name:     gre.svman4.RamMovement
 * JD-Core Version:    0.6.2
 */