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

    public Partido buscarPorId(Long id) {
        try {
            return entityManager.find(Partido.class, id);
        } catch (Exception e) {
            throw new RuntimeException("Error al buscar partido por ID: " + e.getMessage(), e);
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

    public Partido actualizar(Partido partido) {
        try {
            return entityManager.merge(partido);
        } catch (Exception e) {
            throw new RuntimeException("Error al actualizar el partido: " + e.getMessage(), e);
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

    public List<Partido> buscarPorEquipo(Equipo equipo) {
        try {
            TypedQuery<Partido> query = entityManager.createQuery(
                    "SELECT p FROM Partido p WHERE p.equipoLocal = :equipo OR p.equipoVisitante = :equipo ORDER BY p.fechaPartido DESC", Partido.class);
            query.setParameter("equipo", equipo);
            return query.getResultList();
        } catch (Exception e) {
            throw new RuntimeException("Error al buscar partidos por equipo: " + e.getMessage(), e);
        }
    }

    public List<Partido> buscarPorFecha(LocalDate fecha) {
        try {
            TypedQuery<Partido> query = entityManager.createQuery(
                    "SELECT p FROM Partido p WHERE p.fechaPartido = :fecha ORDER BY p.fechaPartido", Partido.class);
            query.setParameter("fecha", fecha);
            return query.getResultList();
        } catch (Exception e) {
            throw new RuntimeException("Error al buscar partidos por fecha: " + e.getMessage(), e);
        }
    }

    public List<Partido> buscarPorRangoFechas(LocalDate fechaInicio, LocalDate fechaFin) {
        try {
            TypedQuery<Partido> query = entityManager.createQuery(
                    "SELECT p FROM Partido p WHERE p.fechaPartido BETWEEN :fechaInicio AND :fechaFin ORDER BY p.fechaPartido", Partido.class);
            query.setParameter("fechaInicio", fechaInicio);
            query.setParameter("fechaFin", fechaFin);
            return query.getResultList();
        } catch (Exception e) {
            throw new RuntimeException("Error al buscar partidos por rango de fechas: " + e.getMessage(), e);
        }
    }

    public Long contarPartidosJugados() {
        try {
            TypedQuery<Long> query = entityManager.createQuery(
                    "SELECT COUNT(p) FROM Partido p", Long.class);
            return query.getSingleResult();
        } catch (Exception e) {
            throw new RuntimeException("Error al contar partidos: " + e.getMessage(), e);
        }
    }

    public List<Partido> buscarPartidosConGoles() {
        try {
            TypedQuery<Partido> query = entityManager.createQuery(
                    "SELECT p FROM Partido p WHERE SIZE(p.goles) > 0 ORDER BY p.fechaPartido DESC", Partido.class);
            return query.getResultList();
        } catch (Exception e) {
            throw new RuntimeException("Error al buscar partidos con goles: " + e.getMessage(), e);
        }
    }
}