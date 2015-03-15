/*     */ package gre.svman4;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import java.awt.Graphics2D;
/*     */ import java.awt.geom.Rectangle2D;
/*     */ import java.util.ArrayList;
/*     */ import robocode.AdvancedRobot;
/*     */ import robocode.util.Utils;
/*     */ 
/*     */ public class WaveSurfer extends OneVsOneMovementStrategy
/*     */ {
/*     */   private static final double DISTANCE_FROM_ENEMY = 300.0D;
/*     */   private ArrayList<EnemyWaveBullet> waves;
/*     */ 
/*     */   public WaveSurfer(RobotState me, Rectangle2D movingArea, EnemyState target, ArrayList<EnemyWaveBullet> waves)
/*     */   {
/*  24 */     super(me, movingArea, target);
/*  25 */     setWaves(waves);
/*     */   }
/*     */ 
/*     */   public void setWaves(ArrayList<EnemyWaveBullet> wave)
/*     */   {
/*  31 */     this.waves = wave;
/*     */   }
/*     */ 
/*     */   public void run(AdvancedRobot robot) {
/*  35 */     EnemyWaveBullet surfWave = getClosestSurfableWave();
/*  36 */     if (surfWave == null) {
/*  37 */       return;
/*     */     }
/*  39 */     double dangerLeft = checkDanger(surfWave, -1);
/*  40 */     double dangerRight = checkDanger(surfWave, 1);
/*     */ 
/*  44 */     this.movingAngle = this.target.getAngleTo(this.mineRobot);
/*  45 */     if (dangerLeft < dangerRight) {
/*  46 */       this.movingAngle -= 1.570796326794897D;
/*  47 */       this.movingAngle = wallSmoothing(this.mineRobot, this.movingAngle, -1);
/*     */     } else {
/*  49 */       this.movingAngle += 1.570796326794897D;
/*  50 */       this.movingAngle = wallSmoothing(this.mineRobot, this.movingAngle, 1);
/*     */     }
/*  52 */     goTo(this.movingAngle, 50.0D, robot);
/*     */   }
/*     */ 
/*     */   public FieldPoint predictPosition(EnemyWaveBullet surfWave, int direction)
/*     */   {
/*  57 */     FieldPoint predictedPosition = (FieldPoint)this.mineRobot.clone();
/*  58 */     double predictedVelocity = this.mineRobot.velocity;
/*  59 */     double predictedHeading = this.mineRobot.heading;
/*     */ 
/*  61 */     int counter = 0;
/*  62 */     boolean intercepted = false;
/*     */     do {
/*  64 */       double moveAngle = wallSmoothing(predictedPosition, 
/*  65 */         surfWave.fireLocation.getAngleTo(predictedPosition) + 
/*  66 */         direction * 1.570796326794897D, direction) - 
/*  67 */         predictedHeading;
/*  68 */       double moveDir = 1.0D;
/*     */ 
/*  70 */       if (Math.cos(moveAngle) < 0.0D) {
/*  71 */         moveAngle += 3.141592653589793D;
/*  72 */         moveDir = -1.0D;
/*     */       }
/*  74 */       moveAngle = Utils.normalRelativeAngle(moveAngle);
/*     */ 
/*  77 */       double maxTurning = 0.004363323129985824D * (40.0D - 3.0D * Math.abs(predictedVelocity));
/*  78 */       predictedHeading = Utils.normalRelativeAngle(predictedHeading + 
/*  79 */         EnemyWaveBullet.limit(-maxTurning, moveAngle, maxTurning));
/*     */ 
/*  84 */       predictedVelocity = predictedVelocity + (predictedVelocity * moveDir < 0.0D ? 2.0D * moveDir : 
/*  84 */         moveDir);
/*  85 */       predictedVelocity = EnemyWaveBullet.limit(-8.0D, predictedVelocity, 8.0D);
/*     */ 
/*  87 */       predictedPosition = predictedPosition.project(predictedVelocity, 
/*  88 */         predictedHeading);
/*  89 */       counter++;
/*     */ 
/*  91 */       if (predictedPosition.distance(surfWave.fireLocation) < surfWave.distanceTraveled + 
/*  91 */         counter * surfWave.bulletVelocity + surfWave.bulletVelocity)
/*  92 */         intercepted = true;
/*     */     }
/*  94 */     while ((!intercepted) && (
/*  94 */       counter < 500));
/*  95 */     return predictedPosition;
/*     */   }
/*     */ 
/*     */   public double checkDanger(EnemyWaveBullet surfWave, int direction) {
/*  99 */     int indexAngle = surfWave.getFactorIndex(predictPosition(surfWave, direction));
/* 100 */     return surfWave.getEnemyCurrentStatus()[indexAngle];
/*     */   }
/*     */ 
/*     */   public EnemyWaveBullet getClosestSurfableWave()
/*     */   {
/* 105 */     double closestDistance = 1.7976931348623157E+308D;
/*     */ 
/* 107 */     EnemyWaveBullet surfWave = null;
/* 108 */     for (int x = 0; x < this.waves.size(); x++) {
/* 109 */       EnemyWaveBullet ew = (EnemyWaveBullet)this.waves.get(x);
/* 110 */       double distance = this.mineRobot.distance(ew.fireLocation) - 
/* 111 */         ew.distanceTraveled;
/* 112 */       if ((distance > ew.bulletVelocity) && (distance < closestDistance)) {
/* 113 */         surfWave = ew;
/* 114 */         closestDistance = distance;
/*     */       }
/*     */     }
/* 117 */     return surfWave;
/*     */   }
/*     */ 
/*     */   public String getName() {
/* 121 */     return "Wave surfer";
/*     */   }
/*     */ 
/*     */   public void onPaint(Graphics2D g) {
/* 125 */     super.onPaint(g);
/* 126 */     g.setColor(Color.GREEN);
/* 127 */     FieldPoint tempPoint = new FieldPoint();
/*     */ 
/* 130 */     double temp = this.mineRobot.distance(this.target);
/* 131 */     g.setColor(getColor(-100.0D, -temp + 300.0D, 100.0D));
/* 132 */     g.drawLine((int)this.mineRobot.x, (int)this.mineRobot.y, (int)this.target.x, 
/* 133 */       (int)this.target.y);
/* 134 */     tempPoint.setLocation(this.mineRobot.getMeanPoint(this.target));
/* 135 */     g.drawString(String.format("%.0fpx", new Object[] { Double.valueOf(temp) }), (int)tempPoint.x, 
/* 136 */       (int)tempPoint.y);
/* 137 */     for (int i = 0; i < this.waves.size(); i++) {
/* 138 */       EnemyWaveBullet wave = (EnemyWaveBullet)this.waves.get(i);
/* 139 */       wave.onPaint(g);
/*     */     }
/*     */   }
/*     */ 
/*     */   public Color getColor()
/*     */   {
/* 145 */     return Color.yellow;
/*     */   }
/*     */ }

/* Location:           E:\naval-robocode\sample\gre.svman4.Morfeas_1.4.jar
 * Qualified Name:     gre.svman4.WaveSurfer
 * JD-Core Version:    0.6.2
 */