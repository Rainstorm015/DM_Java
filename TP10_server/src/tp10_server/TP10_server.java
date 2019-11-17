/**
 * Project : TP Java 10
 * @author Adrien Legrand <legrand.adrien96@gmail.com>
 * Organisation : ISEN Lille AP4
 */
package tp10_server;

import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;



/**
 * Cette classe instancie un server qui écoutera les commandes envoyées par les 
 * client.
 */
public class TP10_server {
    private PrintWriter out;
    private BufferedReader in;
    private College c;

    /**
     * Le constructeur initialise un collège et lance une méthode qui démarre le
     * serveur
     */
    public TP10_server() {
        c = new College();
        startServer();
    }
    
    /**
     * Méthode qui démarre et ferme en boucle un ServerSocket. Une fois démarré,
     * le serveur écoute la commande client.
     */
    public void startServer(){
        ServerSocket server = null;
        Socket socket = null;
        BufferedReader in;
        String input;
        
        try {
            while (true) {
                try {
                    server = new ServerSocket(8080);
                } catch (IOException e) {
                    e.printStackTrace();
                    break;
                }
                socket = server.accept();
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                input = in.readLine();
                if(input != null){
                    System.out.println(input);
                    out = new PrintWriter(socket.getOutputStream(),true);
                    out.println(c.extractDataForClient(input));
                }
                try {
                    server.close();
                } catch (IOException ignored) {}
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if(server != null && !server.isClosed()) {
                    server.close();
                }
            } catch (IOException ignored) {}
        }
    }
    
    
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        new TP10_server();
    }

}
