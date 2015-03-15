/*     */ package dmh.robocode.navigator;
/*     */ 
/*     */ import dmh.robocode.data.BattleConstants;
/*     */ import dmh.robocode.data.Location;
/*     */ import dmh.robocode.simulate.SimulateableRobot;
/*     */ import dmh.robocode.utils.Geometry;
/*     */ import java.awt.Color;
/*     */ import java.awt.Graphics2D;
/*     */ import robocode.Rules;
/*     */ 
/*     */ public class NavigateToLocation
/*     */   implements NavigatorCommand
/*     */ {
/*     */   private static final int maximumSensibleWiggleFactor = 20;
/*     */   private static final double wiggleAngle = 3.0D;
/*     */   private static final double defaultTurningSlowDown = 0.66D;
/*     */   private Location target;
/*     */   private SimulateableRobot myRobot;
/*  20 */   private boolean stopAtTarget = false;
/*  21 */   private int reverseTurnsRemaining = 0;
/*  22 */   private int wiggleFactorTurnsEachWay = 0;
/*  23 */   private int currentWiggleTurn = 0;
/*  24 */   private int currentWiggleDirection = 1;
/*  25 */   private long wiggleExpiry = 0L;
/*  26 */   private double rightTurn = 0.0D;
/*     */ 
/*  29 */   private wiggleModeType wiggleState = wiggleModeType.NO_WIGGLE;
/*     */ 
/*  32 */   private directionOfTravelType directionOfTravel = directionOfTravelType.FORWARD;
/*  33 */   private int autoReverseTimeout = 10;
/*     */   private long remainingTravelTime;
/*     */   private double maxVelocity;
/*     */   private double sharpTurnVelocity;
/*     */ 
/*     */   public NavigateToLocation(Location target, SimulateableRobot myRobot)
/*     */   {
/*  39 */     initialise(target, myRobot, 8.0D, 5.28D);
/*     */   }
/*     */ 
/*     */   public NavigateToLocation(Location target, SimulateableRobot myRobot, double maxVelocity) {
/*  43 */     initialise(target, myRobot, maxVelocity, maxVelocity * 0.66D);
/*     */   }
/*     */ 
/*     */   public void initialise(Location target, SimulateableRobot myRobot, double maxVelocity, double sharpTurnVelocity) {
/*  47 */     this.target = target;
/*  48 */     this.myRobot = myRobot;
/*  49 */     this.maxVelocity = maxVelocity;
/*  50 */     this.sharpTurnVelocity = sharpTurnVelocity;
/*     */ 
/*  52 */     if (myRobot.getVelocity() >= 0.0D)
/*  53 */       this.directionOfTravel = directionOfTravelType.FORWARD;
/*     */     else {
/*  55 */       this.directionOfTravel = directionOfTravelType.BACKWARD;
/*     */     }
/*     */ 
/*  58 */     this.remainingTravelTime = (()(Geometry.getDistanceBetweenLocations(myRobot.getLocation(), target) / maxVelocity * 4.0D));
/*     */ 
/*  60 */     executed();
/*     */   }
/*     */ 
/*     */   public void reverseDirection() {
/*  64 */     if (this.directionOfTravel == directionOfTravelType.FORWARD)
/*  65 */       this.directionOfTravel = directionOfTravelType.BACKWARD;
/*     */     else {
/*  67 */       this.directionOfTravel = directionOfTravelType.FORWARD;
/*     */     }
/*     */ 
/*  70 */     if (this.wiggleState == wiggleModeType.SHARP_TURN)
/*  71 */       this.wiggleState = wiggleModeType.RECOVER_HEADING;
/*     */   }
/*     */ 
/*     */   public NavigateToLocation mustStopAtTarget()
/*     */   {
/*  76 */     this.stopAtTarget = true;
/*  77 */     return this;
/*     */   }
/*     */ 
/*     */   public void setWiggleFactor(int wiggleFactor)
/*     */   {
/*  82 */     this.wiggleFactorTurnsEachWay = Math.min(wiggleFactor, 20);
/*     */ 
/*  85 */     if (this.wiggleState == wiggleModeType.NO_WIGGLE)
/*  86 */       this.wiggleState = wiggleModeType.RECOVER_HEADING;
/*     */   }
/*     */ 
/*     */   public int getWiggleFactor()
/*     */   {
/*  92 */     return this.wiggleFactorTurnsEachWay;
/*     */   }
/*     */ 
/*     */   public void setWiggleExpiry(long wiggleExpiry)
/*     */   {
/*  97 */     this.wiggleExpiry = wiggleExpiry;
/*     */   }
/*     */ 
/*     */   public long getWiggleExpiry()
/*     */   {
/* 102 */     return this.wiggleExpiry;
/*     */   }
/*     */ 
/*     */   public boolean isDone()
/*     */   {
/*     */     boolean isDone;
/*     */     boolean isDone;
/* 109 */     if (this.stopAtTarget)
/* 110 */       isDone = (this.remainingTravelTime <= 0L) || (this.target.isSameAs(this.myRobot.getLocation(), 0.1D));
/*     */     else {
/* 112 */       isDone = (this.remainingTravelTime <= 0L) || (this.target.isSameAs(this.myRobot.getLocation(), this.myRobot.getVelocity() / 2.0D));
/*     */     }
/*     */ 
/* 115 */     return isDone;
/*     */   }
/*     */ 
/*     */   public double getRightTurn()
/*     */   {
/* 120 */     return this.rightTurn;
/*     */   }
/*     */ 
/*     */   public double getAhead()
/*     */   {
/* 125 */     if (this.reverseTurnsRemaining > 0) {
/* 126 */       return -24.0D;
/*     */     }
/* 128 */     if (this.stopAtTarget) {
/* 129 */       return Geometry.getDistanceBetweenLocations(this.myRobot.getLocation(), this.target) * velocitySignum();
/*     */     }
/* 131 */     return 24.0D * velocitySignum();
/*     */   }
/*     */ 
/*     */   public double getVelocity()
/*     */   {
/* 137 */     double velocity = this.maxVelocity * velocitySignum();
/*     */ 
/* 140 */     if (Math.abs(getRightTurn()) > Rules.getTurnRate(velocity)) {
/* 141 */       velocity = this.sharpTurnVelocity * velocitySignum();
/*     */     }
/* 143 */     return velocity;
/*     */   }
/*     */ 
/*     */   private int velocitySignum() {
/* 147 */     return this.directionOfTravel == directionOfTravelType.FORWARD ? 1 : -1;
/*     */   }
/*     */ 
/*     */   public Location getTarget() {
/* 151 */     return this.target;
/*     */   }
/*     */ 
/*     */   public void setTarget(Location newTarget) {
/* 155 */     this.target = newTarget;
/*     */   }
/*     */ 
/*     */   public void executed()
/*     */   {
/* 160 */     if (this.reverseTurnsRemaining > 0) {
/* 161 */       this.reverseTurnsRemaining -= 1;
/*     */     }
/*     */ 
/* 164 */     turnOffWiggleIfExpired();
/* 165 */     this.rightTurn = getRightTurnWithoutWiggle();
/* 166 */     considerReversing();
/* 167 */     this.remainingTravelTime -= 1L;
/*     */ 
/* 169 */     switch ($SWITCH_TABLE$dmh$robocode$navigator$NavigateToLocation$wiggleModeType()[this.wiggleState.ordinal()]) {
/*     */     case 2:
/* 171 */       controlWiggleRecover();
/* 172 */       break;
/*     */     case 3:
/* 175 */       controlWiggleTurn();
/* 176 */       break;
/*     */     case 1:
/*     */     }
/*     */   }
/*     */ 
/*     */   private void considerReversing()
/*     */   {
/* 184 */     this.autoReverseTimeout -= 1;
/* 185 */     if ((this.autoReverseTimeout < 0) && (Math.abs(this.rightTurn) >= 45.0D) && (Math.abs(this.rightTurn) <= 135.0D)) {
/* 186 */       reverseDirection();
/* 187 */       this.autoReverseTimeout = 200;
/*     */     }
/*     */   }
/*     */ 
/*     */   private boolean isVeryNearEdge()
/*     */   {
/* 193 */     double howClose = this.myRobot.getLocation().getHowCloseToEdgeOfBattlefield(BattleConstants.getInstance().getBattlefieldWidth(), BattleConstants.getInstance().getBattlefieldHeight());
/*     */ 
/* 195 */     return howClose < 50.0D;
/*     */   }
/*     */ 
/*     */   private double getRightTurnWithoutWiggle() {
/* 199 */     double requiredHeading = Geometry.getBearingBetweenLocations(this.myRobot.getLocation(), this.target);
/*     */ 
/* 201 */     if (this.directionOfTravel == directionOfTravelType.BACKWARD) {
/* 202 */       requiredHeading = oppositeDirectionOf(requiredHeading);
/*     */     }
/*     */ 
/* 207 */     return Geometry.getRelativeBearing(this.myRobot.getHeading(), requiredHeading);
/*     */   }
/*     */ 
/*     */   private double oppositeDirectionOf(double requiredHeading) {
/* 211 */     if (requiredHeading < 180.0D) {
/* 212 */       return requiredHeading + 180.0D;
/*     */     }
/* 214 */     return requiredHeading - 180.0D;
/*     */   }
/*     */ 
/*     */   private void controlWiggleTurn()
/*     */   {
/* 220 */     if ((this.currentWiggleTurn <= this.wiggleFactorTurnsEachWay) && (!isVeryNearEdge())) {
/* 221 */       this.rightTurn = (this.currentWiggleDirection * 3.0D);
/* 222 */       this.currentWiggleTurn += 1;
/*     */     } else {
/* 224 */       this.wiggleState = wiggleModeType.RECOVER_HEADING;
/*     */     }
/*     */   }
/*     */ 
/*     */   private void controlWiggleRecover() {
/* 229 */     if ((Math.abs(this.rightTurn) < 10.0D) && (!isVeryNearEdge()))
/*     */     {
/* 231 */       this.wiggleState = wiggleModeType.SHARP_TURN;
/* 232 */       this.currentWiggleDirection = (-this.currentWiggleDirection);
/*     */ 
/* 234 */       double maxWiggles = (Geometry.getDistanceBetweenLocations(this.myRobot.getLocation(), this.target) - 50.0D) / 20.0D;
/* 235 */       this.currentWiggleTurn = ((int)Math.max(this.wiggleFactorTurnsEachWay - maxWiggles, 1.0D));
/*     */     }
/*     */   }
/*     */ 
/*     */   private void turnOffWiggleIfExpired() {
/* 240 */     if (this.myRobot.getTime() >= this.wiggleExpiry)
/* 241 */       this.wiggleState = wiggleModeType.NO_WIGGLE;
/*     */   }
/*     */ 
/*     */   public void paint(Graphics2D g)
/*     */   {
/* 247 */     g.setColor(Color.RED);
/* 248 */     g.drawLine((int)this.myRobot.getLocation().getX(), (int)this.myRobot.getLocation().getY(), (int)this.target.getX(), (int)this.target.getY());
/* 249 */     g.fillOval((int)this.target.getX() - 4, (int)this.target.getY() - 4, 8, 8);
/*     */   }
/*     */ 
/*     */   private static enum directionOfTravelType
/*     */   {
/*  31 */     FORWARD, BACKWARD;
/*     */   }
/*     */ 
/*     */   private static enum wiggleModeType
/*     */   {
/*  28 */     NO_WIGGLE, RECOVER_HEADING, SHARP_TURN;
/*     */   }
/*     */ }

/* Location:           E:\naval-robocode\sample\dmh.robocode.robot.BlackDeathMk7_7.0.jar
 * Qualified Name:     dmh.robocode.navigator.NavigateToLocation
 * JD-Core Version:    0.6.2
 */