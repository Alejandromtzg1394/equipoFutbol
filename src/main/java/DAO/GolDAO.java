package DAO;

import entidades.Gol;
import entidades.Partido;
import entidades.Jugador;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import java.util.List;

public class GolDAO {
    private EntityManager entityManager;

    public GolDAO(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public Gol guardar(Gol gol) {
        try {
            entityManager.persist(gol);
            entityManager.flush();
            return gol;
        } catch (Exception e) {
            throw new RuntimeException("Error al guardar el gol: " + e.getMessage(), e);
        }
    }

    public List<Gol> listarTodos() {
        try {
            TypedQuery<Gol> query = entityManager.createQuery(
                    "SELECT g FROM Gol g ORDER BY g.partido.fechaPartido DESC, g.minuto", Gol.class);
            return query.getResultList();
        } catch (Exception e) {
            throw new RuntimeException("Error al listar goles: " + e.getMessage(), e);
        }
    }

    public void eliminar(Long id) {
        try {
            Gol gol = entityManager.find(Gol.class, id);
            if (gol != null) {
                entityManager.remove(gol);
            }
        } catch (Exception e) {
            throw new RuntimeException("Error al eliminar el gol: " + e.getMessage(), e);
        }
    }

}