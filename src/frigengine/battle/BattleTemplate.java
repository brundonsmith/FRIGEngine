package frigengine.battle;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.newdawn.slick.util.xml.XMLElement;

import frigengine.core.component.*;
import frigengine.core.exceptions.data.*;
import frigengine.core.idable.*;
import frigengine.core.scene.*;
import frigengine.core.util.Initializable;

public class BattleTemplate extends IDable<String> implements Initializable {
	// Attributes
	protected List<SceneLayer> layers;
	protected List<Entity> enemies;

	// Constructors and initialization
	public BattleTemplate(String id) {
		this.setId(id);
		this.layers = new ArrayList<SceneLayer>();
		this.enemies = new ArrayList<Entity>();
	}
	public void init(XMLElement xmlElement) {
		// Check element name
		if (!xmlElement.getName().equals(this.getClass().getSimpleName())) {
			throw new InvalidTagException(this.getClass().getSimpleName(), xmlElement.getName());
		}

		// id
		this.setId(xmlElement.getAttribute("id", this.getId()));

		// layers
		for (int i = 0; i < xmlElement.getChildrenByName(SceneLayer.class.getSimpleName()).size(); i++) {
			XMLElement child = xmlElement.getChildrenByName(SceneLayer.class.getSimpleName()).get(i);

			SceneLayer layer = new SceneLayer();
			layer.init(child);
			this.layers.add(layer);
		}
		Collections.sort(this.layers);

		// enemies
		for (int i = 0; i < xmlElement.getChildrenByName(Entity.class.getSimpleName()).size(); i++) {
			XMLElement child = xmlElement.getChildrenByName(Entity.class.getSimpleName()).get(i);
			
			if(child.getAttribute("id") != null) {
				Entity newEntity;
				if(Entity.getEntity(child.getAttribute("id")) != null) {
					newEntity = Entity.getEntity(child.getAttribute("id"));
				} else {
					newEntity = new Entity(child.getAttribute("id"));
				}
				newEntity.init(child);
				this.enemies.add(newEntity);
			}
		}
	}
}
