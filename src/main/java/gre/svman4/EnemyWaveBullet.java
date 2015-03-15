/*    */ package gre.svman4;
/*    */ 
/*    */ import java.awt.BasicStroke;
/*    */ import java.awt.Color;
/*    */ import java.awt.Graphics2D;
/*    */ import java.awt.Stroke;
/*    */ 
/*    */ public class EnemyWaveBullet extends WaveBullet
/*    */ {
/*    */   private Color getColor(double lower, double val, double higher)
/*    */   {
/* 22 */     double range = higher - lower;
/* 23 */     double value = Math.min(range, Math.max(val - lower, 0.0D));
/* 24 */     double H = (range - value) / range * 0.4D;
/*    */ 
/* 26 */     double S = 0.9D;
/* 27 */     double B = 0.9D;
/* 28 */     return Color.getHSBColor((float)H, (float)S, (float)B);
/*    */   }
/*    */ 
/*    */   double getshootingAngle(int OffsetBin) {
/* 32 */     double factor = (2.0D * OffsetBin - 47.0D - 1.0D) / 
/* 33 */       46.0D;
/* 34 */     double offsetAngle = factor * 
/* 35 */       this.enemy.maxEscapeAngle(this.bulletVelocity * this.lateralDirection);
/* 36 */     double shootingAngle = offsetAngle + this.directAngle;
/* 37 */     return shootingAngle;
/*    */   }
/*    */ 
/*    */   private Stroke getStroke(double min, double d, double max) {
/* 41 */     double range = max - min;
/* 42 */     double val = d - min;
/* 43 */     return new BasicStroke((float)(val / range) * 5.0F);
/*    */   }
/*    */ 
/*    */   public void onPaint(Graphics2D g)
/*    */   {
/* 48 */     double[] curStats = getEnemyCurrentStatus();
/* 49 */     g.setStroke(new BasicStroke());
/* 50 */     g.setColor(Color.DARK_GRAY);
/* 51 */     double max = 4.9E-324D;
/* 52 */     double min = 1.7976931348623157E+308D;
/* 53 */     for (int j = 0; j < 47; j++) {
/* 54 */       if (curStats[j] < min)
/* 55 */         min = curStats[j];
/* 56 */       else if (curStats[j] > max) {
/* 57 */         max = curStats[j];
/*    */       }
/*    */     }
/* 60 */     FieldPoint temp1 = new FieldPoint();
/* 61 */     temp1.setLocation(this.fireLocation.project(this.distanceTraveled - 2.0D * 
/* 62 */       this.bulletVelocity, getshootingAngle(0)));
/* 63 */     FieldPoint temp0 = this.fireLocation.project(this.distanceTraveled, this.directAngle);
/* 64 */     g.drawLine((int)this.fireLocation.x, (int)this.fireLocation.y, (int)temp0.x, 
/* 65 */       (int)temp0.y);
/* 66 */     for (int j = 1; j < 47; j++) {
/* 67 */       temp0.setLocation(temp1);
/* 68 */       temp1.setLocation(this.fireLocation.project(this.distanceTraveled - 2.0D * 
/* 69 */         this.bulletVelocity, getshootingAngle(j)));
/* 70 */       g.setStroke(getStroke(min, curStats[j], max));
/* 71 */       g.setColor(getColor(min, curStats[j], max));
/* 72 */       g.drawLine((int)temp0.x, (int)temp0.y, (int)temp1.x, (int)temp1.y);
/* 73 */       g.fillOval((int)temp1.x - 1, (int)temp1.y - 1, 2, 2);
/*    */     }
/*    */   }
/*    */ 
/*    */   public double[] getEnemyCurrentStatus()
/*    */   {
/* 79 */     return this.enemy.getEnemyGunCurrentStatus(Math.abs(this.mineRobotVelocity), 
/* 80 */       Math.abs(this.directAngle), this.distanceToEnemyPosition);
/*    */   }
/*    */ 
/*    */   public void logHit(FieldPoint hitPoint) {
/* 84 */     this.enemy.logHit(Math.abs(this.mineRobotVelocity), this.distanceToEnemyPosition, 
/* 85 */       getFactorIndex(hitPoint));
/*    */   }
/*    */ }

/* Location:           E:\naval-robocode\sample\gre.svman4.Morfeas_1.4.jar
 * Qualified Name:     gre.svman4.EnemyWaveBullet
 * JD-Core Version:    0.6.2
 */