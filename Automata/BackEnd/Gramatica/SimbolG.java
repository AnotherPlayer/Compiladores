package BackEnd;

//2° parcial

public class SimbolG {

    public String NombSimb;
    public int token;
    public boolean esTerminal;

    public SimbolG(String nombre, int token) {
        this.NombSimb = nombre;
        this.token = token;
        this.esTerminal = false;
    }

    public SimbolG(String nombre, int token, boolean esTerminal) {
        this.NombSimb = nombre;
        this.token = token;
        this.esTerminal = esTerminal;
    }

    public void cambiarToken(int nuevoToken) {
        this.token = nuevoToken;
    }

    @Override
    public String toString() {
        return NombSimb;
    }
}
