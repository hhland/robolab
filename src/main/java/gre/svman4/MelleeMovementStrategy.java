/*    */ package gre.svman4;
/*    */ 
/*    */ import java.awt.geom.Rectangle2D;
/*    */ import java.util.HashMap;
/*    */ 
/*    */ public abstract class MelleeMovementStrategy extends Movement
/*    */ {
/*    */   protected HashMap<String, EnemyState> enemies;
/*    */   protected Rectangle2D movingPlace;
/*    */ 
/*    */   public MelleeMovementStrategy(RobotState me, Rectangle2D movingArea, HashMap<String, EnemyState> enemies)
/*    */   {
/* 18 */     super(me);
/* 19 */     setEnemies(enemies);
/* 20 */     setMovingArea(movingArea);
/*    */   }
/*    */   private void setMovingArea(Rectangle2D movingArea) {
/* 23 */     this.movingPlace = movingArea;
/*    */   }
/*    */ 
/*    */   private void setEnemies(HashMap<String, EnemyState> enemies) {
/* 27 */     this.enemies = enemies;
/*    */   }
/*    */ }

/* Location:           E:\naval-robocode\sample\gre.svman4.Morfeas_1.4.jar
 * Qualified Name:     gre.svman4.MelleeMovementStrategy
 * JD-Core Version:    0.6.2
 */