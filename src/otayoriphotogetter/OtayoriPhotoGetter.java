package otayoriphotogetter;

import otayoriphotogetter.gui.LoginBox;
import otayoriphotogetter.gui.MainFrame;

public class OtayoriPhotoGetter
{
	public static void main( String[] args )
	{
		MainFrame mainFrame = new MainFrame();

		LoginBox loginBox = new LoginBox( mainFrame );
		loginBox.setVisible( true );

		
	}
}
