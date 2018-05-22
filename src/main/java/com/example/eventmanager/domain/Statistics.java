package com.example.eventmanager.domain;

public class Statistics {
	private Long asCreator;
	private Long asParticipant;
	private Long messagesSent;
	private String withUs;
	private Long likes;
	private Long males;
	private Long females;
	private String avgAge;
	
	public Long getAsCreator() {
		return asCreator;
	}
	public void setAsCreator(Long asCreator) {
		this.asCreator = asCreator;
	}
	public Long getAsParticipant() {
		return asParticipant;
	}
	public void setAsParticipant(Long asParticipant) {
		this.asParticipant = asParticipant;
	}
	public Long getMessagesSent() {
		return messagesSent;
	}
	public void setMessagesSent(Long messagesSent) {
		this.messagesSent = messagesSent;
	}
	public String getWithUs() {
		return withUs;
	}
	public void setWithUs(String withUs) {
		this.withUs = withUs;
	}
	public Long getLikes() {
		return likes;
	}
	public void setLikes(Long likes) {
		this.likes = likes;
	}
	public Long getMales() {
		return males;
	}
	public void setMales(Long males) {
		this.males = males;
	}
	public Long getFemales() {
		return females;
	}
	public void setFemales(Long females) {
		this.females = females;
	}
	public String getAvgAge() {
		return avgAge;
	}
	public void setAvgAge(String avgAge) {
		this.avgAge = avgAge;
	}
	
	
}
