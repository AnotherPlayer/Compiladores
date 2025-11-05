import java.util.HashMap;
import java.util.ArrayList;
import javax.swing.table.DefaultTableModel;

/**
 * ========================================
 * CLASE CONTROLADORAUTOMATA
 * ========================================
 * PROPÓSITO: Servir como PUENTE entre el Frontend (GUI) y el Backend (AFN, AFD, etc.)
 *
 * ¿POR QUÉ EXISTE?
 * - La GUI no debe conocer los detalles de implementación del backend
 * - El backend no debe saber que existe una GUI
 * - Este controlador los conecta y maneja la comunicación entre ambos
 *
 * PATRÓN DE DISEÑO: Controller (del patrón MVC - Model View Controller)
 * ========================================
 */
public class ConexionBaF {

    // ========== ATRIBUTOS DE LA CLASE ==========

    // Almacena MÚLTIPLES AFNs con nombres únicos (ej: "AFN_Digitos", "AFN_Letras")
    private HashMap<String, AFN> automatasAFN;

    // Almacena MÚLTIPLES AFDs con nombres únicos
    private HashMap<String, AFD> automatasAFD;

    // El AFD que está actualmente seleccionado para análisis
    private AFD afdActual;

    // El analizador léxico que usará el AFD actual para analizar cadenas
    private analizadorLexico analizador;

    /**
     * CONSTRUCTOR
     * Inicializa todas las estructuras de datos vacías
     */
    public ConexionBaF() {
        automatasAFN = new HashMap<>();     // Crea mapa vacío para AFNs
        automatasAFD = new HashMap<>();     // Crea mapa vacío para AFDs
        analizador = new analizadorLexico(); // Crea analizador léxico
        afdActual = null;                    // No hay AFD seleccionado al inicio
    }

    // ========== MÉTODOS PARA CREAR AFNs ==========

    /**
     * CREAR AFN BÁSICO (un solo símbolo)
     *
     * ¿QUÉ HACE?
     * 1. Recibe datos de la GUI: nombre, símbolo, token
     * 2. Crea un AFN usando el backend
     * 3. Lo guarda en el HashMap con el nombre dado
     * 4. Retorna mensaje de éxito/error para mostrar en GUI
     *
     * @param nombre - Nombre único para identificar este AFN (ej: "AFN_a")
     * @param simbolo - El carácter que reconocerá (ej: 'a')
     * @param token - Número que identifica qué tipo de token es (ej: 10 = LETRA)
     * @return String con mensaje de éxito o error para mostrar al usuario
     */
    public String crearAFNBasico(String nombre, char simbolo, int token) {
        try {
            // VALIDACIÓN: Verifica que no exista otro autómata con el mismo nombre
            if (automatasAFN.containsKey(nombre) || automatasAFD.containsKey(nombre)) {
                return "Error: Ya existe un autómata con ese nombre";
            }

            // CREAR: Llama al backend para crear el AFN
            AFN nuevoAFN = new AFN();
            nuevoAFN.doBasic(simbolo);  // ← AQUÍ LLAMA AL MÉTODO DEL PSEUDOCÓDIGO

            // ASIGNAR TOKEN: Le pone el token a todos los estados de aceptación
            for (Estado e : nuevoAFN.getEdosAcept()) {
                e.Token = token;
            }

            // GUARDAR: Lo guarda en el HashMap con el nombre dado
            automatasAFN.put(nombre, nuevoAFN);

            // RETORNAR: Mensaje de éxito para la GUI
            return "AFN '" + nombre + "' creado exitosamente para el símbolo '" + simbolo + "' con token " + token;

        } catch (Exception e) {
            // MANEJO DE ERRORES: Si algo falla, retorna mensaje de error
            return "Error al crear AFN: " + e.getMessage();
        }
    }

    /**
     * CREAR AFN PARA RANGO (ej: [a-z] o [0-9])
     * Igual que el anterior pero para un rango de caracteres
     */
    public String crearAFNRango(String nombre, char inicio, char fin, int token) {
        try {
            // VALIDACIÓN
            if (automatasAFN.containsKey(nombre) || automatasAFD.containsKey(nombre)) {
                return "Error: Ya existe un autómata con ese nombre";
            }

            // CREAR con rango
            AFN nuevoAFN = new AFN();
            nuevoAFN.doBasic(inicio, fin);  // ← LLAMA AL MÉTODO DEL PSEUDOCÓDIGO PARA RANGOS

            // ASIGNAR TOKEN
            for (Estado e : nuevoAFN.getEdosAcept()) {
                e.Token = token;
            }

            // GUARDAR
            automatasAFN.put(nombre, nuevoAFN);

            return "AFN '" + nombre + "' creado exitosamente para el rango [" + inicio + "-" + fin + "] con token " + token;
        } catch (Exception e) {
            return "Error al crear AFN: " + e.getMessage();
        }
    }

