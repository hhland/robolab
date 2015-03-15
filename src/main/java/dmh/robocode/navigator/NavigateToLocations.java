/*     */ package dmh.robocode.navigator;
/*     */ 
/*     */ import dmh.robocode.data.Location;
/*     */ import dmh.robocode.simulate.SimulateableRobot;
/*     */ import java.awt.Graphics2D;
/*     */ 
/*     */ public class NavigateToLocations
/*     */   implements NavigatorCommand
/*     */ {
/*     */   private Location[] targets;
/*  11 */   private int currentTarget = 0;
/*     */   private SimulateableRobot myRobot;
/*  13 */   private boolean stopAtFinalTarget = false;
/*     */   private NavigateToLocation subNavigator;
/*  15 */   private boolean isDoneAlready = false;
/*     */ 
/*     */   public NavigateToLocations(Location[] targets, SimulateableRobot myRobot) {
/*  18 */     this.targets = targets;
/*  19 */     this.myRobot = myRobot;
/*     */ 
/*  21 */     createSubNavigator();
/*     */   }
/*     */ 
/*     */   public NavigateToLocations mustStopAtFinalTarget() {
/*  25 */     this.stopAtFinalTarget = true;
/*  26 */     return this;
/*     */   }
/*     */ 
/*     */   public void setWiggleFactor(int wiggleFactor)
/*     */   {
/*  31 */     if (this.subNavigator != null)
/*  32 */       this.subNavigator.setWiggleFactor(wiggleFactor);
/*     */   }
/*     */ 
/*     */   public int getWiggleFactor()
/*     */   {
/*  39 */     if (this.subNavigator != null) {
/*  40 */       return this.subNavigator.getWiggleFactor();
/*     */     }
/*  42 */     return 0;
/*     */   }
/*     */ 
/*     */   public void setWiggleExpiry(long wiggleExpiry)
/*     */   {
/*  48 */     if (this.subNavigator != null)
/*  49 */       this.subNavigator.setWiggleExpiry(wiggleExpiry);
/*     */   }
/*     */ 
/*     */   public long getWiggleExpiry()
/*     */   {
/*  56 */     if (this.subNavigator != null) {
/*  57 */       return this.subNavigator.getWiggleExpiry();
/*     */     }
/*  59 */     return 0L;
/*     */   }
/*     */ 
/*     */   public void reverseDirection()
/*     */   {
/*  65 */     this.subNavigator.reverseDirection();
/*     */   }
/*     */ 
/*     */   private void createSubNavigator()
/*     */   {
/*  70 */     this.isDoneAlready = (this.currentTarget >= this.targets.length);
/*     */ 
/*  72 */     if (!this.isDoneAlready) {
/*  73 */       this.subNavigator = new NavigateToLocation(this.targets[this.currentTarget], this.myRobot);
/*     */ 
/*  75 */       if ((this.stopAtFinalTarget) && (this.currentTarget == this.targets.length - 1))
/*  76 */         this.subNavigator.mustStopAtTarget();
/*     */     }
/*     */   }
/*     */ 
/*     */   public boolean isDone()
/*     */   {
/*  83 */     return this.isDoneAlready;
/*     */   }
/*     */ 
/*     */   public double getRightTurn()
/*     */   {
/*  88 */     return this.subNavigator.getRightTurn();
/*     */   }
/*     */ 
/*     */   public double getAhead()
/*     */   {
/*  93 */     return this.subNavigator.getAhead();
/*     */   }
/*     */ 
/*     */   public double getVelocity()
/*     */   {
/*  98 */     return this.subNavigator.getVelocity();
/*     */   }
/*     */ 
/*     */   public Location[] getTargets() {
/* 102 */     return this.targets;
/*     */   }
/*     */ 
/*     */   public void executed()
/*     */   {
/* 107 */     if ((!this.isDoneAlready) && (this.subNavigator.isDone())) {
/* 108 */       this.currentTarget += 1;
/* 109 */       createSubNavigator();
/*     */     }
/* 111 */     this.subNavigator.executed();
/*     */   }
/*     */ 
/*     */   public void paint(Graphics2D g)
/*     */   {
/* 116 */     this.subNavigator.paint(g);
/*     */   }
/*     */ }

/* Location:           E:\naval-robocode\sample\dmh.robocode.robot.BlackDeathMk7_7.0.jar
 * Qualified Name:     dmh.robocode.navigator.NavigateToLocations
 * JD-Core Version:    0.6.2
 */