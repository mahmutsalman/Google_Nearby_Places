package com.example.codexist;

//import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "ADDRESS_TBL")
public class AddressPlacesArrayEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    String places;
    String lat;
    String lng;
//    String business_status;
//    String locationLat;
//    String locationLng;

    public AddressPlacesArrayEntity(String places, String lat, String lng) {
        this.places=places;
        this.lat = lat;
        this.lng = lng;
    }

    public void setPlaces(String places) {
        this.places = places;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }


}
