import java.util.ArrayList;

public class Actor {
	private String name;
	private ArrayList<Role> roles;

	public Actor(String theName) { //Object actor contains its name and an array of its roles
		name = theName;
		roles = new ArrayList<>();
	}

	public String getName() {
		return name;
	}

	public void add(Role newRole) {
		roles.add(newRole);
	}

	public String toString() {
		return "Actor: " + name;
	}

	public void print() { //Prints the actor and its roles with proper formatting
		System.out.println(this);
		for (Role i : roles)
			System.out.println(i);
	}
}