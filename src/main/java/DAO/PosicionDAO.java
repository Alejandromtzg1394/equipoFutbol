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
    
    public List<Posicion> listarTodos() {
        try {
            TypedQuery<Posicion> query = entityManager.createQuery(
                    "SELECT p FROM Posicion p ORDER BY p.nombre", Posicion.class);
            return query.getResultList();
        } catch (Exception e) {
            throw new RuntimeException("Error al listar posiciones: " + e.getMessage(), e);
        }
    }
}