import java.util.*;
import java.lang.*;
import java.io.*;

public class Main {
	/**The arraylist holding all the sorted Actor objects
	 */
	private static ArrayList<Actor> arr = new ArrayList<>();

	/**Binary searches for a name in the arr arraylist.
	 * @Param theName the name it is searching for
	 * @Return returns an int[] with the first value being where it is or where it should be and the second value being if it was found or not.
	 */
	private static int[] search(String theName) {
		int min = 0;
		int max = arr.size()-1;
		int mid = max/2;
		int[] ret = {0, 0};
		
		while (min < max) { //Goes until min and max converge
			mid = (max - min)/2 + min;
			if (theName.toLowerCase().compareTo(arr.get(mid).getName().toLowerCase()) == 0) { //If it finds it it returns it
				ret[0] = mid;
				ret[1] = 0;
				return ret;
			}
			else if (theName.toLowerCase().compareTo(arr.get(mid).getName().toLowerCase()) < 0) { //If it is less than the midpoint it moves max to the midpoint
				max = mid - 1;
			}
			else { //If it is more than the midpoint it moves min to the midpoint
				min = mid + 1;
			}
		}
		mid = (max - min)/2 + min;
		if (theName.toLowerCase().compareTo(arr.get(mid).getName().toLowerCase()) == 0) { //If it finds it it returns it
			ret[0] = mid;
			ret[1] = 0;
			return ret;
		}
		else if (theName.toLowerCase().compareTo(arr.get(mid).getName().toLowerCase()) < 0) { //If it needs to go before the closest thing it returns that
			ret[0] = mid;
			ret[1] = 1;
			return ret;
		}
		else { //If it needs to go after the closest thing it returns that
			ret[0] = mid+1;
			ret[1] = 1;
			return ret;
		}
	}

	/**Adds a role to an actor. If the actor already exists, it finds it in arr and adds it.
	 * If it doesn't exist it creates a new actor, adds the role, and inserts the new actor alphabetically into arr.
	 * @Param theName the name of the actor the role needs to be added to.
	 * @Param theMovie the name of the movie.
	 * @Param theRole the name of the character or their non acting role.
	 */
	private static void addNew(String theName, String theMovie, String theRole) {
		Role role = new Role(theMovie, theRole); //Creates a new role object
		if (arr.size() < 1) { //If arr is empty it creates a new actor and adds the role to it
			Actor actor = new Actor(theName);
			actor.add(role);
			arr.add(actor); //Adds the actor to the array
		} else {
			int[] search = search(theName); //Searches for the actor
			if (search[1] == 1) { //If it isn't found it creates a new one and adds the role to it
				Actor actor = new Actor(theName);
				actor.add(role);
				arr.add(search[0], actor); //Adds the actor to the array
			} else {
				arr.get(search[0]).add(role); //If it is found it adds the role to the actor it found
			}
		}
	}

	/**Parses the input string into an array with the important data separated into groups.
	 * Separates by comma but not commas within curly or straight brackets.
	 * @Param str the string that will be parsed
	 * @Param which an indicator of what type of string str is since there are two different setups the string could have based on how I coded it.
	 * @Return a String[] containing all of the separated data
	 */
	private static String[] parse(String str, int which) {
		List<String> ret = new ArrayList<>();
		String cur = "";
		int test = 0;
		int track = 0;

		if (which == 1) { //If it is parsing the whole line
			for (char c : str.toCharArray()) {
				if (c == ',' && test == 0 && track == 0) { //Only parses at commas outside of brackets
					ret.add(cur);
					cur = "";
				} else {
					if (ret.size() == 1 && c == '"' && track == 0)
						track++;
					else if (c == '"' && track == 1)
						track--;
					else if (c == '[' && test == 0) {
						test++;
						cur = "";
					} else if (c == '[')
						test++;
					else if (c == ']')
						test--;
					else
						cur += c;
				}
			}
			ret.add(cur);
		} else { //If it is parsing the cast
			for (char c : str.toCharArray()) {
				if (c == ',' && test == 0 && track == 0) { //Only parses at commas outside of brackets
					if (!cur.equals(""))
						ret.add(cur);
					cur = "";
				} else {
					if (c == '{') {
						test++;
						cur = "";
					}
					else if (c == '}')
						test--;
					else
						cur += c;
				}
			}
			if (!cur.equals(""))
				ret.add(cur);
		}
		String[] rett = ret.toArray(new String[ret.size()]);
		return rett;
	}

