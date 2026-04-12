package com.smartcampus.resource;

/**
 *
 * @author Vishmi
 */

import com.smartcampus.model.Room;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import java.util.*;

@Path("/rooms")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class RoomResource {

    private static Map<Integer, Room> rooms = new HashMap<>();
    private static int idCounter = 1;

    //  Allow other resources (SensorResource) to access rooms
    public static Map<Integer, Room> getRooms() {
        return rooms;
    }

    //  GET all rooms
    @GET
    public Collection<Room> getAllRooms() {
        return rooms.values();
    }

    //  POST create room (IMPROVED)
    @POST
    public Response createRoom(Room room) {

        if (room == null || room.getName() == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Invalid room data")
                    .build();
        }

        room.setId(idCounter++);
        rooms.put(room.getId(), room);

        return Response.status(Response.Status.CREATED)
                .entity(room)
                .build();
    }

    //  GET one room
    @GET
    @Path("/{id}")
    public Response getRoom(@PathParam("id") int id) {

        Room room = rooms.get(id);

        if (room == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Room not found")
                    .build();
        }

        return Response.ok(room).build();
    }

    //  DELETE room (READY FOR SENSOR VALIDATION LATER)
    @DELETE
    @Path("/{id}")
    public Response deleteRoom(@PathParam("id") int id) {

        Room room = rooms.get(id);

        // Room not found
        if (room == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Room not found")
                    .build();
        }

        //  Placeholder for Part 4/5 sensor validation
        boolean hasSensors = false;

        if (hasSensors) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Cannot delete room with active sensors")
                    .build();
        }

        // Delete room
        rooms.remove(id);

        return Response.ok("Room deleted successfully").build();
    }
}