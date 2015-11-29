
public class FlexHash {

	private int entries; //Number of elements in table.
	private int capacity; //Maximum size of table.
	private String [] table; 
	private double threshold; //Limit of table maximum is 1 unless we do separate chaining.
	private double loadFactor; //Number of elements / Capacity.
	private char collisionType; 
	private int colfreq = 0; //Number of collisions.
	private int max = 0; //Maximum number of collisions.
	private char emptyMarkerScheme;

	private int rehashfactorInt;
	private double rehashfactorReal;

	//Does quadratic
	private int numColCell = 0;


	public FlexHash(){

		emptyMarkerScheme = 'A';
		collisionType = 'D';
		entries = 0;
		capacity = 101;
		setRehashThreshold(0.6);
		setRehashFactor(1.2);
		table = new String[capacity];
	}

	public FlexHash(int capacity, char markerScheme, char collisionType){


		setEmptyMarkerScheme(markerScheme);
		this.collisionType = collisionType;
		setRehashThreshold(0.6);
		setRehashFactor(1.2);
		this.capacity = capacity;
		table = new String[capacity];

	}

	/**
	 * @param key to remove
	 * @return the associated value of the key or null.
	 */
	public String remove(String key){

		int code = hashfun(key); //Hash code

		//If something is currently at the bucket.
		if(table[code] != null){

			//If the specific key is actually in the bucket.
			if(table[code].equals(key)){
				String val = table[code];

				if(emptyMarkerScheme == 'A')
					table[code] = "AVAILABLE";

				if(emptyMarkerScheme == 'N')
					table[code] = "- " + key;

				System.out.println("removed  " + val + " at " + code);
				--entries;
				loadFactor = (double)entries/capacity;
				return val;
			}

			//The bucket holds some other key at this point.
			System.out.println("Could not remove, wrong key");
			System.out.println(table[code]);
			return null; 
		}

		//No key at current bucket so key never existed
		System.out.println("Doesnt exist");
		return null;

	}


	public void put(String key, String value){

		int tempMax = 0;

		//Generating hash code.
		int code = hashfun(key);


		//If current spot is empty.
		if(table[code] == null || table[code].equals("AVAILABLE") || table[code].equals("- " + key)){

			//Put value at the key.
			table[code] = value;
			++entries; //Increase by 1 entry.

			//After adding we must verify if threshold has not been exceeded.
			loadFactor = (double)entries/capacity;
			if(loadFactor >= threshold){  //If Exceeded, rehash.
				rehash();
			}
		}

		//A collision has occurred at this point.
		else if (collisionType == 'Q'){

			int factor = 1;
			colfreq++; //Number of collisions happening.
			while(table[code] != null){
				tempMax++; //Counting number of collisions at this current bucket/cell.
				numColCell++;
				//Handling the collision by quadratic hashing.
				if(table[quadratic(code,factor)] == null){

					table[quadratic(code,factor)] = value;
					++entries;

					//After adding we must verify if threshold has not been exceeded.
					loadFactor = (double)entries/capacity;	

					if(loadFactor >= threshold){
						//System.out.println("rehasing2");
						rehash();
					}
					break;
				}
				factor++;
			} //End of while

			//Updating the current maximum number of collisions at a bucket.
			if(tempMax > max)
				max = tempMax;
		}

		else if(collisionType == 'D'){

			int factor = 1;
			colfreq++;
			boolean inserted = false;

			while(!inserted){
				tempMax++;
				numColCell++;

				if(table[doublehash(code,factor)] == null){
					System.out.println("Inside");
					table[doublehash(code,factor)] = value;
					++entries;
					inserted = true;

					loadFactor = (double)entries/capacity;
					if(loadFactor >= threshold)
						rehash();
				}
				factor++;
			}

			if(tempMax > max)
				max = tempMax;
		}
	}

	//Quadratic Collision handling.
	public int quadratic(int code, int factor){
		return (code + factor*factor) % capacity;
	}

	public int doublehash(int code, int factor){

		int doublehashpart = 7 - (code%7);
		System.out.println("loop");
		return (code + factor*doublehashpart)%capacity;

	}

	//Return number of collisions across entire table.
	public int colfreq(){
		return colfreq;
	}

	//Return max number of collisions at a single bucket.
	public int max(){
		return max;
	}

