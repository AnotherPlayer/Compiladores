import java.util.HashMap;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

/**
 * Clase Controladora que conecta el Frontend con el Backend
 * Maneja toda la lógica de negocio entre la GUI y las clases del autómata
 */
public class ControladorAutomata {

    private HashMap<String, AFN> automatasAFN;
    private HashMap<String, AFD> automatasAFD;
    private AFD afdActual;
    private analizadorLexico analizador;
    private int contadorID;

    public ControladorAutomata() {
        automatasAFN = new HashMap<>();
        automatasAFD = new HashMap<>();
        analizador = new analizadorLexico();
        contadorID = 1;
        afdActual = null;
    }

    /**
     * Crear un AFN básico para un símbolo
     */
    public String crearAFNBasico(String nombre, char simbolo, int token) {
        try {
            if (automatasAFN.containsKey(nombre) || automatasAFD.containsKey(nombre)) {
                return "Error: Ya existe un autómata con ese nombre";
            }

            AFN nuevoAFN = new AFN();
            nuevoAFN.doBasic(simbolo);

            // Asignar token a los estados de aceptación
            for (Estado e : nuevoAFN.getEdosAcept()) {
                e.Token = token;
            }

            automatasAFN.put(nombre, nuevoAFN);
            return "AFN '" + nombre + "' creado exitosamente para el símbolo '" + simbolo + "' con token " + token;
        } catch (Exception e) {
            return "Error al crear AFN: " + e.getMessage();
        }
    }

    /**
     * Crear un AFN básico para un rango de símbolos
     */
    public String crearAFNRango(String nombre, char inicio, char fin, int token) {
        try {
            if (automatasAFN.containsKey(nombre) || automatasAFD.containsKey(nombre)) {
                return "Error: Ya existe un autómata con ese nombre";
            }

            AFN nuevoAFN = new AFN();
            nuevoAFN.doBasic(inicio, fin);

            // Asignar token a los estados de aceptación
            for (Estado e : nuevoAFN.getEdosAcept()) {
                e.Token = token;
            }

            automatasAFN.put(nombre, nuevoAFN);
            return "AFN '" + nombre + "' creado exitosamente para el rango [" + inicio + "-" + fin + "] con token " + token;
        } catch (Exception e) {
            return "Error al crear AFN: " + e.getMessage();
        }
    }

    /**
     * Unir dos AFNs
     */
    public String unirAFNs(String nombre1, String nombre2, String nuevoNombre, int token) {
        try {
            if (!automatasAFN.containsKey(nombre1) || !automatasAFN.containsKey(nombre2)) {
                return "Error: Uno o ambos autómatas no existen";
            }

            AFN afn1 = automatasAFN.get(nombre1);
            AFN afn2 = automatasAFN.get(nombre2);

            afn1.AFN_union(afn2);

            // Asignar nuevo token
            for (Estado e : afn1.getEdosAcept()) {
                e.Token = token;
            }

            // Guardar con nuevo nombre
            automatasAFN.put(nuevoNombre, afn1);

            // Opcionalmente eliminar los originales
            automatasAFN.remove(nombre1);
            automatasAFN.remove(nombre2);

            return "Unión realizada exitosamente. Nuevo autómata: " + nuevoNombre;
        } catch (Exception e) {
            return "Error al unir AFNs: " + e.getMessage();
        }
    }

    /**
     * Concatenar dos AFNs
     */
    public String concatenarAFNs(String nombre1, String nombre2, String nuevoNombre) {
        try {
            if (!automatasAFN.containsKey(nombre1) || !automatasAFN.containsKey(nombre2)) {
                return "Error: Uno o ambos autómatas no existen";
            }

            AFN afn1 = automatasAFN.get(nombre1);
            AFN afn2 = automatasAFN.get(nombre2);

            afn1.AFN_join(afn2);

            // Guardar con nuevo nombre
            automatasAFN.put(nuevoNombre, afn1);

            // Opcionalmente eliminar los originales
            automatasAFN.remove(nombre1);
            automatasAFN.remove(nombre2);

            return "Concatenación realizada exitosamente. Nuevo autómata: " + nuevoNombre;
        } catch (Exception e) {
            return "Error al concatenar AFNs: " + e.getMessage();
        }
    }

