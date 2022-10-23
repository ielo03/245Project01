public class Role {
	private String movie;
	private String role;

	public Role(String theMovie, String theRole) { //Role object contains the movie and the part
		movie = theMovie;
		role = theRole;
	}

	public String toString() { //Prints with proper formatting
		return "* Movie: " + movie + " as " + role;
	}
}