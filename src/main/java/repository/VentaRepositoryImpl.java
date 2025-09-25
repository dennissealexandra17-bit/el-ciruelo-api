package repository;

import java.time.LocalDateTime;
import java.util.List;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import repository.modelo.Venta;

@ApplicationScoped
@Transactional
public class VentaRepositoryImpl implements VentaRepository {

    @PersistenceContext
    EntityManager entityManager;

    @Override
    public void crearVenta(Venta venta) {
        entityManager.persist(venta);
    }

    @Override
    public Venta obtenerVentaPorId(Long id) {
        return entityManager.find(Venta.class, id);
    }

    @Override
    public List<Venta> obtenerTodasLasVentas() {
        return entityManager.createQuery("SELECT v FROM Venta v", Venta.class).getResultList();
    }

    @Override
public List<Venta> obtenerVentasPorFecha(LocalDateTime fecha) {
    LocalDateTime inicioDelDia = fecha.toLocalDate().atStartOfDay();
    LocalDateTime finDelDia = fecha.toLocalDate().atTime(23, 59, 59);

    return entityManager.createQuery(
            "SELECT v FROM Venta v WHERE v.fecha BETWEEN :inicio AND :fin", Venta.class)
        .setParameter("inicio", inicioDelDia)
        .setParameter("fin", finDelDia)
        .getResultList();
}


    @Override
    public List<Venta> obtenerVentasEntreFechas(LocalDateTime fechaInicio, LocalDateTime fechaFin) {
    return entityManager.createQuery(
        "SELECT v FROM Venta v WHERE v.fecha BETWEEN :inicio AND :fin", Venta.class)
        .setParameter("inicio", fechaInicio)
        .setParameter("fin", fechaFin)
        .getResultList();
}


}
