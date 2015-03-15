/*     */ package dmh.robocode.data;
/*     */ 
/*     */ public class Location
/*     */ {
/*     */   private double x;
/*     */   private double y;
/*     */ 
/*     */   public Location(double x, double y)
/*     */   {
/*  28 */     this.x = x;
/*  29 */     this.y = y;
/*     */   }
/*     */ 
/*     */   public Location(Location location)
/*     */   {
/*  39 */     if (location != null) {
/*  40 */       this.x = location.x;
/*  41 */       this.y = location.y;
/*     */     }
/*     */   }
/*     */ 
/*     */   public double getX()
/*     */   {
/*  49 */     return this.x;
/*     */   }
/*     */ 
/*     */   public double getY()
/*     */   {
/*  56 */     return this.y;
/*     */   }
/*     */ 
/*     */   public int hashCode()
/*     */   {
/*  62 */     int prime = 31;
/*  63 */     int result = 1;
/*     */ 
/*  65 */     long temp = Double.doubleToLongBits(this.x);
/*  66 */     result = 31 * result + (int)(temp ^ temp >>> 32);
/*  67 */     temp = Double.doubleToLongBits(this.y);
/*  68 */     result = 31 * result + (int)(temp ^ temp >>> 32);
/*  69 */     return result;
/*     */   }
/*     */ 
/*     */   public boolean equals(Object obj)
/*     */   {
/*  75 */     if (this == obj)
/*  76 */       return true;
/*  77 */     if (obj == null)
/*  78 */       return false;
/*  79 */     if (getClass() != obj.getClass())
/*  80 */       return false;
/*  81 */     Location other = (Location)obj;
/*  82 */     if (Double.doubleToLongBits(this.x) != Double.doubleToLongBits(other.x))
/*  83 */       return false;
/*  84 */     if (Double.doubleToLongBits(this.y) != Double.doubleToLongBits(other.y))
/*  85 */       return false;
/*  86 */     return true;
/*     */   }
/*     */ 
/*     */   public boolean isSameAs(Location anotherLocation, double delta)
/*     */   {
/*  96 */     return (Math.abs(anotherLocation.x - this.x) <= Math.abs(delta)) && (
/*  97 */       Math.abs(anotherLocation.y - this.y) <= Math.abs(delta));
/*     */   }
/*     */ 
/*     */   public boolean isOnBattlefield(double width, double height)
/*     */   {
/* 107 */     return (this.x >= 0.0D) && (this.y >= 0.0D) && (this.x <= width) && (this.y <= height);
/*     */   }
/*     */ 
/*     */   public double getHowCloseToEdgeOfBattlefield(double width, double height)
/*     */   {
/* 117 */     if (!isOnBattlefield(width, height)) {
/* 118 */       return 0.0D;
/*     */     }
/* 120 */     return Math.min(Math.min(Math.abs(width - this.x), Math.abs(height - this.y)), Math.min(this.x, this.y));
/*     */   }
/*     */ 
/*     */   public double getHowCloseToEdgeOfBattlefield()
/*     */   {
/* 125 */     return getHowCloseToEdgeOfBattlefield(BattleConstants.getInstance().getBattlefieldWidth(), BattleConstants.getInstance().getBattlefieldHeight());
/*     */   }
/*     */ 
/*     */   public byte getZone(double width, double height)
/*     */   {
/* 141 */     byte row = (byte)(int)(this.y / height * 5.0D);
/* 142 */     byte column = (byte)(int)(this.x / width * 5.0D);
/*     */ 
/* 144 */     return (byte)((4 - row) * 5 + column + 1);
/*     */   }
/*     */ 
/*     */   public String getDebugString()
/*     */   {
/* 152 */     return "[ " + this.x + ", " + this.y + " ]";
/*     */   }
/*     */ 
/*     */   public static Location getMidPoint(Location locationA, Location locationB)
/*     */   {
/* 160 */     return new Location((locationA.x + locationB.x) / 2.0D, (locationA.y + locationB.y) / 2.0D);
/*     */   }
/*     */ 
/*     */   public void chopNearEdgesOfBattlefield(double xGap, double yGap) {
/* 164 */     if (this.x < xGap)
/* 165 */       this.x = xGap;
/* 166 */     else if (this.x > BattleConstants.getInstance().getBattlefieldWidth() - xGap) {
/* 167 */       this.x = (BattleConstants.getInstance().getBattlefieldWidth() - xGap);
/*     */     }
/* 169 */     if (this.y < yGap)
/* 170 */       this.y = yGap;
/* 171 */     else if (this.y > BattleConstants.getInstance().getBattlefieldHeight() - yGap)
/* 172 */       this.y = (BattleConstants.getInstance().getBattlefieldHeight() - yGap);
/*     */   }
/*     */ }

/* Location:           E:\naval-robocode\sample\dmh.robocode.robot.BlackDeathMk7_7.0.jar
 * Qualified Name:     dmh.robocode.data.Location
 * JD-Core Version:    0.6.2
 */