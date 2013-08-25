package frigengine.field;

import java.util.Collections;

import org.lwjgl.input.Keyboard;
import org.newdawn.slick.Input;
import org.newdawn.slick.util.xml.SlickXMLException;
import org.newdawn.slick.util.xml.XMLElement;

import frigengine.battle.*;
import frigengine.core.component.*;
import frigengine.core.exceptions.data.*;
import frigengine.core.gui.*;
import frigengine.core.scene.*;
import frigengine.core.util.*;

public class Area extends Scene implements Initializable {
	// Attributes
	private String name;

	// Constructors and initialization
	public Area(String id) {
		super(id);
		this.name = this.getId();
	}
	@Override
	public void init(XMLElement xmlElement) {
		// Check element name
		if (!xmlElement.getName().equals(this.getClass().getSimpleName())) {
			throw new InvalidTagException(this.getClass().getSimpleName(), xmlElement.getName());
		}

		// id
		this.setId(xmlElement.getAttribute("id", this.getId()));
		
		// width
		try {
			this.width = (float) xmlElement.getDoubleAttribute("width", this.width);
		} catch (SlickXMLException e) {
			throw new AttributeFormatException(xmlElement.getName(), "width",
					xmlElement.getAttribute("width"));
		}
		
		// height
		try {
			this.height = (float) xmlElement.getDoubleAttribute("height", this.height);
		} catch (SlickXMLException e) {
			throw new AttributeFormatException(xmlElement.getName(), "height",
					xmlElement.getAttribute("height"));
		}

		// currentCamera
		this.currentCamera = xmlElement.getAttribute("defaultcamera", this.currentCamera);

		// layers
		for (int i = 0; i < xmlElement.getChildrenByName(SceneLayer.class.getSimpleName()).size(); i++) {
			XMLElement child = xmlElement.getChildrenByName(SceneLayer.class.getSimpleName()).get(i);

			SceneLayer layer = new SceneLayer();
			layer.init(child);
			this.layers.add(layer);
		}
		Collections.sort(this.layers);

		// entities
		for (int i = 0; i < xmlElement.getChildrenByName(Entity.class.getSimpleName()).size(); i++) {
			XMLElement child = xmlElement.getChildrenByName(Entity.class.getSimpleName()).get(i);
			
			if(child.getAttribute("id") != null) {
				Entity newEntity;
				if(Entity.entityExists(child.getAttribute("id"))) {
					newEntity = Entity.getEntity(child.getAttribute("id"));
				} else {
					newEntity = new Entity(child.getAttribute("id"));
				}
				newEntity.init(child);
				this.addEntity(newEntity.getId());
			}
		}
		
		// guiStack

		// name
		this.name = xmlElement.getAttribute("name", this.name);
	}
	@Override
	public void onGainFocus(Scene previousScene) {
		if(previousScene instanceof Battle) {
			for(Entity e : Entity.getEntities(this)) {
				e.revertState("field");
			}
		}
	}
	@Override
	public void onLoseFocus(Scene newScene) {
		if(newScene instanceof Battle) {
			for(Entity e : Entity.getEntities(this)) {
				e.saveState("field", PositionComponent.class, SpriteComponent.class, MovementComponent.class);
			}
		}
	}
	
	// Main loop methods
	@Override
	public void update(int delta, Input input) {
		boolean timeBlocked = false;
		boolean inputBlocked = false;
		
		//////////////////////////////////////////
		// TEMPORARY
		if(input.isKeyPressed(Keyboard.KEY_RSHIFT)) {
			this.openGUI(new SpeechDialog("Hallo thar this is me speaking sup"));
		}
		if(input.isKeyDown(Keyboard.KEY_LSHIFT)) {
			this.zoomCamera(0.99F);
		}
		if(input.isKeyDown(Keyboard.KEY_SPACE)) {
			this.zoomCamera(1.01F);
		}
		//////////////////////////////////////////
		
		
		// GUI
		for (Object o : this.guiStack.toArray()) {
			GUIFrame frame = (GUIFrame) o;
			frame.update(timeBlocked ? 0 : delta, inputBlocked ? null : input);
			if (frame.getBlocksTime()) {
				timeBlocked = true;
			}
			if (frame.getBlocksInput()) {
				inputBlocked = true;
			}
		}
		
		// ParticleEffects
		this.particleSystem.update(timeBlocked ? 0 : delta);

		// Entities
		for (Entity entity : Entity.getEntities(this)) {
			entity.update(timeBlocked ? 0 : delta, inputBlocked ? null : input);
		}

		// Layers
		for (SceneLayer layer : this.layers) {
			layer.update(timeBlocked ? 0 : delta);
		}
	}

	// Getters and setters
	public String getName() {
		return name;
	}
}
