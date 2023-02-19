package model.excommunicationtile;

import java.io.Serializable;

/**
 * The general class excommunication effect
 */
public class ExcommunicationEffect implements Serializable {
	private static final long serialVersionUID = 4319086030631087795L;
	private ExcommunicationTile tile;

	public ExcommunicationTile getTile() {
		return tile;
	}

	public void setTile(ExcommunicationTile tile) {
		this.tile = tile;
	}
}
