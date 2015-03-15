/*    */ package dmh.robocode.data;
/*    */ 
/*    */ import dmh.robocode.utils.ConvertStatistic;
/*    */ import robocode.util.Utils;
/*    */ 
/*    */ public class Movement
/*    */ {
/*    */   private short relativeBearingTimesTen;
/*    */   private short distanceTimesTen;
/*    */ 
/*    */   public Movement(double relativeBearing, double distance)
/*    */   {
/* 36 */     this.relativeBearingTimesTen = ConvertStatistic.makeShort(Utils.normalRelativeAngleDegrees(relativeBearing) * 10.0D);
/* 37 */     this.distanceTimesTen = ConvertStatistic.makeShort(distance * 10.0D);
/*    */   }
/*    */ 
/*    */   public int hashCode()
/*    */   {
/* 43 */     int prime = 31;
/* 44 */     int result = 1;
/* 45 */     result = 31 * result + this.distanceTimesTen;
/* 46 */     result = 31 * result + this.relativeBearingTimesTen;
/* 47 */     return result;
/*    */   }
/*    */ 
/*    */   public boolean equals(Object obj)
/*    */   {
/* 53 */     if (this == obj)
/* 54 */       return true;
/* 55 */     if (obj == null)
/* 56 */       return false;
/* 57 */     if (getClass() != obj.getClass())
/* 58 */       return false;
/* 59 */     Movement other = (Movement)obj;
/* 60 */     if (this.distanceTimesTen != other.distanceTimesTen)
/* 61 */       return false;
/* 62 */     if (this.relativeBearingTimesTen != other.relativeBearingTimesTen)
/* 63 */       return false;
/* 64 */     return true;
/*    */   }
/*    */ 
/*    */   public double getRelativeBearing() {
/* 68 */     return this.relativeBearingTimesTen / 10.0D;
/*    */   }
/*    */ 
/*    */   public double getDistance() {
/* 72 */     return this.distanceTimesTen / 10.0D;
/*    */   }
/*    */ 
/*    */   public String getDebugString() {
/* 76 */     return "[ " + this.relativeBearingTimesTen / 10.0D + ", " + this.distanceTimesTen / 10.0D + " ]";
/*    */   }
/*    */ }

/* Location:           E:\naval-robocode\sample\dmh.robocode.robot.BlackDeathMk7_7.0.jar
 * Qualified Name:     dmh.robocode.data.Movement
 * JD-Core Version:    0.6.2
 */