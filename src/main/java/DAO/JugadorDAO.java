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

    public Jugador buscarPorId(Long id) {
        try {
            return entityManager.find(Jugador.class, id);
        } catch (Exception e) {
            throw new RuntimeException("Error al buscar jugador por ID: " + e.getMessage(), e);
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

    public Jugador actualizar(Jugador jugador) {
        try {
            return entityManager.merge(jugador);
        } catch (Exception e) {
            throw new RuntimeException("Error al actualizar el jugador: " + e.getMessage(), e);
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

    /*
    // DAO (sin transacción)
    public void eliminar(Long id) {
        Jugador jugador = entityManager.find(Jugador.class, id);
        if (jugador != null) {
            entityManager.remove(jugador);
        }
    }

     */



    public List<Jugador> buscarPorNombre(String nombre) {
        try {
            TypedQuery<Jugador> query = entityManager.createQuery(
                    "SELECT j FROM Jugador j WHERE j.nombre LIKE :nombre ORDER BY j.nombre", Jugador.class);
            query.setParameter("nombre", "%" + nombre + "%");
            return query.getResultList();
        } catch (Exception e) {
            throw new RuntimeException("Error al buscar jugadores por nombre: " + e.getMessage(), e);
        }
    }

    public List<Jugador> buscarPorEquipo(Equipo equipo) {
        try {
            TypedQuery<Jugador> query = entityManager.createQuery(
                    "SELECT j FROM Jugador j WHERE j.equipo = :equipo ORDER BY j.nombre", Jugador.class);
            query.setParameter("equipo", equipo);
            return query.getResultList();
        } catch (Exception e) {
            throw new RuntimeException("Error al buscar jugadores por equipo: " + e.getMessage(), e);
        }
    }

    public List<Jugador> buscarPorPosicion(Posicion posicion) {
        try {
            TypedQuery<Jugador> query = entityManager.createQuery(
                    "SELECT j FROM Jugador j WHERE j.posicion = :posicion ORDER BY j.nombre", Jugador.class);
            query.setParameter("posicion", posicion);
            return query.getResultList();
        } catch (Exception e) {
            throw new RuntimeException("Error al buscar jugadores por posición: " + e.getMessage(), e);
        }
    }

    public Long contarPorEquipo(Equipo equipo) {
        try {
            TypedQuery<Long> query = entityManager.createQuery(
                    "SELECT COUNT(j) FROM Jugador j WHERE j.equipo = :equipo", Long.class);
            query.setParameter("equipo", equipo);
            return query.getSingleResult();
        } catch (Exception e) {
            throw new RuntimeException("Error al contar jugadores por equipo: " + e.getMessage(), e);
        }
    }

    public List<Jugador> buscarJugadoresSinEquipo() {
        try {
            TypedQuery<Jugador> query = entityManager.createQuery(
                    "SELECT j FROM Jugador j WHERE j.equipo IS NULL ORDER BY j.nombre", Jugador.class);
            return query.getResultList();
        } catch (Exception e) {
            throw new RuntimeException("Error al buscar jugadores sin equipo: " + e.getMessage(), e);
        }
    }
}