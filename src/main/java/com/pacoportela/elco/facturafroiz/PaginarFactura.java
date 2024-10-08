package com.pacoportela.elco.facturafroiz;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import org.apache.pdfbox.multipdf.Splitter;
import org.apache.pdfbox.pdmodel.PDDocument;
/**
 * Esta clase nos permite separar en p�ginas individuales un documento PDF
 * que contenga varias p�ginas.
 * @author Paco Portela Henche. Marzo 2022.
 */
public class PaginarFactura {

    /**
     * Constructor.
     */
    public PaginarFactura(){
        
    }
    
    /**
     * Este metodo separa en paginas un documento PDF.
     * @param factura Un objeto File que contiene la factura a paginar.
     * @throws IOException Lanza una excepcion de entrada/salida.
     */
    public void paginarDocumento(File factura) throws IOException{
        PDDocument document = PDDocument.load(factura);
        // instantiating Splitter
          Splitter splitter = new Splitter();
           
          // split the pages of a PDF document
          List<PDDocument> paginas = splitter.split(document);
 
          // Creating an iterator 
          Iterator<PDDocument> iterator = paginas.listIterator();
 
          // saving splits as pdf
          int i = 0;
          while(iterator.hasNext()) {
             PDDocument pd = iterator.next();
             pd.save("pagina_" + ++i + ".pdf");
             System.out.println("Grabado el fichero pagina_"+ i +".pdf");
          }
          System.out.println("El fichero pdf proporcionado a sido separado en "
                  + i + "paginas.");
          document.close();

    }
}
