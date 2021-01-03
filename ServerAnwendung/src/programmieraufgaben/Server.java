package programmieraufgaben;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;

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
        Socket client = null;
        //boolean status = true;
        while (true)
        {
            String buffer = null;
            String[] cmd = null;
            try
            {
                client = serverSocket.accept();
                out = new PrintWriter(new OutputStreamWriter(client.getOutputStream(), StandardCharsets.UTF_8),true);
                in = new BufferedReader(new InputStreamReader(client.getInputStream(),StandardCharsets.UTF_8));
                buffer = in.readLine();
                cmd = buffer.split("\\s+",4);
                if (2 == cmd.length & cmd[0].equals("GET"))     // Zeit und Datum kommando
                {

                    Date date = new Date();
                    if (cmd[1].equals("Time"))
                    {
                        String strDateFormat = "HH:mm:ss";
                        SimpleDateFormat sdf = new SimpleDateFormat(strDateFormat);
                        out.print(sdf.format(date));
                    }
                    else if (cmd[1].equals("Date"))
                    {
                        String strDateFormat = "dd.MM.yyyy";
                        SimpleDateFormat sdf = new SimpleDateFormat(strDateFormat);
                        out.print(sdf.format(date));
                    }
                    else
                    {
                        out.print("Unbekannte Anfrage!");
                        // Unbekannte Anfrage!
                    }
                }
                else if (3 == cmd.length)       // Arithmetisches Kommando
                {
                    if (cmd[1].matches("\\D") | cmd[2].matches("\\D"))
                    {
                        out.print("Falsches Format!");
                    }
                    else
                    {
                        int var1 = Integer.valueOf(cmd[1]);
                        int var2 = Integer.valueOf(cmd[2]);
                        if (cmd[0].equals("ADD"))
                        {
                            int result = var1 + var2;
                            out.print(result);
                        }
                        else if (cmd[0].equals("SUB"))
                        {
                            int result = var1 - var2;
                            out.print(result);
                        }
                        else if (cmd[0].equals("MUL"))
                        {
                            int result = var1 * var2;
                            out.print(result);
                        }
                        else if (cmd[0].equals("DIV"))
                        {
                            if (0 == var2)
                            {
                                out.print("undefined");
                            }
                            else
                            {
                                double result = var1 / var2;
                                out.print(result);
                            }
                        } else
                        {
                            out.print("Unbekannte Anfrage!");
                            // Unbekannte Anfrage!
                        }
                    }

                }
                else if (cmd[0].equals("ECHO"))
                {
                    cmd = buffer.split("\\s+",2);
                    out.print(cmd[1]);
                }
                else if (cmd[0].equals("DISCARD"))
                {
                    // Keine Antwort, da der Discard-Dienst alle empfangenen Daten verwirft.
                }
                else if (1 == cmd.length & cmd[0].equals("PING"))
                {
                    out.print("PONG");
                }
                else if (cmd[0].equals("HISTORY") & cmd.length <= 2)
                {
                    if (1 == cmd.length)        // Alle Historie bisher
                    {

                    }
                    else        // letzte <Integer> Historie
                    {
                        if (cmd[1].matches("\\D"))      // Der Parameter der hietorie soll einen Integer sein
                        {
                            out.print("Falsches Format!");
                        }
                        else        // 未完成
                        {
                            int counter = Integer.valueOf(cmd[1]);

                        }
                    }
                }
                else
                {
                    out.print("Unbekannte Anfrage!");
                    // Unbekannte Anfrage!
                }
            } catch (IOException e)
            {
                System.out.println("Die Verbindung ist fehlgeschlagen!");
                //status = false;
            }



            try
            {
                client.close();
            }
            catch (IOException e)
            {
                System.out.println("Die Verbindung ist fehlgeschlagen!");
            }
        }
        disconnect();
    }

    /**
     * Hier soll die Verbindung und alle Streams geschlossen werden.
     */
    public void disconnect() {
        try
        {
            serverSocket.close();
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
