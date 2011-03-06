package bok.labexercise4.extended;

@ClassAnnotation(name="Book", description="Create a new car")
public class Book extends IItem<Book> {
	/**
	 * 
	 */
	

	@FieldAnnotation(name="ISBN", value="ISBN", isKey=true)
	public String ISBN;
	
	@FieldAnnotation(name="title", value="Title")
	public String title;
	
	@FieldAnnotation(name="year", value="Year released")
	public Integer year;
	
	@FieldAnnotation(name="author", value="Author")
	public String author;
	
	public Book() {
		super();
	}

	public Book(String ISBN, String title, int year, String author) {
		super();
		this.ISBN = ISBN;
		this.title = title;
		this.year = year;
		this.author = author;
	}
	
	public static Book build(Book book, String ISBN, String title, int year, String author) {
		book.ISBN = ISBN;
		book.title = title;
		book.year = year;
		book.author = author;
		return book;
	}
	
	public void Update(Book item) {
		this.ISBN = item.ISBN;
		this.title = item.title;
		this.year = item.year;
		this.author = item.author;
		
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
		return this.ISBN.equals(key.toString());
	}

	
}