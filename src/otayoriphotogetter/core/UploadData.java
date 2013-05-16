package otayoriphotogetter.core;


public class UploadData
{
	public final int type;
	public final byte[] data;
	public final String title;
	public final String comment;
	public final String date;

	public UploadData( int type, byte[] data, String title, String comment, String date )
	{
		this.type = type;
		this.data = data;
		this.title = title;
		this.comment = comment;
		this.date = date;
	}

	public String toString()
	{
		return( "type=" + type + ", data.length=" + data.length + ", title=" + title + ", comment=" + comment + ", date=" + date );
	}
}
