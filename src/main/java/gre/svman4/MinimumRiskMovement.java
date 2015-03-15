/*     */ package gre.svman4;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import java.awt.Graphics2D;
/*     */ import java.awt.geom.Point2D.Double;
/*     */ import java.awt.geom.Rectangle2D;
/*     */ import java.awt.geom.Rectangle2D.Double;
/*     */ import java.util.Collection;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import robocode.AdvancedRobot;
/*     */ 
/*     */ public class MinimumRiskMovement extends MelleeMovementStrategy
/*     */ {
/*  23 */   private FieldPoint[] points = new FieldPoint[100];
/*     */ 
/*  27 */   private double[] eval = new double[100];
/*     */ 
/*  40 */   public static double MIN_DISTANCE = 80.0D;
/*     */   public static final int POINTS_COUNTER = 100;
/*  82 */   Point2D.Double lastPosition = new Point2D.Double();
/*     */ 
/*     */   public MinimumRiskMovement(RobotState me, Rectangle2D.Double movingPlace, HashMap<String, EnemyState> enemies)
/*     */   {
/*  31 */     super(me, movingPlace, enemies);
/*  32 */     for (int i = 0; i < this.points.length; i++)
/*  33 */       this.points[i] = new FieldPoint(0.0D, 0.0D);
/*     */   }
/*     */ 
/*     */   public void run(AdvancedRobot robot)
/*     */   {
/*  51 */     int counter = 0;
/*     */ 
/*  54 */     double betterEval = 1.7976931348623157E+308D;
/*  55 */     double addLast = 1.0D - Math.rint(Math.pow(0.3D, this.enemies.size()));
/*  56 */     double distance = MIN_DISTANCE;
/*     */     do {
/*  58 */       double angle = counter / 100.0D * 2.0D * 3.141592653589793D;
/*  59 */       this.points[counter].setLocation(this.mineRobot.project(distance, angle));
/*  60 */       this.eval[counter] = evaluate(this.points[counter], addLast);
/*  61 */       if ((this.movingPlace.contains(this.points[counter])) && 
/*  62 */         (this.eval[counter] < betterEval)) {
/*  63 */         this.nextPosition.setLocation(this.points[counter]);
/*  64 */         betterEval = this.eval[counter];
/*     */       }
/*  66 */       counter++;
/*     */     }
/*  57 */     while (
/*  67 */       counter < 100);
/*  68 */     goTo(this.nextPosition, robot);
/*  69 */     if (Math.abs(this.lastPosition.distance(this.mineRobot)) > 50.0D)
/*  70 */       this.lastPosition.setLocation(this.mineRobot);
/*     */   }
/*     */ 
/*     */   public double evaluate(FieldPoint point, double addLast)
/*     */   {
/*  99 */     double ath = addLast * 0.8D / point.distanceSq(this.lastPosition);
/*     */ 
/* 103 */     Iterator it = this.enemies.values().iterator();
/* 104 */     while (it.hasNext()) {
/* 105 */       EnemyState en = (EnemyState)it.next();
/* 106 */       if (en.isAlive)
/*     */       {
/* 109 */         ath = ath + Math.min(en.energy / this.mineRobot.energy, 2.0D) * (
/* 108 */           1.0D + Math.abs(Math.cos(this.mineRobot.getAngleTo(point) - 
/* 109 */           en.getAngleTo(point)))) / point.distanceSq(en);
/*     */       }
/*     */     }
/* 112 */     return ath;
/*     */   }
/*     */ 
/*     */   public String getName()
/*     */   {
/* 119 */     return "MinimumRiskMovement";
/*     */   }
/*     */ 
/*     */   public void onPaint(Graphics2D g)
/*     */   {
/* 126 */     super.onPaint(g);
/*     */ 
/* 131 */     g.fillOval((int)(this.lastPosition.x - 1.5D), (int)(this.lastPosition.y - 1.5D), 3, 3);
/* 132 */     double biggestEval = 4.9E-324D;
/* 133 */     double lowerEval = 1.7976931348623157E+308D;
/* 134 */     for (int i = 0; i < 100; i++) {
/* 135 */       if (this.eval[i] > biggestEval)
/* 136 */         biggestEval = this.eval[i];
/* 137 */       else if (this.eval[i] < lowerEval) {
/* 138 */         lowerEval = this.eval[i];
/*     */       }
/*     */     }
/* 141 */     for (int i = 0; i < 100; i++)
/* 142 */       if (this.movingPlace.contains(this.points[i])) {
/* 143 */         g.setColor(getColor(lowerEval, biggestEval, this.eval[i]));
/* 144 */         g.fillOval((int)(this.points[i].x - 2.0D), (int)(this.points[i].y - 2.0D), 4, 4);
/*     */       }
/*     */   }
/*     */ 
/*     */   public Color getColor(double lower, double higher, double val)
/*     */   {
/* 155 */     double range = higher - lower;
/* 156 */     double value = Math.min(range, Math.max(val - lower, 0.0D));
/* 157 */     double H = (range - value) / range * 0.4D;
/*     */ 
/* 159 */     double S = 0.9D;
/* 160 */     double B = 0.9D;
/* 161 */     return Color.getHSBColor((float)H, (float)S, (float)B);
/*     */   }
/*     */ 
/*     */   public Color getColor() {
/* 165 */     return Color.BLUE;
/*     */   }
/*     */ }

/* Location:           E:\naval-robocode\sample\gre.svman4.Morfeas_1.4.jar
 * Qualified Name:     gre.svman4.MinimumRiskMovement
 * JD-Core Version:    0.6.2
 */