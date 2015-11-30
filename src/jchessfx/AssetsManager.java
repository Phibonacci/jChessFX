package jchessfx;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import javafx.scene.image.Image;
import javafx.scene.media.AudioClip;
import javafx.scene.text.Font;

public enum AssetsManager {
	INSTANCE;
	
	private Map<String, Image>     images;
	private Map<String, Font>      fonts;
	
	private AssetsManager() {
		images     = new HashMap<String, Image>();
		fonts      = new HashMap<String, Font>();
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

	public Font getFont(String path, int size) throws IOException {
		if (!fonts.containsKey(path)) {
			InputStream is = getStreamFromResource(path);
			Font font = Font.loadFont(is, size);
			is.close();
			fonts.put(path, font);
		}
		return fonts.get(path);
	}
	
	private InputStream getStreamFromResource(String path) throws IOException {
		InputStream stream = getClass().getResourceAsStream(path);
		if (stream == null) {
			throw new IOException("Could not find ressource " + path);
		}
		return stream;
	}
}
