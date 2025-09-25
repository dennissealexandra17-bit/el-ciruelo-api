package repository;

import java.util.List;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import repository.modelo.Inventario;
import service.dto.InventarioDto;

@ApplicationScoped
@Transactional
public class InventarioRepositoryImpl implements InventarioRepository {

    @PersistenceContext
    EntityManager entityManager;

    @Override
    public Inventario obtenerInventarioPorProductoId(Long productoId) {
        try {
            return entityManager.createQuery(
                    "SELECT i FROM Inventario i WHERE i.productoId = :productoId", Inventario.class)
                    .setParameter("productoId", productoId)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    @Override
    public List<InventarioDto> obtenerInventariosConStockBajo() {
        return entityManager.createQuery(
                "SELECT new service.dto.InventarioDto(i.id, i.productoId, i.cantidad, i.cantidadMinima, p.nombre, p.marca) " +
                "FROM Inventario i JOIN Producto p ON i.productoId = p.id " +
                "WHERE i.cantidad <= i.cantidadMinima", InventarioDto.class)
                .getResultList();
    }

    @Override
    public void crearInventario(Inventario inventario) {
        entityManager.persist(inventario);
    }

    @Override
    public void actualizarInventario(Long productoId, Inventario inventario) {
        Inventario inventarioExistente = obtenerInventarioPorProductoId(productoId);
        if (inventarioExistente != null) {
    inventarioExistente.setCantidad(inventario.getCantidad());
    inventarioExistente.setCantidadMinima(inventario.getCantidadMinima());
    entityManager.merge(inventarioExistente);
}

    }

    @Override
    public void eliminarInventario(Long productoId) {
        Inventario inventarioExistente = obtenerInventarioPorProductoId(productoId);
        if (inventarioExistente != null) {
            entityManager.remove(inventarioExistente);
        }
    }

}
