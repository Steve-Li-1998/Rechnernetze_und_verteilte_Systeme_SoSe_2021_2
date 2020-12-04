package programmieraufgaben;

import java.net.Socket;

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

    /**
     * Hier werden die Verbindungsinformationen abgefragt und eine Verbindung eingerichtet.
     */
    public void connect() {

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
