# Instruccioens para cargar una gramática
Primero debes considerar que para todo lo que te quiero pedir hagas lo siguiente:
- No crear clases nuevas
- No crear nuevas funciones dentro del mismo u otro archivo .java
- Solo dejar comentario que separen cada unas de las secciones que te pida
El algoritmo que implementaras entará en la función de "cargarGramatica" dentro del archivo Gramatica.java y estos son los pasos que debe seguir:

(Primera sección)
Primero la función deberá de leer un archivo .txt que está dentro de la carpeta BackEnd llamado "Prueba_LL1.txt"

(Segunda sección)
Cada línea del .txt representa una regla, por lo que cada salto de línea representa una regla nueva; para identificar que partes son un lado izquierdo y derecho consideraras lo siguiete:
1. El primer caracter o palabra representara al lado izquierdo de esa regla y cuando veas una flecha "->" significa que marca la separación entre el lado izquierdo y el derecho, por ende lo que sigue representa al lado derecho. Cuando inicialices un LadoIzq debes de hacerlo haciendo que el "NombSimb" sea igual a la cadena que se analizó, con un token de 10 y con su valor de "esTerminal" igual a verdadero"
2. Despues de la fleca "->" habrá una seríe de caracteres que representan al lado derecho y debes de considerar lo para idetificar si hay más de uno por lo que haras lo siguiente:
    - Cada caracter o palabra representa un SimbolG y para diferenciar entre diferentes SimbolG habra un espacio, entonces una sucesiones de caracteres representara un SimbolG y cuando veas un espacio significa que la siguiente cadena de caracteres representa a otro SimbolG. Este SimbolG se deberá inicializar haciendo que el "NombSimb" sea igual a la cadena que se analizó, con un token que incrementará en 10 en 10 conforme se creen nuevos SimbolG dentro de la Gramatica a partir del SimbolG que se haya creado anteriormente.
3. En caso de ver el siguiente simbolo "|" significa que se creará una nueva regla con un lado izquierdo igual al que se uso en la regla anterior y el lado derecho se hara con los caracteres que están después del "|", respetando todas las considiciones marcadas en el punto anterior.
4. Este proceso se repetira con cada regla dentor del archivo hasta que no haya más que leer.

(Tercera sección)
Ahora debemos de identificar que SimbolG si son terminales y cuales no, en un inicio hicimos que el valor de "esTerminal" de todos los SimbolG sea falso, pero ahora debemos identificar cuales si lo son.
Para ello verificaras cada SimbolG de cada Lado Derecho y lo compararás con los lados Izquierdo que ya existen, como estos se inicializaron como verdaderos en su variable de "esTerminal" solo compararemos ese SimbolG del lado derecho con todos los SimbolG del Lado izquierdo. En caso de que concidan se deberán de poner los valores de "token" y "esTerminal" en el SimbolG del lado derecho con el que se comparó el SimbolG del ladoIzq; en caso de que no concida con algún lado izquierdo se dejará como está.
Esto se hara con todos los SimbolG de todos los lados derechos hasta comṕarar con todos los SimbolG del Lado derecho de cada Lado Izquerdo.

Ejemplo:
Considernando la gramática del ejemplo:
G->Reglas;
Reglas->Regla PC ReglasP;
ReglasP->Regla PC ReglasP |EPSILON;
Regla->LadoIzq FLECHA LadosDerechos;
LadoIzq->SIMBOLO;
LadosDerechos->LadoDerecho LadosDerechosP
LadosDerechosP->LadoDerecho LadosDerechosP;
LadoDerecho->SecSimbolos;
SecSimbolos->SIMBOLO SecSimbolosP;
SecSimbolosP->SIMBOLO SecSimbolosP |EPSILON;

Usando la segunda regla:
ReglasP->Regla PC ReglasP |EPSILON;
Entonces "ReglaP" es el lado izquierdo de la regla, y se creará un SimbolG con: NomSimb=ReglaP, token = 10, esTerminal=true;
El lado derecho empieza después de la flecha "->"
Los caracteres "Regla" "PC" "ReglasP" representan cada uno diferentes SimbolG que formaran parte del ArrayList de LadoDerecho y se inicializaran de la siguiente forma
    Regla con: NomSimb=Regla, token = 20, esTerminal=false;
    PC con: NomSimb=PC, token = 30, esTerminal=false;
    ReglasP con: NomSimb=ReglasP, token = 40, esTerminal=false;

Entonces, considerando toda la gramática, los siguientes SimbolG deben de ser terminales:
    G, Reglas, ReglasP, Regla, LadoIzq, LadosDerechos, LadosDerechosP, LadoDerecho, SecSimbolos, SecSimbolosP.
Y el resto de SimbolG son no terminales