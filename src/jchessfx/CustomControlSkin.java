package jchessfx;

import javafx.scene.control.Skin;
import javafx.scene.control.SkinBase;

/**
 * Represent a default skin used by our custom controls.
 */
class CustomControlSkin extends SkinBase<CustomControl> implements Skin<CustomControl> {
	/**
	 * Create a new empty skin.
	 * @param cc CustomControl associated with the skin.
	 */
	public CustomControlSkin(CustomControl cc) {
		super(cc);
	}
}
