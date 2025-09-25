package repository;

import java.time.LocalDateTime;
import java.util.List;

import repository.modelo.Venta;

public interface VentaRepository {
    void crearVenta(Venta venta);
    Venta obtenerVentaPorId(Long id);
    List<Venta> obtenerTodasLasVentas();

    List<Venta> obtenerVentasPorFecha(LocalDateTime fecha);

    public List<Venta> obtenerVentasEntreFechas(LocalDateTime fechaInicio, LocalDateTime fechaFin);

}
