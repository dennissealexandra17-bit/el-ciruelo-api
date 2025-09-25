package service;

import java.util.List;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import repository.ProductoRepository;
import repository.modelo.Inventario;
import repository.modelo.Producto;
import service.dto.ProductoDto;

@ApplicationScoped
public class ProductoServiceImpl implements ProductoService {

    // Aquí puedes inyectar el repositorio de productos si es necesario
    @Inject
    private ProductoRepository productoRepository;

    // Inyectar el repositorio de inventario
    @Inject
    private repository.InventarioRepository inventarioRepository;

     //metodo para convertir ProductoDto a Producto
     private ProductoDto convertirAProductoDto(Producto producto) {
            ProductoDto productoDto = new ProductoDto();
            productoDto.setId(producto.getId());
            productoDto.setNombre(producto.getNombre());
            productoDto.setStock(producto.getStock());
            productoDto.setPrecioCompra(producto.getPrecioCompra());
            productoDto.setPrecioVenta(producto.getPrecioVenta());
            productoDto.setMarca(producto.getMarca());
            productoDto.setCategoria(producto.getCategoria());
            productoDto.setStockInicial(producto.getStockInicial());
            productoDto.setFechaIngreso(producto.getFechaIngreso());

            return productoDto;
        }
    //metodo para convertir Producto a ProductoDto
    private Producto convertirAProducto(ProductoDto productoDto) {
        Producto producto = new Producto();
        producto.setId(productoDto.getId());
        producto.setNombre(productoDto.getNombre());
        producto.setStock(productoDto.getStock());
        producto.setPrecioCompra(productoDto.getPrecioCompra());
        producto.setPrecioVenta(productoDto.getPrecioVenta());
        producto.setMarca(productoDto.getMarca());
        producto.setCategoria(productoDto.getCategoria());
        producto.setStockInicial(productoDto.getStockInicial());
        producto.setFechaIngreso(productoDto.getFechaIngreso());
        return producto;
    }

    //metodo para convertir una lista de Producto a una lista de ProductoDto
    private List<ProductoDto> convertirAListaProductoDto(List<repository.modelo.Producto> productos) {
        return productos.stream()
                .map(this::convertirAProductoDto)
                .toList();
    }

    @Override
    public void crearProducto(ProductoDto productoDto) {
        if (productoDto == null) {
            throw new IllegalArgumentException("El producto no puede ser nulo");
        }
        Producto producto = convertirAProducto(productoDto);
        // Establecer el stock inicial solo en la creación
    producto.setStockInicial(productoDto.getStock());
        productoRepository.save(producto);

        // Crear registro en Inventario
        Inventario inventario = new Inventario();
        inventario.setProductoId(producto.getId());
        inventario.setCantidad(producto.getStock());
        // Usar cantidadMinima directamente del DTO, si es nulo poner 1
        inventario.setCantidadMinima(productoDto.getCantidadMinima() != null ? productoDto.getCantidadMinima() : 1);
        inventarioRepository.crearInventario(inventario);
    }


    @Override
    public List<ProductoDto> obtenerTodosLosProductos() {
        List<repository.modelo.Producto> productos = productoRepository.findAll();
        return convertirAListaProductoDto(productos);
    }

 @Override
public void actualizarProducto(Long id, ProductoDto productoDto) {
    if (id == null) {
        throw new IllegalArgumentException("El ID no puede ser nulo");
    }

    if (productoDto == null) {
        throw new IllegalArgumentException("El producto no puede ser nulo");
    }

    Producto productoExistente = productoRepository.findById(id);
    if (productoExistente == null) {
        throw new IllegalArgumentException("Producto no encontrado con ID: " + id);
    }

    Producto productoActualizado = convertirAProducto(productoDto);
    productoActualizado.setId(id);

    // Lógica para aumentar stockInicial si hay nueva compra
    int stockAnterior = productoExistente.getStock();
    int stockNuevo = productoDto.getStock();

    if (stockNuevo > stockAnterior) {
        int diferencia = stockNuevo - stockAnterior;
        int nuevoStockInicial = productoExistente.getStockInicial() + diferencia;
        productoActualizado.setStockInicial(nuevoStockInicial);
    } else {
        // Si no hay aumento, se conserva el stockInicial original
        productoActualizado.setStockInicial(productoExistente.getStockInicial());
    }

    productoRepository.update(productoActualizado);

    // Actualizar inventario
    Inventario inventario = new Inventario();
    inventario.setProductoId(productoActualizado.getId());
    inventario.setCantidad(productoActualizado.getStock());
    inventario.setCantidadMinima(productoDto.getCantidadMinima() != null ? productoDto.getCantidadMinima() : 1);
    inventarioRepository.actualizarInventario(productoActualizado.getId(), inventario);
}

    @Override
    public void eliminarProducto(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("El ID no puede ser nulo");
        }
        productoRepository.delete(id);
        // Eliminar inventario asociado
        inventarioRepository.eliminarInventario(id);
    }
    @Override
    public ProductoDto obtenerProductoDtoPorNombre(String nombre) {
        if (nombre == null || nombre.isEmpty()) {
            throw new IllegalArgumentException("El nombre del producto no puede ser nulo o vacío");
        }
        repository.modelo.Producto producto = productoRepository.findByNombre(nombre);
        if (producto == null) {
            return null; // O lanzar una excepción si prefieres
        }
        return convertirAProductoDto(producto);
    }
    @Override
    public List<ProductoDto> obtenerTodosLosProductosPorCategoria(String categoria) {
        if (categoria == null || categoria.isEmpty()) {
            throw new IllegalArgumentException("La categoría no puede ser nula o vacía");
        }
        List<repository.modelo.Producto> productos = productoRepository.findByCategoria(categoria);
        return convertirAListaProductoDto(productos);
    }
    @Override
    public List<ProductoDto> obtenerTodosLosProductosPorMarca(String marca) {
        if (marca == null || marca.isEmpty()) {
            throw new IllegalArgumentException("La marca no puede ser nula o vacía");
        }
        List<repository.modelo.Producto> productos = productoRepository.findByMarca(marca);
        return convertirAListaProductoDto(productos);
    }
    @Override
    public List<ProductoDto> obtenerProductosPorMarcaYCategoria(String marca, String categoria) {
        if (marca == null || marca.isEmpty()) {
            throw new IllegalArgumentException("La marca no puede ser nula o vacía");
        }
        if (categoria == null || categoria.isEmpty()) {
            throw new IllegalArgumentException("La categoría no puede ser nula o vacía");
        }
        List<repository.modelo.Producto> productos = productoRepository.findByMarcaAndCategoria(marca, categoria);
        return convertirAListaProductoDto(productos);
    }
    @Override
    public List<ProductoDto> findByNombreLike(String nombre) {
        List<Producto> productos = productoRepository.findByNombreLike(nombre);
    return productos.stream()
        .map(this::convertirAProductoDto)
        .toList();
    }

}
