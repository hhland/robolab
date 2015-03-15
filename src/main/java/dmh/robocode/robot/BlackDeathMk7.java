/*     */ package dmh.robocode.robot;
/*     */ 
/*     */ import dmh.robocode.bullet.DangerousBullet;
/*     */ import dmh.robocode.data.BattleConstants;
/*     */ import dmh.robocode.data.Location;
/*     */ import dmh.robocode.data.RadarObservation;
/*     */ import dmh.robocode.enemy.EnemyRobot;
/*     */ import dmh.robocode.enemy.EnemySet;
/*     */ import dmh.robocode.gunner.GunnerBlastEnemy;
/*     */ import dmh.robocode.gunner.GunnerCommand;
/*     */ import dmh.robocode.gunner.GunnerDoNothing;
/*     */ import dmh.robocode.gunner.aiming.AimingStrategy;
/*     */ import dmh.robocode.navigator.NavigateToEnemy;
/*     */ import dmh.robocode.navigator.NavigateToLocation;
/*     */ import dmh.robocode.navigator.NavigateToLocations;
/*     */ import dmh.robocode.navigator.NavigatorCommand;
/*     */ import dmh.robocode.navigator.target.EnemyTargetAlgorithm;
/*     */ import dmh.robocode.navigator.target.EnemyTargetDefault;
/*     */ import dmh.robocode.radar.RadarCommand;
/*     */ import dmh.robocode.radar.RadarFullScan;
/*     */ import dmh.robocode.radar.RadarNarrowLock;
/*     */ import dmh.robocode.simulate.RobotMovementSimulator;
/*     */ import dmh.robocode.utils.BearingSelector;
/*     */ import dmh.robocode.utils.Geometry;
/*     */ import java.awt.Color;
/*     */ import java.awt.Graphics2D;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Map.Entry;
/*     */ import robocode.BattleEndedEvent;
/*     */ import robocode.DeathEvent;
/*     */ import robocode.HitByBulletEvent;
/*     */ import robocode.RobotDeathEvent;
/*     */ import robocode.RoundEndedEvent;
/*     */ import robocode.Rules;
/*     */ 
/*     */ public class BlackDeathMk7 extends CommandBasedRobot
/*     */ {
/*  46 */   private static ColourScheme colourScheme = ColourScheme.BLACK_DEATH;
/*  47 */   private static boolean isLearningToAimAllowed = true;
/*  48 */   private static boolean isLearningFromEnemyStatsAllowed = true;
/*  49 */   private static boolean displayEndOfRoundStats = false;
/*  50 */   private static boolean paintAllowed = false;
/*  51 */   private static boolean displayFinalDebugStats = false;
/*     */   private static final double ABORT_ATTACK_RATIO = 1.3D;
/*  58 */   static double edgeSafetyMargin = 100.0D;
/*  59 */   static double startModeEdgeMargin = 150.0D;
/*  60 */   static double searchModeEdgeMargin = 800.0D;
/*     */   private Location searchBottomLeft;
/*     */   private Location searchTopRight;
/*     */   private Location searchTopLeft;
/*     */   private Location searchBottomRight;
/*     */   private Location startBottomLeft;
/*     */   private Location startTopRight;
/*     */   private Location startTopLeft;
/*     */   private Location startBottomRight;
/*     */   private Location battleCentre;
/*     */   private BattleModeType battleMode;
/*     */   private long timeModeStarted;
/*  75 */   private static double aggressionFactor = 1.2D;
/*     */ 
/*  77 */   private EnemyRobot currentEnemyTarget = null;
/*  78 */   private boolean haveAttacked = false;
/*     */ 
/*  80 */   private double closestNewBulletDistance = 1200.0D;
/*     */ 
/*     */   protected void initialiseRobotCommands()
/*     */   {
/*  84 */     createStandardLocations();
/*  85 */     initializeStartMode();
/*  86 */     if (!isLearningFromEnemyStatsAllowed) {
/*  87 */       enemies.turnOffLearning();
/*     */     }
/*  89 */     if (!displayFinalDebugStats)
/*  90 */       doNotDisplayFinalDebugStats();
/*     */   }
/*     */ 
/*     */   private void createStandardLocations()
/*     */   {
/*  96 */     double maxMargin = Math.min(getBattleFieldHeight(), getBattleFieldWidth()) / 4.0D;
/*     */ 
/*  98 */     searchModeEdgeMargin = Math.min(searchModeEdgeMargin, maxMargin);
/*  99 */     startModeEdgeMargin = Math.min(searchModeEdgeMargin, maxMargin);
/*     */ 
/* 101 */     this.searchBottomLeft = new Location(searchModeEdgeMargin, searchModeEdgeMargin);
/* 102 */     this.searchTopRight = new Location(getBattleFieldWidth() - searchModeEdgeMargin, getBattleFieldHeight() - searchModeEdgeMargin);
/* 103 */     this.searchTopLeft = new Location(searchModeEdgeMargin, getBattleFieldHeight() - searchModeEdgeMargin);
/* 104 */     this.searchBottomRight = new Location(getBattleFieldWidth() - searchModeEdgeMargin, searchModeEdgeMargin);
/*     */ 
/* 106 */     this.startBottomLeft = new Location(startModeEdgeMargin, startModeEdgeMargin);
/* 107 */     this.startTopRight = new Location(getBattleFieldWidth() - startModeEdgeMargin, getBattleFieldHeight() - startModeEdgeMargin);
/* 108 */     this.startTopLeft = new Location(startModeEdgeMargin, getBattleFieldHeight() - startModeEdgeMargin);
/* 109 */     this.startBottomRight = new Location(getBattleFieldWidth() - startModeEdgeMargin, startModeEdgeMargin);
/*     */ 
/* 111 */     this.battleCentre = new Location(getBattleFieldWidth() / 2.0D, getBattleFieldHeight() / 2.0D);
/*     */   }
/*     */ 
/*     */   protected void adjustRobotCommands()
/*     */   {
/* 118 */     switch ($SWITCH_TABLE$dmh$robocode$robot$BlackDeathMk7$BattleModeType()[this.battleMode.ordinal()]) {
/*     */     case 1:
/* 120 */       operateStartMode();
/* 121 */       break;
/*     */     case 2:
/* 123 */       operateDefendMode();
/* 124 */       break;
/*     */     case 3:
/* 126 */       operateAttackMode();
/* 127 */       break;
/*     */     case 4:
/* 129 */       operateSearchMode();
/* 130 */       break;
/*     */     case 5:
/* 132 */       operateEscapeMode();
/*     */     }
/*     */ 
/* 136 */     if ((colourScheme == ColourScheme.AIMING_STRATEGY) && (this.gunnerCommand.getAimingStrategy() != null)) {
/* 137 */       setDebugColour(this.gunnerCommand.getAimingStrategy().getDebugColour());
/*     */     }
/* 139 */     this.closestNewBulletDistance = 1200.0D;
/*     */   }
/*     */ 
/*     */   public String getShootingCategory(EnemyRobot enemy, double bulletSpeed)
/*     */   {
/* 145 */     double distance = Geometry.getDistanceBetweenLocations(getLocation(), enemy.getLatestRadarObservation().getLocation());
/* 146 */     if (distance < 200.0D)
/* 147 */       return "SHORT";
/* 148 */     if (distance < 400.0D) {
/* 149 */       return "MEDIUM";
/*     */     }
/* 151 */     return "LONG";
/*     */   }
/*     */ 
/*     */   public void onHitByBullet(HitByBulletEvent event)
/*     */   {
/* 161 */     RadarObservation enemyObservation = enemies.getEnemy(event.getName()).getLatestRadarObservation();
/*     */ 
/* 163 */     if ((enemyObservation != null) && (Geometry.getDistanceBetweenLocations(getLocation(), enemyObservation.getLocation()) > 150.0D)) {
/* 164 */       this.navigatorCommand.setWiggleFactor(Math.min(this.navigatorCommand.getWiggleFactor() + 10, 20));
/* 165 */       this.navigatorCommand.setWiggleExpiry(getTime() + 100L);
/*     */     }
/*     */ 
/* 168 */     super.onHitByBullet(event);
/*     */   }
/*     */ 
/*     */   public void onEnemyHasFired(EnemyRobot enemy, double bulletPower)
/*     */   {
/* 173 */     super.onEnemyHasFired(enemy, bulletPower);
/*     */ 
/* 175 */     double newBulletDistance = Geometry.getDistanceBetweenLocations(getLocation(), enemy.getLatestRadarObservation().getLocation());
/* 176 */     this.closestNewBulletDistance = Math.min(newBulletDistance, this.closestNewBulletDistance);
/*     */   }
/*     */ 
/*     */   public void onRoundEnded(RoundEndedEvent event)
/*     */   {
/* 182 */     super.onRoundEnded(event);
/* 183 */     GunnerBlastEnemy.displayAimingDebugStats();
/*     */   }
/*     */ 
/*     */   public void onBattleEnded(BattleEndedEvent event)
/*     */   {
/* 188 */     super.onBattleEnded(event);
/*     */   }
/*     */ 
/*     */   public void setPanicColours()
/*     */   {
/* 193 */     colourScheme = ColourScheme.PANIC;
/* 194 */     super.setPanicColours();
/*     */   }
/*     */ 
/*     */   public void onDeath(DeathEvent event)
/*     */   {
/* 199 */     super.onDeath(event);
/* 200 */     if (this.haveAttacked)
/* 201 */       aggressionFactor -= 0.1D;
/*     */   }
/*     */ 
/*     */   public void onRobotDeath(RobotDeathEvent event)
/*     */   {
/* 207 */     super.onRobotDeath(event);
/*     */ 
/* 211 */     if (this.haveAttacked)
/* 212 */       switch (getOthers()) {
/*     */       case 0:
/* 214 */         aggressionFactor += 0.05D;
/* 215 */         break;
/*     */       case 1:
/* 217 */         aggressionFactor += 0.02D;
/* 218 */         break;
/*     */       case 2:
/* 220 */         aggressionFactor += 0.01D;
/* 221 */         break;
/*     */       }
/*     */   }
/*     */ 
/*     */   public double getEnemyLikelyToBeAimingForUs(EnemyRobot enemy)
/*     */   {
/* 230 */     EnemySet recentEnemies = getEnemiesSeenRecently();
/* 231 */     double enemyDistance = Geometry.getDistanceBetweenLocations(getLocation(), enemy.getLatestRadarObservation().getLocation());
/* 232 */     if ((enemyDistance <= 200.0D) || (enemy == recentEnemies.getClosestLiveEnemy(getLocation()))) {
/* 233 */       return 100.0D;
/*     */     }
/* 235 */     return 100 / recentEnemies.getNumberOfEnemies();
/*     */   }
/*     */ 
/*     */   protected void displayDebugInfoPerRound()
/*     */   {
/* 241 */     super.displayDebugInfoPerRound();
/* 242 */     if (displayEndOfRoundStats)
/* 243 */       enemies.displayDebugEndOfRound();
/*     */   }
/*     */ 
/*     */   protected void displayDebugInfoPerTurn()
/*     */   {
/* 250 */     super.displayDebugInfoPerTurn();
/*     */   }
/*     */ 
/*     */   public void onPaint(Graphics2D g)
/*     */   {
/* 255 */     if (paintAllowed) {
/* 256 */       if (this.navigatorCommand != null) {
/* 257 */         this.navigatorCommand.paint(g);
/*     */       }
/* 259 */       if (this.radarCommand != null) {
/* 260 */         this.radarCommand.paint(g);
/*     */       }
/* 262 */       if (this.gunnerCommand != null) {
/* 263 */         this.gunnerCommand.paint(g);
/*     */       }
/* 265 */       enemies.paint(g, Color.YELLOW, Color.BLUE, 35, getTime());
/* 266 */       for (DangerousBullet bullet : getDangerousBullets())
/* 267 */         bullet.paint(g, getTime());
/*     */     }
/*     */   }
/*     */ 
/*     */   private void initializeStartMode()
/*     */   {
/* 284 */     this.battleMode = BattleModeType.START;
/* 285 */     setColoursForStartMode();
/* 286 */     navigateToNearestCorner();
/*     */ 
/* 288 */     this.gunnerCommand = new GunnerDoNothing();
/* 289 */     this.radarCommand = new RadarFullScan(this);
/* 290 */     this.timeModeStarted = getTime();
/*     */   }
/*     */ 
/*     */   private void operateStartMode() {
/* 294 */     if (this.radarCommand.isDone()) {
/* 295 */       initializeDefendMode();
/* 296 */       return;
/*     */     }
/*     */   }
/*     */ 
/*     */   private void setColoursForStartMode()
/*     */   {
/* 302 */     switch ($SWITCH_TABLE$dmh$robocode$robot$BlackDeathMk7$ColourScheme()[colourScheme.ordinal()]) {
/*     */     case 1:
/*     */     case 3:
/* 305 */       setAllColors(Color.black);
/* 306 */       setBulletColor(Color.red);
/* 307 */       break;
/*     */     case 2:
/* 310 */       setBodyColor(Color.black);
/* 311 */       setRadarColor(Color.black);
/* 312 */       setDebugColour(Color.blue);
/* 313 */       break;
/*     */     case 4:
/*     */     default:
/* 317 */       setPanicColours();
/*     */     }
/*     */   }
/*     */ 
/*     */   private void navigateToNearestCorner() {
/* 322 */     if (getX() < getBattleFieldWidth() / 2.0D) {
/* 323 */       if (getY() < getBattleFieldHeight() / 2.0D)
/* 324 */         this.navigatorCommand = new NavigateToLocation(this.startBottomLeft, this);
/*     */       else {
/* 326 */         this.navigatorCommand = new NavigateToLocation(this.startTopLeft, this);
/*     */       }
/*     */     }
/* 329 */     else if (getY() < getBattleFieldHeight() / 2.0D)
/* 330 */       this.navigatorCommand = new NavigateToLocation(this.startBottomRight, this);
/*     */     else
/* 332 */       this.navigatorCommand = new NavigateToLocation(this.startTopRight, this);
/*     */   }
/*     */ 
/*     */   private void initializeDefendMode()
/*     */   {
/* 348 */     this.battleMode = BattleModeType.DEFEND;
/* 349 */     this.currentEnemyTarget = null;
/* 350 */     setColoursForDefendMode();
/* 351 */     setupDefensiveFireWithRadar();
/* 352 */     navigateToSafePlace();
/* 353 */     this.timeModeStarted = getTime();
/*     */   }
/*     */ 
/*     */   private void operateDefendMode() {
/* 357 */     if (underCloseAttack()) {
/* 358 */       initializeEscapeMode();
/* 359 */       return;
/*     */     }
/* 361 */     if (safeToAttackNow()) {
/* 362 */       initializeAttackMode();
/* 363 */       return;
/*     */     }
/* 365 */     if ((getGunNotFiredPeriod() > 0) && 
/* 366 */       (getGunNotFiredPeriod() % 20 == 0) && 
/* 367 */       (enemies.getNumberOfLiveEnemiesSeenSince(getTime()) < getOthers())) {
/* 368 */       this.radarCommand = new RadarFullScan(this);
/*     */     }
/* 370 */     if (getJustFired()) {
/* 371 */       this.radarCommand = new RadarFullScan(this);
/*     */     }
/* 373 */     if ((this.radarCommand.isDone()) || (this.gunnerCommand.isDone())) {
/* 374 */       setupDefensiveFireWithRadar();
/*     */     }
/* 376 */     if ((this.navigatorCommand.isDone()) || (this.closestNewBulletDistance < 250.0D)) {
/* 377 */       navigateToSafePlace();
/*     */     }
/* 379 */     if ((enemies.getNumberOfLiveEnemiesSeenSince(getTime() - 10L) == 0) && 
/* 380 */       (getDangerousBullets().size() == 0) && 
/* 381 */       (getTime() - this.timeModeStarted > 30L)) {
/* 382 */       initializeSearchMode();
/* 383 */       return;
/*     */     }
/*     */   }
/*     */ 
/*     */   private void setColoursForDefendMode() {
/* 388 */     if (colourScheme == ColourScheme.ROBOT_STATE)
/* 389 */       setDebugColour(Color.yellow);
/*     */   }
/*     */ 
/*     */   private void navigateToSafePlace()
/*     */   {
/* 428 */     int granularity = 5;
/* 429 */     double rootBearing = getHeading() + 10.0D;
/* 430 */     double bestBearing = rootBearing;
/* 431 */     double bestRating = -9999999.0D;
/* 432 */     double bestVelocity = 0.0D;
/* 433 */     double bestDistance = 0.0D;
/*     */ 
/* 436 */     for (double velocity = 8.0D; velocity > 2.0D; velocity -= 3.0D) {
/* 437 */       for (double bearingOffset = 0.0D; bearingOffset < 360.0D; bearingOffset += 45.0D) {
/* 438 */         double distance = velocity * 15.0D;
/* 439 */         double rating = getTravelSafetyRating(rootBearing + bearingOffset, distance, granularity, velocity);
/* 440 */         if (rating > bestRating) {
/* 441 */           bestRating = rating;
/* 442 */           bestBearing = rootBearing + bearingOffset;
/* 443 */           bestVelocity = velocity;
/* 444 */           bestDistance = distance;
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 452 */     double distance = bestDistance / 2.0D;
/* 453 */     granularity = 2;
/* 454 */     rootBearing = bestBearing;
/* 455 */     bestRating = -999999.0D;
/*     */ 
/* 457 */     for (double bearingOffset = -4.0D; bearingOffset < 4.0D; bearingOffset += 2.0D) {
/* 458 */       double rating = getTravelSafetyRating(rootBearing + bearingOffset, distance, granularity, bestVelocity);
/* 459 */       if (rating > bestRating) {
/* 460 */         bestRating = rating;
/* 461 */         bestBearing = rootBearing + bearingOffset;
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 467 */     Location targetLocation = Geometry.getLocationAtBearing(getLocation(), bestBearing, distance);
/* 468 */     this.navigatorCommand = new NavigateToLocation(targetLocation, this);
/*     */   }
/*     */ 
/*     */   private double getTravelSafetyRating(double bearing, double distance, int granularity, double velocity)
/*     */   {
/* 498 */     Map closestHits = new HashMap();
/* 499 */     Location target = Geometry.getLocationAtBearing(getLocation(), bearing, distance);
/* 500 */     if (target.getHowCloseToEdgeOfBattlefield(BattleConstants.getInstance().getBattlefieldWidth(), BattleConstants.getInstance().getBattlefieldHeight()) < getWidth() + getHeight()) {
/* 501 */       return -999999.0D;
/*     */     }
/*     */ 
/* 504 */     RobotMovementSimulator mySimulator = new RobotMovementSimulator(getLocation(), getHeading(), getVelocity(), getTime());
/* 505 */     NavigateToLocation navigator = new NavigateToLocation(target, mySimulator, velocity);
/* 506 */     int scoreFreq = 0;
/* 507 */     while (!navigator.isDone()) {
/* 508 */       navigator.executed();
/* 509 */       mySimulator.takeTurn(navigator.getRightTurn(), navigator.getAhead(), navigator.getVelocity());
/* 510 */       scoreFreq++;
/* 511 */       if (scoreFreq == granularity) {
/* 512 */         scoreFreq = 0;
/* 513 */         for (DangerousBullet bullet : getDangerousBullets()) {
/* 514 */           double howClose = Geometry.getDistanceBetweenLocations(mySimulator.getLocation(), bullet.getLocationAtTime(mySimulator.getTime()));
/* 515 */           Double existingHowClose = (Double)closestHits.get(bullet);
/* 516 */           if ((existingHowClose == null) || (howClose < existingHowClose.doubleValue())) {
/* 517 */             closestHits.put(bullet, Double.valueOf(howClose));
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/* 522 */     double rating = 0.0D;
/* 523 */     for (Map.Entry entry : closestHits.entrySet()) {
/* 524 */       rating -= getDamageCausedByDangerousBullet((DangerousBullet)entry.getKey(), ((Double)entry.getValue()).doubleValue());
/*     */     }
/* 526 */     return rating + getMotionScoreAdjustment(getEnergy() / 50.0D, velocity, bearing) + getLocationSafetyRating(target);
/*     */   }
/*     */ 
/*     */   private double getMotionScoreAdjustment(double maxScore, double velocity, double bearing)
/*     */   {
/* 532 */     double weighting = 0.0D;
/* 533 */     double maxPossibleWeighting = 6.0D;
/*     */ 
/* 543 */     double changeOfHeading = Math.abs(Geometry.getRelativeBearing(bearing, getHeading()));
/* 544 */     double changeOfVelocity = Math.abs(getVelocity() - velocity);
/*     */ 
/* 546 */     if (changeOfHeading >= 145.0D)
/* 547 */       weighting += 3.0D;
/* 548 */     else if (changeOfHeading >= 45.0D) {
/* 549 */       weighting += 1.0D;
/*     */     }
/*     */ 
/* 552 */     weighting += Math.min(3.0D, changeOfVelocity / 2.0D);
/*     */ 
/* 554 */     return maxScore * weighting / maxPossibleWeighting;
/*     */   }
/*     */ 
/*     */   private double getLocationSafetyRating(Location possibleLocation)
/*     */   {
/* 559 */     EnemySet enemiesSeenRecently = getEnemiesSeenRecently();
/*     */ 
/* 561 */     if (enemiesSeenRecently.getNumberOfEnemies() == 1) {
/* 562 */       return getSingleEnemyLocationSafetyRating(possibleLocation, enemiesSeenRecently.getClosestLiveEnemy(possibleLocation));
/*     */     }
/* 564 */     return getMeleeLocationSafetyRating(possibleLocation, enemiesSeenRecently);
/*     */   }
/*     */ 
/*     */   private double getSingleEnemyLocationSafetyRating(Location possibleLocation, EnemyRobot enemy)
/*     */   {
/* 570 */     double distance = Geometry.getDistanceBetweenLocations(possibleLocation, enemy.getAverageLocation());
/*     */ 
/* 572 */     if (distance < 200.0D)
/* 573 */       return (distance - 200.0D) / 10.0D - 8.0D;
/* 574 */     if (distance > 600.0D) {
/* 575 */       return (600.0D - distance) / 50.0D;
/*     */     }
/* 577 */     return 0.0D;
/*     */   }
/*     */ 
/*     */   private double getMeleeLocationSafetyRating(Location possibleLocation, EnemySet enemiesSeenRecently)
/*     */   {
/* 584 */     double dangerRating = enemiesSeenRecently.getTotalDangerEnergyWithGravity(600.0D, possibleLocation);
/*     */ 
/* 588 */     double targetRating = enemiesSeenRecently.getBestDoubleTargetEnergyWithGravity(400.0D, possibleLocation);
/*     */ 
/* 590 */     if (possibleLocation.getHowCloseToEdgeOfBattlefield(getBattleFieldWidth(), getBattleFieldHeight()) < edgeSafetyMargin) {
/* 591 */       dangerRating += enemies.getTotalDangerEnergy();
/* 592 */       targetRating /= 2.0D;
/*     */     }
/*     */ 
/* 597 */     return targetRating - dangerRating;
/*     */   }
/*     */ 
/*     */   private void setupDefensiveFireWithRadar()
/*     */   {
/* 603 */     EnemyRobot closestEnemy = getEnemiesSeenRecently().getBestShootingTarget(getLocation());
/* 604 */     if (closestEnemy != this.currentEnemyTarget) {
/* 605 */       this.currentEnemyTarget = closestEnemy;
/* 606 */       setGunnerToDefend(this.currentEnemyTarget);
/*     */     }
/* 608 */     setRadarToNarrowTrackEnemy(this.currentEnemyTarget);
/*     */   }
/*     */ 
/*     */   private void setGunnerToDefend(EnemyRobot enemy) {
/* 612 */     if (enemy != null)
/*     */     {
/* 614 */       double fullPowerSuccess = Math.max(10, 20 - getEnemiesSeenRecently().getNumberOfEnemies() * 4);
/*     */ 
/* 617 */       int remainingBullets = 5;
/*     */ 
/* 622 */       double shootingPowerDecayFactor = 1.0D;
/*     */ 
/* 625 */       this.gunnerCommand = new GunnerBlastEnemy(this, enemy, remainingBullets, fullPowerSuccess, isLearningToAimAllowed, shootingPowerDecayFactor);
/*     */     }
/*     */     else {
/* 628 */       this.gunnerCommand = new GunnerDoNothing();
/*     */     }
/*     */   }
/*     */ 
/*     */   private boolean safeToAttackNow()
/*     */   {
/* 639 */     double dangerEnergy = getEnemiesSeenRecently().getTotalDangerEnergy();
/* 640 */     double maxDamageTwoBullets = Rules.getBulletDamage(3.0D) * 2.0D;
/* 641 */     return (dangerEnergy < getEnergy() * aggressionFactor) && (
/* 642 */       dangerEnergy < (getEnergy() - maxDamageTwoBullets) * aggressionFactor * 1.3D);
/*     */   }
/*     */ 
/*     */   private void initializeAttackMode()
/*     */   {
/* 653 */     this.battleMode = BattleModeType.ATTACK;
/* 654 */     setColoursForAttackMode();
/* 655 */     setupEnemyFullAttack();
/* 656 */     this.timeModeStarted = getTime();
/* 657 */     this.haveAttacked = true;
/*     */   }
/*     */ 
/*     */   private void operateAttackMode()
/*     */   {
/* 665 */     if (underCloseAttack()) {
/* 666 */       initializeEscapeMode();
/* 667 */       return;
/*     */     }
/* 669 */     if (this.gunnerCommand.isDone()) {
/* 670 */       setupEnemyFullAttack();
/*     */     }
/* 672 */     if ((enemies.getNumberOfLiveEnemiesSeenSince(getTime() - 10L) == 0) && 
/* 673 */       (getTime() - this.timeModeStarted > 30L)) {
/* 674 */       initializeSearchMode();
/* 675 */       return;
/*     */     }
/* 677 */     if (shouldAbortAttack()) {
/* 678 */       initializeDefendMode();
/* 679 */       return;
/*     */     }
/* 681 */     turnOnWiggleCloseToDangerousBullets(250.0D);
/* 682 */     turnOffWiggleCloseToEnemy(150.0D);
/*     */   }
/*     */ 
/*     */   private void setColoursForAttackMode() {
/* 686 */     if (colourScheme == ColourScheme.ROBOT_STATE)
/* 687 */       setDebugColour(Color.red);
/*     */   }
/*     */ 
/*     */   private void setupEnemyFullAttack()
/*     */   {
/* 693 */     EnemyRobot closestEnemy = getEnemiesSeenRecently().getBestShootingTarget(getLocation());
/*     */ 
/* 696 */     setGunnerToAttackEnemy(closestEnemy);
/* 697 */     setRadarToNarrowTrackEnemy(closestEnemy);
/* 698 */     setNavigatorToChaseEnemy(closestEnemy);
/*     */   }
/*     */ 
/*     */   private boolean shouldAbortAttack() {
/* 702 */     return getEnemiesSeenRecently().getTotalDangerEnergy() > getEnergy() * 1.3D * aggressionFactor;
/*     */   }
/*     */ 
/*     */   private void turnOffWiggleCloseToEnemy(double closeDistance)
/*     */   {
/* 708 */     if ((this.currentEnemyTarget != null) && (this.navigatorCommand.getWiggleFactor() > 0)) {
/* 709 */       double enemyDistance = Geometry.getDistanceBetweenLocations(getLocation(), this.currentEnemyTarget.getLatestRadarObservation().getLocation());
/*     */ 
/* 711 */       if (enemyDistance <= closeDistance)
/* 712 */         this.navigatorCommand.setWiggleFactor(0);
/*     */     }
/*     */   }
/*     */ 
/*     */   private void turnOnWiggleCloseToDangerousBullets(double dangerDistance)
/*     */   {
/* 719 */     int minimumWiggle = (int)getDamageFromBulletsAtFutureLocation(getLocation(), dangerDistance);
/*     */ 
/* 721 */     if (minimumWiggle > 0) {
/* 722 */       this.navigatorCommand.setWiggleFactor(minimumWiggle);
/* 723 */       this.navigatorCommand.setWiggleExpiry(getTime() + 100L);
/*     */     }
/*     */   }
/*     */ 
/*     */   private void initializeEscapeMode()
/*     */   {
/* 736 */     this.battleMode = BattleModeType.ESCAPE;
/* 737 */     setColoursForEscapeMode();
/* 738 */     this.timeModeStarted = getTime();
/*     */ 
/* 740 */     this.navigatorCommand = new NavigateToLocation(getEscapeLocation(), this);
/*     */ 
/* 742 */     EnemyRobot target = getEscapeModeShootingTarget();
/* 743 */     setGunnerToAttackEnemyMaxPower(target);
/* 744 */     setRadarToNarrowTrackEnemy(target);
/*     */   }
/*     */ 
/*     */   private void operateEscapeMode()
/*     */   {
/* 749 */     if (!underCloseAttack()) {
/* 750 */       initializeDefendMode();
/* 751 */       return;
/*     */     }
/*     */ 
/* 755 */     if ((getGunHeat() / getGunCoolingRate() > 3.0D) || (this.gunnerCommand.isDone())) {
/* 756 */       EnemyRobot target = getEscapeModeShootingTarget();
/* 757 */       if (target != this.gunnerCommand.getEnemyTarget()) {
/* 758 */         setGunnerToAttackEnemyMaxPower(target);
/* 759 */         setRadarToNarrowTrackEnemy(target);
/*     */       }
/*     */     }
/*     */ 
/* 763 */     if ((this.navigatorCommand.isDone()) || ((getTime() - this.timeModeStarted) % 5L == 0L))
/* 764 */       this.navigatorCommand = new NavigateToLocation(getEscapeLocation(), this);
/*     */   }
/*     */ 
/*     */   private void setColoursForEscapeMode()
/*     */   {
/* 769 */     if (colourScheme == ColourScheme.ROBOT_STATE)
/* 770 */       setDebugColour(Color.magenta);
/*     */   }
/*     */ 
/*     */   private Location getEscapeLocation()
/*     */   {
/* 775 */     double totalWeight = 0.0D;
/* 776 */     BearingSelector bearingSelector = new BearingSelector(10);
/* 777 */     for (EnemyRobot enemy : enemies.getAllLiveEnemiesCloserThan(getLocation(), 150.0D)) {
/* 778 */       RadarObservation observation = enemy.getLatestRadarObservation();
/*     */ 
/* 781 */       double weighting = Math.abs(observation.getVelocity()) / 8.0D * 2.0D + 1.0D;
/* 782 */       double bearing = Geometry.getBearingBetweenLocations(getLocation(), observation.getLocation());
/* 783 */       bearingSelector.add(bearing, weighting);
/* 784 */       totalWeight += weighting;
/*     */     }
/* 786 */     if (getLocation().getX() < 100.0D) {
/* 787 */       bearingSelector.add(270.0D, totalWeight);
/*     */     }
/* 789 */     if (getLocation().getY() < 100.0D) {
/* 790 */       bearingSelector.add(180.0D, totalWeight);
/*     */     }
/* 792 */     if (getLocation().getX() > getBattleFieldWidth() - 100.0D) {
/* 793 */       bearingSelector.add(90.0D, totalWeight);
/*     */     }
/* 795 */     if (getLocation().getY() > getBattleFieldHeight() - 100.0D) {
/* 796 */       bearingSelector.add(0.0D, totalWeight);
/*     */     }
/* 798 */     return Geometry.getLocationAtBearing(getLocation(), bearingSelector.getLowScoreBearing(), 50.0D);
/*     */   }
/*     */ 
/*     */   private EnemyRobot getEscapeModeShootingTarget()
/*     */   {
/* 813 */     return enemies.getClosestLiveEnemy(getLocation());
/*     */   }
/*     */ 
/*     */   private boolean underCloseAttack() {
/* 817 */     EnemySet veryCloseEnemies = enemies.getAllLiveEnemiesCloserThan(getLocation(), 150.0D);
/* 818 */     return veryCloseEnemies.getNumberOfEnemies() >= 1;
/*     */   }
/*     */ 
/*     */   private void initializeSearchMode()
/*     */   {
/* 832 */     Location[] searchPattern = { this.battleCentre, this.searchBottomLeft, this.searchTopLeft, this.searchTopRight, this.searchBottomRight, this.battleCentre };
/*     */ 
/* 834 */     this.battleMode = BattleModeType.SEARCH;
/* 835 */     setColoursForSearchMode();
/* 836 */     this.timeModeStarted = getTime();
/*     */ 
/* 838 */     this.navigatorCommand = new NavigateToLocations(searchPattern, this);
/* 839 */     this.gunnerCommand = new GunnerDoNothing();
/* 840 */     this.radarCommand = new RadarFullScan(this);
/*     */   }
/*     */ 
/*     */   private void operateSearchMode() {
/* 844 */     if (this.radarCommand.isDone()) {
/* 845 */       this.radarCommand = new RadarFullScan(this);
/*     */ 
/* 847 */       if (enemies.getNumberOfLiveEnemiesSeenSince(getTime() - 10L) > 0)
/* 848 */         initializeDefendMode();
/*     */     }
/*     */   }
/*     */ 
/*     */   private void setColoursForSearchMode()
/*     */   {
/* 854 */     if (colourScheme == ColourScheme.ROBOT_STATE)
/* 855 */       setDebugColour(Color.green);
/*     */   }
/*     */ 
/*     */   private void setGunnerToAttackEnemy(EnemyRobot enemy)
/*     */   {
/* 865 */     if (enemy != null)
/*     */     {
/* 867 */       double fullPowerSuccess = Math.max(10, 20 - getEnemiesSeenRecently().getNumberOfEnemies() * 4);
/*     */ 
/* 869 */       this.gunnerCommand = new GunnerBlastEnemy(this, enemy, 5, fullPowerSuccess, isLearningToAimAllowed, 0.9D);
/*     */     }
/*     */     else {
/* 872 */       this.gunnerCommand = new GunnerDoNothing();
/*     */     }
/*     */   }
/*     */ 
/*     */   private void setGunnerToAttackEnemyMaxPower(EnemyRobot enemy) {
/* 877 */     if (enemy != null)
/*     */     {
/* 879 */       this.gunnerCommand = new GunnerBlastEnemy(this, enemy, 5, 0.0D, isLearningToAimAllowed, 1.0D);
/*     */     }
/*     */     else
/* 882 */       this.gunnerCommand = new GunnerDoNothing();
/*     */   }
/*     */ 
/*     */   private void setRadarToNarrowTrackEnemy(EnemyRobot enemy)
/*     */   {
/* 887 */     if (enemy != null)
/*     */     {
/* 889 */       this.radarCommand = new RadarNarrowLock(enemy, this);
/*     */     }
/* 891 */     else this.radarCommand = new RadarFullScan(this);
/*     */   }
/*     */ 
/*     */   private void setNavigatorToChaseEnemy(EnemyRobot enemy)
/*     */   {
/* 897 */     if (enemy != null)
/*     */     {
/* 899 */       double safeDistanceFromEnemy = Math.min((getBattleFieldHeight() + getBattleFieldWidth()) / 6.0D, 200.0D);
/*     */ 
/* 902 */       EnemyTargetAlgorithm targetAlgorithm = new EnemyTargetDefault(enemy, this, safeDistanceFromEnemy);
/*     */ 
/* 904 */       this.navigatorCommand = new NavigateToEnemy(enemy, this, targetAlgorithm);
/*     */     }
/*     */   }
/*     */ 
/*     */   private EnemySet getEnemiesSeenRecently() {
/* 909 */     return enemies.getLiveEnemiesSeenSince(getTime() - 50L);
/*     */   }
/*     */ 
/*     */   private double getDamageFromBulletsAtFutureLocation(Location location, double safeDistance)
/*     */   {
/* 914 */     long myTravelTime = ()(Geometry.getDistanceBetweenLocations(getLocation(), location) / 8.0D);
/* 915 */     long futureTime = getTime() + myTravelTime;
/*     */ 
/* 917 */     double totalDamage = 0.0D;
/* 918 */     for (DangerousBullet bullet : getDangerousBullets()) {
/* 919 */       double distance = Geometry.getDistanceBetweenLocations(location, bullet.getLocationAtTime(futureTime));
/* 920 */       totalDamage += getDamageCausedByDangerousBullet(bullet, distance);
/*     */     }
/* 922 */     return totalDamage;
/*     */   }
/*     */ 
/*     */   private double getDamageCausedByDangerousBullet(DangerousBullet bullet, double distanceFromUs) {
/* 926 */     double safeBulletDistance = Geometry.getDistanceBetweenLocations(getLocation(), bullet.getFiredFromLocation()) / 3.0D;
/* 927 */     if (distanceFromUs <= safeBulletDistance) {
/* 928 */       return bullet.getDamage() * (1.0D - distanceFromUs / safeBulletDistance) * bullet.getLikelyToBeAimingForUs() / 100.0D;
/*     */     }
/* 930 */     return 0.0D;
/*     */   }
/*     */ 
/*     */   private void setDebugColour(Color colour)
/*     */   {
/* 935 */     setGunColor(colour);
/* 936 */     setBulletColor(colour);
/* 937 */     setScanColor(colour);
/*     */   }
/*     */ 
/*     */   private static enum BattleModeType
/*     */   {
/*  72 */     START, DEFEND, ATTACK, SEARCH, ESCAPE;
/*     */   }
/*     */ 
/*     */   private static enum ColourScheme
/*     */   {
/*  45 */     BLACK_DEATH, ROBOT_STATE, AIMING_STRATEGY, PANIC;
/*     */   }
/*     */ }

/* Location:           E:\naval-robocode\sample\dmh.robocode.robot.BlackDeathMk7_7.0.jar
 * Qualified Name:     dmh.robocode.robot.BlackDeathMk7
 * JD-Core Version:    0.6.2
 */