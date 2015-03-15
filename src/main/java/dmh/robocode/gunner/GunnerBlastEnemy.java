/*     */ package dmh.robocode.gunner;
/*     */ 
/*     */ import dmh.robocode.data.BattleConstants;
/*     */ import dmh.robocode.data.Location;
/*     */ import dmh.robocode.data.RadarObservation;
/*     */ import dmh.robocode.enemy.EnemyRobot;
/*     */ import dmh.robocode.gunner.aiming.AimBetweenFurtherstPossibleForwardsAndBackwards;
/*     */ import dmh.robocode.gunner.aiming.AimByInterpolatingStraightAhead;
/*     */ import dmh.robocode.gunner.aiming.AimByMovementSequenceReplay;
/*     */ import dmh.robocode.gunner.aiming.AimByRepeatingLastMove;
/*     */ import dmh.robocode.gunner.aiming.AimByRepeatingLastMoveImproved;
/*     */ import dmh.robocode.gunner.aiming.AimBySprayingAtLongDistance;
/*     */ import dmh.robocode.gunner.aiming.AimForAverageLocation;
/*     */ import dmh.robocode.gunner.aiming.AimForRecentLocation;
/*     */ import dmh.robocode.gunner.aiming.AimingStrategy;
/*     */ import dmh.robocode.robot.CommandBasedRobot;
/*     */ import dmh.robocode.utils.BulletPowerUtils;
/*     */ import dmh.robocode.utils.Geometry;
/*     */ import java.awt.Color;
/*     */ import java.awt.Graphics2D;
/*     */ import java.util.List;
/*     */ import robocode.Rules;
/*     */ 
/*     */ public class GunnerBlastEnemy
/*     */   implements GunnerCommand
/*     */ {
/*     */   private double shootingPowerDecayFactor;
/*  28 */   private int remainingBullets = 5;
/*     */   private EnemyRobot enemy;
/*     */   private CommandBasedRobot myRobot;
/*     */   private Location impactLocation;
/*     */   private double impactSuccessRating;
/*     */   private AimingStrategy impactAimingStrategy;
/*     */   private double impactBulletPower;
/*     */   private double fullPowerSuccess;
/*     */   private double shootingMaxPower;
/*     */   private boolean isLearningAllowed;
/*  40 */   private long impactTimeOfPreviousShot = -5L;
/*     */   private Location impactLocationOfPreviousShot;
/*     */ 
/*     */   public GunnerBlastEnemy(CommandBasedRobot myRobot, EnemyRobot enemy, int remainingBullets, double fullPowerSuccess, boolean isLearningAllowed, double shootingPowerDecayFactor)
/*     */   {
/*  44 */     this.enemy = enemy;
/*  45 */     this.myRobot = myRobot;
/*  46 */     this.fullPowerSuccess = fullPowerSuccess;
/*  47 */     this.remainingBullets = remainingBullets;
/*  48 */     this.isLearningAllowed = isLearningAllowed;
/*  49 */     this.shootingPowerDecayFactor = shootingPowerDecayFactor;
/*  50 */     this.shootingMaxPower = 3.0D;
/*  51 */     setupAimingStrategies();
/*  52 */     setImpactLocation();
/*     */   }
/*     */ 
/*     */   public boolean isDone()
/*     */   {
/*  57 */     return !this.enemy.isAlive();
/*     */   }
/*     */ 
/*     */   public double getRightTurn()
/*     */   {
/*  63 */     if (this.impactLocation != null) {
/*  64 */       double requiredHeading = Geometry.getBearingBetweenLocations(this.myRobot.getLocation(), this.impactLocation);
/*     */ 
/*  66 */       return Geometry.getRelativeBearing(this.myRobot.getGunHeading(), requiredHeading);
/*     */     }
/*  68 */     return 0.0D;
/*     */   }
/*     */ 
/*     */   public AimingStrategy getAimingStrategy()
/*     */   {
/*  74 */     return this.impactAimingStrategy;
/*     */   }
/*     */ 
/*     */   private void setImpactLocation()
/*     */   {
/*  79 */     if (this.myRobot.getGunHeat() / this.myRobot.getGunCoolingRate() > 5.0D) {
/*  80 */       this.impactLocation = this.enemy.getLatestRadarObservation().getLocation();
/*     */     }
/*     */     else
/*     */     {
/*  87 */       this.impactBulletPower = getMaximumBulletPower();
/*  88 */       calculateBestImpactLocation();
/*     */ 
/*  93 */       if (this.impactSuccessRating < this.fullPowerSuccess)
/*     */       {
/*  95 */         double highBulletPower = this.impactBulletPower;
/*  96 */         Location highTargetLocation = this.impactLocation;
/*  97 */         double highSuccessRating = this.impactSuccessRating;
/*     */ 
/* 100 */         this.impactBulletPower = getLowerBulletPower();
/* 101 */         calculateBestImpactLocation();
/*     */ 
/* 107 */         if (getRateOfDamage(this.impactBulletPower, this.impactLocation, this.impactSuccessRating) < getRateOfDamage(highBulletPower, highTargetLocation, highSuccessRating)) {
/* 108 */           this.impactBulletPower = highBulletPower;
/* 109 */           this.impactLocation = highTargetLocation;
/* 110 */           this.impactSuccessRating = highSuccessRating;
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   private double getRateOfDamage(double bulletPower, Location impactLocation, double successRating)
/*     */   {
/* 125 */     long timeBetweenShots = Math.round(Rules.getGunHeat(bulletPower) / BattleConstants.getInstance().getGunCoolingRate());
/* 126 */     double damagePerShot = Rules.getBulletDamage(bulletPower);
/* 127 */     return damagePerShot / timeBetweenShots * successRating;
/*     */   }
/*     */ 
/*     */   private double getLowerBulletPower()
/*     */   {
/* 143 */     return this.impactBulletPower * 0.75D;
/*     */   }
/*     */ 
/*     */   private void calculateBestImpactLocation()
/*     */   {
/* 150 */     this.impactLocation = null;
/* 151 */     this.impactSuccessRating = -99.0D;
/* 152 */     double bulletSpeed = Rules.getBulletSpeed(this.impactBulletPower);
/* 153 */     String category = this.myRobot.getShootingCategory(this.enemy, bulletSpeed);
/*     */ 
/* 155 */     for (AimingStrategy strategy : this.enemy.getAimingStrategies()) {
/* 156 */       double estimatedSuccess = strategy.getEstimatedSuccessOfShot(category, bulletSpeed);
/* 157 */       if (estimatedSuccess > this.impactSuccessRating) {
/* 158 */         this.impactSuccessRating = estimatedSuccess;
/* 159 */         this.impactLocation = strategy.getTargetForShot(bulletSpeed);
/* 160 */         this.impactAimingStrategy = strategy;
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   private void setupAimingStrategies() {
/* 166 */     if (this.enemy.getAimingStrategies().isEmpty()) {
/* 167 */       this.enemy.addAimingStrategy(new AimByRepeatingLastMove(this.myRobot, this.enemy, this.isLearningAllowed));
/* 168 */       this.enemy.addAimingStrategy(new AimForRecentLocation(this.myRobot, this.enemy, this.isLearningAllowed));
/* 169 */       this.enemy.addAimingStrategy(new AimByInterpolatingStraightAhead(this.myRobot, this.enemy, this.isLearningAllowed));
/* 170 */       this.enemy.addAimingStrategy(new AimByMovementSequenceReplay(this.myRobot, this.enemy, this.isLearningAllowed));
/* 171 */       this.enemy.addAimingStrategy(new AimForAverageLocation(this.myRobot, this.enemy, this.isLearningAllowed, 50L, 10));
/* 172 */       this.enemy.addAimingStrategy(new AimByRepeatingLastMoveImproved(this.myRobot, this.enemy, this.isLearningAllowed));
/* 173 */       this.enemy.addAimingStrategy(new AimBetweenFurtherstPossibleForwardsAndBackwards(this.myRobot, this.enemy, this.isLearningAllowed));
/* 174 */       this.enemy.addAimingStrategy(new AimBySprayingAtLongDistance(this.myRobot, this.enemy, this.isLearningAllowed));
/*     */     }
/*     */   }
/*     */ 
/*     */   public double getFire()
/*     */   {
/* 181 */     if ((this.impactLocation != null) && 
/* 182 */       (this.impactSuccessRating >= 0.0D) && 
/* 183 */       (this.impactLocation.isOnBattlefield(this.myRobot.getBattleFieldWidth(), this.myRobot.getBattleFieldHeight())) && 
/* 184 */       (this.enemy.isAlive()) && 
/* 185 */       (this.impactBulletPower > 0.0D) && 
/* 186 */       (this.myRobot.getGunTurnRemaining() == 0.0D) && 
/* 187 */       (this.enemy.getLatestRadarObservation() != null) && 
/* 188 */       (this.enemy.getLatestRadarObservation().getTimeSeen() == this.myRobot.getTime()))
/*     */     {
/* 193 */       return this.impactBulletPower;
/*     */     }
/*     */ 
/* 198 */     return 0.0D;
/*     */   }
/*     */ 
/*     */   private double getMaximumBulletPower()
/*     */   {
/* 205 */     if ((this.myRobot.getEnergy() < 0.1D) && (this.enemy.getLatestRadarObservation().getEnergy() > 0.0D)) {
/* 206 */       return 0.0D;
/*     */     }
/*     */ 
/* 209 */     double safeMaxPower = this.myRobot.getEnergy() / (this.remainingBullets + 1);
/* 210 */     double enemyEnergy = this.enemy.getLatestRadarObservation().getEnergy();
/* 211 */     double optimumKillingPower = BulletPowerUtils.getAverageBulletPowerToDestroy(enemyEnergy);
/*     */ 
/* 217 */     return Math.min(safeMaxPower, Math.min(this.shootingMaxPower, optimumKillingPower));
/*     */   }
/*     */ 
/*     */   public void executed()
/*     */   {
/* 223 */     if (this.myRobot.getJustFired()) {
/* 224 */       this.shootingMaxPower *= this.shootingPowerDecayFactor;
/* 225 */       this.impactTimeOfPreviousShot = (this.myRobot.getTime() + Math.round(Geometry.getDistanceBetweenLocations(this.myRobot.getLocation(), this.impactLocation) / Rules.getBulletSpeed(this.impactBulletPower)));
/* 226 */       this.impactLocationOfPreviousShot = this.impactLocation;
/*     */     }
/*     */ 
/* 229 */     setImpactLocation();
/*     */ 
/* 231 */     for (AimingStrategy strategy : this.enemy.getAimingStrategies())
/* 232 */       strategy.processTurn();
/*     */   }
/*     */ 
/*     */   public EnemyRobot getEnemyTarget()
/*     */   {
/* 238 */     return this.enemy;
/*     */   }
/*     */ 
/*     */   public static void displayAimingDebugStats()
/*     */   {
/*     */   }
/*     */ 
/*     */   public void hitEnemy(EnemyRobot enemy)
/*     */   {
/* 247 */     this.shootingMaxPower = 3.0D;
/*     */   }
/*     */ 
/*     */   public void paint(Graphics2D g)
/*     */   {
/* 252 */     this.enemy.drawCircle(g, Color.WHITE, 45);
/*     */   }
/*     */ }

/* Location:           E:\naval-robocode\sample\dmh.robocode.robot.BlackDeathMk7_7.0.jar
 * Qualified Name:     dmh.robocode.gunner.GunnerBlastEnemy
 * JD-Core Version:    0.6.2
 */