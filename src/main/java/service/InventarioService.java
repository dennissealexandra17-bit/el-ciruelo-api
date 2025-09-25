package service;

import java.util.List;

import service.dto.InventarioDto;

public interface InventarioService {
    
    InventarioDto obtenerInventarioPorProductoId(Long productoId);

    List<InventarioDto> obtenerInventariosConStockBajo();

    void crearInventario(InventarioDto inventarioDto);

    void actualizarInventario(Long productoId, InventarioDto inventarioDto);

    void eliminarInventario(Long productoId);


    
}
