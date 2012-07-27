package com.facedev.js.editor.appearance;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Display;

/**
 * Provides configurable colors gamma for editor.
 * Colors are accessible by theirs keywords.
 * 
 * @author alex.bereznevatiy@gmail.com
 *
 */
public class ColorManager {
	/**
	 * Key for keywords color
	 */
	public static final String KEYWORD_COLOR = "keyword";
	/**
	 * Key for literals color
	 */
	public static final String LITERAL_COLOR = "literal";
	/**
	 * Key for jsdoc color
	 */
	public static final String JSDOC_COLOR = "jsdoc";
	
	/**
	 * Key for comments color
	 */
	public static final String COMMENT_COLOR = "comment";
	
	/*
	 * Locks for concurrent access:
	 */
	private static final ReadWriteLock LOCK = new ReentrantReadWriteLock();
	
	private static final Lock READ_LOCK = LOCK.readLock();
	
	private static final Lock WRITE_LOCK = LOCK.writeLock();
	
	/*
	 * Temporary implementation uses predefined values from the HashMap below.
	 * In future this map will be representing default values. 
	 */
	private static final Map<String, RGB> DEFAULT_COLORS_MAP;
	
	static {
		Map<String, RGB> m = new HashMap<String, RGB>();
		
		m.put(KEYWORD_COLOR, new RGB(149, 0, 85));
		m.put(LITERAL_COLOR, new RGB(62, 60, 196));
		m.put(JSDOC_COLOR, new RGB(99, 123, 214));
		m.put(COMMENT_COLOR, new RGB(107, 140, 85));
		
		DEFAULT_COLORS_MAP = Collections.unmodifiableMap(m);
	}
	
	/*
	 * Singleton instance of ColorManager or NULL
	 */
	private static ColorManager instance;
	
	/**
	 * @return singleton instance of ColorManager
	 */
	public static ColorManager getInstance() {
		if (instance == null) {
			synchronized (ColorManager.class) {
				if (instance == null) {
					instance = new ColorManager();
				}
			}
		}
		return instance;
	}
	
	private Map<String, Color> colorTable;
	
	/*
	 * For internal use only.
	 */
	private ColorManager()  {
		colorTable = new HashMap<String, Color>();
	}

	/**
	 * @param key to map the color to
	 * @return color matching key
	 */
	public Color getColor(String key) {
		READ_LOCK.lock();
		try {
			Color result = colorTable.get(key);
			if (result == null) {
				result = createForKey(key);
				colorTable.put(key, result);
			}
			return result;
		} finally {
			READ_LOCK.unlock();
		}
	}
	
	private Color createForKey(String key) {
		RGB rgb = DEFAULT_COLORS_MAP.get(key);
		if (rgb == null) {
			rgb = new RGB(33, 33, 33);
		}
		return new Color(Display.getCurrent(), rgb);
	}

	/**
	 * Disposes current color manager and all associated colors.
	 */
	public void dispose() {
		WRITE_LOCK.lock();
		
		for (Color color : colorTable.values()) {
			color.dispose();
		}
		
		instance = null;
		
		WRITE_LOCK.unlock();
	}
}