    /**
     * Aplicar cerradura positiva
     */
    public String aplicarCerraduraPositiva(String nombre) {
        try {
            if (!automatasAFN.containsKey(nombre)) {
                return "Error: El autómata no existe";
            }

            AFN afn = automatasAFN.get(nombre);
            afn.AFN_cerrPos();

            return "Cerradura positiva aplicada exitosamente a " + nombre;
        } catch (Exception e) {
            return "Error al aplicar cerradura positiva: " + e.getMessage();
        }
    }

    /**
     * Aplicar cerradura de Kleene
     */
    public String aplicarCerraduraKleene(String nombre) {
        try {
            if (!automatasAFN.containsKey(nombre)) {
                return "Error: El autómata no existe";
            }

            AFN afn = automatasAFN.get(nombre);
            afn.AFN_cerrKleene();

            return "Cerradura de Kleene aplicada exitosamente a " + nombre;
        } catch (Exception e) {
            return "Error al aplicar cerradura de Kleene: " + e.getMessage();
        }
    }

    /**
     * Aplicar operación opcional
     */
    public String aplicarOpcional(String nombre) {
        try {
            if (!automatasAFN.containsKey(nombre)) {
                return "Error: El autómata no existe";
            }

            AFN afn = automatasAFN.get(nombre);
            afn.AFN_opcional();

            return "Operación opcional aplicada exitosamente a " + nombre;
        } catch (Exception e) {
            return "Error al aplicar opcional: " + e.getMessage();
        }
    }

    /**
     * Convertir un AFN a AFD
     */
    public String convertirAFNaAFD(String nombreAFN, String nombreAFD) {
        try {
            if (!automatasAFN.containsKey(nombreAFN)) {
                return "Error: El AFN no existe";
            }

            AFN afn = automatasAFN.get(nombreAFN);
            AFD afd = AFD.AFNtoAFD(afn);

            automatasAFD.put(nombreAFD, afd);
            afdActual = afd;
            analizador.setAutomata(afd);

            return "Conversión exitosa de " + nombreAFN + " a AFD: " + nombreAFD;
        } catch (Exception e) {
            return "Error al convertir a AFD: " + e.getMessage();
        }
    }

    /**
     * Unir todos los AFNs y convertir a AFD
     */
    public String unirTodosYConvertir() {
        try {
            if (automatasAFN.isEmpty()) {
                return "Error: No hay AFNs para unir";
            }

            AFN afnUnido = null;
            for (AFN afn : automatasAFN.values()) {
                if (afnUnido == null) {
                    afnUnido = afn;
                } else {
                    afnUnido.AFN_union(afn);
                }
            }

            afdActual = AFD.AFNtoAFD(afnUnido);
            analizador.setAutomata(afdActual);
            automatasAFD.put("AFD_Unido", afdActual);

            return "Todos los AFNs unidos y convertidos a AFD exitosamente";
        } catch (Exception e) {
            return "Error al unir y convertir: " + e.getMessage();
        }
    }

    /**
     * Analizar una cadena con el AFD actual
     */
    public String analizarCadena(String cadena) {
        if (afdActual == null) {
            return "Error: Primero debe crear o cargar un AFD";
        }

        StringBuilder resultado = new StringBuilder();
        analizador.SetSigma(cadena);

        resultado.append("=== Análisis Léxico de: \"").append(cadena).append("\" ===\n\n");

        int token;
        boolean hayTokens = false;
        while ((token = analizador.yylex()) != -1) {
            if (token > 0) {
                hayTokens = true;
                resultado.append("Token: ").append(token)
                        .append(" | Lexema: \"").append(analizador.getYytext())
                        .append("\" | Posición: [").append(analizador.getIniLexema())
                        .append(",").append(analizador.getFinLexema()).append("]\n");
            }
        }

        if (!hayTokens) {
            resultado.append("No se encontraron tokens válidos en la cadena.\n");
        }

        return resultado.toString();
    }

