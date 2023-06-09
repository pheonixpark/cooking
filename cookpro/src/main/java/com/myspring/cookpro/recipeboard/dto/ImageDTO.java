package com.myspring.cookpro.recipeboard.dto;

import java.sql.Date;

import org.springframework.stereotype.Component;

@Component
public class ImageDTO {
	
	private int imageFileNo;
	private String imageFileName;
	private Date regDate;
	private int recipe_no;
	
	public int getImageFileNo() {
		return imageFileNo;
	}
	public void setImageFileNo(int imageFileNo) {
		this.imageFileNo = imageFileNo;
	}
	public String getImageFileName() {
		return imageFileName;
	}
	public void setImageFileName(String imageFileName) {
		this.imageFileName = imageFileName;
	}
	public Date getRegDate() {
		return regDate;
	}
	public void setRegDate(Date regDate) {
		this.regDate = regDate;
	}
	public int getrecipeNo() {
		return recipe_no;
	}
	public void setrecipeNo(int recipeNo) {
		this.recipe_no = recipeNo;
	}

}
