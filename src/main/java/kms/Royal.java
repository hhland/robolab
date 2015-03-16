/*     */ package kms;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import java.awt.geom.Point2D;
/*     */
/*     */ import java.awt.geom.RoundRectangle2D;
/*     */ import java.awt.geom.RoundRectangle2D.Double;
/*     */ import robocode.AdvancedRobot;
/*     */ import robocode.ScannedRobotEvent;
/*     */ import robocode.util.Utils;
/*     */ 
/*     */ public class Royal extends AdvancedRobot
/*     */ {
/*     */   static final double MAX_VELOCITY = 8.0D;
/*     */   static final double WALL_MARGIN = 25.0D;
/*     */   Point2D robotLocation;
/*     */   Point2D enemyLocation;
/*     */   double enemyDistance;
/*     */   double enemyAbsoluteBearing;
/*  28 */   double movementLateralAngle = 0.2D;
/*  29 */   double randomizerFactor = 0.0D;
/*     */ 
/*  31 */   static double deathCount = 0.0D;
/*  32 */   static double winCount = 0.0D;
/*     */   static final int maxMatchLen = 30;
/*     */   static final int targetDistance = 200;
/*     */   static final int firePower = 2;
/*     */   static final int fireSpeed = 14;
/*     */   static int distance;
/* 167 */   static String enemyHistory = "";
/*     */ 
/*     */   public void run()
/*     */   {
/*  41 */     setAdjustRadarForGunTurn(true);
/*  42 */     setAdjustGunForRobotTurn(true);
/*     */ 
/*  44 */     setColors(Color.red, Color.white, Color.white);
/*     */     while (true)
/*     */     {
/*  47 */       turnRadarRightRadians(Royal.distance = 1000000);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void onDeath()
/*     */   {
/*  53 */     deathCount += 1.0D;
/*     */   }
/*     */   public void onWin() {
/*  56 */     winCount += 1.0D;
/*     */   }
/*     */ 
/*     */   public void onScannedRobot(ScannedRobotEvent e)
/*     */   {
/*  66 */     this.robotLocation = new Point2D.Double(getX(), getY());
/*  67 */     this.enemyAbsoluteBearing = (getHeadingRadians() + e.getBearingRadians());
/*  68 */     this.enemyDistance = e.getDistance();
/*  69 */     this.enemyLocation = vectorToLocation(this.enemyAbsoluteBearing, this.enemyDistance, this.robotLocation);
/*     */ 
/*  71 */     if (deathCount > 2.0D)
/*     */     {
/*  73 */       this.randomizerFactor += 0.01D;
/*     */     }
/*  75 */     if (winCount > deathCount + 1.0D)
/*     */     {
/*  77 */       if (this.randomizerFactor > 0.09D) {
/*  78 */         this.randomizerFactor -= 0.01D;
/*     */       }
/*     */     }
/*     */ 
/*  82 */     if (Math.random() < this.randomizerFactor) {
/*  83 */       this.movementLateralAngle *= -1.0D;
/*     */     }
/*  85 */     move();
/*     */ 
/*  88 */     int matchLen = 30;
/*     */     int dist;
/*  94 */     if ((dist = (int)e.getDistance()) < distance + 32) { double a = e.getBearingRadians() + getHeadingRadians();
/*     */ 
/*  98 */       setTurnRadarRightRadians((1.0D / 0.0D) * 
/*  99 */         Utils.normalRelativeAngle(a - getRadarHeadingRadians()));
/*     */ 
/* 101 */       enemyHistory = 
/* 102 */         String.valueOf((char)(int)Math.rint(
/* 103 */         e.getVelocity() * Math.sin(e.getHeadingRadians() - a)))
/* 104 */         .concat(enemyHistory);
/*     */       int i;
/*     */       int matchPos;
/* 106 */       while ((matchPos = 
/* 107 */         enemyHistory.indexOf(
/* 108 */         enemyHistory.substring(0, matchLen--), 
/* 109 */         i = dist / 14)) < 0);
/*     */       do { a += (short)enemyHistory.charAt(--matchPos) / dist;
/* 115 */         i--; } while (i > 0);
/* 116 */       setTurnGunRightRadians(
/* 117 */         Utils.normalRelativeAngle(a - getGunHeadingRadians()));
/*     */ 
/* 120 */       fire(2.0D);
/*     */     }
/*     */ 
/* 124 */     setTurnRadarRightRadians(Utils.normalRelativeAngle(this.enemyAbsoluteBearing - getRadarHeadingRadians()) * 2.0D);
/*     */   }
/*     */ 
/*     */   void move()
/*     */   {
/* 130 */     Point2D robotDestination = null;
/* 131 */     double tries = 0.0D;
/*     */     do {
/* 133 */       robotDestination = vectorToLocation(absoluteBearing(this.enemyLocation, this.robotLocation) + this.movementLateralAngle, 
/* 134 */         this.enemyDistance * (1.1D - tries / 100.0D), this.enemyLocation);
/* 135 */       tries += 1.0D;
/* 136 */     }while ((tries < 100.0D) && (!
/* 136 */       fieldRectangle(25.0D).contains(robotDestination)));
/* 137 */     goTo(robotDestination);
/*     */   }
/*     */ 
/*     */   RoundRectangle2D fieldRectangle(double margin) {
/* 141 */     return new RoundRectangle2D.Double(margin, margin, 
/* 142 */       getBattleFieldWidth() - margin * 2.0D, getBattleFieldHeight() - margin * 2.0D, 75.0D, 75.0D);
/*     */   }
/*     */ 
/*     */   void goTo(Point2D destination) {
/* 146 */     double angle = Utils.normalRelativeAngle(absoluteBearing(this.robotLocation, destination) - getHeadingRadians());
/* 147 */     double turnAngle = Math.atan(Math.tan(angle));
/* 148 */     setTurnRightRadians(turnAngle);
/* 149 */     setAhead(this.robotLocation.distance(destination) * (angle == turnAngle ? 1 : -1));
/*     */ 
/* 151 */     setMaxVelocity(Math.abs(getTurnRemaining()) > 33.0D ? 0.0D : 8.0D);
/*     */   }
/*     */ 
/*     */   static Point2D vectorToLocation(double angle, double length, Point2D sourceLocation) {
/* 155 */     return vectorToLocation(angle, length, sourceLocation, new Point2D.Double());
/*     */   }
/*     */ 
/*     */   static Point2D vectorToLocation(double angle, double length, Point2D sourceLocation, Point2D targetLocation) {
/* 159 */     targetLocation.setLocation(sourceLocation.getX() + Math.sin(angle) * length, 
/* 160 */       sourceLocation.getY() + Math.cos(angle) * length);
/* 161 */     return targetLocation;
/*     */   }
/*     */ 
/*     */   static double absoluteBearing(Point2D source, Point2D target) {
/* 165 */     return Math.atan2(target.getX() - source.getX(), target.getY() - source.getY());
/*     */   }
/*     */ }

/* Location:           E:\naval-robocode\sample\kms.Royal_0.15M.jar
 * Qualified Name:     kms.Royal
 * JD-Core Version:    0.6.2
 */