package repository;

import java.util.List;

import repository.modelo.Inventario;
import service.dto.InventarioDto;

public interface InventarioRepository {

    public Inventario obtenerInventarioPorProductoId(Long productoId);
    
    List<InventarioDto> obtenerInventariosConStockBajo();

     public void crearInventario(Inventario inventario);

      public void actualizarInventario(Long productoId, Inventario inventario);

      void eliminarInventario(Long productoId);







}
