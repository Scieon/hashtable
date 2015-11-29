
public class FlexHash {

	private int entries;
	private int capacity;
	private String [] table;
	private double threshold;
	private double loadFactor;
	private char collisionType;
	private int colfreq = 0;
	private int max = 0;

	public int colfreq(){
		return colfreq;
	}
	public FlexHash(){
		entries = 0;
		capacity = 101;
		loadFactor = 0.6;
		table = new String[capacity];
	}

	public void put(String key, String value){

		int code = hashfun(key);

		if(table[code] == null){
			//System.out.println("good");
			table[code] = value;
			++entries;
			//System.out.println(entries);
			threshold = (double)entries/capacity;
			if(threshold >= loadFactor){
				
			//	System.out.println("rehasing1");
				rehash();
			}
			return;
		}

		else{
			
			int factor = 1;
			//System.out.println("collide");
			colfreq++;
			while(table[code] != null){
		
				if(table[quadratic(code,factor)] == null){
				
					table[quadratic(code,factor)] = value;
					++entries;

					threshold = (double)entries/capacity;	
					
					if(threshold >= loadFactor){
						//System.out.println("rehasing2");
						rehash();
			
						
					}
					break;
				
				}
				factor++;
			} //End of while
		}

	}

	public int quadratic(int code, int factor){

		return (code + factor*factor) % capacity;
	}

	public String get(String key){

		int code = hashfun(key);
		return table[code];
	}

	public void rehash(){
	
		colfreq = 0;
		entries = 0; //Resetting Entries
		int oldCap = this.capacity;
		int newCap = 2*oldCap + 1;
		this.capacity = newCap;

		String [] oldTable = new String[oldCap];

		for(int i=0; i<oldCap;i++){
			if(table[i] != null)
				oldTable[i] = table[i];
		}

		table = new String[newCap];

		for(int i=0; i<oldCap;i++){

			if(oldTable[i] != null)
				this.put(oldTable[i],oldTable[i]);

		}


	}

	public int entries(){
		return entries;
	}
	public int size(){
		return capacity;
	}
	
	public void printHashtableStatistics(){
		System.out.println("----------------------");
		System.out.println("Size of Table: " + capacity);
		System.out.println("Number of Elements: " + entries);
		System.out.println("Load Percentage: " + loadFactor);
		System.out.println("Total Collisions: " + colfreq);
		System.out.println("Max: ?");
		System.out.println("Average: ?");
		
	}
	public int hash(String key){

		int hash = 7;
		for (int i = 0; i < key.length(); i++) {
			hash = hash*31 + key.charAt(i);
		}

		return hash;
	}

	public int compress(int hash){

		int val = hash;
		if(val<0){
			val = -hash;
		}

		return val%capacity;
	}

	public int hashfun(String key){
		return compress(hash(key));
	}
}
