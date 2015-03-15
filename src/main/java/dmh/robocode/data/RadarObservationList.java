/*    */ package dmh.robocode.data;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ 
/*    */ public class RadarObservationList
/*    */ {
/*    */   List<RadarObservation> observations;
/*    */   private RadarObservation latest;
/*    */   private RadarObservation previous;
/*    */ 
/*    */   public RadarObservationList()
/*    */   {
/* 13 */     this.observations = new ArrayList();
/*    */   }
/*    */ 
/*    */   public void add(RadarObservation radarObservation) {
/* 17 */     this.observations.add(radarObservation);
/* 18 */     this.previous = this.latest;
/* 19 */     this.latest = radarObservation;
/*    */   }
/*    */ 
/*    */   public RadarObservation getLatest() {
/* 23 */     return this.latest;
/*    */   }
/*    */ 
/*    */   public RadarObservation getPrevious() {
/* 27 */     return this.previous;
/*    */   }
/*    */ 
/*    */   public int getSize() {
/* 31 */     return this.observations.size();
/*    */   }
/*    */ 
/*    */   public List<CompleteMovementSequence> getMovementSequences() {
/* 35 */     return null;
/*    */   }
/*    */ 
/*    */   public List<RadarObservation> getAllSince(long earliestTime)
/*    */   {
/* 40 */     List result = new ArrayList();
/*    */ 
/* 42 */     for (int i = this.observations.size() - 1; i >= 0; i--) {
/* 43 */       RadarObservation observation = (RadarObservation)this.observations.get(i);
/*    */ 
/* 45 */       if (observation.getTimeSeen() < earliestTime) break;
/* 46 */       result.add(observation);
/*    */     }
/*    */ 
/* 52 */     return result;
/*    */   }
/*    */ 
/*    */   public RadarObservation getObservationAtTime(long time) {
/* 56 */     for (int i = this.observations.size() - 1; i >= 0; i--) {
/* 57 */       RadarObservation observation = (RadarObservation)this.observations.get(i);
/*    */ 
/* 59 */       if (observation.getTimeSeen() == time)
/* 60 */         return observation;
/* 61 */       if (observation.getTimeSeen() < time) {
/*    */         break;
/*    */       }
/*    */     }
/* 65 */     return null;
/*    */   }
/*    */ }

/* Location:           E:\naval-robocode\sample\dmh.robocode.robot.BlackDeathMk7_7.0.jar
 * Qualified Name:     dmh.robocode.data.RadarObservationList
 * JD-Core Version:    0.6.2
 */