/*    */ package dmh.robocode.utils;
/*    */ 
/*    */ import robocode.Rules;
/*    */ 
/*    */ public class BulletPowerUtils
/*    */ {
/*    */   public static double getPowerToCauseDamage(double damage)
/*    */   {
/*  8 */     if (damage <= 4.0D) {
/*  9 */       return damage / 4.0D;
/*    */     }
/* 11 */     return 1.0D + (damage - 4.0D) / 6.0D;
/*    */   }
/*    */ 
/*    */   public static int getNumberOfBulletsNeededToDestroy(double remainingEnergy)
/*    */   {
/* 16 */     return (int)Math.ceil(remainingEnergy / Rules.getBulletDamage(3.0D));
/*    */   }
/*    */ 
/*    */   public static double getAverageBulletPowerToDestroy(double remainingEnergy) {
/* 20 */     int shots = getNumberOfBulletsNeededToDestroy(remainingEnergy);
/* 21 */     if (shots > 0) {
/* 22 */       return getPowerToCauseDamage(remainingEnergy / shots);
/*    */     }
/* 24 */     return 0.01D;
/*    */   }
/*    */ 
/*    */   public static double getPowerToTravelAtSpeed(double speed)
/*    */   {
/* 29 */     return (20.0D - speed) / 3.0D;
/*    */   }
/*    */ }

/* Location:           E:\naval-robocode\sample\dmh.robocode.robot.BlackDeathMk7_7.0.jar
 * Qualified Name:     dmh.robocode.utils.BulletPowerUtils
 * JD-Core Version:    0.6.2
 */