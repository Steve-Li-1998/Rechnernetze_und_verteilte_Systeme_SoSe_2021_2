package programmieraufgaben;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * Die Server-Klasse enthält alle Methoden zum Erstellen, Verwenden und Schließen des Servers.
 * <p>
 * Für die Lösung der Aufgabe müssen die Methoden execute, disconnect
 * und checkPort befüllt werden.
 * Es dürfen beliebig viele Methoden und Klassen erzeugt werden, solange
 * die von den oben genannten Methoden aufgerufen werden.
 */
public class Server {
    private int port;
    private ServerSocket serverSocket;
    private PrintWriter out = null;
    private BufferedReader in = null;
    private Socket client = null;
    private boolean continueExecute = true;
    private List<String> historyCommand = new LinkedList<>();
    
    
    public Server() {
        try {
            serverSocket = new ServerSocket();
        } catch (IOException e) {
            System.out.println("Feler! \"serverSocket\" kann nicht erzeugt werden!");
        }
    }
    
    /**
     * Diese Methode beinhaltet die gesamte Ausführung (Verbindungsaufbau und Beantwortung
     * der Client-Anfragen) des Servers.
     */
    public void execute() {
        while (continueExecute) {
            String buffer = null;
            try {
                client = serverSocket.accept();
                out = new PrintWriter(new OutputStreamWriter(client.getOutputStream(), StandardCharsets.UTF_8), true);
                in = new BufferedReader(new InputStreamReader(client.getInputStream(), StandardCharsets.UTF_8));
                try {
                    while (!client.isClosed()) {
                        buffer = in.readLine();
                        response(buffer);
                    }
                } catch (SocketException e) {
                    System.out.println("Connection reset");
                }
            } catch (IOException e) {
            
            }
            if (null != client) {
                closeClient();
            }
        }
        
    }
    
    /**
     * Hier wird der Server die Kommando bearbeiten
     *
     * @param command ist die Kommando (als einzel String) von Client
     */
    private void response(String command) {
        if (command != null) {
            command = command.trim();
            String[] cmd = null;
            cmd = command.split("\\s+", 4);
            if (1 == cmd.length & cmd[0].equals("EXIT")) {
                closeClient();
            }
            switch (cmd[0]) {
                // Zeit und Datum kommando
                case "GET" -> cmdDate(cmd);
                // Arithmetisches Kommando
                case "ADD" -> cmdArith(cmd);
                case "SUB" -> cmdArith(cmd);
                case "MUL" -> cmdArith(cmd);
                case "DIV" -> cmdArith(cmd);
                // ECHO Kommando
                case "ECHO" -> {
                    cmd = command.split("\\s+", 2);
                    out.println("ECHO " + cmd[1]);
                    historyCommand.add("ECHO " + cmd[1]);
                }
                // Keine Antwort, da der Discard-Dienst alle empfangenen Daten verwirft.
                case "DISCARD" -> out.println();
                // PING Kommando
                case "PING" -> cmdPing(cmd);
                // HISTORIE Kommando
                case "HISTORY" -> cmdHistory(cmd);
                // Sonst: Unbekannte Anfrage!
                default -> out.println("ERROR Unbekannte Anfrage!");
            }
        }
    }
    
    /**
     * Hier wird die Verbindung von Client getrennt, und Historien gelöscht
     */
    private void closeClient() {
        try {
            if (null != client) {
                client.close();
            }
            historyCommand.clear();        // Die Historien löschen, nur nach der Trennung der Verbindung
        } catch (IOException e) {
            System.out.println("Die Verbindung ist fehlgeschlagen!");
            e.printStackTrace();
        }
    }
    
    private void cmdDate(String[] cmd) {
        if (2 != cmd.length) {
            out.println("ERROR Falsches Format!");
            return;
        }
        Date date = new Date();
        if (cmd[1].equals("Time")) {
            historyCommand.add("GET Time");
            String strDateFormat = "HH:mm:ss";
            SimpleDateFormat sdf = new SimpleDateFormat(strDateFormat);
            out.println("TIME " + sdf.format(date));
        }
        else if (cmd[1].equals("Date")) {
            historyCommand.add("GET Date");
            String strDateFormat = "dd.MM.yyyy";
            SimpleDateFormat sdf = new SimpleDateFormat(strDateFormat);
            out.println("DATE " + sdf.format(date));
        }
        else {
            out.println("ERROR Unbekannte Anfrage!");
            // Unbekannte Anfrage!
        }
    }
    
