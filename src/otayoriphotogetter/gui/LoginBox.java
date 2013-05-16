package otayoriphotogetter.gui;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JTextField;

import otayoriphotogetter.core.LoginInfo;

@SuppressWarnings("serial")
public class LoginBox extends JDialog implements ActionListener
{
	private FlowLayout baseLayout;

	private JTextField accountField;
	private JTextField passwordField;
	private JButton cancelButton;
	private JButton okButton;

	private LoginFrame loginFrame;

	public LoginBox( LoginFrame loginFrame )
	{
		this.loginFrame = loginFrame;

		setTitle( "login" );
		setSize( 200, 122 );
		setLocationRelativeTo( null );

		initialComponent();
	}

	public void initialComponent()
	{
		baseLayout = new FlowLayout();
		getContentPane().setLayout( baseLayout );

		accountField = new JTextField( 10 );
		getContentPane().add( accountField );

		passwordField = new JTextField( 10 );
		getContentPane().add( passwordField );

		cancelButton = new JButton( "cancel" );
		cancelButton.addActionListener( this );
		getContentPane().add( cancelButton );

		okButton = new JButton( "ok" );
		okButton.addActionListener( this );
		getContentPane().add( okButton );
	}

	public void actionPerformed( ActionEvent e )
	{
		setVisible( false );

		if( e.getSource().equals( okButton ) )
		{
			if( loginFrame != null )
			{
				String account = accountField.getText();
				String password = passwordField.getText();
				LoginInfo loginInfo = new LoginInfo( account, password );

				loginFrame.login( loginInfo );
			}
			else
			{
				System.exit( 0 );
			}
		}
		else
		{
			System.exit( 0 );
		}
	}
}
