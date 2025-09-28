package DAO;

import entidades.Posicion;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import java.util.List;

public class PosicionDAO {
    private EntityManager entityManager;

    public PosicionDAO(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public Posicion guardar(Posicion posicion) {
        try {
            entityManager.persist(posicion);
            entityManager.flush();
            return posicion;
        } catch (Exception e) {
            throw new RuntimeException("Error al guardar la posición: " + e.getMessage(), e);
        }
    }

    public Posicion buscarPorId(Long id) {
        try {
            return entityManager.find(Posicion.class, id);
        } catch (Exception e) {
            throw new RuntimeException("Error al buscar posición por ID: " + e.getMessage(), e);
        }
    }

    public List<Posicion> listarTodos() {
        try {
            TypedQuery<Posicion> query = entityManager.createQuery(
                    "SELECT p FROM Posicion p ORDER BY p.nombre", Posicion.class);
            return query.getResultList();
        } catch (Exception e) {
            throw new RuntimeException("Error al listar posiciones: " + e.getMessage(), e);
        }
    }

    public Posicion actualizar(Posicion posicion) {
        try {
            return entityManager.merge(posicion);
        } catch (Exception e) {
            throw new RuntimeException("Error al actualizar la posición: " + e.getMessage(), e);
        }
    }

    public void eliminar(Long id) {
        try {
            Posicion posicion = entityManager.find(Posicion.class, id);
            if (posicion != null) {
                entityManager.remove(posicion);
            }
        } catch (Exception e) {
            throw new RuntimeException("Error al eliminar la posición: " + e.getMessage(), e);
        }
    }

    public Posicion buscarPorNombre(String nombre) {
        try {
            TypedQuery<Posicion> query = entityManager.createQuery(
                    "SELECT p FROM Posicion p WHERE p.nombre = :nombre", Posicion.class);
            query.setParameter("nombre", nombre);
            return query.getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }

    public boolean existePorNombre(String nombre) {
        try {
            TypedQuery<Long> query = entityManager.createQuery(
                    "SELECT COUNT(p) FROM Posicion p WHERE p.nombre = :nombre", Long.class);
            query.setParameter("nombre", nombre);
            return query.getSingleResult() > 0;
        } catch (Exception e) {
            throw new RuntimeException("Error al verificar existencia de posición: " + e.getMessage(), e);
        }
    }
}