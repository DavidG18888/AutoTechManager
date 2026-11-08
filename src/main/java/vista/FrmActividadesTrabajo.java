/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package vista;
import controlador.ActividadTrabajoControlador;
import controlador.OrdenTrabajoControlador;
import controlador.MecanicoControlador;

import modelo.ActividadTrabajo;
import modelo.OrdenTrabajo;
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
public class FrmActividadesTrabajo extends javax.swing.JFrame {

    private final ActividadTrabajoControlador actividadControlador =
    new ActividadTrabajoControlador();

    private final OrdenTrabajoControlador ordenControlador =
    new OrdenTrabajoControlador();

    private final MecanicoControlador mecanicoControlador =
    new MecanicoControlador();

    private final DateTimeFormatter formatoFecha =
    DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    private DefaultTableModel modeloTabla;
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(FrmActividadesTrabajo.class.getName());

    /**
     * Creates new form FrmActividadesTrabajo
     */
public FrmActividadesTrabajo() {
    initComponents();

    configurarFormulario();

    tablaActividades.setSelectionMode(
            javax.swing.ListSelectionModel.SINGLE_SELECTION);

    tablaActividades.getSelectionModel().addListSelectionListener(e -> {

        if (!e.getValueIsAdjusting()) {
            seleccionarActividad();
        }
    });

    cargarOrdenes();
    cargarMecanicos();
}

    public FrmActividadesTrabajo(int idOrden) {
        this();

        cmbOrden.setSelectedItem(String.valueOf(idOrden));
        cargarActividades();

        setLocationRelativeTo(null);
    }
    private void configurarFormulario() {

        setLocationRelativeTo(null);
        setTitle("Gestión de Actividades de Trabajo");

        txtIdActividad.setEditable(false);
        txtFechaInicio.setEditable(false);
        txtFechaFin.setEditable(false);

        cmbOrden.removeAllItems();
        cmbMecanico.removeAllItems();
        cmbEstado.removeAllItems();

        cmbEstado.addItem("PENDIENTE");
        cmbEstado.addItem("EN_PROCESO");
        cmbEstado.addItem("COMPLETADA");

        modeloTabla = new DefaultTableModel(
                new Object[]{
                    "ID Actividad",
                    "Orden",
                    "Mecánico",
                    "Descripción",
                    "Fecha inicio",
                    "Fecha fin",
                    "Estado"
                }, 0) {

            @Override
            public boolean isCellEditable(int fila, int columna) {
                return false;
            }
        };

        tablaActividades.setModel(modeloTabla);

        cmbOrden.addActionListener(e -> cargarActividades());
    }

    private void cargarOrdenes() {

        try {

            cmbOrden.removeAllItems();

            List<OrdenTrabajo> lista = ordenControlador.listar();

            for (OrdenTrabajo orden : lista) {
                cmbOrden.addItem(String.valueOf(orden.getIdOrden()));
            }

        } catch (SQLException e) {

            JOptionPane.showMessageDialog(this,
                    "Error al cargar las órdenes: " + e.getMessage());
        }
    }

    private void cargarMecanicos() {

        try {

            cmbMecanico.removeAllItems();

            List<Mecanico> lista = mecanicoControlador.listar();

            for (Mecanico mecanico : lista) {

                if ("ACTIVO".equalsIgnoreCase(mecanico.getEstado())) {

                    cmbMecanico.addItem(
                            String.valueOf(mecanico.getIdMecanico()));
                }
            }

        } catch (SQLException e) {

            JOptionPane.showMessageDialog(this,
                    "Error al cargar los mecánicos: " + e.getMessage());
        }
    }

    private void cargarActividades() {

        try {

            modeloTabla.setRowCount(0);

            if (cmbOrden.getSelectedItem() == null) {
                return;
            }

            int idOrden = Integer.parseInt(
                    cmbOrden.getSelectedItem().toString());

            List<ActividadTrabajo> lista =
                    actividadControlador.listarPorOrden(idOrden);

            for (ActividadTrabajo actividad : lista) {

                String fechaInicio = "";

                if (actividad.getFechaInicio() != null) {
                    fechaInicio = actividad.getFechaInicio()
                            .format(formatoFecha);
                }

                String fechaFin = "";

                if (actividad.getFechaFin() != null) {
                    fechaFin = actividad.getFechaFin()
                            .format(formatoFecha);
                }

                modeloTabla.addRow(new Object[]{
                    actividad.getIdActividad(),
                    actividad.getIdOrden(),
                    actividad.getIdMecanico(),
                    actividad.getDescripcion(),
                    fechaInicio,
                    fechaFin,
                    actividad.getEstado()
                });
            }

        } catch (SQLException | NumberFormatException e) {

            JOptionPane.showMessageDialog(this,
                    "Error al cargar las actividades: " + e.getMessage());
        }
    }

