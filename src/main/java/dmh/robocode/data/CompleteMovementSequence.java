/*    */ package dmh.robocode.data;
/*    */ 
/*    */ import java.util.Arrays;
/*    */ import java.util.List;
/*    */ 
/*    */ public class CompleteMovementSequence
/*    */ {
/*    */   Movement[] movements;
/*    */ 
/*    */   public CompleteMovementSequence(Movement[] sequenceAsArray)
/*    */   {
/* 15 */     this.movements = ((Movement[])sequenceAsArray.clone());
/*    */   }
/*    */ 
/*    */   public CompleteMovementSequence(List<Movement> sequenceAsList) {
/* 19 */     if (sequenceAsList == null)
/* 20 */       this.movements = new Movement[0];
/*    */     else
/* 22 */       this.movements = ((Movement[])sequenceAsList.toArray(new Movement[0]));
/*    */   }
/*    */ 
/*    */   public CompleteMovementSequence(DynamicMovementSequence sequence, long startTime, long endTime)
/*    */   {
/* 29 */     this.movements = new Movement[(int)(endTime - startTime + 1L)];
/* 30 */     for (long time = startTime; time <= endTime; time += 1L)
/* 31 */       this.movements[((int)(time - startTime))] = sequence.getAtTime(time);
/*    */   }
/*    */ 
/*    */   public int getLength()
/*    */   {
/* 36 */     return this.movements.length;
/*    */   }
/*    */ 
/*    */   public Movement getMovement(int index) {
/* 40 */     if ((index >= 0) && (index < this.movements.length)) {
/* 41 */       return this.movements[index];
/*    */     }
/* 43 */     return null;
/*    */   }
/*    */ 
/*    */   public String getDebugString()
/*    */   {
/* 49 */     String result = "{ ";
/* 50 */     boolean isFirstMovement = true;
/*    */ 
/* 53 */     for (Movement movement : this.movements) {
/* 54 */       if (isFirstMovement)
/* 55 */         isFirstMovement = false;
/*    */       else {
/* 57 */         result = result + "->";
/*    */       }
/* 59 */       result = result + movement.getDebugString();
/*    */     }
/*    */ 
/* 62 */     return result + " }";
/*    */   }
/*    */ 
/*    */   public int hashCode()
/*    */   {
/* 67 */     int prime = 31;
/* 68 */     int result = 1;
/* 69 */     result = 31 * result + Arrays.hashCode(this.movements);
/* 70 */     return result;
/*    */   }
/*    */ 
/*    */   public boolean equals(Object obj)
/*    */   {
/* 75 */     if (this == obj)
/* 76 */       return true;
/* 77 */     if (obj == null)
/* 78 */       return false;
/* 79 */     if (getClass() != obj.getClass())
/* 80 */       return false;
/* 81 */     CompleteMovementSequence other = (CompleteMovementSequence)obj;
/* 82 */     if (!Arrays.equals(this.movements, other.movements))
/* 83 */       return false;
/* 84 */     return true;
/*    */   }
/*    */ }

/* Location:           E:\naval-robocode\sample\dmh.robocode.robot.BlackDeathMk7_7.0.jar
 * Qualified Name:     dmh.robocode.data.CompleteMovementSequence
 * JD-Core Version:    0.6.2
 */