	//Ignore this method for now.
	public String get(String key){

		int factor = 1;
		boolean found = false;
		int code = hashfun(key);

		//If space is empty
		if(table[code] == null || table[code] == "AVAILABLE"){
			return null;
		}
		//If its where we think it is.
		if(table[code].equals( key))
			return table[code];

		if(table[code].equals("- " + key)){
			System.out.println("The key was removed from the table!");
			return null;
		}

		//If not we have to search possible collisions.
		while(!found){

			int collisionKey;

			if(collisionType == 'Q')
				collisionKey = quadratic(code,factor);
			else
				collisionKey = 2; //Change to double

			if(table[collisionKey] == null){
				System.out.println("Word does not exist in table");
				return null;
			}

			if(table[collisionKey].equals(key)){
				//System.out.println("Found through collision");
				return table[collisionKey];
			}

			factor++;
		}
		return null;
	}

	
	/**
	 * Rehashing is done when it is time to increase the size of the table. 
	 */
	public void rehash(){

		resetHashTableStatistics();
		int oldCap = this.capacity; 

		double newCapRaw;
		int newCap;

		if(rehashfactorInt == 0){
			newCapRaw = oldCap * rehashfactorReal;
			newCap = (int)newCapRaw;
		}

		else{
			newCap = rehashfactorInt + oldCap;
		}


		this.capacity = newCap; //Setting new Capacity

		String [] oldTable = new String[oldCap];

		//Storing a deep copy of old table.
		for(int i=0; i<oldCap;i++){
			if(table[i] != null)
				oldTable[i] = table[i];
		}

		//Setting size of new table.
		table = new String[newCap];

		//Hashing old table into new table.
		for(int i=0; i<oldCap;i++){

			if(oldTable[i] != null)
				this.put(oldTable[i],oldTable[i]);
		}


	}

	/**
	 * Mutator method to adjust the threshold of the table.
	 * @param loadFactor new given threshold.
	 */
	public void setRehashThreshold(double loadFactor){
		this.threshold = loadFactor;
	}

	/**
	 * Mutator method that determines the extension of the table.
	 * @param factorOrNumber Table will be extended by adding this number to old capacity.
	 */
	public void setRehashFactor(int factorOrNumber){

		rehashfactorInt = factorOrNumber;
		rehashfactorReal = 0; //Setting to 0 because we are dealing with integers and not decimals.
	}

	/**
	 * Mutator method that determines the extension of the table.
	 * @param factorOrNumber Extension of the table will be a factor of this number.
	 */
	public void setRehashFactor(double factorOrNumber){

		rehashfactorReal = factorOrNumber;
		rehashfactorInt = 0; //Setting to 0 because we are dealing with decimals and not integers.
	}

	/**
	 * Sets the empty marking scheme for the table.
	 * @param type a character to be used as the marker scheme.
	 */
	public void setEmptyMarkerScheme(char type){

		if(type == 'A' || type == 'N' || type == 'R'){
			emptyMarkerScheme = type;
			return;
		}
		
		//At this point the entered marker scheme is invalid.
		System.out.println("Incorrect marker scheme. Exiting program");
		System.exit(0);
	}

	/**
	 * @return the number of elements in the table.
	 */
	public int entries(){
		return entries;
	}

	/**
	 * @return the total capacity of the table.
	 */
	public int size(){
		return capacity;
	}

	/**
	 * Prints current hash table information.
	 */
	public void printHashtableStatistics(){
		System.out.println("----------Statistics------------");
		System.out.println("Rehash Threshold: " + threshold);
		System.out.print("Rehash Factor: "); 
		if(rehashfactorInt == 0) 
			System.out.println(rehashfactorReal*100 + "%");
		else System.out.println(rehashfactorInt);
		System.out.println("Collision Handling Type: " + collisionType);
		System.out.println("Empty Marker Scheme Type: " + emptyMarkerScheme);
		System.out.println();
		System.out.println("Size of Table: " + capacity);
		System.out.println("Number of Elements: " + entries);
		System.out.println("Load Percentage: " + loadFactor);
		System.out.println("Total Collisions: " + colfreq);
		System.out.println("Max: " + max);
		System.out.println("Average: " + (double)numColCell / colfreq);
		System.out.println();

	}

	/**
	 * Resets the hash table statistics.
	 */
	public void resetHashTableStatistics(){
		numColCell = 0;
		colfreq = 0; //Resetting collision frequency since this is new table.
		entries = 0; //Resetting Entries
	}


	/**
	 * @param key a String to be be used as a key.
	 * @return an integer representing the hash value of the key.
	 */
	private int hash(String key){

		int hash = 8;
		for (int i = 0; i < key.length(); i++) {
			hash = hash*31 + key.charAt(i);
		}
		return hash;
	}


	/**
	 * Compressing the hash value between [0, Capacity-1]
	 * @param hash the hash value generated by the hash function.
	 * @return the hash code.
	 */
	private int compress(int hash){

		int val = hash;
		if(val<0){
			val = -hash;
		}

		return val%capacity;
	}


	/**
	 * Hash Function generating the hash code based off a String key.
	 * @param key a String
	 * @return the hash code based off a hash followed by a compression.
	 */
	public int hashfun(String key){
		return compress(hash(key));
	}
}
