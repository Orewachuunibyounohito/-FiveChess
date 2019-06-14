import java.awt.*;
import java.awt.event.*;
import java.net.*;

class FlagPanet extends Panel implements MouseListener{
	int showFlagPoint;
	int x, y;
	int chessW, chessH, boardW, boardH;
	int status;
	boolean isOver;
	Label turn;
	FivePoints p1, p2;
	InetAddress host;
	int hostPort, myPort;
	TextField[] hostInfo;
	
	// Frame myInput;
	// Button setBtn;
	myInputBox myInput;
	
	FlagPanet( int _boardW, int _boardH, int _chessW, int _chessH ){
		status = MouseEvent.BUTTON1;
		chessW = _chessW; chessH = _chessH; boardW = _boardW; boardH = _boardH;
		p1 = new FivePoints(); p2 = new FivePoints(); isOver = false;
		hostInfo = new TextField[2];
		setLocation( 50, 70 );
		setLayout( null );
		setSize( (boardW-1)*chessW+1+150, (boardH-1)*chessH+1 );
		this.addMouseListener( this );
		
		turn = new Label( "Black Turn" );
		this.add( turn );
		turn.setLocation( getWidth()-150, 0 );
		turn.setSize( 100, 50 );
		turn.setFont( new Font( "Times New Roman", 0, 16 ) );
		
		// createMyInput( "My server port", new Object[]{ "Host", hostInfo[0], "HostPort", hostInfo[1] }, new int[]{ 2,2 } );
		// myInput = new myInputBox(  "My server port", new Object[]{ "Host", hostInfo[0], "HostPort", hostInfo[1] }, new int[]{ 2,2 } );
	}
	
	FlagPanet( Frame window, int _boardW, int _boardH, int _chessW, int _chessH ) throws InterruptedException{
		status = MouseEvent.BUTTON1;
		chessW = _chessW; chessH = _chessH; boardW = _boardW; boardH = _boardH;
		p1 = new FivePoints(); p2 = new FivePoints(); isOver = false;
		hostInfo = new TextField[2];
		for( int i = 0; i < hostInfo.length; i++ ){
			hostInfo[i] = new TextField();
		}
		setLocation( 50, 70 );
		setLayout( null );
		setSize( (boardW-1)*chessW+1+150, (boardH-1)*chessH+1 );
		this.addMouseListener( this );
		
		turn = new Label( "Black Turn" );
		this.add( turn );
		turn.setLocation( getWidth()-150, 0 );
		turn.setSize( 100, 50 );
		turn.setFont( new Font( "Times New Roman", 0, 16 ) );
		
		// createMyInput( "My server port", new Object[]{ "Host", hostInfo[0], "HostPort", hostInfo[1] }, new int[]{ 2,2 } );
		myInput = new myInputBox( window, "My server port", new Object[]{ "Host", hostInfo[0], "HostPort", hostInfo[1] }, new int[]{ 2,2 } );
	}
	
	public void paint( Graphics g ){
		for( int i = 0; i < boardH-1; i++ ){
			g.drawLine( 0+chessW/2, i*chessH+chessH/2, (boardW-1)*chessW-chessW/2, i*chessH+chessH/2 );
		}
		for( int i = 0; i < boardW-1; i++ ){
			g.drawLine( i*chessW+chessW/2, 0+chessH/2, i*chessW+chessW/2, (boardH-1)*chessH-chessH/2 );
		}
		FivePoint temp = p1.getHead();
		g.setColor( new Color( 0, 0, 0 ) );
		while( temp != null ){
			g.fillOval( temp.getX()*chessW+1, temp.getY()*chessH+1, chessW-2, chessH-2 );
			temp = temp.getNext();
		}
		temp = p2.getHead();
		while( temp != null ){
			g.setColor( new Color( 255, 255, 255 ) );
			g.fillOval( temp.getX()*chessW+1, temp.getY()*chessH+1, chessW-2, chessH-2 );
			g.setColor( new Color( 0, 0, 0 ) );
			g.drawOval( temp.getX()*chessW+1, temp.getY()*chessH+1, chessW-2, chessH-2 );
			temp = temp.getNext();
		}
		g.setColor( new Color( 0, 0, 0 ) );
	}
	
	public boolean overlapping( FivePoints Src, FivePoint target ){
		FivePoint temp = Src.getHead();
		for( ; temp != null; ){
			if( temp.getX() == target.getX() && temp.getY()  == target.getY() ){
				return true;
			}
			temp = temp.getNext();
		}
		return false;
	}

	public int checkCombos( FivePoints Src , FivePoint target, int direct, int player ){
		int combos = 1, xx = 0, yy = 0;
		FivePoint temp;
		switch( direct ){
			case 0: // up->down
				xx = 0; yy = -1;
				break;
			case 1: // left->right
				xx = -1; yy = 0;
				break;
			case 2: // leftup->rightdown
				xx = -1; yy = -1;
				break;
			case 3: // leftdown->rightup
				xx = -1; yy = 1;
				break;
		}
		for( int i = 1, j = 0; i < 9; i++ ){
			if( i <= 4 ){
				j = i;
				temp = new FivePoint( target.getX()+j*xx, target.getY()+j*yy );
				if( overlapping( Src, temp ) ) combos++;
				else{ i = 4; continue; }
			}
			else{
				j = i - 4;
				temp = new FivePoint( target.getX()-j*xx, target.getY()-j*yy );
				if( overlapping( Src, temp ) ) combos++;
				else return combos;
			}
			
		}
		
		return combos;
	}
	
