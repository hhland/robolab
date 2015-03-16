/*     */ package dmh.robocode.gunner.aiming;
/*     */ 
/*     */ import dmh.robocode.bullet.SimulatedBullet;
/*     */ import dmh.robocode.bullet.SimulatedBulletList;
/*     */ import dmh.robocode.data.Location;
/*     */ import dmh.robocode.data.RadarObservation;
/*     */ import dmh.robocode.enemy.EnemyRobot;
/*     */ import dmh.robocode.robot.CommandBasedRobot;
/*     */ import dmh.robocode.utils.Geometry;
/*     */ import java.awt.Color;
/*     */ import java.io.PrintStream;
/*     */ import java.util.Collection;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map;
/*     */ import java.util.Map.Entry;
/*     */ 
/*     */ public abstract class CalibratedAimingStrategy
/*     */   implements AimingStrategy
/*     */ {
/*     */   private CommandBasedRobot myRobot;
/*     */   private EnemyRobot enemy;
/*     */   private int segmentCount;
/*     */   private Map<String, SimulatedBulletList[]> allBulletSegments;
/*     */   private boolean isLearningAllowed;
/*     */ 
/*     */   public CalibratedAimingStrategy(CommandBasedRobot myRobot, EnemyRobot enemy, boolean isLearningAllowed)
/*     */   {
/*  25 */     this.myRobot = myRobot;
/*  26 */     this.enemy = enemy;
/*  27 */     this.allBulletSegments = new HashMap();
/*  28 */     this.isLearningAllowed = isLearningAllowed;
/*  29 */     if (isLearningAllowed)
/*  30 */       this.segmentCount = Math.min(20, myRobot.getNumRounds() / 5 + 1);
/*     */     else
/*  32 */       this.segmentCount = 1;
/*     */   }
/*     */ 
/*     */   private SimulatedBulletList[] getSegments(String category)
/*     */   {
/*  37 */     SimulatedBulletList[] segments = (SimulatedBulletList[])this.allBulletSegments.get(category);
/*  38 */     if (segments == null) {
/*  39 */       segments = getNewSegments();
/*  40 */       this.allBulletSegments.put(category, segments);
/*     */     }
/*  42 */     return segments;
/*     */   }
/*     */ 
/*     */   private SimulatedBulletList[] getNewSegments() {
/*  46 */     SimulatedBulletList[] newSegments = new SimulatedBulletList[this.segmentCount];
/*  47 */     for (int s = 0; s < newSegments.length; s++) {
/*  48 */       newSegments[s] = new SimulatedBulletList();
/*     */     }
/*  50 */     return newSegments;
/*     */   }
/*     */ 
/*     */   public final void simulateShot(String category, double bulletSpeed)
/*     */   {
/*  55 */     if (this.isLearningAllowed) {
/*  56 */       Location target = getTargetForShot(bulletSpeed);
/*  57 */       if (target != null) {
/*  58 */         double targetBearing = Geometry.getBearingBetweenLocations(this.myRobot.getLocation(), target);
/*  59 */         double estimatedSuccess = getEstimatedSuccessOfShotUsingRules(bulletSpeed);
/*  60 */         int segmentNumber = getSegmentNumber(estimatedSuccess);
/*  61 */         SimulatedBullet bullet = new SimulatedBullet(this.enemy, this.myRobot.getLocation(), this.myRobot.getTime(), targetBearing, bulletSpeed);
/*  62 */         getSegments(category)[segmentNumber].add(bullet);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public void notifyShotJustFired()
/*     */   {
/*     */   }
/*     */ 
/*     */   public double getEstimatedSuccessOfShot(String category, double bulletSpeed)
/*     */   {
/*  73 */     double estimatedSuccess = getEstimatedSuccessOfShotUsingRules(bulletSpeed);
/*     */ 
/*  75 */     if (this.isLearningAllowed) {
/*  76 */       int segmentNumber = getSegmentNumber(estimatedSuccess);
/*  77 */       double simulatedSuccess = getSegments(category)[segmentNumber].getSuccessRate();
/*  78 */       double segmentMidpoint = getSegmentMidpoint(segmentNumber);
/*  79 */       double adjustment = simulatedSuccess - segmentMidpoint;
/*  80 */       return Math.max(0.0D, estimatedSuccess + adjustment);
/*     */     }
/*  82 */     return Math.max(0.0D, estimatedSuccess - Math.random());
/*     */   }
/*     */ 
/*     */   protected double getDefaultEstimatedSuccess(double bulletSpeed)
/*     */   {
/*  87 */     Location enemyLocation = getEnemy().getLatestRadarObservation().getLocation();
/*  88 */     if (enemyLocation == null) {
/*  89 */       return 0.0D;
/*     */     }
/*  91 */     double distance = Geometry.getDistanceBetweenLocations(getMyRobot().getLocation(), enemyLocation);
/*  92 */     return Math.max(0.0D, 100.0D - distance / bulletSpeed);
/*     */   }
/*     */ 
/*     */   public abstract double getEstimatedSuccessOfShotUsingRules(double paramDouble);
/*     */ 
/*     */   public abstract Location getTargetForShot(double paramDouble);
/*     */ 
/*     */   public abstract Color getDebugColour();
/*     */ 
/*     */   public final void processTurn()
/*     */   {
/*     */
/*     */     int i=0,j;
/* 104 */     for (Iterator localIterator = this.allBulletSegments.values().iterator(); localIterator.hasNext(); 
/* 105 */       )
/*     */     {
/* 104 */       SimulatedBulletList[] segments = (SimulatedBulletList[])localIterator.next();
/*     */       SimulatedBulletList[] arrayOfSimulatedBulletList1;
/* 105 */       j = (arrayOfSimulatedBulletList1 = segments).length;
                SimulatedBulletList segment = arrayOfSimulatedBulletList1[i];
/* 106 */       segment.processCurrentTime(this.myRobot.getTime());
/*     */ 
/* 105 */       i++;
/*     */     }
/*     */   }
/*     */ 
/*     */   public final void processEndOfRound(CommandBasedRobot myRobotInNextRound)
/*     */   {
/*     */     int j;
/*     */     int i=0;
/* 113 */     for (Iterator localIterator = this.allBulletSegments.values().iterator(); localIterator.hasNext(); 
/* 114 */       )
/*     */     {
/* 113 */       SimulatedBulletList[] segments = (SimulatedBulletList[])localIterator.next();
/*     */       SimulatedBulletList[] arrayOfSimulatedBulletList1;
/* 114 */       j = (arrayOfSimulatedBulletList1 = segments).length;
                SimulatedBulletList segment = arrayOfSimulatedBulletList1[i];
/* 115 */       segment.processEndOfRound();
/*     */ 
/* 114 */       i++;
/*     */     }
/*     */ 
/* 118 */     this.myRobot = myRobotInNextRound;
/*     */   }
/*     */ 
/*     */   public void debugDisplayStats()
/*     */   {
/* 123 */     for (Map.Entry categoryEntry : this.allBulletSegments.entrySet()) {
/* 124 */       int hits = 0;
/* 125 */       int misses = 0;
/* 126 */       for (SimulatedBulletList segment : (SimulatedBulletList[])categoryEntry.getValue()) {
/* 127 */         hits += segment.getTotalHits();
/* 128 */         misses += segment.getTotalMisses();
/*     */       }
/* 130 */       if (hits + misses > 0) {
/* 131 */         int successRating = hits * 100 / (hits + misses);
/*     */ 
/* 133 */         System.out.println("    " + (String)categoryEntry.getKey() + "    " + getClass().getSimpleName() + "    " + successRating + "% [ " + 
/* 134 */           hits + "/" + (hits + misses) + " ]");
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   private int getSegmentNumber(double estimatedSuccess) {
/* 140 */     return (int)Math.min(this.segmentCount - 1, Math.floor(estimatedSuccess / 100.0D * this.segmentCount));
/*     */   }
/*     */ 
/*     */   private double getSegmentMidpoint(int segment)
/*     */   {
/* 145 */     return (segment + 0.5D) * 100.0D / this.segmentCount;
/*     */   }
/*     */ 
/*     */   protected EnemyRobot getEnemy() {
/* 149 */     return this.enemy;
/*     */   }
/*     */ 
/*     */   protected CommandBasedRobot getMyRobot() {
/* 153 */     return this.myRobot;
/*     */   }
/*     */ }

/* Location:           E:\naval-robocode\sample\dmh.robocode.robot.BlackDeathMk7_7.0.jar
 * Qualified Name:     dmh.robocode.gunner.aiming.CalibratedAimingStrategy
 * JD-Core Version:    0.6.2
 */