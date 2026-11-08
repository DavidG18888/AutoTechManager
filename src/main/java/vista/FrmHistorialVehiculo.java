/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package vista;
import controlador.VehiculoControlador;
import controlador.OrdenTrabajoControlador;
import controlador.HistorialControlador;

import modelo.Vehiculo;
import modelo.OrdenTrabajo;
import modelo.DetalleOrden;
import modelo.ConsumoOrden;

import java.sql.SQLException;
import java.time.format.DateTimeFormatter;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;


/**
 *
 * @author gesle
 */
public class FrmHistorialVehiculo extends javax.swing.JFrame {
  
    private final VehiculoControlador vehiculoControlador =
    new VehiculoControlador();

    private final OrdenTrabajoControlador ordenControlador =
    new OrdenTrabajoControlador();

    private final HistorialControlador historialControlador =
    new HistorialControlador();

    private List<Vehiculo> vehiculosCargados;
    private List<OrdenTrabajo> ordenesCargadas;

    private final DateTimeFormatter formatoFecha =
    DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(FrmHistorialVehiculo.class.getName());

    /**
     * Creates new form FrmHistorialVehiculo
     */
    public FrmHistorialVehiculo() {
        initComponents();

        configurarTablas();
        cargarVehiculos();

        btnBuscar.addActionListener(e -> buscarHistorial());

        tablaOrdenes.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                cargarDetalleOrden();
            }
        });
    }


    private void configurarTablas() {

        tablaOrdenes.setModel(new DefaultTableModel(
                new Object[][]{},
                new String[]{
                    "ID Orden", "Fecha", "Kilometraje",
                    "Síntomas", "Estado"
                }
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        });

        tablaServicios.setModel(new DefaultTableModel(
                new Object[][]{},
                new String[]{
                    "Servicio", "Cantidad", "Precio", "Subtotal"
                }
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        });

        tablaRepuestos.setModel(new DefaultTableModel(
                new Object[][]{},
                new String[]{
                    "Repuesto", "Cantidad", "Costo", "Total"
                }
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        });
    }

    private void cargarVehiculos() {

        try {

            cmbVehiculo.removeAllItems();

            vehiculosCargados = vehiculoControlador.listar();

            for (Vehiculo vehiculo : vehiculosCargados) {

                String descripcion = vehiculo.getPlaca()
                        + " - " + vehiculo.getMarca()
                        + " " + vehiculo.getModelo()
                        + " (" + vehiculo.getAnio() + ")";

                cmbVehiculo.addItem(descripcion);
            }

        } catch (SQLException ex) {

            JOptionPane.showMessageDialog(this,
                    "Error al cargar los vehículos: " + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void buscarHistorial() {

        int indice = cmbVehiculo.getSelectedIndex();

        if (indice < 0 || vehiculosCargados == null) {

            JOptionPane.showMessageDialog(this,
                    "Seleccione un vehículo.");

            return;
        }

        try {

            Vehiculo vehiculo = vehiculosCargados.get(indice);

            ordenesCargadas = historialControlador.listarPorVehiculo(
                    vehiculo.getIdVehiculo()
            );

            DefaultTableModel modelo =
                    (DefaultTableModel) tablaOrdenes.getModel();

            modelo.setRowCount(0);

            limpiarDetalles();

            if (ordenesCargadas.isEmpty()) {

                JOptionPane.showMessageDialog(this,
                        "Este vehículo todavía no tiene órdenes de trabajo.");

                return;
            }

            for (OrdenTrabajo orden : ordenesCargadas) {

                String fecha = orden.getFecha() != null
                        ? orden.getFecha().format(formatoFecha)
                        : "";

                modelo.addRow(new Object[]{
                    orden.getIdOrden(),
                    fecha,
                    orden.getKilometraje(),
                    orden.getSintomas(),
                    orden.getEstado()
                });
            }

        } catch (SQLException ex) {

            JOptionPane.showMessageDialog(this,
                    "Error al consultar el historial: " + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void cargarDetalleOrden() {

        int fila = tablaOrdenes.getSelectedRow();

        if (fila < 0) {
            return;
        }

        try {

            int filaModelo = tablaOrdenes.convertRowIndexToModel(fila);

            int idOrden = Integer.parseInt(
                    tablaOrdenes.getModel()
                            .getValueAt(filaModelo, 0).toString()
            );

            cargarServicios(idOrden);
            cargarRepuestos(idOrden);

        } catch (SQLException ex) {

            JOptionPane.showMessageDialog(this,
                    "Error al cargar el detalle de la orden: "
                    + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void cargarServicios(int idOrden) throws SQLException {

        DefaultTableModel modelo =
                (DefaultTableModel) tablaServicios.getModel();

        modelo.setRowCount(0);

        List<DetalleOrden> servicios =
                historialControlador.listarServiciosPorOrden(idOrden);

        for (DetalleOrden detalle : servicios) {

            modelo.addRow(new Object[]{
                detalle.getNombreServicio(),
                detalle.getCantidad(),
                detalle.getCostoManoObra(),
                detalle.getSubtotal()
            });
        }
    }

    private void cargarRepuestos(int idOrden) throws SQLException {

        DefaultTableModel modelo =
                (DefaultTableModel) tablaRepuestos.getModel();

        modelo.setRowCount(0);

        List<ConsumoOrden> repuestos =
                historialControlador.listarRepuestosPorOrden(idOrden);

        for (ConsumoOrden consumo : repuestos) {

            double total = consumo.getCantidad()
                    * consumo.getCostoUnitario();

            modelo.addRow(new Object[]{
                consumo.getNombreRepuesto(),
                consumo.getCantidad(),
                consumo.getCostoUnitario(),
                total
            });
        }
    }

    private void limpiarDetalles() {

        ((DefaultTableModel) tablaServicios.getModel()).setRowCount(0);

        ((DefaultTableModel) tablaRepuestos.getModel()).setRowCount(0);
    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        cmbVehiculo = new javax.swing.JComboBox<>();
        btnBuscar = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        tablaOrdenes = new javax.swing.JTable();
        jScrollPane1 = new javax.swing.JScrollPane();
        tablaServicios = new javax.swing.JTable();
        jScrollPane3 = new javax.swing.JScrollPane();
        tablaRepuestos = new javax.swing.JTable();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        btnRegresar = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel1.setText("HISTORIAL DE VEHICULOS");

        jLabel2.setText("Seleccionar vehículo:");

        cmbVehiculo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        btnBuscar.setBackground(new java.awt.Color(255, 255, 204));
        btnBuscar.setText("Buscar historial");
        btnBuscar.setActionCommand("Buscar historial");
        btnBuscar.addActionListener(this::btnBuscarActionPerformed);

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
        jScrollPane2.setViewportView(tablaOrdenes);

        tablaServicios.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane1.setViewportView(tablaServicios);

        tablaRepuestos.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane3.setViewportView(tablaRepuestos);

        jLabel5.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel5.setText("Repuestos utilizados");

        jLabel6.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel6.setText("Órdenes de trabajo");

        jLabel7.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel7.setText("Servicios realizados");

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
                        .addGap(182, 182, 182)
                        .addComponent(jLabel2)
                        .addGap(18, 18, 18)
                        .addComponent(cmbVehiculo, javax.swing.GroupLayout.PREFERRED_SIZE, 219, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(42, 42, 42)
                        .addComponent(btnBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, 133, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(37, 37, 37)
                        .addComponent(btnRegresar))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(105, 105, 105)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 799, Short.MAX_VALUE)
                                .addComponent(jScrollPane2)
                                .addComponent(jScrollPane3))
                            .addComponent(jLabel5)
                            .addComponent(jLabel7)
                            .addComponent(jLabel6))))
                .addContainerGap(72, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jLabel1)
                .addGap(372, 372, 372))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(35, 35, 35)
                .addComponent(jLabel1)
                .addGap(38, 38, 38)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(cmbVehiculo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnBuscar)
                    .addComponent(btnRegresar))
                .addGap(39, 39, 39)
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 183, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(22, 22, 22)
                .addComponent(jLabel7)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 187, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel5)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 178, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(36, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnBuscarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBuscarActionPerformed
        buscarHistorial();
    }//GEN-LAST:event_btnBuscarActionPerformed

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
        java.awt.EventQueue.invokeLater(() -> new FrmHistorialVehiculo().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBuscar;
    private javax.swing.JButton btnRegresar;
    private javax.swing.JComboBox<String> cmbVehiculo;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTable tablaOrdenes;
    private javax.swing.JTable tablaRepuestos;
    private javax.swing.JTable tablaServicios;
    // End of variables declaration//GEN-END:variables
}
