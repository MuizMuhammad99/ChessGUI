package engine;

import engine.piece.Alliance;

/**
 * Player class
 *
 */
public class Player {

	private Alliance alliance;

	public Player(Alliance alliance) {
		this.alliance = alliance;
	}

	public Alliance getAlliance() {
		return alliance;
	}
}
