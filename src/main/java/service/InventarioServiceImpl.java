package service;

import java.util.List;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import repository.InventarioRepository;
import repository.modelo.Inventario;
import service.dto.InventarioDto;

@ApplicationScoped
public class InventarioServiceImpl implements InventarioService {

    @Inject
    private InventarioRepository inventarioRepository;

    @Override
    public List<InventarioDto> obtenerInventariosConStockBajo() {
        return inventarioRepository.obtenerInventariosConStockBajo();
    }

    @Override
    public InventarioDto obtenerInventarioPorProductoId(Long productoId) {
        Inventario inventario = inventarioRepository.obtenerInventarioPorProductoId(productoId);
        if (inventario == null) {
            return null;
        }
        return mapToDto(inventario);
    }

    @Override
    public void crearInventario(InventarioDto inventarioDto) {
        inventarioRepository.crearInventario(mapToEntity(inventarioDto));
    }

    @Override
    public void actualizarInventario(Long productoId, InventarioDto inventarioDto) {
        Inventario existente = inventarioRepository.obtenerInventarioPorProductoId(productoId);
        if (existente == null) {
            throw new RuntimeException("No se encontró inventario para el producto ID: " + productoId);
        }

        existente.setCantidad(inventarioDto.getCantidad());
        existente.setCantidadMinima(inventarioDto.getCantidadMinima());

        inventarioRepository.actualizarInventario(productoId, existente);
    }

    @Override
    public void eliminarInventario(Long productoId) {
        Inventario inventario = inventarioRepository.obtenerInventarioPorProductoId(productoId);
        if (inventario != null) {
            inventarioRepository.eliminarInventario(productoId);
        }

    }

    private InventarioDto mapToDto(Inventario inventario) {
        InventarioDto dto = new InventarioDto();
        dto.setId(inventario.getId());
        dto.setProductoId(inventario.getProductoId());
        dto.setCantidad(inventario.getCantidad());
        dto.setCantidadMinima(inventario.getCantidadMinima());
        return dto;
    }

    private Inventario mapToEntity(InventarioDto dto) {
        Inventario inventario = new Inventario();
        inventario.setId(dto.getId());
        inventario.setProductoId(dto.getProductoId());
        inventario.setCantidad(dto.getCantidad());
        inventario.setCantidadMinima(dto.getCantidadMinima());
        return inventario;
    }
}
