package com.smartcampus.model;

/**
 *
 * @author Vishmi
 */

public class Sensor {

    private int id;
    private String type;
    private int roomId;

    public Sensor() {}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

        public int getRoomId() {
        return roomId;
    }

    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }
}