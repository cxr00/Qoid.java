package cxr.qoid.tests;

import java.io.IOException;

import cxr.qoid.Bill;
import cxr.qoid.Property;
import cxr.qoid.Qoid;
import cxr.qoid.Register;

public class QoidTest {

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
		String dir = System.getProperty("user.dir");
		Bill b = Bill.open(dir + "/test.cxr");
		
		System.out.println(b.toString());
	}
	
	public static void register_open_test() throws IOException {
		String dir = System.getProperty("user.dir");
		Register r = Register.open("example", dir + "/example.cxr");
		
		System.out.println(r.toString());
	}
	
	public static void main(String[] args) throws IOException {
		bill_open_test();
	}
	
}
