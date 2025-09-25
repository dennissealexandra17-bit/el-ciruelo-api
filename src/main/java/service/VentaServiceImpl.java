package service;

import java.time.LocalDateTime;
import java.util.List;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import repository.ProductoRepository;
import repository.VentaRepository;
import repository.modelo.DetalleVenta;
import repository.modelo.Producto;
import repository.modelo.Venta;
import service.dto.DetalleVentaDto;
import service.dto.VentaDto;

@ApplicationScoped
public class VentaServiceImpl implements VentaService {

    @Inject
    private VentaRepository ventaRepository;

    @Inject
    private ProductoRepository productoRepository;

@Override
public void crearVenta(VentaDto ventaDto) {
    Venta venta = new Venta();
    venta.setFecha(ventaDto.getFecha());
    venta.setTotal(ventaDto.getTotal());

    if (ventaDto.getDetalles() != null) {
        List<DetalleVenta> detalles = ventaDto.getDetalles().stream()
            .map(dto -> {
                DetalleVenta detalle = convertirADetalleVenta(dto); // ¡ya maneja todo!
                detalle.setVenta(venta); // Relación bidireccional
                return detalle;
            })
            .toList();

        venta.setDetalles(detalles);
    }

    ventaRepository.crearVenta(venta);
}


    @Override
    public VentaDto obtenerVentaPorId(Long id) {
        Venta venta = ventaRepository.obtenerVentaPorId(id);
        return venta != null ? convertirAVentaDto(venta) : null;
    }

    @Override
    public List<VentaDto> obtenerTodasLasVentas() {
        List<Venta> ventas = ventaRepository.obtenerTodasLasVentas();
        return ventas.stream().map(this::convertirAVentaDto).toList();
    }

    

    private Venta convertirAVenta(VentaDto dto) {
    Venta venta = new Venta();
    venta.setId(dto.getId());
    // Conversión entre LocalDateTime y LocalDate si es necesario
    venta.setFecha(dto.getFecha());
    venta.setTotal(dto.getTotal());

    if (dto.getDetalles() != null) {
        List<DetalleVenta> detalles = dto.getDetalles().stream()
            .map(this::convertirADetalleVenta)
            .toList();

        // Asignar referencia de venta en cada detalle
        detalles.forEach(det -> det.setVenta(venta));

        venta.setDetalles(detalles);
    }

    return venta;
}

private VentaDto convertirAVentaDto(Venta venta) {
    VentaDto dto = new VentaDto();
    dto.setId(venta.getId());
    // Aquí convierte LocalDate a LocalDateTime, asignando la hora a 00:00 por ejemplo
    dto.setFecha(venta.getFecha());
    dto.setTotal(venta.getTotal());

    if (venta.getDetalles() != null) {
        List<DetalleVentaDto> detallesDto = venta.getDetalles().stream()
            .map(this::convertirADetalleVentaDto)
            .toList();

        dto.setDetalles(detallesDto);
    }

    return dto;
}

private DetalleVenta convertirADetalleVenta(DetalleVentaDto dto) {
    DetalleVenta detalle = new DetalleVenta();
    detalle.setId(dto.getId());

    // Buscar el producto completo
    Producto producto = productoRepository.findById(dto.getProductoId());
    if (producto == null) {
        throw new IllegalArgumentException("Producto no encontrado con id: " + dto.getProductoId());
    }

    // Validar stock
    if (producto.getStock() < dto.getCantidad()) {
        throw new IllegalArgumentException("Stock insuficiente para el producto: " + producto.getNombre());
    }

    // Reducir y guardar nuevo stock
    producto.setStock(producto.getStock() - dto.getCantidad());
    productoRepository.update(producto); // Actualiza el stock en la BD

    detalle.setProducto(producto);
    detalle.setCantidad(dto.getCantidad());
    detalle.setPrecioUnitario(dto.getPrecioUnitario());
    detalle.setSubtotal(dto.getSubtotal());

    return detalle;
}


private DetalleVentaDto convertirADetalleVentaDto(DetalleVenta detalle) {
    DetalleVentaDto dto = new DetalleVentaDto();
    dto.setId(detalle.getId());

    if (detalle.getProducto() != null) {
        dto.setProductoId(detalle.getProducto().getId());
        dto.setProductoNombre(detalle.getProducto().getNombre());  // Asumiendo getter nombre
        dto.setProductoMarca(detalle.getProducto().getMarca());    // Asumiendo getter marca
    }

    dto.setCantidad(detalle.getCantidad());
    dto.setPrecioUnitario(detalle.getPrecioUnitario());
    dto.setSubtotal(detalle.getSubtotal());
    return dto;
}

@Override
public List<VentaDto> obtenerVentasPorFecha(LocalDateTime fecha) {
    if (fecha == null) {
        throw new IllegalArgumentException("La fecha no puede ser nula");
    }

    LocalDateTime inicioDelDia = fecha.toLocalDate().atStartOfDay();
    LocalDateTime finDelDia = fecha.toLocalDate().atTime(23, 59, 59);

    List<Venta> ventas = ventaRepository.obtenerVentasEntreFechas(inicioDelDia, finDelDia);
    return ventas.stream().map(this::convertirAVentaDto).toList();
}

@Override
public List<VentaDto> obtenerVentasEntreFechas(LocalDateTime fechaInicio, LocalDateTime fechaFin) {
    if (fechaInicio == null || fechaFin == null) {
        throw new IllegalArgumentException("Las fechas no pueden ser nulas");
    }

    if (fechaInicio.isAfter(fechaFin)) {
        throw new IllegalArgumentException("La fecha de inicio no puede ser posterior a la fecha de fin");
    }

    List<Venta> ventas = ventaRepository.obtenerVentasEntreFechas(fechaInicio, fechaFin);
    return ventas.stream().map(this::convertirAVentaDto).toList();
}

}