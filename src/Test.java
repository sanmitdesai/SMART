
public class Test {
public static void main(String[] args) {
	String example = "RT @richardbranson: So sad to see violence ruling over democracy in #Ukraine &amp; #Venezuela. We all need to stand up for true democracy http:…'";
	System.out.println(example.replaceAll("RT @.*: ", ""));
}
}
