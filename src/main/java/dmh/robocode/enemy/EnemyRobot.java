/*     */ package dmh.robocode.enemy;
/*     */ 
/*     */ import dmh.robocode.data.AverageLocation;
/*     */ import dmh.robocode.data.BattleConstants;
/*     */ import dmh.robocode.data.DynamicMovementSequence;
/*     */ import dmh.robocode.data.Location;
/*     */ import dmh.robocode.data.Movement;
/*     */ import dmh.robocode.data.RadarObservation;
/*     */ import dmh.robocode.data.RadarObservationList;
/*     */ import dmh.robocode.data.ShotAtEnemy;
/*     */ import dmh.robocode.data.ShotByEnemy;
/*     */ import dmh.robocode.gunner.aiming.AimingStrategy;
/*     */ import dmh.robocode.utils.Geometry;
/*     */ import java.awt.Color;
/*     */ import java.awt.Graphics2D;
/*     */ import java.io.PrintStream;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import robocode.Rules;
/*     */ 
/*     */ public class EnemyRobot
/*     */ {
/*     */   private String name;
/*  28 */   private boolean isLearningAllowed = true;
/*  29 */   private boolean isAlive = true;
/*  30 */   long timeOfNextShot = 0L;
/*     */   private AverageLocation averageLocation;
/*     */   private RadarObservationList observations;
/*     */   private List<ShotAtEnemy> shotAtEnemyHistory;
/*     */   private List<ShotByEnemy> shotByEnemyHistory;
/*     */   private List<AimingStrategy> aimingStrategies;
/*     */   private DynamicMovementSequence currentMovementSequence;
/*  39 */   private static double statisticDistanceRange = 50.0D;
/*  40 */   private static short statisticNumberOfRanges = 20;
/*  41 */   private static int minShotsForGoodStatistic = 20;
/*  42 */   private static double averageLocationWeightingFactor = 1.05D;
/*     */ 
/*  44 */   private double[] damageRatios = new double[statisticNumberOfRanges];
/*  45 */   private double[] shootingAccuracies = new double[statisticNumberOfRanges];
/*     */ 
/*  47 */   private double damageRatio = 0.0D;
/*  48 */   private double shootingAccuracy = 50.0D;
/*     */   private EnemyRobotMovementAnalyser movementAnalyser;
/*     */ 
/*     */   public EnemyRobot(String name)
/*     */   {
/*  52 */     this.name = name;
/*  53 */     this.observations = new RadarObservationList();
/*  54 */     this.currentMovementSequence = new DynamicMovementSequence();
/*  55 */     this.shotAtEnemyHistory = new ArrayList();
/*  56 */     this.shotByEnemyHistory = new ArrayList();
/*  57 */     this.movementAnalyser = new EnemyRobotMovementAnalyser();
/*  58 */     this.aimingStrategies = new ArrayList();
/*  59 */     this.averageLocation = new AverageLocation(averageLocationWeightingFactor);
/*     */ 
/*  61 */     for (int range = 0; range < statisticNumberOfRanges; range++) {
/*  62 */       this.damageRatios[range] = this.damageRatio;
/*  63 */       this.shootingAccuracies[range] = this.shootingAccuracy;
/*     */     }
/*     */   }
/*     */ 
/*     */   public String getName() {
/*  68 */     return this.name;
/*     */   }
/*     */ 
/*     */   public List<RadarObservation> getObservationsSince(long earliestTime) {
/*  72 */     return this.observations.getAllSince(earliestTime);
/*     */   }
/*     */ 
/*     */   public void processScannedRobotEvent(String enemyName, RadarObservation newRadarObservation) {
/*  76 */     if (enemyName.equals(this.name)) {
/*  77 */       this.observations.add(newRadarObservation);
/*  78 */       recordLatestMovement();
/*  79 */       this.averageLocation.recordLocation(newRadarObservation.getLocation());
/*     */     }
/*     */   }
/*     */ 
/*     */   private void recordLatestMovement() {
/*  84 */     RadarObservation previousObservation = getPreviousRadarObservation();
/*  85 */     RadarObservation latestObservation = getLatestRadarObservation();
/*     */ 
/*  87 */     if ((previousObservation != null) && (previousObservation.getTimeSeen() == latestObservation.getTimeSeen() - 1L))
/*     */     {
/*  89 */       double rateOfTurn = Geometry.getRelativeBearing(previousObservation.getHeading(), latestObservation.getHeading());
/*  90 */       double distance = latestObservation.getVelocity();
/*     */ 
/*  92 */       Movement movement = new Movement(rateOfTurn, distance);
/*  93 */       this.currentMovementSequence.add(movement, latestObservation.getTimeSeen());
/*  94 */       this.movementAnalyser.notifyNewMovement(this.currentMovementSequence, latestObservation.getTimeSeen());
/*     */     }
/*     */   }
/*     */ 
/*     */   public EnemyRobotMovementAnalyser getMovementAnalyser()
/*     */   {
/* 109 */     return this.movementAnalyser;
/*     */   }
/*     */ 
/*     */   public RadarObservation getLatestRadarObservation() {
/* 113 */     return this.observations.getLatest();
/*     */   }
/*     */ 
/*     */   public RadarObservation getPreviousRadarObservation() {
/* 117 */     return this.observations.getPrevious();
/*     */   }
/*     */ 
/*     */   public RadarObservation getObservationAtTime(long time) {
/* 121 */     return this.observations.getObservationAtTime(time);
/*     */   }
/*     */ 
/*     */   public DynamicMovementSequence getCurrentMovementSequence() {
/* 125 */     return this.currentMovementSequence;
/*     */   }
/*     */ 
/*     */   public boolean isAlive() {
/* 129 */     return this.isAlive;
/*     */   }
/*     */ 
/*     */   public void processRobotDeathEvent(String deadRobotName)
/*     */   {
/* 134 */     if (deadRobotName.equals(this.name))
/* 135 */       this.isAlive = false;
/*     */   }
/*     */ 
/*     */   public List<ShotAtEnemy> getShotAtEnemyHistory()
/*     */   {
/* 140 */     return this.shotAtEnemyHistory;
/*     */   }
/*     */ 
/*     */   public List<ShotByEnemy> getShotByEnemyHistory() {
/* 144 */     return this.shotByEnemyHistory;
/*     */   }
/*     */ 
/*     */   public double getDamageRatio() {
/* 148 */     return this.damageRatio;
/*     */   }
/*     */ 
/*     */   public void resetForNextRound()
/*     */   {
/* 157 */     this.isAlive = true;
/* 158 */     this.currentMovementSequence = new DynamicMovementSequence();
/* 159 */     this.observations = new RadarObservationList();
/* 160 */     if (this.isLearningAllowed) {
/* 161 */       updateStatistics();
/*     */     }
/* 163 */     this.movementAnalyser.resetForNextRound();
/* 164 */     this.averageLocation = new AverageLocation(averageLocationWeightingFactor);
/*     */   }
/*     */ 
/*     */   public void recordMyShot(String category, ShotAtEnemy myShot) {
/* 168 */     this.shotAtEnemyHistory.add(myShot);
/*     */ 
/* 170 */     for (AimingStrategy strategy : this.aimingStrategies) {
/* 171 */       strategy.simulateShot(category, Rules.getBulletSpeed(myShot.getBulletPower()));
/* 172 */       strategy.notifyShotJustFired();
/*     */     }
/*     */   }
/*     */ 
/*     */   public void recordEnemyHitUs(ShotByEnemy shotDetails) {
/* 177 */     this.shotByEnemyHistory.add(shotDetails);
/*     */   }
/*     */ 
/*     */   private void updateStatistics()
/*     */   {
/* 182 */     double allHitDamage = 0.0D;
/* 183 */     double allMissDamage = 0.0D;
/* 184 */     int allShots = 0;
/* 185 */     int allHits = 0;
/*     */ 
/* 187 */     double[] hitDamage = new double[statisticNumberOfRanges];
/* 188 */     double[] missDamage = new double[statisticNumberOfRanges];
/* 189 */     int[] shots = new int[statisticNumberOfRanges];
/* 190 */     int[] hits = new int[statisticNumberOfRanges];
/*     */ 
/* 192 */     for (ShotAtEnemy shot : this.shotAtEnemyHistory) {
/* 193 */       short range = turnDistanceIntoStatisticRange(shot.getEnemyDistance());
/*     */ 
/* 195 */       switch ($SWITCH_TABLE$dmh$robocode$data$ShotAtEnemy$ShotStatus()[shot.getStatus().ordinal()]) {
/*     */       case 2:
/* 197 */         allHitDamage += Rules.getBulletDamage(shot.getBulletPower());
/* 198 */         allShots++;
/* 199 */         allHits++;
/* 200 */         hitDamage[range] += Rules.getBulletDamage(shot.getBulletPower());
/* 201 */         shots[range] += 1;
/* 202 */         hits[range] += 1;
/* 203 */         break;
/*     */       case 5:
/* 205 */         allMissDamage += Rules.getBulletDamage(shot.getBulletPower());
/* 206 */         allShots++;
/* 207 */         missDamage[range] += Rules.getBulletDamage(shot.getBulletPower());
/* 208 */         shots[range] += 1;
/*     */       case 3:
/*     */       case 4:
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 215 */     double allDamageReceived = 0.0D;
/* 216 */     double[] damageReceived = new double[statisticNumberOfRanges];
/*     */ 
/* 218 */     for (ShotByEnemy shot : this.shotByEnemyHistory) {
/* 219 */       allDamageReceived += Rules.getBulletDamage(shot.getBulletPower());
/*     */ 
/* 221 */       if (shot.getEnemyDistance() != ShotByEnemy.unknownEnemyDistance) {
/* 222 */         damageReceived[turnDistanceIntoStatisticRange(shot.getEnemyDistance())] += Rules.getBulletDamage(shot.getBulletPower());
/*     */       }
/*     */     }
/*     */ 
/* 226 */     if (allShots >= minShotsForGoodStatistic) {
/* 227 */       this.damageRatio = ((allHitDamage - allDamageReceived) * 100.0D / (allHitDamage + allMissDamage));
/* 228 */       this.shootingAccuracy = (allHits * 100.0D / allShots);
/*     */     }
/*     */ 
/* 231 */     for (int range = 0; range < statisticNumberOfRanges; range++)
/* 232 */       if (shots[range] >= minShotsForGoodStatistic) {
/* 233 */         this.damageRatios[range] = ((hitDamage[range] - damageReceived[range]) * 100.0D / (hitDamage[range] + missDamage[range]));
/* 234 */         this.shootingAccuracies[range] = (hits[range] * 100.0D / shots[range]);
/*     */       } else {
/* 236 */         this.damageRatios[range] = this.damageRatio;
/* 237 */         this.shootingAccuracies[range] = this.shootingAccuracy;
/*     */       }
/*     */   }
/*     */ 
/*     */   public double getShootingAccuracy()
/*     */   {
/* 264 */     return this.shootingAccuracy;
/*     */   }
/*     */ 
/*     */   public double getDamageRatio(double distance) {
/* 268 */     return this.damageRatios[turnDistanceIntoStatisticRange(distance)];
/*     */   }
/*     */ 
/*     */   public double getShootingAccuracy(double distance) {
/* 272 */     return this.shootingAccuracies[turnDistanceIntoStatisticRange(distance)];
/*     */   }
/*     */ 
/*     */   private short turnDistanceIntoStatisticRange(double distance) {
/* 276 */     double range = Math.floor(distance / statisticDistanceRange);
/* 277 */     return (short)(int)Math.max(0.0D, Math.min(range, statisticNumberOfRanges - 1));
/*     */   }
/*     */ 
/*     */   public String getStatisticsDebugString()
/*     */   {
/* 284 */     System.out.println("Enemy : " + getName());
/*     */ 
/* 286 */     System.out.println("    Damage Ratio =      " + (int)this.damageRatio + "%");
/* 287 */     System.out.println("    Shooting Accuracy = " + (int)this.shootingAccuracy + "%");
/*     */ 
/* 289 */     System.out.print("    Damage Ratio by Range : ");
/* 290 */     for (int range = 0; range < statisticNumberOfRanges; range++) {
/* 291 */       System.out.print((int)this.damageRatios[range] + "%  ");
/*     */     }
/* 293 */     System.out.println();
/*     */ 
/* 295 */     System.out.print("    Shooting Accuracy by Range : ");
/* 296 */     for (int range = 0; range < statisticNumberOfRanges; range++) {
/* 297 */       System.out.print((int)this.shootingAccuracies[range] + "%  ");
/*     */     }
/*     */ 
/* 300 */     return "";
/*     */   }
/*     */ 
/*     */   public double getDangerEnergy()
/*     */   {
/* 306 */     if (this.observations.getLatest() != null) {
/* 307 */       return (100.0D - this.damageRatio) / 100.0D * this.observations.getLatest().getEnergy();
/*     */     }
/* 309 */     return 0.0D;
/*     */   }
/*     */ 
/*     */   public double getDangerEnergyWithGravity(double safeDistance, Location myLocation)
/*     */   {
/* 316 */     if (this.observations.getLatest() != null) {
/* 317 */       double actualDistance = Geometry.getDistanceBetweenLocations(myLocation, this.observations.getLatest().getLocation());
/*     */ 
/* 319 */       if (actualDistance < safeDistance) {
/* 320 */         return (safeDistance - actualDistance) / safeDistance * getDangerEnergy();
/*     */       }
/*     */     }
/* 323 */     return 0.0D;
/*     */   }
/*     */ 
/*     */   public double getTargetEnergy()
/*     */   {
/* 329 */     if (this.observations.getLatest() != null) {
/* 330 */       return this.damageRatio / 100.0D * this.observations.getLatest().getEnergy();
/*     */     }
/* 332 */     return 100.0D;
/*     */   }
/*     */ 
/*     */   public double getTargetEnergyWithGravity(double shootingDistance, Location myLocation)
/*     */   {
/* 339 */     if (this.observations.getLatest() != null) {
/* 340 */       double actualDistance = Geometry.getDistanceBetweenLocations(myLocation, this.observations.getLatest().getLocation());
/*     */ 
/* 342 */       if (actualDistance > shootingDistance) {
/* 343 */         if (getTargetEnergy() > 0.0D) {
/* 344 */           return Math.max(0.0D, (2.0D * shootingDistance - actualDistance) / shootingDistance * getTargetEnergy());
/*     */         }
/* 346 */         return Math.min(0.0D, (2.0D * shootingDistance - actualDistance) / shootingDistance * getTargetEnergy());
/*     */       }
/*     */     }
/*     */ 
/* 350 */     return getTargetEnergy();
/*     */   }
/*     */ 
/*     */   void setDamageRatioForTesting(double testDamageRatio)
/*     */   {
/* 355 */     this.damageRatio = testDamageRatio;
/*     */   }
/*     */ 
/*     */   public DynamicMovementSequence getMovementSequenceCloneSinceTime(long sinceTime)
/*     */   {
/* 362 */     return this.currentMovementSequence.cloneMovementsSince(sinceTime);
/*     */   }
/*     */ 
/*     */   public void addAimingStrategy(AimingStrategy strategy) {
/* 366 */     this.aimingStrategies.add(strategy);
/*     */   }
/*     */ 
/*     */   public void removeAimingStrategy(AimingStrategy strategy) {
/* 370 */     this.aimingStrategies.remove(strategy);
/*     */   }
/*     */ 
/*     */   public List<AimingStrategy> getAimingStrategies() {
/* 374 */     return this.aimingStrategies;
/*     */   }
/*     */ 
/*     */   public void hasJustFired(double bulletPower, long time) {
/* 378 */     this.timeOfNextShot = (time + ()Math.ceil(Rules.getGunHeat(bulletPower) / BattleConstants.getInstance().getGunCoolingRate()));
/*     */   }
/*     */ 
/*     */   public long getTimeOfNextShot() {
/* 382 */     return this.timeOfNextShot;
/*     */   }
/*     */ 
/*     */   public Location getAverageLocation() {
/* 386 */     return this.averageLocation.getAverageLocation();
/*     */   }
/*     */ 
/*     */   public void drawCircle(Graphics2D g, Color upToDateColour, Color fairlyRecentColour, int radius, long currentTime) {
/* 390 */     if (getLatestRadarObservation() != null) {
/* 391 */       long age = currentTime - getLatestRadarObservation().getTimeSeen();
/*     */ 
/* 393 */       if (age == 0L)
/* 394 */         drawCircle(g, upToDateColour, radius);
/* 395 */       else if (age <= 20L)
/* 396 */         drawCircle(g, fairlyRecentColour, radius);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void drawCircle(Graphics2D g, Color colour, int radius)
/*     */   {
/* 402 */     if (getLatestRadarObservation() != null) {
/* 403 */       int x = (int)getLatestRadarObservation().getLocation().getX();
/* 404 */       int y = (int)getLatestRadarObservation().getLocation().getY();
/*     */ 
/* 406 */       g.setColor(colour);
/* 407 */       g.drawOval(x - radius, y - radius, radius * 2, radius * 2);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void turnOffLearning() {
/* 412 */     this.isLearningAllowed = false;
/*     */   }
/*     */ 
/*     */   public boolean isLearningAllowed() {
/* 416 */     return this.isLearningAllowed;
/*     */   }
/*     */ }

/* Location:           E:\naval-robocode\sample\dmh.robocode.robot.BlackDeathMk7_7.0.jar
 * Qualified Name:     dmh.robocode.enemy.EnemyRobot
 * JD-Core Version:    0.6.2
 */