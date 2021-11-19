# Qoid.java

Qoid is a simple markup language which uses tag-value pairs to record data in traditional files and folders with the added depth of tagged elements within each file.

All objects in Qoid have string tags, with values which add recursive depth. Start with a Property, which is a tag-value pair. Then a Qoid tags a list of Properties, a Bill tags a list of Qoids, and a Register tags a list of Bills and Registers. An object in Qoid.java can be created very quickly, then viewed or saved in its Qoid representation.

### Some basics

We can quickly build Bills in Java:

```java
// Create a bill
Bill b = new Bill("Sample bill");

// Create a Qoid and give it some properties
Qoid q = new Qoid("Qoid0");
q.add(new Property("tag", "val"));
q.add(new Property("tag2", "val2"));

// Add the Qoid to the Bill
b.add(q);

// Display the bill's contents in Qoid markup
System.out.println(b.toString());
```

```
/ Sample bill

#Qoid0
tag: val
tag2: val2
```

Or, if you have a file written in Qoid markup with the `.cxr` extension, it can be quickly accessed:
```java
Bill b = Bill.open("/path/to/file.cxr");
```

The tag of b will be assigned automatically based on the file name, so it would be `file` in this example. This can easily be changed with `b.setTag("new tag")`.

We can also construct folders of Qoid objects, called Registers. A Register can contain both Bills and other Registers. Any folder ending with `.cxr` can be opened just like a Bill:

```java
Register r = Register.open("/path/to/folder.cxr");
```

To save either a Bill or Register, use the `.save(filepath)` method:

```java
Bill b = Bill.open("/path/to/file.cxr");
b.save("new/path/to/file.cxr");

Register r = Register.open("/path/to/folder.cxr");
r.save("new/path/to/folder.cxr");
```

### Getting and setting

Using the example Register `example.cxr`, let's go through the process of changing a specific Property.

```java
Register r = Register.open("example.cxr");

// Get the nested Register from example.cxr
Register r2 = r.getRegister("nestedregister");

// Get the Bill from example.cxr/nestedregister.cxr
Bill b = r2.getBill("nestedbill");

// Get the Qoid in b
Qoid q = b.get("Qoid1");

// Get the specific Property in q
Property p = q.get("tag2");

// Set the Property's val to a new String
p.setVal("new val1");
```

The traversal can be chained together like so:

```java
Register r = Register.open("example.cxr");

// Do it all at once
r.getRegister("nestedregister").getBill("nestedbill").get("Qoid1").get("tag2").setVal("new val1");
```

In general, if you have a specific property that you plan to modify frequently, it should be grabbed in this way. Modifying the grabbed Property will directly modify the Bill it's in, no matter how many time it's changed (as long as it's only changed with `setTag()` and `setVal()`).


