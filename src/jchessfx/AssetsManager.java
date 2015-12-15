package jchessfx;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import javafx.scene.image.Image;

/**
 * Singleton containing a cache of the images used by the game,
 * to prevent the operating system from reading them from the disk
 * more than once.
 */
public enum AssetsManager {
	INSTANCE;
	
	private Map<String, Image> images;
	
	private AssetsManager() {
		images = new HashMap<String, Image>();
	}
	
	public Image getImage(String path) throws IOException {
		if (!images.containsKey(path)) {
			InputStream stream = getStreamFromResource(path);
			Image newImage = new Image(stream);
			stream.close();
			images.put(path, newImage);
		}
		return images.get(path);
	}
	
	private InputStream getStreamFromResource(String path) throws IOException {
		InputStream stream = getClass().getResourceAsStream(path);
		if (stream == null) {
			throw new IOException("Could not find ressource " + path);
		}
		return stream;
	}
}
