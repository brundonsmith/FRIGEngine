package frigengine.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import frigengine.exceptions.ComponentException;

public abstract class Component extends IDable<Class<?>> {
	// Attributes
	private static List<Class<?>> registeredComponents = new ArrayList<Class<?>>();
	private static Map<Class<?>, Set<Class<?>>> dependencies = new HashMap<Class<?>, Set<Class<?>>>();
	private static Map<Class<?>, Set<Class<?>>> exclusives = new HashMap<Class<?>, Set<Class<?>>>();

	public Component() {
		this.id = this.getClass();
	}
	
	// Other methods
	protected final static void registerComponent(Class<?> id, Set<Class<?>> dependencies,
			Set<Class<?>> exclusives) {
		
		boolean legalIndex = false;
		//Arrays.sort(dependencies);
		int i;
		for (i = 0; !legalIndex && i <= registeredComponents.size(); i++) { // Attempt each insert spot
			legalIndex = true;
			for (int j = i; j < registeredComponents.size() && legalIndex; j++) {
				//Arrays.sort(Component.dependencies.get(registeredComponents.get(j)));
				if (dependencies.contains(registeredComponents.get(j)))//Arrays.binarySearch(dependencies, registeredComponents.get(j)) >= 0) // If a component after this index is found in the dependency list, invalidate index
					legalIndex = false;
			}

			for (int j = 0; j < i && legalIndex; j++) {
				//Arrays.sort(Component.dependencies.get(registeredComponents.get(j)));
				if (Component.dependencies.get(registeredComponents.get(j)).contains(id))//Arrays.binarySearch(Component.dependencies.get(registeredComponents.get(j)), id) >= 0) // If a component before the chosen index depends on this one, invalidate index
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
	public final static void checkAdditionValidity(Composable<? extends Component, ?> composable,
			Component component) {
		if (composable.hasComponent(component.getId()))
			throw new ComponentException("Composable '" + composable.getId()
					+ "' already has component of type '" + component.getId()
					+ "', and cannot have another one added");

		for (Class<?> dependency : Component.dependencies.get(component.getId()))
			if (!composable.hasComponent(dependency))
				throw new ComponentException("Component '" + component.getId()
						+ "' cannot be added to composable '" + composable.getId()
						+ "' because dependency '" + dependency + "' is not present");

		for (Class<?> exclusive : Component.exclusives.get(component.getId()))
			if (composable.hasComponent(exclusive))
				throw new ComponentException("Component '" + component.getId()
						+ "' cannot be added to composable '" + composable.getId()
						+ "' because exclusive '" + exclusive + "' is present");
	}
	public final static void checkRemovalValidity(Composable<? extends Component, ?> composable,
			Class<?> id) {
		if (!composable.hasComponent(id))
			throw new ComponentException("Composable has no component of type '" + id
					+ "', and cannot have it removed");

		for (Component component : composable)
			if (Arrays.asList(dependencies.get(component.getId())).contains(id))
				throw new ComponentException("Component '" + id
						+ "' cannot be removed from composable '" + composable.getId()
						+ "' because component '" + component.getId() + "' depends on it");
	}
	public static List<Class<?>> getRegisteredComponents() {
		return Collections.unmodifiableList(Component.registeredComponents);
	}
}
