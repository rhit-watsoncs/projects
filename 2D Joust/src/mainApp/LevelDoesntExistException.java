package mainApp;

public class LevelDoesntExistException extends Exception {

	public LevelDoesntExistException() {
		System.err.println("ERROR... LEVEL DOESN'T EXIST!");
	}

	public String getMessage() {

		return ("ERROR LEVEL DOESNT EXIST");
	}

}
