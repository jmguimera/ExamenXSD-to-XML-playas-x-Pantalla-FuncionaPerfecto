package xsdtoxml;
        
import generated.Playa;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;


public class tempAguaXPlaya {

 
    public static void main(String[] args) throws MalformedURLException, IOException {
        Playa documento;
        String playa[] = new String[3];        
        byte tempagua=0;
        
        System.out.println("Consultando la Web de aemet.....\n");
    
        playa[0]="3803806";
        playa[1]="3804302";
        playa[2]="3800116";
        
        try {
          for (int i=0;i<3;i++ ){   
            JAXBContext context = JAXBContext.newInstance(Playa.class);
            Unmarshaller convertidorDeXMLToObjecto = context.createUnmarshaller();  // partir del XML obtenemos el objeto.

            URL url = new URL("http://www.aemet.es/xml/playas/play_v2_"+playa[i]+".xml");   
            URLConnection conexion= url.openConnection();     
 
            documento = (Playa) convertidorDeXMLToObjecto.unmarshal( conexion.getInputStream());      
         
         String fecha=documento.getPrediccion().getDia().get(0).getFecha();
         System.out.println("Fecha: "+fecha);             
         System.out.println("Playa: "+documento.getNombre()); //Se obtiene el nombre del muncipio en función de su código 

         tempagua=documento.getPrediccion().getDia().get(0).getTAgua().getValor1();

         
         System.out.println("Temperatura del agua: "+tempagua);
         
         String edocieloam=documento.getPrediccion().getDia().get(0).getEstadoCielo().getDescripcion1();
         String edocielopm=documento.getPrediccion().getDia().get(0).getEstadoCielo().getDescripcion2();         
        
        System.out.println("Estado del cielo en la mañana: "+edocieloam);
        System.out.println("Estado del cielo en la  tarde: "+edocielopm+"\n");

         
          }
        
        
        } // fin del try
        
        catch (JAXBException ex) {  System.out.println("JAXBE : "+ex.getMessage()); }
        
        catch(FileNotFoundException ex) {System.out.println("Escriba  un codigo válido, por favor!\n"+"El informe "+ex.getMessage()+" no existe"); }

    } // fin del metodo main

} // fin de la clase



    
