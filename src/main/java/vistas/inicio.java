package vistas;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import com.toedter.calendar.JDateChooser;

import servicios.*;
import entidades.*;
import jakarta.persistence.EntityManager;
import DAO.inicializarConexion;



public class inicio extends JFrame {

    // Servicios
    private EquipoServiceImpl equipoService;
    private JugadorServiceImpl jugadorService;
    private PartidoServiceImpl partidoService;
    private GolServiceImpl golService;
    private PosicionService posicionService; // <-- agregar esta línea


    private JTabbedPane tabbedPane1;
    private JPanel panelPrincipal;

    // Componentes reutilizables
    private DefaultTableModel modeloEquipos;
    private DefaultTableModel modeloJugadores;
    private JComboBox<String> cmbEquiposJugadores;
    private JComboBox<String> cmbPosiciones;
    private JComboBox<Equipo> cmbEquipoLocal;
    private JComboBox<Equipo> cmbEquipoVisitante;


    // ---- Atributos globales para GOLES ----
    private JComboBox<Partido> cmbPartidosGoles;
    private JComboBox<Jugador> cmbJugadoresGoles;


    private List<Posicion> listaPosiciones = new ArrayList<>();

    public static void main(String[] args){
        try{ UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); }catch(Exception ignored){}
        SwingUtilities.invokeLater(()-> new inicio().setVisible(true));
    }


    public inicio() {
        configurarVentana();
        inicializarServicios();
        inicializarComponentes();
        configurarPestanias();
    }

    private void configurarVentana() {
        setTitle("Torneo de Fútbol");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 800);
        setLocationRelativeTo(null);
    }

    private void inicializarServicios() {
        try {
            EntityManager em = inicializarConexion.getEntityManager();

            equipoService = new EquipoServiceImpl(em);
            jugadorService = new JugadorServiceImpl(em);
            partidoService = new PartidoServiceImpl(em);
            golService = new GolServiceImpl(em);
            posicionService = new PosicionServiceImpl(em);

        //Conexion a la base de datos
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Error al conectar con la base de datos: " + e.getMessage(),
                    "Error de Conexión",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void inicializarComponentes() {
        panelPrincipal = new JPanel(new BorderLayout());
        tabbedPane1 = new JTabbedPane();
        panelPrincipal.add(tabbedPane1, BorderLayout.CENTER);
        setContentPane(panelPrincipal);
    }

    private void configurarPestanias() {
        tabbedPane1.addTab("Inicio", crearPanelInicio());
        tabbedPane1.addTab("Equipos", crearPanelEquipos());
        tabbedPane1.addTab("Jugadores", crearPanelJugadores());
        tabbedPane1.addTab("Partidos", crearPanelPartidos());
        tabbedPane1.addTab("Goles", crearPanelGoles());

        // 👉 Aquí agregas el listener que se dispara al cambiar de pestaña
        tabbedPane1.addChangeListener(e -> cargarCombos());
    }




    // ----------------> INICIO <----------------
    private JPanel crearPanelInicio() {
        JPanel panel = new JPanel(new BorderLayout(10,10));
        panel.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));
        panel.setBackground(new Color(240,245,250));

        JLabel lblTitulo = new JLabel("Torneo de Futbol", JLabel.CENTER);

        panel.add(lblTitulo, BorderLayout.NORTH);

        JPanel mainPanel = new JPanel(new GridLayout(1,2,20,0));
        mainPanel.setBackground(panel.getBackground());

        System.out.println("Ruta absoluta: " + new java.io.File("src/main/img/futbol.png").getAbsolutePath());
        System.out.println("Existe --->" + new java.io.File("src/main/img/futbol.png").exists());


        //IMG
        ImageIcon icon = new ImageIcon(getClass().getResource("/img/futbol.png"));
        Image img = icon.getImage().getScaledInstance(250, 250, Image.SCALE_SMOOTH);
        JLabel lblImagen = new JLabel(new ImageIcon(img), JLabel.CENTER);




        mainPanel.add(lblImagen);
        panel.add(mainPanel, BorderLayout.CENTER);
        return panel;
    }



    // ---------------- PANEL DE EQUIPOS ----------------
    private JPanel crearPanelEquipos() {
        JPanel panel = new JPanel(new BorderLayout(10,10));
        panel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

        JPanel toolbar = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JTextField txtNombre = new JTextField(20);
        JButton btnAgregar = new JButton("Agregar");
        JButton btnEliminar = new JButton("Eliminar");
        JButton btnActualizar = new JButton("Actualizar");


        //AGregamos botones a la barra menu
        toolbar.add(new JLabel("Nombre del equipo:"));
        toolbar.add(txtNombre);
        toolbar.add(btnAgregar);
        toolbar.add(btnEliminar);
        toolbar.add(btnActualizar);


        //La cosntruccion del arreglo
        String[] columnas = {"ID","Nombre"};


        modeloEquipos = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };

        JTable tabla = new JTable(modeloEquipos);
        JScrollPane scroll = new JScrollPane(tabla);

        btnAgregar.addActionListener(e -> agregarEquipo(txtNombre));
        btnActualizar.addActionListener(e -> actualizarListaEquipos());
        btnEliminar.addActionListener(e -> eliminarEquipo(tabla));

        panel.add(toolbar, BorderLayout.NORTH);
        panel.add(scroll, BorderLayout.CENTER);

        actualizarListaEquipos();
        return panel;
    }

    //Del Boton Eliminar
    private void eliminarEquipo(JTable tabla) {
        try {
            int filaSeleccionada = tabla.getSelectedRow();
            if (filaSeleccionada == -1) {
                JOptionPane.showMessageDialog(this, "Seleccione un equipo para eliminar");
                return;
            }

            Long idEquipo = (Long) modeloEquipos.getValueAt(filaSeleccionada, 0); // columna 0 = ID
            int confirm = JOptionPane.showConfirmDialog(this,
                    "eliminar este equipo",
                    "Confirmar",
                    JOptionPane.YES_NO_OPTION);

            if (confirm == JOptionPane.YES_OPTION) {
                equipoService.eliminarEquipo(idEquipo); // Llama a tu DAO
                actualizarListaEquipos();
                actualizarCombosJugadores();
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al eliminar equipo: " + e.getMessage());
        }
    }

//Del Boton Agregar
    private void agregarEquipo(JTextField txtNombre) {
        try {
            String nombre = txtNombre.getText().trim();
            if(nombre.isEmpty()) { JOptionPane.showMessageDialog(this,"Ingrese un nombre"); return;}
            Equipo equipo = new Equipo(nombre);
            equipoService.guardarEquipo(equipo);
            txtNombre.setText("");
            actualizarListaEquipos();
            actualizarCombosJugadores();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,"Error al agregar equipo: "+e.getMessage());
        }
    }

