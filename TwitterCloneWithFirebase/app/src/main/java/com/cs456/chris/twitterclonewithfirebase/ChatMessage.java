package com.cs456.chris.twitterclonewithfirebase;


/**
 * Created by Chris on 1/31/2016.
 */
public class ChatMessage {
    private String name;
    private String text;
    private String latitude;
    private String longitude;
    private String inReplyTo;
    private String other;
    private String unknown;
    private String message;
    private String sender;
    private String timeStamp;

    public ChatMessage(){

    }

    public ChatMessage(String message){
        this.message = message;
    }

    public String getName(){
        return name;
    }

    public String getText(){
        return text;
    }

    public String getLatitude(){
        return latitude;
    }

    public String getOther(){
        return other;
    }

    public String getUnknown(){
        return unknown;
    }

    public String getInReplyTo(){
        return inReplyTo;
    }

    public String getLongitude(){
        return longitude;
    }

    public String getMessage(){
        return message;
    }

    public String getSender(){
        return sender;
    }

    public String getTimeStamp(){
        return timeStamp;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public void setInReplyTo(String inReplyTo) {
        this.inReplyTo = inReplyTo;
    }

    public void setOther(String other) {
        this.other = other;
    }

    public void setUnknown(String unknown) {
        this.unknown = unknown;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }
}
