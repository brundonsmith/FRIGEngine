package frigengine.entities;

import frigengine.util.IDable;
import frigengine.util.IDableCollection;

public class Chunk extends IDable<String> {
	// Attributes
	private IDableCollection<Class<? extends Category>, Category> categories;
	
	// Constructors and initializaiton
	@SuppressWarnings("unchecked")
	public Chunk(Entity entity, Class<? extends Category> ... categories) {
		// entityId
		this.id = entity.getId();
		
		// categories
		this.categories = new IDableCollection<Class<? extends Category>, Category>();
		for(Class<? extends Category> c : categories) {
			this.categories.put(entity.as(c));
		}
	}
	
	// Getters and setters
	public Category getCategory(Class<? extends Category> category) {
		return this.categories.get(category);
	}
}
