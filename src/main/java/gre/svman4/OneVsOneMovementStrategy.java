/*    */ package gre.svman4;
/*    */ 
/*    */ import java.awt.geom.Rectangle2D;
/*    */ 
/*    */ public abstract class OneVsOneMovementStrategy extends Movement
/*    */ {
/*    */   protected Rectangle2D movingArea;
/*    */   protected EnemyState target;
/*    */ 
/*    */   public OneVsOneMovementStrategy(RobotState me, Rectangle2D movingArea, EnemyState target)
/*    */   {
/* 16 */     super(me);
/* 17 */     setMovingArea(movingArea);
/* 18 */     setTarget(target);
/*    */   }
/*    */ 
/*    */   public void setMovingArea(Rectangle2D area) {
/* 22 */     this.movingArea = area;
/*    */   }
/*    */ 
/*    */   public void setTarget(EnemyState target) {
/* 26 */     this.target = target;
/*    */   }
/*    */ 
/*    */   public double wallSmoothing(FieldPoint botLocation, double angle, int orientation)
/*    */   {
/* 33 */     while (!this.movingArea.contains(botLocation.project(5.0D * Morfeas.WALL_MARGIN, 
/* 33 */       angle))) {
/* 34 */       angle += orientation * 0.05D;
/*    */     }
/* 36 */     return angle;
/*    */   }
/*    */ }

/* Location:           E:\naval-robocode\sample\gre.svman4.Morfeas_1.4.jar
 * Qualified Name:     gre.svman4.OneVsOneMovementStrategy
 * JD-Core Version:    0.6.2
 */