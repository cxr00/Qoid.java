package cxr.qoid.base;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/**
 * An Index is a Qoid whose value is an ArrayList of some type.
 * All overridden methods from List are done on val instead.
 * 
 * @param <V> the type of the Index's ArrayList
 */
public class Index<V extends Qoid<?>> extends Qoid<ArrayList<V>> implements List<V> {

	public Index(String tag) {
		super(tag);
		val = new ArrayList<V>();
	}
	
	public Index(String tag, ArrayList<V> val) {
		super(tag, val);
	}

	@Override
	public int size() { return val.size(); }

	@Override public boolean isEmpty() { return val.isEmpty(); }
	@Override public boolean contains(Object o) { return val.contains(o); }
	@Override public boolean containsAll(Collection<?> c) { return val.containsAll(c); }

	@Override public Iterator<V> iterator() { return val.iterator(); }
	
	@Override public Object[] toArray() { return val.toArray(); }
	@Override public <T> T[] toArray(T[] a) { return val.toArray(a); }

	@Override public boolean add(V e) { return val.add(e); }
	@Override public boolean addAll(Collection<? extends V> c) { return val.addAll(c); }
	@Override public boolean addAll(int index, Collection<? extends V> c) { return val.addAll(index, c); }
	
	@Override public boolean remove(Object o) { return val.remove(o); }
	@Override public boolean removeAll(Collection<?> c) { return val.removeAll(c); }

	@Override public boolean retainAll(Collection<?> c) { return val.retainAll(c); }

	@Override public void clear() { val.clear(); }

	@Override public V get(int index) { return val.get(index); }
	
	public V get(String tag) {
		// Returns the first instance of an element with the given tag
		for (V v : this) {
			if(v.tag().equals(tag)) {
				return v;
			}
		}
		return null;
	}

	@Override public V set(int index, V element) { return val.set(index, element); }

	@Override public void add(int index, V element) { val.add(index, element); }

	@Override public V remove(int index) { return val.remove(index); }

	@Override public int indexOf(Object o) { return val.indexOf(o); }

	@Override public int lastIndexOf(Object o) { return val.lastIndexOf(o); }

	@Override public ListIterator<V> listIterator() { return val.listIterator(); }

	@Override public ListIterator<V> listIterator(int index) { return val.listIterator(index); }

	@Override public List<V> subList(int fromIndex, int toIndex) { return val.subList(fromIndex, toIndex); }

	

}
