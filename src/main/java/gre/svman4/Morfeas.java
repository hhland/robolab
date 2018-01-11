/*     */ package gre.svman4;
/*     */ 
/*     */ import java.awt.event.KeyEvent;
import java.awt.geom.Point2D;
/*     */ import java.awt.geom.Rectangle2D;
/*     */ import java.awt.geom.Rectangle2D.Double;
/*     */ import java.io.PrintStream;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.Vector;
/*     */ import robocode.AdvancedRobot;
/*     */ import robocode.Bullet;
/*     */ import robocode.BulletHitBulletEvent;
/*     */ import robocode.BulletHitEvent;
/*     */ import robocode.DeathEvent;
/*     */ import robocode.HitByBulletEvent;
/*     */ import robocode.HitWallEvent;
/*     */ import robocode.RobotDeathEvent;
/*     */ import robocode.RoundEndedEvent;
/*     */ import robocode.Rules;
/*     */ import robocode.ScannedRobotEvent;
/*     */ import robocode.StatusEvent;
/*     */ import robocode.util.Utils;
/*     */ 
/*     */ public class Morfeas extends AdvancedRobot
/*     */ {
/*     */   static Rectangle2D battleField;
/*  46 */   static HashMap<String, EnemyState> enemies = new HashMap();
/*     */ 
/*  52 */   static ArrayList<EnemyWaveBullet> enemyWaves = new ArrayList();
/*     */   private static Vector<Gun> guns;
/*  61 */   private static Gun mainGun = new LinearTargetingGun();
/*     */ 
/*  65 */   static RobotState me = new RobotState();
/*     */ 
/*  70 */   static ArrayList<MineWaveBullet> mineWaves = new ArrayList();
/*     */ 
/*  74 */   static HashMap<String, Movement> movement = new HashMap();
/*     */   static Rectangle2D.Double movingPlace;
/*  83 */   private static boolean showGun = true;
/*     */ 
/*  87 */   private static boolean showMovement = true;
/*     */ 
/*  92 */   private static boolean showRadar = true;
/*     */   private Movement mainMovement;
/*     */   Strategy strategy;
/* 104 */   EnemyState target = null;
/*     */ 
/* 106 */   public Vector<VirtualBullet> virtualBullets = new Vector();
/*     */ 
/* 469 */   public static double WALL_MARGIN = 60.0D;
/*     */ 
/* 538 */   public static double TARGET_ENERGY_LIMIT_RAM = 3.0D;
/*     */ 
/*     */   void gun()
/*     */   {
/* 112 */     if (!this.target.isAlive) {
/* 113 */       return;
/*     */     }
/* 115 */     double bulletPower = calculateBulletPower(this.target);
/*     */ 
/* 123 */     long bestScore = -1L;
/* 124 */     int gunNumber = 0;
/* 125 */     Iterator i = guns.iterator();
/*     */ 
/* 127 */     while (i.hasNext()) {
/* 128 */       Gun gun = (Gun)i.next();
/* 129 */       if (this.target.hits[gunNumber] > bestScore) {
/* 130 */         bestScore = this.target.hits[gunNumber];
/* 131 */         mainGun = gun;
/*     */       }
/* 133 */       gunNumber++;
/*     */     }
/*     */ 
/* 136 */     setTurnGunRightRadians(Utils.normalRelativeAngle(mainGun.getFiringAngle(me, 
/* 137 */       this.target, bulletPower) - getGunHeadingRadians()));
/* 138 */     if ((getGunHeat() == 0.0D) && (getEnergy() > 3.0D) && 
/* 139 */       (Math.abs(getGunTurnRemaining()) < 5.0D)) {
/* 140 */       setFire(bulletPower);
/* 141 */       Iterator it = guns.iterator();
/* 142 */       gunNumber = 0;
/*     */ 
/* 144 */       while (it.hasNext()) {
/* 145 */         VirtualBullet vBullet = new VirtualBullet(me.x, me.y);
/* 146 */         Gun tempGun = (Gun)it.next();
/* 147 */         tempGun.gunShootingCounter += 1;
/* 148 */         vBullet.heading = tempGun.getFiringAngle(me, this.target, bulletPower);
/* 149 */         vBullet.velocity = Rules.getBulletSpeed(bulletPower);
/* 150 */         vBullet.target = this.target;
/* 151 */         vBullet.gunNumber = gunNumber;
/* 152 */         this.virtualBullets.add(vBullet);
/* 153 */         gunNumber++;
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   void movement()
/*     */   {
/* 162 */     if (this.strategy == Strategy.OneVsOne) {
/* 163 */       if (me.energy < 3.0D) {
/* 164 */         this.mainMovement = ((Movement)movement.get("Straight"));
/* 165 */         ((OneVsOneMovementStrategy)this.mainMovement).setTarget(this.target);
/*     */       } else {
/* 167 */         this.mainMovement = ((Movement)movement.get("Wave surfer"));
/* 168 */         ((OneVsOneMovementStrategy)this.mainMovement).setTarget(this.target);
/*     */       }
/*     */     }
/* 171 */     else if (this.strategy == Strategy.RAM) {
/* 172 */       this.mainMovement = ((Movement)movement.get("Ram"));
/* 173 */       ((RamMovement)this.mainMovement).setTarget(this.target);
/*     */     } else {
/* 175 */       this.mainMovement = ((Movement)movement.get("MinimumRiskMovement"));
/*     */     }
/* 177 */     this.mainMovement.run(this);
/*     */   }
/*     */ 
/*     */   public void onBulletHit(BulletHitEvent evnt) {
/* 181 */     EnemyState enemy = (EnemyState)enemies.get(evnt.getName());
/* 182 */     if (enemy == null)
/*     */     {
/* 184 */       enemy = new EnemyState(guns.size());
/* 185 */       enemies.put(evnt.getName(), enemy);
/*     */     }
/* 187 */     enemy.energy = evnt.getEnergy();
/* 188 */     if (mineWaves.isEmpty());
/*     */   }
/*     */ 
/*     */   public void onBulletHitBullet(BulletHitBulletEvent evnt)
/*     */   {
/* 194 */     if (!enemyWaves.isEmpty()) {
/* 195 */       FieldPoint hitBulletLocation = new FieldPoint(evnt.getBullet().getX(), 
/* 196 */         evnt.getBullet().getY());
/* 197 */       EnemyWaveBullet hitWave = null;
/*     */ 
/* 200 */       for (int x = 0; x < enemyWaves.size(); x++) {
/* 201 */         EnemyWaveBullet ew = (EnemyWaveBullet)enemyWaves.get(x);
/* 202 */         double distanceDiff = Math.abs(ew.distanceTraveled - 
/* 203 */           hitBulletLocation.distance(ew.fireLocation));
/* 204 */         if (distanceDiff < 20.0D) {
/* 205 */           hitWave = ew;
/* 206 */           break;
/*     */         }
/*     */       }
/* 209 */       if (hitWave != null) {
/* 210 */         hitWave.logHit(hitBulletLocation);
/*     */ 
/* 214 */         enemyWaves.remove(enemyWaves.lastIndexOf(hitWave));
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public void onDeath(DeathEvent event)
/*     */   {
/* 221 */     this.virtualBullets.clear();
/*     */   }
/*     */ 
/*     */   public void onHitByBullet(HitByBulletEvent evnt) {
/* 225 */     if (!enemyWaves.isEmpty()) {
/* 226 */       FieldPoint hitBulletLocation = new FieldPoint(evnt.getBullet().getX(), 
/* 227 */         evnt.getBullet().getY());
/* 228 */       EnemyWaveBullet hitWave = null;
/*     */ 
/* 231 */       for (int x = 0; x < enemyWaves.size(); x++) {
/* 232 */         EnemyWaveBullet ew = (EnemyWaveBullet)enemyWaves.get(x);
/* 233 */         if ((Math.abs(ew.distanceTraveled - me.distance(ew.fireLocation)) < 50.0D) && 
/* 234 */           (Math.abs(evnt.getBullet().getVelocity() - ew.bulletVelocity) < 0.001D)) {
/* 235 */           hitWave = ew;
/* 236 */           break;
/*     */         }
/*     */       }
/* 239 */       if (hitWave != null) {
/* 240 */         hitWave.logHit(hitBulletLocation);
/*     */ 
/* 242 */         enemyWaves.remove(enemyWaves.lastIndexOf(hitWave));
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public void onHitWall(HitWallEvent event)
/*     */   {
/*     */   }
/*     */ 
/*     */   public void onKeyPressed(KeyEvent e)
/*     */   {
/* 253 */     switch (e.getKeyCode()) {
/*     */     case 71:
/* 255 */       if (!showGun)
/* 256 */         showGun = true;
/*     */       else {
/* 258 */         showGun = false;
/*     */       }
/* 260 */       break;
/*     */     case 77:
/* 262 */       if (!showMovement)
/* 263 */         showMovement = true;
/*     */       else {
/* 265 */         showMovement = false;
/*     */       }
/* 267 */       break;
/*     */     case 82:
/* 269 */       if (!showRadar)
/* 270 */         showRadar = true;
/*     */       else
/* 272 */         showRadar = false;
/*     */       break;
/*     */     }
/*     */   }
/*     */ 
/*     */   public void onRobotDeath(RobotDeathEvent evnt)
/*     */   {
/* 328 */     EnemyState en = (EnemyState)enemies.get(evnt.getName());
/* 329 */     if (en == null) {
/* 330 */       return;
/*     */     }
/* 332 */     en.isAlive = false;
/*     */   }
/*     */ 
/*     */   public void onRoundEnded(RoundEndedEvent evnt) {
/* 336 */     this.virtualBullets.clear();
/* 337 */     Iterator itEnemyState = enemies.values().iterator();
/*     */     Iterator itGun;
/* 339 */     for (; itEnemyState.hasNext(); 
/* 344 */       itGun.hasNext())
/*     */     {
/* 340 */       EnemyState en = (EnemyState)itEnemyState.next();
/* 341 */       this.out.println("Enemy:" + en.name);
/* 342 */       itGun = guns.iterator();
/* 343 */       int gunNumber = 0;
/* 344 */       //continue;
/* 345 */       Gun gun = (Gun)itGun.next();
/* 346 */       this.out.println("\t" + gun.getName() + " " + en.hits[gunNumber]);
/* 347 */       gunNumber++;
/*     */     }
/*     */ 
/* 350 */     enemyWaves.clear();
/* 351 */     mineWaves.clear();
/* 352 */     itGun = guns.iterator();
/* 353 */     while (itGun.hasNext()) {
/* 354 */       Gun gun = (Gun)itGun.next();
/* 355 */       gun.printStatisticData();
/*     */     }
/*     */   }
/*     */ 
/*     */   public void onScannedRobot(ScannedRobotEvent e)
/*     */   {
/* 365 */     if (this.strategy != Strategy.Melee)
/*     */     {
/* 367 */       double angleToEnemy = getHeadingRadians() + e.getBearingRadians();
/* 368 */       double moduleTurn = Utils.normalRelativeAngle(angleToEnemy - 
/* 369 */         getRadarHeadingRadians());
/* 370 */       double extraTurn = Math.min(Math.atan(50.0D / e.getDistance()), 
/* 371 */         Rules.RADAR_TURN_RATE_RADIANS);
/* 372 */       moduleTurn += (moduleTurn < 0.0D ? -extraTurn : extraTurn);
/* 373 */       setTurnRadarRightRadians(moduleTurn);
/*     */     }
/* 375 */     EnemyState enemy = (EnemyState)enemies.get(e.getName());
/* 376 */     if (enemy == null)
/*     */     {
/* 378 */       enemy = new EnemyState(guns.size());
/* 379 */       enemy.name = e.getName();
/* 380 */       enemies.put(e.getName(), enemy);
/*     */     }
/*     */ 
/* 383 */     enemy.setLocation(me.project(e.getDistance(), 
/* 384 */       getHeadingRadians() + e.getBearingRadians()));
/* 385 */     enemy.deltaHeading = (enemy.heading - e.getHeadingRadians());
/* 386 */     enemy.heading = e.getHeadingRadians();
/* 387 */     enemy.velocity = e.getVelocity();
/* 388 */     if (e.getVelocity() != 0.0D) {
/* 389 */       enemy.lateralDirection = 
/* 390 */         (e.getVelocity() * Math.sin(e
/* 390 */         .getHeadingRadians() - getHeadingRadians() + e.getBearingRadians()) < 0.0D ? -1 : 
/* 391 */         1);
/*     */     }
/* 393 */     enemy.surfDirections.add(0, 
/* 394 */       new Integer(
/* 395 */       getVelocity() * Math.sin(e.getBearingRadians()) >= 0.0D ? 1 : -1));
/* 396 */     enemy.surfAbsBearings.add(0, 
/* 397 */       e.getBearingRadians() + getHeadingRadians() + 3.141592653589793D);
/*     */ 
/* 400 */     double looseEnergy = enemy.energy - e.getEnergy();
/* 401 */     if ((looseEnergy > 0.0D) && (looseEnergy < 3.01D))
/*     */     {
/* 403 */       EnemyWaveBullet ew = new EnemyWaveBullet();
/* 404 */       ew.fireTime = (getTime() - 1L);
/* 405 */       ew.enemy = enemy;
/* 406 */       ew.bulletVelocity = Rules.getBulletSpeed(looseEnergy);
/* 407 */       ew.distanceTraveled = Rules.getBulletSpeed(looseEnergy);
/* 408 */       ew.lateralDirection = ((Integer)enemy.surfDirections.get(2)).intValue();
/* 409 */       ew.directAngle = enemy.surfAbsBearings.get(2);
/* 410 */       ew.fireLocation = ((FieldPoint)enemy.clone());
/* 411 */       ew.mineRobotVelocity = Math.abs(me.velocity);
/* 412 */       ew.distanceToEnemyPosition = me.distance(enemy);
/* 413 */       enemyWaves.add(ew);
/*     */     }
/*     */ 
/* 416 */     enemy.energy = e.getEnergy();
/*     */ 
/* 421 */     if ((!this.target.isAlive) || (enemy.distance(me) < this.target.distance(me)))
/* 422 */       this.target = enemy;
/*     */   }
/*     */ 
/*     */   private double calculateBulletPower(EnemyState enemy)
/*     */   {
/* 434 */     return Math.min(2.0D * (300.0D / enemy.distance(me)), enemy.energy / 3.0D);
/*     */   }
/*     */ 
/*     */   public void onStatus(StatusEvent evnt)
/*     */   {
/* 440 */     me.setLocation(getX(), getY());
/* 441 */     me.heading = getHeadingRadians();
/* 442 */     updateVirtualBullet();
/* 443 */     updateEnemyWaves();
/* 444 */     updateMineWaves();
/*     */ 
/* 446 */     MineWaveBullet mWave = new MineWaveBullet();
/* 447 */     mWave.bulletVelocity = Rules.getBulletSpeed(calculateBulletPower(this.target));
/* 448 */     mWave.directAngle = me.getAngleTo(this.target);
/* 449 */     mWave.distanceTraveled = 0.0D;
/* 450 */     mWave.enemy = this.target;
/* 451 */     mWave.fireTime = getTime();
/* 452 */     mWave.fireLocation = new FieldPoint(me);
/* 453 */     mWave.distanceToEnemyPosition = me.distance(this.target);
/* 454 */     mWave.enemyVelocity = this.target.velocity;
/* 455 */     mWave.lastEnemyVelocity = this.target.lastVelocity;
/* 456 */     mWave.mineRobotVelocity = me.velocity;
/* 457 */     mWave.lateralDirection = this.target.lateralDirection;
/* 458 */     if (getTime() % 20L == 0L) {
/* 459 */       mWave.paint = true;
/*     */     }
/* 461 */     mineWaves.add(mWave);
/*     */   }
/*     */ 
/*     */   public void run()
/*     */   {
/* 472 */     if (movingPlace == null)
/*     */     {
/* 474 */       battleField = new Rectangle2D.Double(0.0D, 0.0D, getBattleFieldWidth(), 
/* 475 */         getBattleFieldHeight());
/* 476 */       movingPlace = new Rectangle2D.Double(WALL_MARGIN, WALL_MARGIN, 
/* 477 */         getBattleFieldWidth() - 2.0D * WALL_MARGIN, getBattleFieldHeight() - 2.0D * 
/* 478 */         WALL_MARGIN);
/*     */ 
/* 480 */       setAdjustGunForRobotTurn(true);
/*     */ 
/* 482 */       setAdjustRadarForGunTurn(true);
/* 483 */       MinimumRiskMovement minimumRiskMovement = new MinimumRiskMovement(me, 
/* 484 */         movingPlace, enemies);
/* 485 */       movement.put(minimumRiskMovement.getName(), minimumRiskMovement);
/* 486 */       RamMovement ramMovement = new RamMovement(me, this.target);
/* 487 */       movement.put(ramMovement.getName(), ramMovement);
/* 488 */       WaveSurfer waveSurfer = new WaveSurfer(me, battleField, this.target, 
/* 489 */         enemyWaves);
/* 490 */       movement.put(waveSurfer.getName(), waveSurfer);
/* 491 */       Straight straight = new Straight(me, battleField, this.target);
/* 492 */       movement.put(straight.getName(), straight);
/*     */ 
/* 496 */       guns = new Vector();
/*     */ 
/* 500 */       guns.add(new GuessFactorTargetingGun());
/*     */     }
/*     */ 
/* 503 */     this.target = new EnemyState(guns.size());
/*     */ 
/* 505 */     this.target.isAlive = false;
/*     */ 
/* 508 */     Iterator it = enemies.values().iterator();
/* 509 */     while (it.hasNext()) {
/* 510 */       EnemyState en = (EnemyState)it.next();
/* 511 */       en.isAlive = true;
/*     */     }
/*     */     while (true) {
/* 514 */       me.energy = getEnergy();
/* 515 */       me.velocity = getVelocity();
/* 516 */       if (getRadarTurnRemaining() == 0.0D) {
/* 517 */         setTurnRadarRightRadians((1.0D / 0.0D));
/*     */       }
/*     */ 
/* 520 */       if (getOthers() > 1)
/* 521 */         this.strategy = Strategy.Melee;
/* 522 */       else if ((this.target.energy < TARGET_ENERGY_LIMIT_RAM) && 
/* 523 */         (enemyWaves.isEmpty()))
/* 524 */         this.strategy = Strategy.RAM;
/*     */       else {
/* 526 */         this.strategy = Strategy.OneVsOne;
/*     */       }
/* 528 */       movement();
/* 529 */       gun();
/* 530 */       execute();
/*     */     }
/*     */   }
/*     */ 
/*     */   public void updateEnemyWaves()
/*     */   {
/* 544 */     for (int x = 0; x < enemyWaves.size(); x++) {
/* 545 */       EnemyWaveBullet ew = (EnemyWaveBullet)enemyWaves.get(x);
/* 546 */       ew.distanceTraveled = ((getTime() - ew.fireTime) * ew.bulletVelocity);
/* 547 */       if (ew.distanceTraveled > me.distance(ew.fireLocation) + 50.0D) {
/* 548 */         enemyWaves.remove(x);
/* 549 */         x--;
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   private void updateMineWaves()
/*     */   {
/* 558 */     for (int x = 0; x < mineWaves.size(); x++) {
/* 559 */       MineWaveBullet mWave = (MineWaveBullet)mineWaves.get(x);
/* 560 */       mWave.distanceTraveled = 
/* 561 */         ((getTime() - mWave.fireTime) * 
/* 561 */         mWave.bulletVelocity);
/* 562 */       double distance = this.target.distance(mWave.fireLocation) - 18.0D;
/* 563 */       if (mWave.distanceTraveled > distance) {
/* 564 */         mWave.logHit();
/* 565 */         mineWaves.remove(x);
/* 566 */         x--;
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   private void updateVirtualBullet()
/*     */   {
/* 577 */     Iterator ite = this.virtualBullets.iterator();
/* 578 */     while (ite.hasNext()) {
/* 579 */       VirtualBullet temp = (VirtualBullet)ite.next();
/* 580 */       temp.setLocation(temp.project(temp.velocity, temp.heading));
/* 581 */       if (!battleField.contains(temp)) {
/* 582 */         ite.remove();
/* 583 */       } else if (temp.distance(temp.target) < 15.0D) {
/* 584 */         temp.target.hits[temp.gunNumber] += 1;
/* 585 */         ((Gun)guns.get(temp.gunNumber)).gunSuccessShootingCounter += 1;
/* 586 */         ite.remove();
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   static enum Strategy
/*     */   {
/*  38 */     Melee, OneVsOne, RAM;
/*     */   }
/*     */ }

/* Location:           E:\naval-robocode\sample\gre.svman4.Morfeas_1.4.jar
 * Qualified Name:     gre.svman4.Morfeas
 * JD-Core Version:    0.6.2
 */