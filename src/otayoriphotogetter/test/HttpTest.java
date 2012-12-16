package otayoriphotogetter.test;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.xpath.operations.String;

import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlImageInput;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlPasswordInput;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;

public class HttpTest
{
	public static void main( String[] args ) throws Exception
	{
		WebClient webClient = new WebClient();

		HtmlPage loginPage = webClient.getPage( "https://otayori-docomo.com" );
		HtmlForm loginForm = loginPage.getFormByName( "form" );

		HtmlTextInput accountField		= loginForm.getInputByName( "memberIDField" );
		HtmlPasswordInput passwordField	= loginForm.getInputByName( "passwordField" );

		HtmlImageInput submitImageButton = loginForm.getInputByName( "executeLoginSubmit" );

		accountField.setValueAttribute( "" );
		passwordField.setValueAttribute( "" );

		Page mainPage = submitImageButton.click();

		HtmlPage samplePage = webClient.getPage( "https://otayori-docomo.com/EditPhotographDetail.html?frameID=518&contentID=20121215_0000001209" );
		HtmlImageInput downloadImageButton = samplePage.getBody().getElementById( "form_downloadButton" );

		Page download = downloadImageButton.click();

		FileOutputStream fout = new FileOutputStream( "./test" );
		
		InputStream in = download.getWebResponse().getContentAsStream();
		copy( in, fout, 1024 );
		fout.flush();
		fout.close();
		in.close();

		webClient.closeAllWindows();
	}

	private static void copy( InputStream in, OutputStream os, int bufferSize ) throws Exception
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
