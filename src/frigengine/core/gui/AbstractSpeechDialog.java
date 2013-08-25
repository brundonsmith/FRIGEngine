package frigengine.core.gui;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Scanner;

import frigengine.core.geom.*;
import frigengine.core.scene.*;

public abstract class AbstractSpeechDialog extends GUIFrame {
	// Attributes
	protected Queue<String> pages;

	// Constructors and initialization
	public AbstractSpeechDialog(Rectangle domain, String content) {
		super(domain);
		
		// pages
		this.setContent(content);
	}
	public AbstractSpeechDialog(Rectangle domain, List<String> pages) {
		super(domain);
		
		// pages
		this.setPages(pages);
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
			if(Scene.stringFitsBox(page.toString(), this.domain, this.font)) {
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
			this.reportCancel();
		}
	}
}
