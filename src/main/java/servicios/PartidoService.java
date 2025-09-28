package servicios;

import entidades.Partido;
import entidades.Equipo;
import java.time.LocalDate;
import java.util.List;

public interface PartidoService {
    Partido guardarPartido(Partido partido);
    Partido buscarPartidoPorId(Long id);
    List<Partido> listarTodosPartidos();
    void eliminarPartido(Long id);
    Partido actualizarPartido(Partido partido);

    List<Partido> buscarPartidosPorEquipo(Equipo equipo);
    List<Partido> buscarPartidosPorFecha(LocalDate fecha);
    List<Partido> buscarPartidosPorRangoFechas(LocalDate fechaInicio, LocalDate fechaFin);
    Long contarPartidosJugados();
    List<Partido> buscarPartidosConGoles();
}