    private void cmdArith(String[] cmd) {
        if (3 != cmd.length) {
            out.println("ERROR Falsches Format!");
            return;
        }
        
        //Falls Buchstaben enthält
        if (cmd[1].matches(".*\\D+.*") | cmd[2].matches(".*\\D+.*")) {
            out.println("ERROR Falsches Format!");
            return;
        }
        int var1, var2;
        try {
            var1 = Integer.parseInt(cmd[1]);
            var2 = Integer.parseInt(cmd[2]);
        } catch (NumberFormatException e) {
            out.println("ARITH_WARNING Die Parametern muss Integern sein!");
            historyCommand.add(cmd[0] + " " + cmd[1] + " " + cmd[2]);
            return;
        }
        switch (cmd[0]) {
            case "ADD": {
                int result = var1 + var2;
                out.println("SUM " + result);
                break;
            }
            case "SUB": {
                int result = var1 - var2;
                out.println("DIFFERENCE " + result);
                break;
            }
            case "MUL": {
                int result = var1 * var2;
                out.println("PRODUCT " + result);
                break;
            }
            case "DIV":
                if (0 == var2) {
                    out.println("QUOTIENT undefined");
                }
                else {
                    double result = (double) var1 / (double) var2;
                    out.println("QUOTIENT " + result);
                }
                break;
            default:
                out.println("ERROR Unbekannte Anfrage!");
                // Unbekannte Anfrage!
                break;
        }
        historyCommand.add(cmd[0] + " " + cmd[1] + " " + cmd[2]);
        
    }
    
    private void cmdPing(String[] cmd) {
        if (1 != cmd.length) {
            out.println("ERROR Falsches Format!");
            return;
        }
        out.println("PONG");
        historyCommand.add("PING");
    }
    
    private void cmdHistory(String[] cmd) {
        if (cmd.length > 2) {
            out.println("ERROR Falsches Format!");
            return;
        }
        // Alle Historie
        if (1 == cmd.length) {
            sendHistory();
        }
        // letzte <Integer> Historie
        else {
            // Der Parameter der hietorie soll einen Integer sein
            if (cmd[1].matches(".*\\D+.*")) {
                out.println("ERROR Falsches Format!");
            }
            else {
                int counter;
                try {
                    counter = Integer.parseInt(cmd[1]);
                } catch (NumberFormatException e) {
                    out.println("HISTORY_WARNING Die Parametern muss Integern sein!");
                    historyCommand.add(cmd[0] + " " + cmd[1]);
                    return;
                }
                sendHistory(counter);
            }
        }
    }
    
    /**
     * Hier soll die Historien gesendet werden
     *
     * @param limit ist die Beschränkung der Anzahl der gesendet Historien, für limit = -1 gibt es kein Anzahl beschränkung
     */
    private void sendHistory(int limit) {
        if (0 == historyCommand.size()) {
            out.println("ERROR Keine Historie vorhanden!");
            return;
        }
        int limit_copy = limit;
        if (limit > historyCommand.size() | -1 == limit) {
            limit = historyCommand.size();
        }
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = historyCommand.size() - 1; i >= historyCommand.size() - limit; i--) {
            stringBuilder.append(historyCommand.get(i));
            if (i != historyCommand.size() - limit) {
                stringBuilder.append("\\n");
            }
        }
        // "HISTORY <Sttring>" ist das Antwortformat
        out.println("HISTORY " + stringBuilder.toString());
        if (-1 == limit_copy) {
            historyCommand.add("HISTORY");
        }
        else {
            historyCommand.add("HISTORY " + limit);
        }
    }
    
    /**
     * Hier soll alle Historien gesendet
     */
    private void sendHistory() {
        sendHistory(-1);
    }
    
    /**
     * Hier soll die Verbindung und alle Streams geschlossen werden.
     */
    public void disconnect() {
        continueExecute = false;
        try {
            if (null != out) {
                out.close();
            }
            if (null != in) {
                in.close();
            }
            if (null != client) {
                closeClient();
            }
            if (null != serverSocket) {
                serverSocket.close();
            }
            
            
        } catch (IOException e) {
            System.out.println("Feler! \"serverSocket\" kann nicht abgeschlossen werden!");
        }
        System.out.println();
    }
    
    /**
     * Überprüfung der Port-Nummer und Speicherung dieser in die Klassen-Variable "port"
     *
     * @param port Portnummer als String
     * @return Port-Nummer ist akzeptabel TRUE oder nicht FALSE
     */
    public boolean checkPort(String port) {
        if (!port.equals("2020")) {
            System.out.println("Kein korrekter Port! Aktuell ist nur Port 2020 möglich.");
            return false;
        }
        this.port = 2020;
        try {
            serverSocket.bind(new InetSocketAddress(this.port));
        } catch (IOException e) {
            System.out.println("Fehler! \"serverSocket\" kann nicht mit Port gebunden werden!");
        }
        return true;
    }
    
    /**
     * Gibt die akzeptierte und gespeicherte Port-Nummer zurück
     *
     * @return ist aktuelle Port
     */
    public int getPort() {
        return port;
    }
    
}