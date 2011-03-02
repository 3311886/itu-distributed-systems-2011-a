package bok.labexercise4.extended;

public class Car extends IItem<Car> {
	private String plateNumber;
	private String model;
	private String brand;
	private int year;
	

	public Car(String plateNumber, String model, String brand, int year) {
		super();
		this.plateNumber = plateNumber;
		this.model = model;
		this.brand = brand;
		this.year = year;
	}

	@Override
	public void Update(Car item) {
		this.plateNumber = item.plateNumber;
		this.model = item.model;
		this.brand = item.brand;
		this.year = item.year;
		
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Car [plateNumber=").append(plateNumber)
				.append(", model=").append(model).append(", brand=")
				.append(brand).append(", year=").append(year).append("]");
		return builder.toString();
	}
	

	@Override
	public int compareTo(Car o) {
		return this.plateNumber.compareTo(o.plateNumber);
	}
@Override 
	public boolean getByKey(Object o) {
		return this.plateNumber.equals(o);
	}
}
