/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package vista;
import controlador.ServicioRepuestoControlador;
import controlador.ServicioControlador;
import controlador.RepuestoControlador;

import modelo.ServicioRepuesto;
import modelo.Servicio;
import modelo.Repuesto;

import java.sql.SQLException;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;


/**
 *
 * @author gesle
 */
public class FrmServiciosRepuestos extends javax.swing.JFrame {


    private final ServicioRepuestoControlador asociacionControlador =
    new ServicioRepuestoControlador();

    private final ServicioControlador servicioControlador =
    new ServicioControlador();

    private final RepuestoControlador repuestoControlador =
    new RepuestoControlador();
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(FrmServiciosRepuestos.class.getName());

    /**
     * Creates new form FrmServiciosRepuestos
     */
    
    public FrmServiciosRepuestos() {
        initComponents();

        setTitle("Asignación de repuestos por servicio");
        setLocationRelativeTo(null);
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        configurarTabla();
        cargarServicios();
        cargarRepuestos();

        cmbServicio.addActionListener(e -> cargarAsociaciones());
    }

    public FrmServiciosRepuestos(int idServicio) {
        this();

        for (int i = 0; i < cmbServicio.getItemCount(); i++) {
            String opcion = cmbServicio.getItemAt(i);

            if (opcion.startsWith(idServicio + " - ")) {
                cmbServicio.setSelectedIndex(i);
                break;
            }
        }

        cargarAsociaciones();
        setLocationRelativeTo(null);
    }
    private void configurarTabla() {

        DefaultTableModel modelo = new DefaultTableModel(
                new Object[][]{},
                new String[]{
                    "ID Asociación",
                    "ID Servicio",
                    "Servicio",
                    "ID Repuesto",
                    "Repuesto",
                    "Cantidad requerida"
                }
        ) {
            @Override
            public boolean isCellEditable(int fila, int columna) {
                return false;
            }
        };

        tablaServiciosRepuestos.setModel(modelo);
    }

