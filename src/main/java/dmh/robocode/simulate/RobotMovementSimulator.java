/*     */ package dmh.robocode.simulate;
/*     */ 
/*     */ import dmh.robocode.data.BattleConstants;
/*     */ import dmh.robocode.data.Location;
/*     */ import dmh.robocode.utils.Geometry;
/*     */ 
/*     */ public class RobotMovementSimulator
/*     */   implements SimulateableRobot
/*     */ {
/*     */   private Location location;
/*     */   private double heading;
/*     */   private double velocity;
/*     */   private long time;
/*     */ 
/*     */   public RobotMovementSimulator(Location location, double heading, double velocity, long time)
/*     */   {
/*  16 */     this.location = location;
/*  17 */     this.heading = heading;
/*  18 */     this.velocity = velocity;
/*  19 */     this.time = time;
/*     */   }
/*     */ 
/*     */   public void takeTurn(double rightTurn, double ahead, double maxVelocity)
/*     */   {
/*  34 */     double maxAllowedTurn = 10.0D - 0.75D * Math.abs(this.velocity);
/*  35 */     if (Math.abs(rightTurn) > maxAllowedTurn) {
/*  36 */       rightTurn = Math.signum(rightTurn) * maxAllowedTurn;
/*     */     }
/*  38 */     this.heading += rightTurn;
/*     */ 
/*  40 */     double acceleration = 0.0D;
/*  41 */     boolean gettingFaster = false;
/*  42 */     if (ahead > 0.0D) {
/*  43 */       if (this.velocity > 0.0D) {
/*  44 */         if (ahead > this.velocity) {
/*  45 */           acceleration = Math.min(1.0D, ahead - this.velocity);
/*  46 */           gettingFaster = true;
/*     */         } else {
/*  48 */           acceleration = Math.max(-2.0D, maxVelocity - this.velocity);
/*     */         }
/*     */       } else {
/*  51 */         acceleration = Math.min(2.0D, -this.velocity);
/*  52 */         acceleration += (2.0D - acceleration) / 2.0D;
/*     */       }
/*     */     }
/*  55 */     else if (this.velocity > 0.0D) {
/*  56 */       acceleration = Math.max(-2.0D, -this.velocity);
/*  57 */       acceleration -= (acceleration + 2.0D) / 2.0D;
/*     */     }
/*  59 */     else if (ahead > this.velocity) {
/*  60 */       acceleration = Math.max(2.0D, maxVelocity - this.velocity);
/*     */     }
/*  62 */     else if (maxVelocity > this.velocity) {
/*  63 */       acceleration = Math.min(2.0D, maxVelocity - this.velocity);
/*     */     } else {
/*  65 */       acceleration = Math.max(-1.0D, ahead - this.velocity);
/*  66 */       gettingFaster = true;
/*     */     }
/*     */ 
/*  72 */     this.velocity += acceleration;
/*  73 */     if (gettingFaster) {
/*  74 */       if ((maxVelocity > 0.0D) && (this.velocity > Math.min(maxVelocity, 8.0D)))
/*  75 */         this.velocity = Math.min(maxVelocity, 8.0D);
/*  76 */       else if ((maxVelocity < 0.0D) && (this.velocity < Math.max(maxVelocity, -8.0D))) {
/*  77 */         this.velocity = Math.max(maxVelocity, -8.0D);
/*     */       }
/*     */     }
/*  80 */     this.location = Geometry.getLocationAtBearing(this.location, this.heading, this.velocity);
/*  81 */     this.time += 1L;
/*  82 */     preventGoingOffEdge();
/*     */   }
/*     */ 
/*     */   private void preventGoingOffEdge()
/*     */   {
/*  88 */     double xGap = BattleConstants.getInstance().getRobotWidth() / 2.0D;
/*  89 */     double yGap = BattleConstants.getInstance().getRobotHeight() / 2.0D;
/*     */ 
/*  91 */     this.location.chopNearEdgesOfBattlefield(xGap, yGap);
/*     */   }
/*     */ 
/*     */   public Location getLocation()
/*     */   {
/*  97 */     return this.location;
/*     */   }
/*     */ 
/*     */   public long getTime() {
/* 101 */     return this.time;
/*     */   }
/*     */ 
/*     */   public double getHeading() {
/* 105 */     return this.heading;
/*     */   }
/*     */ 
/*     */   public double getVelocity() {
/* 109 */     return this.velocity;
/*     */   }
/*     */ }

/* Location:           E:\naval-robocode\sample\dmh.robocode.robot.BlackDeathMk7_7.0.jar
 * Qualified Name:     dmh.robocode.simulate.RobotMovementSimulator
 * JD-Core Version:    0.6.2
 */