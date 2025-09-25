package service;

import java.util.List;


import service.dto.ProductoDto;

public interface ProductoService {
    void crearProducto(ProductoDto productoDto);
    void actualizarProducto(Long id, ProductoDto productoDto);
    void eliminarProducto(Long id);

    ProductoDto obtenerProductoDtoPorNombre(String nombre);
    List<ProductoDto> obtenerTodosLosProductosPorCategoria(String categoria);
    List<ProductoDto> obtenerTodosLosProductosPorMarca(String marca);
    List<ProductoDto> obtenerTodosLosProductos();
    List<ProductoDto> obtenerProductosPorMarcaYCategoria(String marca, String categoria);

    List<ProductoDto> findByNombreLike(String nombre);
}
