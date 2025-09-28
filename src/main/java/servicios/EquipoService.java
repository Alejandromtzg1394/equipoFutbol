package servicios;

import entidades.Equipo;
import java.util.List;

public interface EquipoService {
    Equipo guardarEquipo(Equipo equipo);
    Equipo buscarEquipoPorId(Long id);
    List<Equipo> listarTodosEquipos();
    void eliminarEquipo(Long id);
    Equipo actualizarEquipo(Equipo equipo);

    // Métodos adicionales
    List<Equipo> buscarEquiposPorNombre(String nombre);
    boolean existeEquipoConNombre(String nombre);
    Long obtenerTotalEquipos();
}