    /**
     * Obtener la tabla del AFD actual como modelo para JTable
     */
    public DefaultTableModel obtenerTablaAFD() {
        if (afdActual == null) {
            return null;
        }

        // Crear columnas: Estado + símbolos del alfabeto + Token
        ArrayList<String> columnas = new ArrayList<>();
        columnas.add("Estado");

        for (Character c : afdActual.alfabeto) {
            if (c != simbEspeciales.EPSILON) {
                if (c == ' ') {
                    columnas.add("' '");
                } else {
                    columnas.add(String.valueOf(c));
                }
            }
        }
        columnas.add("Token");

        // Crear datos de la tabla
        Object[][] datos = new Object[afdActual.NumEdos][columnas.size()];

        for (int i = 0; i < afdActual.NumEdos; i++) {
            int col = 0;

            // Estado
            datos[i][col++] = i;

            // Transiciones
            for (Character c : afdActual.alfabeto) {
                if (c != simbEspeciales.EPSILON) {
                    int ascii = (int) c;
                    int destino = afdActual.EdosAFD[i].transAFD[ascii];
                    datos[i][col++] = (destino == -1) ? "-" : destino;
                }
            }

            // Token
            boolean esAceptacion = false;
            for (Integer edoAcept : afdActual.EdosAceptacion) {
                if (edoAcept == i) {
                    esAceptacion = true;
                    break;
                }
            }

            if (esAceptacion) {
                datos[i][col] = afdActual.EdosAFD[i].Token;
            } else {
                datos[i][col] = -1;
            }
        }

        return new DefaultTableModel(datos, columnas.toArray());
    }

    /**
     * Guardar AFD en archivo
     */
    public String guardarAFD(String nombreArchivo) {
        if (afdActual == null) {
            return "Error: No hay AFD para guardar";
        }

        try {
            if (afdActual.SaveAFD(nombreArchivo)) {
                return "AFD guardado exitosamente en " + nombreArchivo;
            } else {
                return "Error al guardar el AFD";
            }
        } catch (Exception e) {
            return "Error al guardar: " + e.getMessage();
        }
    }

    /**
     * Cargar AFD desde archivo
     */
    public String cargarAFD(String nombreArchivo) {
        try {
            afdActual = new AFD();
            if (afdActual.LoadAFD(nombreArchivo)) {
                analizador.setAutomata(afdActual);
                return "AFD cargado exitosamente desde " + nombreArchivo;
            } else {
                afdActual = null;
                return "Error al cargar el AFD";
            }
        } catch (Exception e) {
            afdActual = null;
            return "Error al cargar: " + e.getMessage();
        }
    }

    /**
     * Obtener lista de AFNs creados
     */
    public ArrayList<String> obtenerListaAFNs() {
        return new ArrayList<>(automatasAFN.keySet());
    }

    /**
     * Obtener lista de AFDs creados
     */
    public ArrayList<String> obtenerListaAFDs() {
        return new ArrayList<>(automatasAFD.keySet());
    }

    /**
     * Limpiar todos los autómatas
     */
    public void limpiarTodo() {
        automatasAFN.clear();
        automatasAFD.clear();
        afdActual = null;
        contadorID = 1;
    }

    /**
     * Verificar si hay un AFD activo
     */
    public boolean hayAFDActivo() {
        return afdActual != null;
    }

    /**
     * Establecer un AFD específico como actual
     */
    public String establecerAFDActual(String nombre) {
        if (!automatasAFD.containsKey(nombre)) {
            return "Error: El AFD no existe";
        }

        afdActual = automatasAFD.get(nombre);
        analizador.setAutomata(afdActual);
        return "AFD '" + nombre + "' establecido como actual";
    }
}