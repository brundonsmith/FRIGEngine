package frigengine.commands;

public class CommandInstance {
	// Attributes
	private Command command;
	private String[] arguments;

	// Constructors and initialization
	public CommandInstance(String command, String[] arguments) {
		this.command = Command.valueOf(command);
		this.arguments = arguments;
	}
	public CommandInstance(Command command, String[] arguments) {
		this.command = command;
		this.arguments = arguments;
	}

	// Getters and setters
	public Command getCommand() {
		return command;
	}
	public CommandType getCommandType() {
		return command.getType();
	}
	public String[] getArguments() {
		return arguments;
	}

	// Other methods
	public void execute() {

	}
}
