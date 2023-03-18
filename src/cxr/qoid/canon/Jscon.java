package cxr.qoid.canon;

import java.util.Iterator;
import java.util.Map;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonBuilderFactory;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonValue;

import cxr.qoid.AbstractProperty;
import cxr.qoid.Index;

public class Jscon {

	public static class JsconThing extends AbstractProperty<String, JsonArray> {
		public JsconThing(Thing t) {
			super((String) t.tag());
			JsonBuilderFactory g = Json.createBuilderFactory((Map) null);
			JsonArrayBuilder key = g.createArrayBuilder();
			JsonArrayBuilder val = g.createArrayBuilder();
			Iterator var6 = t.iterator();

			while (var6.hasNext()) {
				Property p = (Property) var6.next();
				key.add((String) p.tag());
				val.add((String) p.val());
			}

			this.setVal(g.createArrayBuilder().add(key).add(val).build());
		}
	}

	public static class JsconBill extends Index<String, JsconThing> {
		public JsconBill(Bill b) {
			super((String) b.tag());
			Iterator var3 = b.iterator();
	
			while (var3.hasNext()) {
				Thing t = (Thing) var3.next();
				this.add(new JsconThing(t));
			}
	
		}
	
		public boolean getAllowsChildren() {
			return false;
		}
	
		public boolean isLeaf() {
			return false;
		}
	
		public JsonObject pack() {
			JsonObjectBuilder job = Json.createBuilderFactory((Map) null).createObjectBuilder();
			Iterator var3 = this.iterator();
	
			while (var3.hasNext()) {
				JsconThing c = (JsconThing) var3.next();
				job.add((String) c.tag(), (JsonValue) c.val());
			}
	
			return job.build();
		}
	}
}
