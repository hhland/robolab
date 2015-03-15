/*    */ package dmh.robocode.data;
/*    */ 
/*    */ import dmh.robocode.utils.Geometry;
/*    */ import robocode.ScannedRobotEvent;
/*    */ 
/*    */ public class RadarObservation
/*    */ {
/*    */   private long timeSeen;
/*    */   private Location location;
/*    */   private double velocity;
/*    */   private double heading;
/*    */   private double energy;
/*    */   private double distance;
/*    */ 
/*    */   public RadarObservation(ScannedRobotEvent event, Location myLocation, double myHeading, RadarObservation previousObservation)
/*    */   {
/* 35 */     this.location = Geometry.getLocationAtBearing(myLocation, event.getBearing() + myHeading, event.getDistance());
/* 36 */     this.timeSeen = event.getTime();
/* 37 */     this.velocity = event.getVelocity();
/* 38 */     this.heading = event.getHeading();
/* 39 */     this.energy = event.getEnergy();
/* 40 */     this.distance = event.getDistance();
/*    */ 
/* 42 */     fixVelocityUsingPreviousObservation(previousObservation);
/*    */   }
/*    */ 
/*    */   public double getDistance()
/*    */   {
/* 49 */     return this.distance;
/*    */   }
/*    */ 
/*    */   public double getHeading()
/*    */   {
/* 56 */     return this.heading;
/*    */   }
/*    */ 
/*    */   public long getTimeSeen()
/*    */   {
/* 63 */     return this.timeSeen;
/*    */   }
/*    */ 
/*    */   public Location getLocation()
/*    */   {
/* 70 */     return this.location;
/*    */   }
/*    */ 
/*    */   public double getVelocity()
/*    */   {
/* 77 */     return this.velocity;
/*    */   }
/*    */ 
/*    */   public double getEnergy()
/*    */   {
/* 84 */     return this.energy;
/*    */   }
/*    */ 
/*    */   private void fixVelocityUsingPreviousObservation(RadarObservation previousObservation)
/*    */   {
/* 92 */     if ((this.velocity == 0.0D) && (previousObservation != null) && (previousObservation.getTimeSeen() == this.timeSeen - 1L)) {
/* 93 */       this.velocity = Geometry.getDistanceBetweenLocations(previousObservation.getLocation(), this.location);
/* 94 */       double forwardHeading = Geometry.getBearingBetweenLocations(previousObservation.getLocation(), this.location);
/* 95 */       if (Math.abs(Geometry.getRelativeBearing(forwardHeading, this.heading)) > 90.0D)
/* 96 */         this.velocity = (-this.velocity);
/*    */     }
/*    */   }
/*    */ }

/* Location:           E:\naval-robocode\sample\dmh.robocode.robot.BlackDeathMk7_7.0.jar
 * Qualified Name:     dmh.robocode.data.RadarObservation
 * JD-Core Version:    0.6.2
 */