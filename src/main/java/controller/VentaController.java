package controller;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import service.VentaService;
import service.dto.VentaDto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.List;

@Path("/ventas")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class VentaController {

    @Inject
    private VentaService ventaService;

    // Crear una nueva venta
    @POST
    public Response crearVenta(VentaDto ventaDto) {
        try {
            ventaService.crearVenta(ventaDto);
            return Response.status(Response.Status.CREATED).build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(e.getMessage())
                    .build();
        }
    }

    // Obtener una venta por su ID
    @GET
    @Path("/{id}")
    public Response obtenerVentaPorId(@PathParam("id") Long id) {
        VentaDto venta = ventaService.obtenerVentaPorId(id);
        if (venta != null) {
            return Response.ok(venta).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Venta no encontrada con id: " + id)
                    .build();
        }
    }

    // Obtener todas las ventas
    @GET
    public Response obtenerTodasLasVentas() {
        List<VentaDto> ventas = ventaService.obtenerTodasLasVentas();
        return Response.ok(ventas).build();
    }

    @GET
    @Path("/fecha/{fecha}")
    public Response obtenerVentasPorFecha(@PathParam("fecha") String fechaStr) {
        try {
            LocalDate fecha = LocalDate.parse(fechaStr); // yyyy-MM-dd
            LocalDateTime fechaDateTime = fecha.atStartOfDay(); // convertir a LocalDateTime
            List<VentaDto> ventas = ventaService.obtenerVentasPorFecha(fechaDateTime);
            return Response.ok(ventas).build();
        } catch (DateTimeParseException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Formato de fecha inválido. Usa yyyy-MM-dd")
                    .build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(e.getMessage())
                    .build();
        }
    }

    @GET
    @Path("/rango")
    public Response obtenerVentasPorRango(
            @QueryParam("inicio") String inicioStr,
            @QueryParam("fin") String finStr) {
        try {
            LocalDate inicio = LocalDate.parse(inicioStr);
            LocalDate fin = LocalDate.parse(finStr);
            LocalDateTime fechaInicio = inicio.atStartOfDay();
            LocalDateTime fechaFin = fin.atTime(23, 59, 59);

            List<VentaDto> ventas = ventaService.obtenerVentasEntreFechas(fechaInicio, fechaFin);
            return Response.ok(ventas).build();
        } catch (DateTimeParseException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Formato de fecha inválido. Usa yyyy-MM-dd")
                    .build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(e.getMessage())
                    .build();
        }
    }

}
