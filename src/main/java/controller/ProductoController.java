package controller;

import java.util.List;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import service.ProductoService;
import service.dto.ProductoDto;

@ApplicationScoped
@Path("/productos")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ProductoController {

    @Inject
    private ProductoService productoService;

    // guardar
    @POST
    public Response crearProducto(ProductoDto productoDto) {
        try {
            productoService.crearProducto(productoDto);
            return Response.status(Response.Status.CREATED).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error al crear el producto: " + e.getMessage()).build();
        }
    }

    // actualizar
    @PUT
    @Path("/{id}")
    public Response actualizarProducto(@PathParam("id") Long id, ProductoDto productoDto) {
        try {
            productoService.actualizarProducto(id, productoDto);
            return Response.status(Response.Status.OK).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error al actualizar el producto: " + e.getMessage()).build();
        }
    }

    // eliminar
    @DELETE
    @Path("/{id}")
    public Response eliminarProducto(@PathParam("id") Long id) {
        try {
            productoService.eliminarProducto(id);
            return Response.status(Response.Status.NO_CONTENT).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error al eliminar el producto: " + e.getMessage()).build();
        }
    }

    // obtener por nombre
    @GET
    @Path("/nombre/{nombre}")
    public Response obtenerProductoPorNombre(@PathParam("nombre") String nombre) {
        try {
            ProductoDto productoDto = productoService.obtenerProductoDtoPorNombre(nombre);
            if (productoDto != null) {
                return Response.ok(productoDto).build();
            } else {
                return Response.status(Response.Status.NOT_FOUND).build();
            }
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error al obtener el producto: " + e.getMessage()).build();
        }
    }

    // obtener todos por categoria
    @GET
    @Path("/categoria/{categoria}")
    public Response obtenerTodosLosProductosPorCategoria(@PathParam("categoria") String categoria) {
        try {
            return Response.ok(productoService.obtenerTodosLosProductosPorCategoria(categoria)).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error al obtener productos por categoría: " + e.getMessage()).build();
        }
    }

    // obtener todos por marca
    @GET
    @Path("/marca/{marca}")
    public Response obtenerTodosLosProductosPorMarca(@PathParam("marca") String marca) {
        try {
            return Response.ok(productoService.obtenerTodosLosProductosPorMarca(marca)).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error al obtener productos por marca: " + e.getMessage()).build();
        }
    }

    // obtener todos
    @GET
    public Response obtenerTodosLosProductos() {
        try {
            return Response.ok(productoService.obtenerTodosLosProductos()).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error al obtener todos los productos: " + e.getMessage()).build();
        }
    }

    // obtener por marca y categoria
    @GET
    @Path("/marca_categoria")
    public Response obtenerProductosPorMarcaYCategoria(@QueryParam("marca") String marca,
            @QueryParam("categoria") String categoria) {
        try {
            return Response.ok(productoService.obtenerProductosPorMarcaYCategoria(marca, categoria)).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error al obtener productos por marca y categoría: " + e.getMessage()).build();
        }
    }

    @GET
    @Path("/buscar")
    @Produces(MediaType.APPLICATION_JSON)
    public Response buscarProductos(@QueryParam("nombre") String nombre) {
        if (nombre == null || nombre.isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Debe enviar un nombre para buscar.").build();
        }
        List<ProductoDto> productos = productoService.findByNombreLike(nombre);
        return Response.ok(productos).build();
    }

}