//Actualuza de nuevo la tabla del panel Equipo
    private void actualizarListaEquipos(){
        modeloEquipos.setRowCount(0);
        List<Equipo> equipos = equipoService.listarTodosEquipos();
        for(Equipo eq: equipos){
            modeloEquipos.addRow(new Object[]{eq.getIdEquipo(), eq.getNombre(), eq.getJugadores().size(), "✏️ 🗑️"});
        }
    }

    // ---------------- PANEL DE JUGADORES ----------------
    private JPanel crearPanelJugadores() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Formulario de registro
        JPanel formulario = new JPanel(new GridLayout(4, 2, 10, 10));
        JTextField txtNombre = new JTextField();
        cmbEquiposJugadores = new JComboBox<>();
        cmbPosiciones = new JComboBox<>();

        // Selector de fecha con calendario
        JDateChooser fechaNacimiento = new JDateChooser();
        fechaNacimiento.setDateFormatString("dd/MM/yyyy"); // formato visible

        // Agregar etiquetas y campos al formulario
        formulario.add(new JLabel("Nombre:"));
        formulario.add(txtNombre);
        formulario.add(new JLabel("Equipo:"));
        formulario.add(cmbEquiposJugadores);
        formulario.add(new JLabel("Posición:"));
        formulario.add(cmbPosiciones);
        formulario.add(new JLabel("Fecha Nacimiento:"));
        formulario.add(fechaNacimiento);

        // Toolbar con botones
        JPanel toolbar = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton btnAgregar = new JButton("Agregar");
        JButton btnEliminar = new JButton("Eliminar");
        JButton btnActualizar = new JButton("Actualizar");
        toolbar.add(btnAgregar);
        toolbar.add(btnEliminar);
        toolbar.add(btnActualizar);

        // Tabla de jugadores
        String[] columnas = {"ID", "Nombre", "Equipo", "Posición", "Fecha Nacimiento"};
        modeloJugadores = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int col) {
                return false; // que la tabla no sea editable
            }
        };
        JTable tabla = new JTable(modeloJugadores);
        JScrollPane scroll = new JScrollPane(tabla);

        // Acciones botones
        btnAgregar.addActionListener(e -> agregarJugador(txtNombre, fechaNacimiento));
        //Nota Acciones anidadas
        btnActualizar.addActionListener(e -> {
            actualizarCombosJugadores();
            actualizarListaJugadores();
        });

        btnEliminar.addActionListener(e -> eliminarJugador(tabla));


        // Agregar componentes al panel principal
        panel.add(formulario, BorderLayout.NORTH);
        panel.add(toolbar, BorderLayout.CENTER);
        panel.add(scroll, BorderLayout.SOUTH);

        // Inicializar combos y lista
        actualizarCombosJugadores();
        actualizarListaJugadores();
        return panel;
    }

    //
    private void eliminarJugador(JTable tabla) {
        try {
            int filaSeleccionada = tabla.getSelectedRow();
            if (filaSeleccionada == -1) {
                JOptionPane.showMessageDialog(this, "Seleccione un jugador para eliminar");
                return;
            }

            Long idJugador = Long.valueOf(modeloJugadores.getValueAt(filaSeleccionada, 0).toString()); // columna 0 = ID

            System.out.println("-->"+idJugador+"<--");

            int confirm = JOptionPane.showConfirmDialog(this,
                    "Eliminar jugador",
                    "Confirmar",
                    JOptionPane.YES_NO_OPTION);

            if (confirm == JOptionPane.YES_OPTION) {
                jugadorService.eliminarJugador(idJugador); // Llama a tu DAO/Servicio
                actualizarListaJugadores(); // Refresca la tabla
                JOptionPane.showMessageDialog(this, "Jugador eliminado correctamente");
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al eliminar jugador: " + e.getMessage());
        }
    }

    private void agregarJugador(JTextField txtNombre, JDateChooser fechaNacimiento) {
        try {
            String nombre = txtNombre.getText().trim();
            if (nombre.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Ingrese nombre");
                return;
            }

            Date fecha = fechaNacimiento.getDate();
            if (fecha == null) {
                JOptionPane.showMessageDialog(this, "Seleccione una fecha de nacimiento");
                return;
            }

            Date hoy = new Date();
            if (fecha.after(hoy)) {
                JOptionPane.showMessageDialog(this, "La fecha de nacimiento no puede ser mayor a la fecha actual");
                return;
            }

            // Crear jugador
            Jugador j = new Jugador();
            j.setNombre(nombre);
            j.setFechaNacimiento(fecha.toInstant().atZone(ZoneId.systemDefault()).toLocalDate());

            // Equipo
            String nombreEquipo = (String) cmbEquiposJugadores.getSelectedItem();
            Equipo equipoSeleccionado = equipoService.listarTodosEquipos()
                    .stream()
                    .filter(e -> e.getNombre().equals(nombreEquipo))
                    .findFirst()
                    .orElse(null);
            if (equipoSeleccionado == null) {
                JOptionPane.showMessageDialog(this, "Seleccione un equipo válido");
                return;
            }
            j.setEquipo(equipoSeleccionado);

            // Posición
            String nombrePosicion = (String) cmbPosiciones.getSelectedItem();
            Posicion posicionSeleccionada = listaPosiciones.stream()
                    .filter(p -> p.getNombre().equals(nombrePosicion))
                    .findFirst()
                    .orElse(null);
            if (posicionSeleccionada == null) {
                JOptionPane.showMessageDialog(this, "Seleccione una posición válida");
                return;
            }
            j.setPosicion(posicionSeleccionada);

            // Guardar en BD
            jugadorService.guardarJugador(j);

            // Limpiar formulario
            txtNombre.setText("");
            fechaNacimiento.setDate(null);
            cmbEquiposJugadores.setSelectedIndex(-1);
            cmbPosiciones.setSelectedIndex(-1);

            actualizarListaJugadores();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al agregar jugador: " + e.getMessage());
        }
    }




    private void actualizarListaJugadores() {
        modeloJugadores.setRowCount(0);
        List<Jugador> jugadores = jugadorService.listarTodosJugadores();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        for (Jugador j : jugadores) {
            String fechaStr = j.getFechaNacimiento() != null
                    ? j.getFechaNacimiento().format(formatter)
                    : "No especificada";

            modeloJugadores.addRow(new Object[]{
                    j.getIdJugador(),
                    j.getNombre(),
                    j.getEquipo() != null ? j.getEquipo().getNombre() : "Sin equipo",
                    j.getPosicion() != null ? j.getPosicion().getNombre() : "Sin posición",
                    fechaStr
            });
        }
    }


    //Solo es para los combos
    private void actualizarCombosJugadores(){
        cmbEquiposJugadores.removeAllItems();
        cmbPosiciones.removeAllItems();

        // Equipos
        for(Equipo e: equipoService.listarTodosEquipos()) {
            cmbEquiposJugadores.addItem(e.getNombre());
        }

        // Posiciones
        listaPosiciones = posicionService.listarTodasPosiciones(); // Debes tener un servicio de Posicion
        for(Posicion p : listaPosiciones) {
            cmbPosiciones.addItem(p.getNombre()); // o el campo correspondiente en Posicion
        }
    }

    // ----------------> PANEL GOLES <----------------
    private JPanel crearPanelGoles() {

        JPanel panel = new JPanel(new BorderLayout(10,10));
        panel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

        JPanel formulario = new JPanel(new GridLayout(3,2,10,10));
        cmbPartidosGoles = new JComboBox<>();
        cmbJugadoresGoles = new JComboBox<>();
        JTextField txtMinuto = new JTextField();

        formulario.add(new JLabel("Partido:")); formulario.add(cmbPartidosGoles);
        formulario.add(new JLabel("Jugador:")); formulario.add(cmbJugadoresGoles);
        formulario.add(new JLabel("Minuto:")); formulario.add(txtMinuto);

        JPanel toolbar = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton btnAgregar = new JButton("Agregar");
        JButton btnEliminar = new JButton("Eliminar");
        JButton btnActualizar = new JButton("Actualizar");
        toolbar.add(btnAgregar); toolbar.add(btnEliminar); toolbar.add(btnActualizar);

        String[] columnas = {"ID","Partido","Jugador","Minuto"};
        DefaultTableModel modeloGoles = new DefaultTableModel(columnas,0){
            @Override
            public boolean isCellEditable(int row,int col){return false;}
        };
        JTable tablaGoles = new JTable(modeloGoles);
        JScrollPane scroll = new JScrollPane(tablaGoles);

        // ---- BOTONES ----
        btnAgregar.addActionListener(e -> {
            Partido partido = (Partido) cmbPartidosGoles.getSelectedItem();
            Jugador jugador = (Jugador) cmbJugadoresGoles.getSelectedItem();
            String strMinuto = txtMinuto.getText().trim();
            if(partido==null || jugador==null || strMinuto.isEmpty()){
                JOptionPane.showMessageDialog(this,"Complete todos los campos");
                return;
            }
            try{
                int minuto = Integer.parseInt(strMinuto);
                Gol gol = new Gol();
                gol.setPartido(partido);
                gol.setJugador(jugador);
                gol.setMinuto(minuto);
                golService.guardarGol(gol);
                txtMinuto.setText("");
                actualizarListaGoles(modeloGoles);
            }catch(NumberFormatException ex){
                JOptionPane.showMessageDialog(this,"El minuto debe ser un número entero");
            }catch(Exception ex){
                JOptionPane.showMessageDialog(this,"Error al agregar gol: "+ex.getMessage());
            }
        });

        btnActualizar.addActionListener(e -> actualizarListaGoles(modeloGoles));

        btnEliminar.addActionListener(e -> {
            int filaSeleccionada = tablaGoles.getSelectedRow();
            if(filaSeleccionada==-1){
                JOptionPane.showMessageDialog(this,"Seleccione un gol");
                return;
            }
            Long idGol = (Long) modeloGoles.getValueAt(filaSeleccionada,0);
            int confirm = JOptionPane.showConfirmDialog(this,"Eliminar gol","Confirmar",JOptionPane.YES_NO_OPTION);
            if(confirm==JOptionPane.YES_OPTION){
                try{
                    golService.eliminarGol(idGol);
                    actualizarListaGoles(modeloGoles);
                }catch(Exception ex){
                    JOptionPane.showMessageDialog(this,"Error al eliminar gol: "+ex.getMessage());
                }
            }
        });

        panel.add(formulario, BorderLayout.NORTH);
        panel.add(toolbar, BorderLayout.CENTER);
        panel.add(scroll, BorderLayout.SOUTH);

        actualizarListaGoles(modeloGoles);

        return panel;
    }

    // ---- Actualiza la tabla de goles ----
    private void actualizarListaGoles(DefaultTableModel modelo){
        modelo.setRowCount(0);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        for(Gol g : golService.listarTodosGoles()){
            String partidoStr = "No especificado";
            if(g.getPartido()!=null){
                Partido p = g.getPartido();
                String local = p.getEquipoLocal()!=null ? p.getEquipoLocal().getNombre():"Sin equipo";
                String visitante = p.getEquipoVisitante()!=null ? p.getEquipoVisitante().getNombre():"Sin equipo";
                String fecha = p.getFechaPartido()!=null ? p.getFechaPartido().format(formatter):"";
                partidoStr = local + " vs " + visitante + " ("+fecha+")";
            }
            String jugadorStr = g.getJugador()!=null ? g.getJugador().getNombre():"No especificado";
            modelo.addRow(new Object[]{g.getIdGol(),partidoStr,jugadorStr,g.getMinuto()});
        }
    }


    // ---------------- PANEL DE PARTIDOS ----------------
    private JPanel crearPanelPartidos() {

        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Formulario de registro
        JPanel formulario = new JPanel(new GridLayout(3, 2, 10, 10));
        cmbEquipoLocal = new JComboBox<>();
        cmbEquipoVisitante = new JComboBox<>();
        JDateChooser fechaPartido = new JDateChooser();
        fechaPartido.setDateFormatString("dd/MM/yyyy");

        formulario.add(new JLabel("Equipo Local:"));
        formulario.add(cmbEquipoLocal);
        formulario.add(new JLabel("Equipo Visitante:"));
        formulario.add(cmbEquipoVisitante);
        formulario.add(new JLabel("Fecha:"));
        formulario.add(fechaPartido);

        // Toolbar con botones
        JPanel toolbar = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton btnAgregar = new JButton("Agregar");
        JButton btnEliminar = new JButton("Eliminar");
        JButton btnActualizar = new JButton("Actualizar");
        toolbar.add(btnAgregar);
        toolbar.add(btnEliminar);
        toolbar.add(btnActualizar);

        // Tabla de partidos
        String[] columnas = {"ID", "Equipo Local", "Equipo Visitante", "Fecha"};
        DefaultTableModel modeloPartidos = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int col) { return false; }
        };
        JTable tablaPartidos = new JTable(modeloPartidos);
        JScrollPane scroll = new JScrollPane(tablaPartidos);

        // ---- BOTONES ----
        btnAgregar.addActionListener(e -> {
            Equipo local = (Equipo) cmbEquipoLocal.getSelectedItem();
            Equipo visitante = (Equipo) cmbEquipoVisitante.getSelectedItem();
            Date fecha = fechaPartido.getDate();

            if (local == null || visitante == null || fecha == null) {
                JOptionPane.showMessageDialog(this, "Complete todos los campos");
                return;
            }
            if (local.equals(visitante)) {
                JOptionPane.showMessageDialog(this, "El equipo local y visitante no pueden ser iguales");
                return;
            }

            try {
                Partido partido = new Partido();
                partido.setEquipoLocal(local);
                partido.setEquipoVisitante(visitante);
                partido.setFechaPartido(fecha.toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
                partidoService.guardarPartido(partido);
                actualizarListaPartidos(modeloPartidos);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error al agregar partido: " + ex.getMessage());
            }
        });

        btnActualizar.addActionListener(e -> actualizarListaPartidos(modeloPartidos));

        btnEliminar.addActionListener(e -> {
            int filaSeleccionada = tablaPartidos.getSelectedRow();
            if (filaSeleccionada == -1) {
                JOptionPane.showMessageDialog(this, "Seleccione un partido para eliminar");
                return;
            }
            Long idPartido = (Long) modeloPartidos.getValueAt(filaSeleccionada, 0);
            int confirm = JOptionPane.showConfirmDialog(this, "Eliminar partido seleccionado", "Confirmar", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                try {
                    partidoService.eliminarPartido(idPartido);
                    actualizarListaPartidos(modeloPartidos);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Error al eliminar partido: " + ex.getMessage());
                }
            }
        });

        panel.add(formulario, BorderLayout.NORTH);
        panel.add(toolbar, BorderLayout.CENTER);
        panel.add(scroll, BorderLayout.SOUTH);

        actualizarListaPartidos(modeloPartidos);

        return panel;
    }

    // ---- Actualiza la tabla de partidos ----
    private void actualizarListaPartidos(DefaultTableModel modelo) {
        modelo.setRowCount(0);
        List<Partido> partidos = partidoService.listarTodosPartidos();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        for (Partido p : partidos) {
            modelo.addRow(new Object[]{
                    p.getIdPartido(),
                    p.getEquipoLocal() != null ? p.getEquipoLocal().getNombre() : "No especificado",
                    p.getEquipoVisitante() != null ? p.getEquipoVisitante().getNombre() : "No especificado",
                    p.getFechaPartido() != null ? p.getFechaPartido().format(formatter) : "No especificada"
            });
        }
    }

    // ---- Metodo global para recargar combos ----
    private void cargarCombos() {
        // ---- Equipos en jugadores ----
        cmbEquiposJugadores.removeAllItems();
        for (Equipo e : equipoService.listarTodosEquipos()) {
            cmbEquiposJugadores.addItem(e.getNombre());
        }

        // ---- Posiciones ----
        cmbPosiciones.removeAllItems();
        listaPosiciones = posicionService.listarTodasPosiciones();
        for (Posicion p : listaPosiciones) {
            cmbPosiciones.addItem(p.getNombre());
        }

        // ---- Equipos en partidos ----
        cmbEquipoLocal.removeAllItems();
        cmbEquipoVisitante.removeAllItems();
        for (Equipo e : equipoService.listarTodosEquipos()) {
            cmbEquipoLocal.addItem(e);
            cmbEquipoVisitante.addItem(e);
        }

        // ---- Partidos en goles ----
        cmbPartidosGoles.removeAllItems();
        for (Partido p : partidoService.listarTodosPartidos()) {
            cmbPartidosGoles.addItem(p);
        }

        // ---- Jugadores en goles ----
        cmbJugadoresGoles.removeAllItems();
        for (Jugador j : jugadorService.listarTodosJugadores()) {
            cmbJugadoresGoles.addItem(j);
        }
    }





    @Override
    public void dispose() {
        inicializarConexion.cerrar(); //Cierra la conexión con la base de datos
        super.dispose();              //Llama al metodo original de JFrame para cerrar la ventana
    }


}
