/*    */ package gre.svman4;
/*    */ 
/*    */ import java.awt.Color;
/*    */ import java.awt.Graphics2D;
/*    */ import robocode.Rules;
/*    */ 
/*    */ public class GuessFactorTargetingGun extends Gun
/*    */ {
/*    */   public Color getColor()
/*    */   {
/* 20 */     return Color.pink;
/*    */   }
/*    */ 
/*    */   public double getFiringAngle(RobotState shooter, EnemyState enemy, double bulletPower)
/*    */   {
/* 25 */     double bulletVelocity = Rules.getBulletSpeed(bulletPower);
/* 26 */     double[] gunBuffer = enemy.getGunCurrentStatus(shooter.distance(enemy), 
/* 27 */       enemy.velocity, enemy.lastVelocity);
/* 28 */     int mostVisited = (gunBuffer.length - 1) / 2;
/* 29 */     for (int i = 0; i < gunBuffer.length; i++) {
/* 30 */       if (gunBuffer[i] > gunBuffer[mostVisited]) {
/* 31 */         mostVisited = i;
/*    */       }
/*    */     }
/* 34 */     double binWidth = enemy.maxEscapeAngle(bulletVelocity) / (
/* 35 */       (gunBuffer.length - 1) / 2);
/*    */ 
/* 39 */     double angle = enemy.lateralDirection * binWidth * (
/* 40 */       mostVisited - (gunBuffer.length - 1) / 2);
/* 41 */     return shooter.getAngleTo(enemy) + angle;
/*    */   }
/*    */ 
/*    */   public String getName() {
/* 45 */     return "GuessFactorTargeting";
/*    */   }
/*    */ 
/*    */   public void onPaint(Graphics2D g)
/*    */   {
/*    */   }
/*    */ 
/*    */   public Color getColor(double lower, double val, double higher) {
/* 53 */     double range = higher - lower;
/* 54 */     double value = Math.min(range, Math.max(val - lower, 0.0D));
/* 55 */     double H = (range - value) / range * 0.4D;
/*    */ 
/* 57 */     double S = 0.9D;
/* 58 */     double B = 0.9D;
/* 59 */     return Color.getHSBColor((float)H, (float)S, (float)B);
/*    */   }
/*    */ }

/* Location:           E:\naval-robocode\sample\gre.svman4.Morfeas_1.4.jar
 * Qualified Name:     gre.svman4.GuessFactorTargetingGun
 * JD-Core Version:    0.6.2
 */