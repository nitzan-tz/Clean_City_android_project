package com.example.signinpage;

public class User
{
    private String Id;
    private String Name;
    private String Address;
    private String Phone;
    private String Email;

    public User(String id, String name, String address, String phone, String email) {
        Id = id;
        Name = name;
        Address = address;
        Phone = phone;
        Email = email;
    }

    public User() {
    }
    public String getName() {
        return Name;
    }
    public void setName(String name) {
        Name = name;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }
}
