package com.pacoportela.elco.facturafroiz;


import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;

/**
 *
 * @author Paco Portela Henche. Marzo 2022.
 */
public class Interfaz extends javax.swing.JFrame {
    /**
     * Creates new form Interfaz
     */
    public Interfaz() {
        initComponents();
        ponerIcono();
        //sacarAviso();
}
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jProgressBar1 = new javax.swing.JProgressBar();
        etiquetaMensajes = new javax.swing.JLabel();
        jMenuBar1 = new javax.swing.JMenuBar();
        menuFichero = new javax.swing.JMenu();
        menuItemSalir = new javax.swing.JMenuItem();
        menuBuscar = new javax.swing.JMenu();
        menuItemOrdenar = new javax.swing.JMenuItem();
        menuSeparar = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jProgressBar1.setStringPainted(true);

        etiquetaMensajes.setText("PULSE BUSCAR Y DESPUES ORDENAR PARA BUSCAR LA FACTURA A ORDENAR");

        menuFichero.setText("Fichero");

        menuItemSalir.setText("Salir");
        menuItemSalir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuItemSalirActionPerformed(evt);
            }
        });
        menuFichero.add(menuItemSalir);

        jMenuBar1.add(menuFichero);

        menuBuscar.setText("Buscar");

        menuItemOrdenar.setText("Ordenar");
        menuItemOrdenar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuItemOrdenarActionPerformed(evt);
            }
        });
        menuBuscar.add(menuItemOrdenar);

        menuSeparar.setText("Paginar");
        menuSeparar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuSepararActionPerformed(evt);
            }
        });
        menuBuscar.add(menuSeparar);

        jMenuBar1.add(menuBuscar);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(etiquetaMensajes, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(layout.createSequentialGroup()
                .addGap(64, 64, 64)
                .addComponent(jProgressBar1, javax.swing.GroupLayout.PREFERRED_SIZE, 259, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(etiquetaMensajes, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jProgressBar1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(30, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void menuItemOrdenarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuItemOrdenarActionPerformed
        File factura = buscarFactura();
        if(factura != null && factura.getName().contains("pdf")){
            this.etiquetaMensajes.setText("ESPERE POR FAVOR...");
            OrdenarWorker ordenar = new OrdenarWorker(this, factura);
            ordenar.addPropertyChangeListener(ordenar);
            ordenar.execute();
        }
        
    }//GEN-LAST:event_menuItemOrdenarActionPerformed

    private void menuItemSalirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuItemSalirActionPerformed
        System.exit(0);
    }//GEN-LAST:event_menuItemSalirActionPerformed

    private void menuSepararActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuSepararActionPerformed
        File factura = buscarFactura();
        paginarFactura(factura);
    }//GEN-LAST:event_menuSepararActionPerformed

   private File buscarFactura(){
       File factura = null;
       JFileChooser buscador = new JFileChooser("C:/Users/PACO/Downloads");
       if (buscador.showOpenDialog(this.getContentPane())
                == JFileChooser.APPROVE_OPTION) {
            factura = buscador.getSelectedFile();
        }
       if(factura == null || !factura.getName().contains("pdf")){
                JOptionPane.showMessageDialog(this, "Debe ser un archivo pdf",
                        "Aviso", JOptionPane.INFORMATION_MESSAGE);
        }
        return factura;
   }
   
   private void ponerIcono(){
    URL url = Interfaz.class.getResource("/froiz.jpeg");
        if(url != null){
            ImageIcon icono = new ImageIcon(url);
            this.setIconImage(icono.getImage());
        }
    }
   
   private JLabel crearEtiquetaAviso(){
       URL url = Interfaz.class.getResource("/cabeza.png");
       ImageIcon imagen = null;
       if(url != null){
           imagen = new ImageIcon(url);
       }
       
       JLabel eti = new JLabel("¡¡¡QUE BURRITA ERES!!!", imagen, JLabel.CENTER);
       eti.setVerticalTextPosition(JLabel.BOTTOM);
       eti.setHorizontalTextPosition(JLabel.CENTER);
       return eti;
   }
   
   private void sacarAviso(){
       int opcion = JOptionPane.showConfirmDialog(this,
               "¿Te acuerdas como era Bianca?",
               "Recordatorio", JOptionPane.YES_NO_OPTION,
               JOptionPane.INFORMATION_MESSAGE);
       if(opcion == JOptionPane.YES_OPTION){
           JOptionPane.showMessageDialog(this,
                   "¡MUY BIEN BIANCA, ASI ME GUSTA!");
       }
       else{
           JOptionPane.showMessageDialog(this,
                   this.crearEtiquetaAviso());
       }
   }
   
   private void paginarFactura(File f){
       if(f != null && f.getName().contains("pdf")){
           PaginarFactura paginar = new PaginarFactura();
           try {
               paginar.paginarDocumento(f);
            } catch (IOException ex) {
               Logger.getLogger(Interfaz.class.getName())
                       .log(Level.SEVERE, null, ex);
           }
       }
       
   }
   
    /**
     *
     * @return
     */
    public Interfaz getInterfaz(){
       return this;
   }
   
    /**
     *
     * @return
     */
    public JLabel getEtiquetaMensajes(){
       return this.etiquetaMensajes;
   }
   
    /**
     *
     * @return
     */
    public JProgressBar getProgressBar(){
       return this.jProgressBar1;
   }
   
   

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel etiquetaMensajes;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JProgressBar jProgressBar1;
    private javax.swing.JMenu menuBuscar;
    private javax.swing.JMenu menuFichero;
    private javax.swing.JMenuItem menuItemOrdenar;
    private javax.swing.JMenuItem menuItemSalir;
    private javax.swing.JMenuItem menuSeparar;
    // End of variables declaration//GEN-END:variables
}