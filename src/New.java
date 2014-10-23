import java.io.ObjectInputStream.GetField;
import java.util.ArrayList;
import java.util.Collections;


public class New {

	public static String returnEmotion(int num){

		if(num == 1){
			return "Joy";
		}
		else if(num == 2){
			return "Trust";
		}
		else if(num == 3){
			return "Fear";
		}
		else if(num == 4){
			return "Surprise";
		}
		else if(num == 5){
			return "Sadness";
		}
		else if(num == 6){
			return "Disgust";
		}
		else if(num == 7){
			return "Anger";
		}
		else if(num == 8){
			return "Anticipation";
		}
		else{
			return null;
		}
	}
	
	static ArrayList<Integer> getIntegerArray(ArrayList<String> stringArray) {
        ArrayList<Integer> result = new ArrayList<Integer>();
        for(String stringValue : stringArray) {
            try {
                //Convert String to Integer, and store it into integer array list.
                result.add(Integer.parseInt(stringValue));
            } catch(NumberFormatException nfe) {
               System.err.println("Could not parse " + nfe);

            } 
        }       
        return result;
    }
	
	
	public static int getMax(ArrayList<Integer> list){
	    int max = Integer.MIN_VALUE;
	    int pos = 0;
	    for(int i=0; i<list.size(); i++){
	        if(list.get(i) > max){
	            max = list.get(i);
	            pos = i;
	        }
	    }
	    return pos;
	}
	
	public String getEmotionsPriority(ArrayList<String> input){
		String output = "";
		ArrayList<Integer> intInput = getIntegerArray(input);
		int sentiScore = intInput.get(0);
		intInput.set(0, 0);
		
		int emotion1 = getMax(intInput);
		intInput.set(emotion1, 0);
		
		int emotion2 = getMax(intInput);
		intInput.set(emotion2, 0);
		
		int emotion3 = getMax(intInput);
//		intInput.set(emotion3, 0);
		
		output+=sentiScore+",";
		output+=returnEmotion(emotion1)+",";
		output+=returnEmotion(emotion2)+",";
		output+=returnEmotion(emotion3);
		
		
		return output;
	}
	
	public static void main(String[] args) {
		ArrayList<String> output = new ArrayList<String>();
		output.add("-2");
		output.add("5");
		output.add("6");
		output.add("2");
		output.add("5");
		output.add("0");
		output.add("0");
		output.add("5");
		
		New obj = new New();
		System.out.println(output);
		System.out.println(obj.getEmotionsPriority(output));
	}

}
