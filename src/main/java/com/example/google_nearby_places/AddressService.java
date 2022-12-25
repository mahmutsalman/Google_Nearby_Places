package com.example.codexist;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AddressService {

    @Autowired
    private AddressRepository repository;

    public AddressPlacesArrayEntity saveAddress(AddressPlacesArrayEntity address){
        return repository.save(address);
    }
    public List<AddressPlacesArrayEntity> saveAddress(List<AddressPlacesArrayEntity> addresses){
        return (List<AddressPlacesArrayEntity>) repository.saveAll(addresses);
    }


    //For GET methods
    public List<AddressPlacesArrayEntity> getAddresses(){

        return (List<AddressPlacesArrayEntity>) repository.findAll();
    }
    public AddressPlacesArrayEntity getAddressById(int id){

        return repository.findById(id).orElse(null);
    }
    public AddressPlacesArrayEntity getAddressByLat(String lat){
        return (AddressPlacesArrayEntity) repository.findByLat(lat);
    }

    // For DELETE
    public String deleteAddress(int id){
        repository.deleteById(id);
        return "Address is deleted : "+id;
    }

}
