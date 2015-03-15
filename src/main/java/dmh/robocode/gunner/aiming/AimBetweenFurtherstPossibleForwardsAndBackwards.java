/*    */ package dmh.robocode.gunner.aiming;
/*    */ 
/*    */ import dmh.robocode.data.BattleConstants;
/*    */ import dmh.robocode.data.Location;
/*    */ import dmh.robocode.data.RadarObservation;
/*    */ import dmh.robocode.enemy.EnemyRobot;
/*    */ import dmh.robocode.gunner.simulator.EnemySimulator;
/*    */ import dmh.robocode.gunner.simulator.SteadyChangeEnemySimulatorImproved;
/*    */ import dmh.robocode.robot.CommandBasedRobot;
/*    */ import dmh.robocode.utils.Geometry;
/*    */ import java.awt.Color;
/*    */ 
/*    */ public class AimBetweenFurtherstPossibleForwardsAndBackwards extends CalibratedAimingStrategyUsingEnemySimulator
/*    */ {
/*    */   private Location targetLocation;
/*    */   long timeOfLastCalculation;
/*    */   int roundOfLastCalculation;
/*    */ 
/*    */   public AimBetweenFurtherstPossibleForwardsAndBackwards(CommandBasedRobot myRobot, EnemyRobot enemy, boolean isLearningAllowed)
/*    */   {
/* 22 */     super(myRobot, enemy, isLearningAllowed);
/* 23 */     this.timeOfLastCalculation = (myRobot.getTime() - 1L);
/* 24 */     this.roundOfLastCalculation = (myRobot.getRoundNum() - 1);
/* 25 */     this.targetLocation = null;
/*    */   }
/*    */ 
/*    */   public Color getDebugColour()
/*    */   {
/* 30 */     return Color.MAGENTA;
/*    */   }
/*    */ 
/*    */   public double getEstimatedSuccessOfShotUsingRules(double bulletSpeed)
/*    */   {
/* 35 */     calculateExpectedTarget(bulletSpeed);
/* 36 */     if (this.targetLocation == null) {
/* 37 */       return 0.0D;
/*    */     }
/* 39 */     return 100.0D;
/*    */   }
/*    */ 
/*    */   public Location getTargetForShot(double bulletSpeed)
/*    */   {
/* 45 */     calculateExpectedTarget(bulletSpeed);
/* 46 */     return this.targetLocation;
/*    */   }
/*    */ 
/*    */   private void calculateExpectedTarget(double bulletSpeed) {
/* 50 */     if ((this.timeOfLastCalculation != getMyRobot().getTime()) || (this.roundOfLastCalculation != getMyRobot().getRoundNum())) {
/* 51 */       RadarObservation latestObservation = getEnemy().getLatestRadarObservation();
/* 52 */       if ((latestObservation != null) && (latestObservation.getTimeSeen() == getMyRobot().getTime())) {
/* 53 */         Location maxForwardsLocation = getSimulatorResult(latestObservation, 8.0D, bulletSpeed);
/* 54 */         Location maxBackwardsLocation = getSimulatorResult(latestObservation, -8.0D, bulletSpeed);
/* 55 */         if ((maxForwardsLocation != null) && (maxBackwardsLocation != null)) {
/* 56 */           double difference = Geometry.getDistanceBetweenLocations(maxForwardsLocation, maxBackwardsLocation);
/* 57 */           if (difference <= Math.min(BattleConstants.getInstance().getRobotWidth(), BattleConstants.getInstance().getRobotHeight())) {
/* 58 */             this.timeOfLastCalculation = getMyRobot().getTime();
/* 59 */             this.roundOfLastCalculation = getMyRobot().getRoundNum();
/* 60 */             this.targetLocation = Location.getMidPoint(maxForwardsLocation, maxBackwardsLocation);
/*    */ 
/* 66 */             return;
/*    */           }
/*    */         }
/*    */       }
/* 70 */       this.targetLocation = null;
/*    */     }
/*    */   }
/*    */ 
/*    */   private Location getSimulatorResult(RadarObservation enemyObservation, double velocity, double bulletSpeed)
/*    */   {
/* 76 */     double rateOfTurn = 0.0D;
/* 77 */     double acceleration = velocity;
/*    */ 
/* 79 */     EnemySimulator simulator = new SteadyChangeEnemySimulatorImproved(enemyObservation.getLocation(), enemyObservation.getVelocity(), enemyObservation.getHeading(), acceleration, rateOfTurn);
/*    */ 
/* 81 */     return getSimulatorIntercept(simulator, bulletSpeed);
/*    */   }
/*    */ }

/* Location:           E:\naval-robocode\sample\dmh.robocode.robot.BlackDeathMk7_7.0.jar
 * Qualified Name:     dmh.robocode.gunner.aiming.AimBetweenFurtherstPossibleForwardsAndBackwards
 * JD-Core Version:    0.6.2
 */