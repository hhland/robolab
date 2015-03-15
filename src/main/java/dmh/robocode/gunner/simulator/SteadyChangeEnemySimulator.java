/*    */ package dmh.robocode.gunner.simulator;
/*    */ 
/*    */ import dmh.robocode.data.Location;
/*    */ import dmh.robocode.utils.Geometry;
/*    */ 
/*    */ public class SteadyChangeEnemySimulator
/*    */   implements EnemySimulator
/*    */ {
/*    */   private Location location;
/*    */   private double velocity;
/*    */   private double heading;
/*    */   private double acceleration;
/*    */   private double rateOfTurn;
/*    */   private double minVelocity;
/*    */   private double maxVelocity;
/*    */ 
/*    */   public SteadyChangeEnemySimulator(Location location, double velocity, double heading, double acceleration, double rateOfTurn)
/*    */   {
/* 18 */     this.location = location;
/* 19 */     this.velocity = velocity;
/* 20 */     this.heading = heading;
/* 21 */     this.acceleration = acceleration;
/* 22 */     this.rateOfTurn = rateOfTurn;
/*    */ 
/* 24 */     switch ((int)Math.signum(velocity)) {
/*    */     case 0:
/* 26 */       this.minVelocity = -8.0D;
/* 27 */       this.maxVelocity = 8.0D;
/* 28 */       break;
/*    */     case 1:
/* 30 */       this.minVelocity = 0.0D;
/* 31 */       this.maxVelocity = 8.0D;
/* 32 */       break;
/*    */     case -1:
/* 34 */       this.minVelocity = -8.0D;
/* 35 */       this.maxVelocity = 0.0D;
/*    */     }
/*    */   }
/*    */ 
/*    */   public Location getLocation()
/*    */   {
/* 42 */     return this.location;
/*    */   }
/*    */ 
/*    */   public void takeOneTurn()
/*    */   {
/* 47 */     this.location = Geometry.getLocationAtBearing(this.location, this.heading, this.velocity);
/*    */ 
/* 49 */     this.heading += this.rateOfTurn;
/* 50 */     this.velocity = Math.max(Math.min(this.velocity + this.acceleration, this.maxVelocity), this.minVelocity);
/*    */   }
/*    */ 
/*    */   public static double getNewVelocityWithoutChangeOfDirection(double oldVelocity, double acceleration) {
/* 54 */     double newVelocity = oldVelocity + acceleration;
/*    */ 
/* 56 */     if (Math.abs(newVelocity) > 8.0D) {
/* 57 */       newVelocity = 8.0D * Math.signum(newVelocity);
/*    */     }
/*    */ 
/* 60 */     if (((newVelocity < 0.0D) && (oldVelocity > 0.0D)) || (
/* 61 */       (newVelocity > 0.0D) && (oldVelocity < 0.0D))) {
/* 62 */       newVelocity = 0.0D;
/*    */     }
/*    */ 
/* 65 */     return newVelocity;
/*    */   }
/*    */ }

/* Location:           E:\naval-robocode\sample\dmh.robocode.robot.BlackDeathMk7_7.0.jar
 * Qualified Name:     dmh.robocode.gunner.simulator.SteadyChangeEnemySimulator
 * JD-Core Version:    0.6.2
 */