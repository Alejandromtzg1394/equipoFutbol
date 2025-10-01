package DAO;

import entidades.Jugador;
import entidades.Equipo;
import entidades.Posicion;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;
import java.util.List;

public class JugadorDAO {
    private EntityManager entityManager;

    public JugadorDAO(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public Jugador guardar(Jugador jugador) {
        try {
            entityManager.persist(jugador);
            entityManager.flush();
            return jugador;
        } catch (Exception e) {
            throw new RuntimeException("Error al guardar el jugador: " + e.getMessage(), e);
        }
    }

    public List<Jugador> listarTodos() {
        try {
            TypedQuery<Jugador> query = entityManager.createQuery(
                    "SELECT j FROM Jugador j ORDER BY j.nombre", Jugador.class);
            return query.getResultList();
        } catch (Exception e) {
            throw new RuntimeException("Error al listar jugadores: " + e.getMessage(), e);
        }
    }


    public void eliminar(Long id) {
        try {
            Jugador jugador = entityManager.find(Jugador.class, id);
            if (jugador != null) {
                entityManager.remove(jugador);
            }
        } catch (Exception e) {
            throw new RuntimeException("Error al eliminar el equipo: " + e.getMessage(), e);
        }
    }
}