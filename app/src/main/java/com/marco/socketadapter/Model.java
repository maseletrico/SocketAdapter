package com.marco.socketadapter;

public class Model {

    //Adapter viriable
    private String label_A;
    private String label_B;
    private String applianceA;
    private String applianceB;
    private int socketA_On;
    private int socketB_On;
    private int tagB_On;
    private int tagA_On;
    private int tagTR_On;
    private int tamperResitant;
    private int lockedTimeOut;
    private String temperature;
    //Tag viriable
    private String tagID;
    private String tagApplianceName;
    private String tagLock;
    private String tagTimeout;

    public Model(){

    }
    //Adater getting/settings
    public String getLabel_A() {
        return label_A;
    }
    public void setLabel_A(String labelA){
        this.label_A = labelA;
    }

    public String getLabel_B() {
        return label_B;
    }
    public void setLabel_B(String labelB){
        this.label_B = labelB;
    }



    public void setTagA_On(int tagA_On) {
        this.tagA_On = tagA_On;
    }
    public int getTagA_On() {
        return tagA_On;
    }

    public void setTagB_On(int tagB_On) {
        this.tagB_On = tagB_On;
    }
    public int getTagtB_On() {
        return tagB_On;
    }

    public void setTagTR_On(int tagTR_On) {
        this.tagTR_On = tagTR_On;
    }
    public int getTagTR_On() {
        return tagTR_On;
    }


    public void setSocketA_On(int socketA_On) {
        this.socketA_On = socketA_On;
    }
    public int isSocketA_On() {
        return socketA_On;
    }

    public void setSocketB_On(int socketB_On) {
        this.socketB_On = socketB_On;
    }
    public int isSocketB_On() {
        return socketB_On;
    }



    public void setTamperResitant(int tamperResitant) {
        this.tamperResitant = tamperResitant;
    }
    public int isTamperResitant() {
        return tamperResitant;
    }

    public void setLockedTimeOut(int lockedTimeOut) {
        this.lockedTimeOut = lockedTimeOut;
    }
    public int isLockedTimeOut() {
        return lockedTimeOut;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }
    public String getTemperature() {
        return temperature;
    }

    public String getApplianceA() {
        return applianceA;
    }
    public void setApplianceA(String appliance_A){
        this.applianceA = appliance_A;
    }

    public String getApplianceB() {
        return applianceB;
    }
    public void setApplianceB(String appliance_B){
        this.applianceB = appliance_B;
    }

    //TAG getting/settings
    public String getTagID() {
        return tagID;
    }
    public void setTagID(String tag_ID){
        this.tagID= tag_ID;
    }

    public String getTagApplianceName() {
        return tagApplianceName;
    }
    public void setTagApplianceName(String tag_ApplianceName){
        this.tagApplianceName= tag_ApplianceName;
    }

    public String getTagLock() {
        return tagLock;
    }
    public void setTagLock(String tag_Lock){
        this.tagLock= tag_Lock;
    }

    public String getTagTimeout() {
        return tagTimeout;
    }
    public void setTagTimeout(String tag_timeout){
        this.tagTimeout= tag_timeout;
    }
}
