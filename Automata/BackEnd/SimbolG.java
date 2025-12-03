package BackEnd;

//2° parcial

public class SimbolG {

    /** Nombre del símbolo (terminal o no terminal). */
    public String NombSimb;
    /** Token asociado (solo aplica para terminales). */
    public int token;
    /** Indica si el símbolo es terminal. */
    public boolean esTerminal;

    public SimbolG(String nombre, int token) {
        this(nombre, token, false);
    }

    public SimbolG(String nombre, int token, boolean esTerminal) {
        this.NombSimb = nombre;
        this.token = token;
        this.esTerminal = esTerminal;
    }

    @Override
    public String toString() {
        return NombSimb;
    }
}
