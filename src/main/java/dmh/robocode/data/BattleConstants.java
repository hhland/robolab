/*    */ package dmh.robocode.data;
/*    */ 
/*    */ public class BattleConstants
/*    */ {
/*    */   private double battlefieldHeight;
/*    */   private double battlefieldWidth;
/*    */   private double robotHeight;
/*    */   private double robotWidth;
/*    */   private double gunCoolingRate;
/* 11 */   private static BattleConstants thisInstance = new BattleConstants();
/*    */ 
/*    */   public static BattleConstants getInstance()
/*    */   {
/* 16 */     return thisInstance;
/*    */   }
/*    */ 
/*    */   public void setBattlefieldHeight(double battlefieldHeight) {
/* 20 */     this.battlefieldHeight = battlefieldHeight;
/*    */   }
/*    */ 
/*    */   public void setBattlefieldWidth(double battlefieldWidth) {
/* 24 */     this.battlefieldWidth = battlefieldWidth;
/*    */   }
/*    */ 
/*    */   public void setRobotHeight(double robotHeight) {
/* 28 */     this.robotHeight = robotHeight;
/*    */   }
/*    */ 
/*    */   public void setRobotWidth(double robotWidth) {
/* 32 */     this.robotWidth = robotWidth;
/*    */   }
/*    */ 
/*    */   public void setGunCoolingRate(double gunCoolingRate) {
/* 36 */     this.gunCoolingRate = gunCoolingRate;
/*    */   }
/*    */ 
/*    */   public double getBattlefieldHeight() {
/* 40 */     return this.battlefieldHeight;
/*    */   }
/*    */ 
/*    */   public double getBattlefieldWidth() {
/* 44 */     return this.battlefieldWidth;
/*    */   }
/*    */ 
/*    */   public double getRobotHeight() {
/* 48 */     return this.robotHeight;
/*    */   }
/*    */ 
/*    */   public double getRobotWidth() {
/* 52 */     return this.robotWidth;
/*    */   }
/*    */ 
/*    */   public double getGunCoolingRate() {
/* 56 */     return this.gunCoolingRate;
/*    */   }
/*    */ }

/* Location:           E:\naval-robocode\sample\dmh.robocode.robot.BlackDeathMk7_7.0.jar
 * Qualified Name:     dmh.robocode.data.BattleConstants
 * JD-Core Version:    0.6.2
 */