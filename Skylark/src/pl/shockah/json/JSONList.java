package pl.shockah.json;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

public class JSONList<T> implements List<T> {
	@SafeVarargs
	public static <T> JSONList<T> make(T... os) {
		JSONList<T> j = new JSONList<>();
		j.put(os);
		return j;
	}
	
	protected List<T> list = Collections.synchronizedList(new ArrayList<T>());
	
	@SuppressWarnings("unchecked")
	public JSONList<T> copy() {
		JSONList<T> ret = new JSONList<>();
		for (T t : list) {
			if (t instanceof JSONList<?>)
				ret.list.add((T)((JSONList<?>)t).copy());
			else if (t instanceof JSONObject)
				ret.list.add((T)((JSONObject)t).copy());
			else
				ret.list.add(t);
		}
		return ret;
	}
	
	public boolean isEmpty() {
		return list.isEmpty();
	}
	public int size() {
		return list.size();
	}
	
	public boolean holdsStrings() {
		if (isEmpty())
			return true;
		for (T t : list)
			if (!(t instanceof String))
				return false;
		return true;
	}
	public boolean holdsBooleans() {
		if (isEmpty())
			return true;
		for (T t : list)
			if (!(t instanceof Boolean))
				return false;
		return true;
	}
	public boolean holdsNumbers() {
		if (isEmpty())
			return true;
		for (T t : list)
			if (!(t instanceof Number))
				return false;
		return true;
	}
	public boolean holdsInts() { return holdsNumbers(); }
	public boolean holdsLongs() { return holdsNumbers(); }
	public boolean holdsFloat() { return holdsNumbers(); }
	public boolean holdsDouble() { return holdsNumbers(); }
	public boolean holdsObjects() {
		if (isEmpty())
			return true;
		for (T t : list)
			if (!(t instanceof JSONObject))
				return false;
		return true;
	}
	public boolean holdsLists() {
		if (isEmpty())
			return true;
		for (T t : list)
			if (!(t instanceof JSONList))
				return false;
		return true;
	}
	
	public boolean isNull(int index) {
		return list.get(index) == null;
	}
	public T get(int index) {
		return list.get(index);
	}
	public String getString(int index) {
		Object o = get(index);
		if (o instanceof String)
			return (String)o;
		throw new IllegalArgumentException("Index '" + index + "' doesn't hold a string.");
	}
	public boolean getBoolean(int index) {
		Object o = get(index);
		if (o instanceof Boolean)
			return (Boolean)o;
		throw new IllegalArgumentException("Index '" + index + "' doesn't hold a boolean.");
	}
	public Number getNumber(int index) {
		Object o = get(index);
		if (o instanceof Number)
			return (Number)o;
		throw new IllegalArgumentException("Index '" + index + "' doesn't hold a number.");
	}
	public int getInt(int index) { return getNumber(index).intValue(); }
	public long getLong(int index) { return getNumber(index).longValue(); }
	public float getFloat(int index) { return getNumber(index).floatValue(); }
	public double getDouble(int index) { return getNumber(index).doubleValue(); }
	
	public JSONObject getObject(int index) {
		Object o = get(index);
		if (o instanceof JSONObject)
			return (JSONObject)o;
		throw new IllegalArgumentException("Index '" + index + "' doesn't hold a JSONObject.");
	}
	public JSONList<?> getList(int index) {
		Object o = get(index);
		if (o instanceof JSONList<?>) 
			return (JSONList<?>)o;
		throw new IllegalArgumentException("Index '" + index + "' doesn't hold a JSONList.");
	}
	
	public JSONObject putNewObject() {
		JSONObject j = new JSONObject();
		put(j);
		return j;
	}
	public JSONList<?> putNewList() {
		JSONList<?> j = new JSONList<>();
		put(j);
		return j;
	}
	public void put(Object... os) {
		for (Object o : os)
			putAt(size(), o);
	}
	@SuppressWarnings("unchecked") public void putAt(int index, Object o) {
		if (!(o == null || o instanceof String || o instanceof Number || o instanceof Boolean || o instanceof JSONObject || o instanceof JSONList<?>))
			throw new IllegalArgumentException("Can't store this type of object.");
		if (index >= size())
			list.add((T)o);
		else
			list.add(index, (T)o);
	}
	
	public T remove(int index) {
		return list.remove(index);
	}
	
	@SuppressWarnings("unchecked")
	public JSONList<String> ofStrings() { return (JSONList<String>)this; }
	@SuppressWarnings("unchecked")
	public JSONList<Boolean> ofBooleans() { return (JSONList<Boolean>)this; }
	@SuppressWarnings("unchecked")
	public JSONList<Integer> ofInts() { return (JSONList<Integer>)this; }
	@SuppressWarnings("unchecked")
	public JSONList<Long> ofLongs() { return (JSONList<Long>)this; }
	@SuppressWarnings("unchecked")
	public JSONList<Float> ofFloats() { return (JSONList<Float>)this; }
	@SuppressWarnings("unchecked")
	public JSONList<Double> ofDoubles() { return (JSONList<Double>)this; }
	@SuppressWarnings("unchecked")
	public JSONList<JSONObject> ofObjects() { return (JSONList<JSONObject>)this; }
	@SuppressWarnings("unchecked")
	public JSONList<JSONList<?>> ofLists() { return (JSONList<JSONList<?>>)this; }
	
	public Iterator<T> iterator() {
		return new LinkedList<>(list).iterator();
	}
	
	public boolean add(T t) {
		put(t);
		return true;
	}
	public boolean addAll(Collection<? extends T> c) {
		for (T t : c)
			put(t);
		return true;
	}
	public void clear() {
		list.clear();
	}
	public boolean contains(Object o) {
		return list.contains(o);
	}
	public boolean containsAll(Collection<?> c) {
		return list.containsAll(c);
	}
	public boolean remove(Object o) {
		return list.remove(o);
	}
	public boolean removeAll(Collection<?> c) {
		return list.removeAll(c);
	}
	public boolean retainAll(Collection<?> c) {
		return list.retainAll(c);
	}
	public Object[] toArray() {
		return list.toArray();
	}
	public <R> R[] toArray(R[] ra) {
		return list.toArray(ra);
	}
	
	@SuppressWarnings("unchecked")
	public JSONObject addNewObject() {
		JSONObject j = new JSONObject();
		add((T)j);
		return j;
	}
	@SuppressWarnings("unchecked")
	public JSONList<?> addNewList() {
		JSONList<?> j = new JSONList<>();
		add((T)j);
		return j;
	}
	public void add(int index, T t) {
		putAt(index, t);
	}
	public boolean addAll(int index, Collection<? extends T> c) {
		return list.addAll(index, c);
	}
	public int indexOf(Object o) {
		return list.indexOf(o);
	}
	public int lastIndexOf(Object o) {
		return list.lastIndexOf(o);
	}
	public ListIterator<T> listIterator() {
		return list.listIterator();
	}
	public ListIterator<T> listIterator(int index) {
		return list.listIterator(index);
	}
	public T set(int index, T t) {
		return list.set(index, t);
	}
	public List<T> subList(int fromIndex, int toIndex) {
		return list.subList(fromIndex, toIndex);
	}
}