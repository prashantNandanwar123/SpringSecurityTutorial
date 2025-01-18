package com.security.payload;
import lombok.Data; 
import java.util.Set;

public class PostDto { 
private long id; 
private String title; 
private String description; 
private String content;
private String creationDate;


public String getCreationDate() {
	return creationDate;
}
public void setCreationDate(String creationDate) {
	this.creationDate = creationDate;
}
public long getId() {
	return id;
}
public void setId(long id) {
	this.id = id;
}
public String getTitle() {
	return title;
}
public void setTitle(String title) {
	this.title = title;
}
public String getDescription() {
	return description;
}
public void setDescription(String description) {
	this.description = description;
}
public String getContent() {
	return content;
}
public void setContent(String content) {
	this.content = content;
} 

}