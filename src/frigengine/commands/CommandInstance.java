package frigengine.commands;

import java.util.Arrays;

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
	public CommandInstance(CommandInstance other) {
	    this.command = other.command;
	    this.arguments = Arrays.copyOf(other.arguments, other.arguments.length);
	}

	public static CommandInstance parseCommand(String line) {
		return new CommandInstance(line.split("(")[0],
				line.split("(")[1].split(")")[0].replace(" ", "")
						.replace("\t", "").split(","));
	}

	// Getters and setters
	public Command getCommand() {
		return command;
	}
	public CommandType getCommandType() {
		return command.getType();
	}
	public String getArgument(int index) {
		return arguments[index];
	}
	public void setArgument(int index, String value) {
		arguments[index] = value;
	}
	public String[] getArguments() {
		return Arrays.copyOf(arguments, arguments.length);
	}

	// Other methods
	public void execute() {

	}
}
