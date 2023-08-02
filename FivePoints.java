import java.awt.Color;

class FivePoints{
		int count;
		private FivePoint Head;
		private FivePoint Tail;
		Color clr;
		
		FivePoints(){
			count = 0; Head = null; clr = new Color( 0, 0 ,0 );
		}
		
		public FivePoint getHead(){ return Head; }
		public FivePoint getLast(){ return Tail; }
		
		public void add( int _x, int _y ){
			FivePoint tmp, newPoint;
			newPoint = new FivePoint( _x, _y );
			if( Head == null ){ Head = newPoint; }
			else{
				tmp = Head;
				while( tmp.getNext() != null ){
					tmp = tmp.getNext();
				}
				tmp.setNext( newPoint );
			}
			Tail = newPoint;
			count++;
		}
		
		public void add( FivePoint Src ){
			add( Src.getX(), Src.getY() );
		}
}
