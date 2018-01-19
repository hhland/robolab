/*     */ package dmh.robocode.data;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Map.Entry;
/*     */ 
/*     */ public class CauseAndEffect<TCause, TEffect>
/*     */ {
/*     */   Map<TCause, Map<TEffect, Integer>> causeAndEffectMap;
/*     */   Map<TCause, Integer> totalCausesMap;
/*     */ 
/*     */   public CauseAndEffect()
/*     */   {
/*  15 */     this.causeAndEffectMap = new HashMap();
/*  16 */     this.totalCausesMap = new HashMap();
/*     */   }
/*     */ 
/*     */   public void record(TCause cause, TEffect effect) {
/*  20 */     Map effects = (Map)this.causeAndEffectMap.get(cause);
/*     */ 
/*  22 */     if (effects == null) {
/*  23 */       effects = new HashMap();
/*  24 */       effects.put(effect, Integer.valueOf(1));
/*  25 */       this.causeAndEffectMap.put(cause, effects);
/*  26 */       this.totalCausesMap.put(cause, Integer.valueOf(1));
/*     */     } else {
/*  28 */       Integer previousEffectCount = (Integer)effects.get(effect);
/*  29 */       if (previousEffectCount == null)
/*  30 */         effects.put(effect, Integer.valueOf(1));
/*     */       else {
/*  32 */         effects.put(effect, Integer.valueOf(previousEffectCount.intValue() + 1));
/*     */       }
/*  34 */       this.totalCausesMap.put(cause, Integer.valueOf(((Integer)this.totalCausesMap.get(cause)).intValue() + 1));
/*     */     }
/*     */   }
/*     */ 
/*     */   public double getProbabilityOf(TCause cause, TEffect effect) {
/*  39 */     Map effects = (Map)this.causeAndEffectMap.get(cause);
/*  40 */     if ((effects != null) && (effect != null)) {
/*  41 */       Integer effectCount = (Integer)effects.get(effect);
/*  42 */       if (effectCount != null) {
/*  43 */         return effectCount.intValue() * 100.0D / ((Integer)this.totalCausesMap.get(cause)).intValue();
/*     */       }
/*     */     }
/*  46 */     return 0.0D;
/*     */   }
/*     */ 
/*     */   public TEffect getMostLikelyEffectOf(TCause cause) {
/*  50 */     Map<TEffect, Integer> effects = this.causeAndEffectMap.get(cause);
/*  51 */     TEffect likelyEffect = null;
/*  52 */     if (effects != null) {
/*  53 */       int maxFrequency = 0;
/*  54 */       for (Map.Entry<TEffect, Integer> entry : effects.entrySet()) {
/*  55 */         if (((Integer)entry.getValue()).intValue() > maxFrequency) {
/*  56 */           maxFrequency = ((Integer)entry.getValue()).intValue();
/*  57 */           likelyEffect = entry.getKey();
/*     */         }
/*     */       }
/*     */     }
/*  61 */     return likelyEffect;
/*     */   }
/*     */ 
/*     */   public void purge(int maxCauses)
/*     */   {
/*  85 */     int needToRemove = this.totalCausesMap.size() - Math.max(0, maxCauses);
/*  86 */     if (needToRemove > 0) {
/*  87 */       List sortedCauses = new ArrayList();
/*  88 */       for (Map.Entry<TCause,Integer> entry : this.totalCausesMap.entrySet()) {
/*  89 */         CauseAndFreq caf= new CauseAndFreq(entry.getKey(),entry.getValue());
/*     */         sortedCauses.add(caf);
                 }
/*  91 */       Collections.sort(sortedCauses);
/*  92 */       while (needToRemove-- > 0)
/*  93 */         ((CauseAndFreq)sortedCauses.get(needToRemove)).purge();
/*     */     }
/*     */   }
/*     */ 
/*     */   public int getLength()
/*     */   {
/* 106 */     return this.totalCausesMap.size();
/*     */   }
/*     */ 
/*     */   private class CauseAndFreq
/*     */     implements Comparable<CauseAndEffect<TCause, TEffect>.CauseAndFreq>
/*     */   {
/*     */     TCause cause;
/*     */     int frequency;
/*     */ 
/*     */    
public CauseAndFreq(TCause cause, Integer frequency) {
	// TODO Auto-generated constructor stub
	               this.cause = cause;
	/*  71 */       this.frequency = frequency;
}
/*     */ 
/*     */     public int compareTo(CauseAndEffect<TCause, TEffect>.CauseAndFreq compareWith) {
/*  75 */       return this.frequency - compareWith.frequency;
/*     */     }
/*     */ 
/*     */     public void purge() {
/*  79 */       CauseAndEffect.this.totalCausesMap.remove(this.cause);
/*  80 */       CauseAndEffect.this.causeAndEffectMap.remove(this.cause);
/*     */     }
/*     */   }
/*     */ }

/* Location:           E:\naval-robocode\sample\dmh.robocode.robot.BlackDeathMk7_7.0.jar
 * Qualified Name:     dmh.robocode.data.CauseAndEffect
 * JD-Core Version:    0.6.2
 */