package servicios;

import entidades.Partido;
import entidades.Equipo;
import java.time.LocalDate;
import java.util.List;

public interface PartidoService {
    Partido guardarPartido(Partido partido);
    List<Partido> listarTodosPartidos();
    void eliminarPartido(Long id);
}