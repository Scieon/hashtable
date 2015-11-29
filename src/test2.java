import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class test2 {

	public static void main(String[] args) {

		HashTable h1 = new HashTable('Q');
		
		Scanner in = null;
		try{
			in = new Scanner(new FileInputStream("hash_test_file1.txt"));
		}

		catch(FileNotFoundException e){
			System.out.println("Error");
			System.exit(0);
		}
		
		while(in.hasNext()){
			
			String word = in.next();
			h1.put(word,word);
		}
		
		h1.getCol();
		
	}

	

}
