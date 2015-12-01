import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Scanner;


public class Driver {

	public static void main(String[] args) {

		//----------------------------------------------------------------------------
		Scanner in = null;
		try{
			in = new Scanner(new FileInputStream("hash_test_file1.txt"));
		}

		catch(FileNotFoundException e){
			System.out.println("Error");
			System.exit(0);
		}
		//----------------------------------------------------------------------------


		long startTime,endTime,duration;

		startTime = System.nanoTime();

		FlexHash h1 = new FlexHash(101 , 'A',  'Q');
		int count = 0;

		System.out.println("Text File 1 starting with table of size 101");
		
		//Begin by adding in specified intervals of 1000, 3000,5000,10000,50000,100000,150000,200000,~240000
		while(in.hasNext()){
			String word = in.next();

			count++;
			h1.put(word, word);
			if(count == 1000 || count == 3000 || count == 5000 || count == 100000 || count == 50000
					|| count == 100000 || count == 150000 || count == 200000){
				System.out.println("Added first " + count + " Strings.");
				h1.printHashtableStatistics();
			}

		}
		System.out.println("Added all strings in textfile.");
		h1.printHashtableStatistics();
		endTime = System.nanoTime();

		duration = (endTime - startTime)/1000000;
		System.out.println("Time taken to add all strings: " + duration + " miliseconds.\n");


		System.out.println("Enter any key to continue.");
		Scanner kb = new Scanner(System.in);
		kb.next();

		
		//--------------------------------------------------------------------------------------------------
		//Remove first 10 000 strings than perform a full get operation.
		startTime = System.nanoTime();
		try{
			in = new Scanner(new FileInputStream("hash_test_file1.txt"));
		}

		catch(FileNotFoundException e){
			System.out.println("Error");
			System.exit(0);
		}

		count = 0;
		while(in.hasNext()){

			if(count < 10000){
				System.out.println("Removed " + h1.remove(in.next()));

			}
			else
				System.out.println(h1.get(in.next()));
			count++;

		}
		endTime = System.nanoTime();
		duration = (endTime - startTime)/1000000;
		System.out.println("Time to taken to get all strings from table after removing first 10 000 Strings: " + duration + " miliseconds. \n");
		
		//--------------------------------------------------------------------------------------------------
		//Added first 10 000 strings than perform a full get operation.
		startTime = System.nanoTime();
		try{
			in = new Scanner(new FileInputStream("hash_test_file1.txt"));
		}

		catch(FileNotFoundException e){
			System.out.println("Error");
			System.exit(0);
		}
		
		System.out.println("Enter any key to continue.");
		kb.next();
		
		count = 0;
		while(in.hasNext()){

			if(count < 10000){
				String value = in.next();
				h1.put(value,value);

			}
			else
				System.out.println(h1.get(in.next()));
			count++;

		}
		endTime = System.nanoTime();
		duration = (endTime - startTime)/1000000;
		System.out.println("Time to taken to get all strings from table after putting back first 10 000 Strings: " + duration + " miliseconds. \n");
		h1.printHashtableStatistics();
	}

}
