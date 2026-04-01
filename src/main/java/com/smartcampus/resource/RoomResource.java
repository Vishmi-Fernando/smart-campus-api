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

    // GET all rooms
    @GET
    public Collection<Room> getAllRooms() {
        return rooms.values();
    }

    // POST create room
    @POST
    public Room createRoom(Room room) {
        room.setId(idCounter++);
        rooms.put(room.getId(), room);
        return room;
    }

    // GET one room
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

    // DELETE room (IMPROVED VERSION)
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

        // Business rule (simulate sensors)
        boolean hasSensors = false;

        if (hasSensors) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Cannot delete room with active sensors")
                    .build();
        }

        // Delete
        rooms.remove(id);

        return Response.ok("Room deleted successfully").build();
    }
}