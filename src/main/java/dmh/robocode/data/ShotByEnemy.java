/*    */ package dmh.robocode.data;
/*    */ 
/*    */ import dmh.robocode.enemy.EnemyRobot;
/*    */ import dmh.robocode.robot.CommandBasedRobot;
/*    */ import robocode.Bullet;
/*    */ import robocode.HitByBulletEvent;
/*    */ 
/*    */ public class ShotByEnemy extends Shot
/*    */ {
/*    */   public ShotByEnemy(CommandBasedRobot myRobot, EnemyRobot enemyRobot, HitByBulletEvent hitEvent)
/*    */   {
/* 34 */     super(myRobot, enemyRobot, hitEvent.getBullet().getPower());
/*    */   }
/*    */ }

/* Location:           E:\naval-robocode\sample\dmh.robocode.robot.BlackDeathMk7_7.0.jar
 * Qualified Name:     dmh.robocode.data.ShotByEnemy
 * JD-Core Version:    0.6.2
 */