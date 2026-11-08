/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package vista;
import controlador.PresupuestoControlador;
import controlador.OrdenTrabajoControlador;
import modelo.Presupuesto;
import modelo.OrdenTrabajo;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

/**
 *
 * @author gesle
 */
public class FrmPresupuestos extends javax.swing.JFrame {
    private static final java.util.logging.Logger logger =
    java.util.logging.Logger.getLogger(
    FrmPresupuestos.class.getName());

    private final PresupuestoControlador controlador =
    new PresupuestoControlador();

    private final OrdenTrabajoControlador ordenControlador =
    new OrdenTrabajoControlador();

    private DefaultTableModel modeloTabla;

    private final DateTimeFormatter formatoFecha =
    DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    public FrmPresupuestos() {
        initComponents();
        txtPorcentajeImpuesto.setText("15");
        txtPorcentajeImpuesto.setEditable(true);
        
        txtSubtotal.setEditable(false);
        txtImpuesto.setEditable(false);
        txtTotal.setEditable(false);

        configurarCalculosAutomaticos();

        setLocationRelativeTo(null);

        txtIdPresupuesto.setEditable(false);
        txtFecha.setEditable(false);
        txtTotal.setEditable(false);

        modeloTabla = new DefaultTableModel(
                new Object[]{
                    "ID", "Orden", "Fecha", "Mano de obra",
                    "Repuestos", "Subtotal", "Descuento",
                    "Impuesto (%)", "Impuesto", "Total", "Estado", "Autorizado"
                }, 0
        ) {
            @Override
            public boolean isCellEditable(int fila, int columna) {
                return false;
            }
        };

        tablaPresupuestos.setModel(modeloTabla);

        cmbEstado.removeAllItems();
        cmbEstado.addItem("PENDIENTE");
        cmbEstado.addItem("APROBADO");
        cmbEstado.addItem("RECHAZADO");

        txtFecha.setText(
                LocalDateTime.now().format(formatoFecha)
        );

        cargarOrdenes();
        cargarPresupuestos();
        txtRepuestos.setEditable(false);

        cmbOrden.addActionListener(e -> cargarCostoRepuestos());
        tablaPresupuestos.getSelectionModel()
                .addListSelectionListener(e -> {
                    if (!e.getValueIsAdjusting()) {
                        seleccionarPresupuesto();
                    }
                });
    }

    

    private void cargarCostoRepuestos() {

    if (cmbOrden.getSelectedItem() == null) {
        return;
    }

    try {
        int idOrden = Integer.parseInt(
                cmbOrden.getSelectedItem().toString().split(" - ")[0]
        );

        double costo = controlador.calcularCostoRepuestosPorOrden(idOrden);

        txtRepuestos.setText(String.format("%.2f", costo));

    } catch (SQLException | IllegalArgumentException ex) {
        JOptionPane.showMessageDialog(this,
                "Error al calcular repuestos: " + ex.getMessage());
    }
}
private void configurarCalculosAutomaticos() {

    DocumentListener listener = new DocumentListener() {

        @Override
        public void insertUpdate(DocumentEvent e) {
            calcularPresupuesto();
        }

        @Override
        public void removeUpdate(DocumentEvent e) {
            calcularPresupuesto();
        }

        @Override
        public void changedUpdate(DocumentEvent e) {
            calcularPresupuesto();
        }
    };

    txtManoObra.getDocument().addDocumentListener(listener);
    txtRepuestos.getDocument().addDocumentListener(listener);
    txtDescuento.getDocument().addDocumentListener(listener);
    txtPorcentajeImpuesto.getDocument().addDocumentListener(listener);

    calcularPresupuesto();
}
    private void calcularPresupuesto() {

        try {
            double manoObra = obtenerValor(txtManoObra.getText());
            double repuestos = obtenerValor(txtRepuestos.getText());
            double descuento = obtenerValor(txtDescuento.getText());
            double porcentaje = obtenerValor(txtPorcentajeImpuesto.getText());

            double subtotal = manoObra + repuestos;

            if (porcentaje < 0 || porcentaje > 100 || descuento > subtotal) {
                txtSubtotal.setText(String.format("%.2f", subtotal));
                txtImpuesto.setText("0.00");
                txtTotal.setText("0.00");
                return;
            }

            double baseImponible = subtotal - descuento;
            double impuesto = baseImponible * (porcentaje / 100.0);
            double total = baseImponible + impuesto;

            txtSubtotal.setText(String.format("%.2f", subtotal));
            txtImpuesto.setText(String.format("%.2f", impuesto));
            txtTotal.setText(String.format("%.2f", total));

        } catch (NumberFormatException e) {
            txtSubtotal.setText("0.00");
            txtImpuesto.setText("0.00");
            txtTotal.setText("0.00");
        }
    }

