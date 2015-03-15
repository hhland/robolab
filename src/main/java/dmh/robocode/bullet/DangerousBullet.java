/*     */ package dmh.robocode.bullet;
/*     */ 
/*     */ import dmh.robocode.data.Location;
/*     */ import dmh.robocode.enemy.EnemyRobot;
/*     */ import dmh.robocode.utils.Geometry;
/*     */ import java.awt.Color;
/*     */ import java.awt.Graphics2D;
/*     */ import robocode.Rules;
/*     */ 
/*     */ public class DangerousBullet
/*     */ {
/*     */   private Location firedFrom;
/*     */   private double speed;
/*     */   private double damage;
/*     */   private double bulletPower;
/*     */   private double estimatedHeading;
/*     */   private long timeFired;
/*     */   private EnemyRobot enemy;
/*  30 */   private Location cachedLocation = null;
/*     */   private long timeOfCachedLocation;
/*     */   private double likelyToBeAimingForUs;
/*  33 */   private double bearingAdjustment = 0.0D;
/*     */ 
/*     */   public DangerousBullet(Location firedFrom, double bulletPower, double estimatedHeading, long timeFired, EnemyRobot enemy, double likelyToBeAimingForUs)
/*     */   {
/*  43 */     this.firedFrom = firedFrom;
/*  44 */     this.speed = Rules.getBulletSpeed(bulletPower);
/*  45 */     this.damage = Rules.getBulletDamage(bulletPower);
/*  46 */     this.bulletPower = bulletPower;
/*  47 */     this.estimatedHeading = estimatedHeading;
/*  48 */     this.timeFired = timeFired;
/*  49 */     this.enemy = enemy;
/*  50 */     this.likelyToBeAimingForUs = likelyToBeAimingForUs;
/*     */   }
/*     */ 
/*     */   public Location getLocationAtTime(long atTime)
/*     */   {
/*  59 */     if ((this.cachedLocation == null) || (atTime != this.timeOfCachedLocation)) {
/*  60 */       double distanceTravelled = this.speed * (atTime - this.timeFired);
/*  61 */       this.cachedLocation = Geometry.getLocationAtBearing(this.firedFrom, getEstimatedHeading(), distanceTravelled);
/*  62 */       this.timeOfCachedLocation = atTime;
/*     */     }
/*  64 */     return this.cachedLocation;
/*     */   }
/*     */ 
/*     */   public double getDamage()
/*     */   {
/*  72 */     return this.damage;
/*     */   }
/*     */ 
/*     */   public double getLikelyToBeAimingForUs()
/*     */   {
/*  77 */     return this.likelyToBeAimingForUs;
/*     */   }
/*     */ 
/*     */   public double getEstimatedHeading()
/*     */   {
/*  82 */     return this.estimatedHeading + this.bearingAdjustment;
/*     */   }
/*     */ 
/*     */   public boolean isPossibleLocation(Location possibleLocation, long atTime, double bulletPower, double marginOfError, EnemyRobot enemy) {
/*  86 */     if ((enemy == this.enemy) && (Math.abs(bulletPower - this.bulletPower) < 0.001D)) {
/*  87 */       double locationDistance = Geometry.getDistanceBetweenLocations(this.firedFrom, possibleLocation);
/*  88 */       double distanceTravelled = (atTime - this.timeFired) * this.speed;
/*  89 */       return Math.abs(distanceTravelled - locationDistance) <= marginOfError;
/*     */     }
/*  91 */     return false;
/*     */   }
/*     */ 
/*     */   public Location getFiredFromLocation() {
/*  95 */     return this.firedFrom;
/*     */   }
/*     */ 
/*     */   public void paint(Graphics2D g, long currentTime) {
/*  99 */     Location predictedLocation = getLocationAtTime(currentTime);
/* 100 */     int x = (int)predictedLocation.getX();
/* 101 */     int y = (int)predictedLocation.getY();
/* 102 */     int radius = 6;
/*     */ 
/* 104 */     g.setColor(Color.red);
/* 105 */     g.fillOval(x - radius, y - radius, radius * 2, radius * 2);
/*     */   }
/*     */ 
/*     */   public void increaseBearingAdjustment(double changeOfAdjustment) {
/* 109 */     this.bearingAdjustment += changeOfAdjustment;
/*     */   }
/*     */ }

/* Location:           E:\naval-robocode\sample\dmh.robocode.robot.BlackDeathMk7_7.0.jar
 * Qualified Name:     dmh.robocode.bullet.DangerousBullet
 * JD-Core Version:    0.6.2
 */