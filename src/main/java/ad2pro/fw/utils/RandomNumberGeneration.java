package ad2pro.fw.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

/**
 * This class helps to generate random number based on the user needs
 */
public class RandomNumberGeneration {
	
	public int  getRandomNuber()
	{
		  Random ran=new Random();
		  int num=ran.nextInt(9999999);
		  return num;
	}
	
	public String getToDayDate()
	{
		
		
		SimpleDateFormat formatToday = new SimpleDateFormat("MMddyy");
		String date=formatToday.format(new Date());
		System.out.println("To Day Date ::"+date);
		
		return date;
		
	}
	
	public String getDateAndTime()
	{
		SimpleDateFormat scrShot = new SimpleDateFormat("MMddyyHHmmss");
		String dateandtime = scrShot.format(new Date());
		System.out.println("Date And Time ::"+dateandtime);
		return  dateandtime;
		
	}
	
	
	public static void main(String[] args) {
		
		RandomNumberGeneration ran=new RandomNumberGeneration();
		ran.getDateAndTime();
	}

	public int getRandomNumber(int val) {   
		Random rand = new Random(); 
		int value = rand.nextInt(val)+2;
		return value;
    }
	
	public String getRandomNumberAsString(int size) {   
		   String chars = "0123456789";
		   String num="";
		   int length = chars.length();
		   for (int i = 0; i < size; i ++){
		     num += chars.split("")[ (int) (Math.random() * (length - 1)) ];
		   }
		   return num;
    }
	
	public String getRandomText(int size){
	    String chars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
	    String ret = "";
	    int length = chars.length();
	    for (int i = 0; i < size; i ++){
	        ret += chars.split("")[ (int) (Math.random() * (length - 1)) ];
	    }
	    return ret;
	}
	
	
	public int getRandomNoInRange(int max, int min) {
		Random random = new Random();
		int rn = random.nextInt(max-min+1)+min;
		return rn;
		
	}
	
	public boolean getRandomBol() {
		boolean[] array = {true, false};
	    int rnd = new Random().nextInt(array.length);
	    return array[rnd];
	}

	public String getRandomAlphaNum(int size) {
		   String chars = "0123456789";
		   String num="";
		   int length = chars.length();
		   for (int i = 0; i < size; i ++){
		     num += chars.split("")[ (int) (Math.random() * (length - 1)) ];
		   }
		   return "AutoTest_"+num;
	}

}
