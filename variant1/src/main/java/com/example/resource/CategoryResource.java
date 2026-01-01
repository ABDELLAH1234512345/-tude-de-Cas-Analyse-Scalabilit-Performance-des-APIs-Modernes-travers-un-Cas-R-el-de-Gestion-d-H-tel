package com.example.resource;

import com.example.model.Category;
import com.example.model.Item;
import com.example.service.CategoryService;
import com.example.service.ItemService;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * JAX-RS Resource for Category entity (Jersey Implementation).
 * <p>
 * This resource manages RESTful endpoints for categories. It manually
 * constructs
 * paginated responses to match the structure expected by the benchmark metrics,
 * simulating a Spring Data REST-like behavior in a thin JAX-RS layer.
 * </p>
 * 
 * @author Halmaoui Abdellah
 * @version 1.0
 * @since 2025
 */
@Path("/categories")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CategoryResource {

    /**
     * Service layer for category logic.
     */
    private final CategoryService categoryService;

    /**
     * Service layer for item logic (to support category/items sub-resource).
     */
    private final ItemService itemService;

    /**
     * Constructor initializing service layers.
     */
    public CategoryResource() {
        this.categoryService = new CategoryService();
        this.itemService = new ItemService();
    }

    /**
     * Retrieves a paginated list of categories.
     * 
     * @param page page number
     * @param size page size
     * @return 200 OK with paginated category data
     */
    @GET
    public Response getAllCategories(
            @QueryParam("page") @DefaultValue("0") int page,
            @QueryParam("size") @DefaultValue("10") int size) {

        List<Category> categories = categoryService.getAllCategories(page, size);
        long total = categoryService.countCategories();

        Map<String, Object> response = new HashMap<>();
        response.put("content", categories);
        response.put("totalElements", total);
        response.put("totalPages", (int) Math.ceil((double) total / size));
        response.put("number", page);
        response.put("size", size);

        return Response.ok(response).build();
    }

    /**
     * Retrieves a category by ID.
     * 
     * @param id category ID
     * @return 200 OK or 404 NOT FOUND
     */
    @GET
    @Path("/{id}")
    public Response getCategoryById(@PathParam("id") Long id) {
        Optional<Category> category = categoryService.getCategoryById(id);

        if (category.isPresent()) {
            return Response.ok(category.get()).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    /**
     * Retrieves items belonging to a specific category (sub-resource).
     * 
     * @param id   category ID
     * @param page page number
     * @param size page size
     * @return 200 OK with paginated item data, or 404 if category not found
     */
    @GET
    @Path("/{id}/items")
    public Response getItemsByCategoryId(
            @PathParam("id") Long id,
            @QueryParam("page") @DefaultValue("0") int page,
            @QueryParam("size") @DefaultValue("10") int size) {

        Optional<Category> category = categoryService.getCategoryById(id);
        if (!category.isPresent()) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        List<Item> items = itemService.getItemsByCategoryId(id, page, size);
        long total = itemService.countItemsByCategoryId(id);

        Map<String, Object> response = new HashMap<>();
        response.put("content", items);
        response.put("totalElements", total);
        response.put("totalPages", (int) Math.ceil((double) total / size));
        response.put("number", page);
        response.put("size", size);

        return Response.ok(response).build();
    }

    /**
     * Creates a new category.
     * 
     * @param category category data
     * @return 201 CREATED with the saved category
     */
    @POST
    public Response createCategory(Category category) {
        Category created = categoryService.createCategory(category);
        return Response.status(Response.Status.CREATED).entity(created).build();
    }

    /**
     * Updates an existing category.
     * 
     * @param id       category ID
     * @param category new category data
     * @return 200 OK or 404 NOT FOUND
     */
    @PUT
    @Path("/{id}")
    public Response updateCategory(@PathParam("id") Long id, Category category) {
        try {
            Category updated = categoryService.updateCategory(id, category);
            return Response.ok(updated).build();
        } catch (RuntimeException e) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    /**
     * Deletes a category.
     * 
     * @param id category ID
     * @return 204 NO CONTENT or 404 NOT FOUND
     */
    @DELETE
    @Path("/{id}")
    public Response deleteCategory(@PathParam("id") Long id) {
        try {
            categoryService.deleteCategory(id);
            return Response.noContent().build();
        } catch (RuntimeException e) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }
}
