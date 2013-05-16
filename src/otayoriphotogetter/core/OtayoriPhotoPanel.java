package otayoriphotogetter.core;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.DomNodeList;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlImageInput;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlPasswordInput;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;

public class OtayoriPhotoPanel
{
	private static URL LOGIN_URL;
	private static URL BASE_URL;
	private static URL TOP_URL;

	static
	{
		try
		{
			LOGIN_URL = new URL( "https://otayori-docomo.com" );
			BASE_URL = new URL( "https://otayori-docomo.com/" );
			TOP_URL = new URL( "https://otayori-docomo.com/Top.htm" );
		}
		catch( MalformedURLException e )
		{
			e.printStackTrace();
			LOGIN_URL = null;
			BASE_URL = null;
			TOP_URL = null;
		}
	}

	private WebClient webClient = new WebClient();
	private HtmlPage currentPage;

	public OtayoriPhotoPanel( String account, String password ) throws Exception
	{
		HtmlPage loginPage = webClient.getPage( LOGIN_URL );
		HtmlForm loginForm = loginPage.getFormByName( "form" );

		HtmlTextInput accountField		= loginForm.getInputByName( "memberIDField" );
		HtmlPasswordInput passwordField	= loginForm.getInputByName( "passwordField" );

		HtmlImageInput submitImageButton = loginForm.getInputByName( "executeLoginSubmit" );

		accountField.setValueAttribute( account );
		passwordField.setValueAttribute( password );

		submitImageButton.click();
		this.currentPage = webClient.getPage( TOP_URL );

		if( this.currentPage == null )
		{
			throw new Exception();
		}
	}

	public void logout() throws Exception
	{
		webClient.closeAllWindows();
	}

	// TODO 最後に見ていたPhotoPanelが取得できない。
	public List<PhotoPanel> getPhotoPanelList()
	{
		ArrayList<PhotoPanel> list = new ArrayList<PhotoPanel>();

		DomNodeList<DomElement> nodeList = this.currentPage.getElementsByTagName( "a" );
		for( DomElement domElement : nodeList )
		{
			String urlString = domElement.getAttribute( "href" );
			if( urlString.startsWith( "Top.htm?frameID=" ) )
			{
				String name = domElement.asText();

				String panelID = "none";
				{
					// FIXME 正しくパースするようにしたい。
					System.out.println( urlString );
					String[] tmp1 = urlString.split( "&" );
					String[] tmp2 = tmp1[ 0 ].split( "=" );
					panelID = tmp2[ 1 ];
				}

				list.add( new PhotoPanel( panelID, name ) );
			}
		}

		return( list );
	}

	public List<UploadData> getUploadDataList( PhotoPanel photoPanel, int pageNum ) throws Exception
	{
		ArrayList<UploadData> list = new ArrayList<UploadData>();
		ArrayList<URL> detailURLList = new ArrayList<URL>();

		URL accessURL = new URL( TOP_URL + "?frameID=" + photoPanel.panelID + "&page=" + pageNum );
		HtmlPage listPage = webClient.getPage( accessURL );

		DomNodeList<DomElement> nodeList = listPage.getElementsByTagName( "a" );
		for( DomElement domElement : nodeList )
		{
			String urlString = domElement.getAttribute( "href" );
			if( urlString.startsWith( "EditPhotographDetail.html?frameID=" ) )
			{
				detailURLList.add( new URL( BASE_URL + urlString ) );
			}
		}

		int count = 0;
		for( URL detailURL : detailURLList )
		{
			System.out.println( count++ );
			try{ Thread.sleep( 1000 ); } catch( Exception e ){}

			HtmlPage detailPage = webClient.getPage( detailURL );
			HtmlImageInput downloadImageButton = detailPage.getBody().getElementById( "form_downloadButton" );

			Page download = downloadImageButton.click();

			ByteArrayOutputStream bout = new ByteArrayOutputStream();
			
			InputStream in = download.getWebResponse().getContentAsStream();
			copy( in, bout, 1024 );
			bout.flush();
			byte[] data = bout.toByteArray();
			bout.close();
			in.close();

			String title = "";
			String comment = "";
			String date = "";

			list.add( new UploadData( 0, data, title, comment, date ) );
		}

		return( list );
	}

	private void copy( InputStream in, OutputStream os, int bufferSize ) throws Exception
	{
		int len = -1;
		byte[] b = new byte[bufferSize * 1024];
		try
		{
			while ((len = in.read(b, 0, b.length)) != -1) {
				os.write(b, 0, len);
			}
			os.flush();
		}
		finally
		{
			if (in != null)
			{
				try
				{
					in.close();
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
			}
			if (os != null)
			{
				try
				{
					os.close();
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		}
	}
}