    private double obtenerValor(String texto) {

        if (texto == null || texto.trim().isEmpty()) {
            return 0;
        }

        return Double.parseDouble(texto.trim().replace(",", ""));
    }
    private void cargarOrdenes() {

        try {
            cmbOrden.removeAllItems();

            List<OrdenTrabajo> lista = ordenControlador.listar();

            for (OrdenTrabajo orden : lista) {
                cmbOrden.addItem(
                        orden.getIdOrden() + " - " + orden.getEstado()
                );
            }

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this,
                    "Error al cargar órdenes: " + ex.getMessage());
        }
    }
    
    
    private void cargarPresupuestos() {

        try {
            modeloTabla.setRowCount(0);

            List<Presupuesto> lista = controlador.listar();

            for (Presupuesto p : lista) {

                modeloTabla.addRow(new Object[]{
                    p.getIdPresupuesto(),
                    p.getIdOrden(),
                    p.getFecha() != null
                    ? p.getFecha().format(formatoFecha) : "",
                    p.getManoObra(),
                    p.getRepuestos(),
                    p.getSubtotal(),
                    p.getDescuento(),
                    p.getPorcentajeImpuesto(),
                    p.getImpuesto(),
                    p.getTotal(),
                    p.getEstado(),
                    p.isAutorizado() ? "SÍ" : "NO"
                });
            }

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this,
                    "Error al cargar presupuestos: " + ex.getMessage());
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

        jScrollPane4 = new javax.swing.JScrollPane();
        txtTotal = new javax.swing.JTextPane();
        jLabel3 = new javax.swing.JLabel();
        jScrollPane9 = new javax.swing.JScrollPane();
        tablaPresupuestos = new javax.swing.JTable();
        jLabel5 = new javax.swing.JLabel();
        jScrollPane5 = new javax.swing.JScrollPane();
        txtManoObra = new javax.swing.JTextPane();
        jLabel7 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        txtIdPresupuesto = new javax.swing.JTextPane();
        jLabel6 = new javax.swing.JLabel();
        cmbOrden = new javax.swing.JComboBox<>();
        jLabel1 = new javax.swing.JLabel();
        btnLimpiar = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        btnGuardar = new javax.swing.JButton();
        jScrollPane3 = new javax.swing.JScrollPane();
        txtFecha = new javax.swing.JTextPane();
        jLabel2 = new javax.swing.JLabel();
        jScrollPane6 = new javax.swing.JScrollPane();
        txtRepuestos = new javax.swing.JTextPane();
        jLabel8 = new javax.swing.JLabel();
        cmbEstado = new javax.swing.JComboBox<>();
        btnActualizar = new javax.swing.JButton();
        jLabel9 = new javax.swing.JLabel();
        jScrollPane7 = new javax.swing.JScrollPane();
        txtSubtotal = new javax.swing.JTextPane();
        jScrollPane8 = new javax.swing.JScrollPane();
        txtDescuento = new javax.swing.JTextPane();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jScrollPane10 = new javax.swing.JScrollPane();
        txtImpuesto = new javax.swing.JTextPane();
        jLabel12 = new javax.swing.JLabel();
        jScrollPane11 = new javax.swing.JScrollPane();
        txtPorcentajeImpuesto = new javax.swing.JTextPane();
        btnRegresar2 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jScrollPane4.setViewportView(txtTotal);

        jLabel3.setText("ID Presupuesto");
        jLabel3.setToolTipText("");

        tablaPresupuestos.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane9.setViewportView(tablaPresupuestos);

        jLabel5.setText("Mano de obra");
        jLabel5.setToolTipText("");

        jScrollPane5.setViewportView(txtManoObra);

        jLabel7.setText("Total");
        jLabel7.setToolTipText("");

        jScrollPane2.setViewportView(txtIdPresupuesto);

        jLabel6.setText("Repuestos");

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel1.setText("PRESUPUESTOS");

        btnLimpiar.setBackground(new java.awt.Color(204, 255, 204));
        btnLimpiar.setText("Limpiar");
        btnLimpiar.addActionListener(this::btnLimpiarActionPerformed);

        jLabel4.setText("Fecha");

        btnGuardar.setBackground(new java.awt.Color(255, 255, 204));
        btnGuardar.setText("Guardar");
        btnGuardar.addActionListener(this::btnGuardarActionPerformed);

        jScrollPane3.setViewportView(txtFecha);

        jLabel2.setText("Orden de trabajo");

        jScrollPane6.setViewportView(txtRepuestos);

        jLabel8.setText("Estado");
        jLabel8.setToolTipText("");

        cmbEstado.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        btnActualizar.setBackground(new java.awt.Color(255, 204, 204));
        btnActualizar.setText("Actualizar");
        btnActualizar.addActionListener(this::btnActualizarActionPerformed);

        jLabel9.setText("SubTotal");

        jScrollPane7.setViewportView(txtSubtotal);

        jScrollPane8.setViewportView(txtDescuento);

        jLabel10.setText("Descuento");

        jLabel11.setText("Impuesto");

        jScrollPane10.setViewportView(txtImpuesto);

        jLabel12.setText("Impuesto %");

        jScrollPane11.setViewportView(txtPorcentajeImpuesto);

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
                    .addGroup(layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel7)
                            .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 225, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel8)
                            .addComponent(cmbEstado, javax.swing.GroupLayout.PREFERRED_SIZE, 225, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(12, 12, 12))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jLabel3)
                                    .addComponent(jLabel6)
                                    .addComponent(jLabel5)
                                    .addComponent(jLabel4)
                                    .addComponent(jLabel2)
                                    .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 225, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 225, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 225, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(cmbOrden, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 225, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jScrollPane6, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 225, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jLabel9)
                                    .addComponent(jScrollPane7, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 225, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jLabel10)
                                    .addComponent(jScrollPane8, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 225, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jLabel11)
                                    .addComponent(jScrollPane10, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 225, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jLabel12)
                                    .addComponent(jScrollPane11, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 225, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(btnLimpiar, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(btnGuardar, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(58, 58, 58)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(btnActualizar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(btnRegresar2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                            .addComponent(jLabel1))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addComponent(jScrollPane9, javax.swing.GroupLayout.PREFERRED_SIZE, 1426, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(15, 15, 15))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(37, 37, 37)
                        .addComponent(jLabel1)
                        .addGap(43, 43, 43)
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel2)
                        .addGap(7, 7, 7)
                        .addComponent(cmbOrden, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
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
                        .addGap(18, 18, 18)
                        .addComponent(jLabel10)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel12)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel11)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel7)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(12, 12, 12)
                        .addComponent(jLabel8)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(cmbEstado, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnGuardar)
                            .addComponent(btnActualizar)))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(23, 23, 23)
                        .addComponent(jScrollPane9, javax.swing.GroupLayout.PREFERRED_SIZE, 784, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(btnRegresar2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnLimpiar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(20, Short.MAX_VALUE))
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

            double manoObra = obtenerValor(txtManoObra.getText());
            double repuestos = obtenerValor(txtRepuestos.getText());
            double descuento = obtenerValor(txtDescuento.getText());
            double porcentaje = obtenerValor(txtPorcentajeImpuesto.getText());

            if (manoObra < 0 || repuestos < 0 || descuento < 0) {
                throw new IllegalArgumentException(
                        "Los importes no pueden ser negativos.");
            }

            if (porcentaje < 0 || porcentaje > 100) {
                throw new IllegalArgumentException(
                        "El impuesto debe estar entre 0 y 100 %.");
            }

            calcularPresupuesto();

            double subtotal = obtenerValor(txtSubtotal.getText());

            if (descuento > subtotal) {
                throw new IllegalArgumentException(
                        "El descuento no puede superar el subtotal.");
            }

            double impuesto = obtenerValor(txtImpuesto.getText());
            double total = obtenerValor(txtTotal.getText());

            int idOrden = Integer.parseInt(
                    cmbOrden.getSelectedItem().toString().split(" - ")[0]);

            Presupuesto p = new Presupuesto();

            p.setIdOrden(idOrden);
            p.setFecha(LocalDateTime.now());
            p.setManoObra(manoObra);
            p.setRepuestos(repuestos);
            p.setDescuento(descuento);
            p.setPorcentajeImpuesto(porcentaje);
            p.setSubtotal(subtotal);
            p.setImpuesto(impuesto);
            p.setTotal(total);

            if (controlador.guardar(p)) {

                JOptionPane.showMessageDialog(this,
                        "Presupuesto registrado correctamente.\n\n"
                        + "Subtotal: L " + String.format("%.2f", subtotal) + "\n"
                        + "Descuento: L " + String.format("%.2f", descuento) + "\n"
                        + "Impuesto (" + porcentaje + "%): L "
                        + String.format("%.2f", impuesto) + "\n"
                        + "Total: L " + String.format("%.2f", total));

                limpiarCampos();
                cargarPresupuestos();
            }

        } catch (NumberFormatException ex) {

            JOptionPane.showMessageDialog(this,
                    "Ingrese cantidades numéricas válidas.");

        } catch (SQLException | IllegalArgumentException ex) {

            JOptionPane.showMessageDialog(this,
                    "No se pudo guardar: " + ex.getMessage());
        }
    }//GEN-LAST:event_btnGuardarActionPerformed

    private void seleccionarPresupuesto() {

        int fila = tablaPresupuestos.getSelectedRow();

     if (fila < 0) {
         return;
     }

     try {

         int id = Integer.parseInt(
                 modeloTabla.getValueAt(fila, 0).toString());

         Presupuesto p = controlador.buscarPorId(id);

         if (p != null) {

             txtIdPresupuesto.setText(
                     String.valueOf(p.getIdPresupuesto()));

             txtFecha.setText(
                     p.getFecha() != null
                     ? p.getFecha().format(formatoFecha) : "");

             txtManoObra.setText(String.valueOf(p.getManoObra()));
             txtRepuestos.setText(String.valueOf(p.getRepuestos()));
             txtDescuento.setText(String.valueOf(p.getDescuento()));

             txtPorcentajeImpuesto.setText(
                     String.valueOf(p.getPorcentajeImpuesto()));

             txtSubtotal.setText(String.format("%.2f", p.getSubtotal()));
             txtImpuesto.setText(String.format("%.2f", p.getImpuesto()));
             txtTotal.setText(String.format("%.2f", p.getTotal()));

             seleccionarOrden(p.getIdOrden());
             txtRepuestos.setText(String.format("%.2f", p.getRepuestos()));

             cmbEstado.setSelectedItem(p.getEstado());
         }

     } catch (SQLException ex) {

         JOptionPane.showMessageDialog(this,
                 "Error al consultar presupuesto: " + ex.getMessage());
     }
    }

    private void seleccionarOrden(int idOrden) {

        for (int i = 0; i < cmbOrden.getItemCount(); i++) {

            String elemento = cmbOrden.getItemAt(i);

            if (elemento.startsWith(idOrden + " - ")) {
                cmbOrden.setSelectedIndex(i);
                break;
            }
        }
    }
    private void btnActualizarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnActualizarActionPerformed
        try {

           if (txtIdPresupuesto.getText().trim().isEmpty()) {
               JOptionPane.showMessageDialog(this,
                       "Seleccione un presupuesto de la tabla.");
               return;
           }

           int id = Integer.parseInt(txtIdPresupuesto.getText());

           String estado = cmbEstado.getSelectedItem().toString();

           boolean resultado;

           if (estado.equals("APROBADO")) {

               resultado = controlador.autorizar(id);

           } else if (estado.equals("RECHAZADO")) {

               resultado = controlador.rechazar(id);

           } else {

               Presupuesto p = controlador.buscarPorId(id);

               if (p == null) {
                   JOptionPane.showMessageDialog(this,
                           "No se encontró el presupuesto.");
                   return;
               }

               if (!p.getEstado().equals("PENDIENTE")) {
                   JOptionPane.showMessageDialog(this,
                           "Solo se pueden modificar presupuestos pendientes.");
                   return;
               }

               double manoObra = obtenerValor(txtManoObra.getText());
               double descuento = obtenerValor(txtDescuento.getText());
               double porcentaje = obtenerValor(txtPorcentajeImpuesto.getText());
               
               double subtotal = manoObra + p.getRepuestos();

                if (descuento > subtotal) {
                    throw new IllegalArgumentException(
                            "El descuento no puede superar el subtotal.");
                }

               if (manoObra < 0 || descuento < 0
                       || porcentaje < 0 || porcentaje > 100) {
                   throw new IllegalArgumentException(
                           "Verifique los importes y el porcentaje de impuesto.");
               }

               p.setManoObra(manoObra);
               p.setDescuento(descuento);
               p.setPorcentajeImpuesto(porcentaje);

               resultado = controlador.actualizar(p);
           }

           if (resultado) {

               JOptionPane.showMessageDialog(this,
                       "Presupuesto actualizado correctamente.");

               limpiarCampos();
               cargarPresupuestos();
           }

       } catch (NumberFormatException ex) {

           JOptionPane.showMessageDialog(this,
                   "Ingrese cantidades numéricas válidas.");

       } catch (SQLException | IllegalArgumentException ex) {

           JOptionPane.showMessageDialog(this,
                   "Error al actualizar: " + ex.getMessage());
       }
    }//GEN-LAST:event_btnActualizarActionPerformed

    private void btnRegresar2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRegresar2ActionPerformed
        FrmPrincipal principal = new FrmPrincipal();
        principal.setLocationRelativeTo(null);
        principal.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_btnRegresar2ActionPerformed
    private void limpiarCampos() {

        txtIdPresupuesto.setText("");

       txtFecha.setText(
               LocalDateTime.now().format(formatoFecha));

       txtManoObra.setText("");
       txtRepuestos.setText("");
       txtDescuento.setText("");
       txtPorcentajeImpuesto.setText("15");

       txtSubtotal.setText("0.00");
       txtImpuesto.setText("0.00");
       txtTotal.setText("0.00");

       if (cmbOrden.getItemCount() > 0) {
           cmbOrden.setSelectedIndex(0);
       }

       cmbEstado.setSelectedItem("PENDIENTE");

       tablaPresupuestos.clearSelection();
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
        java.awt.EventQueue.invokeLater(() -> new FrmPresupuestos().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnActualizar;
    private javax.swing.JButton btnGuardar;
    private javax.swing.JButton btnLimpiar;
    private javax.swing.JButton btnRegresar2;
    private javax.swing.JComboBox<String> cmbEstado;
    private javax.swing.JComboBox<String> cmbOrden;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
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
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JScrollPane jScrollPane8;
    private javax.swing.JScrollPane jScrollPane9;
    private javax.swing.JTable tablaPresupuestos;
    private javax.swing.JTextPane txtDescuento;
    private javax.swing.JTextPane txtFecha;
    private javax.swing.JTextPane txtIdPresupuesto;
    private javax.swing.JTextPane txtImpuesto;
    private javax.swing.JTextPane txtManoObra;
    private javax.swing.JTextPane txtPorcentajeImpuesto;
    private javax.swing.JTextPane txtRepuestos;
    private javax.swing.JTextPane txtSubtotal;
    private javax.swing.JTextPane txtTotal;
    // End of variables declaration//GEN-END:variables
}
