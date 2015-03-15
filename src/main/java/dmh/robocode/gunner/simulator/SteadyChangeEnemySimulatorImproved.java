/*    */ package dmh.robocode.gunner.simulator;
/*    */ 
/*    */ import dmh.robocode.data.Location;
/*    */ import dmh.robocode.simulate.RobotMovementSimulator;
/*    */ 
/*    */ public class SteadyChangeEnemySimulatorImproved
/*    */   implements EnemySimulator
/*    */ {
/*    */   RobotMovementSimulator robot;
/*    */   private double acceleration;
/*    */   private double rateOfTurn;
/*    */ 
/*    */   public SteadyChangeEnemySimulatorImproved(Location location, double velocity, double heading, double acceleration, double rateOfTurn)
/*    */   {
/* 14 */     this.robot = new RobotMovementSimulator(location, heading, velocity, 0L);
/* 15 */     this.acceleration = acceleration;
/* 16 */     this.rateOfTurn = rateOfTurn;
/*    */   }
/*    */ 
/*    */   public Location getLocation()
/*    */   {
/* 21 */     return this.robot.getLocation();
/*    */   }
/*    */ 
/*    */   public void takeOneTurn()
/*    */   {
/* 26 */     this.robot.takeTurn(this.rateOfTurn, this.robot.getVelocity() + this.acceleration, this.robot.getVelocity() + this.acceleration);
/*    */   }
/*    */ }

/* Location:           E:\naval-robocode\sample\dmh.robocode.robot.BlackDeathMk7_7.0.jar
 * Qualified Name:     dmh.robocode.gunner.simulator.SteadyChangeEnemySimulatorImproved
 * JD-Core Version:    0.6.2
 */