/*    */ package dmh.robocode.gunner.aiming;
/*    */ 
/*    */ import dmh.robocode.data.DynamicMovementSequence;
/*    */ import dmh.robocode.data.Location;
/*    */ import dmh.robocode.enemy.EnemyRobot;
/*    */ import dmh.robocode.gunner.simulator.MovementSequenceReplayEnemySimulator;
/*    */ import dmh.robocode.robot.CommandBasedRobot;
/*    */ import java.awt.Color;
/*    */ 
/*    */ public class AimByMovementSequenceReplay extends CalibratedAimingStrategyUsingEnemySimulator
/*    */ {
/*    */   boolean isLearningAllowed;
/*    */ 
/*    */   public AimByMovementSequenceReplay(CommandBasedRobot myRobot, EnemyRobot enemy, boolean isLearningAllowed)
/*    */   {
/* 16 */     super(myRobot, enemy, isLearningAllowed);
/* 17 */     this.isLearningAllowed = isLearningAllowed;
/*    */   }
/*    */ 
/*    */   public Color getDebugColour()
/*    */   {
/* 22 */     return Color.MAGENTA;
/*    */   }
/*    */ 
/*    */   public double getEstimatedSuccessOfShotUsingRules(double bulletSpeed)
/*    */   {
/* 27 */     if (this.isLearningAllowed) {
/* 28 */       return getDefaultEstimatedSuccess(bulletSpeed);
/*    */     }
/* 30 */     return 0.0D;
/*    */   }
/*    */ 
/*    */   public Location getTargetForShot(double bulletSpeed)
/*    */   {
/* 37 */     if (!this.isLearningAllowed) {
/* 38 */       return null;
/*    */     }
/*    */ 
/* 41 */     DynamicMovementSequence currentMovements = getEnemy().getCurrentMovementSequence();
/*    */ 
/* 43 */     if (currentMovements.areUpToDate(getMyRobot().getTime()))
/*    */     {
/* 45 */       MovementSequenceReplayEnemySimulator simulator = new MovementSequenceReplayEnemySimulator(getEnemy());
/* 46 */       return getSimulatorIntercept(simulator, bulletSpeed);
/*    */     }
/* 48 */     return null;
/*    */   }
/*    */ }

/* Location:           E:\naval-robocode\sample\dmh.robocode.robot.BlackDeathMk7_7.0.jar
 * Qualified Name:     dmh.robocode.gunner.aiming.AimByMovementSequenceReplay
 * JD-Core Version:    0.6.2
 */