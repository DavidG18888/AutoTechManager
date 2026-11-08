/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package vista;
import controlador.PagoControlador;
import controlador.PresupuestoControlador;
import controlador.OrdenTrabajoControlador;
import controlador.ClienteControlador;

import modelo.Pago;
import modelo.Presupuesto;
import modelo.OrdenTrabajo;
import modelo.Cliente;

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
public class FrmPagos extends javax.swing.JFrame {
    private final PagoControlador pagoControlador = new PagoControlador();
    private final PresupuestoControlador presupuestoControlador = new PresupuestoControlador();
    private final OrdenTrabajoControlador ordenControlador = new OrdenTrabajoControlador();
    private final ClienteControlador clienteControlador = new ClienteControlador();

    private Presupuesto presupuestoSeleccionado;
    private int idPresupuestoSeleccionado = 0;
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(FrmPagos.class.getName());

    /**
     * Creates new form FrmPagos
     */
    public FrmPagos() {
        initComponents();

        configurarTabla();
        configurarCampos();

        cmbOrden.addActionListener(e -> cargarDatosOrden());

        cargarOrdenesDisponibles();

        if (cmbOrden.getItemCount() > 0) {
            cmbOrden.setSelectedIndex(0);
            cargarDatosOrden();
        }
    }

    private void configurarTabla() {

        DefaultTableModel modelo = new DefaultTableModel(
            new Object[][]{},
            new String[]{
                "ID Pago", "Fecha", "Monto",
                "Método de pago", "Referencia", "Observaciones"
            }
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tablaPagos.setModel(modelo);
    }

    private void configurarCampos() {

        txtIdPago.setEditable(false);
        txtCliente.setEditable(false);
        txtPresupuesto.setEditable(false);
        txtTotalPresupuesto.setEditable(false);
        txtTotalPagado.setEditable(false);
        txtSaldoPendiente.setEditable(false);
        txtFecha.setEditable(false);

        txtFecha.setText(LocalDateTime.now().format(
            DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")
        ));
    }

    private void cargarOrdenesDisponibles() {

        try {
            cmbOrden.removeAllItems();

            List<OrdenTrabajo> ordenes = ordenControlador.listar();

            for (OrdenTrabajo orden : ordenes) {

                Presupuesto presupuesto =
                        presupuestoControlador.buscarPorOrden(orden.getIdOrden());

                if (presupuesto != null && presupuesto.isAutorizado()) {

                    double pagado = pagoControlador.calcularTotalPagado(
                            presupuesto.getIdPresupuesto());

                    double saldo = presupuesto.getTotal() - pagado;

                    if (saldo > 0) {
                        cmbOrden.addItem(String.valueOf(orden.getIdOrden()));
                    }
                }
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this,
                    "Error al cargar las órdenes: " + e.getMessage());
        }
    }

    private void cargarDatosOrden() {

        if (cmbOrden.getSelectedItem() == null) {
            return;
        }

        try {

            int idOrden = Integer.parseInt(
                    cmbOrden.getSelectedItem().toString()
            );

            presupuestoSeleccionado =
                    presupuestoControlador.buscarPorOrden(idOrden);

            if (presupuestoSeleccionado == null) {
                JOptionPane.showMessageDialog(this,
                        "Esta orden no tiene un presupuesto registrado.");
                return;
            }

            if (!presupuestoSeleccionado.isAutorizado()) {
                JOptionPane.showMessageDialog(this,
                        "El presupuesto todavía no está autorizado.");
                return;
            }

            idPresupuestoSeleccionado =
                    presupuestoSeleccionado.getIdPresupuesto();

            OrdenTrabajo orden =
                    ordenControlador.buscarPorId(idOrden);

            if (orden == null) {
                JOptionPane.showMessageDialog(this,
                        "No se encontró la orden de trabajo.");
                return;
            }

            Cliente cliente =
                    clienteControlador.buscarPorId(orden.getIdCliente());

            if (cliente == null) {
                JOptionPane.showMessageDialog(this,
                        "No se encontró el cliente de esta orden.");
                return;
            }

            txtCliente.setText(cliente.getNombre());

            txtPresupuesto.setText(
                    String.valueOf(idPresupuestoSeleccionado)
            );

            double total = presupuestoSeleccionado.getTotal();

            double pagado =
                    pagoControlador.calcularTotalPagado(
                            idPresupuestoSeleccionado
                    );

            double saldo = Math.max(0, total - pagado);

            txtTotalPresupuesto.setText(String.format("%.2f", total));
            txtTotalPagado.setText(String.format("%.2f", pagado));
            txtSaldoPendiente.setText(String.format("%.2f", saldo));

            cargarPagos();

        } catch (SQLException e) {

            JOptionPane.showMessageDialog(this,
                    "Error al consultar: " + e.getMessage());

        } catch (NumberFormatException e) {

            JOptionPane.showMessageDialog(this,
                    "El número de orden no es válido.");
        }
    }

private void cargarPagos() {

    if (idPresupuestoSeleccionado <= 0) {
        return;
    }

    try {

        DefaultTableModel modelo =
            (DefaultTableModel) tablaPagos.getModel();

        modelo.setRowCount(0);

        List<Pago> pagos =
            pagoControlador.listarPorPresupuesto(
                idPresupuestoSeleccionado
            );

        DateTimeFormatter formato =
            DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

        for (Pago pago : pagos) {

            modelo.addRow(new Object[]{
                pago.getIdPago(),
                pago.getFecha() != null
                    ? pago.getFecha().format(formato) : "",
                pago.getMonto(),
                pago.getMetodoPago(),
                pago.getReferencia(),
                pago.getObservaciones()
            });
        }

    } catch (SQLException e) {

        JOptionPane.showMessageDialog(
            this,
            "Error al cargar los pagos: " + e.getMessage()
        );
    }
}

private void limpiarCampos() {

    txtIdPago.setText("");
    txtCliente.setText("");
    txtPresupuesto.setText("");
    txtTotalPresupuesto.setText("");
    txtTotalPagado.setText("");
    txtSaldoPendiente.setText("");
    txtMonto.setText("");
    txtReferencia.setText("");
    txtObservaciones.setText("");

    txtFecha.setText(LocalDateTime.now().format(
        DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")
    ));

    cmbOrden.setSelectedIndex(-1);
    cmbMetodoPago.setSelectedIndex(0);

    presupuestoSeleccionado = null;
    idPresupuestoSeleccionado = 0;

    ((DefaultTableModel) tablaPagos.getModel()).setRowCount(0);
}

private static class OrdenPagoItem {

    private final int idOrden;
    private final int idPresupuesto;

    public OrdenPagoItem(int idOrden, int idPresupuesto) {
        this.idOrden = idOrden;
        this.idPresupuesto = idPresupuesto;
    }

    @Override
    public String toString() {
        return "Orden #" + idOrden;
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

        jLabel8 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        cmbMetodoPago = new javax.swing.JComboBox<>();
        btnActualizar = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jScrollPane5 = new javax.swing.JScrollPane();
        txtPresupuesto = new javax.swing.JTextPane();
        jScrollPane7 = new javax.swing.JScrollPane();
        txtTotalPagado = new javax.swing.JTextPane();
        jLabel7 = new javax.swing.JLabel();
        jScrollPane8 = new javax.swing.JScrollPane();
        txtSaldoPendiente = new javax.swing.JTextPane();
        jScrollPane2 = new javax.swing.JScrollPane();
        txtIdPago = new javax.swing.JTextPane();
        jLabel6 = new javax.swing.JLabel();
        cmbOrden = new javax.swing.JComboBox<>();
        jLabel1 = new javax.swing.JLabel();
        btnLimpiar = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        btnGuardar = new javax.swing.JButton();
        jScrollPane3 = new javax.swing.JScrollPane();
        txtCliente = new javax.swing.JTextPane();
        jLabel2 = new javax.swing.JLabel();
        jScrollPane6 = new javax.swing.JScrollPane();
        txtTotalPresupuesto = new javax.swing.JTextPane();
        jLabel10 = new javax.swing.JLabel();
        jScrollPane4 = new javax.swing.JScrollPane();
        txtMonto = new javax.swing.JTextPane();
        jLabel11 = new javax.swing.JLabel();
        jScrollPane10 = new javax.swing.JScrollPane();
        txtFecha = new javax.swing.JTextPane();
        jLabel12 = new javax.swing.JLabel();
        jScrollPane12 = new javax.swing.JScrollPane();
        txtObservaciones = new javax.swing.JTextPane();
        jLabel13 = new javax.swing.JLabel();
        jScrollPane11 = new javax.swing.JScrollPane();
        tablaPagos = new javax.swing.JTable();
        jScrollPane9 = new javax.swing.JScrollPane();
        txtReferencia = new javax.swing.JTextPane();
        btnRegresar2 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel8.setText("Método de pago");
        jLabel8.setToolTipText("");

        jLabel3.setText("ID Pago");
        jLabel3.setToolTipText("");

        cmbMetodoPago.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "EFECTIVO", "TARJETA", "TRANSFERENCIA" }));

