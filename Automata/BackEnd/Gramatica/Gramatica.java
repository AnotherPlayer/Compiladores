package BackEnd;

//2° parcial

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashSet;
import java.util.HashMap;

public class Gramatica {

    public static final String FIN_CADENA = "$";

    public int numReglas;

    public ArrayList<LadoIzq> Reglas;      // P
    public ArrayList<SimbolG> Vn;          // No terminales
    public ArrayList<SimbolG> Vt;          // Terminales
    public SimbolG SimbIni;                // Símbolo inicial

    public Gramatica(){

        numReglas = 0;
        Reglas = new ArrayList<LadoIzq>();
        Vn = new ArrayList<SimbolG>();
        Vt = new ArrayList<SimbolG>();

    }

    public void moreReglas( LadoIzq Regla ){

        if( numReglas == 0 ){

            SimbIni = Regla.SimbIzq;

        }

        Reglas.add( Regla );
        numReglas++;


    }

    public boolean asignarToken( String nombreSimb, int nuevoToken ){

        // Identificar terminales y no terminales actuales
        obtenerVt( Reglas );
        obtenerVn( Reglas );

        // Verificar que el token no esté usado por otro símbolo distinto
        for( LadoIzq li : Reglas ){
            if( li.SimbIzq != null && li.SimbIzq.token == nuevoToken && !li.SimbIzq.NombSimb.equals( nombreSimb ) )
                return false;
            for( SimbolG s : li.LadoDerecho ){
                if( s.token == nuevoToken && !s.NombSimb.equals( nombreSimb ) )
                    return false;
            }
        }
        for( SimbolG s : Vn ){
            if( s.token == nuevoToken && !s.NombSimb.equals( nombreSimb ) )
                return false;
        }
        for( SimbolG s : Vt ){
            if( s.token == nuevoToken && !s.NombSimb.equals( nombreSimb ) )
                return false;
        }

        boolean actualizado = false;

        for( LadoIzq li : Reglas ){
            if( li.SimbIzq != null && li.SimbIzq.NombSimb.equals( nombreSimb ) ){
                li.SimbIzq.token = nuevoToken;
                actualizado = true;
            }
            for( SimbolG s : li.LadoDerecho ){
                if( s.NombSimb.equals( nombreSimb ) ){
                    s.token = nuevoToken;
                    actualizado = true;
                }
            }
        }

        for( SimbolG s : Vn ){
            if( s.NombSimb.equals( nombreSimb ) ){
                s.token = nuevoToken;
                actualizado = true;
            }
        }
        for( SimbolG s : Vt ){
            if( s.NombSimb.equals( nombreSimb ) ){
                s.token = nuevoToken;
                actualizado = true;
            }
        }

        return actualizado;
    }

    public void cargarGramatica(){

        // ====================== Primera sección ======================
        ArrayList<String> lineas = new ArrayList<String>();
        try{
            File archivo = localizarArchivoGramatica();
            if(archivo == null){
                throw new IOException("No se encontró Prueba_LL1.txt");
            }
            BufferedReader br = new BufferedReader( new FileReader( archivo ) );
            String linea;
            while( (linea = br.readLine()) != null ){
                if( linea.trim().isEmpty() )
                    continue;
                lineas.add( linea.trim() );
            }
            br.close();
        }catch(IOException e){
            e.printStackTrace();
            return;
        }

        // ====================== Segunda sección ======================
        int tokenActual = 10;
        for( String linea : lineas ){

            String[] partes = linea.split("->");
            if( partes.length < 2 )
                continue;

            String ladoIzqCadena = partes[0].trim();
            SimbolG simbIzq = new SimbolG( ladoIzqCadena, tokenActual, true );
            tokenActual += 10;
            LadoIzq reglaBase = new LadoIzq( simbIzq );

            String ladoDerCompleto = partes[1].trim();
            if( ladoDerCompleto.endsWith(";") )
                ladoDerCompleto = ladoDerCompleto.substring( 0, ladoDerCompleto.length() - 1 );

            String[] alternativas = ladoDerCompleto.split("\\|");
            for( int idxAlt = 0 ; idxAlt < alternativas.length ; idxAlt++ ){

                String alternativa = alternativas[idxAlt].trim();
                LadoIzq reglaActual = ( idxAlt == 0 ) ? reglaBase : new LadoIzq( simbIzq );

                if( alternativa.endsWith(";") )
                    alternativa = alternativa.substring( 0, alternativa.length() - 1 );

                if( !alternativa.isEmpty() ){
                    String[] simbolos = alternativa.split("\\s+");
                    for( String s : simbolos ){
                        if( s.trim().isEmpty() )
                            continue;
                        String limpio = s.trim();
                        if( limpio.endsWith(";") )
                            limpio = limpio.substring( 0, limpio.length() - 1 );

                        SimbolG simb = new SimbolG( limpio, tokenActual, false );
                        tokenActual += 10;
                        reglaActual.AgregarSimbolo( simb );
                    }
                }

                moreReglas( reglaActual );

            }

        }

        // ====================== Tercera sección ======================
        for( LadoIzq regla : Reglas ){
            for( SimbolG simbDer : regla.LadoDerecho ){
                for( LadoIzq reglaIzq : Reglas ){
                    if( simbDer.NombSimb.equals( reglaIzq.SimbIzq.NombSimb ) ){
                        simbDer.token = reglaIzq.SimbIzq.token;
                        simbDer.esTerminal = reglaIzq.SimbIzq.esTerminal;
                        break;
                    }
                }
            }
        }

    }