    private void limpiarCampos() {

        txtIdActividad.setText("");
        txtDescripcion.setText("");
        txtFechaInicio.setText("");
        txtFechaFin.setText("");

        if (cmbEstado.getItemCount() > 0) {
            cmbEstado.setSelectedItem("PENDIENTE");
        }

        tablaActividades.clearSelection();
    }

    private boolean seleccionandoActividad = false;

    private void seleccionarActividad() {

        int fila = tablaActividades.getSelectedRow();

        if (fila == -1) {
            return;
        }

        try {
            seleccionandoActividad = true;

            String idActividad =
                    tablaActividades.getValueAt(fila, 0).toString();

            String idOrden =
                    tablaActividades.getValueAt(fila, 1).toString();

            String idMecanico =
                    tablaActividades.getValueAt(fila, 2).toString();

            String descripcion =
                    tablaActividades.getValueAt(fila, 3).toString();

            String fechaInicio =
                    tablaActividades.getValueAt(fila, 4).toString();

            String fechaFin =
                    tablaActividades.getValueAt(fila, 5).toString();

            String estado =
                    tablaActividades.getValueAt(fila, 6).toString();

            cmbOrden.setSelectedItem(idOrden);
            cmbMecanico.setSelectedItem(idMecanico);

            txtIdActividad.setText(idActividad);
            txtDescripcion.setText(descripcion);
            txtFechaInicio.setText(fechaInicio);
            txtFechaFin.setText(fechaFin);

            cmbEstado.setSelectedItem(estado);

        } finally {
            seleccionandoActividad = false;
        }
    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel7 = new javax.swing.JLabel();
        txtDescripcion = new javax.swing.JTextField();
        btnLimpiar = new javax.swing.JButton();
        txtIdActividad = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        txtFechaInicio = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tablaActividades = new javax.swing.JTable();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        btnGuardar = new javax.swing.JButton();
        jLabel6 = new javax.swing.JLabel();
        btnActualizar = new javax.swing.JButton();
        jLabel8 = new javax.swing.JLabel();
        txtFechaFin = new javax.swing.JTextField();
        cmbOrden = new javax.swing.JComboBox<>();
        cmbEstado = new javax.swing.JComboBox<>();
        cmbMecanico = new javax.swing.JComboBox<>();
        btnRegresar = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jLabel7.setText("Estado");

        btnLimpiar.setBackground(new java.awt.Color(204, 255, 204));
        btnLimpiar.setText("Limpiar");
        btnLimpiar.addActionListener(this::btnLimpiarActionPerformed);

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel1.setText("GESTIÓN DE ACTIVIDADES DE TRABAJO");

        jLabel2.setText("Orden de trabajo");

        jLabel3.setText("ID actividad");

        tablaActividades.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane1.setViewportView(tablaActividades);

        jLabel4.setText("Fecha de inicio");

        jLabel5.setText("Descripción");

        btnGuardar.setBackground(new java.awt.Color(255, 255, 204));
        btnGuardar.setText("Guardar");
        btnGuardar.addActionListener(this::btnGuardarActionPerformed);

        jLabel6.setText("Mecánico");

        btnActualizar.setBackground(new java.awt.Color(255, 204, 204));
        btnActualizar.setText("Actualizar");
        btnActualizar.addActionListener(this::btnActualizarActionPerformed);

        jLabel8.setText("Fecha de finalización");

        cmbOrden.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        cmbEstado.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        cmbMecanico.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        btnRegresar.setBackground(new java.awt.Color(204, 255, 255));
        btnRegresar.setText("Menu");
        btnRegresar.addActionListener(this::btnRegresarActionPerformed);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(139, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel8)
                    .addComponent(txtFechaFin, javax.swing.GroupLayout.PREFERRED_SIZE, 187, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(jLabel5)
                                .addComponent(jLabel7)
                                .addComponent(txtDescripcion, javax.swing.GroupLayout.DEFAULT_SIZE, 187, Short.MAX_VALUE)
                                .addComponent(jLabel2)
                                .addComponent(cmbOrden, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addComponent(cmbEstado, javax.swing.GroupLayout.PREFERRED_SIZE, 187, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(91, 91, 91)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtIdActividad, javax.swing.GroupLayout.DEFAULT_SIZE, 187, Short.MAX_VALUE)
                            .addComponent(jLabel4)
                            .addComponent(jLabel6)
                            .addComponent(jLabel3)
                            .addComponent(txtFechaInicio, javax.swing.GroupLayout.DEFAULT_SIZE, 187, Short.MAX_VALUE)
                            .addComponent(cmbMecanico, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(32, 32, 32)
                        .addComponent(btnGuardar)
                        .addGap(70, 70, 70)
                        .addComponent(btnActualizar)
                        .addGap(59, 59, 59)
                        .addComponent(btnLimpiar)
                        .addGap(43, 43, 43)
                        .addComponent(btnRegresar)))
                .addGap(113, 113, 113))
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(31, 31, 31)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 676, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(193, 193, 193)
                        .addComponent(jLabel1)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addComponent(jLabel1)
                .addGap(35, 35, 35)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jLabel3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtIdActividad, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cmbOrden, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(27, 27, 27)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(jLabel6))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(txtDescripcion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(27, 27, 27))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(cmbMecanico, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(jLabel4))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtFechaInicio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cmbEstado, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(26, 26, 26)
                .addComponent(jLabel8)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(txtFechaFin, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 39, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnGuardar)
                    .addComponent(btnActualizar)
                    .addComponent(btnLimpiar)
                    .addComponent(btnRegresar))
                .addGap(43, 43, 43)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 349, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnLimpiarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLimpiarActionPerformed
        limpiarCampos();
    }//GEN-LAST:event_btnLimpiarActionPerformed

    private void btnGuardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGuardarActionPerformed
        try {

            if (cmbOrden.getSelectedItem() == null) {
                JOptionPane.showMessageDialog(this,
                        "Seleccione una orden de trabajo.");
                return;
            }

            if (cmbMecanico.getSelectedItem() == null) {
                JOptionPane.showMessageDialog(this,
                        "Seleccione un mecánico.");
                return;
            }

            if (txtDescripcion.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "Ingrese la descripción de la actividad.");
                return;
            }

            ActividadTrabajo actividad = new ActividadTrabajo();

            actividad.setIdOrden(Integer.parseInt(
                    cmbOrden.getSelectedItem().toString()));

            actividad.setIdMecanico(Integer.parseInt(
                    cmbMecanico.getSelectedItem().toString()));

            actividad.setDescripcion(
                    txtDescripcion.getText().trim());

            actividad.setFechaInicio(LocalDateTime.now());

            actividad.setEstado(
                    cmbEstado.getSelectedItem().toString());

            actividad.setObservaciones(null);

            if (actividadControlador.guardar(actividad)) {

                JOptionPane.showMessageDialog(this,
                        "Actividad registrada correctamente.");

                cargarActividades();
                limpiarCampos();

            } else {

                JOptionPane.showMessageDialog(this,
                        "No se pudo registrar la actividad.");
            }

        } catch (SQLException | IllegalArgumentException e) {

            JOptionPane.showMessageDialog(this,
                    "Error al guardar la actividad: " + e.getMessage());
        }     
    }//GEN-LAST:event_btnGuardarActionPerformed

    private void btnActualizarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnActualizarActionPerformed
        try {

            if (txtIdActividad.getText().trim().isEmpty()) {

                JOptionPane.showMessageDialog(this,
                        "Seleccione una actividad de la tabla.");
                return;
            }

            String estado = cmbEstado.getSelectedItem().toString();

            int idActividad = Integer.parseInt(
                    txtIdActividad.getText().trim());

            int confirmacion = JOptionPane.showConfirmDialog(
                    this,
                    "¿Desea cambiar el estado de esta actividad a "
                    + estado + "?",
                    "Confirmar actualización",
                    JOptionPane.YES_NO_OPTION
            );

            if (confirmacion != JOptionPane.YES_OPTION) {
                return;
            }

            if (actividadControlador.actualizarEstado(
                    idActividad, estado)) {

                JOptionPane.showMessageDialog(this,
                        "Actividad actualizada correctamente.");

                cargarActividades();
                limpiarCampos();

            } else {

                JOptionPane.showMessageDialog(this,
                        "No se pudo actualizar la actividad.");
            }

        } catch (SQLException | IllegalArgumentException e) {

            JOptionPane.showMessageDialog(this,
                    "Error al actualizar: " + e.getMessage());
        }     
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
        java.awt.EventQueue.invokeLater(() -> new FrmActividadesTrabajo().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnActualizar;
    private javax.swing.JButton btnGuardar;
    private javax.swing.JButton btnLimpiar;
    private javax.swing.JButton btnRegresar;
    private javax.swing.JComboBox<String> cmbEstado;
    private javax.swing.JComboBox<String> cmbMecanico;
    private javax.swing.JComboBox<String> cmbOrden;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tablaActividades;
    private javax.swing.JTextField txtDescripcion;
    private javax.swing.JTextField txtFechaFin;
    private javax.swing.JTextField txtFechaInicio;
    private javax.swing.JTextField txtIdActividad;
    // End of variables declaration//GEN-END:variables
}
