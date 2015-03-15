/*     */ package gre.svman4;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import java.awt.Graphics2D;
/*     */ import robocode.AdvancedRobot;
/*     */ import robocode.util.Utils;
/*     */ 
/*     */ public abstract class Movement
/*     */ {
/*  16 */   protected RobotState mineRobot = new RobotState();
/*  17 */   protected FieldPoint nextPosition = new FieldPoint();
/*     */   protected double movingAngle;
/* 118 */   public static double MAX_DISTANCE = 150.0D;
/*     */ 
/* 123 */   public static double MIN_ANGLE_TO_LOW_SPEED = 25.0D;
/*     */ 
/* 128 */   public static double SPEED_ON_ROBOT_TURN = 2.5D;
/*     */ 
/*     */   public Movement(RobotState me)
/*     */   {
/*  20 */     setMe(me);
/*     */   }
/*     */ 
/*     */   public abstract void run(AdvancedRobot paramAdvancedRobot);
/*     */ 
/*     */   public FieldPoint getNextPosition()
/*     */   {
/*  28 */     return this.nextPosition;
/*     */   }
/*     */ 
/*     */   public void setMe(RobotState me) {
/*  32 */     this.mineRobot = me;
/*     */   }
/*     */ 
/*     */   public abstract String getName();
/*     */ 
/*     */   public abstract Color getColor();
/*     */ 
/*     */   public void onPaint(Graphics2D g)
/*     */   {
/*  41 */     g.setColor(getColor(0.0D, 8.0D - Math.abs(this.mineRobot.velocity), 8.0D));
/*  42 */     double temp = Math.abs(this.mineRobot.velocity / 8.0D) * 50.0D;
/*  43 */     FieldPoint tempPoint = this.mineRobot.project(15.0D + temp, this.movingAngle);
/*  44 */     g.drawLine((int)this.mineRobot.x, (int)this.mineRobot.y, (int)tempPoint.x, 
/*  45 */       (int)tempPoint.y);
/*  46 */     g.fillOval((int)(tempPoint.x - 1.5D), (int)(tempPoint.y - 1.5D), 3, 3);
/*     */   }
/*     */ 
/*     */   protected void goTo(FieldPoint destination, AdvancedRobot robot)
/*     */   {
/*  59 */     double distance = this.mineRobot.distance(destination);
/*  60 */     this.movingAngle = Math.toRadians(normalRelativeAngle(Math.toDegrees(this.mineRobot
/*  61 */       .getAngleTo(destination)) - robot.getHeading()));
/*  62 */     this.movingAngle = this.mineRobot.getAngleTo(destination);
/*  63 */     goTo(this.movingAngle, distance, robot);
/*     */   }
/*     */ 
/*     */   public Color getColor(double lower, double val, double higher)
/*     */   {
/*  71 */     double range = higher - lower;
/*  72 */     double value = Math.min(range, Math.max(val - lower, 0.0D));
/*  73 */     double H = (range - value) / range * 0.4D;
/*     */ 
/*  75 */     double S = 0.9D;
/*  76 */     double B = 0.9D;
/*  77 */     return Color.getHSBColor((float)H, (float)S, (float)B);
/*     */   }
/*     */ 
/*     */   public void goTo(double goAngle, double distance, AdvancedRobot robot)
/*     */   {
/*  93 */     double angle = Utils.normalRelativeAngle(goAngle - 
/*  94 */       robot.getHeadingRadians());
/*  95 */     if (Math.abs(angle) > 1.570796326794897D) {
/*  96 */       if (angle < 0.0D)
/*  97 */         robot.setTurnRightRadians(3.141592653589793D + angle);
/*     */       else {
/*  99 */         robot.setTurnLeftRadians(3.141592653589793D - angle);
/*     */       }
/* 101 */       robot.setBack(distance);
/*     */     } else {
/* 103 */       if (angle < 0.0D)
/* 104 */         robot.setTurnLeftRadians(-1.0D * angle);
/*     */       else {
/* 106 */         robot.setTurnRightRadians(angle);
/*     */       }
/* 108 */       robot.setAhead(distance);
/*     */     }
/* 110 */     speedLimit(robot);
/*     */   }
/*     */ 
/*     */   public void speedLimit(AdvancedRobot robot)
/*     */   {
/* 139 */     robot
/* 140 */       .setMaxVelocity(Math.abs(robot.getTurnRemainingRadians()) > MIN_ANGLE_TO_LOW_SPEED ? SPEED_ON_ROBOT_TURN : 
/* 141 */       8.0D);
/*     */   }
/*     */ 
/*     */   private double normalRelativeAngle(double angle)
/*     */   {
/* 146 */     angle = Math.toRadians(angle);
/* 147 */     return Math.toDegrees(Math.atan2(Math.sin(angle), Math.cos(angle)));
/*     */   }
/*     */ }

/* Location:           E:\naval-robocode\sample\gre.svman4.Morfeas_1.4.jar
 * Qualified Name:     gre.svman4.Movement
 * JD-Core Version:    0.6.2
 */