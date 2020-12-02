package programmieraufgaben;

/**
 * Hier sollen die Nutzereingaben sowie die Resultate gespeichert werden.
 * Die Struktur der Klasse und die Variablen können frei gewählt werden.
 */
public class DataPackage {
    //maximale Datenteil-Länge
    private int dataPackageLength;
    private int serialNumber;
    private int IPVersion;
    private String absender;
    private String empfaenger;
    private String message;

    /**
     * Erzeugt ein DataPackage Objekt und speichert beim erzeugen die maximale Datenteil-Länge
     * @param dataPackageLength
     */
    public DataPackage(int dataPackageLength) {
        this.dataPackageLength = dataPackageLength;
    }

    /**
     *
     */
    public DataPackage(int dataPackageLength, int serialNumber, int IPVersion, String absender, String empfaenger, String message){
        this.dataPackageLength = dataPackageLength;
        this.serialNumber = serialNumber;
        this.IPVersion = IPVersion;
        this.absender = absender;
        this.empfaenger = empfaenger;
        this.message = message;
    }

    /**
     * Gibt die maximale Datenteil-Länge zurück
     * @return maximale Datenteil-Länge
     */
    public int getDataPackageLength() {
        return dataPackageLength;
    }

    /**
     * Setzt die maximale Datenteil-Länge
     * @param dataPackageLength
     */
    public void setDataPackageLength(int dataPackageLength) {
        this.dataPackageLength = dataPackageLength;
    }

    public void show(){
        System.out.println("Version: " + IPVersion);
        System.out.println("Absender: " + absender);
        System.out.println("Empfänger: " + empfaenger);
        System.out.println("Paketlaufnummer: " + serialNumber);
        System.out.println("Datenteil-Länge: " + dataPackageLength);
        System.out.println("Datenteil: " + message);
        System.out.println();
    }
}
