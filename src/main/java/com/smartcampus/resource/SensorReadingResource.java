package com.smartcampus.resource;

/**
 *
 * @author Vishmi
 */

import com.smartcampus.model.Sensor;
import com.smartcampus.model.SensorReading;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import java.util.*;

@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class SensorReadingResource {

    private int sensorId;

    private static Map<Integer, List<SensorReading>> readingsMap = new HashMap<>();
    private static int readingIdCounter = 1;

    public SensorReadingResource(int sensorId) {
        this.sensorId = sensorId;
    }

    // GET readings
    @GET
    public List<SensorReading> getReadings() {
        return readingsMap.getOrDefault(sensorId, new ArrayList<>());
    }

    // POST reading
    @POST
    public Response addReading(SensorReading reading) {

        Sensor sensor = SensorResource.getSensors().get(sensorId);

        if (sensor == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Sensor not found")
                    .build();
        }

        if (reading == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Invalid reading")
                    .build();
        }

        reading.setId(readingIdCounter++);

        readingsMap
                .computeIfAbsent(sensorId, k -> new ArrayList<>())
                .add(reading);

        sensor.setCurrentValue(reading.getValue());

        return Response.status(Response.Status.CREATED)
                .entity(reading)
                .build();
    }
}