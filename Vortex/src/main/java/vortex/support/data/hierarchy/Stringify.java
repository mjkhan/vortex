package vortex.support.data.hierarchy;

import java.util.Collection;
import java.util.function.Predicate;

import vortex.support.AbstractObject;

public class Stringify<T extends HierarchyElement> extends AbstractObject {
	@FunctionalInterface
	public static interface ToString<T extends HierarchyElement> {
		public String get(T element, int level);
	}
		
	private ToString<T>
		emptyString = (e, level) -> "",
		beginElement = emptyString,
		endElement = emptyString,
		beginChildren = emptyString,
		endChildren = emptyString;
	private Predicate<T> proceed;
		
	public static final String indent(String s, int n) {
		String result = "";
		for (int i = 0; i < n; ++i) {
			result += s;
		}
		return result;
	}
	
	public Stringify<T> beginElement(ToString<T> begin) {
		this.beginElement = begin;
		return this;
	}
	
	public Stringify<T> endElement(ToString<T> end) {
		this.endElement = end;
		return this;
	}
	
	public Stringify<T> beginChildren(ToString<T> begin) {
		this.beginChildren = begin;
		return this;
	}
	
	public Stringify<T> endChildren(ToString<T> end) {
		this.endChildren = end;
		return this;
	}
	
	public Stringify<T> proceed(Predicate<T> proceed) {
		this.proceed = proceed;
		return this;
	}

	public String toString(Iterable<T> elements) {
		if (isEmpty(elements)) return "";

		StringBuilder buff = new StringBuilder();
		convert(elements, buff, 0);
		return buff.toString().trim();
	}
	
	private void convert(Iterable<T> elements, StringBuilder buff, int level) {
		elements.forEach(element -> {
			if (element == null) return;
			
			boolean parent = element instanceof CompositeElement;
			Collection<? extends HierarchyElement> children = parent ? ((CompositeElement)element).getChildren() : null;
			parent = parent && !isEmpty(children);
			
			buff.append(beginElement.get(element, level));
			if (parent) {
				int sublevel = level + 1;
				buff.append(beginChildren.get(element, sublevel));
				convert((Iterable<T>)children, buff, sublevel);
				buff.append(endChildren.get(element, sublevel));
			}
			buff.append(endElement.get(element, level));
		});
	}
}