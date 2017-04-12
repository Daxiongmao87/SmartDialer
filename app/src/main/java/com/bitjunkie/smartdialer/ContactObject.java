package com.bitjunkie.smartdialer;

/**
 * Created by Cameron on 4/11/2017.
 */

import java.util.ArrayList;

class ContactObject {
    String firstName;
    String lastName;
    String descriptor;

    ArrayList<String> phoneNum = new ArrayList();
    ArrayList<String> numLabel = new ArrayList();

    ArrayList<String> address = new ArrayList();
    ArrayList<String> addressLabel = new ArrayList();

    ArrayList<String> email = new ArrayList();
    ArrayList<String> emailLabel = new ArrayList();
    String photoDir;
    int speedDial;

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
        numLabel.add(numLabel.size, label);
        phoneNum.add(phoneNum.size, number);
    }
    public void RemoveNum(int index) {

    }
    public void editNum(int index, String newNum) {

    }
    public void editNumLabel(int index, String newLabel) {

    }

    // Address

    // Email

    // speedDial

}
