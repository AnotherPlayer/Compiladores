//3° Parcial

public class SymbolHoc {

    public String name;
    public EnumTipoSymbol TipoSymbol;
    public float value;
    public EnumBLTIN FuncPredef;

    public SymbolHoc(){

        this.name = "";
        //this.TipoSymbol = EnumTipoSymbol.VALOR;
        this.value = 0;
        //this.FuncPredef = EnumBLTIN.NINGUNA;

    }

    public SymbolHoc( String n, EnumTipoSymbol t, float v){

        this.name = n;
        this.TipoSymbol = t;
        this.value = v;

    }

    public SymbolHoc( String n, EnumTipoSymbol t, EnumBLTIN f ){

        this.name = n;
        this.TipoSymbol = t;
        this.FuncPredef = f;

    }

    public void SetSymbol( String n, EnumTipoSymbol t, EnumBLTIN f ){

        this.name = n;
        this.TipoSymbol = t;
        this.FuncPredef = f;

    }

    public void SetSymbol( String n, EnumTipoSymbol t, float v ){

        this.name = n;
        this.TipoSymbol = t;
        this.value = v;

    }
    
}
