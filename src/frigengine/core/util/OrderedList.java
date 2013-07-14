package frigengine.core.util;

import java.util.Collection;
import java.util.Comparator;
import java.util.LinkedList;

@SuppressWarnings("serial")
public class OrderedList<T extends Comparable<T>> extends LinkedList<T> {
	// Attributes
	private Comparator<T> comparator;
	
	// Constructors and initialization
	public OrderedList() {
		this.comparator = null;
	}
	public OrderedList(Comparator<T> comparator) {
		this.comparator = comparator;
	}
	
	// Operations
	@Override
	public boolean add(T element) {
		if(this.isEmpty()) {
			super.add(element);
		} else {
			int index = this.getItemIndex(element);
			if(index < this.size()) {
				super.add(index, element);
			} else {
				super.add(element);
			}
		}
		return true;
	}
	@Override
	public boolean addAll(Collection<? extends T> elements) {
		if(elements == null || elements.isEmpty()) {
			return false;
		} else {
			for(T element : elements) {
				this.add(element);
			}
			return true;
		}
	}
	private int getItemIndex(T element) {
		int min = 0;
		int max = this.size();
		int index = (min + max) / 2;
		
		if(this.comparator == null) {
			while(min < max) {
				index = (min + max) / 2;
				if(this.get(index).compareTo(element) < 0) { // if item should be to the right
					min = index + 1;
				} else if(this.get(index).compareTo(element) > 0) { // if item should be to the left
					max = index;
				} else {
					return index;
				}
			}
		} else {
			while(min < max) {
				index = (min + max) / 2;
				if(this.comparator.compare(this.get(index), element) < 0) { // if item should be to the right
					min = index + 1;
				} else if(this.comparator.compare(this.get(index), element) > 0) { // if item should be to the left
					max = index;
				} else {
					return index;
				}
			}
		}
		
		if(min == max) {
			index = min;
		}
		return index;
	}
}
