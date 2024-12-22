package com.immobiliere.model;

import jakarta.persistence.*;

@Entity
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private String comment;

    @Column(nullable = false)
    private int rating;

    @ManyToOne
    @JoinColumn(name = "house_id", nullable = false)
    private House house;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public int getRating() {
		return rating;
	}

	public void setRating(int rating) {
		this.rating = rating;
	}

	public House getHouse() {
		return house;
	}

	public void setHouse(House house) {
		this.house = house;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	/**
	 * @param comment
	 * @param rating
	 * @param house
	 * @param user
	 */
	public Comment(String comment, int rating, House house, User user) {
		super();
		this.comment = comment;
		this.rating = rating;
		this.house = house;
		this.user = user;
	}

	/**
	 * 
	 */
	public Comment() {
		super();
	}

  
}
