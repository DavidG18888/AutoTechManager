/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package vista;
import controlador.ControlCalidadControlador;
import controlador.OrdenTrabajoControlador;
import controlador.MecanicoControlador;

import modelo.ControlCalidad;
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
public class FrmControlCalidad extends javax.swing.JFrame {
    private final ControlCalidadControlador controlControlador =
    new ControlCalidadControlador();

    private final OrdenTrabajoControlador ordenControlador =
    new OrdenTrabajoControlador();

    private final MecanicoControlador mecanicoControlador =
    new MecanicoControlador();

    private final DateTimeFormatter formatoFecha =
    DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    private DefaultTableModel modeloTabla;

    private boolean seleccionandoControl = false;   
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(FrmControlCalidad.class.getName());

    /**
     * Creates new form FrmControlCalidad
     */
    public FrmControlCalidad() {
        initComponents();

        configurarFormulario();

        tablaControles.setSelectionMode(
                javax.swing.ListSelectionModel.SINGLE_SELECTION);

        tablaControles.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                seleccionarControl();
            }
        });

        cargarOrdenes();
        cargarMecanicos();
    }
    public FrmControlCalidad(int idOrden) {
          this();

          cmbOrden.setSelectedItem(String.valueOf(idOrden));
          cargarControles();

          setLocationRelativeTo(null);
      }  
    private void configurarFormulario() {

        setLocationRelativeTo(null);
        setTitle("Gestión de Control de Calidad");

        txtIdControl.setEditable(false);
        txtFecha.setEditable(false);

        cmbOrden.removeAllItems();
        cmbMecanico.removeAllItems();
        cmbResultado.removeAllItems();

        cmbResultado.addItem("PENDIENTE");
        cmbResultado.addItem("APROBADO");
        cmbResultado.addItem("RECHAZADO");

        modeloTabla = new DefaultTableModel(
                new Object[]{
                    "ID Control",
                    "Orden",
                    "Mecánico",
                    "Punto de control",
                    "Fecha",
                    "Resultado",
                    "Observaciones"
                }, 0) {

            @Override
            public boolean isCellEditable(int fila, int columna) {
                return false;
            }
        };

        tablaControles.setModel(modeloTabla);

        cmbOrden.addActionListener(e -> {
            if (!seleccionandoControl) {
                cargarControles();
            }
        });
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

    private void cargarControles() {

        try {

            modeloTabla.setRowCount(0);

            if (cmbOrden.getSelectedItem() == null) {
                return;
            }

            int idOrden = Integer.parseInt(
                    cmbOrden.getSelectedItem().toString());

            List<ControlCalidad> lista =
                    controlControlador.listarPorOrden(idOrden);

            for (ControlCalidad control : lista) {

                String fecha = "";

                if (control.getFecha() != null) {
                    fecha = control.getFecha().format(formatoFecha);
                }

                modeloTabla.addRow(new Object[]{
                    control.getIdControl(),
                    control.getIdOrden(),
                    control.getIdMecanico(),
                    control.getDescripcion(),
                    fecha,
                    control.getResultado(),
                    control.getObservaciones()
                });
            }

        } catch (SQLException | NumberFormatException e) {

            JOptionPane.showMessageDialog(this,
                    "Error al cargar los controles: " + e.getMessage());
        }
    }

    private void seleccionarControl() {

        int fila = tablaControles.getSelectedRow();

        if (fila == -1) {
            return;
        }

        try {

            seleccionandoControl = true;

            txtIdControl.setText(
                    tablaControles.getValueAt(fila, 0).toString());

            cmbOrden.setSelectedItem(
                    tablaControles.getValueAt(fila, 1).toString());

            cmbMecanico.setSelectedItem(
                    tablaControles.getValueAt(fila, 2).toString());

            txtPuntoControl.setText(
                    tablaControles.getValueAt(fila, 3).toString());

            txtFecha.setText(
                    tablaControles.getValueAt(fila, 4).toString());

            cmbResultado.setSelectedItem(
                    tablaControles.getValueAt(fila, 5).toString());

            Object observaciones =
                    tablaControles.getValueAt(fila, 6);

            txtObservaciones.setText(
                    observaciones == null ? "" : observaciones.toString());

        } finally {

            seleccionandoControl = false;
        }
    }

    private void limpiarCampos() {

        txtIdControl.setText("");
        txtFecha.setText("");
        txtPuntoControl.setText("");
        txtObservaciones.setText("");

        if (cmbResultado.getItemCount() > 0) {
            cmbResultado.setSelectedItem("PENDIENTE");
        }

        tablaControles.clearSelection();
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
        txtIdControl = new javax.swing.JTextField();
        btnGuardar = new javax.swing.JButton();
        jLabel6 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        btnActualizar = new javax.swing.JButton();
        jLabel8 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        txtObservaciones = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        cmbOrden = new javax.swing.JComboBox<>();
        jScrollPane1 = new javax.swing.JScrollPane();
        tablaControles = new javax.swing.JTable();
        txtFecha = new javax.swing.JTextField();
        cmbMecanico = new javax.swing.JComboBox<>();
        btnLimpiar = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        txtPuntoControl = new javax.swing.JTextField();
        cmbResultado = new javax.swing.JComboBox<>();
        btnRegresar = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jLabel5.setText("Mecánico");

        btnGuardar.setBackground(new java.awt.Color(255, 255, 204));
        btnGuardar.setText("Guardar");
        btnGuardar.addActionListener(this::btnGuardarActionPerformed);

        jLabel6.setText("Fecha");

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel1.setText("GESTIÓN DE CONTROL CALIDAD");

        btnActualizar.setBackground(new java.awt.Color(255, 204, 204));
        btnActualizar.setText("Actualizar");
        btnActualizar.addActionListener(this::btnActualizarActionPerformed);

        jLabel8.setText("Observaciones");

        jLabel2.setText("Orden de trabajo");

        jLabel7.setText("Punto de control");

        jLabel3.setText("ID de control");

        cmbOrden.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        tablaControles.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane1.setViewportView(tablaControles);

        cmbMecanico.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        btnLimpiar.setBackground(new java.awt.Color(204, 255, 204));
        btnLimpiar.setText("Limpiar");
        btnLimpiar.addActionListener(this::btnLimpiarActionPerformed);

        jLabel4.setText("Resultado");

        cmbResultado.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        btnRegresar.setBackground(new java.awt.Color(204, 255, 255));
        btnRegresar.setText("Menu");
        btnRegresar.addActionListener(this::btnRegresarActionPerformed);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(19, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 676, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel8)
                            .addComponent(txtObservaciones, javax.swing.GroupLayout.PREFERRED_SIZE, 187, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addGroup(layout.createSequentialGroup()
                                    .addComponent(btnGuardar)
                                    .addGap(50, 50, 50)
                                    .addComponent(btnActualizar)
                                    .addGap(58, 58, 58)
                                    .addComponent(btnLimpiar)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(btnRegresar))
                                .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jLabel5)
                                        .addComponent(jLabel7)
                                        .addComponent(jLabel2)
                                        .addComponent(cmbOrden, javax.swing.GroupLayout.PREFERRED_SIZE, 187, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(cmbMecanico, javax.swing.GroupLayout.PREFERRED_SIZE, 187, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(txtPuntoControl, javax.swing.GroupLayout.PREFERRED_SIZE, 187, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGap(91, 91, 91)
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(txtFecha, javax.swing.GroupLayout.PREFERRED_SIZE, 187, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(txtIdControl, javax.swing.GroupLayout.PREFERRED_SIZE, 187, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel4)
                                        .addComponent(jLabel3)
                                        .addComponent(jLabel6)
                                        .addComponent(cmbResultado, javax.swing.GroupLayout.PREFERRED_SIZE, 187, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                        .addGap(150, 150, 150))))
            .addGroup(layout.createSequentialGroup()
                .addGap(197, 197, 197)
                .addComponent(jLabel1)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addComponent(jLabel1)
                .addGap(33, 33, 33)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jLabel3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtIdControl, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cmbOrden, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(27, 27, 27)
                        .addComponent(jLabel5))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel6)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cmbMecanico, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtFecha, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(27, 27, 27)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(jLabel4))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtPuntoControl, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cmbResultado, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(26, 26, 26)
                .addComponent(jLabel8)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(txtObservaciones, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 12, Short.MAX_VALUE)
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

            if (txtPuntoControl.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "Ingrese el punto de control.");
                return;
            }

            ControlCalidad control = new ControlCalidad();

            control.setIdOrden(Integer.parseInt(
                    cmbOrden.getSelectedItem().toString()));

            control.setIdMecanico(Integer.parseInt(
                    cmbMecanico.getSelectedItem().toString()));

            control.setFecha(LocalDateTime.now());

            control.setDescripcion(
                    txtPuntoControl.getText().trim());

            control.setResultado(
                    cmbResultado.getSelectedItem().toString());

            control.setObservaciones(
                    txtObservaciones.getText().trim());

            if (controlControlador.guardar(control)) {

                JOptionPane.showMessageDialog(this,
                        "Control de calidad registrado correctamente.");

                cargarControles();
                limpiarCampos();

            } else {

                JOptionPane.showMessageDialog(this,
                        "No se pudo registrar el control.");
            }

        } catch (SQLException | IllegalArgumentException e) {

            JOptionPane.showMessageDialog(this,
                    "Error al guardar: " + e.getMessage());
        }
    }//GEN-LAST:event_btnGuardarActionPerformed

    private void btnActualizarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnActualizarActionPerformed

        try {

            if (txtIdControl.getText().trim().isEmpty()) {

                JOptionPane.showMessageDialog(this,
                        "Seleccione un control de la tabla.");
                return;
            }

            if (cmbOrden.getSelectedItem() == null
                    || cmbMecanico.getSelectedItem() == null) {

                JOptionPane.showMessageDialog(this,
                        "Seleccione la orden y el mecánico.");
                return;
            }

            if (txtPuntoControl.getText().trim().isEmpty()) {

                JOptionPane.showMessageDialog(this,
                        "Ingrese el punto de control.");
                return;
            }

            ControlCalidad control = new ControlCalidad();

            control.setIdControl(Integer.parseInt(
                    txtIdControl.getText().trim()));

            control.setIdOrden(Integer.parseInt(
                    cmbOrden.getSelectedItem().toString()));

            control.setIdMecanico(Integer.parseInt(
                    cmbMecanico.getSelectedItem().toString()));

            control.setFecha(LocalDateTime.now());

            control.setDescripcion(
                    txtPuntoControl.getText().trim());

            control.setResultado(
                    cmbResultado.getSelectedItem().toString());

            control.setObservaciones(
                    txtObservaciones.getText().trim());

            int confirmacion = JOptionPane.showConfirmDialog(
                    this,
                    "¿Desea actualizar este control de calidad?",
                    "Confirmar actualización",
                    JOptionPane.YES_NO_OPTION
            );

            if (confirmacion != JOptionPane.YES_OPTION) {
                return;
            }

            if (controlControlador.actualizar(control)) {

                JOptionPane.showMessageDialog(this,
                        "Control de calidad actualizado correctamente.");

                cargarControles();
                limpiarCampos();

            } else {

                JOptionPane.showMessageDialog(this,
                        "No se pudo actualizar el control.");
            }

        } catch (SQLException | IllegalArgumentException e) {

            JOptionPane.showMessageDialog(this,
                    "Error al actualizar: " + e.getMessage());
        }  
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
        java.awt.EventQueue.invokeLater(() -> new FrmControlCalidad().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnActualizar;
    private javax.swing.JButton btnGuardar;
    private javax.swing.JButton btnLimpiar;
    private javax.swing.JButton btnRegresar;
    private javax.swing.JComboBox<String> cmbMecanico;
    private javax.swing.JComboBox<String> cmbOrden;
    private javax.swing.JComboBox<String> cmbResultado;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tablaControles;
    private javax.swing.JTextField txtFecha;
    private javax.swing.JTextField txtIdControl;
    private javax.swing.JTextField txtObservaciones;
    private javax.swing.JTextField txtPuntoControl;
    // End of variables declaration//GEN-END:variables
}
