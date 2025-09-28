package entidades;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "posiciones")
public class Posicion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_Posicion")
    private Long idPosicion;

    @Column(name = "Nombre", nullable = false, unique = true, length = 50)
    private String nombre;

    @OneToMany(mappedBy = "posicion", fetch = FetchType.LAZY)
    private List<Jugador> jugadores = new ArrayList<>();

    public Posicion() {}
    public Posicion(String nombre) { this.nombre = nombre; }

    // Getters y Setters
    public Long getIdPosicion() { return idPosicion; }
    public void setIdPosicion(Long idPosicion) { this.idPosicion = idPosicion; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
}