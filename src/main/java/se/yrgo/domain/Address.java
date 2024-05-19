package se.yrgo.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
@Embeddable
public class Address {
    @Column(length = 40)

    private String street;
    @Column(length = 40)

    private String city;
    @Column(length = 30)

    private String zipcode;

    public Address() {}

    public Address(String street, String city, String zipcode) {
        this.street = street;
        this.city = city;
        this.zipcode = zipcode;
    }

    @Override
    public String toString() {
        return street + " " + city + " " + zipcode;
    }
}
