package com.example.codexist;


import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;


@RestController
@CrossOrigin("https://codexisttest2.s3.amazonaws.com/index.html?#/close-buy")
public class AddressController {

  @Autowired
  private AddressService service;
   static int test=1;
   static double lat=11;
   static String places = "";
   //TODO make hashmap key consists of more than one key
  static HashMap<String, String> placesHash = new HashMap<String, String>();// (lat - api response)

  @GetMapping
  public String home(){
    return " This is home";
  }

  @GetMapping("/addressing")
  @CrossOrigin("https://codexisttest2.s3.amazonaws.com/index.html?#/close-buy")
  @ResponseBody
    public String getAddress(@RequestParam String lat){

    if(service.getAddressByLat(lat)!=null){
      System.out.println("GET request is made, this is request is present in the DB. ");
      return "yes";
    }
    else{
      System.out.println(" GET request is made, this is a new request. A request to Google API is going to be made.");
      return "no";
    }

  }


  @PostMapping("/addressing/register")
  @CrossOrigin(origins = "https://codexisttest2.s3.amazonaws.com/index.html?#/close-buy", maxAge = 3600)
  public String register(@RequestBody AddressPlacesArrayEntity ad) throws JsonProcessingException {

    if(service.getAddressByLat(ad.lat)==null){
      System.out.println("New key arrived (NEW REQUEST)");
      System.out.println("Key is being inserted to the DB");
      service.saveAddress(ad);
      System.out.println("New address successfully inserted to the DB");
      return "Inserted to DB";
    }
    else{
      System.out.println("Key is already in DB");
      System.out.println("Lat value of the current address ... "+ad.lat);
      System.out.println("Latitude is got from DB and it is  ..."+service.getAddressByLat(ad.lat).lat);
      return "Lat key is already in DB";
    }


  }
  @GetMapping("/addressing/getFromDB")
  @CrossOrigin(origins = "https://codexisttest2.s3.amazonaws.com/index.html?#/close-buy", maxAge = 3600)
  @ResponseBody
  public String getAddressFromDB(@RequestParam String lat){

    return service.getAddressByLat(lat).places;
  }
}

