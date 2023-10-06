package com.root32.dto;

import java.util.List;

public class Location {

  private String pin;

  private String country;

  private String state;

  private String district;

  private String division;

  private String city;

  private List<String> blocks;

  private List<String> areas;

  public String getPin() {
    return pin;
  }

  public void setPin(String pin) {
    this.pin = pin;
  }

  public String getCountry() {
    return country;
  }

  public void setCountry(String country) {
    this.country = country;
  }

  public String getState() {
    return state;
  }

  public void setState(String state) {
    this.state = state;
  }

  public String getDistrict() {
    return district;
  }

  public void setDistrict(String district) {
    this.district = district;
  }

  public String getDivision() {
    return division;
  }

  public void setDivision(String division) {
    this.division = division;
  }

  public String getCity() {
    return city;
  }

  public void setCity(String city) {
    this.city = city;
  }

  public List<String> getBlocks() {
    return blocks;
  }

  public void setBlocks(List<String> blocks) {
    this.blocks = blocks;
  }

  public List<String> getAreas() {
    return areas;
  }

  public void setAreas(List<String> areas) {
    this.areas = areas;
  }
}
