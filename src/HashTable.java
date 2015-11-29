import java.util.Random;


public class HashTable {

	private int entries;
	private int capacity;
	private String [] table;
	private double threshold;
	private double loadFactor;
	private char collisionType;
	private int col = 0;




	public HashTable(char type){
		capacity = 101;
		loadFactor = 0.6;
		table = new String[capacity];
		setCollisionHandling(type);
	}

	public HashTable(int size, double loadFactor, char type){
		this.capacity = size;
		this.loadFactor = loadFactor;
		this.setCollisionHandling(type);
	}

	//Copy Constructor
	public HashTable (HashTable h1){

		int oldCap = h1.capacity;
		int newCap = 2*oldCap + 1;
		this.table = new String[newCap];
		this.entries = h1.entries;
		//threshold
		//loadfactor
		this.collisionType = h1.collisionType;

		for(int i=0; i<h1.capacity;i++){
			this.table[i] = h1.table[i];
		}
	}

	public void rehash(){

		int oldCap = this.capacity;
		int newCap = 2*oldCap + 1;


		String [] newTable = new String[newCap];

		for(int i=0; i<oldCap;i++){
			newTable[i] = table[i];
		}

		this.capacity = newCap;
		this.table = newTable;
	}

	/**
	 * @return number of entries in the hash table.
	 */
	public int size(){
		return entries;
	}

	public int capacity(){
		return capacity;
	}

	public boolean isEmpty(){
		return entries == 0;
	}



	public void put(String key, String value){

		int factor = 0;
		int hashcode = hashfunction(key);

		if(table[hashcode] == null){ //If its empty than put and break.
			table[hashcode] = value;
			this.threshold = (double)entries/capacity;
			++entries;
			System.out.println("Good " + entries);

			if(setRehashThreshold(this.threshold)){
				System.out.println("rehashing");
				rehash();
			}
			return;
		}

		//Quadratic solve
		if(collisionType == 'Q'){
			while(table[hashcode] != null){
				hashcode = (hashcode + factor*factor) % capacity;
				++factor;

				if(table[hashcode] == null){ //If its empty than put and break.
					table[hashcode] = value;
					this.threshold = (double)entries/capacity;
					++entries;
					System.out.println("Collide " + entries);



					if(setRehashThreshold(this.threshold)){
						System.out.println("rehashing");
						rehash();
					}
					col++;
					return;
				}
			}


		}


	}


	//-- Assignment Methods


	public void getCol(){
		System.out.println(col);
	}

	public boolean setRehashThreshold(double loadFactor){

		//System.out.println(threshold);
		//System.out.println(loadFactor);
		if(threshold >= this.loadFactor)
			return true;

		return false;
	}
	public void setRehashFactor(int factorOrNumber){

	}
	public void setCollisionHandling(char type){

		if(type == 'Q' || type == 'q'){
			collisionType = 'Q';
		}

		if(type == 'D' || type == 'd'){
			collisionType = 'D';
		}

		else if(collisionType != 'D' && collisionType != 'Q'){


			System.out.println("Invalid choice of collision handling");
			System.exit(0);
		}
	}




	public int hash(String key){
//		int hash = 7;
//		for (int i = 0; i < key.length(); i++) {
//			hash = hash*1 + key.charAt(i);
//		}
		
		int hash = key.hashCode(); //This is default java hash...
	
		if(hash < 0)
			hash = -hash;
		
		return hash;
	}

	public  int compress(int hashvalue){
		
		Random rand = new Random();
		int randomNum = rand.nextInt(((capacity-1) - 0) + 1) + 0;
		int randomNum2 = rand.nextInt(((capacity-1) - 0) + 1) + 0;
		
		return ((randomNum + hashvalue ) / randomNum2 )%capacity;
	}

	public int hashfunction(String key){

		return compress(hash(key));
	}




}
