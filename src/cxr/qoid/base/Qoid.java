package cxr.qoid.base;


public class Qoid<V> implements Comparable<Qoid<?>>{
	
	protected String tag;
	protected V val;
	
	public Qoid(String tag) {
		this.tag = tag;
		val = null;
	}
	
	public Qoid(String tag, V val) {
		this.tag = tag;
		this.val = val;
	}
	
	public String tag() { return this.tag; }
	public void setTag(String tag) { this.tag = tag; }
	
	public V val() { return this.val; }
	public void setVal(V val) { this.val = val; }

	@Override
	public int compareTo(Qoid<?> o) { return -o.tag().compareTo(this.tag()); }
	
}
