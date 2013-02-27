package frigengine.util;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import frigengine.exceptions.ComponentException;

public abstract class Component extends IDable {
	// Attributes
	private static Set<String> registeredComponents = new HashSet<String>();
	private static Map<String, String[]> dependencies = new HashMap<String, String[]>();
	private static Map<String, String[]> exclusives = new HashMap<String, String[]>();

	// Other methods
	protected final static void registerComponent(Class<? extends Component> c,
			String id, String[] dependencies, String[] exclusives) {
		Component.registeredComponents.add(id);
		Component.dependencies.put(id, dependencies);
		Component.exclusives.put(id, exclusives);
	}

	public final static void checkAdditionValidity(
			Composable<? extends Component> composable, Component component) {
		if (composable.hasComponent(component.getID()))
			throw new ComponentException("Composable '" + composable.getID()
					+ "' already has component of type '" + component.getID()
					+ "', and cannot have another one added");

		for (String dependency : Component.dependencies.get(component.getID()))
			if (!composable.hasComponent(dependency))
				throw new ComponentException("Component '" + component.getID()
						+ "' cannot be added to composable '"
						+ composable.getID() + "' because dependency '"
						+ dependency + "' is not present");

		for (String exclusive : Component.exclusives.get(component.getID()))
			if (composable.hasComponent(exclusive))
				throw new ComponentException("Component '" + component.getID()
						+ "' cannot be added to composable '"
						+ composable.getID() + "' because exclusive '"
						+ exclusive + "' is present");
	}

	public final static void checkRemovalValidity(
			Composable<? extends Component> composable, String id) {
		if (!composable.hasComponent(id))
			throw new ComponentException(
					"Composable has no component of type '" + id
							+ "', and cannot have it removed");

		for (Component component : composable)
			if (Arrays.asList(dependencies.get(component.getID())).contains(id))
				throw new ComponentException("Component '" + id
						+ "' cannot be removed from composable '"
						+ composable.getID() + "' because component '"
						+ component.getID() + "' depends on it");
	}
}
