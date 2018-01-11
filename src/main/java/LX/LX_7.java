/*     */ package LX;
/*     */ 
/*     */ import java.io.PrintStream;
/*     */ import java.util.Iterator;
/*     */ import java.util.Random;
/*     */ import java.util.Vector;
/*     */ import robocode.AdvancedRobot;
/*     */ import robocode.BulletHitBulletEvent;
/*     */ import robocode.BulletHitEvent;
/*     */ import robocode.BulletMissedEvent;
/*     */ import robocode.DeathEvent;
/*     */ import robocode.Event;
/*     */ import robocode.HitByBulletEvent;
/*     */ import robocode.HitRobotEvent;
/*     */ import robocode.HitWallEvent;
/*     */ import robocode.RobotDeathEvent;
/*     */ import robocode.ScannedRobotEvent;
/*     */ import robocode.WinEvent;
/*     */ 
/*     */ public class LX_7 extends AdvancedRobot
/*     */ {
public class vect_t {

	public vect_t(LX_7 lx_7, double to_circle_center, double r) {
		// TODO Auto-generated constructor stub
	}

	public void addrad(double from_circle_center, double r) {
		// TODO Auto-generated method stub
		
	}

	}
public class targets_t {

	public targets_t(LX_7 lx_7) {
		// TODO Auto-generated constructor stub
	}

	public void begin_round() {
		// TODO Auto-generated method stub
		
	}

	public void begin_turn() {
		// TODO Auto-generated method stub
		
	}

	public void update(ScannedRobotEvent e) {
		// TODO Auto-generated method stub
		
	}

	public void hit(BulletHitEvent e) {
		// TODO Auto-generated method stub
		
	}

	public void one_less(RobotDeathEvent e) {
		// TODO Auto-generated method stub
		
	}

	public void collide(HitRobotEvent e) {
		// TODO Auto-generated method stub
		
	}

	public void hit_by_bullet(HitByBulletEvent e) {
		// TODO Auto-generated method stub
		
	}

	public void show_statistics() {
		// TODO Auto-generated method stub
		
	}

	}
public class radar_t {

	public radar_t(LX_7 lx_7) {
		// TODO Auto-generated constructor stub
	}

	public void begin_turn() {
		// TODO Auto-generated method stub
		
	}

	public void begin_round() {
		// TODO Auto-generated method stub
		
	}

	public void end_turn() {
		// TODO Auto-generated method stub
		
	}

	}
public class driver_t {

	public driver_t(LX_7 lx_7) {
		// TODO Auto-generated constructor stub
	}

	public void begin_turn() {
		// TODO Auto-generated method stub
		
	}

	public void begin_round() {
		// TODO Auto-generated method stub
		
	}

	public void robot_collision() {
		// TODO Auto-generated method stub
		
	}

	public void wall_collision() {
		// TODO Auto-generated method stub
		
	}

	public void hit_by_bullet() {
		// TODO Auto-generated method stub
		
	}

	public void end_turn() {
		// TODO Auto-generated method stub
		
	}

	}
public class gunner_t {

	public gunner_t(LX_7 lx_7) {
		// TODO Auto-generated constructor stub
	}

	public void begin_turn() {
		// TODO Auto-generated method stub
		
	}

	public void begin_round() {
		// TODO Auto-generated method stub
		
	}

	public void end_turn() {
		// TODO Auto-generated method stub
		
	}

	}
/*   9 */   private final boolean ANY_DEBUG = true;
/*  10 */   private final boolean TO_LOG_FILE = true;
/*  11 */   private final boolean TO_CONSOLE = false;
/*  12 */   private final boolean SWITCH_DEBUG = true;
/*  13 */   private final boolean INPUTS_LOGGING = true;
/*  14 */   private final boolean OUTPUTS_LOGGING = true;
/*  15 */   private final boolean OBJECTS_LOGGING = true;
/*  16 */   private final boolean A0_LOGGING = true;
/*  17 */   private final boolean A0_BEGIN_LOGGING = true;
/*  18 */   private final boolean A0_END_LOGGING = true;
/*  19 */   private final boolean A0_ERROR_LOGGING = true;
/*  20 */   private final boolean A0_TRANS_LOGGING = true;
/*     */ 
/*  22 */   private final double precision = 1.0E-006D;
/*  23 */   private final double PI = 3.141592653589793D;
/*  24 */   private final double dPI = 6.283185307179586D;
/*  25 */   private final double PI2 = 1.570796326794897D;
/*  26 */   private final double gun_rotation_speed = Math.toRadians(20.0D);
/*  27 */   private final double speed_max = 8.0D;
/*  28 */   private final double turning_speed_max = Math.toRadians(10.0D);
/*  29 */   private final double max_firepower = 3.0D;
/*  30 */   private final double base_firepower = 1.5D;
/*  31 */   private final double fire_delay_critical = 400.0D;
/*  32 */   private final double life_ok = 50.0D;
/*  33 */   private final double life_warning = 30.0D;
/*  34 */   private final double life_critical = 15.0D;
/*     */   private double battlefield_width;
/*     */   private double battlefield_height;
/*     */   private double robot_size;
/*     */   private double robot_size2;
/*     */   private double collision_delta;
/*     */   private int cur_robots_count;
/*     */   private int total_robots_count;
/*     */   private long cur_time;
/*  43 */   private Random random = new Random();
/*     */   private double cur_life;
/*     */   private long last_fire_time;
/*     */   private long hits;
/*     */   private long misses;
/*     */   private long hitted_by_bullet;
/*     */   private long walls_collisions;
/*     */   private LX_7.radar_t radar;
/*     */   private LX_7.driver_t driver;
/*     */   private LX_7.gunner_t gunner;
/*     */   private LX_7.targets_t targets;
/*     */   private Vector events;
/*  54 */   private int y0 = 0;
/*     */ 
/*     */   public void run()
/*     */   {
/*  60 */     A0(9);
/*     */     while (true)
/*     */     {
/*  64 */       this.cur_time = getTime();
/*  65 */       log("--------- " + this.cur_time + " ---------");
/*     */ 
/*  67 */       check_events();
/*     */ 
/*  69 */       A0(10);
/*  70 */       this.gunner.begin_turn();
/*  71 */       this.radar.begin_turn();
/*  72 */       this.driver.begin_turn();
/*     */ 
/*  74 */       end_turn();
/*     */     }
/*     */   }
/*     */ 
/*     */   public void onBulletHit(BulletHitEvent first_e)
/*     */   {
/*  83 */     this.events.add(first_e);
/*     */   }
/*     */ 
/*     */   public void onBulletMissed(BulletMissedEvent first_e)
/*     */   {
/*  91 */     this.events.add(first_e);
/*     */   }
/*     */ 
/*     */   public void onBulletHitBullet(BulletHitBulletEvent first_e)
/*     */   {
/*  99 */     this.events.add(first_e);
/*     */   }
/*     */ 
/*     */   public void onScannedRobot(ScannedRobotEvent first_e)
/*     */   {
/* 107 */     this.events.add(first_e);
/*     */   }
/*     */ 
/*     */   public void onRobotDeath(RobotDeathEvent first_e)
/*     */   {
/* 115 */     this.events.add(first_e);
/*     */   }
/*     */ 
/*     */   public void onHitRobot(HitRobotEvent first_e)
/*     */   {
/* 123 */     this.events.add(first_e);
/*     */   }
/*     */ 
/*     */   public void onHitWall(HitWallEvent first_e)
/*     */   {
/* 131 */     this.events.add(first_e);
/*     */   }
/*     */ 
/*     */   public void onHitByBullet(HitByBulletEvent first_e)
/*     */   {
/* 139 */     this.events.add(first_e);
/*     */   }
/*     */ 
/*     */   public void onWin(WinEvent first_e)
/*     */   {
/* 147 */     A0(21);
/*     */   }
/*     */ 
/*     */   public void onDeath(DeathEvent first_e)
/*     */   {
/* 155 */     A0(20);
/*     */   }
/*     */ 
/*     */   private void A0(int e)
/*     */   {
/* 162 */     int y_old = this.y0;
/*     */ 
/* 165 */     log("��� ������� '����������':");
/*     */ 
/* 168 */     log_begin("A0", this.y0, e);
/*     */ 
/* 170 */     switch (this.y0)
/*     */     {
/*     */     case 0:
/* 173 */       if (e == 9) { z10_0(); this.y0 = 1; }
/* 174 */       break;
/*     */     case 1:
/* 177 */       if ((e == 20) || (e == 21)) this.y0 = 2;
/* 179 */       else if (e == 10) z10_2();
/* 180 */       break;
/*     */     case 2:
/* 183 */       if (e == 9) this.y0 = 1;
/* 184 */       break;
/*     */     default:
/* 188 */       log_error("A0", this.y0);
/*     */     }
/*     */ 
/* 191 */     if (this.y0 != y_old)
/*     */     {
/* 195 */       log_trans("A0", this.y0, y_old);
/*     */ 
/* 197 */       switch (this.y0)
/*     */       {
/*     */       case 1:
/* 200 */         z10_1(); z10_2();
/* 201 */         break;
/*     */       case 2:
/* 204 */         z20();
/* 205 */         break;
/*     */       default:
/* 209 */         log_error("A0", this.y0);
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 214 */     log_end("A0", this.y0);
/*     */   }
/*     */ 
/*     */   private void z10_0()
/*     */   {
/* 222 */     log_output("z10_0", "������������� ��� �������");
/*     */ 
/* 225 */     setEventPriority("RobotDeathEvent", 17);
/* 226 */     setEventPriority("ScannedRobotEvent", 16);
/* 227 */     setEventPriority("HitRobotEvent", 15);
/* 228 */     setEventPriority("HitWallEvent", 14);
/* 229 */     setEventPriority("BulletHitEvent", 13);
/* 230 */     setEventPriority("HitByBulletEvent", 12);
/* 231 */     setEventPriority("BulletMissedEvent", 11);
/*     */ 
/* 234 */     this.battlefield_width = getBattleFieldWidth();
/* 235 */     this.battlefield_height = getBattleFieldHeight();
/* 236 */     this.robot_size = ((getWidth() + getHeight()) / 2.0D);
/* 237 */     this.robot_size2 = (this.robot_size / 2.0D);
/* 238 */     this.collision_delta = (this.robot_size2 - 5.0D);
/* 239 */     this.hits = 0L; this.misses = 0L; this.hitted_by_bullet = 0L;
/* 240 */     this.walls_collisions = 0L;
/* 241 */     this.total_robots_count = getOthers();
/*     */ 
/* 244 */     this.targets = new LX_7.targets_t(this);
/* 245 */     this.radar = new LX_7.radar_t(this);
/* 246 */     this.driver = new LX_7.driver_t(this);
/* 247 */     this.gunner = new LX_7.gunner_t(this);
/* 248 */     this.events = new Vector();
/*     */   }
/*     */ 
/*     */   private void z10_1()
/*     */   {
/* 255 */     log_output("z10_1", "������������� � ������ ������");
/*     */ 
/* 257 */     log("");
/* 258 */     log("====================================");
/* 259 */     log("***");
/* 260 */     log("*** ����� " + (getRoundNum() + 1));
/* 261 */     log("***");
/* 262 */     log("====================================");
/* 263 */     log("");
/*     */ 
/* 265 */     clearAllEvents();
/*     */ 
/* 267 */     setAdjustGunForRobotTurn(true);
/* 268 */     setAdjustRadarForGunTurn(true);
/*     */ 
/* 270 */     this.cur_time = 0L;
/*     */ 
/* 273 */     this.targets.begin_round();
/* 274 */     this.driver.begin_round();
/* 275 */     this.radar.begin_round();
/* 276 */     this.gunner.begin_round();
/*     */ 
/* 278 */     if (this.events != null) this.events.clear();
/*     */   }
/*     */ 
/*     */   private void z10_2()
/*     */   {
/* 285 */     log_output("z10_2", "������������� � ������ ����");
/*     */ 
/* 287 */     this.cur_robots_count = getOthers();
/* 288 */     this.cur_life = getLife();
/*     */ 
/* 291 */     this.targets.begin_turn();
/*     */   }
/*     */ 
/*     */   private void z20()
/*     */   {
/* 298 */     log_output("z20", "������� ���������� ������");
/*     */ 
/* 300 */     show_statistics();
/*     */   }
/*     */ 
/*     */   private void check_events()
/*     */   {
/* 309 */     Iterator events_iter = this.events.iterator();
/*     */ 
/* 312 */     while (events_iter.hasNext())
/*     */     {
/* 314 */       Event e = (Event)events_iter.next();
/*     */ 
/* 316 */       if ((e instanceof ScannedRobotEvent))
/*     */       {
/* 319 */         this.targets.update((ScannedRobotEvent)e);
/*     */       }
/* 322 */       else if ((e instanceof BulletHitEvent))
/*     */       {
/* 325 */         this.targets.hit((BulletHitEvent)e);
/* 326 */         this.hits += 1L;
/*     */       }
/* 329 */       else if ((e instanceof RobotDeathEvent))
/*     */       {
/* 332 */         this.targets.one_less((RobotDeathEvent)e);
/*     */       }
/* 335 */       else if ((e instanceof HitRobotEvent))
/*     */       {
/* 338 */         this.driver.robot_collision();
/* 339 */         this.targets.collide((HitRobotEvent)e);
/*     */       }
/* 342 */       else if ((e instanceof HitWallEvent))
/*     */       {
/* 345 */         this.driver.wall_collision();
/* 346 */         this.walls_collisions += 1L;
/*     */       }
/* 349 */       else if ((e instanceof BulletMissedEvent))
/*     */       {
/* 352 */         this.misses += 1L;
/*     */       }
/* 355 */       else if ((e instanceof HitByBulletEvent))
/*     */       {
/* 358 */         this.driver.hit_by_bullet();
/* 359 */         this.targets.hit_by_bullet((HitByBulletEvent)e);
/* 360 */         this.hitted_by_bullet += 1L;
/*     */       }
/*     */     }
/*     */ 
/* 364 */     this.events.clear();
/*     */   }
/*     */ 
/*     */   private void end_turn()
/*     */   {
/* 373 */     this.driver.end_turn();
/* 374 */     this.radar.end_turn();
/* 375 */     this.gunner.end_turn();
/*     */   }
/*     */ 
/*     */   private void show_statistics()
/*     */   {
/* 382 */     long shots = this.hits + this.misses;
/*     */ 
/* 384 */     this.targets.show_statistics();
/* 385 */     log("���������: " + shots + ", ���������: " + this.hits + ", ��������: " + this.misses);
/* 386 */     log("��������: " + this.hits / shots);
/* 387 */     log("������ � ���: " + this.hitted_by_bullet);
/* 388 */     log("������������ �� �������: " + this.walls_collisions);
/*     */   }
/*     */ 
/*     */   private LX_7.vect_t get_path(double v, double w, double heading, double T)
/*     */   {
/*     */     LX_7.vect_t path;
/* 398 */     if (Math.abs(w) > 1.0E-006D)
/*     */     {
/* 401 */       double R = Math.abs(v / w);
/*     */ 
/* 404 */       double to_circle_center = normalize_angle(
/* 405 */         w * v >= 0.0D ? 
/* 406 */         heading + 1.570796326794897D : 
/* 407 */         heading - 1.570796326794897D);
/* 408 */       double from_circle_center = normalize_angle(to_circle_center + 3.141592653589793D + w * T);
/*     */ 
/* 410 */       path = new LX_7.vect_t(this, to_circle_center, R);
/* 411 */       path.addrad(from_circle_center, R);
/*     */     }
/*     */     else
/*     */     {
/* 416 */       path = new LX_7.vect_t(this, heading, v * T);
/*     */     }
/*     */ 
/* 419 */     return path;
/*     */   }
/*     */ 
/*     */   private double get_bullet_speed(double firepower)
/*     */   {
/* 426 */     return 20.0D - 3.0D * firepower;
/*     */   }
/*     */ 
/*     */   private double get_turning_speed(double speed)
/*     */   {
/* 434 */     return Math.min(
/* 436 */       this.turning_speed_max, 
/* 437 */       this.turning_speed_max * (0.4D + 0.6D * (1.0D - speed / 8.0D)));
/*     */   }
/*     */ 
/*     */   private double normalize_angle(double a)
/*     */   {
/* 445 */     a = 6.283185307179586D + a % 6.283185307179586D;
/* 446 */     a %= 6.283185307179586D;
/* 447 */     return a;
/*     */   }
/*     */ 
/*     */   private double get_angle_diff(double from, double to)
/*     */   {
/* 455 */     double diff = to - from;
/*     */ 
/* 457 */     if (Math.abs(diff) <= 3.141592653589793D) return diff;
/*     */ 
/* 459 */     if (diff < 0.0D) {
/* 460 */       diff += 6.283185307179586D;
/*     */     }
/* 462 */     else if (diff > 0.0D) {
/* 463 */       diff -= 6.283185307179586D;
/*     */     }
/* 465 */     return diff % 6.283185307179586D;
/*     */   }
/*     */ 
/*     */   private double shortest_turn(double da)
/*     */   {
/* 473 */     if (da > 3.141592653589793D) {
/* 474 */       da -= 6.283185307179586D;
/*     */     }
/* 476 */     else if (da < -3.141592653589793D) {
/* 477 */       da += 6.283185307179586D;
/*     */     }
/* 479 */     return da;
/*     */   }
/*     */ 
/*     */   private double get_angle(double x, double y)
/*     */   {
/* 488 */     if (y == 0.0D)
/*     */     {
/* 490 */       return x > 0.0D ? 1.570796326794897D : 4.71238898038469D;
/*     */     }
/*     */ 
/* 493 */     double a = Math.atan(x / y);
/*     */ 
/* 495 */     if (y < 0.0D) a += 3.141592653589793D;
/*     */ 
/* 497 */     return a;
/*     */   }
/*     */ 
/*     */   private void log(String str)
/*     */   {
/* 506 */     System.out.println(str);
/*     */   }
/*     */ 
/*     */   private void log(String str, String symbol)
/*     */   {
/* 512 */     String out_str = symbol + " " + str;
/*     */ 
/* 516 */     System.out.println(out_str);
/*     */   }
/*     */ 
/*     */   private void log_begin(String a_name, int state, int event)
/*     */   {
/* 522 */     log(a_name + ": ������� " + a_name + 
/* 523 */       " ������� � ��������� " + state + 
/* 524 */       " � �������� e" + event, "{");
/*     */   }
/*     */ 
/*     */   private void log_end(String a_name, int state)
/*     */   {
/* 530 */     log(a_name + ": ������� " + a_name + 
/* 531 */       " �������� ���� ������ � ��������� " + state, "}");
/*     */   }
/*     */ 
/*     */   private void log_error(String a_name, int state)
/*     */   {
/* 537 */     log(a_name + ": ����������� ��������� " + state + "!", "!");
/*     */   }
/*     */ 
/*     */   private void log_trans(String a_name, int state_to, int state_from)
/*     */   {
/* 543 */     log(a_name + ": ������� " + a_name + 
/* 544 */       " ������� �� ��������� " + state_from + 
/* 545 */       " � ��������� " + state_to, " T");
/*     */   }
/*     */ 
/*     */   private void log_input(String x_name, String comment, boolean result)
/*     */   {
/* 551 */     String res_str = result ? "��" : "���";
/* 552 */     log(x_name + ": " + comment + "? - " + res_str + ".", " i");
/*     */   }
/*     */ 
/*     */   private void log_output(String z_name, String comment)
/*     */   {
/* 558 */     log(z_name + ": " + comment + ".", " *");
/*     */   }
/*     */ }

/* Location:           E:\naval-robocode\sample\
 * Qualified Name:     LX.LX_7
 * JD-Core Version:    0.6.2
 */