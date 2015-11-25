package jchessfx;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;

import javafx.scene.image.Image;
import javafx.scene.media.AudioClip;

public enum AssetsManager {
	INSTANCE;
	
	private Map<String, Image>     images;
	private Map<String, AudioClip> audioClips;
	
	private AssetsManager() {
		images     = new HashMap<String, Image>();
		audioClips = new HashMap<String, AudioClip>();
	}
	
	public Image getImage(String path) throws FileNotFoundException {
		if (!images.containsKey(path)) {
			/*
			 * Throws:
			 *    java.lang.NullPointerException     - if URL is null
			 *    java.lang.IllegalArgumentException - if URL is invalid or unsupported
			 */
			java.io.FileInputStream fis = new FileInputStream(path);
			Image newImage = new Image(fis);
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
}
