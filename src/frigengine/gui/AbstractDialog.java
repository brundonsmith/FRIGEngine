package frigengine.gui;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Scanner;

import frigengine.Scene;

public abstract class AbstractDialog extends GUIFrame {
	// Attributes
	protected Queue<String> pages;

	// Constructors and initialization
	public AbstractDialog(String content) {
		// pages
		this.setContent(content);
	}
	public AbstractDialog(List<String> pages) {
		// pages
		this.setPages(pages);
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
	public void setContent(String content) {
		this.pages = new LinkedList<String>();
		
		Scanner contentScanner = new Scanner(content);
		StringBuilder page = new StringBuilder();
		while(contentScanner.hasNext()) {
			String word = contentScanner.next();
			if(Scene.stringFitsBox(page.toString(), this.presence, this.font)) {
				page.append(word + " ");
			} else {
				this.pages.add(page.toString());
				page = new StringBuilder(word + " ");
			}
		}
		this.pages.add(page.toString());
		contentScanner.close();
	}
	public void setPages(List<String> pages) {
		this.pages = new LinkedList<String>();
		for(String page : pages)
			this.pages.add(page);
	}
	
	// Controls
	public void iterate() {
		this.pages.remove();
		if(this.pages.size() == 0) {
			this.notifyClose();
		}
	}
}
