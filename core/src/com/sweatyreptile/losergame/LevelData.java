package com.sweatyreptile.losergame;

public class LevelData {

	private String alias;
	private String type;
	private String title;
	private float viewportWidth;
	private float viewportHeight;
	private float timerLength;
	private String bgname;
	
	private EntityData[] entities;

	public LevelData(String alias, String type, String title,
			float viewportWidth, float viewportHeight, float timerLength,
			String bgname, EntityData[] entities) {
		super();
		this.alias = alias;
		this.type = type;
		this.title = title;
		this.viewportWidth = viewportWidth;
		this.viewportHeight = viewportHeight;
		this.timerLength = timerLength;
		this.bgname = bgname;
		this.entities = entities;
	}

	public String getAlias() {
		return alias;
	}
	public void setAlias(String alias) {
		this.alias = alias;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public float getViewportWidth() {
		return viewportWidth;
	}
	public void setViewportWidth(float viewportWidth) {
		this.viewportWidth = viewportWidth;
	}
	public float getViewportHeight() {
		return viewportHeight;
	}
	public void setViewportHeight(float viewportHeight) {
		this.viewportHeight = viewportHeight;
	}
	public float getTimerLength() {
		return timerLength;
	}
	public void setTimerLength(float timerLength) {
		this.timerLength = timerLength;
	}
	public String getBgname() {
		return bgname;
	}
	public void setBgname(String bgname) {
		this.bgname = bgname;
	}
	public EntityData[] getEntities() {
		return entities;
	}
	public void setEntities(EntityData[] entities) {
		this.entities = entities;
	}
}
