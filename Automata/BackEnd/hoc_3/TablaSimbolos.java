//3° Parcial

import java.util.ArrayList;
import java.util.List;

public class TablaSimbolos {

    ArrayList<SymbolHoc> ListaSimbolos;

    public TablaSimbolos(){

        ListaSimbolos = new ArrayList<SymbolHoc>();
        this.init();

    }

    public SymbolHoc buscar( String name ){

        SymbolHoc s;
        Iterator it = ListaSimbolos.iterator();

        while(it.hasNext()){

            s = (SymbolHoc) it.next();
            if(s.name.equals(name))
                return s;
            
        }

        return null;

    }

    public SymbolHoc instalar( String name,EnumTipoSymbol t, float v ){

        SymbolHoc s = new SymbolHoc();
        s.SetSymbol( name, t, EnumBLTIN.NINGUNA );

        ListaSimbolos.add( s );
        return s;

    }

    public SymbolHoc instalar( String name,EnumTipoSymbol t, EnumBLTIN f ){

        SymbolHoc s = new SymbolHoc();
        s.SetSymbol( name, t, f );

        ListaSimbolos.add( s );
        return s;

    }

    public void init(){

        ListaSimbolos.clear();
        InitConstPredef();
        InitFuncPredef();

    }

    public void InitConstPredef(){

        SymbolHoc s;
        float val;

        //PI
        s = new SymbolHoc();
        val = (float) 3.141592653589793;
        s.SetSymbol("PI", EnumTipoSymbol.CONST_PREDEF, val);
        ListaSimbolos.add(s);

        //E
        s = new SymbolHoc();
        val = (float) 2.718281828459045;
        s.SetSymbol("E", EnumTipoSymbol.CONST_PREDEF, val);
        ListaSimbolos.add(s);

        //Gamma
        s = new SymbolHoc();
        val = (float) 0.577215664901532;
        s.SetSymbol("GAMMA", EnumTipoSymbol.CONST_PREDEF, val);
        ListaSimbolos.add(s);

        //DEG
        s = new SymbolHoc();
        val = (float) 57.29577951308232;
        s.SetSymbol("DEG", EnumTipoSymbol.CONST_PREDEF, val);
        ListaSimbolos.add(s);

        //PHI
        s = new SymbolHoc();
        val = (float) 1.618033988749895;
        s.SetSymbol("PHI", EnumTipoSymbol.CONST_PREDEF, val);
        ListaSimbolos.add(s);

    }
    
    public void InitFuncPredef(){

        SymbolHoc s;

        //sin
        s = new SymbolHoc();
        s.SetSymbol("sin", EnumTipoSymbol.BLTIN, EnumBLTIN.SIN);
        ListaSimbolos.add(s);

        //cos
        s = new SymbolHoc();
        s.SetSymbol("cos", EnumTipoSymbol.BLTIN, EnumBLTIN.COS);
        ListaSimbolos.add(s);

        //atan
        s = new SymbolHoc();
        s.SetSymbol("atan", EnumTipoSymbol.BLTIN, EnumBLTIN.ATAN);
        ListaSimbolos.add(s);

        //log
        s = new SymbolHoc();
        s.SetSymbol("log", EnumTipoSymbol.BLTIN, EnumBLTIN.LOG);
        ListaSimbolos.add(s);

        //log10
        s = new SymbolHoc();
        s.SetSymbol("log10", EnumTipoSymbol.BLTIN, EnumBLTIN.LOG10);
        ListaSimbolos.add(s);

        //exp
        s = new SymbolHoc();
        s.SetSymbol("exp", EnumTipoSymbol.BLTIN, EnumBLTIN.EXP);
        ListaSimbolos.add(s);

        //sqrt
        s = new SymbolHoc();
        s.SetSymbol("sqrt", EnumTipoSymbol.BLTIN, EnumBLTIN.SQRT);
        ListaSimbolos.add(s);

        //int
        s = new SymbolHoc();
        s.SetSymbol("int", EnumTipoSymbol.BLTIN, EnumBLTIN.INT);
        ListaSimbolos.add(s);

        //abs
        s = new SymbolHoc();
        s.SetSymbol("abs", EnumTipoSymbol.BLTIN, EnumBLTIN.ABS);
        ListaSimbolos.add(s);

    }

}
