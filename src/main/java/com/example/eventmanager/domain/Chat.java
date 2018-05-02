package com.example.eventmanager.domain;

import lombok.Data;

@Data
public class Chat {
	private long id;
	private long eventId;
	private boolean withCreator;
	private String image;
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public long getEventId() {
		return eventId;
	}
	public void setEventId(long eventId) {
		this.eventId = eventId;
	}
	public boolean isWithCreator() {
		return withCreator;
	}
	public void setWithCreator(boolean withCreator) {
		this.withCreator = withCreator;
	}
	public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
	}
	
}
