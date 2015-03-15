/*    */ package gre.svman4;
/*    */ 
/*    */ class RobotState extends MotionState
/*    */ {
/*    */   private static final long serialVersionUID = -2132589243133044834L;
/*    */   public double deltaHeading;
/* 18 */   public double energy = 100.0D;
/*    */ 
/* 20 */   public double maxEscapeAngle(double velocity) { double temp = Math.asin(8.0D / velocity);
/* 21 */     return temp;
/*    */   }
/*    */ }

/* Location:           E:\naval-robocode\sample\gre.svman4.Morfeas_1.4.jar
 * Qualified Name:     gre.svman4.RobotState
 * JD-Core Version:    0.6.2
 */