package frigengine.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import frigengine.exceptions.ComponentException;

public abstract class Component extends IDable {
	// Attributes
	private static List<String> registeredComponents = new ArrayList<String>();
	private static Map<String, String[]> dependencies = new HashMap<String, String[]>();
	private static Map<String, String[]> exclusives = new HashMap<String, String[]>();

	// Other methods
	protected final static void registerComponent(String id, String[] dependencies,
			String[] exclusives) {
		ArrayList<String> al = (ArrayList<String>) Component.registeredComponents;

		boolean legalIndex = false;
		Arrays.sort(dependencies);
		int i;
		for (i = 0; !legalIndex && i <= registeredComponents.size(); i++) { // Attempt each insert spot
			legalIndex = true;
			for (int j = i; j < registeredComponents.size() && legalIndex; j++) {
				Arrays.sort(Component.dependencies.get(registeredComponents.get(j)));
				if (Arrays.binarySearch(dependencies, registeredComponents.get(j)) >= 0) // If a component after this index is found in the dependency list, invalidate index
					legalIndex = false;
			}

			for (int j = 0; j < i && legalIndex; j++) {
				Arrays.sort(Component.dependencies.get(registeredComponents.get(j)));
				String[] d = Component.dependencies.get(registeredComponents.get(j));
				if (Arrays
						.binarySearch(Component.dependencies.get(registeredComponents.get(j)), id) >= 0) // If a component before the chosen index depends on this one, invalidate index
					legalIndex = false;
			}
		}
		i--;
		if (registeredComponents.size() > 0 && !legalIndex)
			throw new ComponentException("Component '" + id
					+ "' has one or more cyclical dependencies");

		Component.registeredComponents.add(i, id);
		Component.dependencies.put(id, dependencies);
		Component.exclusives.put(id, exclusives);
	}

	public final static void checkAdditionValidity(Composable<? extends Component> composable,
			Component component) {
		if (composable.hasComponent(component.getID()))
			throw new ComponentException("Composable '" + composable.getID()
					+ "' already has component of type '" + component.getID()
					+ "', and cannot have another one added");

		for (String dependency : Component.dependencies.get(component.getID()))
			if (!composable.hasComponent(dependency))
				throw new ComponentException("Component '" + component.getID()
						+ "' cannot be added to composable '" + composable.getID()
						+ "' because dependency '" + dependency + "' is not present");

		for (String exclusive : Component.exclusives.get(component.getID()))
			if (composable.hasComponent(exclusive))
				throw new ComponentException("Component '" + component.getID()
						+ "' cannot be added to composable '" + composable.getID()
						+ "' because exclusive '" + exclusive + "' is present");
	}

	public final static void checkRemovalValidity(Composable<? extends Component> composable,
			String id) {
		if (!composable.hasComponent(id))
			throw new ComponentException("Composable has no component of type '" + id
					+ "', and cannot have it removed");

		for (Component component : composable)
			if (Arrays.asList(dependencies.get(component.getID())).contains(id))
				throw new ComponentException("Component '" + id
						+ "' cannot be removed from composable '" + composable.getID()
						+ "' because component '" + component.getID() + "' depends on it");
	}

	public static List<String> getRegisteredComponents() {
		return Collections.unmodifiableList(Component.registeredComponents);
	}
}
