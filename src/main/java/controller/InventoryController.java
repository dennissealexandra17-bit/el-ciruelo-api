package controller;

import java.util.List;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import service.InventarioService;
import service.dto.InventarioDto;

@Path("/inventario")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class InventoryController {

    @Inject
    private InventarioService inventarioService;

    @GET
    @Path("/bajo-stock")
    public Response obtenerInventariosConStockBajo() {
        List<InventarioDto> inventarios = inventarioService.obtenerInventariosConStockBajo();
        return Response.ok(inventarios).build();
    }

    @GET
    @Path("/{productoId}")
    public Response obtenerInventarioPorProductoId(@PathParam("productoId") Long productoId) {
        InventarioDto inventario = inventarioService.obtenerInventarioPorProductoId(productoId);
        if (inventario == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(inventario).build();
    }

    @POST
    public Response crearInventario(InventarioDto inventarioDto) {
        inventarioService.crearInventario(inventarioDto);
        return Response.status(Response.Status.CREATED).build();
    }

    @PUT
    @Path("/{productoId}")
    public Response actualizarInventario(@PathParam("productoId") Long productoId, InventarioDto inventarioDto) {
        try {
            inventarioService.actualizarInventario(productoId, inventarioDto);
            return Response.ok().build();
        } catch (RuntimeException e) {
            return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
        }
    }

    @DELETE
    @Path("/{productoId}")
    public Response eliminarInventario(@PathParam("productoId") Long productoId) {
        inventarioService.eliminarInventario(productoId);
        return Response.noContent().build();
    }
}
