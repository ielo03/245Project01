public class Role {
	private String movie;
	private String role;

	public Role(String theMovie, String theRole) {
		movie = theMovie;
		role = theRole;
	}

	public String toString() {
		return "* Movie: " + movie + " as " + role;
	}
}