package cxr.qoid.tests;

import java.io.IOException;

import cxr.qoid.Bill;
import cxr.qoid.Property;
import cxr.qoid.Qoid;
import cxr.qoid.Register;

public class QoidTest {
	
	public static String dir = System.getProperty("user.dir");

	public static void example_qoid() {
		Qoid q = new Qoid("test");
		q.add(new Property("tag", "with val"));
		q.add(new Property("tag without val"));
		
		System.out.println(q.toString());
	}
	
	public static void example_bill() {

		Qoid q = new Qoid("test");
		q.add(new Property("tag", "with val"));
		q.add(new Property("tag without val"));
		
		Bill b = new Bill("Bill Test");
		
		b.add(q);
		b.add(q);
		
		System.out.println(b.toString());
	}
	
	public static void example_register() {
		Qoid q = new Qoid("test");
		q.add(new Property("tag", "with val"));
		q.add(new Property("tag without val"));
		
		Bill b = new Bill("Bill Test");
		
		b.add(q);
		b.add(q);
		
		Register r = new Register("Register Test");
		Register r2 = new Register("Subregister test");
		
		r.add(b);
		r.add(b);
		
		r2.add(b);
		r.add(r2);
		
		System.out.println(r.toString());
	}
	
	public static void parse_test() {

		Qoid q = new Qoid("test");
		q.add(new Property("tag", "with val"));
		q.add(new Property("tag without val"));
		
		Bill b = new Bill("Bill Test");
		
		b.add(q);
		b.add(q);
		
		Bill b2 = Bill.parse("Bill Parse Test", b.toString());
		
		System.out.println(b2.toString());
	}
	
	public static void bill_open_test() throws IOException {
		Bill b = Bill.open(dir + "/test.cxr");
		
		System.out.println(b.toString());
	}
	
	public static void register_open_test() throws IOException {
		Register r = Register.open(dir + "/example.cxr");
		
		System.out.println(r.toString());
	}
	
	public static void register_open_and_save_test() throws IOException {
		Register r = Register.open(dir + "/example.cxr");
		
		System.out.println(r.toString());
		
		r.save(dir + "/example2.cxr");
	}
	
	public static void get_test() throws IOException {
		
		Register r = Register.open(dir + "/example.cxr");
		
		Register search0 = r.getAll("examplebill");
		
		System.out.println(search0.toString());
		
		Bill search1 = ((Bill) r.get("examplebill")).getAll("Qoid2");
		
		System.out.println(search1.toString());
	}
	
	public static void get_exclusive_test() throws IOException {
		Register r = Register.open(dir + "/example.cxr");
		
		Bill b = r.getBill("nestedregister");
		
		Register r2 = r.getRegister("nestedregister");
		
		System.out.println(b.toString());
		
		System.out.println(r2.toString());
	}
	
	public static void main(String[] args) throws IOException {
		get_exclusive_test();
	}
	
}
