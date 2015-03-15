/*    */ package dmh.robocode.gunner.simulator;
/*    */ 
/*    */ import dmh.robocode.data.CompleteMovementSequence;
/*    */ import dmh.robocode.data.DynamicMovementSequence;
/*    */ import dmh.robocode.data.Location;
/*    */ import dmh.robocode.data.Movement;
/*    */ import dmh.robocode.data.RadarObservation;
/*    */ import dmh.robocode.enemy.EnemyRobot;
/*    */ import dmh.robocode.enemy.EnemyRobotMovementAnalyser;
/*    */ import dmh.robocode.enemy.EnemyRobotMovementAnalyser.Prediction;
/*    */ import dmh.robocode.utils.Geometry;
/*    */ 
/*    */ public class MovementSequenceReplayEnemySimulator
/*    */   implements EnemySimulator
/*    */ {
/*    */   private Location location;
/*    */   long currentTime;
/*    */   private DynamicMovementSequence allRecentMovements;
/*    */   private CompleteMovementSequence replayMovements;
/*    */   private EnemyRobotMovementAnalyser enemyMovementAnalyser;
/*    */   private double heading;
/*    */   private double velocity;
/* 21 */   private int replayIndex = 0;
/* 22 */   private double probabilityTotal = 0.0D;
/* 23 */   private int probabilityCount = 0;
/*    */ 
/*    */   public MovementSequenceReplayEnemySimulator(EnemyRobot enemy) {
/* 26 */     this.location = enemy.getLatestRadarObservation().getLocation();
/* 27 */     this.currentTime = enemy.getLatestRadarObservation().getTimeSeen();
/* 28 */     this.allRecentMovements = enemy.getMovementSequenceCloneSinceTime(this.currentTime - 20L);
/* 29 */     this.replayMovements = null;
/* 30 */     this.enemyMovementAnalyser = enemy.getMovementAnalyser();
/* 31 */     this.heading = enemy.getLatestRadarObservation().getHeading();
/* 32 */     this.velocity = enemy.getLatestRadarObservation().getVelocity();
/*    */   }
/*    */ 
/*    */   public Location getLocation()
/*    */   {
/* 42 */     return this.location;
/*    */   }
/*    */ 
/*    */   public double getProbability() {
/* 46 */     return this.probabilityTotal / this.probabilityCount;
/*    */   }
/*    */ 
/*    */   public void takeOneTurn()
/*    */   {
/* 51 */     if ((this.replayMovements == null) || (this.replayIndex >= this.replayMovements.getLength())) {
/* 52 */       chooseNextReplaySequence();
/*    */     }
/* 54 */     double relativeBearing = 0.0D;
/* 55 */     if (this.replayMovements != null) {
/* 56 */       relativeBearing = this.replayMovements.getMovement(this.replayIndex).getRelativeBearing();
/* 57 */       this.heading += relativeBearing;
/* 58 */       this.velocity = this.replayMovements.getMovement(this.replayIndex).getDistance();
/* 59 */       this.replayIndex += 1;
/*    */     }
/* 61 */     this.location = Geometry.getLocationAtBearing(this.location, this.heading, this.velocity);
/* 62 */     this.currentTime += 1L;
/* 63 */     this.allRecentMovements.add(new Movement(relativeBearing, this.velocity), this.currentTime);
/*    */   }
/*    */ 
/*    */   private void chooseNextReplaySequence()
/*    */   {
/* 79 */     EnemyRobotMovementAnalyser.Prediction prediction = this.enemyMovementAnalyser.predictNextMoves(this.allRecentMovements, this.currentTime);
/*    */ 
/* 81 */     this.replayMovements = prediction.getSequence();
/* 82 */     this.replayIndex = 0;
/*    */ 
/* 84 */     if (this.replayMovements != null) {
/* 85 */       this.probabilityTotal += prediction.getProbability();
/*    */     }
/* 87 */     this.probabilityCount += 1;
/*    */   }
/*    */ }

/* Location:           E:\naval-robocode\sample\dmh.robocode.robot.BlackDeathMk7_7.0.jar
 * Qualified Name:     dmh.robocode.gunner.simulator.MovementSequenceReplayEnemySimulator
 * JD-Core Version:    0.6.2
 */