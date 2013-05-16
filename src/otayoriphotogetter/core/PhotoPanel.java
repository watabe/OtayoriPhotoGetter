package otayoriphotogetter.core;

public class PhotoPanel
{
	public final String panelID; // equal frameID
	public final String name;

	public PhotoPanel( String panelID, String name )
	{
		this.panelID = panelID;
		this.name = name;
	}

	public String toString()
	{
		return( "panelID=" + panelID + ", naem=" + name );
	}
}
