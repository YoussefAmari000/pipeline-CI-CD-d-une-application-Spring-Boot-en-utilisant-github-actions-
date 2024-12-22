package com.immobiliere.model;

import java.math.BigDecimal;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class House {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String address;
    private String description;
    private BigDecimal price;
    private boolean isAvailable;
    private String image;
    
    private int bedrooms;
    
    private double surface;
    
    private String city;
    
  

    @ManyToOne
    @JoinColumn(name = "owner_id")
    private User owner; // Association to the User

 
    public int getBedrooms() {
		return bedrooms;
	}

	public void setBedrooms(int bedrooms) {
		this.bedrooms = bedrooms;
	}

	public double getSurface() {
		return surface;
	}

	public void setSurface(double surface) {
		this.surface = surface;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}
	
	public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public boolean isAvailable() {
		return isAvailable;
	}

	public void setAvailable(boolean isAvailable) {
		this.isAvailable = isAvailable;
	}

	public User getOwner() {
		return owner;
	}

	public void setOwner(User owner) {
		this.owner = owner;
	}

	
	 public House(String address, String description, BigDecimal price, boolean available, String image, int bedrooms, double surface, String city, User owner) {
	        this.address = address;
	        this.description = description;
	        this.price = price;
	        this.isAvailable = available;
	        this.image = image;
	        this.bedrooms = bedrooms;
	        this.surface = surface;
	        this.city = city;
	        this.owner = owner;
	    }

	public House() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * 
	 */
	

    // Getters and Setters
}