	// public int createMyInput( String title, Object[] Context, int[] index ) throws InterruptedException{
		// System.out.println( index.length );
		
		// myInput = new Frame( title );
		// myInput.setSize( 150, 25 );
		// myInput.setLocation( 1000, 100 );
		// myInput.setLayout( null );
		// myInput.setResizable( false );
		// Panel CenterP = new Panel(), HPanel = new Panel();
		// CenterP.setLocation( myInput.getWidth()/4, myInput.getHeight()/4 );
		// CenterP.setSize( myInput.getWidth()/2, myInput.getHeight()/2 );
		// CenterP.setLayout( new GridLayout( index.length, 1, 0, 5 ) );
		// // Box VBox = createVerticalBox(), HBox = createHorizontalBox();
		// int curr = 0;
		// String test;
		// myInput.setSize( myInput.getWidth(), myInput.getHeight()+index.length*50 );
		// CenterP.setLocation( myInput.getWidth()/4, (myInput.getHeight()-25)/4+25 );
		// CenterP.setSize( myInput.getWidth()/2, myInput.getHeight()/2 );
		// for( int i = 0; i < index.length; i++ ){
			// HPanel = new Panel();
			// // HPanel.setSize( index[i]*(50)+10, 25 );
			// HPanel.setLayout( new GridLayout( 1, index[i], 5, 0 ) );
			// // HBox = createHorizontalBox();
			// for( int j = 0; j < index[i]; j++ ){
				// // System.out.println( Context.toString() );
				// try{
					// test = (String)Context[curr];
					// System.out.println( "Is String!" );
					// System.out.println( test );
					// HPanel.add( new Label( test ) );
				// }
				// catch( Exception e ){
					// System.out.println( "Not String!" );
					// HPanel.add( (Component)Context[curr]);
				// }
				// // HBox.add( Context[curr] );
				// curr++;
			// }
			// CenterP.add( HPanel );
			// // VBox.add( HBox );
		// }
		// myInput.add( CenterP );
		
		// myInput.setSize( myInput.getWidth(), myInput.getHeight()+50 );
		// int comfirm = 0;
		// setBtn = new Button( "OK" );
		// setBtn.setSize( 50, 25 );
		// setBtn.setLocation( myInput.getWidth()/2-setBtn.getWidth()/2, myInput.getHeight()-50 );
		// setBtn.addMouseListener( new MouseAdapter(){
			// public void mouseClicked( MouseEvent e ){
				// if( e.getButton() == MouseEvent.BUTTON1 ){
					// comfirm = 1;
					// myInput.setVisible(false);
				// }
			// }
		// } );
		// myInput.add( setBtn );
		// myInput.setVisible( true );
		// myInput.addWindowListener( new WindowAdapter(){ public void windowClosing( WindowEvent e ){ comfirm = 0; myInput.setVisible(false); } } );
		// while( myInput.isVisible() ){
			// try{}
			// catch( Exception e ){ Thread.sleep( 50 ); }
		// }
		// return comfirm;
	// }
	
	public void mouseClicked( MouseEvent e ){
		if( isOver == true ) return;
		if( e.getButton() != status ) return;
		if( e.getX() < 0 || e.getX() > (boardW-1)*chessW || e.getY() < 0 || e.getY() > (boardH-1)*chessH ) return;
		int px, py;
		px = e.getX() / chessW; py = e.getY() / chessH;
		FivePoint temp = new FivePoint( px, py );
		if( overlapping( p2, temp ) ) return;
		if( overlapping( p1, temp ) ) return;
		switch( e.getButton() ){
			case MouseEvent.BUTTON1:
				status = MouseEvent.BUTTON3;
				turn.setText( "White Turn" );
				p1.add( temp );
				repaint();
				for( int i = 0; i < 4; i++ ){
					if( checkCombos( p1, temp, i, 1 ) >= 5 ){
						System.out.println( "Black Win!" );
						isOver = true;
						break;
					}
				}
				break;
			case MouseEvent.BUTTON3:
				status = MouseEvent.BUTTON1;
				turn.setText( "Black Turn" );
				p2.add( temp );
				repaint();
				for( int i = 0; i < 4; i++ ){
					if( checkCombos( p2, temp, i, 2 ) >= 5 ){
						System.out.println( "White Win!" );
						isOver = true;
						break;
					}
				}
				break;
		}
		
		
		// System.out.println( e.getX()+", "+e.getY() );
	}
	public void mousePressed( MouseEvent e ){}
	public void mouseReleased( MouseEvent e ){}
	public void mouseEntered( MouseEvent e ){}
	public void mouseExited( MouseEvent e ){}
}