package dk.itu.smds.android;

public class Photo {
	
	public Photo() {
	}

	public String Title;
	
	public String Summary;
	
	public String Url;
	
	
	public String ClickUrl;
	
	
	public String RefererUrl;
	
	
	public String FileSize;
	
	
	public String FileFormat;
	
	
	public String Height;
	
	
	public String Width;


	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Photo [Title=").append(Title).append(", Summary=")
				.append(Summary).append(", Url=").append(Url)
				.append(", ClickUrl=").append(ClickUrl).append(", RefererUrl=")
				.append(RefererUrl).append(", FileSize=").append(FileSize)
				.append(", FileFormat=").append(FileFormat).append(", Height=")
				.append(Height).append(", Width=").append(Width).append("]");
		return builder.toString();
	}
	
	
	
	
	
}

