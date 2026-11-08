/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package vista;
import controlador.CambioMotorControlador;
import controlador.ClienteControlador;
import controlador.MotorControlador;
import controlador.OrdenTrabajoControlador;
import controlador.VehiculoControlador;

import modelo.CambioMotor;
import modelo.Cliente;
import modelo.Motor;
import modelo.OrdenTrabajo;
import modelo.Vehiculo;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;


/**
 *
 * @author gesle
 */
public class FrmCambioMotor extends javax.swing.JFrame {
    
    private final CambioMotorControlador cambioControlador =
    new CambioMotorControlador();

    private final OrdenTrabajoControlador ordenControlador =
    new OrdenTrabajoControlador();

    private final VehiculoControlador vehiculoControlador =
    new VehiculoControlador();

    private final ClienteControlador clienteControlador =
    new ClienteControlador();

    private final MotorControlador motorControlador =
    new MotorControlador();

    private final Map<String, Motor> motoresCargados = new HashMap<>();

    private boolean cargandoCombos = false;

    private final DateTimeFormatter formatoFecha =
    DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(FrmCambioMotor.class.getName());

    /**
     * Creates new form FrmCambioMotor
     */
    public FrmCambioMotor() {
        initComponents();

        configurarTabla();
        configurarCampos();
        cargarOrdenes();
        cargarMotores();
        cargarTabla();
        configurarEventos();
    }
    public FrmCambioMotor(int idOrden) {
        this();

        cmbOrden.setSelectedItem(String.valueOf(idOrden));
        cargarDatosOrdenSeleccionada();

        setLocationRelativeTo(null);
    }

