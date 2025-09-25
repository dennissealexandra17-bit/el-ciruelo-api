package service;

import java.time.LocalDateTime;
import java.util.List;

import service.dto.VentaDto;

public interface VentaService {
    void crearVenta(VentaDto venta);
    VentaDto obtenerVentaPorId(Long id);
    List<VentaDto> obtenerTodasLasVentas();

    List<VentaDto> obtenerVentasPorFecha(LocalDateTime fecha);

    List<VentaDto> obtenerVentasEntreFechas(LocalDateTime fechaInicio, LocalDateTime fechaFin);
}
