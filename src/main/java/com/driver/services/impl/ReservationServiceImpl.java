package com.driver.services.impl;

import com.driver.model.*;
import com.driver.repository.ParkingLotRepository;
import com.driver.repository.ReservationRepository;
import com.driver.repository.SpotRepository;
import com.driver.repository.UserRepository;
import com.driver.services.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReservationServiceImpl implements ReservationService {
    @Autowired
    UserRepository userRepository3;
    @Autowired
    SpotRepository spotRepository3;
    @Autowired
    ReservationRepository reservationRepository3;
    @Autowired
    ParkingLotRepository parkingLotRepository3;

    public SpotType getSpotTypeByWheels(int numberOfWheels){
        if(numberOfWheels<=2){
            return  SpotType.TWO_WHEELER;
        }

        else if (numberOfWheels==4 || numberOfWheels==3) {
           return  SpotType.FOUR_WHEELER;

        }
            return  SpotType.OTHERS;
    }
    @Override
    public Reservation reserveSpot(Integer userId, Integer parkingLotId, Integer timeInHours, Integer numberOfWheels) throws Exception {
      Reservation newReservation = new Reservation();
      try {
          newReservation.setUser(userRepository3.findById(userId).get());
          ParkingLot parkingLot=parkingLotRepository3.findById(parkingLotId).get();

          List<Spot> spotList =parkingLot.getSpotList();
         Spot spot1=new Spot();
         int totalPrice=Integer.MAX_VALUE;
          for(Spot spot:spotList){
             int spotPrice=spot.getPricePerHour()*timeInHours;

              if(!spot.getOccupied() && spot.getSpotType()==getSpotTypeByWheels(numberOfWheels) && totalPrice>spotPrice){
                  spot1=spot;
                  totalPrice=spotPrice;
              }
          }
          newReservation.setSpot(spot1);

          newReservation.setNoOfHours(timeInHours);

          reservationRepository3.save(newReservation);

          return newReservation;

      }catch(Exception e){
          throw new Exception("Cannot make reservation");
      }

    }
}
