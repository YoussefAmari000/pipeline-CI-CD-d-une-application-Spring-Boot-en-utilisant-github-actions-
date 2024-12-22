package com.immobiliere.model;

import jakarta.persistence.*;

@Entity
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private String content;
    
    
    @Column(nullable = true)
    private String reply;

    @ManyToOne
    @JoinColumn(name = "house_id", nullable = false)
    private House house;

    @ManyToOne
    @JoinColumn(name = "sender_id", nullable = false)
    private User sender;

    @ManyToOne
    @JoinColumn(name = "recipient_id", nullable = false)
    private User recipient;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	 public String getReply() {
	        return reply;
	    }

	    public void setReply(String reply) {
	        this.reply = reply;
	    }

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public House getHouse() {
		return house;
	}

	public void setHouse(House house) {
		this.house = house;
	}

	public User getSender() {
		return sender;
	}

	public void setSender(User sender) {
		this.sender = sender;
	}

	public User getRecipient() {
		return recipient;
	}

	public void setRecipient(User recipient) {
		this.recipient = recipient;
	}

	/**
	 * @param content
	 * @param house
	 * @param sender
	 * @param recipient
	 */
	public Message(String content, House house, User sender, User recipient) {
        this.content = content;
        this.house = house;
        this.sender = sender;
        this.recipient = recipient;
    }

	/**
	 * 
	 */
	public Message() {
		super();
	}

    // Getters et Setters
}
