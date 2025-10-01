package vistas;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.List;
import com.toedter.calendar.JDateChooser;

import servicios.*;
import entidades.*;
import jakarta.persistence.EntityManager;
import DAO.inicializarConexion;

public class inicio extends JFrame {

    // ---- Servicios ----
    private EquipoServiceImpl equipoService;
    private JugadorServiceImpl jugadorService;
    private PartidoServiceImpl partidoService;
    private GolServiceImpl golService;
    private PosicionService posicionService;

    // ---- Componentes principales ----
    private JTabbedPane tabbedPane1;
    private JPanel panelPrincipal;
    private DefaultTableModel modeloEquipos, modeloJugadores, modeloPartidos, modeloGoles;
    private JComboBox<String> cmbEquiposJugadores, cmbPosiciones;
    private JComboBox<Equipo> cmbEquipoLocal, cmbEquipoVisitante;
    private JComboBox<Partido> cmbPartidosGoles;
    private JComboBox<Jugador> cmbJugadoresGoles;
    private List<Posicion> listaPosiciones = new ArrayList<>();

    // ---- MAIN ----
    public static void main(String[] args){
        try{ UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); }catch(Exception ignored){}
        SwingUtilities.invokeLater(() -> new inicio().setVisible(true));
    }

    // ---- Constructor ----
    public inicio() {
        configurarVentana();
        inicializarServicios();
        inicializarComponentes();
        configurarPestanias();
    }

    // ---- Configuración general de ventana ----
    private void configurarVentana() {
        setTitle("Torneo de Fútbol");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 800);
        setLocationRelativeTo(null);
    }

    // ---- Inicializa servicios con conexión BD ----
    private void inicializarServicios() {
        try {
            EntityManager em = inicializarConexion.getEntityManager();
            equipoService = new EquipoServiceImpl(em);
            jugadorService = new JugadorServiceImpl(em);
            partidoService = new PartidoServiceImpl(em);
            golService = new GolServiceImpl(em);
            posicionService = new PosicionServiceImpl(em);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,"Error al conectar con la base de datos: " + e.getMessage(),
                    "Error de Conexión", JOptionPane.ERROR_MESSAGE);
        }
    }

    // ---- Panel raíz con pestañas ----
    private void inicializarComponentes() {
        panelPrincipal = new JPanel(new BorderLayout());
        tabbedPane1 = new JTabbedPane();
        panelPrincipal.add(tabbedPane1, BorderLayout.CENTER);
        setContentPane(panelPrincipal);
    }

    // ---- Configuración de pestañas ----
    private void configurarPestanias() {
        tabbedPane1.addTab("Inicio", crearPanelInicio());
        tabbedPane1.addTab("Equipos", crearPanelEquipos());
        tabbedPane1.addTab("Jugadores", crearPanelJugadores());
        tabbedPane1.addTab("Partidos", crearPanelPartidos());
        tabbedPane1.addTab("Goles", crearPanelGoles());
        tabbedPane1.addChangeListener(e -> cargarCombos());
    }

    // ----------------> PANEL INICIO <----------------
    private JPanel crearPanelInicio() {
        JPanel panel = new JPanel(new BorderLayout(10,10));
        panel.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));
        JLabel lblTitulo = new JLabel("Torneo de Futbol", JLabel.CENTER);

        ImageIcon icon = new ImageIcon(getClass().getResource("/img/futbol.png"));
        Image img = icon.getImage().getScaledInstance(250, 250, Image.SCALE_SMOOTH);
        JLabel lblImagen = new JLabel(new ImageIcon(img), JLabel.CENTER);

        JPanel mainPanel = new JPanel(new GridLayout(1,1));
        mainPanel.add(lblImagen);

        panel.add(lblTitulo, BorderLayout.NORTH);
        panel.add(mainPanel, BorderLayout.CENTER);
        return panel;
    }

    // ----------------> PANEL EQUIPOS <----------------
    private JPanel crearPanelEquipos() {
        JPanel panel = new JPanel(new BorderLayout(10,10));
        JPanel toolbar = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JTextField txtNombre = new JTextField(20);
        JButton btnAgregar = new JButton("Agregar");
        JButton btnEliminar = new JButton("Eliminar");
        JButton btnActualizar = new JButton("Actualizar");

        toolbar.add(new JLabel("Nombre del equipo:")); toolbar.add(txtNombre);
        toolbar.add(btnAgregar); toolbar.add(btnEliminar); toolbar.add(btnActualizar);

        modeloEquipos = new DefaultTableModel(new String[]{"ID","Nombre"}, 0){
            @Override public boolean isCellEditable(int r,int c){ return false; }
        };
        JTable tabla = new JTable(modeloEquipos);

        btnAgregar.addActionListener(e -> agregarEquipo(txtNombre));
        btnActualizar.addActionListener(e -> actualizarListaEquipos());
        btnEliminar.addActionListener(e -> eliminarEquipo(tabla));

        panel.add(toolbar, BorderLayout.NORTH);
        panel.add(new JScrollPane(tabla), BorderLayout.CENTER);
        actualizarListaEquipos();
        return panel;
    }

    private void agregarEquipo(JTextField txtNombre) {
        String nombre = txtNombre.getText().trim();
        if(nombre.isEmpty()){ JOptionPane.showMessageDialog(this,"Ingrese un nombre"); return; }
        equipoService.guardarEquipo(new Equipo(nombre));
        txtNombre.setText("");
        actualizarListaEquipos();
        actualizarCombosJugadores();
    }

    private void eliminarEquipo(JTable tabla) {
        int fila = tabla.getSelectedRow();
        if(fila==-1){ JOptionPane.showMessageDialog(this,"Seleccione un equipo"); return; }
        Long id = (Long) modeloEquipos.getValueAt(fila,0);
        if(JOptionPane.showConfirmDialog(this,"Eliminar equipo?","Confirmar",JOptionPane.YES_NO_OPTION)==JOptionPane.YES_OPTION){
            equipoService.eliminarEquipo(id);
            actualizarListaEquipos();
            actualizarCombosJugadores();
        }
    }

    private void actualizarListaEquipos(){
        modeloEquipos.setRowCount(0);
        for(Equipo eq: equipoService.listarTodosEquipos()){
            modeloEquipos.addRow(new Object[]{eq.getIdEquipo(), eq.getNombre()});
        }
    }

    // ----------------> PANEL JUGADORES <----------------
    private JPanel crearPanelJugadores() {
        JPanel panel = new JPanel(new BorderLayout(10,10));
        JPanel form = new JPanel(new GridLayout(4,2,10,10));
        JTextField txtNombre = new JTextField();
        cmbEquiposJugadores = new JComboBox<>();
        cmbPosiciones = new JComboBox<>();
        JDateChooser fechaNacimiento = new JDateChooser();
        fechaNacimiento.setDateFormatString("dd/MM/yyyy");

        form.add(new JLabel("Nombre:")); form.add(txtNombre);
        form.add(new JLabel("Equipo:")); form.add(cmbEquiposJugadores);
        form.add(new JLabel("Posición:")); form.add(cmbPosiciones);
        form.add(new JLabel("Fecha Nacimiento:")); form.add(fechaNacimiento);

        JPanel toolbar = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton btnAgregar = new JButton("Agregar");
        JButton btnEliminar = new JButton("Eliminar");
        JButton btnActualizar = new JButton("Actualizar");
        toolbar.add(btnAgregar); toolbar.add(btnEliminar); toolbar.add(btnActualizar);

        modeloJugadores = new DefaultTableModel(
                new String[]{"ID","Nombre","Equipo","Posición","Fecha Nacimiento"},0){
            @Override public boolean isCellEditable(int r,int c){ return false; }
        };
        JTable tabla = new JTable(modeloJugadores);

        btnAgregar.addActionListener(e -> agregarJugador(txtNombre,fechaNacimiento));
        btnEliminar.addActionListener(e -> eliminarJugador(tabla));
        btnActualizar.addActionListener(e -> { actualizarCombosJugadores(); actualizarListaJugadores(); });

        panel.add(form,BorderLayout.NORTH);
        panel.add(toolbar,BorderLayout.CENTER);
        panel.add(new JScrollPane(tabla),BorderLayout.SOUTH);

        actualizarCombosJugadores();
        actualizarListaJugadores();
        return panel;
    }

    private void agregarJugador(JTextField txtNombre, JDateChooser fechaNacimiento) {
        String nombre = txtNombre.getText().trim();
        Date fecha = fechaNacimiento.getDate();
        if(nombre.isEmpty()||fecha==null){ JOptionPane.showMessageDialog(this,"Complete datos"); return; }
        if(fecha.after(new Date())){ JOptionPane.showMessageDialog(this,"Fecha inválida"); return; }

        Jugador j = new Jugador();
        j.setNombre(nombre);
        j.setFechaNacimiento(fecha.toInstant().atZone(ZoneId.systemDefault()).toLocalDate());

        Equipo equipo = equipoService.listarTodosEquipos().stream()
                .filter(e->e.getNombre().equals(cmbEquiposJugadores.getSelectedItem()))
                .findFirst().orElse(null);
        Posicion pos = listaPosiciones.stream()
                .filter(p->p.getNombre().equals(cmbPosiciones.getSelectedItem()))
                .findFirst().orElse(null);

        if(equipo==null||pos==null){ JOptionPane.showMessageDialog(this,"Seleccione equipo/posición"); return; }
        j.setEquipo(equipo); j.setPosicion(pos);

        jugadorService.guardarJugador(j);
        txtNombre.setText(""); fechaNacimiento.setDate(null);
        actualizarListaJugadores();
    }

    private void eliminarJugador(JTable tabla) {
        int fila = tabla.getSelectedRow();
        if(fila==-1){ JOptionPane.showMessageDialog(this,"Seleccione un jugador"); return; }
        Long id = Long.valueOf(modeloJugadores.getValueAt(fila,0).toString());
        if(JOptionPane.showConfirmDialog(this,"Eliminar jugador?","Confirmar",JOptionPane.YES_NO_OPTION)==JOptionPane.YES_OPTION){
            jugadorService.eliminarJugador(id);
            actualizarListaJugadores();
        }
    }

    private void actualizarListaJugadores() {
        modeloJugadores.setRowCount(0);
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        for(Jugador j: jugadorService.listarTodosJugadores()){
            modeloJugadores.addRow(new Object[]{
                    j.getIdJugador(),
                    j.getNombre(),
                    j.getEquipo()!=null?j.getEquipo().getNombre():"Sin equipo",
                    j.getPosicion()!=null?j.getPosicion().getNombre():"Sin posición",
                    j.getFechaNacimiento()!=null?j.getFechaNacimiento().format(fmt):"No especificada"
            });
        }
    }

    private void actualizarCombosJugadores(){
        cmbEquiposJugadores.removeAllItems();
        cmbPosiciones.removeAllItems();
        for(Equipo e: equipoService.listarTodosEquipos()) cmbEquiposJugadores.addItem(e.getNombre());
        listaPosiciones = posicionService.listarTodasPosiciones();
        for(Posicion p: listaPosiciones) cmbPosiciones.addItem(p.getNombre());
    }

    // ----------------> PANEL PARTIDOS <----------------
    private JPanel crearPanelPartidos() {
        JPanel panel = new JPanel(new BorderLayout(10,10));
        JPanel form = new JPanel(new GridLayout(3,2,10,10));
        cmbEquipoLocal = new JComboBox<>(); cmbEquipoVisitante = new JComboBox<>();
        JDateChooser fechaPartido = new JDateChooser(); fechaPartido.setDateFormatString("dd/MM/yyyy");

        form.add(new JLabel("Equipo Local:")); form.add(cmbEquipoLocal);
        form.add(new JLabel("Equipo Visitante:")); form.add(cmbEquipoVisitante);
        form.add(new JLabel("Fecha:")); form.add(fechaPartido);

        JPanel toolbar = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton btnAgregar = new JButton("Agregar"); JButton btnEliminar = new JButton("Eliminar");
        JButton btnActualizar = new JButton("Actualizar");
        toolbar.add(btnAgregar); toolbar.add(btnEliminar); toolbar.add(btnActualizar);

        modeloPartidos = new DefaultTableModel(new String[]{"ID","Local","Visitante","Fecha"},0){
            @Override public boolean isCellEditable(int r,int c){ return false; }
        };
        JTable tabla = new JTable(modeloPartidos);

        btnAgregar.addActionListener(e -> {
            Equipo local = (Equipo)cmbEquipoLocal.getSelectedItem();
            Equipo visitante = (Equipo)cmbEquipoVisitante.getSelectedItem();
            Date fecha = fechaPartido.getDate();
            if(local==null||visitante==null||fecha==null){ JOptionPane.showMessageDialog(this,"Complete todos los campos"); return; }
            if(local.equals(visitante)){ JOptionPane.showMessageDialog(this,"Local y visitante no pueden ser iguales"); return; }

            Partido p = new Partido(); p.setEquipoLocal(local); p.setEquipoVisitante(visitante);
            p.setFechaPartido(fecha.toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
            partidoService.guardarPartido(p);
            actualizarListaPartidos();
        });

        btnActualizar.addActionListener(e -> actualizarListaPartidos());
        btnEliminar.addActionListener(e -> {
            int fila = tabla.getSelectedRow(); if(fila==-1){ JOptionPane.showMessageDialog(this,"Seleccione partido"); return; }
            Long id = (Long)modeloPartidos.getValueAt(fila,0);
            if(JOptionPane.showConfirmDialog(this,"Eliminar partido?","Confirmar",JOptionPane.YES_NO_OPTION)==JOptionPane.YES_OPTION){
                partidoService.eliminarPartido(id); actualizarListaPartidos();
            }
        });

        panel.add(form,BorderLayout.NORTH);
        panel.add(toolbar,BorderLayout.CENTER);
        panel.add(new JScrollPane(tabla),BorderLayout.SOUTH);
        actualizarListaPartidos();
        return panel;
    }

    private void actualizarListaPartidos() {
        modeloPartidos.setRowCount(0);
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        for(Partido p: partidoService.listarTodosPartidos()){
            modeloPartidos.addRow(new Object[]{
                    p.getIdPartido(),
                    p.getEquipoLocal().getNombre(),
                    p.getEquipoVisitante().getNombre(),
                    p.getFechaPartido().format(fmt)
            });
        }
    }

    // ----------------> PANEL GOLES <----------------
    private JPanel crearPanelGoles() {
        JPanel panel = new JPanel(new BorderLayout(10,10));
        JPanel form = new JPanel(new GridLayout(3,2,10,10)); // 3 filas: Partido, Jugador, Minuto
        cmbPartidosGoles = new JComboBox<>();
        cmbJugadoresGoles = new JComboBox<>();
        JTextField txtMinuto = new JTextField();

        form.add(new JLabel("Partido:")); form.add(cmbPartidosGoles);
        form.add(new JLabel("Jugador:")); form.add(cmbJugadoresGoles);
        form.add(new JLabel("Minuto:")); form.add(txtMinuto);

        JPanel toolbar = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton btnAgregar = new JButton("Agregar");
        JButton btnEliminar = new JButton("Eliminar");
        JButton btnActualizar = new JButton("Actualizar");
        toolbar.add(btnAgregar); toolbar.add(btnEliminar); toolbar.add(btnActualizar);

        modeloGoles = new DefaultTableModel(new String[]{"ID","Partido","Jugador","Minuto"},0){
            @Override public boolean isCellEditable(int r,int c){ return false; }
        };
        JTable tabla = new JTable(modeloGoles);

        // ---- BOTONES ----
        btnAgregar.addActionListener(e -> {
            Partido partido = (Partido)cmbPartidosGoles.getSelectedItem();
            Jugador jugador = (Jugador)cmbJugadoresGoles.getSelectedItem();
            String strMinuto = txtMinuto.getText().trim();
            if(partido==null || jugador==null || strMinuto.isEmpty()){
                JOptionPane.showMessageDialog(this,"Complete todos los campos");
                return;
            }
            try {
                int minuto = Integer.parseInt(strMinuto);
                Gol gol = new Gol();
                gol.setPartido(partido);
                gol.setJugador(jugador);
                gol.setMinuto(minuto);
                golService.guardarGol(gol);
                txtMinuto.setText("");
                actualizarListaGoles();
            } catch(NumberFormatException ex){
                JOptionPane.showMessageDialog(this,"El minuto debe ser un número entero");
            } catch(Exception ex){
                JOptionPane.showMessageDialog(this,"Error al agregar gol: "+ex.getMessage());
            }
        });

        btnActualizar.addActionListener(e -> actualizarListaGoles());

        btnEliminar.addActionListener(e -> {
            int fila = tabla.getSelectedRow();
            if(fila==-1){ JOptionPane.showMessageDialog(this,"Seleccione un gol"); return; }
            Long id = (Long)modeloGoles.getValueAt(fila,0);
            if(JOptionPane.showConfirmDialog(this,"Eliminar gol?","Confirmar",JOptionPane.YES_NO_OPTION)==JOptionPane.YES_OPTION){
                golService.eliminarGol(id);
                actualizarListaGoles();
            }
        });

        panel.add(form,BorderLayout.NORTH);
        panel.add(toolbar,BorderLayout.CENTER);
        panel.add(new JScrollPane(tabla),BorderLayout.SOUTH);
        actualizarListaGoles();
        return panel;
    }

    // ---- Actualiza la tabla de goles ----
    private void actualizarListaGoles() {
        modeloGoles.setRowCount(0);
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        for(Gol g: golService.listarTodosGoles()){
            String partidoStr = g.getPartido()!=null
                    ? g.getPartido().getEquipoLocal().getNombre() + " vs " + g.getPartido().getEquipoVisitante().getNombre()
                    + " (" + g.getPartido().getFechaPartido().format(fmt) + ")"
                    : "No especificado";
            String jugadorStr = g.getJugador()!=null ? g.getJugador().getNombre():"No especificado";
            modeloGoles.addRow(new Object[]{g.getIdGol(), partidoStr, jugadorStr, g.getMinuto()});
        }
    }


    // ---- Carga del combos ----
    private void cargarCombos() {
        actualizarCombosJugadores();
        cmbEquipoLocal.removeAllItems(); cmbEquipoVisitante.removeAllItems();
        for (Equipo e : equipoService.listarTodosEquipos()) { cmbEquipoLocal.addItem(e); cmbEquipoVisitante.addItem(e); }
        cmbPartidosGoles.removeAllItems();
        for (Partido p : partidoService.listarTodosPartidos()) cmbPartidosGoles.addItem(p);
        cmbJugadoresGoles.removeAllItems();
        for (Jugador j : jugadorService.listarTodosJugadores()) cmbJugadoresGoles.addItem(j);
    }

    // ---- Cierra conexión BD al cerrar ventana ----
    @Override
    public void dispose() {
        inicializarConexion.cerrar();
        super.dispose();
    }
}
