package programmieraufgaben;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

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
    private PrintWriter out = null;
    private BufferedReader in = null;
    private Socket client = null;
    private boolean continueExecute = true;
    private List<String> historyCommand = new LinkedList<>();


    public Server()
    {
        try
        {
            serverSocket = new ServerSocket();
        } catch (IOException e)
        {
            System.out.println("Feler! \"serverSocket\" kann nicht erzeugt werden!");
        }
    }

    /**
     * Diese Methode beinhaltet die gesamte Ausführung (Verbindungsaufbau und Beantwortung
     * der Client-Anfragen) des Servers.
     */
    public void execute() {
        //boolean status = true;
        while (continueExecute)
        {
            String buffer = null;
            try
            {
                client = serverSocket.accept();
                out = new PrintWriter(new OutputStreamWriter(client.getOutputStream(), StandardCharsets.UTF_8),true);
                in = new BufferedReader(new InputStreamReader(client.getInputStream(),StandardCharsets.UTF_8));
                buffer = in.readLine();
                response(buffer);
            } catch (IOException e)
            {
                System.out.println("Die Verbindung kann nicht aufgebaut werden");
                e.printStackTrace();
                //status = false;
            }
            
            try
            {
                if (null != client)
                {
                    client.close();
                }
            }
            catch (IOException e)
            {
                System.out.println("Die Verbindung ist fehlgeschlagen!");
                e.printStackTrace();
            }
        }

    }

    /**
     * Hier wird der Server die Kommando bearbeiten
     * @param command ist die Kommando (als einzel String) von Client
     */
    private void response(String command)
    {
        String[] cmd = null;
        cmd = command.split("\\s+",4);
        if (2 == cmd.length & cmd[0].equals("GET"))     // Zeit und Datum kommando
        {
            Date date = new Date();
            if (cmd[1].equals("Time"))
            {
                historyCommand.add("GET Time");
                String strDateFormat = "HH:mm:ss";
                SimpleDateFormat sdf = new SimpleDateFormat(strDateFormat);
                out.println(sdf.format(date));
            }
            else if (cmd[1].equals("Date"))
            {
                historyCommand.add("GET Date");
                String strDateFormat = "dd.MM.yyyy";
                SimpleDateFormat sdf = new SimpleDateFormat(strDateFormat);
                out.println(sdf.format(date));
            }
            else
            {
                out.println("Unbekannte Anfrage!");
                // Unbekannte Anfrage!
            }
        }
        // Arithmetisches Kommando
        else if (3 == cmd.length & (cmd[0].equals("ADD") | cmd[0].equals("SUB") | cmd[0].equals("MUL") | cmd[0].equals("DIV")))
        {
            if (cmd[1].matches(".*\\D+.*") | cmd[2].matches(".*\\D+.*"))
            {
                out.println("Falsches Format!");
            }
            else
            {
                int var1 = Integer.valueOf(cmd[1]);
                int var2 = Integer.valueOf(cmd[2]);
                if (cmd[0].equals("ADD"))
                {
                    int result = var1 + var2;
                    out.println(result);
                }
                else if (cmd[0].equals("SUB"))
                {
                    int result = var1 - var2;
                    out.println(result);
                }
                else if (cmd[0].equals("MUL"))
                {
                    int result = var1 * var2;
                    out.println(result);
                }
                else if (cmd[0].equals("DIV"))
                {
                    if (0 == var2)
                    {
                        out.println("undefined");
                    }
                    else
                    {
                        double result = var1 / var2;
                        out.println(result);
                    }
                } else
                {
                    out.println("Unbekannte Anfrage!");
                    // Unbekannte Anfrage!
                }
                historyCommand.add(cmd[0] + " " + cmd[1] + " " + cmd[2]);
            }

        }
        else if (cmd[0].equals("ECHO"))
        {
            cmd = command.split("\\s+",2);
            out.println(cmd[1]);
            historyCommand.add("ECHO " + cmd[1]);
        }
        else if (cmd[0].equals("DISCARD"))
        {
            // Keine Antwort, da der Discard-Dienst alle empfangenen Daten verwirft.
        }
        else if (1 == cmd.length & cmd[0].equals("PING"))
        {
            out.println("PONG");
            historyCommand.add("PING");
        }
        else if (cmd[0].equals("HISTORY") & cmd.length <= 2)
        {
            if (1 == cmd.length)        // Alle Historie bisher
            {
                showHistory();
                historyCommand.add(cmd[0]);
            }
            else        // letzte <Integer> Historie
            {
                if (cmd[1].matches(".*\\D+.*"))      // Der Parameter der hietorie soll einen Integer sein
                {
                    out.println("Falsches Format!");
                }
                else        // 未完成
                {
                    int counter = Integer.valueOf(cmd[1]);
                    showHistory(counter);
                    historyCommand.add(cmd[0] + " " + cmd[1]);
                }
            }
        }
        else
        {
            out.println("Unbekannte Anfrage!");
            // Unbekannte Anfrage!
        }
    }

    /**
     * Hier soll die Historien gesendet werden
     * @param limit ist die Beschränkung der Anzahl der gesendet Historien, für limit = -1 gibt es kein Anzahl beschränkung
     */
    private void showHistory(int limit)
    {
        if (limit > historyCommand.size() | -1 == limit)
        {
            limit = historyCommand.size();
        }
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = historyCommand.size() - 1; i >= historyCommand.size() - limit; i--)
        {
            stringBuilder.append(historyCommand.get(i));
            if (i != historyCommand.size() - limit)
            {
                stringBuilder.append("\n");
            }
        }
        out.println(stringBuilder.toString());
    }

    /**
     * Hier soll alle Historien gesendet
     */
    private void showHistory()
    {
        showHistory(-1);
    }

    /**
     * Hier soll die Verbindung und alle Streams geschlossen werden.
     */
    public void disconnect() {
        continueExecute = false;
        /*
        try
        {
            Thread.sleep(1);
        }
        catch (InterruptedException e)
        {
            System.out.println("Sleep fail");
        }
        */
        try
        {
            if (null != client)
            {
                client.close();
            }
            serverSocket.close();
            if (null != out)
            {
                out.close();
            }
            if (null != in)
            {
                in.close();
            }


        } catch (IOException e)
        {
            System.out.println("Feler! \"serverSocket\" kann nicht abgeschlossen werden!");
        }
    }

    /**
     * Überprüfung der Port-Nummer und Speicherung dieser in die Klassen-Variable "port"
     * @param port Portnummer als String
     * @return Port-Nummer ist akzeptabel TRUE oder nicht FALSE
     */
    public boolean checkPort(String port) {
        if (!port.equals("2020"))
        {
            System.out.println("Kein korrekter Port! Aktuell ist nur Port 2020 möglich.");
            return false;
        }
        this.port = 2020;
        try
        {
            serverSocket.bind(new InetSocketAddress(this.port));
        } catch (IOException e)
        {
            System.out.println("Fehler! \"serverSocket\" kann nicht mit Port gebunden werden!");
        }
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
