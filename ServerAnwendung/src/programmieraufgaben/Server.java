package programmieraufgaben;

import java.net.ServerSocket;

/**
 * Die Server-Klasse enthält alle Methoden zum Erstellen, Verwenden und Schließen des Servers.
 *
 * Für die Lösung der Aufgabe müssen die Methoden execute, disconnect
 * und checkPort befüllt werden.
 * Es dürfen beliebig viele Methoden und Klassen erzeugt werden, solange
 * die von den oben genannten Methoden aufgerufen werden.
 */
public class Server{
    private int port;
    private ServerSocket serverSocket;


    public Server()
    {

    }

    /**
     * Diese Methode beinhaltet die gesamte Ausführung (Verbindungsaufbau und Beantwortung
     * der Client-Anfragen) des Servers.
     */
    public void execute() {

    }

    /**
     * Hier soll die Verbindung und alle Streams geschlossen werden.
     */
    public void disconnect() {

    }

    /**
     * Überprüfung der Port-Nummer und Speicherung dieser in die Klassen-Variable "port"
     * @param port Portnummer als String
     * @return Port-Nummer ist akzeptabel TRUE oder nicht FALSE
     */
    public boolean checkPort(String port) {
        if (!port.equals(2020))
        {
            System.out.println("Kein korrekter Port! Aktuell ist nur Port 2020 möFoglich.");
            return false;
        }
        this.port = 2020;
        return true;
    }

    /**
     * Gibt die akzeptierte und gespeicherte Port-Nummer zurück
     * @return
     */
    public int getPort() {
        return port;
    }

}
