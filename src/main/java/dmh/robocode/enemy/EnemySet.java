/*     */ package dmh.robocode.enemy;
/*     */ 
/*     */ import dmh.robocode.data.Location;
/*     */ import dmh.robocode.data.RadarObservation;
/*     */ import dmh.robocode.gunner.aiming.AimingStrategy;
/*     */ import dmh.robocode.utils.Geometry;
/*     */ import java.awt.Color;
/*     */ import java.awt.Graphics2D;
/*     */ import java.io.PrintStream;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashSet;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ 
/*     */ public class EnemySet
/*     */   implements Iterable<EnemyRobot>
/*     */ {
/*     */   private Set<EnemyRobot> enemies;
/*  19 */   private boolean isLearningAllowed = true;
/*     */ 
/*     */   public EnemySet() {
/*  22 */     this.enemies = new HashSet();
/*     */   }
/*     */ 
/*     */   public Iterator<EnemyRobot> iterator()
/*     */   {
/*  27 */     return this.enemies.iterator();
/*     */   }
/*     */ 
/*     */   public EnemyRobot getEnemy(String enemyName) {
/*  31 */     for (EnemyRobot enemy : this.enemies) {
/*  32 */       if (enemy.getName().equals(enemyName)) {
/*  33 */         return enemy;
/*     */       }
/*     */     }
/*     */ 
/*  37 */     EnemyRobot newEnemy = new EnemyRobot(enemyName);
/*  38 */     if (!this.isLearningAllowed) {
/*  39 */       newEnemy.turnOffLearning();
/*     */     }
/*  41 */     this.enemies.add(newEnemy);
/*     */ 
/*  43 */     return newEnemy;
/*     */   }
/*     */ 
/*     */   public int getNumberOfEnemies() {
/*  47 */     return this.enemies.size();
/*     */   }
/*     */ 
/*     */   public void addEnemyForTesting(EnemyRobot mockEnemy)
/*     */   {
/*  52 */     this.enemies.add(mockEnemy);
/*     */   }
/*     */ 
/*     */   public int getNumberOfLiveEnemiesSeenSince(long sinceTime) {
/*  56 */     int seen = 0;
/*     */ 
/*  58 */     for (EnemyRobot enemy : this.enemies) {
/*  59 */       if ((enemy.getLatestRadarObservation() != null) && (enemy.isAlive()) && 
/*  60 */         (enemy.getLatestRadarObservation().getTimeSeen() >= sinceTime)) {
/*  61 */         seen++;
/*     */       }
/*     */     }
/*  64 */     return seen;
/*     */   }
/*     */ 
/*     */   public void resetForNextRound() {
/*  68 */     for (EnemyRobot enemy : this.enemies)
/*  69 */       enemy.resetForNextRound();
/*     */   }
/*     */ 
/*     */   public List<AimingStrategy> getAllAimingStrategies()
/*     */   {
/*  74 */     List result = new ArrayList();
/*     */     Iterator localIterator2;
/*  75 */     for (Iterator localIterator1 = this.enemies.iterator(); localIterator1.hasNext(); 
/*  76 */       localIterator2.hasNext())
/*     */     {
/*  75 */       EnemyRobot enemy = (EnemyRobot)localIterator1.next();
/*  76 */       localIterator2 = enemy.getAimingStrategies().iterator();  
                AimingStrategy aimingStrategy = (AimingStrategy)localIterator2.next();
/*  77 */       result.add(aimingStrategy);
/*     */     }
/*     */ 
/*  80 */     return result;
/*     */   }
/*     */ 
/*     */   public void displayDebugEndOfRound()
/*     */   {
/*     */     Iterator localIterator2;
/*  84 */     for (Iterator localIterator1 = this.enemies.iterator(); localIterator1.hasNext(); 
/*  89 */       localIterator2.hasNext())
/*     */     {
/*  84 */       EnemyRobot enemy = (EnemyRobot)localIterator1.next();
/*  85 */       System.out.println("--------------------");
/*  86 */       System.out.println(enemy.getName());
/*  87 */       System.out.println("--------------------");
/*  88 */       System.out.println(enemy.getStatisticsDebugString());
/*  89 */       localIterator2 = enemy.getAimingStrategies().iterator(); 
                AimingStrategy aimingStrategy = (AimingStrategy)localIterator2.next();
/*  90 */       aimingStrategy.debugDisplayStats();
/*     */     }
/*     */   }
/*     */ 
/*     */   public double getTotalDangerEnergy()
/*     */   {
/*  96 */     double dangerEnergy = 0.0D;
/*     */ 
/*  98 */     for (EnemyRobot enemy : this.enemies) {
/*  99 */       if (enemy.isAlive()) {
/* 100 */         dangerEnergy += enemy.getDangerEnergy();
/*     */       }
/*     */     }
/* 103 */     return dangerEnergy;
/*     */   }
/*     */ 
/*     */   public EnemyRobot getClosestLiveEnemy(Location myLocation)
/*     */   {
/* 108 */     double closestDistance = 0.0D;
/* 109 */     EnemyRobot closestEnemy = null;
/*     */ 
/* 111 */     for (EnemyRobot enemy : this.enemies) {
/* 112 */       RadarObservation observation = enemy.getLatestRadarObservation();
/*     */ 
/* 114 */       if ((enemy.isAlive()) && (observation != null)) {
/* 115 */         double distance = Geometry.getDistanceBetweenLocations(myLocation, observation.getLocation());
/*     */ 
/* 117 */         if ((distance < closestDistance) || (closestEnemy == null)) {
/* 118 */           closestDistance = distance;
/* 119 */           closestEnemy = enemy;
/*     */         }
/*     */       }
/*     */     }
/* 123 */     return closestEnemy;
/*     */   }
/*     */ 
/*     */   public double getTotalDangerEnergyWithGravity(double safeDistance, Location myLocation) {
/* 127 */     double dangerEnergy = 0.0D;
/*     */ 
/* 129 */     for (EnemyRobot enemy : this.enemies) {
/* 130 */       if (enemy.isAlive()) {
/* 131 */         dangerEnergy += enemy.getDangerEnergyWithGravity(safeDistance, myLocation);
/*     */       }
/*     */     }
/*     */ 
/* 135 */     return dangerEnergy;
/*     */   }
/*     */ 
/*     */   public EnemyRobot getBestShootingTarget(Location myLocation)
/*     */   {
/* 140 */     EnemyRobot closestEnemy = getClosestLiveEnemy(myLocation);
/* 141 */     if (closestEnemy == null) {
/* 142 */       return null;
/*     */     }
/*     */ 
/* 145 */     double bestTargetScore = 0.0D;
/* 146 */     EnemyRobot bestTarget = null;
/*     */ 
/* 148 */     double maxDistance = 1.25D * Geometry.getDistanceBetweenLocations(myLocation, closestEnemy.getLatestRadarObservation().getLocation());
/*     */ 
/* 150 */     for (EnemyRobot enemy : this.enemies) {
/* 151 */       RadarObservation observation = enemy.getLatestRadarObservation();
/*     */ 
/* 153 */       if ((enemy.isAlive()) && (observation != null)) {
/* 154 */         double distance = Geometry.getDistanceBetweenLocations(myLocation, observation.getLocation());
/*     */ 
/* 156 */         if (distance <= maxDistance) {
/* 157 */           double targetScore = enemy.getShootingAccuracy(distance);
/*     */ 
/* 159 */           if ((targetScore > bestTargetScore) || (bestTarget == null)) {
/* 160 */             bestTargetScore = targetScore;
/* 161 */             bestTarget = enemy;
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/* 166 */     return bestTarget;
/*     */   }
/*     */ 
/*     */   public double getBestDoubleTargetEnergyWithGravity(double safeDistance, Location myLocation) {
/* 170 */     double best = 0.0D;
/* 171 */     double secondBest = 0.0D;
/*     */ 
/* 173 */     for (EnemyRobot enemy : this.enemies) {
/* 174 */       if (enemy.isAlive()) {
/* 175 */         double targetEnergy = enemy.getTargetEnergyWithGravity(safeDistance, myLocation);
/*     */ 
/* 177 */         if (targetEnergy >= best) {
/* 178 */           secondBest = best;
/* 179 */           best = targetEnergy;
/*     */         } else {
/* 181 */           secondBest = Math.max(targetEnergy, secondBest);
/*     */         }
/*     */       }
/*     */     }
/*     */ 
/* 186 */     return best + secondBest;
/*     */   }
/*     */ 
/*     */   public EnemySet getLiveEnemiesSeenSince(long sinceTime) {
/* 190 */     EnemySet filteredSet = new EnemySet();
/*     */ 
/* 192 */     for (EnemyRobot enemy : this.enemies) {
/* 193 */       if ((enemy.getLatestRadarObservation() != null) && (enemy.isAlive()) && 
/* 194 */         (enemy.getLatestRadarObservation().getTimeSeen() >= sinceTime)) {
/* 195 */         filteredSet.enemies.add(enemy);
/*     */       }
/*     */     }
/* 198 */     return filteredSet;
/*     */   }
/*     */ 
/*     */   public void paint(Graphics2D g, Color upToDateColour, Color fairlyRecentColour, int radius, long currentTime) {
/* 202 */     for (EnemyRobot enemy : this.enemies) {
/* 203 */       enemy.drawCircle(g, upToDateColour, fairlyRecentColour, radius, currentTime);
/* 204 */       drawAverageLocationBlob(g, enemy);
/*     */     }
/*     */   }
/*     */ 
/*     */   private void drawAverageLocationBlob(Graphics2D g, EnemyRobot enemy) {
/* 209 */     Location averageLocation = enemy.getAverageLocation();
/* 210 */     if (averageLocation != null) {
/* 211 */       g.setColor(Color.CYAN);
/* 212 */       g.fillOval((int)averageLocation.getX() - 5, (int)averageLocation.getY() - 5, 10, 10);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void turnOffLearning() {
/* 217 */     this.isLearningAllowed = false;
/*     */   }
/*     */ 
/*     */   public EnemySet getAllLiveEnemiesCloserThan(Location location, double distance)
/*     */   {
/* 222 */     EnemySet filteredSet = new EnemySet();
/*     */ 
/* 224 */     for (EnemyRobot enemy : this.enemies) {
/* 225 */       if ((enemy.getLatestRadarObservation() != null) && (enemy.isAlive()) && 
/* 226 */         (Geometry.getDistanceBetweenLocations(location, enemy.getLatestRadarObservation().getLocation()) < distance)) {
/* 227 */         filteredSet.enemies.add(enemy);
/*     */       }
/*     */     }
/* 230 */     return filteredSet;
/*     */   }
/*     */ 
/*     */   public EnemySet getEnemiesWithAtLeastEnergy(double minimumEnergy) {
/* 234 */     EnemySet filteredSet = new EnemySet();
/*     */ 
/* 236 */     for (EnemyRobot enemy : this.enemies) {
/* 237 */       if ((enemy.getLatestRadarObservation() != null) && (enemy.isAlive()) && 
/* 238 */         (enemy.getLatestRadarObservation().getEnergy() >= minimumEnergy)) {
/* 239 */         filteredSet.enemies.add(enemy);
/*     */       }
/*     */     }
/* 242 */     return filteredSet;
/*     */   }
/*     */ }

/* Location:           E:\naval-robocode\sample\dmh.robocode.robot.BlackDeathMk7_7.0.jar
 * Qualified Name:     dmh.robocode.enemy.EnemySet
 * JD-Core Version:    0.6.2
 */