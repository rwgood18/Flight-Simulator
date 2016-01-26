package com.cooksys.mvc;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cooksys.core.beans.FlightGenerator;
import com.cooksys.core.models.FlightModel;
import com.cooksys.core.models.Plane;


@Controller
public class FlightModelController {
	
	@Autowired
	private FlightGenerator flightGen;
	
	
	@RequestMapping(value = "/getFlightModel", method = RequestMethod.GET)
	public @ResponseBody FlightModel getFlightModel(){
		FlightModel flightModel = new FlightModel();
		
		flightModel.setCurrentDay(flightGen.getCurrentDay());
		
		Long time = System.currentTimeMillis();
		Long fromStart = time - flightGen.getStartTime();
		Long nextDay = fromStart - (flightModel.getCurrentDay() * 30000);
		
		flightModel.setSecondsTillNextDay(nextDay);
		
		flightModel.setFlights(new ArrayList<>());
		
		for(Plane plane : flightGen.getAllPlanes()){
			flightModel.getFlights().addAll(plane.getFlightDeque());
		}
		
		return flightModel;
	}

}
