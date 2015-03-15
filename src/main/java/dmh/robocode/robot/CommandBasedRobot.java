/*     */ package dmh.robocode.robot;
/*     */ 
/*     */ import dmh.robocode.bullet.DangerousBullet;
/*     */ import dmh.robocode.data.AverageLocation;
/*     */ import dmh.robocode.data.BattleConstants;
/*     */ import dmh.robocode.data.Location;
/*     */ import dmh.robocode.data.RadarObservation;
/*     */ import dmh.robocode.data.RoundSummary;
/*     */ import dmh.robocode.data.ShotAtEnemy;
/*     */ import dmh.robocode.data.ShotAtEnemy.ShotStatus;
/*     */ import dmh.robocode.data.ShotByEnemy;
/*     */ import dmh.robocode.enemy.EnemyRobot;
/*     */ import dmh.robocode.enemy.EnemySet;
/*     */ import dmh.robocode.gunner.GunnerCommand;
/*     */ import dmh.robocode.gunner.aiming.AimingStrategy;
/*     */ import dmh.robocode.navigator.NavigatorCommand;
/*     */ import dmh.robocode.radar.RadarCommand;
/*     */ import dmh.robocode.simulate.SimulateableRobot;
/*     */ import dmh.robocode.utils.Geometry;
/*     */ import java.awt.Color;
/*     */ import java.io.PrintStream;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.LinkedList;
/*     */ import java.util.List;
/*     */ import java.util.ListIterator;
/*     */ import robocode.AdvancedRobot;
/*     */ import robocode.BattleEndedEvent;
/*     */ import robocode.Bullet;
/*     */ import robocode.BulletHitBulletEvent;
/*     */ import robocode.BulletHitEvent;
/*     */ import robocode.BulletMissedEvent;
/*     */ import robocode.DeathEvent;
/*     */ import robocode.HitByBulletEvent;
/*     */ import robocode.HitRobotEvent;
/*     */ import robocode.HitWallEvent;
/*     */ import robocode.RobotDeathEvent;
/*     */ import robocode.RoundEndedEvent;
/*     */ import robocode.Rules;
/*     */ import robocode.ScannedRobotEvent;
/*     */ import robocode.SkippedTurnEvent;
/*     */ import robocode.WinEvent;
/*     */ 
/*     */ public abstract class CommandBasedRobot extends AdvancedRobot
/*     */   implements SimulateableRobot
/*     */ {
/*  46 */   private static double myAverageLocationWeightingFactor = 1.05D;
/*     */ 
/*  48 */   private static boolean displayFinalDebugStats = true;
/*     */   protected NavigatorCommand navigatorCommand;
/*     */   protected RadarCommand radarCommand;
/*     */   protected GunnerCommand gunnerCommand;
/*  53 */   protected static EnemySet enemies = new EnemySet();
/*     */ 
/*  55 */   private boolean rammedEnemy = false;
/*  56 */   private boolean hitByBullet = false;
/*     */   private HashMap<Bullet, ShotAtEnemy> liveShotsAtEnemy;
/*     */   private boolean justFired;
/*     */   private List<DangerousBullet> dangerousBullets;
/*     */   private Location myLocation;
/*     */   private int gunNotFiredPeriod;
/*     */   private AverageLocation averageLocation;
/*  66 */   private static List<RoundSummary> roundSummaries = new LinkedList();
/*     */   private RoundSummary roundSummary;
/*     */ 
/*     */   protected void doNotDisplayFinalDebugStats()
/*     */   {
/*  71 */     displayFinalDebugStats = false;
/*     */   }
/*     */ 
/*     */   public Location getLocation()
/*     */   {
/*  76 */     return this.myLocation;
/*     */   }
/*     */ 
/*     */   public int getGunNotFiredPeriod() {
/*  80 */     return this.gunNotFiredPeriod;
/*     */   }
/*     */ 
/*     */   public final boolean getHitByBullet() {
/*  84 */     return this.hitByBullet;
/*     */   }
/*     */ 
/*     */   public final boolean getRammedEnemy() {
/*  88 */     return this.rammedEnemy;
/*     */   }
/*     */ 
/*     */   public final EnemySet getEnemies() {
/*  92 */     return enemies;
/*     */   }
/*     */ 
/*     */   public final boolean getJustFired() {
/*  96 */     return this.justFired;
/*     */   }
/*     */ 
/*     */   List<DangerousBullet> getDangerousBullets() {
/* 100 */     return this.dangerousBullets;
/*     */   }
/*     */ 
/*     */   public void onHitWall(HitWallEvent event)
/*     */   {
/* 105 */     this.navigatorCommand.reverseDirection();
/*     */   }
/*     */ 
/*     */   public void onRobotDeath(RobotDeathEvent event)
/*     */   {
/* 110 */     enemies.getEnemy(event.getName()).processRobotDeathEvent(event.getName());
/*     */   }
/*     */ 
/*     */   public void onScannedRobot(ScannedRobotEvent event)
/*     */   {
/* 117 */     this.myLocation = new Location(getX(), getY());
/*     */ 
/* 119 */     EnemyRobot enemy = enemies.getEnemy(event.getName());
/* 120 */     RadarObservation previousObservation = enemy.getLatestRadarObservation();
/* 121 */     RadarObservation radarObservation = new RadarObservation(event, this.myLocation, getHeading(), previousObservation);
/* 122 */     enemy.processScannedRobotEvent(event.getName(), radarObservation);
/*     */ 
/* 125 */     if ((previousObservation != null) && (previousObservation.getTimeSeen() == radarObservation.getTimeSeen() - 1L)) {
/* 126 */       double energyChange = radarObservation.getEnergy() - previousObservation.getEnergy();
/* 127 */       if ((energyChange < 0.0D) && (energyChange >= -3.0D))
/* 128 */         onEnemyHasFired(enemy, -energyChange);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void onEnemyHasFired(EnemyRobot enemy, double bulletPower)
/*     */   {
/* 135 */     Location firedFrom = enemy.getLatestRadarObservation().getLocation();
/* 136 */     double bulletTravelTime = Geometry.getDistanceBetweenLocations(firedFrom, getLocation()) / Rules.getBulletSpeed(bulletPower);
/* 137 */     Location estimatedTarget = Geometry.getLocationAtBearing(getLocation(), getHeading(), bulletTravelTime * getVelocity());
/* 138 */     double estimatedHeading = Geometry.getBearingBetweenLocations(firedFrom, estimatedTarget);
/* 139 */     DangerousBullet dangerousBullet = new DangerousBullet(firedFrom, bulletPower, estimatedHeading, getTime(), enemy, getEnemyLikelyToBeAimingForUs(enemy));
/* 140 */     this.dangerousBullets.add(dangerousBullet);
/*     */ 
/* 147 */     enemy.hasJustFired(bulletPower, getTime());
/*     */   }
/*     */ 
/*     */   public double getEnemyLikelyToBeAimingForUs(EnemyRobot enemy)
/*     */   {
/* 154 */     return 100 / getOthers();
/*     */   }
/*     */ 
/*     */   public void onHitRobot(HitRobotEvent event)
/*     */   {
/* 160 */     this.rammedEnemy = true;
/* 161 */     if (event.isMyFault())
/* 162 */       this.navigatorCommand.reverseDirection();
/*     */   }
/*     */ 
/*     */   public void onHitByBullet(HitByBulletEvent event)
/*     */   {
/* 168 */     this.hitByBullet = true;
/* 169 */     EnemyRobot enemy = enemies.getEnemy(event.getName());
/* 170 */     ShotByEnemy shotDetails = new ShotByEnemy(this, enemy, event);
/* 171 */     enemy.recordEnemyHitUs(shotDetails);
/*     */ 
/* 174 */     DangerousBullet whatHitUs = null;
/* 175 */     double hittingDistance = 0.0D;
/*     */ 
/* 178 */     double marginForError = Math.max(getWidth(), getHeight());
/* 179 */     for (DangerousBullet bullet : getDangerousBullets()) {
/* 180 */       if (bullet.isPossibleLocation(this.myLocation, getTime(), event.getPower(), marginForError, enemy)) {
/* 181 */         double distanceFromUs = Geometry.getDistanceBetweenLocations(bullet.getLocationAtTime(getTime()), this.myLocation);
/* 182 */         if ((whatHitUs == null) || (distanceFromUs < hittingDistance)) {
/* 183 */           whatHitUs = bullet;
/* 184 */           hittingDistance = distanceFromUs;
/*     */         }
/*     */       }
/*     */     }
/* 188 */     if (whatHitUs != null)
/*     */     {
/* 191 */       this.dangerousBullets.remove(whatHitUs);
/*     */     }
/*     */ 
/* 203 */     this.roundSummary.gotHit(Rules.getBulletDamage(event.getBullet().getPower()));
/*     */   }
/*     */ 
/*     */   public void onBulletHit(BulletHitEvent event)
/*     */   {
/* 217 */     ShotAtEnemy myShot = (ShotAtEnemy)this.liveShotsAtEnemy.get(event.getBullet());
/*     */ 
/* 219 */     if (myShot != null)
/*     */     {
/* 221 */       EnemyRobot intendedTarget = myShot.getEnemy();
/*     */ 
/* 223 */       if (event.getName().equals(intendedTarget.getName()))
/* 224 */         myShot.setStatus(ShotAtEnemy.ShotStatus.HIT_TARGET);
/*     */       else {
/* 226 */         myShot.setStatus(ShotAtEnemy.ShotStatus.HIT_OTHER);
/*     */       }
/*     */ 
/* 229 */       this.liveShotsAtEnemy.remove(event.getBullet());
/*     */ 
/* 231 */       this.gunnerCommand.hitEnemy(enemies.getEnemy(event.getName()));
/*     */     } else {
/* 233 */       panic("OOOOPS - No such shot!!!!");
/*     */     }
/* 235 */     if (event.getEnergy() <= 0.0D) {
/* 236 */       System.out.println("***** KILLED " + event.getName() + "*******");
/* 237 */       this.roundSummary.killedRobot();
/*     */     }
/* 239 */     this.roundSummary.bulletHit(Rules.getBulletDamage(event.getBullet().getPower()));
/*     */   }
/*     */ 
/*     */   public void onBulletMissed(BulletMissedEvent event)
/*     */   {
/* 244 */     ShotAtEnemy myShot = (ShotAtEnemy)this.liveShotsAtEnemy.get(event.getBullet());
/*     */ 
/* 246 */     if (myShot != null) {
/* 247 */       EnemyRobot intendedTarget = myShot.getEnemy();
/*     */ 
/* 249 */       if (intendedTarget.isAlive())
/* 250 */         myShot.setStatus(ShotAtEnemy.ShotStatus.MISS);
/*     */       else {
/* 252 */         myShot.setStatus(ShotAtEnemy.ShotStatus.ALREADY_DEAD);
/*     */       }
/*     */ 
/* 255 */       this.liveShotsAtEnemy.remove(event.getBullet());
/*     */     } else {
/* 257 */       panic("OOOOPS - No such shot!!!!");
/*     */     }
/*     */   }
/*     */ 
/*     */   public void onBulletHitBullet(BulletHitBulletEvent event)
/*     */   {
/* 264 */     ShotAtEnemy myShot = (ShotAtEnemy)this.liveShotsAtEnemy.get(event.getBullet());
/*     */ 
/* 266 */     if (myShot != null) {
/* 267 */       myShot.setStatus(ShotAtEnemy.ShotStatus.HIT_BULLET);
/*     */ 
/* 269 */       this.liveShotsAtEnemy.remove(event.getBullet());
/*     */ 
/* 273 */       EnemyRobot enemy = enemies.getEnemy(event.getHitBullet().getName());
/* 274 */       Location bulletLocation = new Location(event.getHitBullet().getX(), event.getHitBullet().getY());
/* 275 */       DangerousBullet whatHitUs = null;
/* 276 */       double hittingDistance = 0.0D;
/* 277 */       double marginForError = 30.0D;
/* 278 */       for (DangerousBullet bullet : getDangerousBullets()) {
/* 279 */         if (bullet.isPossibleLocation(bulletLocation, getTime(), event.getHitBullet().getPower(), marginForError, enemy)) {
/* 280 */           double distanceFromUs = Geometry.getDistanceBetweenLocations(bullet.getLocationAtTime(getTime()), this.myLocation);
/* 281 */           if ((whatHitUs == null) || (distanceFromUs < hittingDistance)) {
/* 282 */             whatHitUs = bullet;
/* 283 */             hittingDistance = distanceFromUs;
/*     */           }
/*     */         }
/*     */       }
/* 287 */       if (whatHitUs != null)
/*     */       {
/* 290 */         this.dangerousBullets.remove(whatHitUs);
/*     */       }
/*     */     }
/*     */     else
/*     */     {
/* 295 */       panic("OOOOPS - No such shot!!!!");
/*     */     }
/*     */   }
/*     */ 
/*     */   public void onSkippedTurn(SkippedTurnEvent event)
/*     */   {
/* 301 */     panic("Skipped Turn");
/*     */   }
/*     */ 
/*     */   private void panic(String errorMessage) {
/* 305 */     System.out.println("*** PANIC ***");
/* 306 */     System.out.println(errorMessage);
/* 307 */     System.out.println("*************");
/*     */ 
/* 309 */     setPanicColours();
/*     */   }
/*     */ 
/*     */   public void setPanicColours() {
/* 313 */     setAllColors(Color.RED);
/*     */   }
/*     */ 
/*     */   public void onDeath(DeathEvent event)
/*     */   {
/* 318 */     cleanupEndOfRound();
/* 319 */     this.roundSummary.setPlacing(getOthers() + 1);
/* 320 */     this.roundSummary.setEndTime(getTime());
/* 321 */     this.roundSummary.setFinalEnergy(getEnergy());
/*     */   }
/*     */ 
/*     */   public void onWin(WinEvent event)
/*     */   {
/* 326 */     super.onWin(event);
/* 327 */     this.roundSummary.setPlacing(1);
/* 328 */     this.roundSummary.setEndTime(getTime());
/* 329 */     this.roundSummary.setFinalEnergy(getEnergy());
/*     */   }
/*     */ 
/*     */   public void onRoundEnded(RoundEndedEvent event)
/*     */   {
/* 334 */     System.out.println("*** ROUND HAS ENDED ***");
/* 335 */     cleanupEndOfRound();
/*     */   }
/*     */ 
/*     */   public void onBattleEnded(BattleEndedEvent event)
/*     */   {
/* 341 */     if (displayFinalDebugStats)
/* 342 */       displayDebugInfoEndOfBattle();
/*     */   }
/*     */ 
/*     */   private void cleanupEndOfRound()
/*     */   {
/* 347 */     for (ShotAtEnemy shot : this.liveShotsAtEnemy.values())
/* 348 */       shot.setStatus(ShotAtEnemy.ShotStatus.END_ROUND);
/*     */   }
/*     */ 
/*     */   private void purgeDangerousBulletsGoneOutOfBattle()
/*     */   {
/* 353 */     ListIterator iter = this.dangerousBullets.listIterator();
/*     */ 
/* 355 */     while (iter.hasNext()) {
/* 356 */       DangerousBullet bullet = (DangerousBullet)iter.next();
/* 357 */       if (!bullet.getLocationAtTime(getTime()).isOnBattlefield(getBattleFieldWidth(), getBattleFieldHeight()))
/* 358 */         iter.remove();
/*     */     }
/*     */   }
/*     */ 
/*     */   private void purgeDangerousBulletsMovingAwayFromUs()
/*     */   {
/* 365 */     ListIterator iter = this.dangerousBullets.listIterator();
/*     */ 
/* 367 */     while (iter.hasNext()) {
/* 368 */       DangerousBullet bullet = (DangerousBullet)iter.next();
/* 369 */       double currentDistance = Geometry.getDistanceBetweenLocations(getLocation(), bullet.getLocationAtTime(getTime()));
/* 370 */       double previousDistance = Geometry.getDistanceBetweenLocations(getLocation(), bullet.getLocationAtTime(getTime() - 1L));
/* 371 */       if (currentDistance >= previousDistance + 8.0D)
/* 372 */         iter.remove();
/*     */     }
/*     */   }
/*     */ 
/*     */   public final void run()
/*     */   {
/* 382 */     setColours();
/* 383 */     setBattleConstants();
/*     */ 
/* 385 */     enemies.resetForNextRound();
/* 386 */     this.liveShotsAtEnemy = new HashMap();
/* 387 */     this.dangerousBullets = new ArrayList();
/* 388 */     this.myLocation = new Location(getX(), getY());
/* 389 */     this.averageLocation = new AverageLocation(myAverageLocationWeightingFactor);
/* 390 */     this.roundSummary = new RoundSummary();
/* 391 */     roundSummaries.add(this.roundSummary);
/* 392 */     this.roundSummary.setStartingLocation(this.myLocation);
/*     */ 
/* 394 */     informAboutThisNewRobotInstance();
/*     */ 
/* 396 */     initialiseRobotCommands();
/*     */ 
/* 398 */     double previousGunHeat = getGunHeat();
/*     */     while (true)
/*     */     {
/* 405 */       purgeDangerousBulletsGoneOutOfBattle();
/* 406 */       purgeDangerousBulletsMovingAwayFromUs();
/*     */ 
/* 409 */       this.myLocation = new Location(getX(), getY());
/* 410 */       this.averageLocation.recordLocation(this.myLocation);
/*     */ 
/* 414 */       this.justFired = (previousGunHeat < getGunHeat());
/* 415 */       if ((previousGunHeat == 0.0D) && (getGunHeat() == 0.0D))
/* 416 */         this.gunNotFiredPeriod += 1;
/*     */       else {
/* 418 */         this.gunNotFiredPeriod = 0;
/*     */       }
/* 420 */       previousGunHeat = getGunHeat();
/*     */ 
/* 425 */       adjustRobotCommands();
/*     */ 
/* 428 */       this.hitByBullet = false;
/* 429 */       this.rammedEnemy = false;
/*     */ 
/* 433 */       if (this.navigatorCommand != null) {
/* 434 */         setTurnRight(this.navigatorCommand.getRightTurn());
/* 435 */         setAhead(this.navigatorCommand.getAhead());
/* 436 */         setMaxVelocity(this.navigatorCommand.getVelocity());
/*     */       }
/*     */ 
/* 448 */       if (this.radarCommand != null) {
/* 449 */         setAdjustRadarForGunTurn(true);
/* 450 */         setAdjustRadarForRobotTurn(true);
/* 451 */         setTurnRadarRight(this.radarCommand.getRightTurn());
/*     */       }
/*     */ 
/* 456 */       if (this.gunnerCommand != null) {
/* 457 */         setAdjustGunForRobotTurn(true);
/*     */ 
/* 459 */         if ((getGunHeat() == 0.0D) && (this.gunnerCommand.getFire() > 0.0D))
/*     */         {
/* 461 */           Bullet bullet = setFireBullet(this.gunnerCommand.getFire());
/* 462 */           EnemyRobot enemy = this.gunnerCommand.getEnemyTarget();
/*     */ 
/* 464 */           if ((bullet != null) && (enemy != null)) {
/* 465 */             ShotAtEnemy myShot = new ShotAtEnemy(this, enemy, bullet.getPower(), this.gunnerCommand.getAimingStrategy());
/* 466 */             this.liveShotsAtEnemy.put(bullet, myShot);
/* 467 */             String category = getShootingCategory(enemy, bullet.getVelocity());
/* 468 */             enemy.recordMyShot(category, myShot);
/*     */           }
/*     */         }
/*     */ 
/* 472 */         setTurnGunRight(this.gunnerCommand.getRightTurn());
/*     */       }
/*     */ 
/* 477 */       execute();
/* 478 */       displayDebugInfoPerTurn();
/* 479 */       if (getTime() == 100L)
/* 480 */         this.roundSummary.setEnergyAtTime100(getEnergy());
/* 481 */       else if (getTime() == 200L) {
/* 482 */         this.roundSummary.setEnergyAtTime200(getEnergy());
/*     */       }
/*     */ 
/* 487 */       if (this.navigatorCommand != null) {
/* 488 */         this.navigatorCommand.executed();
/*     */       }
/* 490 */       if (this.radarCommand != null) {
/* 491 */         this.radarCommand.executed();
/*     */       }
/* 493 */       if (this.gunnerCommand != null)
/* 494 */         this.gunnerCommand.executed();
/*     */     }
/*     */   }
/*     */ 
/*     */   protected void adjustRobotCommands()
/*     */   {
/*     */   }
/*     */ 
/*     */   protected void initialiseRobotCommands()
/*     */   {
/*     */   }
/*     */ 
/*     */   protected void displayDebugInfoPerTurn()
/*     */   {
/*     */   }
/*     */ 
/*     */   protected void displayDebugInfoPerRound()
/*     */   {
/*     */   }
/*     */ 
/*     */   protected void displayDebugInfoEndOfBattle()
/*     */   {
/* 518 */     System.out.println(RoundSummary.getResultHeadingWithTabs());
/* 519 */     for (RoundSummary summary : roundSummaries)
/* 520 */       System.out.println(summary.getResultWithTabs());
/*     */   }
/*     */ 
/*     */   public String getShootingCategory(EnemyRobot enemy, double bulletSpeed)
/*     */   {
/* 525 */     return "DEFAULT";
/*     */   }
/*     */ 
/*     */   protected void setColours()
/*     */   {
/* 530 */     setBodyColor(Color.red);
/* 531 */     setGunColor(Color.black);
/* 532 */     setRadarColor(Color.yellow);
/* 533 */     setBulletColor(Color.green);
/* 534 */     setScanColor(Color.green);
/*     */   }
/*     */ 
/*     */   private void setBattleConstants() {
/* 538 */     BattleConstants.getInstance().setBattlefieldHeight(getBattleFieldHeight());
/* 539 */     BattleConstants.getInstance().setBattlefieldWidth(getBattleFieldWidth());
/* 540 */     BattleConstants.getInstance().setRobotHeight(getHeight());
/* 541 */     BattleConstants.getInstance().setRobotWidth(getWidth());
/* 542 */     BattleConstants.getInstance().setGunCoolingRate(getGunCoolingRate());
/*     */   }
/*     */ 
/*     */   private void informAboutThisNewRobotInstance() {
/* 546 */     for (AimingStrategy aimingStrategy : enemies.getAllAimingStrategies()) {
/* 547 */       aimingStrategy.processEndOfRound(this);
/*     */     }
/* 549 */     displayDebugInfoPerRound();
/*     */   }
/*     */ }

/* Location:           E:\naval-robocode\sample\dmh.robocode.robot.BlackDeathMk7_7.0.jar
 * Qualified Name:     dmh.robocode.robot.CommandBasedRobot
 * JD-Core Version:    0.6.2
 */