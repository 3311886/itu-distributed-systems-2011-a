package bok.labexercise4.extended;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ServerData<T> implements IDataCollection<IItem<T>> {
	List<IItem<T>> items = new ArrayList<IItem<T>>();
	public ServerData() {
	}

	public Object AddItem(IItem<T> item) throws IOException {
		return items.add(item);
	}

	@SuppressWarnings("unchecked")
	public boolean  Update(Object key, IItem<T> itemNew) throws IOException {
		IItem<T> item = Get(key);
		item.Update((T) itemNew);
		return true;
	}

	public IItem<T> Get(Object key) throws IOException {
		IItem<T> item = null;
		 for (IItem<T> i: items) {
			 if (i.getByKey(key))
				 item = i;
		 }
		 return item;
	}

	public List<IItem<T>> GetAll() throws IOException {
		return  items;
	}

	public boolean Remove(Object key) throws IOException {
		return items.remove(Get(key));
	}

	public void Synchronize(List<?> list) {
		List<IItem<T>> list2= (List<IItem<T>>) list;
		for (IItem<T> item : list2) {
			items.add(item);
		}
	}	
}
