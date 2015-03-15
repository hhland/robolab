/*     */ package r;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import java.io.BufferedReader;
/*     */ import java.io.FileReader;
/*     */ import java.io.IOException;
/*     */ import java.io.PrintStream;
/*     */ import robocode.AdvancedRobot;
/*     */ import robocode.HitByBulletEvent;
/*     */ import robocode.HitRobotEvent;
/*     */ import robocode.HitWallEvent;
/*     */ import robocode.ScannedRobotEvent;
/*     */ 
/*     */ public class GenRobot extends AdvancedRobot
/*     */ {
/*     */   public double[] p;
/*     */   public int[] run;
/*     */   public int[] scan;
/*     */   public int[] hbb;
/*     */   public int[] hw;
/*     */   public int[] hr;
/*     */ 
/*     */   public void run()
/*     */   {
/*  18 */     this.out.println("1.Robot zagrugen");
/*  19 */     setColors(Color.green, Color.green, Color.green);
/*     */     try {
/*  21 */       BufferedReader reader = new BufferedReader(new FileReader(getDataFile("robot.gen")));
/*  22 */       String s = reader.readLine();
/*  23 */       reader.close();
/*     */ 
/*  25 */       this.out.println(s);
/*  26 */       this.out.println("2.FileReader zagrugen");
/*     */ 
/*  28 */       String[] a = s.split("/");
/*  29 */       String[] m = a[0].split(" ");
/*  30 */       this.p = new double[m.length];
/*  31 */       for (int i = 0; i < m.length; i++) {
/*  32 */         this.p[i] = Double.parseDouble(m[i]);
/*     */       }
/*  34 */       m = a[1].split(";");
/*  35 */       s = "";
/*  36 */       for (int i = 0; i < m.length; i++)
/*     */       {
/*  38 */         String[] m2 = m[i].split(" ");
/*  39 */         for (int u = 0; u < m2.length; u++)
/*  40 */           s = s + m2[u] + " ";
/*     */       }
/*  42 */       s = s.substring(0, s.length() - 1);
/*  43 */       this.out.println(s);
/*  44 */       m = s.split(" ");
/*  45 */       this.run = new int[m.length];
/*  46 */       for (int i = 0; i < m.length; i++) {
/*  47 */         this.run[i] = Integer.parseInt(m[i]);
/*     */       }
/*  49 */       m = a[2].split(";");
/*  50 */       s = "";
/*  51 */       for (int i = 0; i < m.length; i++)
/*     */       {
/*  53 */         String[] m2 = m[i].split(" ");
/*  54 */         for (int u = 0; u < m2.length; u++)
/*  55 */           s = s + m2[u] + " ";
/*     */       }
/*  57 */       s = s.substring(0, s.length() - 1);
/*  58 */       this.out.println(s);
/*  59 */       m = s.split(" ");
/*  60 */       this.scan = new int[m.length];
/*  61 */       for (int i = 0; i < m.length; i++) {
/*  62 */         this.scan[i] = Integer.parseInt(m[i]);
/*     */       }
/*  64 */       m = a[3].split(";");
/*  65 */       s = "";
/*  66 */       for (int i = 0; i < m.length; i++)
/*     */       {
/*  68 */         String[] m2 = m[i].split(" ");
/*  69 */         for (int u = 0; u < m2.length; u++)
/*  70 */           s = s + m2[u] + " ";
/*     */       }
/*  72 */       s = s.substring(0, s.length() - 1);
/*  73 */       this.out.println(s);
/*  74 */       m = s.split(" ");
/*  75 */       this.hbb = new int[m.length];
/*  76 */       for (int i = 0; i < m.length; i++) {
/*  77 */         this.hbb[i] = Integer.parseInt(m[i]);
/*     */       }
/*  79 */       m = a[4].split(";");
/*  80 */       s = "";
/*  81 */       for (int i = 0; i < m.length; i++)
/*     */       {
/*  83 */         String[] m2 = m[i].split(" ");
/*  84 */         for (int u = 0; u < m2.length; u++)
/*  85 */           s = s + m2[u] + " ";
/*     */       }
/*  87 */       s = s.substring(0, s.length() - 1);
/*  88 */       this.out.println(s);
/*  89 */       m = s.split(" ");
/*  90 */       this.hw = new int[m.length];
/*  91 */       for (int i = 0; i < m.length; i++) {
/*  92 */         this.hw[i] = Integer.parseInt(m[i]);
/*     */       }
/*  94 */       m = a[5].split(";");
/*  95 */       s = "";
/*  96 */       for (int i = 0; i < m.length; i++)
/*     */       {
/*  98 */         String[] m2 = m[i].split(" ");
/*  99 */         for (int u = 0; u < m2.length; u++)
/* 100 */           s = s + m2[u] + " ";
/*     */       }
/* 102 */       s = s.substring(0, s.length() - 1);
/* 103 */       this.out.println(s);
/* 104 */       m = s.split(" ");
/* 105 */       this.hr = new int[m.length];
/* 106 */       for (int i = 0; i < m.length; i++) {
/* 107 */         this.hr[i] = Integer.parseInt(m[i]);
/*     */       }
/* 109 */       this.out.println("3.Vse massivy zagrugeny");
/*     */     } catch (IOException e) {
/* 111 */       this.out.println("FileReader ne zagrugen");
/* 112 */       this.out.println(e);
/*     */     }
/*     */     while (true) {
/* 115 */       setTurnRadarLeft(360.0D);
/* 116 */       this.p[0] = getX();
/* 117 */       this.p[1] = getY();
/* 118 */       this.p[2] = getEnergy();
/* 119 */       Function(this.run); } 
/*     */   }
/* 121 */   public void onScannedRobot(ScannedRobotEvent e) { this.p[3] = (Math.sin(getHeadingRadians() + e.getBearingRadians()) * e.getDistance());
/* 122 */     this.p[4] = (Math.cos(getHeadingRadians() + e.getBearingRadians()) * e.getDistance());
/* 123 */     this.p[5] = e.getDistance();
/* 124 */     this.p[6] = getEnergy();
/* 125 */     this.p[7] = getVelocity();
/* 126 */     this.p[8] = getHeading();
/* 127 */     this.p[9] = e.getBearing();
/* 128 */     Function(this.scan); } 
/* 129 */   public void onHitByBullet(HitByBulletEvent e) { Function(this.hbb); } 
/* 130 */   public void onHitWall(HitWallEvent e) { Function(this.hw); } 
/* 131 */   public void onHitRobot(HitRobotEvent e) { Function(this.hr); }
/*     */ 
/*     */   public void Function(int[] a) {
/* 134 */     for (int i = 0; i < a.length; i++)
/*     */     {
/* 136 */       switch (a[i])
/*     */       {
/*     */       case 1:
/*     */       case 2:
/*     */       case 3:
/*     */       case 4:
/*     */       case 5:
/*     */       case 6:
/*     */       case 7:
/*     */       case 8:
/*     */       case 9:
/*     */       case 10:
/*     */       case 11:
/*     */       case 12:
/* 151 */         i = f2(i, a);
/* 152 */         break;
/*     */       case 13:
/* 156 */         if (this.p[a[(i + 1)]] > this.p[a[(i + 2)]])
/*     */         {
/* 158 */           i += 4;
/* 159 */           i = f2(i, a);
/*     */         }
/*     */         else
/*     */         {
/* 163 */           i += 3;
/* 164 */           i += a[i];
/*     */         }
/* 166 */         break;
/*     */       case 14:
/* 170 */         if (this.p[a[(i + 1)]] == this.p[a[(i + 2)]])
/*     */         {
/* 172 */           i += 4;
/* 173 */           i = f2(i, a);
/*     */         }
/*     */         else
/*     */         {
/* 177 */           i += 3;
/* 178 */           i += a[i];
/*     */         }
/* 180 */         break;
/*     */       case 15:
/* 184 */         if (this.p[a[(i + 1)]] > this.p[a[(i + 2)]])
/*     */         {
/* 186 */           i += 4;
/* 187 */           i = f2(i, a);
/* 188 */           i++;
/* 189 */           i += a[i];
/*     */         }
/*     */         else
/*     */         {
/* 193 */           i += 3;
/* 194 */           i += a[i] + 2;
/* 195 */           i = f2(i, a);
/*     */         }
/* 197 */         break;
/*     */       case 16:
/* 201 */         if (this.p[a[(i + 1)]] == this.p[a[(i + 2)]])
/*     */         {
/* 203 */           i += 4;
/* 204 */           i = f2(i, a);
/* 205 */           i++;
/* 206 */           i += a[i];
/*     */         }
/*     */         else
/*     */         {
/* 210 */           i += 3;
/* 211 */           i += a[i] + 2;
/* 212 */           i = f2(i, a);
/*     */         }
/*     */         break;
/*     */       }
/*     */     }
/*     */ 
/* 218 */     execute();
/*     */   }
/*     */ 
/*     */   public int f2(int i, int[] a)
/*     */   {
/* 223 */     switch (a[i])
/*     */     {
/*     */     case 1:
/* 227 */       setAhead(this.p[a[(i + 1)]]);
/* 228 */       i++;
/* 229 */       break;
/*     */     case 2:
/* 233 */       setBack(this.p[a[(i + 1)]]);
/* 234 */       i++;
/* 235 */       break;
/*     */     case 3:
/* 239 */       setTurnLeft(this.p[a[(i + 1)]]);
/* 240 */       i++;
/* 241 */       break;
/*     */     case 4:
/* 245 */       setTurnRight(this.p[a[(i + 1)]]);
/* 246 */       i++;
/* 247 */       break;
/*     */     case 5:
/* 251 */       setTurnGunLeft(this.p[a[(i + 1)]]);
/* 252 */       i++;
/* 253 */       break;
/*     */     case 6:
/* 257 */       setTurnGunRight(this.p[a[(i + 1)]]);
/* 258 */       i++;
/* 259 */       break;
/*     */     case 7:
/* 263 */       fire(this.p[a[(i + 1)]]);
/* 264 */       i++;
/* 265 */       break;
/*     */     case 8:
/* 269 */       this.p[a[(i + 1)]] = this.p[a[(i + 2)]];
/* 270 */       i += 2;
/* 271 */       break;
/*     */     case 9:
/* 275 */       this.p[a[(i + 1)]] = (this.p[a[(i + 2)]] + this.p[a[(i + 3)]]);
/* 276 */       i += 3;
/* 277 */       break;
/*     */     case 10:
/* 281 */       this.p[a[(i + 1)]] = (this.p[a[(i + 2)]] - this.p[a[(i + 3)]]);
/* 282 */       i += 3;
/* 283 */       break;
/*     */     case 11:
/* 287 */       this.p[a[(i + 1)]] = (this.p[a[(i + 2)]] * this.p[a[(i + 3)]]);
/* 288 */       i += 3;
/* 289 */       break;
/*     */     case 12:
/* 293 */       if (this.p[a[(i + 3)]] != 0.0D)
/* 294 */         this.p[a[(i + 1)]] = (this.p[a[(i + 2)]] / this.p[a[(i + 3)]]);
/* 295 */       i += 3;
/*     */     }
/*     */ 
/* 299 */     return i;
/*     */   }
/*     */ }

/* Location:           E:\naval-robocode\sample\r.GenRobot_1.0.jar
 * Qualified Name:     r.GenRobot
 * JD-Core Version:    0.6.2
 */