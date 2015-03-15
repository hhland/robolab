/*    */ package dmh.robocode.data;
/*    */ 
/*    */ public class RoundSummary
/*    */ {
/*  4 */   private int killed = 0;
/*  5 */   private int placing = 0;
/*  6 */   private long endTime = 0L;
/*  7 */   private double energyAtTime100 = 0.0D;
/*  8 */   private double energyAtTime200 = 0.0D;
/*  9 */   private double finalEnergy = 0.0D;
/*    */   private Location startingLocation;
/* 11 */   private int hitsLanded = 0;
/* 12 */   private double damageCaused = 0.0D;
/* 13 */   private int hitsTaken = 0;
/* 14 */   private double damageReceived = 0.0D;
/*    */ 
/*    */   public void bulletHit(double damage)
/*    */   {
/* 20 */     this.hitsLanded += 1;
/* 21 */     this.damageCaused += damage;
/*    */   }
/*    */ 
/*    */   public void gotHit(double damage) {
/* 25 */     this.hitsTaken += 1;
/* 26 */     this.damageReceived += damage;
/*    */   }
/*    */ 
/*    */   public void setEnergyAtTime100(double energyAtTime100) {
/* 30 */     this.energyAtTime100 = energyAtTime100;
/*    */   }
/*    */ 
/*    */   public void setEnergyAtTime200(double energyAtTime200) {
/* 34 */     this.energyAtTime200 = energyAtTime200;
/*    */   }
/*    */ 
/*    */   public void setFinalEnergy(double finalEnergy) {
/* 38 */     this.finalEnergy = finalEnergy;
/*    */   }
/*    */ 
/*    */   public void setStartingLocation(Location startingLocation) {
/* 42 */     this.startingLocation = startingLocation;
/*    */   }
/*    */ 
/*    */   public void setEndTime(long endTime) {
/* 46 */     this.endTime = endTime;
/*    */   }
/*    */ 
/*    */   public void setPlacing(int placing) {
/* 50 */     this.placing = placing;
/*    */   }
/*    */ 
/*    */   public void killedRobot() {
/* 54 */     this.killed += 1;
/*    */   }
/*    */ 
/*    */   public String getResultWithTabs() {
/* 58 */     return this.placing + "\t" + 
/* 59 */       this.endTime + "\t" + 
/* 60 */       this.killed + "\t" + 
/* 61 */       this.hitsLanded + "\t" + 
/* 62 */       (int)this.damageCaused + "\t" + 
/* 63 */       this.hitsTaken + "\t" + 
/* 64 */       (int)this.damageReceived + "\t" + 
/* 65 */       (int)this.energyAtTime100 + "\t" + 
/* 66 */       (int)this.energyAtTime200 + "\t" + 
/* 67 */       (int)this.finalEnergy + "\t" + 
/* 68 */       (int)this.startingLocation.getX() + "\t" + 
/* 69 */       (int)this.startingLocation.getY();
/*    */   }
/*    */ 
/*    */   public static String getResultHeadingWithTabs() {
/* 73 */     return "Placing\tTime\tKilled\tHits Landed\tDamage Caused\tHits Taken\tDamage Received\tEnergy at 100\tEnergy at 200\tFinal Energy\tStart X\tStart Y";
/*    */   }
/*    */ }

/* Location:           E:\naval-robocode\sample\dmh.robocode.robot.BlackDeathMk7_7.0.jar
 * Qualified Name:     dmh.robocode.data.RoundSummary
 * JD-Core Version:    0.6.2
 */