package com.bitjunkie.smartdialer;

/**
 * Created by Cameron on 4/11/2017.
 */

import java.util.ArrayList;

class ContactObject {
    private String firstName;
    private String lastName;
    private String descriptor;

    private ArrayList<String> phoneNum = new ArrayList();
    private ArrayList<String> numLabel = new ArrayList();

    private ArrayList<String> address = new ArrayList();
    private ArrayList<String> addressLabel = new ArrayList();

    private ArrayList<String> email = new ArrayList();
    private ArrayList<String> emailLabel = new ArrayList();
    private String photoDir;
    private int speedDial;

    public ContactObject () {

    }

    // First Name
    public String FirstName() {
        return (firstName);
    }
    public void ChangeFirstName(String newName) {
        firstName = newName;
    }

    // Last Name
    public String LastName() {
        return (lastName);
    }
    public void ChangeLastName(String newName) {
        lastName = newName;
    }

    // Descriptor
    public String Descriptor() {
        return (descriptor);
    }
    public void ChangeDescriptor(String newDescriptor) {
        descriptor = newDescriptor;
    }

    // Phone Number
    public String PhoneNum(int index) {
        return (phoneNum.get(index));
    }
    public String NumLabel(int index) {
        return (numLabel.get(index));
    }
    public void AddNum(String label, String number) {
        numLabel.add(numLabel.size(), label);
        phoneNum.add(phoneNum.size(), number);
    }
    public void RemoveNum(int index) {
        phoneNum.remove(index);
        numLabel.remove(index);
    }
    public void editNum(int index, String newNum) {
        phoneNum.set(index, newNum);
    }
    public void editNumLabel(int index, String newLabel) {
        numLabel.set(index, newLabel);
    }

    // Address
    public String Address(int index) {
        return (address.get(index));
    }
    public String AddressLabel(int index) {
        return (addressLabel.get(index));
    }
    public void AddAddress(String label, String newAddress) {
        addressLabel.add(addressLabel.size(), label);
        address.add(address.size(), newAddress);
    }
    public void RemoveAddress(int index) {
        address.remove(index);
        addressLabel.remove(index);
    }
    public void editAddress(int index, String newAddress) {
        address.set(index, newAddress);
    }
    public void editAddressLabel(int index, String newLabel) {
        addressLabel.set(index, newLabel);
    }

    // Email
    public String email(int index) {
        return (address.get(index));
    }
    public String emailLabel(int index) {
        return (addressLabel.get(index));
    }
    public void AddEmail(String label, String newEmail) {
        emailLabel.add(emailLabel.size(), label);
        email.add(email.size(), newEmail);
    }
    public void RemoveEmail(int index) {
        email.remove(index);
        emailLabel.remove(index);
    }
    public void editEmail(int index, String newAddress) {
        email.set(index, newAddress);
    }
    public void editEmailLabel(int index, String newLabel) {
        emailLabel.set(index, newLabel);
    }

    // speedDial
    public int speedDial() {
        return speedDial;
    }
    public void editSpeedDial(int n) {
        speedDial = n;
    }
}
