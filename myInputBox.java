import java.awt.*;
import java.awt.event.*;

class myInputBox extends Frame{
	private int comfirm;
	Button setBtn;
	MouseAdapter BtnEvent;
	WindowAdapter closing;
	Frame window;
	
	myInputBox(){
		setBtn = new Button( "OK" );
		BtnEvent = new MouseAdapter(){
			public void mouseClicked( MouseEvent e ){
				if( e.getButton() == MouseEvent.BUTTON1 ){
					comfirm = 1;
					setVisible(false);
				}
			}
		};
		closing = new WindowAdapter(){ public void windowClosing( WindowEvent e ){
			comfirm = 0;
			setVisible(false);
			window.setEnabled( true );
			window.requestFocus();
		} };
		comfirm = -1;
		setBtn.addMouseListener( BtnEvent );
		addWindowListener( closing );
	}
	
	myInputBox( Frame _window, String title, Object[] Context, int[] index ){
		this();
		window = _window;
		this.setTitle( title );
		setSize( 150, 15+50 );
		setLocation( 1000, 100 );
		setLayout( null );
		setResizable( false );
		Panel CenterP = new Panel(), HPanel = new Panel();
		CenterP.setLocation( 50, 15+50 );
		CenterP.setSize( getWidth()-100, getHeight()-15-50 );
		CenterP.setLayout( new GridLayout( index.length, 1, 0, 5 ) );
		
		int curr = 0;
		String test;
		int maxItem = index[0];
		for( int i = 1; i < index.length; i++ ){
			if( maxItem < index[i] ) maxItem = index[i];
		}
		setSize( getWidth()+maxItem*100, getHeight()+index.length*25 );
		CenterP.setLocation( 50, 15+50 );
		CenterP.setSize( getWidth()-100, (getHeight()-15)-50 );
		setSize( getWidth(), getHeight()+25 );
		setSize( getWidth(), getHeight()+50 );
		
		
		for( int i = 0; i < index.length; i++ ){
			HPanel = new Panel();
			HPanel.setLayout( new GridLayout( 1, index[i], 5, 0 ) );
			for( int j = 0; j < index[i]; j++ ){
				try{
					test = (String)Context[curr];
					// System.out.println( "Is String!" );
					// System.out.println( test );
					HPanel.add( new Label( test ) );
				}
				catch( Exception e ){
					// System.out.println( "Not String!" );
					HPanel.add( (Component)Context[curr]);
				}
				curr++;
			}
			CenterP.add( HPanel );
		}
		add( CenterP );
		
		setBtn = new Button( "OK" );
		setBtn.setSize( 50, 25 );
		setBtn.setLocation( getWidth()/2-setBtn.getWidth()/2, CenterP.getY()+CenterP.getHeight()+10 );
		setBtn.addMouseListener( new MouseAdapter(){
			public void mouseClicked( MouseEvent e ){
				if( e.getButton() == MouseEvent.BUTTON1 ){
					comfirm = 1;
					setVisible(false);
					window.setEnabled( true );
					window.requestFocus();
				}
			}
		} );
		
		add( setBtn );
		setVisible( true );
		window.setEnabled( false );
		while( comfirm == -1 ){
			try{ Thread.sleep(50); }
			catch( Exception e ){}
		}
	}
	
	public static int myInputBoxA( Frame _window, String title, Object[] Context, int[] index ){
		return (new myInputBox( _window, title, Context, index )).getConfirm();
	}
	
	public int getConfirm(){ return comfirm; }
}