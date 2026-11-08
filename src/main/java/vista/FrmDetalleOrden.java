/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package vista;
import controlador.OrdenTrabajoControlador;
import controlador.ServicioControlador;
import controlador.DetalleOrdenControlador;

import modelo.OrdenTrabajo;
import modelo.Servicio;
import modelo.DetalleOrden;

import java.sql.SQLException;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;


/**
 *
 * @author gesle
 */
public class FrmDetalleOrden extends javax.swing.JFrame {
    private final OrdenTrabajoControlador ordenControlador =
    new OrdenTrabajoControlador();

    private final ServicioControlador servicioControlador =
    new ServicioControlador();

    private final DetalleOrdenControlador detalleControlador =
    new DetalleOrdenControlador();

    private List<OrdenTrabajo> listaOrdenes;
    private List<Servicio> listaServicios;    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(FrmDetalleOrden.class.getName());

    /**
     * Creates new form FrmDetalleOrden
     */
    public FrmDetalleOrden() {
        initComponents();

        configurarTabla();
        cargarOrdenes();
        cargarServicios();

        cmbServicio.addActionListener(e -> seleccionarServicio());

        txtCantidad.getDocument().addDocumentListener(
                new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                calcularSubtotal();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                calcularSubtotal();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                calcularSubtotal();
            }
        });

        btnGuardar.addActionListener(e -> guardarDetalle());
        btnLimpiar.addActionListener(e -> limpiarFormulario());
        cmbOrden.addActionListener(e -> {
        if (cmbOrden.getSelectedItem() != null) {
            int idOrden = Integer.parseInt(
                    cmbOrden.getSelectedItem().toString().split(" - ")[0]
            );

            cargarDetalles(idOrden);
        }
    });
    }
    public FrmDetalleOrden(int idOrden) {
        this();

        for (int i = 0; i < cmbOrden.getItemCount(); i++) {
            String opcion = cmbOrden.getItemAt(i);

            if (opcion.startsWith(idOrden + " - ")) {
                cmbOrden.setSelectedIndex(i);
                break;
            }
        }

        cargarDetalles(idOrden);
        setLocationRelativeTo(null);
    }
    private void configurarTabla() {

        tablaDetalles.setModel(new DefaultTableModel(
                new Object[][]{},
                new String[]{
                    "ID", "Servicio", "Cantidad",
                    "Costo", "Tiempo", "Subtotal"
                }
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        });
    }

    private void cargarOrdenes() {

        try {

            cmbOrden.removeAllItems();

            listaOrdenes = ordenControlador.listar();

            for (OrdenTrabajo orden : listaOrdenes) {

                cmbOrden.addItem(
                        orden.getIdOrden() + " - " + orden.getEstado()
                );
            }

        } catch (SQLException ex) {

            JOptionPane.showMessageDialog(this,
                    "Error al cargar órdenes: " + ex.getMessage());
        }
    }

    private void cargarServicios() {

        try {

            cmbServicio.removeAllItems();

            listaServicios = servicioControlador.listar();

            for (Servicio servicio : listaServicios) {

                if ("ACTIVO".equalsIgnoreCase(servicio.getEstado())) {

                    cmbServicio.addItem(
                            servicio.getIdServicio()
                            + " - " + servicio.getNombre()
                    );
                }
            }

        } catch (SQLException ex) {

            JOptionPane.showMessageDialog(this,
                    "Error al cargar servicios: " + ex.getMessage());
        }
    }

    private Servicio obtenerServicioSeleccionado() {

        int indice = cmbServicio.getSelectedIndex();

        if (indice < 0) {
            return null;
        }

        int contador = -1;

        for (Servicio servicio : listaServicios) {

            if ("ACTIVO".equalsIgnoreCase(servicio.getEstado())) {

                contador++;

                if (contador == indice) {
                    return servicio;
                }
            }
        }

        return null;
    }

    private void seleccionarServicio() {

        Servicio servicio = obtenerServicioSeleccionado();

        if (servicio == null) {
            return;
        }

        txtCostoManoObra.setText(
                String.valueOf(servicio.getCostoManoObra())
        );

        txtTiempoEstimado.setText(
                String.valueOf(servicio.getTiempoEstimado())
        );

        calcularSubtotal();
    }

    private void calcularSubtotal() {

        try {

            Servicio servicio = obtenerServicioSeleccionado();

            if (servicio == null) {
                txtSubtotal.setText("");
                return;
            }

            int cantidad = Integer.parseInt(txtCantidad.getText().trim());

            if (cantidad <= 0) {
                txtSubtotal.setText("");
                return;
            }

            double subtotal = cantidad * servicio.getCostoManoObra();

            txtSubtotal.setText(String.format(
                    java.util.Locale.US, "%.2f", subtotal
            ));

        } catch (NumberFormatException ex) {

            txtSubtotal.setText("");
        }
    }

    private void guardarDetalle() {

        try {

            if (cmbOrden.getSelectedIndex() < 0) {
                JOptionPane.showMessageDialog(this,
                        "Seleccione una orden de trabajo.");
                return;
            }

            Servicio servicio = obtenerServicioSeleccionado();

            if (servicio == null) {
                JOptionPane.showMessageDialog(this,
                        "Seleccione un servicio.");
                return;
            }

            int cantidad = Integer.parseInt(txtCantidad.getText().trim());

            if (cantidad <= 0) {
                JOptionPane.showMessageDialog(this,
                        "La cantidad debe ser mayor que cero.");
                return;
            }

            OrdenTrabajo orden =
                    listaOrdenes.get(cmbOrden.getSelectedIndex());

            DetalleOrden detalle = new DetalleOrden();

            detalle.setIdOrden(orden.getIdOrden());
            detalle.setIdServicio(servicio.getIdServicio());
            detalle.setCantidad(cantidad);
            detalle.setCostoManoObra(servicio.getCostoManoObra());
            detalle.setTiempoEstimado(servicio.getTiempoEstimado());
            detalle.setSubtotal(
                    cantidad * servicio.getCostoManoObra()
            );

            boolean guardado = detalleControlador.guardar(detalle);

            if (guardado) {

                JOptionPane.showMessageDialog(this,
                        "Servicio agregado correctamente.");

                cargarDetalles(orden.getIdOrden());
                limpiarCamposDetalle();
            }

        } catch (NumberFormatException ex) {

            JOptionPane.showMessageDialog(this,
                    "Ingrese una cantidad válida.");

        } catch (SQLException | IllegalArgumentException ex) {

            JOptionPane.showMessageDialog(this,
                    "No se pudo guardar: " + ex.getMessage());
        }
    }

    private void cargarDetalles(int idOrden) {

        try {

            DefaultTableModel modelo =
                    (DefaultTableModel) tablaDetalles.getModel();

            modelo.setRowCount(0);

            List<DetalleOrden> detalles =
                    detalleControlador.listarPorOrden(idOrden);

            for (DetalleOrden detalle : detalles) {

                modelo.addRow(new Object[]{
                    detalle.getIdDetalle(),
                    detalle.getNombreServicio(),
                    detalle.getCantidad(),
                    detalle.getCostoManoObra(),
                    detalle.getTiempoEstimado(),
                    detalle.getSubtotal()
                });
            }

        } catch (SQLException ex) {

            JOptionPane.showMessageDialog(this,
                    "Error al cargar detalles: " + ex.getMessage());
        }
    }

    private void limpiarCamposDetalle() {

        txtCantidad.setText("");
        txtCostoManoObra.setText("");
        txtTiempoEstimado.setText("");
        txtSubtotal.setText("");

        cmbServicio.setSelectedIndex(-1);
    }

    private void limpiarFormulario() {

        limpiarCamposDetalle();

        if (cmbOrden.getItemCount() > 0) {
            cmbOrden.setSelectedIndex(0);
        }

        ((DefaultTableModel) tablaDetalles.getModel()).setRowCount(0);
    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel3 = new javax.swing.JLabel();
        jScrollPane9 = new javax.swing.JScrollPane();
        tablaDetalles = new javax.swing.JTable();
        jLabel5 = new javax.swing.JLabel();
        jScrollPane5 = new javax.swing.JScrollPane();
        txtCostoManoObra = new javax.swing.JTextPane();
        jScrollPane3 = new javax.swing.JScrollPane();
        txtCantidad = new javax.swing.JTextPane();
        jLabel2 = new javax.swing.JLabel();
        jScrollPane6 = new javax.swing.JScrollPane();
        txtTiempoEstimado = new javax.swing.JTextPane();
        jLabel9 = new javax.swing.JLabel();
        jScrollPane7 = new javax.swing.JScrollPane();
        txtSubtotal = new javax.swing.JTextPane();
        jLabel6 = new javax.swing.JLabel();
        cmbOrden = new javax.swing.JComboBox<>();
        jLabel1 = new javax.swing.JLabel();
        btnLimpiar = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        btnGuardar = new javax.swing.JButton();
        cmbServicio = new javax.swing.JComboBox<>();
        btnRegresar = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jLabel3.setText("Orden de trabajo");
        jLabel3.setToolTipText("");

        tablaDetalles.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane9.setViewportView(tablaDetalles);

        jLabel5.setText("Costo de mano de obra");
        jLabel5.setToolTipText("");

        jScrollPane5.setViewportView(txtCostoManoObra);

        jScrollPane3.setViewportView(txtCantidad);

        jLabel2.setText("Servicio");

        jScrollPane6.setViewportView(txtTiempoEstimado);

        jLabel9.setText("Subtotal");

        jScrollPane7.setViewportView(txtSubtotal);

        jLabel6.setText("Tiempo estimado");

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel1.setText("DETALLE DE ORDENES");

        btnLimpiar.setBackground(new java.awt.Color(204, 255, 204));
        btnLimpiar.setText("Limpiar");
        btnLimpiar.addActionListener(this::btnLimpiarActionPerformed);

        jLabel4.setText("Cantidad");

        btnGuardar.setBackground(new java.awt.Color(255, 255, 204));
        btnGuardar.setText("Guardar");
        btnGuardar.addActionListener(this::btnGuardarActionPerformed);

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
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(jLabel3)
                                .addComponent(jLabel6)
                                .addComponent(jLabel5)
                                .addComponent(jLabel4)
                                .addComponent(jLabel2)
                                .addComponent(jLabel1)
                                .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 225, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 225, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(cmbOrden, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 225, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jScrollPane6, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 225, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(jLabel9)
                                .addComponent(jScrollPane7, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 225, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(cmbServicio, javax.swing.GroupLayout.PREFERRED_SIZE, 225, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addComponent(btnGuardar, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(51, 51, 51)
                                .addComponent(btnLimpiar, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addComponent(btnRegresar)
                                .addGap(93, 93, 93)))))
                .addComponent(jScrollPane9, javax.swing.GroupLayout.PREFERRED_SIZE, 833, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(88, 88, 88))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(39, 39, 39)
                        .addComponent(jLabel1)
                        .addGap(41, 41, 41)
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cmbOrden, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(cmbServicio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(13, 13, 13)
                        .addComponent(jLabel4)
                        .addGap(8, 8, 8)
                        .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(12, 12, 12)
                        .addComponent(jLabel6)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel9)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(27, 27, 27)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnGuardar)
                            .addComponent(btnLimpiar))
                        .addGap(18, 18, 18)
                        .addComponent(btnRegresar))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(31, 31, 31)
                        .addComponent(jScrollPane9, javax.swing.GroupLayout.PREFERRED_SIZE, 494, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(41, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnLimpiarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLimpiarActionPerformed
        limpiarCampos();
    }//GEN-LAST:event_btnLimpiarActionPerformed

    private void btnGuardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGuardarActionPerformed
   
    }//GEN-LAST:event_btnGuardarActionPerformed

    private void btnRegresarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRegresarActionPerformed
        FrmPrincipal principal = new FrmPrincipal();
        principal.setLocationRelativeTo(null);
        principal.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_btnRegresarActionPerformed
    private void limpiarCampos() {
        txtCantidad.setText("");
        txtCostoManoObra.setText("");
        txtTiempoEstimado.setText("");
        txtSubtotal.setText("");

        cmbServicio.setSelectedIndex(-1);
        cmbOrden.setSelectedIndex(-1);

        tablaDetalles.clearSelection();
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
        java.awt.EventQueue.invokeLater(() -> new FrmDetalleOrden().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnGuardar;
    private javax.swing.JButton btnLimpiar;
    private javax.swing.JButton btnRegresar;
    private javax.swing.JComboBox<String> cmbOrden;
    private javax.swing.JComboBox<String> cmbServicio;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JScrollPane jScrollPane9;
    private javax.swing.JTable tablaDetalles;
    private javax.swing.JTextPane txtCantidad;
    private javax.swing.JTextPane txtCostoManoObra;
    private javax.swing.JTextPane txtSubtotal;
    private javax.swing.JTextPane txtTiempoEstimado;
    // End of variables declaration//GEN-END:variables
}
