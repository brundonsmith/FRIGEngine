package frigengine.field;

import frigengine.core.component.*;
import frigengine.core.util.Initializable;

public abstract class AbstractTriggeredEvent implements Initializable {
	public abstract void execute(Entity catalyst);
}
