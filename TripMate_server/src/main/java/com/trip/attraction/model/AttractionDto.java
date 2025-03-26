package com.trip.attraction.model;

/**
 * 관광 정보에 대한 DTO 클래스
 */

public class AttractionDto {

	private String contentId;
	private String title;
	private String contentTypeId;
	private String areaCode;
	private String siGunGuCode;
	private String firstImage1;
	private String firstImage2;
	private int mapLevel;
	private String latitude;
	private String longitude;
	private String tel;
	private String addr1;
	private String addr2;
	private String homePage;
	private String overView;
	
	public String getContentId() {
		return contentId;
	}

	public void setContentId(String contentId) {
		this.contentId = contentId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContentTypeId() {
		return contentTypeId;
	}

	public void setContentTypeId(String contentTypeId) {
		this.contentTypeId = contentTypeId;
	}

	public String getAreaCode() {
		return areaCode;
	}

	public void setAreaCode(String areaCode) {
		this.areaCode = areaCode;
	}

	public String getSiGunGuCode() {
		return siGunGuCode;
	}

	public void setSiGunGuCode(String siGunGuCode) {
		this.siGunGuCode = siGunGuCode;
	}

	public String getFirstImage1() {
		return firstImage1;
	}

	public void setFirstImage1(String firstImage1) {
		this.firstImage1 = firstImage1;
	}

	public String getFirstImage2() {
		return firstImage2;
	}

	public void setFirstImage2(String firstImage2) {
		this.firstImage2 = firstImage2;
	}

	public int getMapLevel() {
		return mapLevel;
	}

	public void setMapLevel(int mapLevel) {
		this.mapLevel = mapLevel;
	}

	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	public String getAddr1() {
		return addr1;
	}

	public void setAddr1(String addr1) {
		this.addr1 = addr1;
	}

	public String getAddr2() {
		return addr2;
	}

	public void setAddr2(String addr2) {
		this.addr2 = addr2;
	}

	public String getHomePage() {
		return homePage;
	}

	public void setHomePage(String homePage) {
		this.homePage = homePage;
	}

	public String getOverView() {
		return overView;
	}

	public void setOverView(String overView) {
		this.overView = overView;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("[title=").append(title).append(", addr1=").append(addr1).append("]");
		return builder.toString();
	}

	

}
