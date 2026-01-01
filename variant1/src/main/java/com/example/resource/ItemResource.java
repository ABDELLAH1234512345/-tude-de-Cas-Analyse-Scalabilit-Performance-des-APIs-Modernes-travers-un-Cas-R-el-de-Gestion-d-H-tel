package com.example.resource;

import com.example.model.Item;
import com.example.service.ItemService;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * JAX-RS Resource for Item entity (Jersey Implementation).
 * <p>
 * This resource manages RESTful endpoints for items. It supports basic CRUD
 * operations and filtering by category. Responses are manually bundled into
 * maps to provide pagination metadata in the JSON output.
 * </p>
 * 
 * @author Halmaoui Abdellah
 * @version 1.0
 * @since 2025
 */
@Path("/items")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ItemResource {

    /**
     * Service layer for item business logic.
     */
    private final ItemService itemService;

    /**
     * Constructor initializing the item service.
     */
    public ItemResource() {
        this.itemService = new ItemService();
    }

    /**
     * Retrieves a paginated list of items, optionally filtered by category.
     * 
     * @param page       page number
     * @param size       page size
     * @param categoryId optional category filter ID
     * @return 200 OK with paginated result
     */
    @GET
    public Response getAllItems(
            @QueryParam("page") @DefaultValue("0") int page,
            @QueryParam("size") @DefaultValue("10") int size,
            @QueryParam("categoryId") Long categoryId) {

        List<Item> items;
        long total;

        if (categoryId != null) {
            items = itemService.getItemsByCategoryId(categoryId, page, size);
            total = itemService.countItemsByCategoryId(categoryId);
        } else {
            items = itemService.getAllItems(page, size);
            total = itemService.countItems();
        }

        Map<String, Object> response = new HashMap<>();
        response.put("content", items);
        response.put("totalElements", total);
        response.put("totalPages", (int) Math.ceil((double) total / size));
        response.put("number", page);
        response.put("size", size);

        return Response.ok(response).build();
    }

    /**
     * Retrieves a single item by ID.
     * 
     * @param id item ID
     * @return 200 OK or 404 NOT FOUND
     */
    @GET
    @Path("/{id}")
    public Response getItemById(@PathParam("id") Long id) {
        Optional<Item> item = itemService.getItemById(id);

        if (item.isPresent()) {
            return Response.ok(item.get()).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    /**
     * Creates a new item.
     * 
     * @param item item data
     * @return 201 CREATED with the saved item
     */
    @POST
    public Response createItem(Item item) {
        Item created = itemService.createItem(item);
        return Response.status(Response.Status.CREATED).entity(created).build();
    }

    /**
     * Updates an existing item.
     * 
     * @param id   item ID
     * @param item new item data
     * @return 200 OK or 404 NOT FOUND
     */
    @PUT
    @Path("/{id}")
    public Response updateItem(@PathParam("id") Long id, Item item) {
        try {
            Item updated = itemService.updateItem(id, item);
            return Response.ok(updated).build();
        } catch (RuntimeException e) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    /**
     * Deletes an item.
     * 
     * @param id item ID
     * @return 204 NO CONTENT or 404 NOT FOUND
     */
    @DELETE
    @Path("/{id}")
    public Response deleteItem(@PathParam("id") Long id) {
        try {
            itemService.deleteItem(id);
            return Response.noContent().build();
        } catch (RuntimeException e) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }
}
