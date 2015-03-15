/*    */ package dmh.robocode.gunner.aiming;
/*    */ 
/*    */ import dmh.robocode.data.Location;
/*    */ import dmh.robocode.enemy.EnemyRobot;
/*    */ import dmh.robocode.gunner.simulator.EnemySimulator;
/*    */ import dmh.robocode.robot.CommandBasedRobot;
/*    */ import dmh.robocode.utils.Geometry;
/*    */ 
/*    */ public abstract class CalibratedAimingStrategyUsingEnemySimulator extends CalibratedAimingStrategy
/*    */ {
/*    */   private static final double maxShootingRange = 1200.0D;
/*    */ 
/*    */   public CalibratedAimingStrategyUsingEnemySimulator(CommandBasedRobot myRobot, EnemyRobot enemy, boolean isLearningAllowed)
/*    */   {
/* 14 */     super(myRobot, enemy, isLearningAllowed);
/*    */   }
/*    */ 
/*    */   protected Location getSimulatorIntercept(EnemySimulator simulator, double bulletSpeed)
/*    */   {
/* 21 */     Location lastLocationOnBattlefield = null;
/* 22 */     long turn = 0L;
/*    */ 
/* 24 */     while (turn < 1200.0D / bulletSpeed) {
/* 25 */       turn += 1L;
/* 26 */       simulator.takeOneTurn();
/*    */ 
/* 29 */       if (simulator.getLocation().isOnBattlefield(getMyRobot().getBattleFieldWidth(), 
/* 29 */         getMyRobot().getBattleFieldHeight()))
/* 30 */         lastLocationOnBattlefield = simulator.getLocation();
/*    */       else {
/* 32 */         return lastLocationOnBattlefield;
/*    */       }
/*    */ 
/* 35 */       double targetDistance = Geometry.getDistanceBetweenLocations(simulator.getLocation(), getMyRobot().getLocation());
/* 36 */       double travelDistance = turn * bulletSpeed;
/*    */ 
/* 38 */       if ((travelDistance - bulletSpeed < targetDistance) && 
/* 39 */         (travelDistance + bulletSpeed > targetDistance)) {
/* 40 */         return simulator.getLocation();
/*    */       }
/*    */     }
/* 43 */     return null;
/*    */   }
/*    */ }

/* Location:           E:\naval-robocode\sample\dmh.robocode.robot.BlackDeathMk7_7.0.jar
 * Qualified Name:     dmh.robocode.gunner.aiming.CalibratedAimingStrategyUsingEnemySimulator
 * JD-Core Version:    0.6.2
 */