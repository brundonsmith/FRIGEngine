package frigengine.commands;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;

import frigengine.FRIGGame;

public class ScriptThread implements Runnable {

	private String[] args;
	private Queue<CommandInstance> commands;
	
	ScriptThread() {
		this.args = null;
		this.commands = new LinkedList<CommandInstance>();
	}
	void setArguments(String[] args) {
		this.args = Arrays.copyOf(args, args.length);
	}
	void addCommand(CommandInstance command) {
		CommandInstance newCommand = new CommandInstance(command);
		for(int i = 0; i < newCommand.getArguments().length; i++)
			if(newCommand.getArgument(i).matches("\\$[0-9]+*"))
				newCommand.setArgument(i, args[Integer.parseInt(newCommand.getArgument(i).substring(1))]);
		commands.add(newCommand);
	}
	
	@Override
	public void run() {
		while(!commands.isEmpty()) {
			if(commands.peek().getCommandType() == CommandType.META_COMMAND) {
				switch(commands.peek().getCommand()) {
				case WAIT:
					try {
						wait(commands.peek().getArgument(0));
					} catch (NumberFormatException e) {
						
					}
					break;
				default:
					break;
				}
			}
			else
				FRIGGame.getInstance().executeCommand(commands.peek());
			
			commands.remove();
		}
	}
	
	private void wait(String seconds) {
		try {
			Thread.sleep((int)Math.round(Float.parseFloat(seconds) * 1000));
		} catch (NumberFormatException e) {
			//throw new CommandParseException();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
