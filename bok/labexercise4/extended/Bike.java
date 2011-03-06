package bok.labexercise4.extended;

public class Bike extends IItem<Bike> {
	@FieldAnnotation(name="serialNumber", value="Serial Number",  isKey=true )
	public String serialNumber;
	@FieldAnnotation(name="brand", value="Brand")
	public String brand;
	@FieldAnnotation(name="model", value="Model")
	public String model;
	@FieldAnnotation(name="year", value="Year")
	public Integer year;

	@Override
	public void Update(Bike itemNew) {
		this.serialNumber = itemNew.serialNumber;
		this.brand = itemNew.brand;
		this.model = itemNew.model;
		this.year = itemNew.year;
		
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Bike [serialNumber=").append(serialNumber)
				.append(", brand=").append(brand).append(", model=")
				.append(model).append(", year=").append(year).append("]");
		return builder.toString();
	}

	@Override
	public int compareTo(Bike o) {
		return this.serialNumber.compareTo(o.serialNumber);
	}

	@Override
	public boolean getByKey(Object key) {
		return this.serialNumber.equals(key);
	}
	

}
