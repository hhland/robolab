/*    */ package gre.svman4;
/*    */ 
/*    */ import java.awt.Color;
/*    */ import java.awt.Graphics2D;
/*    */ import robocode.Rules;
/*    */ 
/*    */ class LinearTargetingGun extends Gun
/*    */ {
/*    */   public Color getColor()
/*    */   {
/* 10 */     return Color.YELLOW;
/*    */   }
/*    */ 
/*    */   public double getFiringAngle(RobotState shooter, EnemyState target, double bulletPower)
/*    */   {
/* 15 */     double directAngle = shooter.getAngleTo(target);
/* 16 */     return directAngle - target.velocity * 
/* 17 */       Math.sin(directAngle - target.heading) / 
/* 18 */       Rules.getBulletSpeed(bulletPower);
/*    */   }
/*    */ 
/*    */   public String getName() {
/* 22 */     return "LinearTargetingGun";
/*    */   }
/*    */ 
/*    */   public void onPaint(Graphics2D g)
/*    */   {
/*    */   }
/*    */ }

/* Location:           E:\naval-robocode\sample\gre.svman4.Morfeas_1.4.jar
 * Qualified Name:     gre.svman4.LinearTargetingGun
 * JD-Core Version:    0.6.2
 */