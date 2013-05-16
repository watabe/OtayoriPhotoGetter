package otayoriphotogetter.gui;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import otayoriphotogetter.core.LoginInfo;
import otayoriphotogetter.core.OtayoriPhotoPanel;

@SuppressWarnings("serial")
public class MainFrame extends JFrame implements LoginFrame
{
	private JScrollPane scrollPane;
	private JPanel mainPanel;

	private OtayoriPhotoPanel core;

	public MainFrame()
	{
		setTitle( "OtayoriPhotoGetter" );
		setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
		setSize( 800, 600 );
		setLocationRelativeTo( null );

		initialComponent();
	}

	public void initialComponent()
	{
		mainPanel = new JPanel();

		scrollPane = new JScrollPane( mainPanel );
		getContentPane().add( scrollPane );
	}

	public void login( LoginInfo loginInfo )
	{
		// TODO 別スレッド化しないと重くて閉じない。
		// TODO ログイン時の例外処理を何とかしたい。
		try
		{
			core = new OtayoriPhotoPanel(
				loginInfo.account
				, loginInfo.password
			);

			core.getPhotoPanelList();
		}
		catch( Exception e )
		{
			e.printStackTrace();
		}
	}
}
