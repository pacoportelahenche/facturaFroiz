
package com.pacoportela.elco.facturafroiz;

import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import javax.swing.SwingWorker;
import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;

/**
 *
 * Esta clase extiende de la clase SwingWorker que nos permite ejecutar una
 * tarea de larga duracion en segundo plano. Actualizando la propiedad progress
 * podemos gestionar una barra de progreso que mostraremos al usuario para 
 * informarle del progreso de dicha tarea.
 * @author Paco Portela Henche. Marzo 2022.
 */
public class OrdenarWorker extends SwingWorker<Void, Void> 
        implements PropertyChangeListener{
    Interfaz interfaz;
    File factura;
    int contador = 0;
    boolean servidos = false;
    boolean ordenar = true;
    
    /**
     * Constructor.
     * @param in la interfaz donde presentaremos la barra de progreso.
     * @param fact el fichero que contiene la factura que vamos a ordenar.
     * @param ordenar un boolean que nos indica si queremos ordenar ó no el 
     * documento resultante.
     */
    public OrdenarWorker(Interfaz in, File fact, boolean ordenar){
        this.interfaz = in;
        this.factura = fact;
        this.ordenar = ordenar;
    }
    
    /*
    * Metodo que se ejecuta cuando se acaba la tarea pesada.
    */
    @Override
    public void done(){
        interfaz.getEtiquetaMensajes().setText("¡FACTURA PREPARADA!");
    }

    /*
    * Metodo que ejecuta la tarea en segundo plano. Permite actualizar la
    * propiedad progress, que podemos visualizar en la barra de progresos.
    * En este caso lo que hacemos aqui es recorrer el documento PDF pagina
    * por pagina y utilizando las capacidades de la clase ITesseract
    * convertimos las imagenes del pdf en texto legible. Obtenemos dicho texto,
    * lo vamos guardando linea por linea y finalmente lo ordenamos y creamos
    * un fichero que contiene dicho texto ordenado.
    */
    @Override
    protected Void doInBackground() throws Exception {
        // iniciamos el progreso.
        int progreso = 0;
        // actualizamos la propiedad progress.
        this.setProgress(progreso);
        // creamos la lista que contendra la lineas de texto.
        List<String> listaLineas = new ArrayList<>();
        // cargamos el documento pdf
        PDDocument document = PDDocument.load(factura); 
        PDFRenderer pdfRenderer = new PDFRenderer(document);
        // creamos el objeto ITesseract
        ITesseract tesseract = new Tesseract();
        // le pasamos el datapath y el lenguaje a usar
        tesseract.setDatapath("tessdata");
        tesseract.setLanguage("spa");
        // recorremos el documento pagina a pagina
        for (int page = 0; page < document.getNumberOfPages(); page++) {
            // calculamos el progreso para actualizar progress
            Double indice = Double.valueOf(page+1);
            Double total = Double.valueOf(document.getNumberOfPages());
            double avance = (indice / total) * 100;
            progreso = (int)Math.round(avance);
            // actualizamos progress
            this.setProgress(progreso);
            // obtenemos la imagen de la pagina del pdf
            BufferedImage bufferedImage = pdfRenderer.renderImageWithDPI
            (page, 300, ImageType.RGB);
            // usamos tesseract para reconocer los caracteres de la imagen
            try {
                // creamos un string que contiene toda la pagina
                String str = tesseract.doOCR(bufferedImage);
                // ponemos todos los caracteres en mayusculas
                str = str.toUpperCase();
                // creamos un array de strings separando todas las lineas
                String lineas[] = str.split("[\r\n]+");
                String nuevaLinea = "";
                // recorremos el array de strings
                for(int i = 0; i < lineas.length; i++){
                    
                    if(lineas[i].contains("SERVIDOS")){
                        // si llegamos a los articulos no servidos ponemos el
                        // boolean servidos a true y a partir de ese momento
                        // descartamos todas la demas lineas
                        servidos = true;
                        break;
                    }
                    // si la linea contiene alguna de estas palabras no la
                    // guardamos y continuamos con la siguiente
                    if(     servidos ||
                            lineas[i].contains("POIO") ||
                            lineas[i].contains("DISTRIBUCIONES") ||
                            lineas[i].contains("FECHA") ||
                            lineas[i].contains("TELE:") ||
                            lineas[i].contains("PEDIDO") || 
                            lineas[i].contains("SALDO") || 
                            lineas[i].contains("ALBARAN") || 
                            lineas[i].contains("ELCO") || 
                            lineas[i].contains("CADUCIDAD") || 
                            lineas[i].contains("LOTE") || 
                            lineas[i].contains("SUMA") || 
                            lineas[i].contains("WMW") ||
                            lineas[i].contains("W?W") ||
                            lineas[i].contains("W(WW") ||
                            lineas[i].contains("W?W") || 
                            lineas[i].contains("CORU") || 
                            lineas[i].contains("FROIZ") || 
                            lineas[i].contains("PONTEV") || 
                            lineas[i].contains("CODIGO") || 
                            lineas[i].contains("PRECIO") || 
                            lineas[i].contains("TELF") || 
                            lineas[i].contains("BALDOMIR") || 
                            lineas[i].contains("MAIL") || 
                            lineas[i].contains("FACTURA") || 
                            lineas[i].contains("FAC TURA") || 
                            lineas[i].contains("PROPUESTA") || 
                            lineas[i].contains("REALIZADO") || 
                            lineas[i].contains("PAGAR") || 
                            lineas[i].contains("IMPONIBLE") || 
                            lineas[i].contains("ARTICULO")) continue;
  // recorremos los caracteres de cada linea y descartamos los que no sean
  // letras de la A a la Z. Lo hacemos para quitar los numeros de linea.
                    for(int j = 0; j < lineas[i].length(); j++){
                        if(lineas[i].charAt(j) <='Z' && 
                           lineas[i].charAt(j) >= 'A'){
                            nuevaLinea = lineas[i].substring(j);
                            break;
                        }
                    }
                    // añadimos la linea a la lista de lineas
                    if(nuevaLinea.length() > 0) listaLineas.add(nuevaLinea);
                }
                
            } catch (TesseractException ex) {
                System.out.println(ex.toString());
            }
        }
        // ordenamos alfabeticamente 
        //la lista de lineas si es que las queremos ordenadas.
        if(ordenar) Collections.sort(listaLineas);
        
        // cerramos el documento
        document.close();
        // obtenemos la fecha del día para crear el nombre del fichero.
        Calendar fecha = Calendar.getInstance();
        String dia = Integer.toString(fecha.get(Calendar.DAY_OF_MONTH));
        if(dia.length() == 1) dia = "0" + dia;
        String mes = Integer.toString(fecha.get(Calendar.MONTH)+1);
        if(mes.length() == 1) mes = "0" + mes;
        String ano = Integer.toString(fecha.get(Calendar.YEAR));
        String nombreFichero= "";
        if(ordenar){
            nombreFichero = "ordenada_" + dia + mes + ano + ".txt";
        }
        else{
            nombreFichero = "noOrdenada_" + dia + mes + ano + ".txt";
        }
        
        
        // escribimos el fichero en el disco.
        Files.write(Paths.get(nombreFichero),
                listaLineas,
                Charset.forName("ISO-8859-1"),
                StandardOpenOption.CREATE,
                StandardOpenOption.TRUNCATE_EXISTING);
        return null;
    }

    /*
    * Método que se ejecuta cuando cuando cambia una propiedad, en nuestro
    * caso cuando cambiamos progress. Actualiza la barra de progreso y muestra
    * un texto al usuario.
    */
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if("progress".equals(evt.getPropertyName())){
            contador++;
            int progress = (Integer) evt.getNewValue();
            interfaz.getProgressBar().setValue(progress);
            interfaz.getEtiquetaMensajes()
                    .setText("ORDENANDO LA PÁGINA: " + contador);
        }
    }
    
}