        btnActualizar.setBackground(new java.awt.Color(255, 204, 204));
        btnActualizar.setText("Consultar");
        btnActualizar.addActionListener(this::btnActualizarActionPerformed);

        jLabel5.setText("Presupuesto");
        jLabel5.setToolTipText("");

        jLabel9.setText("Total pagado");

        jScrollPane5.setViewportView(txtPresupuesto);

        jScrollPane7.setViewportView(txtTotalPagado);

        jLabel7.setText("Monto a pagar");
        jLabel7.setToolTipText("");

        jScrollPane8.setViewportView(txtSaldoPendiente);

        jScrollPane2.setViewportView(txtIdPago);

        jLabel6.setText("Total del presupuesto");

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel1.setText("PAGOS");

        btnLimpiar.setBackground(new java.awt.Color(204, 255, 204));
        btnLimpiar.setText("Limpiar");
        btnLimpiar.addActionListener(this::btnLimpiarActionPerformed);

        jLabel4.setText("Cliente");

        btnGuardar.setBackground(new java.awt.Color(255, 255, 204));
        btnGuardar.setText("Registrar pago");
        btnGuardar.addActionListener(this::btnGuardarActionPerformed);

        jScrollPane3.setViewportView(txtCliente);

        jLabel2.setText("Orden de trabajo");

