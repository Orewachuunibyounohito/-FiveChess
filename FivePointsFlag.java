import java.awt.*;
import java.awt.event.*;

class FivePointsFlag extends Frame{
	WindowAdapter closing;
	FlagPanet chessboard;
	Button reBtn;
	MouseAdapter restart;
	Thread server, client;
	int bW, bH, cW, cH;
	FivePointsFlag(){
		bW = 30; bH = 30; cW = 20; cH = 20;
		setTitle( "New Window" );
		chessboard = new FlagPanet( bW, bH, cW, cH );
		setLocation( 100, 100 );
		setSize( chessboard.boardW*chessboard.chessW+200, chessboard.boardH*chessboard.chessH+140 );
		setLayout( null );
		setVisible( true );
		
		closing = new WindowAdapter(){	public void windowClosing( WindowEvent e ){ System.exit(0); } };
		add( chessboard );
		addWindowListener( closing );
		
		restart = new MouseAdapter(){
								public void mouseClicked( MouseEvent e ){
									if( e.getButton() == MouseEvent.BUTTON1 ){
										remove( chessboard );
										chessboard = new FlagPanet( bW, bH, cW, cH );
										add( chessboard );
									}
								}
							};
		
		Button reBtn = new Button( "Restart" );
		reBtn.setLocation( chessboard.getX()+(chessboard.boardW-1)*chessboard.chessW-100, chessboard.getY()+(chessboard.boardH-1)*chessboard.chessH+30 );
		reBtn.setSize( 100, 30 );
		add( reBtn );
		reBtn.addMouseListener( restart );
	}
	
	FivePointsFlag( int _bW, int _bH, int _cW, int _cH ) throws InterruptedException{
		bW = _bW; bH = _bH; cW = _cW; cH = _cH;
		chessboard = new FlagPanet( this, bW, bH, cW, cH );
		setLocation( 100, 100 );
		setSize( chessboard.boardW*chessboard.chessW+200, chessboard.boardH*chessboard.chessH+140 );
		setLayout( null );
		setVisible( true );
		
		closing = new WindowAdapter(){	public void windowClosing( WindowEvent e ){ System.exit(0); } };
		add( chessboard );
		addWindowListener( closing );
		
		restart = new MouseAdapter(){
								public void mouseClicked( MouseEvent e ){
									if( e.getButton() == MouseEvent.BUTTON1 ){
										remove( chessboard );
										chessboard = new FlagPanet( bW, bH, cW, cH );
										add( chessboard );
									}
								}
							};
		
		Button reBtn = new Button( "Restart" );
		reBtn.setLocation( chessboard.getX()+(chessboard.boardW-1)*chessboard.chessW-100, chessboard.getY()+(chessboard.boardH-1)*chessboard.chessH+30 );
		reBtn.setSize( 100, 30 );
		add( reBtn );
		reBtn.addMouseListener( restart );
	}
	
	FivePointsFlag( String title ){
		this();
		setTitle( title );
	}
	
	FivePointsFlag( int _bW, int _bH, int _cW, int _cH, String title ) throws InterruptedException{
		this( _bW, _bH, _cW, _cH );
		setTitle( title );
		
		server = new Thread(){
			public void run(){
				final int buffSize = 2048;
				byte[] buff = new byte[buffSize];
				DatagramPacket packet = new DatagramPacket( buff, 0, buffSize );
				DatagramSocket socket = new DatagramSocket( chessboard.myPort );
				while( !chessboard.isOver ){
					// status == 1: my turn ; status == 0: opponent turn 
					switch( chessboard.status ){
						case 0:
							socket.receive( packet );
							break;
						case 1:
							break;
					}
				}
			}
		};
	}
	
}