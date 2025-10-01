package DAO;

import entidades.Equipo;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import java.util.List;

public class EquipoDAO {
    private EntityManager entityManager;

    public EquipoDAO(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public Equipo guardar(Equipo equipo) {
        try {
            entityManager.persist(equipo);
            entityManager.flush();
            return equipo;
        } catch (Exception e) {
            throw new RuntimeException("Error al guardar el equipo: " + e.getMessage(), e);
        }
    }


    public List<Equipo> listarTodos() {
        try {
            TypedQuery<Equipo> query = entityManager.createQuery(
                    "SELECT e FROM Equipo e ORDER BY e.nombre", Equipo.class);
            return query.getResultList();
        } catch (Exception e) {
            throw new RuntimeException("Error al listar equipos: " + e.getMessage(), e);
        }
    }

    public Equipo actualizar(Equipo equipo) {
        try {
            return entityManager.merge(equipo);
        } catch (Exception e) {
            throw new RuntimeException("Error al actualizar el equipo: " + e.getMessage(), e);
        }
    }

    public void eliminar(Long id) {
        try {
            Equipo equipo = entityManager.find(Equipo.class, id);
            if (equipo != null) {
                entityManager.remove(equipo);
            }
        } catch (Exception e) {
            throw new RuntimeException("Error al eliminar el equipo: " + e.getMessage(), e);
        }
    }


}