/*    */ package dmh.robocode.utils;
/*    */ 
/*    */ import robocode.util.Utils;
/*    */ 
/*    */ public class BearingSelector
/*    */ {
/*    */   int granularity;
/*    */   double[] highScores;
/*    */   double[] lowScores;
/*    */ 
/*    */   public BearingSelector(int granularity)
/*    */   {
/* 12 */     this.granularity = granularity;
/* 13 */     this.highScores = new double[360 / granularity];
/* 14 */     this.lowScores = new double[360 / granularity];
/*    */   }
/*    */ 
/*    */   public double getHighScoreBearing() {
/* 18 */     int highIndex = 0;
/* 19 */     double highScore = this.highScores[highIndex];
/*    */ 
/* 21 */     for (int i = 1; i < this.highScores.length; i++) {
/* 22 */       if (this.highScores[i] > highScore) {
/* 23 */         highScore = this.highScores[i];
/* 24 */         highIndex = i;
/*    */       }
/*    */     }
/* 27 */     return highIndex * this.granularity;
/*    */   }
/*    */ 
/*    */   public double getLowScoreBearing() {
/* 31 */     int lowIndex = 0;
/* 32 */     double lowScore = this.lowScores[lowIndex];
/*    */ 
/* 34 */     for (int i = 1; i < this.lowScores.length; i++) {
/* 35 */       if (this.lowScores[i] < lowScore) {
/* 36 */         lowScore = this.lowScores[i];
/* 37 */         lowIndex = i;
/*    */       }
/*    */     }
/* 40 */     return lowIndex * this.granularity;
/*    */   }
/*    */ 
/*    */   public void add(double bearing, double score)
/*    */   {
/* 45 */     int bearingIndex = fixScoreIndex((int)Math.round(Utils.normalAbsoluteAngleDegrees(bearing) / this.granularity));
/*    */ 
/* 47 */     double highScore = score;
/* 48 */     double lowScore = score;
/* 49 */     double highScoreReduction = score / 100.0D;
/* 50 */     double lowScoreReduction = score / 100.0D;
/* 51 */     this.highScores[bearingIndex] += highScore;
/* 52 */     this.lowScores[bearingIndex] += lowScore;
/*    */ 
/* 55 */     for (int i = 1; i < this.highScores.length / 2; i++) {
/* 56 */       highScore -= highScoreReduction;
/* 57 */       lowScore -= lowScoreReduction;
/* 58 */       highScoreReduction *= 1.1D;
/* 59 */       lowScoreReduction *= 0.9D;
/* 60 */       this.highScores[fixScoreIndex(bearingIndex + i)] += highScore;
/* 61 */       this.highScores[fixScoreIndex(bearingIndex - i)] += highScore;
/* 62 */       this.lowScores[fixScoreIndex(bearingIndex + i)] += lowScore;
/* 63 */       this.lowScores[fixScoreIndex(bearingIndex - i)] += lowScore;
/*    */     }
/* 65 */     this.highScores[fixScoreIndex(bearingIndex + this.highScores.length / 2)] += highScore - highScoreReduction;
/* 66 */     this.lowScores[fixScoreIndex(bearingIndex + this.highScores.length / 2)] += lowScore - lowScoreReduction;
/*    */   }
/*    */ 
/*    */   private int fixScoreIndex(int index)
/*    */   {
/* 81 */     if (index >= this.highScores.length)
/* 82 */       index -= this.highScores.length;
/* 83 */     else if (index < 0) {
/* 84 */       index += this.highScores.length;
/*    */     }
/* 86 */     return index;
/*    */   }
/*    */ }

/* Location:           E:\naval-robocode\sample\dmh.robocode.robot.BlackDeathMk7_7.0.jar
 * Qualified Name:     dmh.robocode.utils.BearingSelector
 * JD-Core Version:    0.6.2
 */