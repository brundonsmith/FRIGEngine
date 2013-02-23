package frigengine;

import org.newdawn.slick.util.xml.XMLElement;

public interface Initializable {
	String getTagName();
	void init(XMLElement xmlElement);
}
