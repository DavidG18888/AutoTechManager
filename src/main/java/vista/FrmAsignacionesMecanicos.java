/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package vista;
import controlador.AsignacionMecanicoControlador;
import controlador.MecanicoControlador;
import controlador.OrdenTrabajoControlador;
import modelo.AsignacionMecanico;
import modelo.Mecanico;
import modelo.OrdenTrabajo;

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
public class FrmAsignacionesMecanicos extends javax.swing.JFrame {
    private final AsignacionMecanicoControlador asignacionControlador =
    new AsignacionMecanicoControlador();

    private final OrdenTrabajoControlador ordenControlador =
    new OrdenTrabajoControlador();

    private final MecanicoControlador mecanicoControlador =
    new MecanicoControlador();

    private final DateTimeFormatter formatoFecha =
    DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(FrmAsignacionesMecanicos.class.getName());

    /**
     * Creates new form FrmAsignacionesMecanicos
     */
    
    public FrmAsignacionesMecanicos() {
        initComponents();

        setTitle("Asignación de mecánicos");
        setLocationRelativeTo(null);
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        txtFecha.setEditable(false);

        configurarTabla();
        cargarOrdenes();
        cargarMecanicos();

        cmbOrden.addActionListener(e -> cargarAsignaciones());
    }

    public FrmAsignacionesMecanicos(int idOrden) {
        this();

        cmbOrden.setSelectedItem(String.valueOf(idOrden));
        cargarAsignaciones();

        setLocationRelativeTo(null);
    }
    private void configurarTabla() {
        DefaultTableModel modelo = new DefaultTableModel(
                new Object[][]{},
                new String[]{
                    "ID Asignación",
                    "ID Orden",
                    "ID Mecánico",
                    "Responsable principal",
                    "Fecha de asignación"
                }
        ) {
            @Override
            public boolean isCellEditable(int fila, int columna) {
                return false;
            }
        };

        tablaAsignaciones.setModel(modelo);
    }

