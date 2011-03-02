package bok.labexercise4.extended;

public class Book extends IItem<Book> {
	private String ISBN;
	private String title;
	private int year;
	private String author;  
	
	
	public Book(String ISBN, String title, int year, String author) {
		super();
		this.ISBN = ISBN;
		this.title = title;
		this.year = year;
		this.author = author;
	}

	public void Update(Book item) {
		this.ISBN = item.ISBN;
		this.title = item.title;
		this.year = item.year;
		
	}

	@Override
	public int compareTo(Book o) {
		return this.title.compareTo(o.title);
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Book [ISBN=").append(ISBN).append(", title=")
				.append(title).append(", year=").append(year)
				.append(", author=").append(author).append("]");
		return builder.toString();
	}
	
	@Override 
	public boolean getByKey(Object key) {
		return this.ISBN.equals(key);
	}
}