    private void configurarTabla() {
        String[] columnas = {
            "ID Cambio",
            "Orden",
            "Vehículo",
            "Motor anterior",
            "Motor nuevo",
            "Fecha",
            "Garantía",
            "Resultado"
        };

        DefaultTableModel modelo = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int fila, int columna) {
                return false;
            }
        };

        tablaCambiosMotor.setModel(modelo);
    }

    private void configurarCampos() {
        txtIdCambio.setEditable(false);
        txtVehiculo.setEditable(false);
        txtCliente.setEditable(false);
        txtFechaCambio.setEditable(false);

        txtMarcaMotor.setEditable(false);
        txtModeloMotor.setEditable(false);
        txtAnioMotor.setEditable(false);
        txtSerieMotorNuevo.setEditable(false);
    }

    private void configurarEventos() {
        cmbOrden.addActionListener(e -> {
            if (!cargandoCombos) {
                cargarDatosOrdenSeleccionada();
            }
        });

        cmbMotorNuevo.addActionListener(e -> {
            if (!cargandoCombos) {
                cargarDatosMotorSeleccionado();
            }
        });

        tablaCambiosMotor.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                cargarCambioSeleccionado();
            }
        });
    }

    private void cargarOrdenes() {
        cargandoCombos = true;

        try {
            cmbOrden.removeAllItems();

            List<OrdenTrabajo> ordenes = ordenControlador.listar();

            for (OrdenTrabajo orden : ordenes) {
                cmbOrden.addItem(String.valueOf(orden.getIdOrden()));
            }

            cmbOrden.setSelectedIndex(-1);

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(
                    this,
                    "Error al cargar las órdenes: " + ex.getMessage()
            );
        } finally {
            cargandoCombos = false;
        }
    }

    private void cargarMotores() {
        cargandoCombos = true;

        try {
            cmbMotorNuevo.removeAllItems();
            motoresCargados.clear();

            List<Motor> motores = motorControlador.listarDisponibles();

            for (Motor motor : motores) {
                String descripcion = motor.getIdMotor()
                        + " - " + motor.getMarca()
                        + " " + motor.getModelo()
                        + " (" + motor.getNumeroSerie() + ")";

                cmbMotorNuevo.addItem(descripcion);
                motoresCargados.put(descripcion, motor);
            }

            cmbMotorNuevo.setSelectedIndex(-1);

            limpiarDatosMotor();

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(
                    this,
                    "Error al cargar los motores: " + ex.getMessage()
            );
        } finally {
            cargandoCombos = false;
        }
    }

    private void cargarDatosOrdenSeleccionada() {
        try {
            if (cmbOrden.getSelectedItem() == null) {
                limpiarDatosOrden();
                return;
            }

            int idOrden = Integer.parseInt(
                    cmbOrden.getSelectedItem().toString()
            );

            OrdenTrabajo orden =
                    ordenControlador.buscarPorId(idOrden);

            if (orden == null) {
                limpiarDatosOrden();
                return;
            }

            Vehiculo vehiculo =
                    vehiculoControlador.buscarPorId(orden.getIdVehiculo());

            Cliente cliente =
                    clienteControlador.buscarPorId(orden.getIdCliente());

            if (vehiculo != null) {
                txtVehiculo.setText(
                        vehiculo.getMarca() + " "
                        + vehiculo.getModelo() + " ("
                        + vehiculo.getPlaca() + ")"
                );
            } else {
                txtVehiculo.setText("");
            }

            if (cliente != null) {
                txtCliente.setText(cliente.getNombre());
            } else {
                txtCliente.setText("");
            }

                txtIdCambio.setText("");
                txtFechaCambio.setText(LocalDateTime.now().format(formatoFecha));

                CambioMotor existente = cambioControlador.buscarPorOrden(idOrden);

                if (existente != null) {
                    mostrarCambio(existente);
                }

        } catch (SQLException | NumberFormatException ex) {
            JOptionPane.showMessageDialog(
                    this,
                    "Error al consultar la orden: " + ex.getMessage()
            );
        }
    }

    private void cargarDatosMotorSeleccionado() {
        String seleccionado = (String) cmbMotorNuevo.getSelectedItem();

        if (seleccionado == null) {
            limpiarDatosMotor();
            return;
        }

        Motor motor = motoresCargados.get(seleccionado);

        if (motor != null) {
            txtMarcaMotor.setText(motor.getMarca());
            txtModeloMotor.setText(motor.getModelo());
            txtAnioMotor.setText(String.valueOf(motor.getAnio()));
            txtSerieMotorNuevo.setText(motor.getNumeroSerie());
        }
    }

    private void limpiarDatosOrden() {
        txtVehiculo.setText("");
        txtCliente.setText("");
        txtIdCambio.setText("");
        txtFechaCambio.setText("");
    }

    private void limpiarDatosMotor() {
        txtMarcaMotor.setText("");
        txtModeloMotor.setText("");
        txtAnioMotor.setText("");
        txtSerieMotorNuevo.setText("");
    }

    private void mostrarCambio(CambioMotor cambio) {
        txtIdCambio.setText(String.valueOf(cambio.getIdCambio()));

        if (cambio.getFechaCambio() != null) {
            txtFechaCambio.setText(
                    cambio.getFechaCambio().format(formatoFecha)
            );
        }

        txtSerieAnterior.setText(
                cambio.getNumeroSerieAnterior()
        );

        txtDestinoMotorAnterior.setText(
                cambio.getDestinoMotorAnterior()
        );

        chkAutorizacionCliente.setSelected(
                cambio.isAutorizacionCliente()
        );

        txtGarantiaMeses.setText(
                String.valueOf(cambio.getGarantiaMeses())
        );

        txtPruebasRealizadas.setText(
                cambio.getPruebasRealizadas()
        );

        txtResultadoPruebas.setText(
                cambio.getResultadoPruebas()
        );

        Motor motor = null;

        try {
            motor = motorControlador.buscarPorId(
                    cambio.getIdMotorNuevo()
            );
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(
                    this,
                    "No se pudo consultar el motor: " + ex.getMessage()
            );
        }

        if (motor != null) {
            txtMarcaMotor.setText(motor.getMarca());
            txtModeloMotor.setText(motor.getModelo());
            txtAnioMotor.setText(String.valueOf(motor.getAnio()));
            txtSerieMotorNuevo.setText(motor.getNumeroSerie());
        }
    }

    private void cargarTabla() {
        DefaultTableModel modelo =
                (DefaultTableModel) tablaCambiosMotor.getModel();

        modelo.setRowCount(0);

        try {
            List<OrdenTrabajo> ordenes = ordenControlador.listar();

            for (OrdenTrabajo orden : ordenes) {

                CambioMotor cambio =
                        cambioControlador.buscarPorOrden(orden.getIdOrden());

                if (cambio == null) {
                    continue;
                }

                Vehiculo vehiculo =
                        vehiculoControlador.buscarPorId(orden.getIdVehiculo());

                Motor motor =
                        motorControlador.buscarPorId(cambio.getIdMotorNuevo());

                String nombreVehiculo = "";

                if (vehiculo != null) {
                    nombreVehiculo = vehiculo.getMarca()
                            + " " + vehiculo.getModelo()
                            + " (" + vehiculo.getPlaca() + ")";
                }

                String nombreMotor = "";

                if (motor != null) {
                    nombreMotor = motor.getMarca()
                            + " " + motor.getModelo();
                }

                String fecha = "";

                if (cambio.getFechaCambio() != null) {
                    fecha = cambio.getFechaCambio().format(formatoFecha);
                }

                modelo.addRow(new Object[]{
                    cambio.getIdCambio(),
                    cambio.getIdOrden(),
                    nombreVehiculo,
                    cambio.getNumeroSerieAnterior(),
                    nombreMotor,
                    fecha,
                    cambio.getGarantiaMeses() + " meses",
                    cambio.getResultadoPruebas()
                });
            }

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(
                    this,
                    "Error al cargar los cambios de motor: "
                    + ex.getMessage()
            );
        }
    }

    private void limpiarCampos() {
        txtIdCambio.setText("");
        txtVehiculo.setText("");
        txtCliente.setText("");
        txtFechaCambio.setText("");

        txtSerieAnterior.setText("");
        txtDestinoMotorAnterior.setText("");

        txtMarcaMotor.setText("");
        txtModeloMotor.setText("");
        txtAnioMotor.setText("");
        txtSerieMotorNuevo.setText("");

        txtGarantiaMeses.setText("");
        txtPruebasRealizadas.setText("");
        txtResultadoPruebas.setText("");

        chkAutorizacionCliente.setSelected(false);

        cmbOrden.setSelectedIndex(-1);
        cmbMotorNuevo.setSelectedIndex(-1);

        limpiarDatosOrden();
        limpiarDatosMotor();
    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jScrollPane9 = new javax.swing.JScrollPane();
        tablaCambiosMotor = new javax.swing.JTable();
        btnGuardar = new javax.swing.JButton();
        btnActualizar = new javax.swing.JButton();
        btnLimpiar = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        chkAutorizacionCliente = new javax.swing.JCheckBox();
        jLabel18 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        cmbOrden = new javax.swing.JComboBox<>();
        cmbMotorNuevo = new javax.swing.JComboBox<>();
        txtIdCambio = new javax.swing.JTextField();
        txtVehiculo = new javax.swing.JTextField();
        txtCliente = new javax.swing.JTextField();
        txtFechaCambio = new javax.swing.JTextField();
        txtSerieAnterior = new javax.swing.JTextField();
        txtDestinoMotorAnterior = new javax.swing.JTextField();
        txtModeloMotor = new javax.swing.JTextField();
        txtMarcaMotor = new javax.swing.JTextField();
        txtAnioMotor = new javax.swing.JTextField();
        txtSerieMotorNuevo = new javax.swing.JTextField();
        txtGarantiaMeses = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        txtPruebasRealizadas = new javax.swing.JTextArea();
        jScrollPane2 = new javax.swing.JScrollPane();
        txtResultadoPruebas = new javax.swing.JTextArea();
        btnRegresar = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jLabel5.setText("Orden de trabajo");
        jLabel5.setToolTipText("");

        jLabel6.setText("Fecha del cambio");

        tablaCambiosMotor.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane9.setViewportView(tablaCambiosMotor);

        btnGuardar.setBackground(new java.awt.Color(255, 255, 204));
        btnGuardar.setText("Guardar");
        btnGuardar.addActionListener(this::btnGuardarActionPerformed);

        btnActualizar.setBackground(new java.awt.Color(255, 204, 204));
        btnActualizar.setText("Actualizar");
        btnActualizar.setToolTipText("");
        btnActualizar.addActionListener(this::btnActualizarActionPerformed);

        btnLimpiar.setBackground(new java.awt.Color(204, 255, 204));
        btnLimpiar.setText("Limpiar");
        btnLimpiar.addActionListener(this::btnLimpiarActionPerformed);

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel1.setText("CAMBIO DE MOTOR");

        jLabel2.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel2.setText("Pruebas del motor");

        jLabel4.setText("ID Cambio");

        jLabel8.setText("Vehículo");

        jLabel9.setText("Cliente");

        jLabel3.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel3.setText("Motor anterior");

        jLabel10.setText("Número de serie anterior");

        jLabel11.setText("Destino del motor anterior");
        jLabel11.setToolTipText("");

        jLabel12.setText("Marca del motor");
        jLabel12.setToolTipText("");

        jLabel7.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel7.setText("Motor nuevo");

        jLabel13.setText("Motor disponible");

        jLabel14.setText("Modelo del motor");
        jLabel14.setToolTipText("");

        jLabel15.setText("Año del motor");
        jLabel15.setToolTipText("");

        jLabel16.setText("Número de serie");
        jLabel16.setToolTipText("");

        jLabel17.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel17.setText("Identificación del cambio");

        chkAutorizacionCliente.setText("Autorización del cliente");

        jLabel18.setText("Garantía (meses)");

        jLabel19.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel19.setText("Autorización y garantía");

        jLabel20.setText("Pruebas realizadas");

        jLabel21.setText("Resultado de las pruebas");

        cmbOrden.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        cmbMotorNuevo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        txtPruebasRealizadas.setColumns(20);
        txtPruebasRealizadas.setRows(5);
        jScrollPane1.setViewportView(txtPruebasRealizadas);

        txtResultadoPruebas.setColumns(20);
        txtResultadoPruebas.setRows(5);
        jScrollPane2.setViewportView(txtResultadoPruebas);

        btnRegresar.setBackground(new java.awt.Color(204, 255, 255));
        btnRegresar.setText("Menu");
        btnRegresar.addActionListener(this::btnRegresarActionPerformed);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(49, 49, 49)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel20)
                                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 166, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(74, 74, 74)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel21)
                                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 166, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addComponent(jLabel17)
                            .addComponent(jLabel18)
                            .addComponent(jLabel19)
                            .addComponent(jLabel2)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel4)
                                    .addComponent(jLabel8)
                                    .addComponent(jLabel6)
                                    .addComponent(txtIdCambio, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtVehiculo, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(38, 38, 38)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txtCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel9)
                                    .addComponent(jLabel5)
                                    .addComponent(cmbOrden, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(txtGarantiaMeses, javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(chkAutorizacionCliente, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addComponent(txtFechaCambio, javax.swing.GroupLayout.PREFERRED_SIZE, 254, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(42, 42, 42)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel12)
                                    .addComponent(jLabel7)
                                    .addComponent(jLabel13)
                                    .addComponent(jLabel15))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txtSerieMotorNuevo, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel16)
                                    .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 139, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtModeloMotor, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(txtAnioMotor, javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txtMarcaMotor))
                                .addGap(149, 149, 149))
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel3)
                                    .addGroup(layout.createSequentialGroup()
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                            .addComponent(txtSerieAnterior, javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(cmbMotorNuevo, javax.swing.GroupLayout.Alignment.LEADING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(jLabel10, javax.swing.GroupLayout.Alignment.LEADING))
                                        .addGap(38, 38, 38)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addComponent(jLabel11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(txtDestinoMotorAnterior, javax.swing.GroupLayout.PREFERRED_SIZE, 139, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                    .addGroup(layout.createSequentialGroup()
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addComponent(btnGuardar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(btnLimpiar, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGap(18, 18, 18)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addComponent(btnActualizar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(btnRegresar, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE))))
                                .addGap(0, 0, Short.MAX_VALUE))))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(28, 28, 28)
                                .addComponent(jScrollPane9, javax.swing.GroupLayout.PREFERRED_SIZE, 905, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(338, 338, 338)
                                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 267, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 13, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(29, 29, 29)
                .addComponent(jLabel1)
                .addGap(65, 65, 65)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(jLabel17))
                .addGap(12, 12, 12)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel4)
                            .addComponent(jLabel5))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(cmbOrden, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtIdCambio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(12, 12, 12)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel8)
                            .addComponent(jLabel9))
                        .addGap(3, 3, 3)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtVehiculo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtCliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addComponent(jLabel6)
                        .addGap(12, 12, 12)
                        .addComponent(txtFechaCambio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel19)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(chkAutorizacionCliente)
                        .addGap(6, 6, 6)
                        .addComponent(jLabel18)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtGarantiaMeses, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(2, 2, 2)
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel20)
                            .addComponent(jLabel21)))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel10)
                            .addComponent(jLabel11))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtSerieAnterior, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtDestinoMotorAnterior, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel7)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel13)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cmbMotorNuevo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(21, 21, 21)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel14)
                            .addComponent(jLabel12))
                        .addGap(12, 12, 12)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtModeloMotor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtMarcaMotor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel15)
                            .addComponent(jLabel16))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtAnioMotor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtSerieMotorNuevo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnGuardar)
                            .addComponent(btnActualizar))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnLimpiar)
                            .addComponent(btnRegresar))))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane9, javax.swing.GroupLayout.PREFERRED_SIZE, 436, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(21, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnGuardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGuardarActionPerformed
        try {
            if (cmbOrden.getSelectedItem() == null) {
                JOptionPane.showMessageDialog(this,
                        "Seleccione una orden de trabajo.");
                return;
            }

            String motorSeleccionado =
                    (String) cmbMotorNuevo.getSelectedItem();

            if (motorSeleccionado == null) {
                JOptionPane.showMessageDialog(this,
                        "Seleccione el motor nuevo.");
                return;
            }

            Motor motor = motoresCargados.get(motorSeleccionado);

            if (motor == null) {
                JOptionPane.showMessageDialog(this,
                        "No se encontró el motor seleccionado.");
                return;
            }

            int idOrden = Integer.parseInt(
                    cmbOrden.getSelectedItem().toString()
            );

            // Evitar registrar dos cambios para la misma orden.
            CambioMotor existente =
                    cambioControlador.buscarPorOrden(idOrden);

            if (existente != null) {
                JOptionPane.showMessageDialog(this,
                        "Esta orden ya tiene un cambio de motor registrado.");
                mostrarCambio(existente);
                return;
            }

            if (txtSerieAnterior.getText().trim().isEmpty()
                    || txtDestinoMotorAnterior.getText().trim().isEmpty()
                    || txtGarantiaMeses.getText().trim().isEmpty()) {

                JOptionPane.showMessageDialog(this,
                        "Complete los datos del motor anterior y la garantía.");
                return;
            }

            if (!chkAutorizacionCliente.isSelected()) {
                JOptionPane.showMessageDialog(this,
                        "Debe confirmar la autorización del cliente.");
                return;
            }

            int garantia = Integer.parseInt(
                    txtGarantiaMeses.getText().trim()
            );

            if (garantia <= 0) {
                JOptionPane.showMessageDialog(this,
                        "La garantía debe ser mayor que cero.");
                return;
            }

            int confirmacion = JOptionPane.showConfirmDialog(
                    this,
                    "¿Desea registrar el cambio de motor?",
                    "Confirmar registro",
                    JOptionPane.YES_NO_OPTION
            );

            if (confirmacion != JOptionPane.YES_OPTION) {
                return;
            }

            OrdenTrabajo orden =
                    ordenControlador.buscarPorId(idOrden);

            if (orden == null) {
                JOptionPane.showMessageDialog(this,
                        "No se encontró la orden seleccionada.");
                return;
            }

            Vehiculo vehiculo =
                    vehiculoControlador.buscarPorId(orden.getIdVehiculo());

            if (vehiculo == null) {
                JOptionPane.showMessageDialog(this,
                        "No se encontró el vehículo de esta orden.");
                return;
            }

            CambioMotor cambio = new CambioMotor();

            cambio.setIdOrden(idOrden);
            cambio.setIdMotorNuevo(motor.getIdMotor());

            cambio.setNumeroSerieAnterior(
                    txtSerieAnterior.getText().trim()
            );

            cambio.setDestinoMotorAnterior(
                    txtDestinoMotorAnterior.getText().trim()
            );

            cambio.setAutorizacionCliente(true);
            cambio.setGarantiaMeses(garantia);

            cambio.setPruebasRealizadas(
                    txtPruebasRealizadas.getText().trim()
            );

            cambio.setResultadoPruebas(
                    txtResultadoPruebas.getText().trim()
            );

            boolean registrado =
                    cambioControlador.registrarCambio(cambio, vehiculo);

            if (registrado) {

                CambioMotor guardado =
                        cambioControlador.buscarPorOrden(idOrden);

                if (guardado != null) {
                    mostrarCambio(guardado);
                }

                cargarMotores();
                cargarTabla();

                JOptionPane.showMessageDialog(this,
                        "Cambio de motor registrado correctamente.");

            } else {
                JOptionPane.showMessageDialog(this,
                        "No se pudo registrar el cambio de motor.");
            }

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this,
                    "La garantía debe contener un número válido.");

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this,
                    "Error al registrar el cambio: " + ex.getMessage());

        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this,
                    ex.getMessage());
        }
    }//GEN-LAST:event_btnGuardarActionPerformed
    private void cargarCambioSeleccionado() {
        int fila = tablaCambiosMotor.getSelectedRow();

        if (fila == -1) {
            return;
        }

        try {
            int idOrden = Integer.parseInt(
                    tablaCambiosMotor.getValueAt(fila, 1).toString()
            );

            cmbOrden.setSelectedItem(String.valueOf(idOrden));

            // Carga los datos del cambio registrado
            cargarDatosOrdenSeleccionada();

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this,
                    "No se pudo identificar la orden seleccionada.");
        }
    }

    private void btnActualizarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnActualizarActionPerformed

        cargarOrdenes();
        cargarMotores();
        cargarTabla();

        JOptionPane.showMessageDialog(this,
                "Información actualizada correctamente.");
    }//GEN-LAST:event_btnActualizarActionPerformed

    private void btnLimpiarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLimpiarActionPerformed
        limpiarCampos();
    }//GEN-LAST:event_btnLimpiarActionPerformed

    private void btnRegresarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRegresarActionPerformed
        FrmPrincipal principal = new FrmPrincipal();
        principal.setLocationRelativeTo(null);
        principal.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_btnRegresarActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ReflectiveOperationException | javax.swing.UnsupportedLookAndFeelException ex) {
            logger.log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> new FrmCambioMotor().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnActualizar;
    private javax.swing.JButton btnGuardar;
    private javax.swing.JButton btnLimpiar;
    private javax.swing.JButton btnRegresar;
    private javax.swing.JCheckBox chkAutorizacionCliente;
    private javax.swing.JComboBox<String> cmbMotorNuevo;
    private javax.swing.JComboBox<String> cmbOrden;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane9;
    private javax.swing.JTable tablaCambiosMotor;
    private javax.swing.JTextField txtAnioMotor;
    private javax.swing.JTextField txtCliente;
    private javax.swing.JTextField txtDestinoMotorAnterior;
    private javax.swing.JTextField txtFechaCambio;
    private javax.swing.JTextField txtGarantiaMeses;
    private javax.swing.JTextField txtIdCambio;
    private javax.swing.JTextField txtMarcaMotor;
    private javax.swing.JTextField txtModeloMotor;
    private javax.swing.JTextArea txtPruebasRealizadas;
    private javax.swing.JTextArea txtResultadoPruebas;
    private javax.swing.JTextField txtSerieAnterior;
    private javax.swing.JTextField txtSerieMotorNuevo;
    private javax.swing.JTextField txtVehiculo;
    // End of variables declaration//GEN-END:variables
}