    // ========== MÉTODOS PARA OPERACIONES ENTRE AFNs ==========

    /**
     * UNIR DOS AFNs (Operación OR)
     * Une dos AFNs para que reconozca cualquiera de los dos patrones
     * Ejemplo: AFN('a') UNION AFN('b') = reconoce 'a' O 'b'
     */
    public String unirAFNs(String nombre1, String nombre2, String nuevoNombre, int token) {
        try {
            // VALIDACIÓN: Verificar que ambos AFNs existan
            if (!automatasAFN.containsKey(nombre1) || !automatasAFN.containsKey(nombre2)) {
                return "Error: Uno o ambos autómatas no existen";
            }

            // OBTENER: Busca los AFNs en el HashMap
            AFN afn1 = automatasAFN.get(nombre1);
            AFN afn2 = automatasAFN.get(nombre2);

            // UNIR: Llama al método del backend
            afn1.AFN_union(afn2);  // ← MÉTODO DEL PSEUDOCÓDIGO

            // ASIGNAR TOKEN al resultado
            for (Estado e : afn1.getEdosAcept()) {
                e.Token = token;
            }

            // GUARDAR resultado con nuevo nombre
            automatasAFN.put(nuevoNombre, afn1);

            // LIMPIAR: Elimina los AFNs originales (ya están unidos)
            automatasAFN.remove(nombre1);
            automatasAFN.remove(nombre2);

            return "Unión realizada exitosamente. Nuevo autómata: " + nuevoNombre;
        } catch (Exception e) {
            return "Error al unir AFNs: " + e.getMessage();
        }
    }

    /**
     * CONCATENAR DOS AFNs
     * Une dos AFNs en secuencia (primero uno, luego el otro)
     * Ejemplo: AFN('a') CONCAT AFN('b') = reconoce "ab"
     */
    public String concatenarAFNs(String nombre1, String nombre2, String nuevoNombre) {
        try {
            // VALIDACIÓN
            if (!automatasAFN.containsKey(nombre1) || !automatasAFN.containsKey(nombre2)) {
                return "Error: Uno o ambos autómatas no existen";
            }

            // OBTENER
            AFN afn1 = automatasAFN.get(nombre1);
            AFN afn2 = automatasAFN.get(nombre2);

            // CONCATENAR
            afn1.AFN_join(afn2);  // ← MÉTODO DEL PSEUDOCÓDIGO

            // GUARDAR
            automatasAFN.put(nuevoNombre, afn1);

            // LIMPIAR
            automatasAFN.remove(nombre1);
            automatasAFN.remove(nombre2);

            return "Concatenación realizada exitosamente. Nuevo autómata: " + nuevoNombre;
        } catch (Exception e) {
            return "Error al concatenar AFNs: " + e.getMessage();
        }
    }

    // ========== MÉTODOS PARA CERRADURAS ==========

    /**
     * CERRADURA POSITIVA (+)
     * Modifica un AFN para que reconozca una o más repeticiones
     * Ejemplo: AFN('a')+ = reconoce "a", "aa", "aaa", etc.
     */
    public String aplicarCerraduraPositiva(String nombre) {
        try {
            // VALIDACIÓN
            if (!automatasAFN.containsKey(nombre)) {
                return "Error: El autómata no existe";
            }

            // OBTENER
            AFN afn = automatasAFN.get(nombre);

            // APLICAR CERRADURA
            afn.AFN_cerrPos();  // ← MÉTODO DEL PSEUDOCÓDIGO

            return "Cerradura positiva aplicada exitosamente a " + nombre;
        } catch (Exception e) {
            return "Error al aplicar cerradura positiva: " + e.getMessage();
        }
    }

    /**
     * CERRADURA DE KLEENE (*)
     * Modifica un AFN para que reconozca cero o más repeticiones
     * Ejemplo: AFN('a')* = reconoce "", "a", "aa", "aaa", etc.
     */
    public String aplicarCerraduraKleene(String nombre) {
        try {
            // VALIDACIÓN
            if (!automatasAFN.containsKey(nombre)) {
                return "Error: El autómata no existe";
            }

            // OBTENER
            AFN afn = automatasAFN.get(nombre);

            // APLICAR CERRADURA
            afn.AFN_cerrKleene();  // ← MÉTODO DEL PSEUDOCÓDIGO

            return "Cerradura de Kleene aplicada exitosamente a " + nombre;
        } catch (Exception e) {
            return "Error al aplicar cerradura de Kleene: " + e.getMessage();
        }
    }

