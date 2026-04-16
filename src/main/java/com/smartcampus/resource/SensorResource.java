package com.smartcampus.resource;

/**
 *
 * @author Vishmi
 */
import com.smartcampus.model.Sensor;
import com.smartcampus.model.Room;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import java.util.*;

@Path("/sensors")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class SensorResource {

    private static Map<Integer, Sensor> sensors = new HashMap<>();
    private static int idCounter = 1;

    public static Map<Integer, Sensor> getSensors() {
        return sensors;
    }

    // CREATE SENSOR
    @POST
    public Response createSensor(Sensor sensor) {

        Map<Integer, Room> rooms = RoomResource.getRooms();

        if (sensor == null || !rooms.containsKey(sensor.getRoomId())) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Invalid sensor or room does not exist")
                    .build();
        }

        sensor.setId(idCounter++);
        sensors.put(sensor.getId(), sensor);

        return Response.status(Response.Status.CREATED)
                .entity(sensor)
                .build();
    }

    // GET SENSORS (WITH FILTER)
    @GET
    public Collection<Sensor> getSensors(@QueryParam("type") String type) {

        if (type == null) {
            return sensors.values();
        }

        List<Sensor> filtered = new ArrayList<>();

        for (Sensor s : sensors.values()) {
            if (s.getType().equalsIgnoreCase(type)) {
                filtered.add(s);
            }
        }

        return filtered;
    }

    //  SUB-RESOURCE LOCATOR
    @Path("/{id}/readings")
    public SensorReadingResource getReadingResource(@PathParam("id") int id) {

        if (!sensors.containsKey(id)) {
            throw new NotFoundException("Sensor not found");
        }

        return new SensorReadingResource(id);
    }
}