package com.fh.entity.system;

public class SegaProportion {

	private String SEGA_ID;
    private String SEGA_PROPORTION;
    private String ROOM_ID;
    
	public SegaProportion() {
		super();
	}

	public String getSEGA_ID() {
		return SEGA_ID;
	}

	public void setSEGA_ID(String sEGA_ID) {
		SEGA_ID = sEGA_ID;
	}

	public String getSEGA_PROPORTION() {
		return SEGA_PROPORTION;
	}

	public void setSEGA_PROPORTION(String sEGA_PROPORTION) {
		SEGA_PROPORTION = sEGA_PROPORTION;
	}

	public String getROOM_ID() {
		return ROOM_ID;
	}

	public void setROOM_ID(String rOOM_ID) {
		ROOM_ID = rOOM_ID;
	}

	@Override
	public String toString() {
		return "SegaProportion [SEGA_ID=" + SEGA_ID + ", SEGA_PROPORTION=" + SEGA_PROPORTION + ", ROOM_ID=" + ROOM_ID
				+ "]";
	}
	
	
}
