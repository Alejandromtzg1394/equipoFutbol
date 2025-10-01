package servicios;

import entidades.Jugador;
import entidades.Equipo;
import entidades.Posicion;
import java.util.List;

public interface JugadorService {
    Jugador guardarJugador(Jugador jugador);
    List<Jugador> listarTodosJugadores();
    void eliminarJugador(Long id);
}