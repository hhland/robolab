/*     */ package dmh.robocode.data;
/*     */ 
/*     */ import dmh.robocode.utils.Geometry;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ 
/*     */ public class DynamicMovementSequence
/*     */   implements Cloneable
/*     */ {
/*     */   private List<Movement> moves;
/*  14 */   private long timeAtEndOfCurrentMoves = 0L;
/*     */ 
/*     */   public DynamicMovementSequence() {
/*  17 */     this.moves = new ArrayList();
/*     */   }
/*     */ 
/*     */   public DynamicMovementSequence(Movement movement, long timeSeen) {
/*  21 */     this.moves = new ArrayList();
/*  22 */     add(movement, timeSeen);
/*     */   }
/*     */ 
/*     */   public int getLength() {
/*  26 */     return this.moves.size();
/*     */   }
/*     */ 
/*     */   public Movement getLatest() {
/*  30 */     int index = this.moves.size() - 1;
/*     */ 
/*  32 */     if (index < 0) {
/*  33 */       return null;
/*     */     }
/*  35 */     return (Movement)this.moves.get(index);
/*     */   }
/*     */ 
/*     */   public Movement getPrevious()
/*     */   {
/*  40 */     int index = this.moves.size() - 2;
/*     */ 
/*  42 */     if (index < 0) {
/*  43 */       return null;
/*     */     }
/*  45 */     return (Movement)this.moves.get(index);
/*     */   }
/*     */ 
/*     */   public Movement getAtTime(long time)
/*     */   {
/*  50 */     int index = (int)(getLength() - 1 - (this.timeAtEndOfCurrentMoves - time));
/*     */ 
/*  52 */     if ((index >= 0) && (index < getLength())) {
/*  53 */       return (Movement)this.moves.get(index);
/*     */     }
/*  55 */     return null;
/*     */   }
/*     */ 
/*     */   public void add(Movement movement, long timeSeen)
/*     */   {
/*  60 */     if ((timeSeen != this.timeAtEndOfCurrentMoves + 1L) && (this.timeAtEndOfCurrentMoves != 0L)) {
/*  61 */       this.moves.clear();
/*     */     }
/*  63 */     this.moves.add(movement);
/*  64 */     this.timeAtEndOfCurrentMoves = timeSeen;
/*     */   }
/*     */ 
/*     */   public boolean areUpToDate(long timeNow) {
/*  68 */     return this.timeAtEndOfCurrentMoves == timeNow;
/*     */   }
/*     */ 
/*     */   public int getConsistentMoveCount(int maxMoves)
/*     */   {
/*  73 */     if (this.moves.size() <= 2) {
/*  74 */       return 0;
/*     */     }
/*     */ 
/*  77 */     int sameMoves = 0;
/*  78 */     int moveIndex = Math.max(1, this.moves.size() - maxMoves);
/*  79 */     Movement previousMove = (Movement)this.moves.get(moveIndex - 1);
/*     */ 
/*  81 */     while (moveIndex < this.moves.size()) {
/*  82 */       Movement nextMove = (Movement)this.moves.get(moveIndex);
/*  83 */       if (nextMove.equals(previousMove)) {
/*  84 */         sameMoves++;
/*     */       }
/*  86 */       previousMove = nextMove;
/*  87 */       moveIndex++;
/*     */     }
/*  89 */     return sameMoves;
/*     */   }
/*     */ 
/*     */   public Movement getAverageMove(int maxMoves)
/*     */   {
/*  94 */     if (this.moves.isEmpty()) {
/*  95 */       return null;
/*     */     }
/*     */ 
/*  98 */     Location zeroStart = new Location(0.0D, 0.0D);
/*  99 */     Location location = new Location(zeroStart);
/* 100 */     int moveCount = 0;
/* 101 */     int moveIndex = Math.max(0, this.moves.size() - maxMoves);
/*     */ 
/* 103 */     while (moveIndex < this.moves.size()) {
/* 104 */       moveCount++;
/* 105 */       Movement thisMove = (Movement)this.moves.get(moveIndex);
/* 106 */       moveIndex++;
/* 107 */       location = Geometry.getLocationAtBearing(location, thisMove.getRelativeBearing(), thisMove.getDistance());
/*     */     }
/*     */ 
/* 110 */     double totalDistance = Geometry.getDistanceBetweenLocations(zeroStart, location);
/* 111 */     double overallBearing = Geometry.getBearingBetweenLocations(zeroStart, location);
/*     */ 
/* 113 */     return new Movement(overallBearing / moveCount, totalDistance / moveCount);
/*     */   }
/*     */ 
/*     */   public Movement getAverageMoveCRAP(int maxMoves)
/*     */   {
/* 118 */     if (this.moves.isEmpty()) {
/* 119 */       return null;
/*     */     }
/*     */ 
/* 122 */     int moveCount = 0;
/* 123 */     double turnTotal = 0.0D;
/* 124 */     double distanceTotal = 0.0D;
/*     */ 
/* 126 */     int moveIndex = Math.max(0, this.moves.size() - maxMoves);
/*     */ 
/* 128 */     while (moveIndex < this.moves.size()) {
/* 129 */       moveCount++;
/* 130 */       turnTotal += ((Movement)this.moves.get(moveIndex)).getRelativeBearing();
/* 131 */       distanceTotal += ((Movement)this.moves.get(moveIndex)).getDistance();
/* 132 */       moveIndex++;
/*     */     }
/*     */ 
/* 135 */     return new Movement(turnTotal / moveCount, distanceTotal / moveCount);
/*     */   }
/*     */ 
/*     */   public int countSameMovement(int maxMoves, Movement expectedMove)
/*     */   {
/* 142 */     if (this.moves.size() <= 2) {
/* 143 */       return 0;
/*     */     }
/*     */ 
/* 146 */     int sameMoves = 0;
/* 147 */     int moveIndex = Math.max(1, this.moves.size() - maxMoves);
/*     */ 
/* 149 */     while (moveIndex < this.moves.size())
/*     */     {
/* 151 */       if (((Movement)this.moves.get(moveIndex)).equals(expectedMove)) {
/* 152 */         sameMoves++;
/*     */       }
/* 154 */       moveIndex++;
/*     */     }
/* 156 */     return sameMoves;
/*     */   }
/*     */ 
/*     */   public DynamicMovementSequence cloneMovementsSince(long time) {
/* 160 */     DynamicMovementSequence clone = new DynamicMovementSequence();
/* 161 */     clone.timeAtEndOfCurrentMoves = this.timeAtEndOfCurrentMoves;
/* 162 */     int startFromIndex = (int)Math.max(0L, this.moves.size() - 1 - this.timeAtEndOfCurrentMoves + time);
/* 163 */     for (int i = startFromIndex; i < this.moves.size(); i++) {
/* 164 */       clone.moves.add((Movement)this.moves.get(i));
/*     */     }
/* 166 */     return clone;
/*     */   }
/*     */ }

/* Location:           E:\naval-robocode\sample\dmh.robocode.robot.BlackDeathMk7_7.0.jar
 * Qualified Name:     dmh.robocode.data.DynamicMovementSequence
 * JD-Core Version:    0.6.2
 */