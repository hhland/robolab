/*    */ package dmh.robocode.utils;
/*    */ 
/*    */ public class ConvertStatistic
/*    */ {
/*    */   public static byte makeByte(long originalValue)
/*    */   {
/* 12 */     if (originalValue > 127L)
/* 13 */       return 127;
/* 14 */     if (originalValue < -128L) {
/* 15 */       return -128;
/*    */     }
/* 17 */     return (byte)(int)originalValue;
/*    */   }
/*    */ 
/*    */   public static byte makeByte(double originalValue)
/*    */   {
/* 22 */     long roundedValue = Math.round(originalValue);
/* 23 */     return makeByte(roundedValue);
/*    */   }
/*    */ 
/*    */   public static short makeShort(long originalValue) {
/* 27 */     if (originalValue > 32767L)
/* 28 */       return 32767;
/* 29 */     if (originalValue < -32768L) {
/* 30 */       return -32768;
/*    */     }
/* 32 */     return (short)(int)originalValue;
/*    */   }
/*    */ 
/*    */   public static short makeShort(double originalValue)
/*    */   {
/* 37 */     long roundedValue = Math.round(originalValue);
/* 38 */     return makeShort(roundedValue);
/*    */   }
/*    */ }

/* Location:           E:\naval-robocode\sample\dmh.robocode.robot.BlackDeathMk7_7.0.jar
 * Qualified Name:     dmh.robocode.utils.ConvertStatistic
 * JD-Core Version:    0.6.2
 */