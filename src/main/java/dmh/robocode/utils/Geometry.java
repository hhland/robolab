/*    */ package dmh.robocode.utils;
/*    */ 
/*    */ import dmh.robocode.data.Location;
/*    */ import robocode.util.Utils;
/*    */ 
/*    */ public class Geometry
/*    */ {
/*    */   public static Location getLocationAtBearing(Location start, double bearing, double distance)
/*    */   {
/* 16 */     double normalBearing = Utils.normalAbsoluteAngleDegrees(bearing);
/* 17 */     double radians = Math.toRadians(normalBearing);
/*    */ 
/* 19 */     return new Location(start.getX() + distance * Math.sin(radians), start.getY() + distance * Math.cos(radians));
/*    */   }
/*    */ 
/*    */   public static double getDistanceBetweenLocations(Location start, Location finish) {
/* 23 */     double distanceX = start.getX() - finish.getX();
/* 24 */     double distanceY = start.getY() - finish.getY();
/*    */ 
/* 26 */     return Math.sqrt(distanceX * distanceX + distanceY * distanceY);
/*    */   }
/*    */ 
/*    */   public static double getBearingBetweenLocations(Location start, Location finish)
/*    */   {
/* 31 */     double distanceX = finish.getX() - start.getX();
/* 32 */     double distanceY = finish.getY() - start.getY();
/*    */ 
/* 34 */     if (distanceY >= 0.0D) {
/* 35 */       return Utils.normalAbsoluteAngleDegrees(Math.toDegrees(Math.atan(distanceX / distanceY)));
/*    */     }
/*    */ 
/* 38 */     return Utils.normalAbsoluteAngleDegrees(180.0D + Math.toDegrees(Math.atan(distanceX / distanceY)));
/*    */   }
/*    */ 
/*    */   public static double getRelativeBearing(double currentHeading, double newHeading)
/*    */   {
/* 43 */     double relativeBearing = newHeading - currentHeading;
/*    */ 
/* 45 */     if (relativeBearing > 180.0D) {
/* 46 */       relativeBearing -= 360.0D;
/*    */     }
/* 48 */     else if (relativeBearing < -180.0D) {
/* 49 */       relativeBearing += 360.0D;
/*    */     }
/*    */ 
/* 52 */     return relativeBearing;
/*    */   }
/*    */ 
/*    */   public static double getTargetBearingMarginOfError(double distance, double targetSmallestDimension) {
/* 56 */     return Math.toDegrees(Math.atan(targetSmallestDimension / 2.0D / distance));
/*    */   }
/*    */ }

/* Location:           E:\naval-robocode\sample\dmh.robocode.robot.BlackDeathMk7_7.0.jar
 * Qualified Name:     dmh.robocode.utils.Geometry
 * JD-Core Version:    0.6.2
 */