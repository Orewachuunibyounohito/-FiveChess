import java.awt.*;
import java.awt.event.*;
import java.net.*;

class FivePointsFlag extends Frame{
	WindowAdapter closing;
	FlagPanet chessboard;
	Button reBtn, sendBtn, linkBtn;
	MouseAdapter restart, send, link;
	Thread server, client;
	int clientEvent, status;
	int bW, bH, cW, cH, player;
	boolean isTalking, isLinking;
	FivePointsFlag(){
		bW = 30; bH = 30; cW = 20; cH = 20;
		setTitle( "New Window" );
		chessboard = new FlagPanet( bW, bH, cW, cH );
		setLocation( 100, 100 );
		setSize( chessboard.boardW*chessboard.chessW+50+0, chessboard.boardH*chessboard.chessH+140 );
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
	
	FivePointsFlag( int _bW, int _bH, int _cW, int _cH ){
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
										chessboard = new FlagPanet( (FivePointsFlag)getFrames()[0], bW, bH, cW, cH );
										add( chessboard );
									}
								}
							};
		
		reBtn = new Button( "Restart" );
		reBtn.setLocation( chessboard.getX()+(chessboard.boardW-1)*chessboard.chessW-100, chessboard.getY()+(chessboard.boardH-1)*chessboard.chessH+30 );
		reBtn.setSize( 100, 30 );
		add( reBtn );
		reBtn.addMouseListener( restart );
		
		send = new MouseAdapter(){
								public void mouseClicked( MouseEvent e ){
									if( e.getButton() == MouseEvent.BUTTON1 ){
										clientEvent = 1;
										// System.out.println( "send Something" );
									}
								}
							};
		
		sendBtn = new Button( "Send" );
		sendBtn.setLocation( reBtn.getX()-120, reBtn.getY() );
		sendBtn.setSize( 100, 30 );
		add( sendBtn );
		sendBtn.addMouseListener( send );
		
		link = new MouseAdapter(){
								public void mouseClicked( MouseEvent e ){
									if( e.getButton() == MouseEvent.BUTTON1 ){
										if( !isLinking ){
											player = 1+(int)(Math.random()*(2-1+1));
											if( player == 1 ){
												chessboard.p1.clr = new Color( 0, 0, 0 );
												chessboard.p2.clr = new Color( 255, 255, 255 );
												chessboard.status = 0;
											}
											else if ( player == 2 ){
												chessboard.p1.clr = new Color( 255, 255, 255 );
												chessboard.p2.clr = new Color( 0, 0, 0 );
												chessboard.status = 1;
											}
											client.start();
										}
										// System.out.println( "send Something" );
									}
								}
							};
		
		linkBtn = new Button( "Link" );
		linkBtn.setLocation( sendBtn.getX()-120, sendBtn.getY() );
		linkBtn.setSize( 100, 30 );
		add( linkBtn );
		linkBtn.addMouseListener( link );
	}
	
	FivePointsFlag( String title ){
		this();
		setTitle( title );
	}
	
	FivePointsFlag( int _bW, int _bH, int _cW, int _cH, String title ) throws SocketException{
		this( _bW, _bH, _cW, _cH );
		setTitle( title );
		isTalking = false; isLinking = false;
		clientEvent = -2;
		status = 0;
		// addMouseListener( new MouseAdapter(){ public void mouseClicked( MouseEvent e ){ if( e.getButton() == e.BUTTON1 ) clientEvent = 1; } } );
		server = new Thread(){
			public void run(){
				String msg, header;
				final int buffSize = 2048;
				byte[] buff = new byte[buffSize];
				try{
					while( true ){
						DatagramPacket packet = new DatagramPacket( buff, buffSize );
						// System.out.println( "SpacketOK" );
						DatagramSocket socket = new DatagramSocket( chessboard.myPort );
						// System.out.println( "SsocketOK" );
						if( isLinking ){
							try{ socket.setSoTimeout( 30000 ); }
							catch( Exception e ){}
						}
						//status 1 receive Commond -- status 2 receive OK -- status 3 receive OK
						if( clientEvent == 0 && status == 1 ){
							try{ socket.setSoTimeout( 5000 ); }
							catch( Exception e ){}
						}
						try{
							socket.receive( packet );
							// System.out.println( "ReceiveOK" );
						}
						catch( Exception e ){
							status = 0;
							socket.setSoTimeout( 0 );
						}
						msg = new String( buff, 0, packet.getLength() );
						// System.out.println( msg );
						header = msg.substring( 0, msg.indexOf("/") );
						// System.out.println( header );
						if( !isLinking ){
							if( header.compareTo( "@OK" ) == 0 ){
								clientEvent = 0;
								status = 1;
								System.out.println( "OK" );
								isLinking = true;
								chessboard.isStart = true;
							}
							else if( header.compareTo( "@l" ) == 0 ){
								msg = msg.substring( msg.indexOf("/")+1, msg.length() );
								System.out.println( msg );
								if( msg.compareTo( "1" ) == 0 ){
									chessboard.status = 1;
									chessboard.p1.clr = new Color( 255, 255, 255 );
									chessboard.p2.clr = new Color( 0, 0 ,0 );
									System.out.println( "Opponent first." );
								}
								else{
									chessboard.status = 0;
									chessboard.p1.clr = new Color( 0, 0 ,0 );
									chessboard.p2.clr = new Color( 255, 255, 255 );
									System.out.println( "You first." );
								}
								clientEvent = 0;
								status = 1;
								System.out.println( "Host Linked" );
								isLinking = true;
								chessboard.isStart = true;
								client.start();
							}
						}
						else{
							switch( status ){
								case 0:
									if( header.compareTo( "@OK" ) == 0 ){
										clientEvent = 0;
										status = 1;
										System.out.println( "OK" );
										try{ socket.setSoTimeout( 5000 ); }
										catch( Exception e ){}
									}
									else if( header.compareTo( "@t" ) == 0 ){
										msg = msg.substring( msg.indexOf("/")+1, msg.length() );
										System.out.println( msg );
										clientEvent = 0;
										status = 1;
										// System.out.println( "Talk" );
										try{ socket.setSoTimeout( 5000 ); }
										catch( Exception e ){}
									}
									else if( header.compareTo( "@p" ) == 0 ){
										msg = msg.substring( msg.indexOf("/")+1, msg.length() );
										int px = Integer.parseInt( msg.substring( 0, msg.indexOf( "," ) ) );
										int py = Integer.parseInt( msg.substring( msg.indexOf( "," )+1, msg.length() ) );
										FivePoint temp = new FivePoint( px, py );
										chessboard.p2.add( temp );
										chessboard.repaint();
										for( int i = 0; i < 4; i++ ){
											if( chessboard.checkCombos( chessboard.p2, temp, i, 2 ) >= 5 ){
												System.out.println( "You Lost!" );
												chessboard.isOver = true;
												break;
											}
										}
										chessboard.status = 0;
										System.out.println( msg );
										clientEvent = 0;
										status = 1;
										// System.out.println( "Talk" );
									}
									break;
								case 1:
									if( header.compareTo( "@OK" ) == 0 ){
										clientEvent = -1;
										status = 0;
										System.out.println( "zz: " + chessboard.status );
										System.out.println( "OK1" );
									}
									break;
							}
						}
						socket.close();
						try{ sleep(50); }
						catch( Exception e ){}
					}
					// socket.close();
				}
				catch( Exception e ){ System.out.println( "No response -> Game Over!" ); }
			}
		};
		
		client = new Thread(){
			public void run(){
				int cnt = 0;
				String msg;
				byte[] buff = new byte[0];
				try{ 
					while( true ){
						DatagramPacket packet = new DatagramPacket( buff, buff.length, chessboard.host, chessboard.hostPort );
						// System.out.println( "CpacketOK" );
						DatagramSocket socket = new DatagramSocket();
						// System.out.println( "CsocketOK" );
						if( !isLinking ){
							switch( clientEvent ){
								case -2:
									msg = "@l/";
									msg = msg + String.valueOf( player );
									buff = msg.getBytes();
									packet.setData( buff );
									socket.send( packet );
									break;
								case 0:
									msg = "@OK/";
									buff = msg.getBytes();
									packet.setData( buff );
									socket.send( packet );
									if( status == 1 ) cnt++;
									if( cnt == 10 ){ status = 0; cnt = 0; clientEvent = -2; isLinking = true; }
									break;
							}
						}
						else
						{
							switch( clientEvent ){
								case 0:
									msg = "@OK/";
									buff = msg.getBytes();
									packet.setData( buff );
									socket.send( packet );
									if( status == 1 ) cnt++;
									if( cnt == 10 ){ status = 0; cnt = 0; clientEvent = -2; }
									break;
								case 1:
									msg = "ZZZ you are good!";
									msg = "@t/" + msg;
									buff = msg.getBytes(); 
									packet.setData( buff );
									socket.send( packet );
									break;
								case 2:
									msg = "@p/";
									msg = msg + String.valueOf( chessboard.p1.getLast().getX() ) + "," + String.valueOf( chessboard.p1.getLast().getY() );
									buff = msg.getBytes();
									packet.setData( buff );
									socket.send( packet );
									break;
							}
						}
						socket.close();
						try{ sleep(50); }
						catch( Exception e ){}
					}
				}
				catch( Exception e ){ System.out.println( "zz2" ); }
			}
		};
		
		server.start();
		// client.start();
	}
	
}