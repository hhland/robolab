/*    */ package dmh.robocode.bullet;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.Iterator;
/*    */ import java.util.List;
/*    */ 
/*    */ public class SimulatedBulletList
/*    */ {
/*    */   private List<SimulatedBullet> bullets;
/* 10 */   private int hits = 0;
/* 11 */   private int misses = 0;
/*    */ 
/*    */   public SimulatedBulletList() {
/* 14 */     this.bullets = new ArrayList();
/*    */   }
/*    */ 
/*    */   public int getInProgressCount() {
/* 18 */     return this.bullets.size();
/*    */   }
/*    */ 
/*    */   public double getSuccessRate() {
/* 22 */     if ((this.hits > 0) || (this.misses > 0)) {
/* 23 */       return this.hits / (this.hits + this.misses) * 100.0D;
/*    */     }
/* 25 */     return 0.0D;
/*    */   }
/*    */ 
/*    */   public int getTotalHits()
/*    */   {
/* 30 */     return this.hits;
/*    */   }
/*    */ 
/*    */   public int getTotalMisses() {
/* 34 */     return this.misses;
/*    */   }
/*    */ 
/*    */   public void add(SimulatedBullet bullet) {
/* 38 */     this.bullets.add(bullet);
/*    */   }
/*    */ 
/*    */   public void processCurrentTime(long time) {
/* 42 */     for (Iterator bulletIterator = this.bullets.iterator(); bulletIterator.hasNext(); ) {
/* 43 */       SimulatedBullet bullet = (SimulatedBullet)bulletIterator.next();
/* 44 */       bullet.processAtTime(time);
/* 45 */       switch ($SWITCH_TABLE$dmh$robocode$bullet$SimulatedBullet$BulletResult()[bullet.getResult().ordinal()]) {
/*    */       case 2:
/* 47 */         this.hits += 1;
/* 48 */         bulletIterator.remove();
/* 49 */         break;
/*    */       case 3:
/* 52 */         this.misses += 1;
/* 53 */         bulletIterator.remove();
/* 54 */         break;
/*    */       case 4:
/* 57 */         bulletIterator.remove();
/*    */       }
/*    */     }
/*    */   }
/*    */ 
/*    */   public void processEndOfRound()
/*    */   {
/* 66 */     this.bullets.clear();
/*    */   }
/*    */ }

/* Location:           E:\naval-robocode\sample\dmh.robocode.robot.BlackDeathMk7_7.0.jar
 * Qualified Name:     dmh.robocode.bullet.SimulatedBulletList
 * JD-Core Version:    0.6.2
 */