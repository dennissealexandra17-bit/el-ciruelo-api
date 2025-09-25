package repository;

import java.util.List;
import java.util.Collections;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.PersistenceException;
import jakarta.transaction.Transactional;
import jakarta.persistence.NoResultException;
import repository.modelo.Producto;

@ApplicationScoped
@Transactional
public class ProductoRepositoryImpl implements ProductoRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public void save(Producto producto) {
        try {
            entityManager.persist(producto);
        } catch (PersistenceException e) {
            System.err.println("Error al guardar el producto: " + e.getMessage());
            throw new RuntimeException("Error al guardar el producto", e);
        }
    }

    @Override
    public void update(Producto producto) {
        try {
            entityManager.merge(producto);
        } catch (PersistenceException e) {
            System.err.println("Error al actualizar el producto: " + e.getMessage());
            throw new RuntimeException("Error al actualizar el producto", e);
        }
    }

    @Override
    public void delete(Long id) {
        try {
            Producto producto = entityManager.find(Producto.class, id);
            if (producto != null) {
                entityManager.remove(producto);
            }
        } catch (PersistenceException e) {
            System.err.println("Error al eliminar el producto: " + e.getMessage());
            throw new RuntimeException("Error al eliminar el producto", e);
        }
    }

    @Override
    public Producto findByNombre(String nombre) {
        try {
            return entityManager.createQuery("SELECT p FROM Producto p WHERE p.nombre = :nombre", Producto.class)
                    .setParameter("nombre", nombre)
                    .getSingleResult();
        } catch (NoResultException e) {
            System.err.println("No se encontró producto con nombre: " + nombre);
            return null;
        } catch (PersistenceException e) {
            System.err.println("Error al buscar producto por nombre: " + e.getMessage());
            throw new RuntimeException("Error al buscar producto por nombre", e);
        }
    }

    @Override
    public List<Producto> findByCategoria(String categoria) {
        try {
            return entityManager.createQuery("SELECT p FROM Producto p WHERE p.categoria = :categoria", Producto.class)
                    .setParameter("categoria", categoria)
                    .getResultList();
        } catch (PersistenceException e) {
            System.err.println("Error al buscar productos por categoría: " + e.getMessage());
            return Collections.emptyList();
        }
    }

    @Override
    public List<Producto> findByMarca(String marca) {
        try {
            return entityManager.createQuery("SELECT p FROM Producto p WHERE p.marca = :marca", Producto.class)
                    .setParameter("marca", marca)
                    .getResultList();
        } catch (PersistenceException e) {
            System.err.println("Error al buscar productos por marca: " + e.getMessage());
            return Collections.emptyList();
        }
    }

    @Override
    public List<Producto> findAll() {
        try {
            return entityManager.createQuery("SELECT p FROM Producto p", Producto.class)
                    .getResultList();
        } catch (PersistenceException e) {
            System.err.println("Error al obtener todos los productos: " + e.getMessage());
            return Collections.emptyList();
        }
    }

    @Override
    public List<Producto> findByMarcaAndCategoria(String marca, String categoria) {
        try {
            return entityManager.createQuery("SELECT p FROM Producto p WHERE p.marca = :marca AND p.categoria = :categoria", Producto.class)
                    .setParameter("marca", marca)
                    .setParameter("categoria", categoria)
                    .getResultList();
        } catch (PersistenceException e) {
            System.err.println("Error al buscar productos por marca y categoría: " + e.getMessage());
            return Collections.emptyList();
        }
    }

    @Override
    public Producto findById(Long id) {
        try {
            return entityManager.find(Producto.class, id);
        } catch (PersistenceException e) {
            System.err.println("Error al buscar producto por ID: " + e.getMessage());
            throw new RuntimeException("Error al buscar producto por ID", e);
        }
    }

    public List<Producto> findByNombreLike(String nombre) {
    return entityManager.createQuery(
            "SELECT p FROM Producto p WHERE LOWER(p.nombre) LIKE LOWER(CONCAT('%', :nombre, '%'))", Producto.class)
        .setParameter("nombre", nombre)
        .getResultList();
}

    
}
