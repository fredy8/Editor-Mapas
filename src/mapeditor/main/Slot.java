package mapeditor.main;

import org.newdawn.slick.opengl.Texture;

public class Slot {

	private Texture Tile, Portal, Monster, NPC, Object;

	public Texture getTile() {
		return Tile;
	}

	public void setTile(Texture tile) {
		Tile = tile;
	}

	public Texture getPortal() {
		return Portal;
	}

	public void setPortal(Texture portal) {
		Portal = portal;
	}

	public Texture getMonster() {
		return Monster;
	}

	public void setMonster(Texture monster) {
		Monster = monster;
	}

	public Texture getNPC() {
		return NPC;
	}

	public void setNPC(Texture nPC) {
		NPC = nPC;
	}

	public Texture getObject() {
		return Object;
	}

	public void setObject(Texture object) {
		Object = object;
	}
	
	
	
}
