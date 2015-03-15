/*    */ package dmh.robocode.data;
/*    */ 
/*    */ public class AverageLocation
/*    */ {
/*    */   private double weightingIncreaseFactor;
/*  6 */   double x = 0.0D;
/*  7 */   double y = 0.0D;
/*  8 */   double totalWeight = 0.0D;
/*  9 */   double nextWeight = 1.0D;
/*    */ 
/*    */   public AverageLocation(double weightingIncreaseFactor) {
/* 12 */     this.weightingIncreaseFactor = weightingIncreaseFactor;
/*    */   }
/*    */ 
/*    */   public void recordLocation(Location location) {
/* 16 */     this.x = ((this.totalWeight * this.x + this.nextWeight * location.getX()) / (this.totalWeight + this.nextWeight));
/* 17 */     this.y = ((this.totalWeight * this.y + this.nextWeight * location.getY()) / (this.totalWeight + this.nextWeight));
/* 18 */     this.totalWeight += this.nextWeight;
/* 19 */     this.nextWeight *= this.weightingIncreaseFactor;
/*    */   }
/*    */ 
/*    */   public Location getAverageLocation() {
/* 23 */     if (this.totalWeight > 0.0D) {
/* 24 */       return new Location(this.x, this.y);
/*    */     }
/* 26 */     return null;
/*    */   }
/*    */ }

/* Location:           E:\naval-robocode\sample\dmh.robocode.robot.BlackDeathMk7_7.0.jar
 * Qualified Name:     dmh.robocode.data.AverageLocation
 * JD-Core Version:    0.6.2
 */