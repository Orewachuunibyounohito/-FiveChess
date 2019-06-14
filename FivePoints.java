class FivePoints{
		int count;
		private FivePoint Head;
		
		FivePoints(){
			count = 0; Head = null;
		}
		
		public FivePoint getHead(){ return Head; }
		
		public void add( int _x, int _y ){
			FivePoint tmp, newPoint;
			newPoint = new FivePoint( _x, _y );
			if( Head == null ) Head = newPoint;
			else{
				tmp = Head;
				while( tmp.getNext() != null ){
					tmp = tmp.getNext();
				}
				tmp.setNext( newPoint );
			}
			count++;
		}
		
		public void add( FivePoint Src ){
			FivePoint tmp;
			if( Head == null ) Head = Src;
			else{
				tmp = Head;
				while( tmp.getNext() != null ){
					tmp = tmp.getNext();
				}
				tmp.setNext( Src );
			}
			count++;
		}
}
