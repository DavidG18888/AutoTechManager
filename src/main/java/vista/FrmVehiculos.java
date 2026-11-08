/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package vista;
import controlador.VehiculoControlador;
import controlador.ClienteControlador;
import modelo.Vehiculo;
import modelo.Cliente;
import java.sql.SQLException;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author gesle
 */
public class FrmVehiculos extends javax.swing.JFrame {
   private final VehiculoControlador controlador = new VehiculoControlador();
   private final ClienteControlador clienteControlador = new ClienteControlador();
   private DefaultTableModel modeloTabla;   
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(FrmVehiculos.class.getName());

    /**
     * Creates new form FrmVehiculos
     */
    
    
    
    private void cargarClientes() {
        try {
            cmbCliente.removeAllItems();

            List<Cliente> clientes = clienteControlador.listar();

            if (clientes == null || clientes.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "No hay clientes registrados en la base de datos.");
                return;
            }

            int contador = 0;

            for (Cliente cliente : clientes) {

                String estado = cliente.getEstado();

                if (estado != null && estado.trim().equalsIgnoreCase("ACTIVO")) {

                    cmbCliente.addItem(
                            cliente.getIdCliente() + " - " + cliente.getNombre()
                    );

                    contador++;
                }
            }

            if (contador == 0) {
                JOptionPane.showMessageDialog(this,
                        "Se encontraron " + clientes.size()
                        + " clientes, pero ninguno tiene estado ACTIVO.");
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this,
                    "Error al consultar clientes: " + e.getMessage());
        }
    }

    private void cargarVehiculos() {
        try {
            modeloTabla.setRowCount(0);

            List<Vehiculo> vehiculos = controlador.listar();

            for (Vehiculo vehiculo : vehiculos) {
                Cliente cliente = clienteControlador.buscarPorId(
                        vehiculo.getIdCliente());

                String nombreCliente = cliente != null
                        ? cliente.getNombre() : "Sin cliente";

                modeloTabla.addRow(new Object[]{
                    vehiculo.getIdVehiculo(),
                    nombreCliente,
                    vehiculo.getPlaca(),
                    vehiculo.getVin(),
                    vehiculo.getMarca(),
                    vehiculo.getModelo(),
                    vehiculo.getAnio(),
                    vehiculo.getColor(),
                    vehiculo.getKilometraje(),
                    vehiculo.getCombustible()
                });
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this,
                    "Error al cargar vehículos: " + e.getMessage());
        }
    }

    private int obtenerIdCliente() {
        Object seleccionado = cmbCliente.getSelectedItem();

        if (seleccionado == null) {
            throw new IllegalArgumentException(
                    "Selecciona un cliente propietario.");
        }

        String texto = seleccionado.toString();
        return Integer.parseInt(texto.split(" - ")[0]);
    }

    private Vehiculo obtenerDatosFormulario() {
        Vehiculo vehiculo = new Vehiculo();

        vehiculo.setIdCliente(obtenerIdCliente());
        vehiculo.setPlaca(txtPlaca.getText().trim());
        vehiculo.setVin(txtVin.getText().trim());
        vehiculo.setMarca(txtMarca.getText().trim());
        vehiculo.setModelo(txtModelo.getText().trim());
        vehiculo.setAnio(Integer.parseInt(txtAnio.getText().trim()));
        vehiculo.setColor(jTextPane7.getText().trim());
        vehiculo.setKilometraje(
                Integer.parseInt(txtKilometraje.getText().trim()));
        vehiculo.setCombustible(
                cmbCombustible.getSelectedItem().toString());

        return vehiculo;
    }

    private void guardarVehiculo() {
        try {
            if (txtPlaca.getText().trim().isEmpty()
                    || txtVin.getText().trim().isEmpty()
                    || txtMarca.getText().trim().isEmpty()
                    || txtModelo.getText().trim().isEmpty()
                    || txtAnio.getText().trim().isEmpty()
                    || jTextPane7.getText().trim().isEmpty()
                    || txtKilometraje.getText().trim().isEmpty()) {

                JOptionPane.showMessageDialog(this,
                        "Completa todos los campos.");
                return;
            }

            Vehiculo vehiculo = obtenerDatosFormulario();

            if (vehiculo.getAnio() <= 0
                    || vehiculo.getKilometraje() < 0) {
                JOptionPane.showMessageDialog(this,
                        "Verifica el año y el kilometraje.");
                return;
            }

            if (controlador.guardar(vehiculo)) {
                JOptionPane.showMessageDialog(this,
                        "Vehículo registrado correctamente.");
                limpiarCampos();
                cargarVehiculos();
            } else {
                JOptionPane.showMessageDialog(this,
                        "No se pudo registrar el vehículo.");
            }

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this,
                    "El año y el kilometraje deben ser números válidos.");
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this,
                    "Error al guardar: " + e.getMessage());
        }
    }

    private void actualizarVehiculo() {
        try {
            if (txtIdVehiculo.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "Selecciona un vehículo de la tabla.");
                return;
            }

            Vehiculo vehiculo = obtenerDatosFormulario();

            vehiculo.setIdVehiculo(
                    Integer.parseInt(txtIdVehiculo.getText().trim()));

            if (controlador.actualizar(vehiculo)) {
                JOptionPane.showMessageDialog(this,
                        "Vehículo actualizado correctamente.");
                limpiarCampos();
                cargarVehiculos();
            } else {
                JOptionPane.showMessageDialog(this,
                        "No se pudo actualizar el vehículo.");
            }

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this,
                    "Verifica que el año y kilometraje sean números.");
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this,
                    "Error al actualizar: " + e.getMessage());
        }
    }

    private void seleccionarVehiculo() {
        int fila = jTable1.getSelectedRow();

        if (fila == -1) {
            return;
        }

        try {
            txtIdVehiculo.setText(
                    modeloTabla.getValueAt(fila, 0).toString()
            );

            int idCliente = controlador.buscarPorId(
                    Integer.parseInt(
                            modeloTabla.getValueAt(fila, 0).toString()
                    )
            ).getIdCliente();

            for (int i = 0; i < cmbCliente.getItemCount(); i++) {
                String elemento = cmbCliente.getItemAt(i);
                int id = Integer.parseInt(elemento.split(" - ")[0]);

                if (id == idCliente) {
                    cmbCliente.setSelectedIndex(i);
                    break;
                }
            }
            txtPlaca.setText(modeloTabla.getValueAt(fila, 2).toString());
            txtVin.setText(modeloTabla.getValueAt(fila, 3).toString());
            txtMarca.setText(modeloTabla.getValueAt(fila, 4).toString());
            txtModelo.setText(modeloTabla.getValueAt(fila, 5).toString());
            txtAnio.setText(modeloTabla.getValueAt(fila, 6).toString());
            jTextPane7.setText(modeloTabla.getValueAt(fila, 7).toString());
            txtKilometraje.setText(modeloTabla.getValueAt(fila, 8).toString());

            cmbCombustible.setSelectedItem(
                    modeloTabla.getValueAt(fila, 9).toString()
            );

        } catch (SQLException | NumberFormatException e) {
            JOptionPane.showMessageDialog(this,
                    "Error al seleccionar el vehículo: " + e.getMessage());
        }
    }

    private void limpiarCampos() {
        txtIdVehiculo.setText("");
        txtPlaca.setText("");
        txtVin.setText("");
        txtMarca.setText("");
        txtModelo.setText("");
        txtAnio.setText("");
        jTextPane7.setText("");
        txtKilometraje.setText("");

        if (cmbCliente.getItemCount() > 0) {
            cmbCliente.setSelectedIndex(0);
        }

        if (cmbCombustible.getItemCount() > 0) {
            cmbCombustible.setSelectedIndex(0);
        }

        jTable1.clearSelection();
    }
    

    public FrmVehiculos() {
        initComponents();

        setLocationRelativeTo(null);
        setTitle("Gestión de vehículos");

        txtIdVehiculo.setEditable(false);

        modeloTabla = new DefaultTableModel(
            new Object[]{"ID", "Cliente", "Placa", "VIN", "Marca",
                "Modelo", "Año", "Color", "Kilometraje", "Combustible"}, 0
        ) {
            @Override
            public boolean isCellEditable(int fila, int columna) {
                return false;
            }
        };

        jTable1.setModel(modeloTabla);

        jButton1.addActionListener(e -> guardarVehiculo());
        jButton2.addActionListener(e -> actualizarVehiculo());
        jButton3.addActionListener(e -> limpiarCampos());

        jTable1.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && jTable1.getSelectedRow() != -1) {
                seleccionarVehiculo();
            }
        });

        cargarClientes();
        cargarVehiculos();
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
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        txtIdVehiculo = new javax.swing.JTextPane();
        jScrollPane2 = new javax.swing.JScrollPane();
        txtPlaca = new javax.swing.JTextPane();
        jScrollPane3 = new javax.swing.JScrollPane();
        txtModelo = new javax.swing.JTextPane();
        jScrollPane4 = new javax.swing.JScrollPane();
        txtVin = new javax.swing.JTextPane();
        jScrollPane5 = new javax.swing.JScrollPane();
        txtMarca = new javax.swing.JTextPane();
        jScrollPane6 = new javax.swing.JScrollPane();
        txtAnio = new javax.swing.JTextPane();
        txtColor = new javax.swing.JScrollPane();
        jTextPane7 = new javax.swing.JTextPane();
        jScrollPane8 = new javax.swing.JScrollPane();
        txtKilometraje = new javax.swing.JTextPane();
        cmbCombustible = new javax.swing.JComboBox<>();
        cmbCliente = new javax.swing.JComboBox<>();
        jScrollPane9 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        btnRegresar2 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel1.setText("GESTIÓN DE VEHÍCULOS");

        jLabel2.setText("ID del vehículo");

        jLabel3.setText("Placa");

        jLabel4.setText("Cliente propietario");

        jLabel5.setText("VIN / Chasis");

        jLabel6.setText("Marca");

        jLabel7.setText("Modelo");

        jLabel8.setText("Color");

        jLabel9.setText("Año");

        jLabel10.setText("Kilometraje");
        jLabel10.setToolTipText("");

        jLabel11.setText("Combustible");
        jLabel11.setToolTipText("");

        jScrollPane1.setViewportView(txtIdVehiculo);

        jScrollPane2.setViewportView(txtPlaca);

        jScrollPane3.setViewportView(txtModelo);

        jScrollPane4.setViewportView(txtVin);

        jScrollPane5.setViewportView(txtMarca);

        jScrollPane6.setViewportView(txtAnio);

        txtColor.setViewportView(jTextPane7);

        jScrollPane8.setViewportView(txtKilometraje);

        cmbCombustible.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "GASOLINA", "DIESEL", "HIBRIDO", "ELECTRICO" }));

        cmbCliente.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane9.setViewportView(jTable1);

        jButton1.setBackground(new java.awt.Color(255, 255, 204));
        jButton1.setText("Guardar");

        jButton2.setBackground(new java.awt.Color(255, 204, 204));
        jButton2.setText("Actualizar");
        jButton2.setToolTipText("");

        jButton3.setBackground(new java.awt.Color(204, 255, 204));
        jButton3.setText("Limpiar");

        btnRegresar2.setBackground(new java.awt.Color(204, 255, 255));
        btnRegresar2.setText("Menu");
        btnRegresar2.addActionListener(this::btnRegresar2ActionPerformed);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(49, 49, 49)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel11)
                    .addComponent(jLabel10)
                    .addComponent(jLabel9)
                    .addComponent(jLabel8)
                    .addComponent(jLabel7)
                    .addComponent(jLabel6)
                    .addComponent(jLabel5)
                    .addComponent(jLabel4)
                    .addComponent(jLabel3)
                    .addComponent(jLabel2)
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane1)
                    .addComponent(jScrollPane2)
                    .addComponent(jScrollPane3)
                    .addComponent(jScrollPane4)
                    .addComponent(jScrollPane5)
                    .addComponent(jScrollPane6)
                    .addComponent(txtColor)
                    .addComponent(jScrollPane8)
                    .addComponent(cmbCombustible, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(cmbCliente, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jButton1)
                            .addComponent(jButton3))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(btnRegresar2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jButton2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addGap(65, 65, 65)
                .addComponent(jScrollPane9, javax.swing.GroupLayout.PREFERRED_SIZE, 1223, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(27, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(39, 39, 39)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addGap(31, 31, 31)
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel4)
                        .addGap(13, 13, 13)
                        .addComponent(cmbCliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(24, 24, 24)
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel5)
                        .addGap(16, 16, 16)
                        .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel6)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel7)
                        .addGap(18, 18, 18)
                        .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel9)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel8)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtColor, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel10)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jScrollPane8, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel11)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(cmbCombustible, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jButton1)
                            .addComponent(jButton2))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jButton3)
                            .addComponent(btnRegresar2)))
                    .addComponent(jScrollPane9))
                .addContainerGap(28, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

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
        java.awt.EventQueue.invokeLater(() -> new FrmVehiculos().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnRegresar2;
    private javax.swing.JComboBox<String> cmbCliente;
    private javax.swing.JComboBox<String> cmbCombustible;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JScrollPane jScrollPane8;
    private javax.swing.JScrollPane jScrollPane9;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextPane jTextPane7;
    private javax.swing.JTextPane txtAnio;
    private javax.swing.JScrollPane txtColor;
    private javax.swing.JTextPane txtIdVehiculo;
    private javax.swing.JTextPane txtKilometraje;
    private javax.swing.JTextPane txtMarca;
    private javax.swing.JTextPane txtModelo;
    private javax.swing.JTextPane txtPlaca;
    private javax.swing.JTextPane txtVin;
    // End of variables declaration//GEN-END:variables
}
