package com.carolinawings.webapp.dto;

public class UserResponseDTO
{
    private String id;
    private String name;
    private String email;
    private String phoneNumber;
    private String newsletterMember;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getNewsletterMember() {
        return newsletterMember;
    }

    public void setNewsletterMember(String newsletterMember) {
        this.newsletterMember = newsletterMember;
    }
}
