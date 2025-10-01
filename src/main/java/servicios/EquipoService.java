package servicios;

import entidades.Equipo;
import java.util.List;

public interface EquipoService {
    Equipo guardarEquipo(Equipo equipo);
    List<Equipo> listarTodosEquipos();
    void eliminarEquipo(Long id);
    Equipo actualizarEquipo(Equipo equipo);

}