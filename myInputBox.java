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
		setBtn.addMouseListener( BtnEvent );
		addWindowListener( closing );
	}
	
	myInputBox( Frame _window, String title, Object[] Context, int[] index ) throws InterruptedException{
		this();
		window = _window;
		this.setTitle( title );
		setSize( 150, 25 );
		setLocation( 1000, 100 );
		setLayout( null );
		setResizable( false );
		Panel CenterP = new Panel(), HPanel = new Panel();
		CenterP.setLocation( 50, 75 );
		CenterP.setSize( getWidth()/2, getHeight()/2 );
		CenterP.setLayout( new GridLayout( index.length, 1, 0, 5 ) );
		int curr = 0;
		String test;
		setSize( getWidth(), getHeight()+index.length*50 );
		CenterP.setLocation( getWidth()/4, (getHeight()-25)/4+25 );
		CenterP.setSize( getWidth()/2, getHeight()/2 );
		for( int i = 0; i < index.length; i++ ){
			HPanel = new Panel();
			HPanel.setLayout( new GridLayout( 1, index[i], 5, 0 ) );
			for( int j = 0; j < index[i]; j++ ){
				try{
					test = (String)Context[curr];
					System.out.println( "Is String!" );
					System.out.println( test );
					HPanel.add( new Label( test ) );
				}
				catch( Exception e ){
					System.out.println( "Not String!" );
					HPanel.add( (Component)Context[curr]);
				}
				curr++;
			}
			CenterP.add( HPanel );
		}
		add( CenterP );
		
		setSize( getWidth(), getHeight()+50 );
		comfirm = 0;
		setBtn = new Button( "OK" );
		setBtn.setSize( 50, 25 );
		setBtn.setLocation( getWidth()/2-setBtn.getWidth()/2, getHeight()-50 );
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
		// while( isVisible() ){
			// // try{}
			// // catch( Exception e ){ window.Thread.sleep( 1000 ); }
		// }
	}
	
	public int getConfirm(){ return comfirm; }
}