/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package vista;
import controlador.InventarioControlador;
import controlador.ProveedorControlador;
import modelo.Repuesto;
import modelo.Proveedor;
import java.sql.SQLException;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author gesle
 */
public class FrmRepuestos extends javax.swing.JFrame {
    private final InventarioControlador controlador = new InventarioControlador();
    private final ProveedorControlador proveedorControlador = new ProveedorControlador();
    private DefaultTableModel modeloTabla;
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(FrmRepuestos.class.getName());

    /**
     * Creates new form FrmRepuestos
     */
    public FrmRepuestos() {
        initComponents();

        txtIdRepuesto.setEditable(false);

        modeloTabla = (DefaultTableModel) tablaRepuestos.getModel();

        modeloTabla.setColumnIdentifiers(new String[]{
            "ID", "Código", "Nombre", "Descripción",
            "Proveedor", "Existencia", "Stock mínimo", "Costo"
        });

        cargarProveedores();

        tablaRepuestos.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                seleccionarRepuesto();
            }
        });

        cargarRepuestos();
    }

    private void cargarProveedores() {
        try {
            cmbProveedor.removeAllItems();

            List<Proveedor> proveedores = proveedorControlador.listar();

            for (Proveedor p : proveedores) {
                if ("ACTIVO".equalsIgnoreCase(p.getEstado())) {
                    cmbProveedor.addItem(
                        p.getIdProveedor() + " - " + p.getNombre()
                    );
                }
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this,
                    "Error al cargar proveedores: " + e.getMessage());
        }
    }

    private void cargarRepuestos() {
        try {
            modeloTabla.setRowCount(0);

            List<Repuesto> repuestos = controlador.listar();

            for (Repuesto r : repuestos) {
                Proveedor p = proveedorControlador.buscarPorId(r.getIdProveedor());

                String nombreProveedor = p != null ? p.getNombre() : "Sin proveedor";

                modeloTabla.addRow(new Object[]{
                    r.getIdRepuesto(),
                    r.getCodigo(),
                    r.getNombre(),
                    r.getDescripcion(),
                    nombreProveedor,
                    r.getExistencia(),
                    r.getStockMinimo(),
                    r.getCosto()
                });
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this,
                    "Error al cargar repuestos: " + e.getMessage());
        }
    }


    private void seleccionarRepuesto() {
        int fila = tablaRepuestos.getSelectedRow();

        if (fila >= 0) {
            txtIdRepuesto.setText(modeloTabla.getValueAt(fila, 0).toString());
            txtCodigo.setText(modeloTabla.getValueAt(fila, 1).toString());
            txtNombre.setText(modeloTabla.getValueAt(fila, 2).toString());
            txtDescripcion.setText(modeloTabla.getValueAt(fila, 3).toString());

            int idProveedor = 0;

            try {
                Repuesto r = controlador.buscarPorId(
                        Integer.parseInt(txtIdRepuesto.getText())
                );

                idProveedor = r.getIdProveedor();

            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, e.getMessage());
            }

            for (int i = 0; i < cmbProveedor.getItemCount(); i++) {
                String elemento = cmbProveedor.getItemAt(i);

                if (elemento.startsWith(idProveedor + " - ")) {
                    cmbProveedor.setSelectedIndex(i);
                    break;
                }
            }

            txtExistencia.setText(modeloTabla.getValueAt(fila, 5).toString());
            txtStockMinimo.setText(modeloTabla.getValueAt(fila, 6).toString());
            txtCosto.setText(modeloTabla.getValueAt(fila, 7).toString());
        }
    }

    private void limpiarCampos() {
        txtIdRepuesto.setText("");
        txtCodigo.setText("");
        txtNombre.setText("");
        txtDescripcion.setText("");
        txtExistencia.setText("");
        txtStockMinimo.setText("");
        txtCosto.setText("");

        if (cmbProveedor.getItemCount() > 0) {
            cmbProveedor.setSelectedIndex(0);
        }

        tablaRepuestos.clearSelection();
        txtCodigo.requestFocus();
    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane3 = new javax.swing.JScrollPane();
        txtNombre = new javax.swing.JTextPane();
        jLabel6 = new javax.swing.JLabel();
        jScrollPane9 = new javax.swing.JScrollPane();
        tablaRepuestos = new javax.swing.JTable();
        btnLimpiar = new javax.swing.JButton();
        jScrollPane4 = new javax.swing.JScrollPane();
        txtExistencia = new javax.swing.JTextPane();
        jLabel1 = new javax.swing.JLabel();
        cmbProveedor = new javax.swing.JComboBox<>();
        jLabel3 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        txtIdRepuesto = new javax.swing.JTextPane();
        btnGuardar = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        btnActualizar = new javax.swing.JButton();
        jScrollPane5 = new javax.swing.JScrollPane();
        txtDescripcion = new javax.swing.JTextPane();
        jLabel8 = new javax.swing.JLabel();
        jScrollPane6 = new javax.swing.JScrollPane();
        txtStockMinimo = new javax.swing.JTextPane();
        jLabel9 = new javax.swing.JLabel();
        jScrollPane7 = new javax.swing.JScrollPane();
        txtCosto = new javax.swing.JTextPane();
        jScrollPane8 = new javax.swing.JScrollPane();
        txtCodigo = new javax.swing.JTextPane();
        btnRegresar2 = new javax.swing.JButton();
        btnLimpiar1 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jScrollPane3.setViewportView(txtNombre);

        jLabel6.setText("Proveedor");

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
        jScrollPane9.setViewportView(tablaRepuestos);

        btnLimpiar.setBackground(new java.awt.Color(204, 255, 204));
        btnLimpiar.setText("Limpiar");
        btnLimpiar.addActionListener(this::btnLimpiarActionPerformed);

        jScrollPane4.setViewportView(txtExistencia);

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel1.setText("GESTION DE REPUESTOS");

        jLabel3.setText("ID");

        jLabel2.setText("Código");

        jScrollPane2.setViewportView(txtIdRepuesto);

        btnGuardar.setBackground(new java.awt.Color(255, 255, 204));
        btnGuardar.setText("Guardar");
        btnGuardar.addActionListener(this::btnGuardarActionPerformed);

        jLabel5.setText("Descripción");
        jLabel5.setToolTipText("");

        jLabel7.setText("Existencia");
        jLabel7.setToolTipText("");

        jLabel4.setText("Nombre");

        btnActualizar.setBackground(new java.awt.Color(255, 204, 204));
        btnActualizar.setText("Actualizar");
        btnActualizar.setToolTipText("");
        btnActualizar.addActionListener(this::btnActualizarActionPerformed);

        jScrollPane5.setViewportView(txtDescripcion);

        jLabel8.setText("Stock mínimo");
        jLabel8.setToolTipText("");

        jScrollPane6.setViewportView(txtStockMinimo);

        jLabel9.setText("Costo");
        jLabel9.setToolTipText("");

        jScrollPane7.setViewportView(txtCosto);

        jScrollPane8.setViewportView(txtCodigo);

        btnRegresar2.setBackground(new java.awt.Color(204, 255, 255));
        btnRegresar2.setText("Menu");
        btnRegresar2.addActionListener(this::btnRegresar2ActionPerformed);

        btnLimpiar1.setBackground(new java.awt.Color(123, 224, 199));
        btnLimpiar1.setText("Proveedores");
        btnLimpiar1.addActionListener(this::btnLimpiar1ActionPerformed);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(49, 49, 49)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(jScrollPane7, javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jScrollPane6, javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel9)
                                    .addComponent(jLabel8)
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(jLabel3)
                                        .addComponent(jLabel7)
                                        .addComponent(jLabel6)
                                        .addComponent(jLabel5)
                                        .addComponent(jLabel4)
                                        .addComponent(jLabel2)
                                        .addComponent(jScrollPane4)
                                        .addComponent(jScrollPane3)
                                        .addComponent(cmbProveedor, 0, 198, Short.MAX_VALUE)
                                        .addComponent(jScrollPane2)
                                        .addComponent(jScrollPane5)
                                        .addComponent(jScrollPane8)))))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 23, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(41, 41, 41)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(btnLimpiar, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnGuardar, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnLimpiar1, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(btnActualizar, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 94, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnRegresar2, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(94, 94, 94)))
                .addComponent(jScrollPane9, javax.swing.GroupLayout.PREFERRED_SIZE, 1152, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(39, 39, 39)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane9)
                        .addGap(25, 25, 25))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addGap(41, 41, 41)
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(19, 19, 19)
                        .addComponent(jLabel4)
                        .addGap(8, 8, 8)
                        .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(12, 12, 12)
                        .addComponent(jLabel6)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(cmbProveedor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel7)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel8)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel9)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnGuardar)
                            .addComponent(btnActualizar))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnLimpiar)
                            .addComponent(btnLimpiar1))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnRegresar2)
                        .addGap(0, 12, Short.MAX_VALUE))))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnLimpiarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLimpiarActionPerformed
        limpiarCampos();
    }//GEN-LAST:event_btnLimpiarActionPerformed

    private void btnGuardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGuardarActionPerformed
        try {
            if (txtCodigo.getText().trim().isEmpty()
                    || txtNombre.getText().trim().isEmpty()
                    || txtExistencia.getText().trim().isEmpty()
                    || txtStockMinimo.getText().trim().isEmpty()
                    || txtCosto.getText().trim().isEmpty()
                    || cmbProveedor.getSelectedItem() == null) {

                JOptionPane.showMessageDialog(this,
                        "Completa los campos obligatorios.");
                return;
            }

            Repuesto r = new Repuesto();

            r.setCodigo(txtCodigo.getText().trim());
            r.setNombre(txtNombre.getText().trim());
            r.setDescripcion(txtDescripcion.getText().trim());

            int idProveedor = Integer.parseInt(
                    cmbProveedor.getSelectedItem().toString().split(" - ")[0]
            );

            r.setIdProveedor(idProveedor);
            r.setExistencia(Integer.parseInt(txtExistencia.getText().trim()));
            r.setStockMinimo(Integer.parseInt(txtStockMinimo.getText().trim()));
            r.setCosto(Double.parseDouble(txtCosto.getText().trim()));

            if (controlador.guardarRepuesto(r)) {
                JOptionPane.showMessageDialog(this,
                        "Repuesto registrado correctamente.");

                limpiarCampos();
                cargarRepuestos();
            }

        } catch (SQLException | NumberFormatException e) {
            JOptionPane.showMessageDialog(this,
                    "Error al guardar: " + e.getMessage());
        }
    }//GEN-LAST:event_btnGuardarActionPerformed

    private void btnActualizarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnActualizarActionPerformed
        try {
            if (txtIdRepuesto.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "Selecciona un repuesto.");
                return;
            }

            Repuesto r = new Repuesto();

            r.setIdRepuesto(Integer.parseInt(txtIdRepuesto.getText()));
            r.setCodigo(txtCodigo.getText().trim());
            r.setNombre(txtNombre.getText().trim());
            r.setDescripcion(txtDescripcion.getText().trim());

            int idProveedor = Integer.parseInt(
                    cmbProveedor.getSelectedItem().toString().split(" - ")[0]
            );

            r.setIdProveedor(idProveedor);
            r.setExistencia(Integer.parseInt(txtExistencia.getText().trim()));
            r.setStockMinimo(Integer.parseInt(txtStockMinimo.getText().trim()));
            r.setCosto(Double.parseDouble(txtCosto.getText().trim()));

            if (controlador.actualizarRepuesto(r)) {
                JOptionPane.showMessageDialog(this,
                        "Repuesto actualizado correctamente.");

                limpiarCampos();
                cargarRepuestos();
            }

        } catch (SQLException | NumberFormatException e) {
            JOptionPane.showMessageDialog(this,
                    "Error al actualizar: " + e.getMessage());
        }    
    }//GEN-LAST:event_btnActualizarActionPerformed

    private void btnRegresar2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRegresar2ActionPerformed
        FrmPrincipal principal = new FrmPrincipal();
        principal.setLocationRelativeTo(null);
        principal.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_btnRegresar2ActionPerformed

    private void btnLimpiar1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLimpiar1ActionPerformed
        FrmProveedores formulario = new FrmProveedores();
        formulario.setLocationRelativeTo(this);
        formulario.setVisible(true);
    }//GEN-LAST:event_btnLimpiar1ActionPerformed

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
        java.awt.EventQueue.invokeLater(() -> new FrmRepuestos().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnActualizar;
    private javax.swing.JButton btnGuardar;
    private javax.swing.JButton btnLimpiar;
    private javax.swing.JButton btnLimpiar1;
    private javax.swing.JButton btnRegresar2;
    private javax.swing.JComboBox<String> cmbProveedor;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JScrollPane jScrollPane8;
    private javax.swing.JScrollPane jScrollPane9;
    private javax.swing.JTable tablaRepuestos;
    private javax.swing.JTextPane txtCodigo;
    private javax.swing.JTextPane txtCosto;
    private javax.swing.JTextPane txtDescripcion;
    private javax.swing.JTextPane txtExistencia;
    private javax.swing.JTextPane txtIdRepuesto;
    private javax.swing.JTextPane txtNombre;
    private javax.swing.JTextPane txtStockMinimo;
    // End of variables declaration//GEN-END:variables
}
