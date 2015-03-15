/*    */ package dmh.robocode.data;
/*    */ 
/*    */ import dmh.robocode.enemy.EnemyRobot;
/*    */ import dmh.robocode.robot.CommandBasedRobot;
/*    */ import dmh.robocode.utils.ConvertStatistic;
/*    */ import dmh.robocode.utils.Geometry;
/*    */ 
/*    */ public abstract class Shot
/*    */ {
/* 21 */   public static byte unknownObservationAge = -1;
/* 22 */   public static byte unknownEnemyZone = -1;
/* 23 */   public static short unknownEnemyDistance = -1;
/*    */   private byte observationAge;
/*    */   private byte enemyZone;
/*    */   private byte myZone;
/*    */   private byte numberOfLiveEnemies;
/*    */   private byte bulletPowerTimesTen;
/*    */   private short enemyDistance;
/*    */   private int roundNum;
/*    */   private EnemyRobot enemy;
/*    */ 
/*    */   public Shot(CommandBasedRobot myRobot, EnemyRobot enemyRobot, double bulletPower)
/*    */   {
/* 37 */     RadarObservation enemyObservation = enemyRobot.getLatestRadarObservation();
/* 38 */     Location myLocation = myRobot.getLocation();
/*    */ 
/* 40 */     if (enemyObservation == null) {
/* 41 */       this.observationAge = unknownObservationAge;
/* 42 */       this.enemyZone = unknownEnemyZone;
/* 43 */       this.enemyDistance = unknownEnemyDistance;
/*    */     } else {
/* 45 */       this.observationAge = ConvertStatistic.makeByte(myRobot.getTime() - enemyObservation.getTimeSeen());
/* 46 */       this.enemyZone = enemyObservation.getLocation().getZone(myRobot.getBattleFieldWidth(), myRobot.getBattleFieldHeight());
/* 47 */       this.enemyDistance = ConvertStatistic.makeShort(Geometry.getDistanceBetweenLocations(myLocation, enemyObservation.getLocation()));
/*    */     }
/*    */ 
/* 50 */     this.myZone = myLocation.getZone(myRobot.getBattleFieldWidth(), myRobot.getBattleFieldHeight());
/* 51 */     this.numberOfLiveEnemies = ConvertStatistic.makeByte(myRobot.getOthers());
/* 52 */     this.bulletPowerTimesTen = ConvertStatistic.makeByte(bulletPower * 10.0D);
/* 53 */     this.enemy = enemyRobot;
/* 54 */     this.roundNum = myRobot.getRoundNum();
/*    */   }
/*    */ 
/*    */   public byte getObservationAge() {
/* 58 */     return this.observationAge;
/*    */   }
/*    */ 
/*    */   public byte getEnemyZone() {
/* 62 */     return this.enemyZone;
/*    */   }
/*    */ 
/*    */   public byte getMyZone() {
/* 66 */     return this.myZone;
/*    */   }
/*    */ 
/*    */   public byte getNumberOfLiveEnemies() {
/* 70 */     return this.numberOfLiveEnemies;
/*    */   }
/*    */ 
/*    */   public double getBulletPower() {
/* 74 */     return this.bulletPowerTimesTen / 10.0D;
/*    */   }
/*    */ 
/*    */   public short getEnemyDistance() {
/* 78 */     return this.enemyDistance;
/*    */   }
/*    */ 
/*    */   public int getRoundNum() {
/* 82 */     return this.roundNum;
/*    */   }
/*    */ 
/*    */   public EnemyRobot getEnemy() {
/* 86 */     return this.enemy;
/*    */   }
/*    */ }

/* Location:           E:\naval-robocode\sample\dmh.robocode.robot.BlackDeathMk7_7.0.jar
 * Qualified Name:     dmh.robocode.data.Shot
 * JD-Core Version:    0.6.2
 */