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

    public Gol buscarPorId(Long id) {
        try {
            return entityManager.find(Gol.class, id);
        } catch (Exception e) {
            throw new RuntimeException("Error al buscar gol por ID: " + e.getMessage(), e);
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

    public Gol actualizar(Gol gol) {
        try {
            return entityManager.merge(gol);
        } catch (Exception e) {
            throw new RuntimeException("Error al actualizar el gol: " + e.getMessage(), e);
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

    public List<Gol> buscarPorPartido(Partido partido) {
        try {
            TypedQuery<Gol> query = entityManager.createQuery(
                    "SELECT g FROM Gol g WHERE g.partido = :partido ORDER BY g.minuto", Gol.class);
            query.setParameter("partido", partido);
            return query.getResultList();
        } catch (Exception e) {
            throw new RuntimeException("Error al buscar goles por partido: " + e.getMessage(), e);
        }
    }

    public List<Gol> buscarPorJugador(Jugador jugador) {
        try {
            TypedQuery<Gol> query = entityManager.createQuery(
                    "SELECT g FROM Gol g WHERE g.jugador = :jugador ORDER BY g.partido.fechaPartido DESC", Gol.class);
            query.setParameter("jugador", jugador);
            return query.getResultList();
        } catch (Exception e) {
            throw new RuntimeException("Error al buscar goles por jugador: " + e.getMessage(), e);
        }
    }

    public Long contarGolesPorJugador(Jugador jugador) {
        try {
            TypedQuery<Long> query = entityManager.createQuery(
                    "SELECT COUNT(g) FROM Gol g WHERE g.jugador = :jugador", Long.class);
            query.setParameter("jugador", jugador);
            return query.getSingleResult();
        } catch (Exception e) {
            throw new RuntimeException("Error al contar goles por jugador: " + e.getMessage(), e);
        }
    }

    public Long contarGolesPorPartido(Partido partido) {
        try {
            TypedQuery<Long> query = entityManager.createQuery(
                    "SELECT COUNT(g) FROM Gol g WHERE g.partido = :partido", Long.class);
            query.setParameter("partido", partido);
            return query.getSingleResult();
        } catch (Exception e) {
            throw new RuntimeException("Error al contar goles por partido: " + e.getMessage(), e);
        }
    }

    public List<Object[]> obtenerGoleadores() {
        try {
            TypedQuery<Object[]> query = entityManager.createQuery(
                    "SELECT g.jugador, COUNT(g) FROM Gol g GROUP BY g.jugador ORDER BY COUNT(g) DESC", Object[].class);
            return query.getResultList();
        } catch (Exception e) {
            throw new RuntimeException("Error al obtener goleadores: " + e.getMessage(), e);
        }
    }

    public List<Gol> buscarPorMinuto(int minuto) {
        try {
            TypedQuery<Gol> query = entityManager.createQuery(
                    "SELECT g FROM Gol g WHERE g.minuto = :minuto ORDER BY g.partido.fechaPartido DESC", Gol.class);
            query.setParameter("minuto", minuto);
            return query.getResultList();
        } catch (Exception e) {
            throw new RuntimeException("Error al buscar goles por minuto: " + e.getMessage(), e);
        }
    }
}