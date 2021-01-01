package programmieraufgaben;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

/**
 * Die Server-Klasse enthält alle Methoden zum Erstellen, Verwenden und Schließen des Servers.
 *
 * Für die Lösung der Aufgabe müssen die Methoden connect, disconnect,
 * request und extract befüllt werden.
 * Es dürfen beliebig viele Methoden und Klassen erzeugt werden, solange
 * die von den oben genannten Methoden aufgerufen werden.
 */
public class Client {
    //Diese Variable gibt den Socket an an dem die Verbindung aufgabaut werden soll
    private Socket clientSocket;
    private String IPAddress;
    private int port;
    private boolean standby = true;
    private PrintWriter out = null;
    private BufferedReader in = null;

    public Client()
    {
        String buffer;
        Scanner input = new Scanner(System.in);
        System.out.println("Mit welchem Server wollen Sie sich verbinden?\n");
        System.out.print("IP-Adresse: ");
        IPAddress = input.nextLine();
        IPAddress = IPAddress.replace("localhost","127.0.0.1");
        if (!IPAddress.equals("127.0.0.1"))
        {
            System.out.println("\nFalsche IP-Adresse! Aktuell ist nur die IPv4-Adresse 127.0.0.1 und die Eingabe localhost möglich.\n");
            standby = false;
        }
        if (standby)
        {
            System.out.print("Port: ");
            buffer = input.nextLine();
            port = Integer.parseInt(buffer);
            if (port != 2020)
            {
                System.out.println("\nKein korrekter Port! Aktuell ist nur Port 2020 möglich.\n");
                standby = false;
            }
        }

    }

    /**
     * Hier werden die Verbindungsinformationen abgefragt und eine Verbindung eingerichtet.
     */
    public void connect() {
        if (!standby)
        {
            return;
        }
        clientSocket = new Socket();
        try
        {
            clientSocket.connect(new InetSocketAddress(IPAddress,port));
            out = new PrintWriter(new OutputStreamWriter(clientSocket.getOutputStream(), StandardCharsets.UTF_8),true);
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream(),StandardCharsets.UTF_8));
        } catch (IOException e)
        {
            e.printStackTrace();
        }




        //new InetSocketAddress(IPAdresse, port )
    }

    /**
     * Hier soll die Verbindung und alle Streams geschlossen werden.
     */
    public void disconnect() {

    }

    /**
     * In dieser Methode sollen die Eingaben des Benutzers an den Server gesendet und die Antwort empfangen werden
     * @param userInput Eingabe des Benutzers
     * @return Die vom Server empfangene Nachricht
     */
    public String request(String userInput) {

        return "";
    }

    /**
     * Die vom Server empfangene Nachricht soll hier für die Konsolenausgabe aufbereitet werden.
     * @param reply Die vom Server empfangene Nachricht
     * @return Ausgabe für die Konsole
     */
    public String extract(String reply) {

        return "";
    }

    /**
     * Gibt den Status der Verbindung an
     * @return Wenn die Verbindung aufgebaut ist: TRUE sonst FALSE
     */
    public boolean isConnected() {
        return (clientSocket != null && clientSocket.isConnected());
    }
}
