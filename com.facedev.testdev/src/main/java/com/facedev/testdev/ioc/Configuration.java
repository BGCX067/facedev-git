package com.facedev.testdev.ioc;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.servlet.ServletContext;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class Configuration {
	private static final String CONFIG_LOCATION = "/WEB-INF/application.xml";
	private static final String CLASS_TAG_NAME = "class";
	
	private static volatile Configuration instance;
	
	private final List<String> classes;
	
	private Configuration(List<String> classes) {
		this.classes = classes;
	}
	
	public static Configuration get() {
		if (instance == null) {
			throw new ContainerException("Configuration is not loaded yet");
		}
		return instance;
	}
	
	public List<String> getConfiguredClassNames() {
		return classes;
	}
	
	static Configuration load(ServletContext servletContext) {
		InputStream config = servletContext.getResourceAsStream(CONFIG_LOCATION);
		if (config == null) {
			throw new ContainerException("Unable to find container configuration");
		}
		try {
			load(config);
		} catch (IOException ex) {
			throw new ContainerException("Unable to load container configuration", ex);
		} catch (SAXException ex) {
			throw new ContainerException("Unable to load container configuration", ex);
		} catch (ParserConfigurationException ex) {
			throw new ContainerException("Unable to load container configuration", ex);
		}
		return instance;
	}

	private static void load(InputStream config) throws IOException, SAXException, ParserConfigurationException {
		try {
			load(DocumentBuilderFactory.newInstance()
					.newDocumentBuilder().parse(config));
		} finally {
			config.close();
		}
	}

	private static void load(Document document) {
		List<String> classes = readClasses(document);
		instance = new Configuration(classes);
	}

	private static List<String> readClasses(Document document) {
		NodeList classes = document.getElementsByTagName(CLASS_TAG_NAME);
		String[] result = new String[classes.getLength()];
		for (int i = 0; i < classes.getLength(); i++) {
			Element classEl = (Element)classes.item(i);
			result[i] = classEl.getTextContent().trim();
		}
		return Collections.unmodifiableList(new CopyOnWriteArrayList<String>(result));
	}
}
