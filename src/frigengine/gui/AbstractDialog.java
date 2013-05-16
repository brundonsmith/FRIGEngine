package frigengine.gui;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Scanner;

import frigengine.scene.Scene;

public abstract class AbstractDialog extends GUIFrame {
	// Attributes
	protected Queue<String> pages;

	// Constructors and initialization
	public AbstractDialog(Scene context, String content) {
		super(context);
	}
	public AbstractDialog(Scene context, List<String> content) {
		super(context);
		
		// pages
		this.pages = new LinkedList<String>();
		for(String page : content)
			this.pages.add(page);
	}
	public AbstractDialog(AbstractDialog source) {
		super(source);
		
		this.pages = new LinkedList<String>();
		for(String page : source.pages)
			this.pages.add(page);
	}
	
	// Getters and setters
	public String getCurrentPage() {
		return this.pages.peek();
	}
	public void setPages(List<String> pages) {
		this.pages = new LinkedList<String>();
		for(String page : pages)
			this.pages.add(page);
	}
	public void setContent(String content) {
		this.pages = new LinkedList<String>();
		
		Scanner contentScanner = new Scanner(content);
		StringBuilder page = new StringBuilder();
		while(contentScanner.hasNext()) {
			String word = contentScanner.next();
			if(Scene.stringFitsBox(page.toString(), this.presence, this.font))
				page.append(word + " ");
			else {
				this.pages.add(page.toString());
				page = new StringBuilder(word + " ");
			}
		}
		this.pages.add(page.toString());
		contentScanner.close();
	}
	
	// Controls
	public void iterate() {
		this.pages.remove();
		if(this.pages.size() == 0)
			this.close();
	}
}
