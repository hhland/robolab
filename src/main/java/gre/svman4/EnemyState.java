/*     */ package gre.svman4;
/*     */ 
/*     */ import java.awt.Graphics2D;
/*     */ import java.awt.geom.Rectangle2D;
import java.awt.geom.Rectangle2D.Double;
/*     */ import java.util.ArrayList;
/*     */ 
/*     */ final class EnemyState extends RobotState
/*     */ {
/*     */   private static final long serialVersionUID = 5185811350477189479L;
/*     */   public int[] hits;
/*     */   public static final int WAVE_BINS = 47;
/*     */   public static final int WAVE_VELOCITY_INDEXES = 5;
/*     */   public static final int WAVE_DISTANCE_INDEXES = 5;
/*     */   public static final int BINS = 42;
/*  34 */   public final double[][][] waveStats = new double[5][5][47];
/*     */ 
/*  39 */   public boolean isAlive = true;
/*     */   public String name;
/*  50 */   public ArrayList<Double> surfAbsBearings = new ArrayList();
/*  51 */   public ArrayList<Integer> surfDirections = new ArrayList();
/*     */ 
/*  56 */   public static double[][][][] gunBuffers = new double[5][5][5][42];
/*     */   public int lateralDirection;
/*     */   public double lastVelocity;
/*     */   public static final int WAVE_BEARING_BINS = 5;
/*     */ 
/*     */   public EnemyState(int numberOfGuns)
/*     */   {
/*  16 */     this.hits = new int[numberOfGuns];
/*     */   }
/*     */ 
/*     */   public void onPaint(Graphics2D g)
/*     */   {
/*  46 */     if (this.isAlive)
/*  47 */       g.draw(new Rectangle2D.Double(this.x - 18.0D, this.y - 18.0D, 36.0D, 36.0D));
/*     */   }
/*     */ 
/*     */   public double[] getGunCurrentStatus(double distance, double velocity, double lastVelocity)
/*     */   {
/*  82 */     int distanceIndex = (int)(distance / 180.0D);
/*  83 */     int velocityIndex = (int)Math.abs(velocity / 2.0D);
/*  84 */     int lastVelocityIndex = (int)Math.abs(lastVelocity / 2.0D);
/*  85 */     return gunBuffers[distanceIndex][velocityIndex][lastVelocityIndex];
/*     */   }
/*     */ 
/*     */   public void logGunHit(double distance, double velocity, double lastVelocity, int offsetIndex)
/*     */   {
/*  90 */     int distanceIndex = (int)(distance / 180.0D);
/*  91 */     int velocityIndex = (int)Math.abs(velocity / 2.0D);
/*  92 */     int lastVelocityIndex = (int)Math.abs(lastVelocity / 2.0D);
/*  93 */     double temp = 0.0D;
/*  94 */     if (gunBuffers[distanceIndex][velocityIndex][lastVelocityIndex][offsetIndex] > 42.0D) {
/*  95 */       temp = 1.0D;
/*     */     }
/*  97 */     for (int x = 0; x < 42; x++)
/*  98 */       gunBuffers[distanceIndex][velocityIndex][lastVelocityIndex][offsetIndex] += 1.0D / (Math.pow(offsetIndex - x, 2.0D) + 1.0D) - temp;
/*     */   }
/*     */ 
/*     */   public void logHit(double mineRobotVelocity, double distanceToEnemyPosition, int indexAngle)
/*     */   {
/* 121 */     int indexVelocity = (int)Math.min(Math.abs(mineRobotVelocity) / 
/* 122 */       1.0D, 
/* 123 */       4.0D);
/* 124 */     int indexDistance = (int)Math.min(Math.abs(distanceToEnemyPosition) / 
/* 125 */       240.0D, 
/* 126 */       4.0D);
/* 127 */     int temp = 0;
/* 128 */     if (this.waveStats[indexDistance][indexVelocity][indexAngle] > 5.0D) {
/* 129 */       temp = 1;
/*     */     }
/* 131 */     for (int t = 0; t < 5; t++)
/* 132 */       for (int y = 0; y < 5; y++)
/* 133 */         for (int x = 0; x < 47; x++)
/*     */         {
/* 138 */           this.waveStats[t][y][x] += 1.0D / (
/* 139 */             Math.pow(indexAngle - x, 2.0D) + Math.pow(indexVelocity - y, 2.0D) + 
/* 140 */             Math.pow(indexDistance - t, 2.0D) + 1.0D) - temp;
/* 141 */           if (this.waveStats[t][y][x] < 0.0D)
/* 142 */             this.waveStats[t][y][x] = 0.0D;
/*     */         }
/*     */   }
/*     */ 
/*     */   private double[] getEnemyGunCurrentStatus(int indexVelocity, int indexDistance)
/*     */   {
/* 151 */     return this.waveStats[indexDistance][indexVelocity];
/*     */   }
/*     */ 
/*     */   public double[] getEnemyGunCurrentStatus(double mineVelocity, double angle, double distance)
/*     */   {
/* 171 */     int indexVelocity = (int)Math.min(Math.abs(mineVelocity) / 
/* 172 */       1.0D, 4.0D);
/* 173 */     int indexDistance = (int)Math.min(Math.abs(distance) / 
/* 174 */       240.0D, 5.0D);
/* 175 */     return getEnemyGunCurrentStatus(indexVelocity, indexDistance);
/*     */   }
/*     */ }

/* Location:           E:\naval-robocode\sample\gre.svman4.Morfeas_1.4.jar
 * Qualified Name:     gre.svman4.EnemyState
 * JD-Core Version:    0.6.2
 */