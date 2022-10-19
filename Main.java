import java.util.ArrayList;
import java.lang.Math;
import java.util.Scanner;
import java.io.*;

public class Main {
	/**The arraylist holding all the sorted Actor objects
	 */
	private static ArrayList<Actor> arr = new ArrayList<>();

	/**Searches for a name in the arr arraylist.
	 * @Param theName the name it is searching for
	 * @Return returns the index if it is found, return the index but negative if it is not found, indicating where it needs to be inserted.
	 */
	private static int search(String theName) {
		int min = 0;
		int max = arr.size()-1;
		int mid = max/2;
		
		while (min < max) {
			mid = (max - min)/2;
			if (arr.get(mid).getName().compareTo(theName) == 0)
				return mid;
			else if (arr.get(mid).getName().compareTo(theName) > 0)
				max = mid;
			else
				min = mid;
		}
		if (arr.get(mid).getName().compareTo(theName) == 0)
				return mid;
		else if (arr.get(mid).getName().compareTo(theName) > 0)
			return -(mid-1);
		else
			return -(mid+1);
	}

	/**Adds a role to an actor. If the actor already exists, it finds it in arr and adds it.
	 * If it doesn't exist it creates a new actor, adds the role, and inserts the new actor alphabetically into arr.
	 * @Param theName the name of the actor the role needs to be added to.
	 * @Param theMovie the name of the movie.
	 * @Param theRole the name of the character or their non acting role.
	 */
	private static void addRole(String theName, String theMovie, String theRole) {
		Role role = new Role(theMovie, theRole);
		if (arr.size() < 1) {
			Actor actor = new Actor(theName);
			actor.add(role);
			arr.add(actor);
		} else {
			int search = search(theName);
			if (search < 0) {
				Actor actor = new Actor(theName);
				actor.add(role);
				arr.add(Math.abs(search), actor);
			} else
				arr.get(search).add(role);
		}
	}

	/**Prints all actors and their movies with proper formatting.
	 */
	private static void print() {
		for (Actor i : arr) {
			i.print();
		}
	}

	/*private static void skipFirst(BufferedReader br) {
		try {
			while ()
		} catch (Exception FileNotFoundException) {
			System.out.println("File not found.");
		}
	}
	private static String getMovie(BufferedReader br) {

	}*/

	/**Reads the file and creates the arraylist from it.
	 * @Param theFile the file to read from
	 */
	private static void readFile(String theFile) {
		String theName;
		String theMovie;
		String theRole;
		String cast;
		try {
			File f = new File(theFile);
			FileReader fr = new FileReader(f);
			BufferedReader br = new BufferedReader(fr);
			String line = br.readLine();
			line = br.readLine();
			while (line != null) {
				while (line.length() < 100)
					line = br.readLine();
				line = line.substring(line.indexOf(",")+1, line.length());
				if (line.substring(0,1).equals("\"")) {
					line = line.substring(line.indexOf("\"") + 1, line.length());
					theMovie = line.substring(0, line.indexOf("\""));
				}
				else
					theMovie = line.substring(0, line.indexOf(","));
				cast = line.substring(line.indexOf("["), line.indexOf("]") + 1);
				line = line.substring(line.indexOf("]") + 4, line.length() - 1);

				int ind = cast.indexOf("\"\"character\"\": \"\"");
				//System.out.println(cast);
				while (ind != -1) {
					System.out.println(cast);
					cast = cast.substring(ind + 14, cast.length());
					System.out.println(cast);
					theRole = cast.substring(0, cast.indexOf("\""));
					//System.out.println(theRole);
					ind = cast.indexOf("\"\"character\"\": \"\"");
				}
				line = br.readLine();
			}
		}
		catch (Exception e) {
			System.out.println(e);
		}
	}

	public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		String file = reader.readLine();
		try{
			//Scanner sc = new Scanner(new File("tmdb_5000_credits.csv"));
			readFile(file);
			//sc.close();
		} catch (Exception e) {
			System.out.println(e);
		}
	}
}