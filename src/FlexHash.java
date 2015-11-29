
public class FlexHash {

	private int entries; //Number of elements in table.
	private int capacity; //Maximum size of table.
	private String [] table; 
	private double threshold; //Limit of table maximum is 1 unless we do separate chaining.
	private double loadFactor; //Number of elements / Capacity.
	private char collisionType; 
	private int colfreq = 0; //Number of collisions.
	private int max = 0; //Maximum number of collisions.
	
	//Does quadratic
	private int numColCell = 0;

	
	public FlexHash(){
		entries = 0;
		capacity = 101;
		threshold = 0.6;
		table = new String[capacity];
	}

	public void put(String key, String value){

		int tempMax = 0;
		
		//Generating hash code.
		int code = hashfun(key);

		//If current spot was always empty.
		if(table[code] == null){
			
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
		else{
			
			int factor = 1;
			//System.out.println("collide");
			colfreq++; //Number of collisions happening.
			while(table[code] != null){
				tempMax++;
				//Handling the collision by quadratic.
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
			
			if(tempMax > max)
				max = tempMax;
		}

	}

	//Quadratic Collision handling.
	public int quadratic(int code, int factor){
		return (code + factor*factor) % capacity;
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

		int code = hashfun(key);
		return table[code];
	}

	//Rehash is done when it is time to increase the size of the table.
	public void rehash(){
		
		colfreq = 0; //Resetting collision frequency since this is new table.
		entries = 0; //Resetting Entries
		int oldCap = this.capacity; 
		int newCap = 2*oldCap + 1;
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

	//Return number of elements in table.
	public int entries(){
		return entries;
	}
	
	//Return capacity of table.
	public int size(){
		return capacity;
	}
	
	public void printHashtableStatistics(){
		System.out.println("----------------------");
		System.out.println("Size of Table: " + capacity);
		System.out.println("Number of Elements: " + entries);
		System.out.println("Load Percentage: " + threshold);
		System.out.println("Total Collisions: " + colfreq);
		System.out.println("Max: " + max);
		System.out.println("Average: ?");
		
	}
	
	//Generate a hash value based of String key.
	public int hash(String key){

		int hash = 7;
		for (int i = 0; i < key.length(); i++) {
			hash = hash*31 + key.charAt(i);
		}

		return hash;
	}

	//Compress the hash value.
	public int compress(int hash){

		int val = hash;
		if(val<0){
			val = -hash;
		}

		return val%capacity;
	}

	//Hashfunction geenrating hash code based off String key.
	public int hashfun(String key){
		return compress(hash(key));
	}
}
