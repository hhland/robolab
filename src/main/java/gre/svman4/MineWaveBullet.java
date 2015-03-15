/*     */ package gre.svman4;
/*     */ 
/*     */ import java.awt.BasicStroke;
/*     */ import java.awt.Color;
/*     */ import java.awt.Graphics2D;
/*     */ import java.awt.Stroke;
/*     */ import robocode.util.Utils;
/*     */ 
/*     */ public class MineWaveBullet extends WaveBullet
/*     */ {
/*  17 */   public boolean paint = false;
/*     */   public double enemyVelocity;
/*     */   public double lastEnemyVelocity;
/*     */ 
/*     */   public void onPaint(Graphics2D g)
/*     */   {
/*  20 */     if (!this.paint)
/*  21 */       return;
/*  22 */     double[] curStats = getCurrentStatus();
/*  23 */     g.setStroke(new BasicStroke());
/*  24 */     g.setColor(Color.BLACK);
/*  25 */     double max = 4.9E-324D;
/*  26 */     double min = 1.7976931348623157E+308D;
/*  27 */     for (int j = 0; j < 42; j++) {
/*  28 */       if (curStats[j] < min)
/*  29 */         min = curStats[j];
/*  30 */       else if (curStats[j] > max) {
/*  31 */         max = curStats[j];
/*     */       }
/*     */     }
/*  34 */     FieldPoint temp1 = this.fireLocation.project(this.distanceTraveled, 
/*  35 */       getShootingAngle(0));
/*  36 */     FieldPoint temp0 = this.fireLocation.project(this.distanceTraveled, this.directAngle);
/*  37 */     for (int j = 1; j < 42; j++) {
/*  38 */       temp0.setLocation(temp1);
/*  39 */       temp1.setLocation(this.fireLocation.project(this.distanceTraveled, 
/*  40 */         getShootingAngle(j)));
/*  41 */       g.setStroke(getStroke(min, curStats[j], max));
/*  42 */       g.setColor(getColor(min, curStats[j], max));
/*  43 */       g.drawLine((int)temp0.x, (int)temp0.y, (int)temp1.x, (int)temp1.y);
/*  44 */       g.fillOval((int)temp1.x - 1, (int)temp1.y - 1, 2, 2);
/*     */     }
/*     */   }
/*     */ 
/*     */   private Stroke getStroke(double min, double d, double max)
/*     */   {
/*  50 */     double range = max - min;
/*  51 */     double val = d - min;
/*  52 */     return new BasicStroke((float)(val / range) * 5.0F);
/*     */   }
/*     */ 
/*     */   double getShootingAngle(int OffsetBin)
/*     */   {
/*  57 */     double factor = (2.0D * OffsetBin - 42.0D - 1.0D) / 
/*  58 */       41.0D;
/*  59 */     double offsetAngle = factor * 
/*  60 */       this.enemy.maxEscapeAngle(this.bulletVelocity * this.lateralDirection);
/*  61 */     double shootingAngle = offsetAngle + this.directAngle;
/*  62 */     return shootingAngle;
/*     */   }
/*     */ 
/*     */   private Color getColor(double lower, double val, double higher)
/*     */   {
/*  71 */     double range = higher - lower;
/*  72 */     double value = Math.min(range, Math.max(val - lower, 0.0D));
/*  73 */     double H = value / range * 0.4D;
/*     */ 
/*  75 */     double S = 0.9D;
/*  76 */     double B = 0.9D;
/*  77 */     return Color.getHSBColor((float)H, (float)S, (float)B);
/*     */   }
/*     */ 
/*     */   private double[] getCurrentStatus()
/*     */   {
/*  86 */     return this.enemy.getGunCurrentStatus(this.distanceToEnemyPosition, this.enemyVelocity, 
/*  87 */       this.lastEnemyVelocity);
/*     */   }
/*     */ 
/*     */   public int getFactorIndex(FieldPoint targetLocation) {
/*  91 */     double offsetAngle = this.fireLocation.getAngleTo(targetLocation) - this.directAngle;
/*  92 */     double factor = Utils.normalRelativeAngle(offsetAngle) / 
/*  93 */       this.enemy.maxEscapeAngle(this.bulletVelocity) * this.lateralDirection;
/*     */ 
/*  95 */     return (int)limit(0.0D, factor * 20.0D + 
/*  96 */       20.0D, 41.0D);
/*     */   }
/*     */ 
/*     */   public void logHit()
/*     */   {
/* 104 */     logHit(this.enemy);
/*     */   }
/*     */ 
/*     */   public void logHit(FieldPoint hitPoint)
/*     */   {
/* 109 */     this.enemy.logGunHit(this.distanceToEnemyPosition, this.enemyVelocity, this.lastEnemyVelocity, 
/* 110 */       getFactorIndex(this.enemy));
/*     */   }
/*     */ }

/* Location:           E:\naval-robocode\sample\gre.svman4.Morfeas_1.4.jar
 * Qualified Name:     gre.svman4.MineWaveBullet
 * JD-Core Version:    0.6.2
 */