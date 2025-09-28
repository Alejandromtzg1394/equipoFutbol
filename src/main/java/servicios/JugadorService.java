package servicios;

import entidades.Jugador;
import entidades.Equipo;
import entidades.Posicion;
import java.util.List;

public interface JugadorService {
    Jugador guardarJugador(Jugador jugador);
    Jugador buscarJugadorPorId(Long id);
    List<Jugador> listarTodosJugadores();
    void eliminarJugador(Long id);
    Jugador actualizarJugador(Jugador jugador);

    List<Jugador> buscarJugadoresPorNombre(String nombre);
    List<Jugador> buscarJugadoresPorEquipo(Equipo equipo);
    List<Jugador> buscarJugadoresPorPosicion(Posicion posicion);
    Long contarJugadoresPorEquipo(Equipo equipo);
    List<Jugador> buscarJugadoresSinEquipo();
}