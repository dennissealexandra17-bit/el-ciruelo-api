package repository;

import java.util.List;

import repository.modelo.Producto;

public interface ProductoRepository {

    // Aquí puedes definir métodos específicos para interactuar con la entidad Producto


    //metodo para ingresar un producto
    void save(Producto producto);
    //metodo para actualizar un producto
    void update(Producto producto); 
    //metodo para eliminar un producto
    void delete(Long id);

    //metodo para buscar un producto por nombre
    Producto findByNombre(String nombre);   
    //metodo para buscar los productos por categoria
    List<Producto> findByCategoria(String categoria); 
    //metodo para buscar los productos  por marca
    List<Producto> findByMarca(String marca);
    //metodo para buscar todos los productos
    List<Producto> findAll();
    //metodo para buscar un producto por marca y categoria
    List<Producto> findByMarcaAndCategoria(String marca, String categoria);

    //metodo para buscar un producto por id
    Producto findById(Long id);

    //buscar productos por nombre de forma dinamica
    List<Producto> findByNombreLike(String nombre);


    
}
