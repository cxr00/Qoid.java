package cxr.qoid.canon.string;

public abstract class AbstractStringMacro extends AbstractStringProperty {
	public AbstractStringMacro(String tag) {
		super(tag);
	}

	public AbstractStringMacro(String tag, String val) {
		super(tag, val);
	}

	public abstract char delimiter();

	public String cxrString() {
		return this.delimiter() + super.cxrString();
	}
}