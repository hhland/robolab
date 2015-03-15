/*    */ package dmh.robocode.gunner.aiming;
/*    */ 
/*    */ import dmh.robocode.data.DynamicMovementSequence;
/*    */ import dmh.robocode.data.Location;
/*    */ import dmh.robocode.data.Movement;
/*    */ import dmh.robocode.data.RadarObservation;
/*    */ import dmh.robocode.enemy.EnemyRobot;
/*    */ import dmh.robocode.gunner.simulator.EnemySimulator;
/*    */ import dmh.robocode.gunner.simulator.SteadyChangeEnemySimulator;
/*    */ import dmh.robocode.robot.CommandBasedRobot;
/*    */ import dmh.robocode.utils.Geometry;
/*    */ import java.awt.Color;
/*    */ 
/*    */ public class AimByRepeatingLastMove extends CalibratedAimingStrategyUsingEnemySimulator
/*    */ {
/*    */   private Movement expectedMove;
/*    */   private Location targetLocation;
/*    */   long timeOfLastCalculation;
/*    */   int roundOfLastCalculation;
/*    */ 
/*    */   public AimByRepeatingLastMove(CommandBasedRobot myRobot, EnemyRobot enemy, boolean isLearningAllowed)
/*    */   {
/* 23 */     super(myRobot, enemy, isLearningAllowed);
/* 24 */     this.timeOfLastCalculation = (myRobot.getTime() - 1L);
/* 25 */     this.roundOfLastCalculation = (myRobot.getRoundNum() - 1);
/* 26 */     this.targetLocation = null;
/*    */   }
/*    */ 
/*    */   public Color getDebugColour()
/*    */   {
/* 31 */     return Color.GREEN;
/*    */   }
/*    */ 
/*    */   public double getEstimatedSuccessOfShotUsingRules(double bulletSpeed)
/*    */   {
/* 36 */     calculateExpectedMoveAndTarget(bulletSpeed);
/* 37 */     if (this.targetLocation == null) {
/* 38 */       return 0.0D;
/*    */     }
/* 40 */     double distance = Geometry.getDistanceBetweenLocations(getMyRobot().getLocation(), this.targetLocation);
/* 41 */     int bulletTravelTime = (int)(distance / bulletSpeed);
/* 42 */     int consistentMoves = getEnemy().getCurrentMovementSequence().getConsistentMoveCount(bulletTravelTime);
/* 43 */     int expectedMoves = getEnemy().getCurrentMovementSequence().countSameMovement(bulletTravelTime, this.expectedMove);
/* 44 */     return (50 * consistentMoves + 50 * expectedMoves) / bulletTravelTime;
/*    */   }
/*    */ 
/*    */   public Location getTargetForShot(double bulletSpeed)
/*    */   {
/* 50 */     calculateExpectedMoveAndTarget(bulletSpeed);
/* 51 */     return this.targetLocation;
/*    */   }
/*    */ 
/*    */   private void calculateExpectedMoveAndTarget(double bulletSpeed) {
/* 55 */     if ((this.timeOfLastCalculation != getMyRobot().getTime()) || (this.roundOfLastCalculation != getMyRobot().getRoundNum())) {
/* 56 */       DynamicMovementSequence currentMovements = getEnemy().getCurrentMovementSequence();
/* 57 */       RadarObservation latestObservation = getEnemy().getLatestRadarObservation();
/* 58 */       RadarObservation previousObservation = getEnemy().getPreviousRadarObservation();
/*    */ 
/* 60 */       if ((currentMovements.areUpToDate(getMyRobot().getTime())) && (previousObservation != null))
/*    */       {
/* 62 */         double rateOfTurn = Geometry.getRelativeBearing(previousObservation.getHeading(), latestObservation.getHeading());
/* 63 */         double acceleration = latestObservation.getVelocity() - previousObservation.getVelocity();
/*    */ 
/* 65 */         EnemySimulator simulator = new SteadyChangeEnemySimulator(latestObservation.getLocation(), latestObservation.getVelocity(), latestObservation.getHeading(), acceleration, rateOfTurn);
/*    */ 
/* 67 */         this.targetLocation = getSimulatorIntercept(simulator, bulletSpeed);
/* 68 */         this.expectedMove = new Movement(rateOfTurn, latestObservation.getVelocity());
/*    */ 
/* 70 */         this.timeOfLastCalculation = getMyRobot().getTime();
/* 71 */         this.roundOfLastCalculation = getMyRobot().getRoundNum();
/* 72 */       } else if ((this.roundOfLastCalculation != getMyRobot().getRoundNum()) || (this.timeOfLastCalculation + 10L < getMyRobot().getTime())) {
/* 73 */         this.targetLocation = null;
/*    */       }
/*    */     }
/*    */   }
/*    */ }

/* Location:           E:\naval-robocode\sample\dmh.robocode.robot.BlackDeathMk7_7.0.jar
 * Qualified Name:     dmh.robocode.gunner.aiming.AimByRepeatingLastMove
 * JD-Core Version:    0.6.2
 */