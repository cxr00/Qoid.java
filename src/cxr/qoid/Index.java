package cxr.qoid;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import javax.swing.tree.TreeNode;

public abstract class Index<T extends Comparable<T>, V extends Qoid<?, ?>> extends Qoid<T, ArrayList<V>>
		implements
			List<V>,
			TreeNode {
	protected TreeNode parent;

	public Index(T tag) {
		super(tag);
		this.val = new ArrayList<V>();
	}

	public Index(T tag, ArrayList<V> val) {
		super(tag, val);
	}

	public boolean add(V x) {
		boolean out = this.val().add(x);
		if (out && x instanceof Index) {
			((Index) x).setParent(this);
		}

		return out;
	}

	public void add(int n, V x) {
		this.val().add(n, x);
		if (this.contains((Object) x) && x instanceof Index) {
			((Index) x).setParent(this);
		}

	}

	public boolean addAll(Collection<? extends V> x) {
		return this.val().addAll(x);
	}

	public boolean addAll(int n, Collection<? extends V> x) {
		return this.val().addAll(n, x);
	}

	public void clear() {
		this.val().clear();
	}

	public boolean contains(Object x) {
		return this.val().contains(x);
	}

	public Enumeration<? extends TreeNode> children() {
		return Collections.enumeration((Collection) this.val());
	}

	public boolean containsAll(Collection<?> x) {
		return this.containsAll(x);
	}

	public V get(int n) {
		return this.val().get(n);
	}

	public TreeNode getChildAt(int n) {
		return (TreeNode) this.get(n);
	}

	public int getChildCount() {
		return this.getAllowsChildren() ? this.size() : 0;
	}

	public int getIndex(TreeNode t) {
		return this.indexOf(t);
	}

	public TreeNode getParent() {
		return this.parent;
	}

	public int indexOf(Object x) {
		return this.val().indexOf(x);
	}

	public boolean isEmpty() {
		return this.val().isEmpty();
	}

	public Iterator<V> iterator() {
		return this.val().iterator();
	}

	public int lastIndexOf(Object x) {
		return this.val().lastIndexOf(x);
	}

	public ListIterator<V> listIterator() {
		return this.val().listIterator();
	}

	public ListIterator<V> listIterator(int n) {
		return this.val().listIterator(n);
	}

	public boolean remove(Object x) {
		return this.val().remove(x);
	}

	public V remove(int n) {
		return this.val().remove(n);
	}

	public boolean removeAll(Collection<?> x) {
		return this.val().removeAll(x);
	}

	public boolean retainAll(Collection<?> x) {
		return this.val().retainAll(x);
	}

	public V set(int n, V x) {
		return this.val().set(n, x);
	}

	public int size() {
		return this.val().size();
	}

	public List<V> subList(int x, int y) {
		return this.val().subList(x, y);
	}

	public Object[] toArray() {
		return this.val().toArray();
	}

	public <N> N[] toArray(N[] x) {
		return this.val().toArray(x);
	}

	public void changeTo(Index<T, V> in) {
		this.tag = in.tag;
		this.val = in.val();
	}

	public boolean contains(T x) {
		Iterator var3 = this.iterator();

		while (var3.hasNext()) {
			V each = (V) var3.next();
			if (each.tag().equals(x)) {
				return true;
			}
		}

		return false;
	}

	public V get(String tag) {
		Iterator var3 = this.iterator();

		while (var3.hasNext()) {
			V each = (V) var3.next();
			if (each.tag().toString().equalsIgnoreCase(tag)) {
				return each;
			}
		}

		return null;
	}

	public TreeNode getParent(int n) {
		if (n < 0) {
			return null;
		} else if (n == 0) {
			return this;
		} else {
			try {
				TreeNode out = this.getParent();

				for (int k = 1; k < n; ++k) {
					out = out.getParent();
				}

				return out;
			} catch (NullPointerException var4) {
				return null;
			}
		}
	}

	public boolean hasParent() {
		return this.parent != null;
	}

	public Index<?, ?> jumpTo(List<String> path) {
		TreeNode p = this.getParent(path.size());
		return p == null ? null : ((Index) p).query(path);
	}

	public Index<?, ?> query(List<String> path) {
		List<String> p = new ArrayList();
		p.addAll(path);
		if (p.size() == 0) {
			return this;
		} else {
			Index out = (Index) this.get((String) p.remove(0));

			try {
				while (out != null && !p.isEmpty()) {
					out = (Index) out.get((String) p.remove(0));
				}

				return out;
			} catch (ClassCastException var5) {
				var5.printStackTrace();
				System.out.println("Returning empty query");
				return null;
			} catch (NullPointerException var6) {
				var6.printStackTrace();
				System.out.println("Returning empty query");
				return null;
			}
		}
	}

	public boolean setParent(TreeNode t) {
		this.parent = t;
		return true;
	}
}