/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package vista;
import controlador.OrdenTrabajoControlador;
import controlador.ClienteControlador;
import controlador.VehiculoControlador;
import controlador.MecanicoControlador;

import modelo.OrdenTrabajo;
import modelo.Cliente;
import modelo.Vehiculo;
import modelo.Mecanico;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
/**
 *
 * @author gesle
 */
public class FrmOrdenesTrabajo extends javax.swing.JFrame {
    
    private final OrdenTrabajoControlador controlador = new OrdenTrabajoControlador();
    private final ClienteControlador clienteControlador = new ClienteControlador();
    private final VehiculoControlador vehiculoControlador = new VehiculoControlador();
    private final MecanicoControlador mecanicoControlador = new MecanicoControlador();

    private DefaultTableModel modeloTabla;
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(FrmOrdenesTrabajo.class.getName());

    /**
     * Creates new form FrmOrdenesTrabajo
     */
    public FrmOrdenesTrabajo() {
        initComponents();

        jScrollPane6.setPreferredSize(new java.awt.Dimension(225, 60));
        jScrollPane8.setPreferredSize(new java.awt.Dimension(225, 75));
        
        txtIdOrden.setEditable(false);
        txtFecha.setEditable(false);

        modeloTabla = (DefaultTableModel) tablaOrdenes.getModel();

        modeloTabla.setColumnIdentifiers(new String[]{
            "ID", "Cliente", "Vehículo", "Mecánico",
            "Fecha", "Kilometraje", "Síntomas", "Estado"
        });

        cmbEstado.setModel(new javax.swing.DefaultComboBoxModel<>(
            new String[]{
                "RECIBIDA",
                "EN DIAGNÓSTICO",
                "PENDIENTE DE APROBACIÓN",
                "EN REPARACIÓN",
                "EN CONTROL DE CALIDAD",
                "FINALIZADA Y ENTREGADA",
                "RECHAZADA",
                "CANCELADA"
            }
        ));

        txtFecha.setText(LocalDateTime.now().format(
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        ));

        cargarClientes();
        cargarMecanicos();
        cargarOrdenes();

        cmbCliente.addActionListener(e -> cargarVehiculos());

        tablaOrdenes.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                seleccionarOrden();
            }
        });
    }

    private int obtenerIdOrdenSeleccionada() {
        int fila = tablaOrdenes.getSelectedRow();

        if (fila == -1) {
            JOptionPane.showMessageDialog(this,
                    "Primero seleccione una orden de trabajo.");
            return -1;
        }

        return Integer.parseInt(
                tablaOrdenes.getValueAt(fila, 0).toString()
        );
    }
    private void cargarClientes() {
        try {
            cmbCliente.removeAllItems();

            List<Cliente> clientes = clienteControlador.listar();

            for (Cliente c : clientes) {
                if ("ACTIVO".equalsIgnoreCase(c.getEstado())) {
                    cmbCliente.addItem(
                        c.getIdCliente() + " - " + c.getNombre()
                    );
                }
            }

            cargarVehiculos();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this,
                    "Error al cargar clientes: " + e.getMessage());
        }
    }

    private void cargarVehiculos() {
        try {
            cmbVehiculo.removeAllItems();

            if (cmbCliente.getSelectedItem() == null) {
                return;
            }

            int idCliente = Integer.parseInt(
                cmbCliente.getSelectedItem().toString().split(" - ")[0]
            );

            List<Vehiculo> vehiculos =
                    vehiculoControlador.listarPorCliente(idCliente);

            for (Vehiculo v : vehiculos) {
                cmbVehiculo.addItem(
                    v.getIdVehiculo() + " - " +
                    v.getMarca() + " " + v.getModelo() +
                    " (" + v.getPlaca() + ")"
                );
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this,
                    "Error al cargar vehículos: " + e.getMessage());
        }
    }


    private void cargarMecanicos() {
        try {
            cmbMecanico.removeAllItems();

            List<Mecanico> mecanicos = mecanicoControlador.listar();

            for (Mecanico m : mecanicos) {
                if ("ACTIVO".equalsIgnoreCase(m.getEstado())) {
                    cmbMecanico.addItem(
                        m.getIdMecanico() + " - " + m.getNombre()
                    );
                }
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this,
                    "Error al cargar mecánicos: " + e.getMessage());
        }
    }

    private void cargarOrdenes() {
        try {
            modeloTabla.setRowCount(0);

            List<OrdenTrabajo> ordenes = controlador.listar();

            for (OrdenTrabajo o : ordenes) {

                Cliente cliente = clienteControlador.buscarPorId(o.getIdCliente());
                Vehiculo vehiculo = vehiculoControlador.buscarPorId(o.getIdVehiculo());
                Mecanico mecanico = mecanicoControlador.buscarPorId(
                        o.getIdMecanicoResponsable()
                );

                String nombreCliente = cliente != null
                        ? cliente.getNombre() : "Sin cliente";

                String nombreVehiculo = vehiculo != null
                        ? vehiculo.getMarca() + " " + vehiculo.getModelo()
                        : "Sin vehículo";

                String nombreMecanico = mecanico != null
                        ? mecanico.getNombre() : "Sin mecánico";

                modeloTabla.addRow(new Object[]{
                    o.getIdOrden(),
                    nombreCliente,
                    nombreVehiculo,
                    nombreMecanico,
                    o.getFecha(),
                    o.getKilometraje(),
                    o.getSintomas(),
                    o.getEstado()
                });
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this,
                    "Error al cargar órdenes: " + e.getMessage());
        }
    }

    private void seleccionarOrden() {
        int fila = tablaOrdenes.getSelectedRow();

        if (fila == -1) {
            return;
        }

        try {
            int id = Integer.parseInt(
                    tablaOrdenes.getValueAt(fila, 0).toString()
            );

            OrdenTrabajo orden = controlador.buscarPorId(id);

            if (orden != null) {
                txtIdOrden.setText(String.valueOf(orden.getIdOrden()));

                txtFecha.setText(orden.getFecha() != null
                        ? orden.getFecha().format(
                                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
                        : "");

                txtKilometraje.setText(
                        String.valueOf(orden.getKilometraje()));

                txtSintomas.setText(orden.getSintomas());

                txtDiagnostico.setText(
                        orden.getDiagnosticoPreliminar());

                cmbEstado.setSelectedItem(orden.getEstado());

                // Seleccionar cliente
                cmbCliente.setSelectedItem(
                        buscarElemento(cmbCliente, orden.getIdCliente()));

                // Recargar vehículos del cliente seleccionado
                cargarVehiculos();

                // Seleccionar vehículo
                cmbVehiculo.setSelectedItem(
                        buscarElemento(cmbVehiculo, orden.getIdVehiculo()));

                // Seleccionar mecánico
                cmbMecanico.setSelectedItem(
                        buscarElemento(cmbMecanico,
                                orden.getIdMecanicoResponsable()));
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this,
                    "Error al seleccionar la orden: " + e.getMessage());
        }
    }

    private String buscarElemento(
            javax.swing.JComboBox<String> combo, int id) {

        for (int i = 0; i < combo.getItemCount(); i++) {
            String elemento = combo.getItemAt(i);

            if (elemento.startsWith(id + " - ")) {
                return elemento;
            }
        }

        return null;
    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel6 = new javax.swing.JLabel();
        jScrollPane5 = new javax.swing.JScrollPane();
        txtFecha = new javax.swing.JTextPane();
        jScrollPane9 = new javax.swing.JScrollPane();
        tablaOrdenes = new javax.swing.JTable();
        btnLimpiar = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        txtIdOrden = new javax.swing.JTextPane();
        btnGuardar = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        btnActualizar = new javax.swing.JButton();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jScrollPane6 = new javax.swing.JScrollPane();
        txtSintomas = new javax.swing.JTextPane();
        jScrollPane7 = new javax.swing.JScrollPane();
        txtKilometraje = new javax.swing.JTextPane();
        jScrollPane8 = new javax.swing.JScrollPane();
        txtDiagnostico = new javax.swing.JTextPane();
        jLabel10 = new javax.swing.JLabel();
        cmbEstado = new javax.swing.JComboBox<>();
        cmbCliente = new javax.swing.JComboBox<>();
        cmbVehiculo = new javax.swing.JComboBox<>();
        cmbMecanico = new javax.swing.JComboBox<>();
        btnRegresar2 = new javax.swing.JButton();
        btnDiagnostico = new javax.swing.JButton();
        btnDetalleOrden = new javax.swing.JButton();
        btnActividades = new javax.swing.JButton();
        btnAsignarMecanicos = new javax.swing.JButton();
        btnConsumoRepuestos = new javax.swing.JButton();
        btnCambioMotor = new javax.swing.JButton();
        btnControlCalidad = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jLabel6.setText("Fecha");

        jScrollPane5.setViewportView(txtFecha);

        tablaOrdenes.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane9.setViewportView(tablaOrdenes);

        btnLimpiar.setBackground(new java.awt.Color(204, 255, 204));
        btnLimpiar.setText("Limpiar");
        btnLimpiar.addActionListener(this::btnLimpiarActionPerformed);

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel1.setText("ORDENES DE TRABAJO");

        jLabel3.setText("ID de orden");

        jLabel2.setText("Cliente");

        jScrollPane2.setViewportView(txtIdOrden);

        btnGuardar.setBackground(new java.awt.Color(255, 255, 204));
        btnGuardar.setText("Guardar");
        btnGuardar.addActionListener(this::btnGuardarActionPerformed);

        jLabel5.setText("Mecánico responsable");
        jLabel5.setToolTipText("");

        jLabel7.setText("Kilometraje");
        jLabel7.setToolTipText("");

        jLabel4.setText("Vehículo");

        btnActualizar.setBackground(new java.awt.Color(255, 204, 204));
        btnActualizar.setText("Actualizar");
        btnActualizar.setToolTipText("");
        btnActualizar.addActionListener(this::btnActualizarActionPerformed);

        jLabel8.setText("Síntomas");
        jLabel8.setToolTipText("");

        jLabel9.setText("Diagnóstico preliminar");
        jLabel9.setToolTipText("");

        jScrollPane6.setViewportView(txtSintomas);

        jScrollPane7.setViewportView(txtKilometraje);

        jScrollPane8.setViewportView(txtDiagnostico);

        jLabel10.setText("Estado");
        jLabel10.setToolTipText("");

        cmbEstado.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "RECIBIDA", "EN DIAGNÓSTICO", "PENDIENTE DE APROBACIÓN", "EN REPARACIÓN", "EN CONTROL DE CALIDAD", "FINALIZADA Y ENTREGADA", "RECHAZADA", "CANCELADA" }));

        cmbCliente.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        cmbVehiculo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        cmbMecanico.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        btnRegresar2.setBackground(new java.awt.Color(204, 255, 255));
        btnRegresar2.setText("Menu");
        btnRegresar2.addActionListener(this::btnRegresar2ActionPerformed);

        btnDiagnostico.setBackground(new java.awt.Color(255, 204, 153));
        btnDiagnostico.setText("Diagnóstico");
        btnDiagnostico.addActionListener(this::btnDiagnosticoActionPerformed);

        btnDetalleOrden.setBackground(new java.awt.Color(255, 204, 204));
        btnDetalleOrden.setText("Detalle de orden");
        btnDetalleOrden.addActionListener(this::btnDetalleOrdenActionPerformed);

        btnActividades.setBackground(new java.awt.Color(204, 255, 204));
        btnActividades.setText("Actividades");
        btnActividades.addActionListener(this::btnActividadesActionPerformed);

        btnAsignarMecanicos.setBackground(new java.awt.Color(204, 255, 255));
        btnAsignarMecanicos.setText("Asignar mecánicos");
        btnAsignarMecanicos.addActionListener(this::btnAsignarMecanicosActionPerformed);

        btnConsumoRepuestos.setBackground(new java.awt.Color(204, 255, 204));
        btnConsumoRepuestos.setText("Consumo de repuestos");
        btnConsumoRepuestos.addActionListener(this::btnConsumoRepuestosActionPerformed);

        btnCambioMotor.setBackground(new java.awt.Color(255, 255, 204));
        btnCambioMotor.setText("Cambio de motor");
        btnCambioMotor.addActionListener(this::btnCambioMotorActionPerformed);

        btnControlCalidad.setBackground(new java.awt.Color(249, 207, 207));
        btnControlCalidad.setText("Control de calidad");
        btnControlCalidad.setActionCommand("- Control de calidad");
        btnControlCalidad.addActionListener(this::btnControlCalidadActionPerformed);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(49, 49, 49)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 249, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(jScrollPane6, javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jScrollPane7, javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jScrollPane5, javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(cmbMecanico, javax.swing.GroupLayout.Alignment.LEADING, 0, 222, Short.MAX_VALUE)
                                .addComponent(cmbVehiculo, javax.swing.GroupLayout.Alignment.LEADING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(cmbCliente, javax.swing.GroupLayout.Alignment.LEADING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel7, javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel6, javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel5, javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel4, javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel8, javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel9, javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel10, javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.LEADING))
                            .addComponent(jScrollPane8, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 225, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(cmbEstado, javax.swing.GroupLayout.PREFERRED_SIZE, 225, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(btnLimpiar, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(btnGuardar, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(58, 58, 58)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(btnActualizar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(btnRegresar2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane9, javax.swing.GroupLayout.PREFERRED_SIZE, 1077, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
            .addGroup(layout.createSequentialGroup()
                .addGap(321, 321, 321)
                .addComponent(btnDiagnostico)
                .addGap(35, 35, 35)
                .addComponent(btnDetalleOrden)
                .addGap(38, 38, 38)
                .addComponent(btnActividades)
                .addGap(37, 37, 37)
                .addComponent(btnAsignarMecanicos)
                .addGap(35, 35, 35)
                .addComponent(btnConsumoRepuestos)
                .addGap(27, 27, 27)
                .addComponent(btnCambioMotor)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnControlCalidad)
                .addGap(27, 27, 27))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(39, 39, 39)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jScrollPane9)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addGap(41, 41, 41)
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel2)
                        .addGap(7, 7, 7)
                        .addComponent(cmbCliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cmbVehiculo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(28, 28, 28)
                        .addComponent(jLabel5)
                        .addGap(10, 10, 10)
                        .addComponent(cmbMecanico, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel6)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel7)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jScrollPane7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(16, 16, 16)
                        .addComponent(jLabel8)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel9)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel10)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cmbEstado, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(41, 41, 41)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnGuardar)
                            .addComponent(btnActualizar))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnLimpiar)
                            .addComponent(btnRegresar2))))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnDiagnostico)
                    .addComponent(btnDetalleOrden)
                    .addComponent(btnActividades)
                    .addComponent(btnAsignarMecanicos)
                    .addComponent(btnConsumoRepuestos)
                    .addComponent(btnCambioMotor)
                    .addComponent(btnControlCalidad))
                .addContainerGap(95, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnLimpiarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLimpiarActionPerformed
        limpiarCampos();
    }//GEN-LAST:event_btnLimpiarActionPerformed

    private void btnGuardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGuardarActionPerformed
        try {
            if (cmbCliente.getSelectedItem() == null
                    || cmbVehiculo.getSelectedItem() == null
                    || cmbMecanico.getSelectedItem() == null
                    || txtKilometraje.getText().trim().isEmpty()
                    || txtSintomas.getText().trim().isEmpty()) {

                JOptionPane.showMessageDialog(this,
                        "Complete todos los campos obligatorios.");
                return;
            }

            OrdenTrabajo orden = new OrdenTrabajo();

            orden.setIdCliente(Integer.parseInt(
                    cmbCliente.getSelectedItem().toString().split(" - ")[0]));

            orden.setIdVehiculo(Integer.parseInt(
                    cmbVehiculo.getSelectedItem().toString().split(" - ")[0]));

            orden.setIdMecanicoResponsable(Integer.parseInt(
                    cmbMecanico.getSelectedItem().toString().split(" - ")[0]));

            orden.setFecha(LocalDateTime.now());
            orden.setKilometraje(Integer.parseInt(txtKilometraje.getText().trim()));
            orden.setSintomas(txtSintomas.getText().trim());
            orden.setDiagnosticoPreliminar(txtDiagnostico.getText().trim());
            orden.setEstado("RECIBIDA");

            int id = controlador.guardar(orden);

            if (id > 0) {
                JOptionPane.showMessageDialog(this,
                        "Orden registrada correctamente. ID: " + id);

                limpiarCampos();
                cargarOrdenes();
            }

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this,
                    "El kilometraje debe ser un número válido.");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this,
                    "Error al guardar la orden: " + e.getMessage());
        }
    }//GEN-LAST:event_btnGuardarActionPerformed

    private void btnActualizarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnActualizarActionPerformed
        try {
            int id = Integer.parseInt(txtIdOrden.getText().trim());
            String estado = cmbEstado.getSelectedItem().toString();

            boolean actualizado = controlador.actualizarEstado(id, estado);

            if (actualizado) {
                JOptionPane.showMessageDialog(this,
                        "Estado de la orden actualizado correctamente.");

                cargarOrdenes();
            }

        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this,
                    ex.getMessage(),
                    "Cambio de estado no permitido",
                    JOptionPane.WARNING_MESSAGE);

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this,
                    "Error al actualizar: " + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_btnActualizarActionPerformed

    private void btnRegresar2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRegresar2ActionPerformed
        FrmPrincipal principal = new FrmPrincipal();
        principal.setLocationRelativeTo(null);
        principal.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_btnRegresar2ActionPerformed

    private void btnDiagnosticoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDiagnosticoActionPerformed
        int idOrden = obtenerIdOrdenSeleccionada();

        if (idOrden == -1) {
            return;
        }

        FrmDiagnosticos formulario = new FrmDiagnosticos(idOrden);
        formulario.setLocationRelativeTo(this);
        formulario.setVisible(true);
    }//GEN-LAST:event_btnDiagnosticoActionPerformed

    private void btnDetalleOrdenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDetalleOrdenActionPerformed
        int idOrden = obtenerIdOrdenSeleccionada();

        if (idOrden == -1) {
            return;
        }

        FrmDetalleOrden formulario = new FrmDetalleOrden(idOrden);
        formulario.setLocationRelativeTo(this);
        formulario.setVisible(true);
    }//GEN-LAST:event_btnDetalleOrdenActionPerformed

    private void btnActividadesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnActividadesActionPerformed
        int idOrden = obtenerIdOrdenSeleccionada();

        if (idOrden == -1) {
            return;
        }

        FrmActividadesTrabajo formulario =
                new FrmActividadesTrabajo(idOrden);

        formulario.setLocationRelativeTo(this);
        formulario.setVisible(true);
    }//GEN-LAST:event_btnActividadesActionPerformed

    private void btnAsignarMecanicosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAsignarMecanicosActionPerformed
        int idOrden = obtenerIdOrdenSeleccionada();

        if (idOrden == -1) {
            return;
        }

        FrmAsignacionesMecanicos formulario =
                new FrmAsignacionesMecanicos(idOrden);

        formulario.setLocationRelativeTo(this);
        formulario.setVisible(true);
    }//GEN-LAST:event_btnAsignarMecanicosActionPerformed

    private void btnConsumoRepuestosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnConsumoRepuestosActionPerformed
        int idOrden = obtenerIdOrdenSeleccionada();

        if (idOrden == -1) {
            return;
        }

        FrmConsumosOrden formulario = new FrmConsumosOrden(idOrden);
        formulario.setLocationRelativeTo(this);
        formulario.setVisible(true);
    }//GEN-LAST:event_btnConsumoRepuestosActionPerformed

    private void btnCambioMotorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCambioMotorActionPerformed
        int idOrden = obtenerIdOrdenSeleccionada();

        if (idOrden == -1) {
            return;
        }

        FrmCambioMotor formulario = new FrmCambioMotor(idOrden);
        formulario.setLocationRelativeTo(this);
        formulario.setVisible(true);
    }//GEN-LAST:event_btnCambioMotorActionPerformed

    private void btnControlCalidadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnControlCalidadActionPerformed
        int idOrden = obtenerIdOrdenSeleccionada();

        if (idOrden == -1) {
            return;
        }

        FrmControlCalidad formulario = new FrmControlCalidad(idOrden);
        formulario.setLocationRelativeTo(this);
        formulario.setVisible(true);
    }//GEN-LAST:event_btnControlCalidadActionPerformed

    private void limpiarCampos() {
        txtIdOrden.setText("");

        txtFecha.setText(LocalDateTime.now().format(
                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));

        txtKilometraje.setText("");
        txtSintomas.setText("");
        txtDiagnostico.setText("");

        if (cmbCliente.getItemCount() > 0) {
            cmbCliente.setSelectedIndex(0);
        }

        if (cmbVehiculo.getItemCount() > 0) {
            cmbVehiculo.setSelectedIndex(0);
        }

        if (cmbMecanico.getItemCount() > 0) {
            cmbMecanico.setSelectedIndex(0);
        }

        cmbEstado.setSelectedItem("RECIBIDA");

        tablaOrdenes.clearSelection();
    }
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
        java.awt.EventQueue.invokeLater(() -> new FrmOrdenesTrabajo().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnActividades;
    private javax.swing.JButton btnActualizar;
    private javax.swing.JButton btnAsignarMecanicos;
    private javax.swing.JButton btnCambioMotor;
    private javax.swing.JButton btnConsumoRepuestos;
    private javax.swing.JButton btnControlCalidad;
    private javax.swing.JButton btnDetalleOrden;
    private javax.swing.JButton btnDiagnostico;
    private javax.swing.JButton btnGuardar;
    private javax.swing.JButton btnLimpiar;
    private javax.swing.JButton btnRegresar2;
    private javax.swing.JComboBox<String> cmbCliente;
    private javax.swing.JComboBox<String> cmbEstado;
    private javax.swing.JComboBox<String> cmbMecanico;
    private javax.swing.JComboBox<String> cmbVehiculo;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JScrollPane jScrollPane8;
    private javax.swing.JScrollPane jScrollPane9;
    private javax.swing.JTable tablaOrdenes;
    private javax.swing.JTextPane txtDiagnostico;
    private javax.swing.JTextPane txtFecha;
    private javax.swing.JTextPane txtIdOrden;
    private javax.swing.JTextPane txtKilometraje;
    private javax.swing.JTextPane txtSintomas;
    // End of variables declaration//GEN-END:variables
}
