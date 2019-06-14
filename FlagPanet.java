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
	TextField myP;
	TextField[] hostInfo;
	
	// Frame myInput;
	// Button setBtn;
	myInputBox myInput;
	
	FlagPanet( int _boardW, int _boardH, int _chessW, int _chessH ){
		status = MouseEvent.BUTTON1;
		chessW = _chessW; chessH = _chessH; boardW = _boardW; boardH = _boardH;
		p1 = new FivePoints(); p2 = new FivePoints(); isOver = false;
		myP = new TextField();
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
	}
	
	FlagPanet( Frame window, int _boardW, int _boardH, int _chessW, int _chessH ){
		status = MouseEvent.BUTTON1;
		chessW = _chessW; chessH = _chessH; boardW = _boardW; boardH = _boardH;
		p1 = new FivePoints(); p2 = new FivePoints(); isOver = false;
		myP = new TextField();
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
		if( myInputBox.myInputBoxA( window, "My Server Port", 
									new Object[]{ "Port", myP },
									new int[]{ 2 } ) == 1 ){
			try{ myPort =  Integer.parseInt( myP.getText() ); }
			catch( Exception e ){ System.out.println( "Port ERROR!" ); }
			System.out.println( "My Port: " + myPort );
		}
		
		if( myInputBox.myInputBoxA( window, "Host Information", 
									new Object[]{ "Host", hostInfo[0], "HostPort", hostInfo[1] },
									new int[]{ 2,2 } ) == 1 ){
			try{ host = InetAddress.getByName( hostInfo[0].getText() );	}
			catch( UnknownHostException e ){ System.out.println( "Host ERROR!" ); }
			try{ hostPort = Integer.parseInt( hostInfo[1].getText() ); }
			catch( Exception e ){ System.out.println( "Port ERROR!" ); }
			System.out.println( "Host: " + host.getHostAddress() + ": " + hostPort );
		}
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