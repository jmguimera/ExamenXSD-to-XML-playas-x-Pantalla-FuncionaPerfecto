package xsdtoxml;
        
import generated.Playa;
import generated.Prediccion;
import generated.Dia;
import generated.TAgua;
import generated.Playa;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.datatype.XMLGregorianCalendar;

/**
 *
 * @author Jose Miguel Guimera Padron
 *
 */
public class tempXhora {

    /**
     * @param args the command line arguments
     * @throws java.net.MalformedURLException
     */

    public static void main(String[] args) throws MalformedURLException, IOException {
        Playa documento;
        String playa="";
        byte tempagua=0;

        // se captura por el teclado el municipio que se va a consultar
       // Scanner teclado = new Scanner(System.in);
       // System.out.print ("Indique el código del Municipio a consultar: ");
      //  municipio = teclado.nextLine();

        // una vez escrito el municipio y si no hubo error se recopila la informacion
        System.out.println("Consultando la Web de aemet.....\n");
    
        
        
        try {
            JAXBContext context = JAXBContext.newInstance(Playa.class);
            Unmarshaller convertidorDeXMLToObjecto = context.createUnmarshaller();    // partir del XML obtenemos el objeto.
            // el xml por horas es lo que se esta utilizando en este caso
            URL url = new URL("http://www.aemet.es/xml/playas/play_v2_3803806.xml");   
            URLConnection conexion= url.openConnection();     
 
            //documento contendrá los datos necesarios para hacer el informe pedido
            documento = (Playa) convertidorDeXMLToObjecto.unmarshal( conexion.getInputStream());      
         
         System.out.println("Playa: "+documento.getNombre()); //Se obtiene el nombre del muncipio en función de su código 
         
         tempagua=documento.getPrediccion().getDia().get(0).getTAgua().getValor1();
         List<Dia> dias= documento.getPrediccion().getDia(); // Se obtiene la lista de días que arroja el documento

         for ( int i=0; i <dias.size(); i++) { // recorrido por cada dia
            //Obtenemos la lista de temperatura por horas de cada dia
            List<Temperatura> temp = documento.getPrediccion().getDia().get(i).getTemperatura();
         
            // se crean las columnas con los datos fecha, hora y temperatura
            List<Columna> cols=new ArrayList<Columna>();
             // se rellena la lista con los datos extraidos de la web
            
             for (int j=0;j<temp.size();j++){
             // se crea un objeto que contendrá los datos fecha, hora y temp  
             Columna c=new Columna(dias.get(i).getFecha(),temp.get(j).getPeriodo(),temp.get(j).getValue());                
             // se añade el objeto a la lista
             cols.add(c);
            }
           
            // vamos a la rutina para realizar el reporte
            verDiaHoraTemp(cols);
         }
         
        } // fin del try
        
        catch (JAXBException ex) {  System.out.println("JAXBE : "+ex.getMessage()); }
        
        catch(FileNotFoundException ex) {System.out.println("Escriba  un codigo válido, por favor!\n"+"El informe "+ex.getMessage()+" no existe"); }

    } // fin del metodo main
    
    static void verDiaHoraTemp(List<Columna> cols){
    
       for (int i=0;i<cols.size();i++){
           // se captura la fecha para comparar luego en el bucle
           XMLGregorianCalendar fech=cols.get(i).getFecha();
           
           boolean ok=true; // variable bandera que indica dentro del bucle que la fecha ha cambiado
           boolean t=true; // variable bandera que indica cuando salir del bucle
           
           while (t){ // bucle sin fin, la ruptura es controlada dentro del bucle
               if(ok){ // si ok es true es la primera vez entonces....
                   System.out.println("Fecha: "+fech); // se imprime la fecha
                   System.out.println("   Hora  Temp"); ok=false; // se coloca ok a false
                   //en la proxima iteración no imprime nada hasta qye vuelva otra vez ok a true
                }

               System.out.print("    "+cols.get(i).getHora()); // imprime hora
               System.out.println("    "+cols.get(i).getTemp()+"\t"); // imprime temperatura
           
               i++; // suma 1 a i
               
               if(i==cols.size()){ break;} // controla que no se pase del ultimo dato, evita error de exception
               // si la fechas no son iguales termina el bucle while al colocar t iugal a false
               if(!(fech==cols.get(i).getFecha())) t=false;
            }
           
           System.out.println(""); // imprime una linea en blanco para la proxima fecha y sus datos
        } 

    
    }// fin del metodo verDiaHoraTemp
    
} // fin de la clase


// mi intencion era crear un formateo horizontal y no vertical
// mostrando columnas de horas bajo la fecha correspondiente
// en esta version lo he dejado asi por cuestión de tiempo.
//Para lograr lo antes dicho mi plan era crear listas de fecha
// listas de horas y listas de temperatura




 
     

    
