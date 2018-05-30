package com.example.eventmanager.domain;

public class MessageWrapper {
	private String login;
	private String image;
	private String text;
	private String date;

	public String getLogin() {
		return login;
	}

	public String getImage() {
		return image;
	}

	public String getText() {
		return text;
	}

	public String getDate() {
		return date;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public void setText(String text) {
		this.text = text;
	}

	public void setDate(String date) {
		this.date = date;
	}
}
