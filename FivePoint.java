class FivePoint{
		private int x, y;
		private FivePoint next;
		
		FivePoint(){
			x = 0; y = 0; next = null;
		}
		
		FivePoint( FivePoint Src ){
			this();
			x = Src.x; y = Src.y;
		}
		
		FivePoint( int _x, int _y ){
			this();
			x = _x; y = _y;
		}
		
		FivePoint( int _x, int _y, FivePoint _next ){
			this( _x, _y );
			next = _next;
		}
		
		public void setX( int _x ){ x = _x; }
		public void setY( int _y ){ y = _y; }
		public void setNext( FivePoint _next ){ next = _next; }
		public int getX(){ return x; }
		public int getY(){ return y; }
		public FivePoint getNext(){ return next; }
}
