/*    */ package dmh.robocode.enemy;
/*    */ 
/*    */ import dmh.robocode.data.CauseAndEffect;
/*    */ import dmh.robocode.data.CompleteMovementSequence;
/*    */ import dmh.robocode.data.DynamicMovementSequence;
/*    */ 
/*    */ public class EnemyRobotMovementAnalyser
/*    */ {
/* 17 */   static int causeLength = 8;
/* 18 */   static int effectLength = 1;
/*    */   private CauseAndEffect<CompleteMovementSequence, CompleteMovementSequence> prototypeCauseAndEffectInfo;
/*    */ 
/*    */   public EnemyRobotMovementAnalyser()
/*    */   {
/* 23 */     this.prototypeCauseAndEffectInfo = new CauseAndEffect();
/*    */   }
/*    */ 
/*    */   void notifyNewMovement(DynamicMovementSequence currentMovementSequence, long timeNow)
/*    */   {
/* 32 */     if (currentMovementSequence.getLength() >= causeLength + effectLength) {
/* 33 */       CompleteMovementSequence cause = new CompleteMovementSequence(currentMovementSequence, 
/* 34 */         timeNow + 1L - causeLength - effectLength, timeNow - effectLength);
/* 35 */       CompleteMovementSequence effect = new CompleteMovementSequence(currentMovementSequence, 
/* 36 */         timeNow + 1L - effectLength, timeNow);
/* 37 */       this.prototypeCauseAndEffectInfo.record(cause, effect);
/*    */     }
/*    */   }
/*    */ 
/*    */   public void resetForNextRound() {
/* 42 */     this.prototypeCauseAndEffectInfo.purge(100);
/*    */   }
/*    */ 
/*    */   public Prediction predictNextMoves(DynamicMovementSequence allRecentMovements, long currentTime)
/*    */   {
/* 47 */     CompleteMovementSequence cause = new CompleteMovementSequence(allRecentMovements, currentTime + 1L - causeLength, currentTime);
/* 48 */     CompleteMovementSequence effect = (CompleteMovementSequence)this.prototypeCauseAndEffectInfo.getMostLikelyEffectOf(cause);
/* 49 */     double probability = this.prototypeCauseAndEffectInfo.getProbabilityOf(cause, effect);
/*    */ 
/* 52 */     return new Prediction(effect, probability);
/*    */   }
/*    */ 
/*    */   public class Prediction {
/*    */     private CompleteMovementSequence sequence;
/*    */     double probability;
/*    */ 
/*    */     public Prediction(CompleteMovementSequence sequence, double probability) {
/* 61 */       this.sequence = sequence;
/* 62 */       this.probability = probability;
/*    */     }
/*    */ 
/*    */     public double getProbability() {
/* 66 */       return this.probability;
/*    */     }
/*    */ 
/*    */     public CompleteMovementSequence getSequence() {
/* 70 */       return this.sequence;
/*    */     }
/*    */   }
/*    */ }

/* Location:           E:\naval-robocode\sample\dmh.robocode.robot.BlackDeathMk7_7.0.jar
 * Qualified Name:     dmh.robocode.enemy.EnemyRobotMovementAnalyser
 * JD-Core Version:    0.6.2
 */