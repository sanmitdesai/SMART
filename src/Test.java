import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;




import org.jfree.chart.urls.URLUtilities;
import org.w3c.dom.Text;


public class Test {
public static void main(String[] args) throws UnsupportedEncodingException {
//	String example = "RT @richardbranson: So sad to see violence ruling over democracy in #Ukraine &amp; #Venezuela. We all need to stand up for true democracy http:…'";
//	System.out.println(example.replaceAll("RT @.*: ", ""));
	
	
	
	String input = "OMG this cuiteeeee is so hooootttt funny !!!!!!!!! :-)))";
//	System.out.println(input.replaceAll("(.)\\1{2,}", "$1"));
	
	String data = "President Obama called Wednesday on Congress to extend a tax break for students included in last year's economic stimulus package, arguing that the policy provides more generous assistance.";
	String temp = URLUtilities.encode(data, "UTF-8");
	System.out.println(temp.replace("+", "%20"));
	
}
}
