package cxr.qoid.canon.raw;

import cxr.qoid.Qoid;
import cxr.qoid.canon.string.StringIndex;

import java.util.ArrayList;

public abstract class RawIndex<V extends Qoid<?, ?>> extends StringIndex<V> {
	public String path;

	public RawIndex(String path, String tag) {
		super(tag);
		this.path = path;
	}

	public RawIndex(String path, String tag, ArrayList<V> val) {
		super(tag, val);
		this.path = path;
	}

	public String getFilePath() {
		return this.path;
	}

	public void setFilePath(String path) {
		this.path = path;
	}

	public abstract String alias();
}