package com.example.neighbours;

public class Apartment {
    private String id;
    private long latitude;
    private long longitude;
    private int floor;
    private int rooms;
    private double area;
    private double price;
    private boolean waterboiler;
    private boolean ac;
    private boolean kosherkitchen;
    private double arnona;
    private double water;
    private double elctricity;
    private boolean elevator;
    private boolean pets;
    private int roomates;



    public Apartment(String id, long latitude, long longitude, int floor, int rooms, double area, double price, boolean waterboiler, boolean ac, boolean kosherkitchen, double arnona, double water, double elctricity, boolean elevator, boolean pets, int roomates) {
        this.id = id;
        this.latitude = latitude;
        this.longitude = longitude;
        this.floor = floor;
        this.rooms = rooms;
        this.area = area;
        this.price = price;
        this.waterboiler = waterboiler;
        this.ac = ac;
        this.kosherkitchen = kosherkitchen;
        this.arnona = arnona;
        this.water = water;
        this.elctricity = elctricity;
        this.elevator = elevator;
        this.pets = pets;
        this.roomates = roomates;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public long getLatitude() {
        return latitude;
    }

    public void setLatitude(long latitude) {
        this.latitude = latitude;
    }

    public long getLongitude() {
        return longitude;
    }

    public void setLongitude(long longitude) {
        this.longitude = longitude;
    }

    public int getFloor() {
        return floor;
    }

    public void setFloor(int floor) {
        this.floor = floor;
    }

    public int getRooms() {
        return rooms;
    }

    public void setRooms(int rooms) {
        this.rooms = rooms;
    }

    public double getArea() {
        return area;
    }

    public void setArea(double area) {
        this.area = area;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public boolean isWaterboiler() {
        return waterboiler;
    }

    public void setWaterboiler(boolean waterboiler) {
        this.waterboiler = waterboiler;
    }

    public boolean isAc() {
        return ac;
    }

    public void setAc(boolean ac) {
        this.ac = ac;
    }

    public boolean isKosherkitchen() {
        return kosherkitchen;
    }

    public void setKosherkitchen(boolean kosherkitchen) {
        this.kosherkitchen = kosherkitchen;
    }

    public double getArnona() {
        return arnona;
    }

    public void setArnona(double arnona) {
        this.arnona = arnona;
    }

    public double getWater() {
        return water;
    }

    public void setWater(double water) {
        this.water = water;
    }

    public double getElctricity() {
        return elctricity;
    }

    public void setElctricity(double elctricity) {
        this.elctricity = elctricity;
    }

    public boolean isElevator() {
        return elevator;
    }

    public void setElevator(boolean elevator) {
        this.elevator = elevator;
    }

    public boolean getPets() {
        return pets;
    }

    public void setPets(boolean pets) {
        this.pets = pets;
    }

    public int getRoomates() {
        return roomates;
    }

    public void setRoomates(int roomates) {
        this.roomates = roomates;
    }
}