    private void cargarOrdenes() {
        try {
            cmbOrden.removeAllItems();

            List<OrdenTrabajo> ordenes = ordenControlador.listar();

            for (OrdenTrabajo orden : ordenes) {
                cmbOrden.addItem(String.valueOf(orden.getIdOrden()));
            }

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this,
                    "Error al cargar las órdenes: " + ex.getMessage());
        }
    }

    private void cargarMecanicos() {
        try {
            cmbMecanico.removeAllItems();

            List<Mecanico> mecanicos = mecanicoControlador.listar();

            for (Mecanico mecanico : mecanicos) {
                if ("ACTIVO".equalsIgnoreCase(mecanico.getEstado())) {
                    cmbMecanico.addItem(
                            String.valueOf(mecanico.getIdMecanico())
                    );
                }
            }

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this,
                    "Error al cargar los mecánicos: " + ex.getMessage());
        }
    }

    private int obtenerIdSeleccionado(
            javax.swing.JComboBox<String> combo) {

        if (combo.getSelectedItem() == null) {
            return -1;
        }

        try {
            return Integer.parseInt(
                    combo.getSelectedItem().toString()
            );
        } catch (NumberFormatException ex) {
            return -1;
        }
    }

    private void cargarAsignaciones() {
        int idOrden = obtenerIdSeleccionado(cmbOrden);

        DefaultTableModel modelo =
                (DefaultTableModel) tablaAsignaciones.getModel();

        modelo.setRowCount(0);

        if (idOrden <= 0) {
            return;
        }

        try {
            List<AsignacionMecanico> asignaciones =
                    asignacionControlador.listarPorOrden(idOrden);

            for (AsignacionMecanico asignacion : asignaciones) {

                modelo.addRow(new Object[]{
                    asignacion.getIdAsignacion(),
                    asignacion.getIdOrden(),
                    asignacion.getIdMecanico(),
                    asignacion.isResponsable() ? "SÍ" : "NO",
                    asignacion.getFechaAsignacion() == null
                            ? ""
                            : asignacion.getFechaAsignacion().format(formatoFecha)
                });
            }

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this,
                    "Error al consultar las asignaciones: "
                    + ex.getMessage());
        }
    }

    private void guardarAsignacion() {
        int idOrden = obtenerIdSeleccionado(cmbOrden);
        int idMecanico = obtenerIdSeleccionado(cmbMecanico);

        if (idOrden <= 0) {
            JOptionPane.showMessageDialog(this,
                    "Seleccione una orden de trabajo.");
            return;
        }

        if (idMecanico <= 0) {
            JOptionPane.showMessageDialog(this,
                    "Seleccione un mecánico.");
            return;
        }

        try {
            AsignacionMecanico asignacion = new AsignacionMecanico();

            asignacion.setIdOrden(idOrden);
            asignacion.setIdMecanico(idMecanico);
            asignacion.setResponsable(chkResponsable.isSelected());
            asignacion.setFechaAsignacion(LocalDateTime.now());

            boolean guardado = asignacionControlador.guardar(asignacion);

            if (guardado) {
                JOptionPane.showMessageDialog(this,
                        "Mecánico asignado correctamente.");

                txtFecha.setText(
                        LocalDateTime.now().format(formatoFecha)
                );

                cargarAsignaciones();
                limpiarCampos();
            } else {
                JOptionPane.showMessageDialog(this,
                        "No se pudo guardar la asignación.");
            }

        } catch (SQLException | IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this,
                    ex.getMessage(),
                    "Error de asignación",
                    JOptionPane.WARNING_MESSAGE);
        }
    }

    private void limpiarCampos() {
        cmbMecanico.setSelectedIndex(-1);
        chkResponsable.setSelected(false);
        txtFecha.setText("");
    }

    private void actualizarAsignaciones() {
        cargarOrdenes();
        cargarMecanicos();
        cargarAsignaciones();

        JOptionPane.showMessageDialog(this,
                "Información actualizada.");
    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        btnLimpiar = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        txtFecha = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tablaAsignaciones = new javax.swing.JTable();
        btnGuardar = new javax.swing.JButton();
        jLabel6 = new javax.swing.JLabel();
        btnActualizar = new javax.swing.JButton();
        chkResponsable = new javax.swing.JCheckBox();
        cmbOrden = new javax.swing.JComboBox<>();
        cmbMecanico = new javax.swing.JComboBox<>();
        btnRegresar = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        btnLimpiar.setBackground(new java.awt.Color(204, 255, 204));
        btnLimpiar.setText("Limpiar");
        btnLimpiar.addActionListener(this::btnLimpiarActionPerformed);

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel1.setText("ASIGNACION DE MECANICOS");

        jLabel2.setText("Orden de Trabajo");

        jLabel3.setText("Mecanico");

        tablaAsignaciones.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane1.setViewportView(tablaAsignaciones);

        btnGuardar.setBackground(new java.awt.Color(255, 255, 204));
        btnGuardar.setText("Guardar");
        btnGuardar.addActionListener(this::btnGuardarActionPerformed);

        jLabel6.setText("Fecha de Asignacion");

        btnActualizar.setBackground(new java.awt.Color(255, 204, 204));
        btnActualizar.setText("Actualizar");
        btnActualizar.addActionListener(this::btnActualizarActionPerformed);

        chkResponsable.setText("Responsable principal");

        cmbOrden.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        cmbMecanico.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        btnRegresar.setBackground(new java.awt.Color(204, 255, 255));
        btnRegresar.setText("Menu");
        btnRegresar.addActionListener(this::btnRegresarActionPerformed);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(85, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtFecha, javax.swing.GroupLayout.PREFERRED_SIZE, 187, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cmbOrden, javax.swing.GroupLayout.PREFERRED_SIZE, 187, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2)
                    .addComponent(jLabel6))
                .addGap(87, 87, 87)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(cmbMecanico, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel3)
                            .addComponent(chkResponsable, javax.swing.GroupLayout.PREFERRED_SIZE, 187, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addGap(74, 74, 74))
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(102, 102, 102)
                        .addComponent(btnGuardar)
                        .addGap(50, 50, 50)
                        .addComponent(btnActualizar)
                        .addGap(44, 44, 44)
                        .addComponent(btnLimpiar)
                        .addGap(44, 44, 44)
                        .addComponent(btnRegresar))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(42, 42, 42)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 533, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(178, 178, 178)
                        .addComponent(jLabel1)))
                .addContainerGap(46, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addComponent(jLabel1)
                .addGap(31, 31, 31)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jLabel3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cmbOrden, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cmbMecanico, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(33, 33, 33)
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtFecha, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(chkResponsable))
                .addGap(35, 35, 35)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnGuardar)
                    .addComponent(btnActualizar)
                    .addComponent(btnLimpiar)
                    .addComponent(btnRegresar))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 408, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(14, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnLimpiarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLimpiarActionPerformed
        limpiarCampos();
    }//GEN-LAST:event_btnLimpiarActionPerformed

    private void btnGuardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGuardarActionPerformed
        guardarAsignacion();
    }//GEN-LAST:event_btnGuardarActionPerformed

    private void btnActualizarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnActualizarActionPerformed
        actualizarAsignaciones();
    }//GEN-LAST:event_btnActualizarActionPerformed

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
        java.awt.EventQueue.invokeLater(() -> new FrmAsignacionesMecanicos().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnActualizar;
    private javax.swing.JButton btnGuardar;
    private javax.swing.JButton btnLimpiar;
    private javax.swing.JButton btnRegresar;
    private javax.swing.JCheckBox chkResponsable;
    private javax.swing.JComboBox<String> cmbMecanico;
    private javax.swing.JComboBox<String> cmbOrden;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tablaAsignaciones;
    private javax.swing.JTextField txtFecha;
    // End of variables declaration//GEN-END:variables
}