        jScrollPane6.setViewportView(txtTotalPresupuesto);

        jLabel10.setText("Saldo pendiente");

        jScrollPane4.setViewportView(txtMonto);

        jLabel11.setText("Fecha de pago");

        jScrollPane10.setViewportView(txtFecha);

        jLabel12.setText("Referencia");
        jLabel12.setToolTipText("");

        jScrollPane12.setViewportView(txtObservaciones);

        jLabel13.setText("Observaciones");
        jLabel13.setToolTipText("");

        tablaPagos.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane11.setViewportView(tablaPagos);

        jScrollPane9.setViewportView(txtReferencia);

        btnRegresar2.setBackground(new java.awt.Color(204, 255, 255));
        btnRegresar2.setText("Menu");
        btnRegresar2.addActionListener(this::btnRegresar2ActionPerformed);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(49, 49, 49)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addGroup(layout.createSequentialGroup()
                            .addGap(0, 0, Short.MAX_VALUE)
                            .addComponent(btnActualizar, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                            .addGap(143, 143, 143)
                            .addComponent(btnRegresar2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(jLabel3)
                                        .addComponent(jLabel6)
                                        .addComponent(jLabel5)
                                        .addComponent(jLabel4)
                                        .addComponent(jLabel2)
                                        .addComponent(jLabel1)
                                        .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 225, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 225, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 225, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(cmbOrden, javax.swing.GroupLayout.PREFERRED_SIZE, 225, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 225, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(jLabel9)
                                        .addComponent(jScrollPane7, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 225, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(jLabel10)
                                        .addComponent(jScrollPane8, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 225, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(jLabel11)
                                        .addComponent(jScrollPane10, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 225, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jLabel8)
                                        .addComponent(cmbMetodoPago, javax.swing.GroupLayout.PREFERRED_SIZE, 225, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addComponent(jLabel12, javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel13, javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jScrollPane12, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 225, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jScrollPane9, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 225, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGap(0, 0, Short.MAX_VALUE)))
                    .addComponent(jLabel7)
                    .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 225, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnGuardar)
                    .addComponent(btnLimpiar, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 53, Short.MAX_VALUE)
                .addComponent(jScrollPane11, javax.swing.GroupLayout.PREFERRED_SIZE, 889, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(38, 38, 38))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(39, 39, 39)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel3)
                        .addGap(18, 18, 18)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cmbOrden, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel6)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel9)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel10)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel11)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel7)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel8)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cmbMetodoPago, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel12)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel13)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jScrollPane12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(32, 32, 32)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnGuardar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(btnActualizar))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnLimpiar)
                            .addComponent(btnRegresar2)))
                    .addComponent(jScrollPane11))
                .addGap(27, 27, 27))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnActualizarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnActualizarActionPerformed
    cargarDatosOrden();
    }//GEN-LAST:event_btnActualizarActionPerformed

    private void btnLimpiarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLimpiarActionPerformed
        limpiarCampos();
    }//GEN-LAST:event_btnLimpiarActionPerformed

    private void btnGuardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGuardarActionPerformed
        try {

            if (idPresupuestoSeleccionado <= 0) {
                JOptionPane.showMessageDialog(this,
                    "Seleccione una orden de trabajo.");
                return;
            }

            if (txtMonto.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this,
                    "Ingrese el monto del pago.");
                return;
            }

            double monto = Double.parseDouble(txtMonto.getText().trim());

            if (monto <= 0) {
                JOptionPane.showMessageDialog(this,
                    "El monto debe ser mayor que cero.");
                return;
            }

            Pago pago = new Pago();

            pago.setIdPresupuesto(idPresupuestoSeleccionado);
            pago.setFecha(LocalDateTime.now());
            pago.setMonto(monto);
            pago.setMetodoPago(cmbMetodoPago.getSelectedItem().toString());
            pago.setReferencia(txtReferencia.getText().trim());
            pago.setObservaciones(txtObservaciones.getText().trim());

            if (pagoControlador.guardar(pago)) {

                JOptionPane.showMessageDialog(this,
                    "Pago registrado correctamente.");

                cargarDatosOrden();

                txtMonto.setText("");
                txtReferencia.setText("");
                txtObservaciones.setText("");

            } else {

                JOptionPane.showMessageDialog(this,
                    "No se pudo registrar el pago.");
            }

        } catch (NumberFormatException e) {

            JOptionPane.showMessageDialog(this,
                "Ingrese un monto válido.");

        } catch (SQLException e) {

            JOptionPane.showMessageDialog(this,
                "Error al registrar el pago: " + e.getMessage());
        }
    }//GEN-LAST:event_btnGuardarActionPerformed

    private void btnRegresar2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRegresar2ActionPerformed
        FrmPrincipal principal = new FrmPrincipal();
        principal.setLocationRelativeTo(null);
        principal.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_btnRegresar2ActionPerformed

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
        java.awt.EventQueue.invokeLater(() -> new FrmPagos().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnActualizar;
    private javax.swing.JButton btnGuardar;
    private javax.swing.JButton btnLimpiar;
    private javax.swing.JButton btnRegresar2;
    private javax.swing.JComboBox<String> cmbMetodoPago;
    private javax.swing.JComboBox<String> cmbOrden;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JScrollPane jScrollPane10;
    private javax.swing.JScrollPane jScrollPane11;
    private javax.swing.JScrollPane jScrollPane12;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JScrollPane jScrollPane8;
    private javax.swing.JScrollPane jScrollPane9;
    private javax.swing.JTable tablaPagos;
    private javax.swing.JTextPane txtCliente;
    private javax.swing.JTextPane txtFecha;
    private javax.swing.JTextPane txtIdPago;
    private javax.swing.JTextPane txtMonto;
    private javax.swing.JTextPane txtObservaciones;
    private javax.swing.JTextPane txtPresupuesto;
    private javax.swing.JTextPane txtReferencia;
    private javax.swing.JTextPane txtSaldoPendiente;
    private javax.swing.JTextPane txtTotalPagado;
    private javax.swing.JTextPane txtTotalPresupuesto;
    // End of variables declaration//GEN-END:variables
}
