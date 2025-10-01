package servicios;

import entidades.Equipo;
import DAO.EquipoDAO;
import jakarta.persistence.EntityManager;
import java.util.List;

public class EquipoServiceImpl implements EquipoService {

    private EquipoDAO equipoDAO;
    private EntityManager entityManager;

    public EquipoServiceImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
        this.equipoDAO = new EquipoDAO(entityManager);
    }

    @Override
    public Equipo guardarEquipo(Equipo equipo) {
        try {
            entityManager.getTransaction().begin();
            Equipo equipoGuardado = equipoDAO.guardar(equipo);
            entityManager.getTransaction().commit();
            return equipoGuardado;
        } catch (Exception e) {
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
            throw new RuntimeException("Error al guardar equipo: " + e.getMessage(), e);
        }
    }


    @Override
    public List<Equipo> listarTodosEquipos() {
        return equipoDAO.listarTodos();
    }

    @Override
    public void eliminarEquipo(Long id) {
        try {
            entityManager.getTransaction().begin();
            equipoDAO.eliminar(id);
            entityManager.getTransaction().commit();
        } catch (Exception e) {
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
            throw new RuntimeException("Error al eliminar equipo: " + e.getMessage(), e);
        }
    }

    @Override
    public Equipo actualizarEquipo(Equipo equipo) {
        try {
            entityManager.getTransaction().begin();
            Equipo equipoActualizado = equipoDAO.actualizar(equipo);
            entityManager.getTransaction().commit();
            return equipoActualizado;
        } catch (Exception e) {
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
            throw new RuntimeException("Error al actualizar equipo: " + e.getMessage(), e);
        }
    }



}