	/**Takes strings containing the actor and their role and splits them into easily readable strings.
	 * @Param line the string that contains the actor and role
	 * @Return returns a String[] with the name in the first spot and the role in the second
	 */
	private static String[] getNR(String line) {
		String theName;
		String theRole;
		line = line.substring(line.indexOf("\"\"character\"\": \"\"") + 17, line.length());
		theRole = line.substring(0, line.indexOf("\"")); //Splits to find the role
		line = line.substring(line.indexOf("\"\"name\"\": \"\"") + 12, line.length());
		theName = line.substring(0, line.indexOf("\"")); //Splits to find the name
		String[] ret = {theName, theRole};
		return ret;
	}

	/**Reads the file and creates the arraylist from it.
	 * Goes line by line isolating the important data and uses it to call addNew()
	 * @Param theFile the file to read from
	 */
	private static void readFile(String theFile) throws Exception {
		String theName = "";
		String theMovie = "";
		String theRole = "";
		String[] list;
		String[] cast;
		String[] nameRole;
		int o = 0;
		try {
			File f = new File(theFile);
			FileReader fr = new FileReader(f);
			BufferedReader br = new BufferedReader(fr); //Creates a file reader to read each line about the movie and its cast and crew
			String line = br.readLine();
			line = br.readLine();
			System.out.print("Loading."); //Indicates to the user that it is loading and isn't stuck in a loop
			while (line != null) {
				o++;
				if (o % 100 == 0) //Adds dots to show it is continuing to load
					System.out.print(".");
				list = parse(line, 1); //Splits the whole line into the movie_id,title,cast,crew
				cast = parse(list[2], 0); //Splits the whole cast into individuals
				theMovie = list[1]; //Sets the movie name
				for (String i : cast) { //For each cast member
					nameRole = getNR(i); //Gets their name and role from the string
					theName = nameRole[0]; //Sets the name
					theRole = nameRole[1]; //Sets the role
					addNew(theName, theMovie, theRole); //Adds the role to the actor and creates a new actor if they haven't been added yet
				}
				line = br.readLine(); //Goes to the next movie
			}
			fr.close();
			br.close();
		} catch (Exception e) {
			throw e;
		}
	}

	/**Runs the user interraction with the program
	 * Asks for an actor to find and prints the data if it's found.
	 * If it's not found it asks if the user meant someone close to what they typed
	 * Exits when they type EXIT
	 */
	private static void runUser() {
		Scanner sc = new Scanner(System.in);
		String input = "";
		System.out.print("Welcome to the Movie Wall!\nEnter the name of an actor (or \"EXIT\" to quit): ");
		input = sc.nextLine(); //Scans user input
		while (!input.toUpperCase().equals("EXIT")) { //Checks if they exited
			int[] search = search(input); //Searches for the actor
			if (search[1] == 0) { //If the actor is found it prints their data
				arr.get(search[0]).print();
				System.out.print("\nEnter the name of an actor (or \"EXIT\" to quit): ");
			}
			else { //If the actor is not found it prints the nearest name found by the search and asks if that is who they meant
				if (search[0] >= arr.size()) //Makes sure it is not out of bounds
					search[0] = arr.size() - 1;
				System.out.print("No such actor. Did you mean \"" + arr.get(search[0]).getName() + "\" (Y/N): ");
				input = sc.nextLine(); //Takes the input of if it's who they found
				while (!input.toUpperCase().equals("Y") && !input.toUpperCase().equals("N")) { //Checks if it is a valid answer Y/N and continues until it is
					System.out.print("Not a valid input. Please enter either \"Y\" or \"N\": ");
					input = sc.nextLine();
				}
				if (input.toUpperCase().equals("Y")) //Prints if it is
					arr.get(search[0]).print();
				System.out.print("\nEnter the name of an actor (or \"EXIT\" to quit): ");
			}
			input = sc.nextLine(); //Scans next user input
		}
		sc.close();
	}

	public static void main(String[] args) throws IOException {
		try{
			readFile(args[0]); //Sends the file location to the readFile() function to be read and sorted into arr
			System.out.println("");
			runUser(); //Takes user input to give them data
			System.out.println("Thanks for using the Movie Wall!");
		} catch (Exception e) {
			System.out.println("Something went wrong finding the file.");
		}
	}
}