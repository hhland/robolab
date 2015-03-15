/*     */ package kms;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import java.awt.Graphics2D;
/*     */ import java.awt.geom.Point2D;
/*     */ import java.awt.geom.Point2D.Double;
/*     */ import java.awt.geom.Rectangle2D;
/*     */ import java.awt.geom.Rectangle2D.Double;
/*     */ import java.util.ArrayList;
/*     */ import robocode.AdvancedRobot;
/*     */ import robocode.Bullet;
/*     */ import robocode.Condition;
/*     */ import robocode.HitByBulletEvent;
/*     */ import robocode.ScannedRobotEvent;
/*     */ import robocode.StatusEvent;
/*     */ import robocode.util.Utils;
/*     */ 
/*     */ public class Golden extends AdvancedRobot
/*     */ {
/*  30 */   public static int BINS = 52;
/*  31 */   public static double[] _surfStats = new double[BINS];
/*     */   public Point2D.Double _myLocation;
/*     */   public Point2D.Double _enemyLocation;
/*     */   public ArrayList _enemyWaves;
/*     */   public ArrayList _surfDirections;
/*     */   public ArrayList _surfAbsBearings;
/*  41 */   public static double _oppEnergy = 100.0D;
/*     */ 
/*  45 */   public static Rectangle2D.Double _fieldRect = new Rectangle2D.Double(18.0D, 18.0D, 764.0D, 564.0D);
/*     */ 
/*  47 */   public static double WALL_STICK = 125.0D;
/*     */   private boolean waves;
/*     */   private boolean scanned;
/*  55 */   double enemyLatVel = 0.0D;
/*     */   boolean ATR;
/*     */   static final double BATTLE_FIELD_WIDTH = 800.0D;
/*     */   static final double BATTLE_FIELD_HEIGHT = 600.0D;
/*     */   static final double MAX_DISTANCE = 900.0D;
/*     */   static final double MAX_BULLET_POWER = 3.0D;
/*     */   static final double BULLET_POWER = 1.9D;
/*     */   static final double WALL_MARGIN = 18.0D;
/*     */   static final double MAX_TRIES = 125.0D;
/*     */   static final double REVERSE_TUNER = 0.421075D;
/*     */   static final double WALL_BOUNCE_TUNER = 0.699484D;
/*     */   static final int DISTANCE_INDEXES = 5;
/*     */   static final int VELOCITY_INDEXES = 5;
/*     */   static final int LAST_VELOCITY_INDEXES = 5;
/*     */   static final int WALL_INDEXES = 2;
/*     */   static final int DECCEL_TIME_INDEXES = 6;
/*     */   static final int AIM_FACTORS = 25;
/*     */   static final int MIDDLE_FACTOR = 12;
/*     */   static Point2D enemyLocation;
/*     */   static double enemyVelocity;
/*     */   static int timeSinceDeccel;
/*     */   static double bearingDirection;
/*  81 */   static int[][][][][][] aimFactors = new int[5][5][5][6][2][25];
/*  82 */   static double direction = 0.4D;
/*     */   static double enemyFirePower;
/*     */   static int GF1Hits;
/*     */   static double tries;
/*     */ 
/*     */   public void run()
/*     */   {
/*  88 */     this._enemyWaves = new ArrayList();
/*  89 */     this._surfDirections = new ArrayList();
/*  90 */     this._surfAbsBearings = new ArrayList();
/*     */ 
/*  92 */     setAdjustGunForRobotTurn(true);
/*  93 */     setAdjustRadarForGunTurn(true);
/*     */ 
/*  96 */     setReadyState();
/*     */     while (true)
/*     */     {
/* 100 */       turnRadarRightRadians((1.0D / 0.0D));
/* 101 */       setTurnGunRightRadians(1.0D);
/*     */     }
/*     */   }
/*     */ 
/*     */   private void setReadyState() {
/* 106 */     this.waves = false;
/* 107 */     this.scanned = false;
/* 108 */     setColors(
/* 109 */       Color.yellow, Color.yellow, Color.yellow, Color.yellow, Color.yellow);
/*     */   }
/*     */ 
/*     */   public void onScannedRobot(ScannedRobotEvent e)
/*     */   {
/* 114 */     this.scanned = true;
/*     */ 
/* 116 */     Wave wave = new Wave();
/*     */ 
/* 118 */     this.enemyLatVel = (e.getVelocity() * Math.sin(e.getBearingRadians()));
/* 119 */     if ((this.enemyLatVel < 1.0D) && (this.enemyLatVel > -1.0D)) {
/* 120 */       this.ATR = true;
/* 121 */       wave.wBulletPower = 3.0D;
/*     */     }
/*     */ 
/* 124 */     this._myLocation = new Point2D.Double(getX(), getY());
/*     */ 
/* 126 */     double latVel = getVelocity() * Math.sin(e.getBearingRadians());
/* 127 */     double absBearing = e.getBearingRadians() + getHeadingRadians();
/*     */ 
/* 129 */     setTurnRadarRightRadians(Utils.normalRelativeAngle(absBearing - getRadarHeadingRadians()) * 2.0D);
/*     */ 
/* 131 */     this._surfDirections.add(0, 
/* 132 */       new Integer(latVel >= 0.0D ? 1 : -1));
/* 133 */     this._surfAbsBearings.add(0, new Double(absBearing + 3.141592653589793D));
/*     */ 
/* 136 */     double bulletPower = _oppEnergy - e.getEnergy();
/* 137 */     if ((bulletPower <= 3.0D) && (bulletPower >= 0.1D) && 
/* 138 */       (this._surfDirections.size() > 2)) {
/* 139 */       EnemyWave ew = new EnemyWave();
/* 140 */       ew.fireTime = (getTime() - 1L);
/* 141 */       ew.bulletVelocity = bulletVelocity(bulletPower);
/* 142 */       ew.distanceTraveled = bulletVelocity(bulletPower);
/* 143 */       ew.direction = ((Integer)this._surfDirections.get(2)).intValue();
/* 144 */       ew.directAngle = ((Double)this._surfAbsBearings.get(2)).doubleValue();
/* 145 */       ew.fireLocation = ((Point2D.Double)this._enemyLocation.clone());
/*     */ 
/* 147 */       this._enemyWaves.add(ew);
/*     */ 
/* 149 */       this.waves = true;
/*     */     }
/*     */ 
/* 152 */     _oppEnergy = e.getEnergy();
/*     */ 
/* 155 */     this._enemyLocation = project(this._myLocation, absBearing, e.getDistance());
/*     */ 
/* 157 */     updateWaves();
/* 158 */     doSurfing();
/*     */ 
/* 172 */     double enemyAbsoluteBearing = getHeadingRadians() + e.getBearingRadians();
/*     */     double enemyDistance;
/* 174 */     enemyLocation = project(wave.wGunLocation = new Point2D.Double(getX(), getY()), enemyAbsoluteBearing, enemyDistance = e.getDistance());
/* 175 */     Rectangle2D fieldRectangle = new Rectangle2D.Double(18.0D, 18.0D, 
/* 176 */       764.0D, 564.0D);
/* 177 */     int lastVelocityIndex = (int)Math.abs(enemyVelocity) / 2;
/* 178 */     int velocityIndex = (int)Math.abs((Golden.enemyVelocity = e.getVelocity()) / 2.0D);
/* 179 */     if (velocityIndex < lastVelocityIndex) {
/* 180 */       timeSinceDeccel = 0;
/*     */     }
/*     */ 
/* 183 */     if (enemyVelocity != 0.0D) {
/* 184 */       bearingDirection = enemyVelocity * Math.sin(e.getHeadingRadians() - enemyAbsoluteBearing) > 0.0D ? 
/* 185 */         0.05833333333333333D : -0.05833333333333333D;
/*     */     }
/* 187 */     wave.wBearingDirection = bearingDirection;
/*     */     int distanceIndex;
/* 190 */     wave.wBulletPower = Math.min(e.getEnergy() / 4.0D, 
/* 191 */       (distanceIndex = (int)(enemyDistance / 180.0D)) > 1 ? 1.9D : 3.0D);
/*     */ 
/* 194 */     wave.wAimFactors = aimFactors[distanceIndex][velocityIndex][lastVelocityIndex][Math.min(5, timeSinceDeccel++ / 13)][
/* 195 */       0];
/*     */ 
/* 197 */     wave.wBearing = enemyAbsoluteBearing;
/*     */ 
/* 199 */     int mostVisited = 12; int i = 25;
/*     */     do {
/* 201 */       if (wave.wAimFactors[(--i)] > wave.wAimFactors[mostVisited])
/* 202 */         mostVisited = i;
/*     */     }
/* 204 */     while (i > 0);
/*     */ 
/* 206 */     setTurnGunRightRadians(Utils.normalRelativeAngle(enemyAbsoluteBearing - getGunHeadingRadians() + 
/* 207 */       wave.wBearingDirection * (mostVisited - 12)));
/*     */ 
/* 209 */     setFire(wave.wBulletPower);
/* 210 */     if (getEnergy() >= 1.9D)
/* 211 */       addCustomEvent(wave);
/*     */   }
/*     */ 
/*     */   public void updateWaves()
/*     */   {
/* 216 */     for (int x = 0; x < this._enemyWaves.size(); x++) {
/* 217 */       EnemyWave ew = (EnemyWave)this._enemyWaves.get(x);
/*     */ 
/* 219 */       ew.distanceTraveled = ((getTime() - ew.fireTime) * ew.bulletVelocity);
/*     */ 
/* 221 */       if (ew.distanceTraveled > 
/* 221 */         this._myLocation.distance(ew.fireLocation) + 50.0D) {
/* 222 */         this._enemyWaves.remove(x);
/* 223 */         x--;
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public EnemyWave getClosestSurfableWave() {
/* 229 */     double closestDistance = 50000.0D;
/* 230 */     EnemyWave surfWave = null;
/*     */ 
/* 232 */     for (int x = 0; x < this._enemyWaves.size(); x++) {
/* 233 */       EnemyWave ew = (EnemyWave)this._enemyWaves.get(x);
/* 234 */       double distance = this._myLocation.distance(ew.fireLocation) - 
/* 235 */         ew.distanceTraveled;
/*     */ 
/* 237 */       if ((distance > ew.bulletVelocity) && (distance < closestDistance)) {
/* 238 */         surfWave = ew;
/* 239 */         closestDistance = distance;
/*     */       }
/*     */     }
/*     */ 
/* 243 */     return surfWave;
/*     */   }
/*     */ 
/*     */   public static int getFactorIndex(EnemyWave ew, Point2D.Double targetLocation)
/*     */   {
/* 249 */     double offsetAngle = absoluteBearing(ew.fireLocation, targetLocation) - 
/* 250 */       ew.directAngle;
/* 251 */     double factor = Utils.normalRelativeAngle(offsetAngle) / 
/* 252 */       maxEscapeAngle(ew.bulletVelocity) * ew.direction;
/*     */ 
/* 254 */     return (int)limit(0.0D, 
/* 255 */       factor * ((BINS - 1) / 2) + (BINS - 1) / 2, 
/* 256 */       BINS - 1);
/*     */   }
/*     */ 
/*     */   public void logHit(EnemyWave ew, Point2D.Double targetLocation)
/*     */   {
/* 262 */     int index = getFactorIndex(ew, targetLocation);
/*     */ 
/* 264 */     for (int x = 0; x < BINS; x++)
/*     */     {
/* 268 */       _surfStats[x] += 1.0D / (Math.pow(index - x, 2.0D) + 1.0D);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void onHitByBullet(HitByBulletEvent e)
/*     */   {
/* 274 */     if (tries < 30.0D) {
/* 275 */       GF1Hits += 1;
/*     */     }
/* 277 */     enemyFirePower = e.getPower();
/*     */ 
/* 281 */     if (!this._enemyWaves.isEmpty()) {
/* 282 */       Point2D.Double hitBulletLocation = new Point2D.Double(
/* 283 */         e.getBullet().getX(), e.getBullet().getY());
/* 284 */       EnemyWave hitWave = null;
/*     */ 
/* 287 */       for (int x = 0; x < this._enemyWaves.size(); x++) {
/* 288 */         EnemyWave ew = (EnemyWave)this._enemyWaves.get(x);
/*     */ 
/* 291 */         if (Math.abs(ew.distanceTraveled - 
/* 291 */           this._myLocation.distance(ew.fireLocation)) < 50.0D)
/*     */         {
/* 293 */           if (Math.abs(bulletVelocity(e.getBullet().getPower()) - 
/* 293 */             ew.bulletVelocity) < 0.001D) {
/* 294 */             hitWave = ew;
/* 295 */             break;
/*     */           }
/*     */         }
/*     */       }
/* 299 */       if (hitWave != null) {
/* 300 */         logHit(hitWave, hitBulletLocation);
/*     */ 
/* 303 */         this._enemyWaves.remove(this._enemyWaves.lastIndexOf(hitWave));
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public Point2D.Double predictPosition(EnemyWave surfWave, int direction)
/*     */   {
/* 311 */     Point2D.Double predictedPosition = (Point2D.Double)this._myLocation.clone();
/* 312 */     double predictedVelocity = getVelocity();
/* 313 */     double predictedHeading = getHeadingRadians();
/*     */ 
/* 316 */     int counter = 0;
/* 317 */     boolean intercepted = false;
/*     */     do
/*     */     {
/* 320 */       double moveAngle = 
/* 321 */         wallSmoothing(predictedPosition, absoluteBearing(surfWave.fireLocation, 
/* 322 */         predictedPosition) + direction * 1.570796326794897D, direction) - 
/* 323 */         predictedHeading;
/* 324 */       double moveDir = 1.0D;
/*     */ 
/* 326 */       if (Math.cos(moveAngle) < 0.0D) {
/* 327 */         moveAngle += 3.141592653589793D;
/* 328 */         moveDir = -1.0D;
/*     */       }
/*     */ 
/* 331 */       moveAngle = Utils.normalRelativeAngle(moveAngle);
/*     */ 
/* 334 */       double maxTurning = 0.004363323129985824D * (40.0D - 3.0D * Math.abs(predictedVelocity));
/* 335 */       predictedHeading = Utils.normalRelativeAngle(predictedHeading + 
/* 336 */         limit(-maxTurning, moveAngle, maxTurning));
/*     */ 
/* 341 */       predictedVelocity += (predictedVelocity * moveDir < 0.0D ? 2.0D * moveDir : moveDir);
/* 342 */       predictedVelocity = limit(-8.0D, predictedVelocity, 8.0D);
/*     */ 
/* 345 */       predictedPosition = project(predictedPosition, predictedHeading, predictedVelocity);
/*     */ 
/* 347 */       counter++;
/*     */ 
/* 350 */       if (predictedPosition.distance(surfWave.fireLocation) < 
/* 350 */         surfWave.distanceTraveled + counter * surfWave.bulletVelocity + 
/* 351 */         surfWave.bulletVelocity)
/* 352 */         intercepted = true;
/*     */     }
/* 354 */     while ((!intercepted) && (counter < 500));
/*     */ 
/* 356 */     return predictedPosition;
/*     */   }
/*     */ 
/*     */   public double checkDanger(EnemyWave surfWave, int direction) {
/* 360 */     int index = getFactorIndex(surfWave, 
/* 361 */       predictPosition(surfWave, direction));
/*     */ 
/* 363 */     return _surfStats[index];
/*     */   }
/*     */ 
/*     */   public void doSurfing() {
/* 367 */     EnemyWave surfWave = getClosestSurfableWave();
/*     */ 
/* 369 */     if (surfWave == null) return;
/*     */ 
/* 371 */     double dangerLeft = checkDanger(surfWave, -1);
/* 372 */     double dangerRight = checkDanger(surfWave, 1);
/*     */ 
/* 374 */     double goAngle = absoluteBearing(surfWave.fireLocation, this._myLocation);
/* 375 */     if (dangerLeft < dangerRight)
/* 376 */       goAngle = wallSmoothing(this._myLocation, goAngle - 1.570796326794897D, -1);
/*     */     else {
/* 378 */       goAngle = wallSmoothing(this._myLocation, goAngle + 1.570796326794897D, 1);
/*     */     }
/*     */ 
/* 381 */     setBackAsFront(this, goAngle);
/*     */   }
/*     */ 
/*     */   public double wallSmoothing(Point2D.Double botLocation, double angle, int orientation)
/*     */   {
/* 398 */     while (!_fieldRect.contains(project(botLocation, angle, 160.0D))) {
/* 399 */       angle += orientation * 0.05D;
/*     */     }
/* 401 */     return angle;
/*     */   }
/*     */ 
/*     */   public static Point2D.Double project(Point2D.Double sourceLocation, double angle, double length)
/*     */   {
/* 408 */     return new Point2D.Double(sourceLocation.x + Math.sin(angle) * length, 
/* 409 */       sourceLocation.y + Math.cos(angle) * length);
/*     */   }
/*     */ 
/*     */   public static double absoluteBearing(Point2D.Double source, Point2D.Double target)
/*     */   {
/* 415 */     return Math.atan2(target.x - source.x, target.y - source.y);
/*     */   }
/*     */ 
/*     */   public static double limit(double min, double value, double max) {
/* 419 */     return Math.max(min, Math.min(value, max));
/*     */   }
/*     */ 
/*     */   public static double bulletVelocity(double power) {
/* 423 */     return 20.0D - 3.0D * power;
/*     */   }
/*     */ 
/*     */   public static double maxEscapeAngle(double velocity) {
/* 427 */     return Math.asin(8.0D / velocity);
/*     */   }
/*     */ 
/*     */   public static void setBackAsFront(AdvancedRobot robot, double goAngle)
/*     */   {
/* 432 */     double angle = 
/* 433 */       Utils.normalRelativeAngle(goAngle - robot.getHeadingRadians());
/*     */ 
/* 435 */     if (Math.abs(angle) > 1.570796326794897D) {
/* 436 */       if (angle < 0.0D)
/* 437 */         robot.setTurnRightRadians(3.141592653589793D + angle);
/*     */       else {
/* 439 */         robot.setTurnLeftRadians(3.141592653589793D - angle);
/*     */       }
/* 441 */       robot.setBack(100.0D);
/*     */     } else {
/* 443 */       if (angle < 0.0D)
/* 444 */         robot.setTurnLeftRadians(-1.0D * angle);
/*     */       else {
/* 446 */         robot.setTurnRightRadians(angle);
/*     */       }
/* 448 */       robot.setAhead(100.0D);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void onPaint(Graphics2D g) {
/* 453 */     g.setColor(Color.green);
/*     */ 
/* 455 */     for (int i = 0; i < this._enemyWaves.size(); i++) {
/* 456 */       EnemyWave w = (EnemyWave)this._enemyWaves.get(i);
/* 457 */       Point2D.Double center = w.fireLocation;
/*     */ 
/* 459 */       int radius = (int)w.distanceTraveled;
/*     */ 
/* 462 */       if (radius - 40 < center.distance(this._myLocation))
/* 463 */         g.drawOval((int)(center.x - radius), (int)(center.y - radius), radius * 2, radius * 2); 
/*     */     }
/*     */   }
/*     */ 
/* 467 */   static double bulletSpeed(double power) { return 20.0D - 3.0D * power; }
/*     */ 
/*     */   static Point2D project(Point2D sourceLocation, double angle, double length)
/*     */   {
/* 471 */     return new Point2D.Double(sourceLocation.getX() + Math.sin(angle) * length, 
/* 472 */       sourceLocation.getY() + Math.cos(angle) * length);
/*     */   }
/*     */ 
/*     */   static double absoluteBearing(Point2D source, Point2D target) {
/* 476 */     return Math.atan2(target.getX() - source.getX(), target.getY() - source.getY()); } 
/*     */   public void onStatus(StatusEvent e) {  } 
/*     */   class EnemyWave { Point2D.Double fireLocation;
/*     */     long fireTime;
/*     */     double bulletVelocity;
/*     */     double directAngle;
/*     */     double distanceTraveled;
/*     */     int direction;
/*     */ 
/*     */     public EnemyWave() {  }  } 
/*     */   class Wave extends Condition { double wBulletPower;
/*     */     Point2D wGunLocation;
/*     */     double wBearing;
/*     */     double wBearingDirection;
/*     */     int[] wAimFactors;
/*     */     double wDistance;
/*     */ 
/*     */     Wave() {  } 
/* 488 */     public boolean test() { if (this.wDistance += Golden.bulletSpeed(this.wBulletPower) > this.wGunLocation.distance(Golden.enemyLocation) - 18.0D) {
/*     */         try {
/* 490 */           this.wAimFactors[((int)Math.round(Utils.normalRelativeAngle(Golden.absoluteBearing(this.wGunLocation, Golden.enemyLocation) - this.wBearing) / 
/* 491 */             this.wBearingDirection + 12.0D))] += 1;
/*     */         }
/*     */         catch (Exception localException)
/*     */         {
/*     */         }
/* 495 */         Golden.this.removeCustomEvent(this);
/*     */       }
/* 497 */       return false;
/*     */     }
/*     */   }
/*     */ }

/* Location:           E:\naval-robocode\sample\kms.Golden_0.10.jar
 * Qualified Name:     kms.Golden
 * JD-Core Version:    0.6.2
 */