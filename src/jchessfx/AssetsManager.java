package jchessfx;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import javafx.scene.image.Image;
import javafx.scene.media.AudioClip;
import javafx.scene.text.Font;

public enum AssetsManager {
	INSTANCE;
	
	private Map<String, Image>     images;
	private Map<String, AudioClip> audioClips;
	private Map<String, Font>      fonts;
	
	private AssetsManager() {
		images     = new HashMap<String, Image>();
		audioClips = new HashMap<String, AudioClip>();
		fonts      = new HashMap<String, Font>();
	}
	
	public Image getImage(String path) throws IOException {
		if (!images.containsKey(path)) {
			/*
			 * Throws:
			 *    java.lang.NullPointerException     - if URL is null
			 *    java.lang.IllegalArgumentException - if URL is invalid or unsupported
			 */
			FileInputStream fis = new FileInputStream(getFullPath(path));
			Image newImage = new Image(fis);
			fis.close();
			images.put(path, newImage);
		}
		return images.get(path);
	}

	public AudioClip getAudioClip(String path) {
		if (!images.containsKey(path)) {
			/*
			 * Throws:
			 *    java.lang.NullPointerException     - if URL is null
			 *    java.lang.IllegalArgumentException - if URL is invalid or unsupported
			 */
			AudioClip newAudioClip = new AudioClip(path);
			audioClips.put(path, newAudioClip);
		}
		return audioClips.get(path);
	}

	public Font getFont(String path, int size) throws IOException {
		if (!fonts.containsKey(path)) {
			FileInputStream fis = new FileInputStream(getFullPath(path));
			Font font = Font.loadFont(fis, size);
			fis.close();
			fonts.put(path, font);
		}
		return fonts.get(path);
	}
	
	private String getFullPath(String relativePath) {
		return Paths.get(relativePath).toAbsolutePath().normalize().toString();
	}
}