    /**
     * OPERACIÓN OPCIONAL (?)
     * Modifica un AFN para que sea opcional
     * Ejemplo: AFN('a')? = reconoce "" o "a"
     */
    public String aplicarOpcional(String nombre) {
        try {
            // VALIDACIÓN
            if (!automatasAFN.containsKey(nombre)) {
                return "Error: El autómata no existe";
            }

            // OBTENER
            AFN afn = automatasAFN.get(nombre);

            // APLICAR OPCIONAL
            afn.AFN_opcional();  // ← MÉTODO DEL PSEUDOCÓDIGO

            return "Operación opcional aplicada exitosamente a " + nombre;
        } catch (Exception e) {
            return "Error al aplicar opcional: " + e.getMessage();
        }
    }

    // ========== CONVERSIÓN AFN A AFD ==========

    /**
     * CONVERTIR AFN A AFD
     * Convierte un AFN (no determinista) a AFD (determinista)
     * El AFD es más eficiente para análisis léxico
     *
     * ⚠️ PROBLEMA: AFD.AFNtoAFD() NO ESTÁ IMPLEMENTADO EN AFD.java
     */
    public String convertirAFNaAFD(String nombreAFN, String nombreAFD) {
        try {
            // VALIDACIÓN
            if (!automatasAFN.containsKey(nombreAFN)) {
                return "Error: El AFN no existe";
            }

            // OBTENER AFN
            AFN afn = automatasAFN.get(nombreAFN);

            // CONVERTIR (ESTE MÉTODO NO EXISTE EN AFD.java)
            AFD afd = AFD.AFNtoAFD(afn);  // ← DEBERÍA ESTAR EN EL PSEUDOCÓDIGO

            // GUARDAR AFD
            automatasAFD.put(nombreAFD, afd);

            // ESTABLECER COMO ACTUAL
            afdActual = afd;
            analizador.setAutomata(afd);

            return "Conversión exitosa de " + nombreAFN + " a AFD: " + nombreAFD;
        } catch (Exception e) {
            return "Error al convertir a AFD: " + e.getMessage();
        }
    }

    /**
     * UNIR TODOS Y CONVERTIR
     * Une TODOS los AFNs existentes y los convierte a un solo AFD
     * Útil para crear un analizador léxico completo
     */
    public String unirTodosYConvertir() {
        try {
            // VALIDACIÓN
            if (automatasAFN.isEmpty()) {
                return "Error: No hay AFNs para unir";
            }

            // UNIR TODOS
            AFN afnUnido = null;
            for (AFN afn : automatasAFN.values()) {
                if (afnUnido == null) {
                    afnUnido = afn;  // El primero
                } else {
                    afnUnido.AFN_union(afn);  // Une con los demás
                }
            }

            // CONVERTIR A AFD
            afdActual = AFD.AFNtoAFD(afnUnido);  //NO EXISTE
            analizador.setAutomata(afdActual);

            // GUARDAR
            automatasAFD.put("AFD_Unido", afdActual);

            return "Todos los AFNs unidos y convertidos a AFD exitosamente";
        } catch (Exception e) {
            return "Error al unir y convertir: " + e.getMessage();
        }
    }

    // ========== ANÁLISIS LÉXICO ==========

    /**
     * ANALIZAR CADENA
     * Usa el AFD actual para analizar una cadena y extraer tokens
     *
     * PROCESO:
     * 1. Recibe cadena de la GUI
     * 2. Usa analizadorLexico con el AFD actual
     * 3. Extrae todos los tokens
     * 4. Formatea resultado para mostrar en GUI
     */
    public String analizarCadena(String cadena) {
        // VALIDACIÓN: Debe haber un AFD cargado
        if (afdActual == null) {
            return "Error: Primero debe crear o cargar un AFD";
        }

        // PREPARAR ANÁLISIS
        StringBuilder resultado = new StringBuilder();
        analizador.SetSigma(cadena);  // Establece la cadena a analizar

        // ENCABEZADO
        resultado.append("=== Análisis Léxico de: \"").append(cadena).append("\" ===\n\n");

        // ANALIZAR: Extrae tokens uno por uno
        int token;
        boolean hayTokens = false;
        while ((token = analizador.yylex()) != -1) {  // ← MÉTODO DEL PSEUDOCÓDIGO
            if (token > 0) {
                hayTokens = true;
                // Formatea cada token encontrado
                resultado.append("Token: ").append(token)
                        .append(" | Lexema: \"").append(analizador.getYytext())
                        .append("\" | Posición: [").append(analizador.getIniLexema())
                        .append(",").append(analizador.getFinLexema()).append("]\n");
            }
        }

        // RESULTADO FINAL
        if (!hayTokens) {
            resultado.append("No se encontraron tokens válidos en la cadena.\n");
        }

        return resultado.toString();
    }

