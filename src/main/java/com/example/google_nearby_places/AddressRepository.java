package com.example.codexist;

import org.springframework.data.repository.CrudRepository;

public interface AddressRepository extends CrudRepository<AddressPlacesArrayEntity,Integer> {


    Object findByLat(String lat);
    Object findByLatAndLong(String lat, String lng);


}