    private void cargarServicios() {

        try {
            cmbServicio.removeAllItems();

            List<Servicio> servicios = servicioControlador.listar();

            for (Servicio servicio : servicios) {
                cmbServicio.addItem(
                        servicio.getIdServicio() + " - " + servicio.getNombre()
                );
            }

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this,
                    "Error al cargar los servicios: " + ex.getMessage());
        }
    }

    private void cargarRepuestos() {

        try {
            cmbRepuesto.removeAllItems();

            List<Repuesto> repuestos = repuestoControlador.listar();

            for (Repuesto repuesto : repuestos) {
                cmbRepuesto.addItem(
                        repuesto.getIdRepuesto() + " - " + repuesto.getNombre()
                );
            }

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this,
                    "Error al cargar los repuestos: " + ex.getMessage());
        }
    }

    private int obtenerIdSeleccionado(
            javax.swing.JComboBox<String> combo) {

        Object seleccionado = combo.getSelectedItem();

        if (seleccionado == null) {
            return -1;
        }

        try {
            String texto = seleccionado.toString();
            return Integer.parseInt(texto.split(" - ", 2)[0]);

        } catch (NumberFormatException ex) {
            return -1;
        }
    }

    private void cargarAsociaciones() {

        int idServicio = obtenerIdSeleccionado(cmbServicio);

        DefaultTableModel modelo =
                (DefaultTableModel) tablaServiciosRepuestos.getModel();

        modelo.setRowCount(0);

        if (idServicio <= 0) {
            return;
        }

        try {
            List<ServicioRepuesto> lista =
                    asociacionControlador.listarPorServicio(idServicio);

            for (ServicioRepuesto asociacion : lista) {

                Repuesto repuesto = repuestoControlador.buscarPorId(
                        asociacion.getIdRepuesto()
                );

                String nombreRepuesto = repuesto != null
                        ? repuesto.getNombre()
                        : "No encontrado";

                String nombreServicio =
                        cmbServicio.getSelectedItem().toString().split(" - ", 2)[1];

                modelo.addRow(new Object[]{
                    asociacion.getIdServicioRepuesto(),
                    asociacion.getIdServicio(),
                    nombreServicio,
                    asociacion.getIdRepuesto(),
                    nombreRepuesto,
                    asociacion.getCantidad()
                });
            }

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this,
                    "Error al cargar las asociaciones: " + ex.getMessage());
        }
    }

    private void guardarAsociacion() {

        int idServicio = obtenerIdSeleccionado(cmbServicio);
        int idRepuesto = obtenerIdSeleccionado(cmbRepuesto);

        if (idServicio <= 0) {
            JOptionPane.showMessageDialog(this,
                    "Seleccione un servicio.");
            return;
        }

        if (idRepuesto <= 0) {
            JOptionPane.showMessageDialog(this,
                    "Seleccione un repuesto.");
            return;
        }

        int cantidad;

        try {
            cantidad = Integer.parseInt(txtCantidad.getText().trim());

            if (cantidad <= 0) {
                JOptionPane.showMessageDialog(this,
                        "La cantidad debe ser mayor que cero.");
                return;
            }

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this,
                    "Ingrese una cantidad válida.");
            return;
        }

        try {
            ServicioRepuesto asociacion = new ServicioRepuesto();

            asociacion.setIdServicio(idServicio);
            asociacion.setIdRepuesto(idRepuesto);
            asociacion.setCantidad(cantidad);

            boolean guardado = asociacionControlador.guardar(asociacion);

            if (guardado) {
                JOptionPane.showMessageDialog(this,
                        "Repuesto asociado correctamente al servicio.");

                limpiarCampos();
                cargarAsociaciones();

            } else {
                JOptionPane.showMessageDialog(this,
                        "No se pudo guardar la asociación.");
            }

        } catch (SQLException | IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this,
                    ex.getMessage(),
                    "Error",
                    JOptionPane.WARNING_MESSAGE);
        }
    }

    private void actualizarTabla() {
        cargarServicios();
        cargarRepuestos();
        cargarAsociaciones();

        JOptionPane.showMessageDialog(this,
                "Información actualizada correctamente.");
    }

    private void limpiarCampos() {
        cmbRepuesto.setSelectedIndex(-1);
        txtCantidad.setText("");
    }


    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        btnGuardar = new javax.swing.JButton();
        jLabel6 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        btnActualizar = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        cmbServicio = new javax.swing.JComboBox<>();
        txtCantidad = new javax.swing.JTextField();
        cmbRepuesto = new javax.swing.JComboBox<>();
        jLabel3 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tablaServiciosRepuestos = new javax.swing.JTable();
        btnLimpiar = new javax.swing.JButton();
        btnRegresar2 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        btnGuardar.setBackground(new java.awt.Color(255, 255, 204));
        btnGuardar.setText("Guardar");
        btnGuardar.addActionListener(this::btnGuardarActionPerformed);

        jLabel6.setText("Cantidad requerida");

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel1.setText("ASIGNACIÓN DE REPUESTOS POR SERVICIO");

        btnActualizar.setBackground(new java.awt.Color(204, 255, 204));
        btnActualizar.setText("Actualizar");
        btnActualizar.addActionListener(this::btnActualizarActionPerformed);

        jLabel2.setText("Servicio");
        jLabel2.setToolTipText("");

        cmbServicio.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        cmbRepuesto.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jLabel3.setText("Repuesto");

        tablaServiciosRepuestos.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane1.setViewportView(tablaServiciosRepuestos);

        btnLimpiar.setBackground(new java.awt.Color(255, 204, 204));
        btnLimpiar.setText("Limpiar");
        btnLimpiar.addActionListener(this::btnLimpiarActionPerformed);

        btnRegresar2.setBackground(new java.awt.Color(204, 255, 255));
        btnRegresar2.setText("Menu");
        btnRegresar2.addActionListener(this::btnRegresar2ActionPerformed);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtCantidad, javax.swing.GroupLayout.PREFERRED_SIZE, 187, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cmbServicio, javax.swing.GroupLayout.PREFERRED_SIZE, 187, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2)
                    .addComponent(jLabel6))
                .addGap(87, 87, 87)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(cmbRepuesto, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addGap(74, 74, 74))
            .addGroup(layout.createSequentialGroup()
                .addGap(102, 102, 102)
                .addComponent(btnGuardar)
                .addGap(39, 39, 39)
                .addComponent(btnActualizar)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnLimpiar)
                .addGap(31, 31, 31)
                .addComponent(btnRegresar2)
                .addGap(66, 66, 66))
            .addGroup(layout.createSequentialGroup()
                .addGap(42, 42, 42)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 533, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel1)
                .addGap(113, 113, 113))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addComponent(jLabel1)
                .addGap(30, 30, 30)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jLabel3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cmbServicio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cmbRepuesto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(33, 33, 33)
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(txtCantidad, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(31, 31, 31)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnGuardar)
                    .addComponent(btnActualizar)
                    .addComponent(btnLimpiar)
                    .addComponent(btnRegresar2))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 408, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(14, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnGuardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGuardarActionPerformed
        guardarAsociacion();        
    }//GEN-LAST:event_btnGuardarActionPerformed

    private void btnActualizarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnActualizarActionPerformed
    actualizarTabla();        
    }//GEN-LAST:event_btnActualizarActionPerformed

    private void btnLimpiarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLimpiarActionPerformed
        limpiarCampos();
    }//GEN-LAST:event_btnLimpiarActionPerformed

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
        java.awt.EventQueue.invokeLater(() -> new FrmServiciosRepuestos().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnActualizar;
    private javax.swing.JButton btnGuardar;
    private javax.swing.JButton btnLimpiar;
    private javax.swing.JButton btnRegresar2;
    private javax.swing.JComboBox<String> cmbRepuesto;
    private javax.swing.JComboBox<String> cmbServicio;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tablaServiciosRepuestos;
    private javax.swing.JTextField txtCantidad;
    // End of variables declaration//GEN-END:variables
}
