import java.sql.Timestamp;


public class Test {
	static void timeStamp(){
		java.util.Date date= new java.util.Date();
		System.out.println(new Timestamp(date.getTime()));
	}
	public static void main(String[] args) {
		timeStamp();
	}
}
