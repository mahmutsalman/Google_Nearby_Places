<template>
  <div class="ui grid">
    <div class="six wide column red">
      <form class="ui segment large form">
        <div class="ui message red" v-show="error">{{error}}</div>
        <div class="ui segment">
          <div class="field">

          </div>
          <input type="text" placeholder="Lat" v-model="latInput" />
          <input type="text" placeholder="Long" v-model="lngInput" />
          <button class="ui button" @click="findCloseBuyButtonPressed">Find near by places</button>
        </div>

        <div class="field">
          <div class="two fields">
            <div class="field">
              <select v-model="type">
                <option value="restaurant">Restaurant</option>
                <option value="restaurant">Library</option>
                <option value="restaurant">Mosque</option>

              </select>
            </div>
            <div class="field">
              <select v-model="radius">
                <option value="5">5 km</option>
                <option value="10">10 km</option>
                <option value="15">15 km</option>
                <option value="20">20 km</option>
              </select>
            </div>
          </div>
        </div>
      </form>
      <div class="ui segment">
        <div class="ui divided items">
          <div class="item" v-for="place in places" :key="place.id">
            <div class="content">
              <div class="header">{{place.name}}</div>
              <div class="meta">{{place.vicinity}}</div>
            </div>
          </div>
        </div>
      </div>
    </div>
    <div class="ten wide column blue" ref="map"></div>
  </div>
</template>

<script>
import axios from 'axios'

export default {
  data() {
    return {
      address: "",
      latInput: "",
      lngInput: "",
      error: "",
      spinner:false,
      apiKey:"API-KEY",
      lat:0,
      lng:0,
      type:'',
      radius:0,
      places:[],
      jsonObj:"",
      controlFlag:0,

    }
  },
  methods: {

    getAddressFrom(lat,long){

      axios.get("https://maps.googleapis.com/maps/api/geocode/json?latlng="
        + lat +
        ","
        + long
        + "&key=" + this.apiKey)
        .then(response =>{
          if(response.data.error_message){
            console.log(response.data.error_message);
          }else{
            this.address = response.data.results[0].formatted_address;
            // console.log(response.data.results[0].formatted_address);
          }
        })
        .catch(error => {
          console.log(error.message)
        })
    },
     findCloseBuyButtonPressed() {
      const URL = `YOUR-FRONT-END-URL`;
      const URL2 = `https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=${
         this.latInput
       },${this.lngInput}&type=${this.type}&radius=${this.radius * 1000}&key=${
         this.apiKey
       }`;

      console.log("User input for lat: " +this.latInput);
      console.log("User input for lng: " +this.lngInput);

       //TODO first check if it is in the backend
       //TODO fill the this.place from backend
       console.log("Checking if it is in the backend...");
       axios
         .get(URL + '?'+"lat="+this.latInput,{
           header: {
             "Access-Control-Allow-Origin": "*",
             "Access-Control-Allow-Methods": "GET, POST, PATCH, PUT, DELETE, OPTIONS",
             "Access-Control-Allow-Headers": "Origin, Content-Type, X-Auth-Token"
           }
         })
         .then(response => {
           console.log("If 'yes' the message is already in the Database, if not, it is not in the Database");
           console.log("The response is : "+response)
           //TODO if upcoming request is new(response is "") then make a call to google api
            if(response.data == "no"){
              console.log("The response is no, so it is not in the Database. Making a call to google api...");
              axios
                .get(URL2,{
                  header: {
                    "Access-Control-Allow-Origin": "*",
                    "Access-Control-Allow-Methods": "GET, POST, PATCH, PUT, DELETE, OPTIONS",
                    "Access-Control-Allow-Headers": "Origin, Content-Type, X-Auth-Token"
                  }
                })
                .then(response => {
                  this.places = response.data.results;
                  this.showPlacesOnMap();
                  this.jsonObj = JSON.stringify(this.places);

                  // console.log(this.jsonObj);
                  //TODO Make POST request to backend and save this.places in there
                  axios.post('BACK-END-ADDRESS/addressing/register',{
                    places: this.jsonObj,
                    lng: this.lngInput,
                    lat:this.latInput
                    },
                    {
                      "Access-Control-Allow-Origin": "*",
                      "Access-Control-Allow-Methods": "GET, POST, PATCH, PUT, DELETE, OPTIONS",
                      "Access-Control-Allow-Headers": "Origin, Content-Type, X-Auth-Token"
                    }
                  ).then(response => {
                    console.log(response);
                  })

                }).catch(error => {
                this.error = error.message;
              })
            }else{
              //if the request info coming from database
              console.log("The response is yes, so it is in the Database");
              // GET from database
              axios.get(`BACK-END-ADDRESS/addressing/getFromDB?lat=`+this.latInput)
                .then(response =>{
                  if(response.data.error_message){
                    console.log(response.data.error_message);
                  }else{
                    console.log("Information is coming from the  Database.");
                    this.places=response.data;
                    this.showPlacesOnMap();
                  }
                })
                .catch(error => {
                  console.log(error.message)
                })

            }
         }).catch(error => {
         this.error = error.message;
       })

    },

    showPlacesOnMap(){

      const mapDisplayElement = document.getElementById('map');

      const map = new google.maps.Map(
        this.$refs["map"],
        {
          zoom: 15,
          center: new google.maps.LatLng(this.latInput, this.lngInput),
          mapTypeId: google.maps.MapTypeId.ROADMAP
        }
      );

      for(let i = 0; i<this.places.length; i++){
        const lat = this.places[i].geometry.location.lat;
        const lng =  this.places[i].geometry.location.lng;

        const marker =  new google.maps.Marker({
          position: new google.maps.LatLng(lat,lng),
          map: map
        })
      }
    }
  }
}
</script>
<style>
.ui.button{
  background-color: red;
  color: wheat;
}
</style>