    // ========== MÉTODOS PARA LA GUI ==========

    /**
     * OBTENER TABLA AFD
     * Convierte el AFD actual a formato de tabla para mostrar en GUI
     *
     * GENERA:
     * - Columnas: Estado, cada símbolo del alfabeto, Token
     * - Filas: Un estado por fila con sus transiciones
     */
    public DefaultTableModel obtenerTablaAFD() {
        // VALIDACIÓN
        if (afdActual == null) {
            return null;
        }

        // CREAR COLUMNAS
        ArrayList<String> columnas = new ArrayList<>();
        columnas.add("Estado");  // Primera columna

        // Agregar una columna por cada símbolo del alfabeto
        for (Character c : afdActual.alfabeto) {
            if (c != simbEspeciales.EPSILON) {  // No mostrar epsilon
                if (c == ' ') {
                    columnas.add("' '");  // Espacio visible
                } else {
                    columnas.add(String.valueOf(c));
                }
            }
        }
        columnas.add("Token");  // Última columna

        // CREAR DATOS (matriz de objetos)
        Object[][] datos = new Object[afdActual.NumEdos][columnas.size()];

        // LLENAR TABLA
        for (int i = 0; i < afdActual.NumEdos; i++) {
            int col = 0;

            // Columna Estado
            datos[i][col++] = i;

            // Columnas de Transiciones
            for (Character c : afdActual.alfabeto) {
                if (c != simbEspeciales.EPSILON) {
                    int ascii = (int) c;
                    int destino = afdActual.EdosAFD[i].transAFD[ascii];
                    datos[i][col++] = (destino == -1) ? "-" : destino;
                }
            }

            // Columna Token (solo si es estado de aceptación)
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
                datos[i][col] = -1;  // No es de aceptación
            }
        }

        // RETORNAR modelo de tabla para JTable
        return new DefaultTableModel(datos, columnas.toArray());
    }

    // ========== PERSISTENCIA (GUARDAR/CARGAR) ==========

    /**
     * GUARDAR AFD EN ARCHIVO
     * Serializa el AFD actual a un archivo
     *
     * ⚠️ PROBLEMA: SaveAFD() está vacío en AFD.java
     */
    public String guardarAFD(String nombreArchivo) {
        // VALIDACIÓN
        if (afdActual == null) {
            return "Error: No hay AFD para guardar";
        }

        try {
            // GUARDAR (⚠️ Método vacío en AFD.java)
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
     * CARGAR AFD DESDE ARCHIVO
     * Deserializa un AFD desde un archivo
     *
     * ⚠️ PROBLEMA: LoadAFD() está vacío en AFD.java
     */
    public String cargarAFD(String nombreArchivo) {
        try {
            afdActual = new AFD();

            // CARGAR (⚠️ Método vacío en AFD.java)
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

    // ========== MÉTODOS AUXILIARES ==========

    /**
     * OBTENER LISTA DE AFNs
     * Retorna los nombres de todos los AFNs creados
     * Para mostrar en listas desplegables de la GUI
     */
    public ArrayList<String> obtenerListaAFNs() {
        return new ArrayList<>(automatasAFN.keySet());
    }

    /**
     * OBTENER LISTA DE AFDs
     * Retorna los nombres de todos los AFDs creados
     */
    public ArrayList<String> obtenerListaAFDs() {
        return new ArrayList<>(automatasAFD.keySet());
    }

    /**
     * LIMPIAR TODO
     * Borra todos los autómatas creados
     * Útil para empezar de cero
     */
    public void limpiarTodo() {
        automatasAFN.clear();     // Vacía HashMap de AFNs
        automatasAFD.clear();     // Vacía HashMap de AFDs
        afdActual = null;         // Quita AFD actual
    }

    /**
     * HAY AFD ACTIVO
     * Verifica si hay un AFD seleccionado
     * La GUI lo usa para habilitar/deshabilitar botones
     */
    public boolean hayAFDActivo() {
        return afdActual != null;
    }

    /**
     * ESTABLECER AFD ACTUAL
     * Cambia el AFD activo por otro del HashMap
     * Útil cuando hay múltiples AFDs creados
     */
    public String establecerAFDActual(String nombre) {
        // VALIDACIÓN
        if (!automatasAFD.containsKey(nombre)) {
            return "Error: El AFD no existe";
        }

        // CAMBIAR AFD ACTUAL
        afdActual = automatasAFD.get(nombre);
        analizador.setAutomata(afdActual);

        return "AFD '" + nombre + "' establecido como actual";
    }
}