    private File localizarArchivoGramatica() {
        Path[] candidatos = new Path[] {
            Paths.get("Prueba_LL1.txt"),
            Paths.get("Automata/BackEnd/Prueba_LL1.txt"),
            Paths.get("BackEnd/Prueba_LL1.txt")
        };
        for(Path p : candidatos){
            if(Files.exists(p)){
                return p.toFile();
            }
        }
        return null;
    }

    public ArrayList<SimbolG> First(ArrayList<SimbolG> l){
        HashSet<String> visit = new HashSet<>();
        return FirstSecuencia(l, visit);
    }

    private ArrayList<SimbolG> FirstSecuencia(List<SimbolG> sec, HashSet<String> visit){
        ArrayList<SimbolG> R = new ArrayList<>();
        if(sec == null || sec.isEmpty()){
            return R;
        }

        for(int idx = 0; idx < sec.size(); idx++){
            SimbolG s = sec.get(idx);
            ArrayList<SimbolG> firstS = FirstSimbolo(s, visit);

            boolean contieneEpsilon = false;
            for(SimbolG f : firstS){
                if(esEpsilon(f)){
                    contieneEpsilon = true;
                    continue;
                }
                agregarSiNoEsta(R, f);
            }

            if(!contieneEpsilon){
                // No propaga epsilon, terminamos
                break;
            }

            // Si es el último y tenía epsilon, se agrega epsilon
            if(idx == sec.size() - 1){
                for(SimbolG f : firstS){
                    if(esEpsilon(f)){
                        agregarSiNoEsta(R, f);
                    }
                }
            }
        }

        return R;
    }

    private ArrayList<SimbolG> FirstSimbolo(SimbolG s, HashSet<String> visit){
        ArrayList<SimbolG> R = new ArrayList<>();

        if(s == null){
            return R;
        }

        if(s.esTerminal){
            agregarSiNoEsta(R, s);
            return R;
        }

        if(visit.contains(s.NombSimb)){
            return R;
        }
        visit.add(s.NombSimb);

        for(LadoIzq prod : Reglas){
            if(!prod.SimbIzq.NombSimb.equals(s.NombSimb)){
                continue;
            }
            R.addAll(FirstSecuencia(prod.LadoDerecho, visit));
        }

        return R;
    }
    
    public ArrayList<SimbolG> Follow(SimbolG s){
        ArrayList<SimbolG> vacio = new ArrayList<>();
        if(s == null || s.esTerminal){
            return vacio;
        }

        // Inicializar conjuntos de follow
        HashMap<String, ArrayList<SimbolG>> followMap = new HashMap<>();
        for(LadoIzq prod : Reglas){
            followMap.put(prod.SimbIzq.NombSimb, new ArrayList<SimbolG>());
        }

        // Agregar fin de cadena al símbolo inicial
        agregarSiNoEsta(followMap.get(SimbIni.NombSimb), new SimbolG(FIN_CADENA, SimbEspeciales.FIN, true));

        boolean cambio = true;
        while(cambio){
            cambio = false;

            for(LadoIzq prod : Reglas){
                List<SimbolG> beta = prod.LadoDerecho;
                for(int i = 0; i < beta.size(); i++){
                    SimbolG B = beta.get(i);
                    if(B.esTerminal){
                        continue;
                    }

                    List<SimbolG> resto = beta.subList(i + 1, beta.size());
                    ArrayList<SimbolG> firstResto = First(new ArrayList<>(resto));

                    for(SimbolG f : firstResto){
                        if(esEpsilon(f)){
                            continue;
                        }
                        if(agregarSiNoEsta(followMap.get(B.NombSimb), f)){
                            cambio = true;
                        }
                    }

                    boolean restoGeneraEpsilon = firstResto.stream().anyMatch(this::esEpsilon) || resto.isEmpty();
                    if(restoGeneraEpsilon){
                        ArrayList<SimbolG> followA = followMap.get(prod.SimbIzq.NombSimb);
                        if(followA != null){
                            for(SimbolG f : followA){
                                if(agregarSiNoEsta(followMap.get(B.NombSimb), f)){
                                    cambio = true;
                                }
                            }
                        }
                    }
                }
            }
        }

        ArrayList<SimbolG> res = followMap.getOrDefault(s.NombSimb, new ArrayList<SimbolG>());
        return res;
    }

    private boolean esEpsilon(SimbolG s){
        if(s == null){
            return false;
        }
        return "EPSILON".equalsIgnoreCase(s.NombSimb) || "EPS".equalsIgnoreCase(s.NombSimb) || s.token == SimbEspeciales.EPSILON;
    }

    private boolean agregarSiNoEsta(ArrayList<SimbolG> lista, SimbolG elem){
        if(lista == null || elem == null){
            return false;
        }
        for(SimbolG s : lista){
            if(s.NombSimb.equals(elem.NombSimb)){
                return false;
            }
        }
        lista.add(elem);
        return true;
    }

    public ArrayList<SimbolG> listToArray( List<SimbolG> l ){
        
        ArrayList<SimbolG> R = new ArrayList<SimbolG>();

        for( SimbolG s : l )
            R.add(s);
        
        return R;

    }
    
    public void obtenerVt( ArrayList<LadoIzq> Reglas ){

        for( LadoIzq li : Reglas ){
            for( SimbolG s : li.LadoDerecho ){

                if( s.esTerminal && !Vt.contains(s) )
                    Vt.add(s);

            }

        }

    }

    public void obtenerVn( ArrayList<LadoIzq> Reglas ){

        for( LadoIzq li : Reglas ){
            for( SimbolG s : li.LadoDerecho ){

                if( !s.esTerminal && !Vn.contains(s) )
                    Vn.add(s);

            }

        }

    }
    
    

}
