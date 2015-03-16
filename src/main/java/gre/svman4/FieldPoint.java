/*    */ package gre.svman4;
/*    */ 
/*    */ import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Double;
/*    */ 
/*    */ class FieldPoint extends Point2D.Double
/*    */ {
/*    */   private static final long serialVersionUID = 6797587839090734395L;
/*    */ 
/*    */   public FieldPoint()
/*    */   {
/*    */   }
/*    */ 
/*    */   public FieldPoint getMeanPoint(FieldPoint point)
/*    */   {
/* 20 */     return new FieldPoint((this.x + point.x) / 2.0D, (this.y + point.y) / 2.0D);
/*    */   }
/*    */ 
/*    */   public FieldPoint(double x, double y) {
/* 24 */     super(x, y);
/*    */   }
/*    */ 
/*    */   public FieldPoint(FieldPoint point)
/*    */   {
/* 29 */     super(point.x, point.y);
/*    */   }
/*    */ 
/*    */   public double getAngleTo(FieldPoint point)
/*    */   {
/* 41 */     return getAngleFromSides(point.x - this.x, point.y - this.y);
/*    */   }
/*    */ 
/*    */   public static double getAngleFromSides(double x, double y)
/*    */   {
/* 54 */     double angle = Math.atan2(x, y);
/* 55 */     while (angle < 0.0D) {
/* 56 */       angle += 6.283185307179586D;
/*    */     }
/* 58 */     while (angle >= 6.283185307179586D) {
/* 59 */       angle -= 6.283185307179586D;
/*    */     }
/* 61 */     return angle;
/*    */   }
/*    */ 
/*    */   public FieldPoint project(double distance, double angle)
/*    */   {
/* 69 */     return new FieldPoint(this.x + distance * Math.sin(angle), this.y + distance * 
/* 70 */       Math.cos(angle));
/*    */   }
/*    */ }

/* Location:           E:\naval-robocode\sample\gre.svman4.Morfeas_1.4.jar
 * Qualified Name:     gre.svman4.FieldPoint
 * JD-Core Version:    0.6.2
 */