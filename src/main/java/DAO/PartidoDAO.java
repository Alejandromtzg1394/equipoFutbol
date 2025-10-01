package DAO;

import entidades.Partido;
import entidades.Equipo;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import java.time.LocalDate;
import java.util.List;

public class PartidoDAO {
    private EntityManager entityManager;

    public PartidoDAO(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public Partido guardar(Partido partido) {
        try {
            entityManager.persist(partido);
            entityManager.flush();
            return partido;
        } catch (Exception e) {
            throw new RuntimeException("Error al guardar el partido: " + e.getMessage(), e);
        }
    }


    public List<Partido> listarTodos() {
        try {
            TypedQuery<Partido> query = entityManager.createQuery(
                    "SELECT p FROM Partido p ORDER BY p.fechaPartido DESC", Partido.class);
            return query.getResultList();
        } catch (Exception e) {
            throw new RuntimeException("Error al listar partidos: " + e.getMessage(), e);
        }
    }


    public void eliminar(Long id) {
        try {
            Partido partido = entityManager.find(Partido.class, id);
            if (partido != null) {
                entityManager.remove(partido);
            }
        } catch (Exception e) {
            throw new RuntimeException("Error al eliminar el partido: " + e.getMessage(), e);
        }
    }
    
}