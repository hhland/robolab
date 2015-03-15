/*    */ package gre.svman4;
/*    */ 
/*    */ import java.awt.geom.Line2D;
/*    */ import java.awt.geom.Line2D.Double;
/*    */ import robocode.util.Utils;
/*    */ 
/*    */ public class WaveBullet
/*    */ {
/*    */   public double distanceToEnemyPosition;
/*    */   public double mineRobotVelocity;
/*    */   double bulletVelocity;
/*    */   double directAngle;
/*    */   int lateralDirection;
/*    */   double distanceTraveled;
/*    */   EnemyState enemy;
/*    */   long fireTime;
/*    */   protected FieldPoint fireLocation;
/*    */ 
/*    */   public static double limit(double min, double value, double max)
/*    */   {
/* 13 */     return Math.max(min, Math.min(value, max));
/*    */   }
/*    */ 
/*    */   public double distanceFromCenterLine(FieldPoint point)
/*    */   {
/* 52 */     Line2D line = getShootingLine();
/* 53 */     return line.ptLineDist(point);
/*    */   }
/*    */ 
/*    */   public double distanceSqFromCenterLine(FieldPoint point) {
/* 57 */     Line2D line = getShootingLine();
/* 58 */     return line.ptLineDistSq(point);
/*    */   }
/*    */ 
/*    */   public int getFactorIndex(FieldPoint targetLocation) {
/* 62 */     double offsetAngle = this.fireLocation.getAngleTo(targetLocation) - this.directAngle;
/* 63 */     double factor = Utils.normalRelativeAngle(offsetAngle) / 
/* 64 */       this.enemy.maxEscapeAngle(this.bulletVelocity) * this.lateralDirection;
/*    */ 
/* 66 */     return (int)limit(0.0D, factor * 23.0D + 
/* 67 */       23.0D, 46.0D);
/*    */   }
/*    */ 
/*    */   public Line2D getShootingLine() {
/* 71 */     FieldPoint temp = this.fireLocation.project(this.distanceTraveled, this.directAngle);
/* 72 */     Line2D line = new Line2D.Double();
/* 73 */     line.setLine(this.fireLocation, temp);
/* 74 */     return line;
/*    */   }
/*    */ }

/* Location:           E:\naval-robocode\sample\gre.svman4.Morfeas_1.4.jar
 * Qualified Name:     gre.svman4.WaveBullet
 * JD-Core Version:    0.6.2
 */