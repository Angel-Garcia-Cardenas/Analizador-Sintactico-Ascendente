import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

public class ASA implements Parser {
    private int i = 0;
    private boolean hayErrores = false;
    private Token preanalisis;
    private final List<Token> tokens;
    private Stack<Integer> estados;

    private static final Map<String, Integer> NO_TERMINALES_MAPA = new HashMap<>();
    private static final Map<String, Integer> SIMBOLOS_MAPA = new HashMap<>();

    static {
        NO_TERMINALES_MAPA.put("Q", 0);
        NO_TERMINALES_MAPA.put("D", 1);
        NO_TERMINALES_MAPA.put("T", 2);
        NO_TERMINALES_MAPA.put("P", 3);
        NO_TERMINALES_MAPA.put("A", 4);
        NO_TERMINALES_MAPA.put("A1", 5);
        NO_TERMINALES_MAPA.put("A2", 6);
        NO_TERMINALES_MAPA.put("T1", 7);
        NO_TERMINALES_MAPA.put("T2", 8);

        SIMBOLOS_MAPA.put("SELECT", 0);
        SIMBOLOS_MAPA.put("select", 0);
        SIMBOLOS_MAPA.put("id", 1);
        SIMBOLOS_MAPA.put("from", 2);
        SIMBOLOS_MAPA.put("distinct", 3);
        SIMBOLOS_MAPA.put(".", 4);
        SIMBOLOS_MAPA.put(",", 5);
        SIMBOLOS_MAPA.put("*", 6);
        SIMBOLOS_MAPA.put("Epsilon", 7);
        SIMBOLOS_MAPA.put("$", 8);
    }

    private static final int[][] ACCION = {
            //-1000 error e la gramatica
            //cambio de estado - numero positivo con el numero de estado
            //reduccion - numero negativo con el numero de la reduccion
            //| ESTADO | select |   id | from | distinct |   .   |   ,    |   *   | Epsilon |   $   |
            {     0,         2,   -1000, -1000,  -1000,    -1000,  -1000,   -1000,   -1000,   -1000},   // Estado 0
            {     1,     -1000,   -1000, -1000,  -1000,    -1000,  -1000,   -1000,   -1000,       0},   // Estado 1
            {     2,     -1000,      11, -1000,      6,    -1000,  -1000,       8,   -1000,   -1000},   // Estado 2
            {     3,     -1000,   -1000,     4,  -1000,    -1000,      5,   -1000,   -1000,   -1000},   // Estado 3
            {     4,     -1000,      14, -1000,  -1000,    -1000,  -1000,   -1000,   -1000,   -1000},   // Estado 4
            {     5,        -1,      -1,    -1,     -1,       -1,     15,      -1,      -1,      -1},   // Estado 5
            {     6,     -1000,      11, -1000,  -1000,    -1000,  -1000,       8,   -1000,   -1000},   // Estado 6
            {     7,        -2,      -2,    -2,     -2,       -2,     -2,      -2,      -2,      -2},   // Estado 7
            {     8,        -3,      -3,    -3,     -3,       -3,     -3,      -3,      -3,      -3},   // Estado 8
            {     9,        -4,      -4,    -4,     -4,       -4,     17,      -4,      -4,      -4},   // Estado 9
            {    10,        -5,      -5,    -5,     -5,       -5,     -5,      -5,      -5,      -5},   // Estado 10
            {    11,     -1000,   -1000, -1000,  -1000,       12,  -1000,   -1000,   -1000,   -1000},   // Estado 11
            {    12,     -1000,      19, -1000,  -1000,    -1000,  -1000,   -1000,   -1000,   -1000},   // Estado 12
            {    13,        -6,      -6,    -6,     -6,       -6,     -6,      -6,      -6,      -6},   // Estado 13
            {    14,     -1000,      21, -1000,  -1000,    -1000,  -1000,   -1000,   -1000,   -1000},   // Estado 14
            {    15,     -1000,   -1000, -1000,  -1000,    -1000,  -1000,   -1000,   -1000,   -1000},   // Estado 15
            {    16,        -7,      -7,    -7,     -7,       -7,     -7,      -7,      -7,      -7},   // Estado 16
            {    17,     -1000,      11, -1000,  -1000,    -1000,  -1000,   -1000,   -1000,   -1000},   // Estado 17
            {    18,        -8,      -8,    -8,     -8,       -8,     -8,      -8,      -8,      -8},   // Estado 18
            {    19,        -9,      -9,    -9,     -9,       -9,     -9,      -9,      -9,      -9},   // Estado 29
            {    20,       -10,     -10,   -10,    -10,      -10,    -10,     -10,     -10,     -10},   // Estado 20
            {    21,       -11,     -11,   -11,    -11,      -11,    -11,     -11,     -11,     -11},   // Estado 21
            {    22,       -12,     -12,   -12,    -12,      -12,    -12,     -12,     -12,     -12},   // Estado 22
            {    23,       -13,     -13,   -13,    -13,      -13,    -13,     -13,     -13,     -13}    // Estado 23



    };

    private static final int[][] IR_A = {

            // | ESTADO |   Q   |   D   |   T  |  P   |   A  |  A1  |   A2 |  T1  |  T2  |
            {       0,         1, -1000, -1000, -1000, -1000, -1000,  1000, -1000, -1000},   // Estado 0
            {       1,     -1000, -1000, -1000, -1000, -1000, -1000, -1000, -1000, -1000},   // Estado 1
            {       2,     -1000,     3, -1000,     7,     9,    10, -1000, -1000, -1000},   // Estado 2
            {       3,     -1000, -1000, -1000, -1000, -1000, -1000, -1000, -1000, -1000},   // Estado 3
            {       4,     -1000, -1000, -1000,     5, -1000, -1000, -1000,    13, -1000},   // Estado 4
            {       5,     -1000, -1000, -1000, -1000, -1000, -1000, -1000, -1000, -1000},   // Estado 5
            {       6,     -1000, -1000, -1000,    16,     9,    10, -1000, -1000, -1000},   // Estado 6
            {       7,     -1000, -1000, -1000, -1000, -1000, -1000, -1000, -1000, -1000},   // Estado 7
            {       8,     -1000, -1000, -1000, -1000, -1000, -1000, -1000, -1000, -1000},   // Estado 8
            {       9,     -1000, -1000, -1000, -1000, -1000, -1000, -1000, -1000, -1000},   // Estado 9
            {      10,     -1000, -1000, -1000, -1000, -1000, -1000, -1000, -1000, -1000},   // Estado 10
            {      11,     -1000, -1000, -1000, -1000, -1000, -1000,    18, -1000, -1000},   // Estado 11
            {      12,     -1000, -1000, -1000, -1000, -1000, -1000, -1000, -1000, -1000},   // Estado 12
            {      13,     -1000, -1000, -1000, -1000, -1000, -1000, -1000, -1000, -1000},   // Estado 13
            {      14,     -1000, -1000, -1000, -1000, -1000, -1000, -1000, -1000,    22},   // Estado 14
            {      15,     -1000, -1000, -1000, -1000, -1000, -1000, -1000,    23, -1000},   // Estado 15
            {      16,     -1000, -1000, -1000, -1000, -1000, -1000, -1000, -1000, -1000},   // Estado 16
            {      17,     -1000, -1000, -1000, -1000, -1000,    20, -1000, -1000, -1000},   // Estado 17
            {      18,     -1000, -1000, -1000, -1000, -1000, -1000, -1000, -1000, -1000},   // Estado 18
            {      19,     -1000, -1000, -1000, -1000, -1000, -1000, -1000, -1000, -1000},   // Estado 19
            {      20,     -1000, -1000, -1000, -1000, -1000, -1000, -1000, -1000, -1000},   // Estado 20
            {      21,     -1000, -1000, -1000, -1000, -1000, -1000, -1000, -1000, -1000},   // Estado 21
            {      22,     -1000, -1000, -1000, -1000, -1000, -1000, -1000, -1000, -1000},   // Estado 22
            {      23,     -1000, -1000, -1000, -1000, -1000, -1000, -1000, -1000, -1000},   // Estado 23
    };

    public ASA(List<Token> tokens) {
        this.tokens = tokens;
        preanalisis = this.tokens.get(i);
        estados = new Stack<>();
        //El estado inicial seun nuestra tabla de Excel es 0
        estados.push(0);
    }

    @Override
    public boolean parse() {
        Stack<String> simbolos = new Stack<>();
        simbolos.push("$");

        while (!simbolos.isEmpty() && !hayErrores) {
            int estadoActual = estados.peek();
            String simboloEntrada = obtenerSimboloEntrada();
            //Para la accion, nuestro actual es -1 por los indices de un array
            //para la dimension 2 sumamos 1 debido a la columna estado
            int accion = ACCION[estadoActual][obtenerIndiceSimbolo(simboloEntrada)];

            if (accion > 0) {
                // Desplazamiento
                simbolos.push(simboloEntrada);
                estados.push(accion);
                obtenerSiguienteSimbolo();
            } else if (accion < 0) {
                // Reducción
                reducir(-accion, simbolos);
            } else if (accion == 0) {
                // Aceptación
                System.out.println("Análisis sintáctico exitoso.");
                break;
            } else {
                // Error
                hayErrores = true;
                System.out.println("Error en el análisis sintáctico.");
                break;
            }
        }

        return !hayErrores;
    }

    private void reducir(int regla, Stack<String> simbolos) {
        switch (regla) {
            case 1:
                // Regla de reducción Q -> select D from T
                simbolos.pop();
                simbolos.pop();
                simbolos.pop();
                simbolos.pop();
                estados.pop();
                estados.pop();
                estados.pop();
                estados.pop();
                simbolos.push("Q");
                break;
            case 2:
                // Regla de reducción D -> P
                simbolos.pop();
                estados.pop();
                simbolos.push("D");
                break;
            case 3:
                // Regla de reducción P -> *
                simbolos.pop();
                estados.pop();
                simbolos.push("P");
                break;
            case 4:
                // Regla de reducción P -> A
                simbolos.pop();
                estados.pop();
                simbolos.push("P");
                break;
            case 5:
                // Regla de reducción A -> A1
                simbolos.pop();
                estados.pop();
                simbolos.push("A");
                break;
            case 6:
                // Regla de reducción T -> T1
                simbolos.pop();
                estados.pop();
                simbolos.push("T");
                break;
            case 7:
                // Regla de reducción D -> distinct P
                simbolos.pop();
                simbolos.pop();
                estados.pop();
                estados.pop();
                simbolos.push("D");
                break;
            case 8:
                // Regla de reducción A1 -> id A2
                simbolos.pop();
                simbolos.pop();
                estados.pop();
                estados.pop();
                simbolos.push("A1");
                break;
            case 9:
                // Regla de reducción A2 -> id
                simbolos.pop();
                estados.pop();
                simbolos.push("A2");
                break;
            case 10:
                // Regla de reducción A1 -> A, A1
                simbolos.pop();
                simbolos.pop();
                simbolos.pop();
                estados.pop();
                estados.pop();
                estados.pop();
                simbolos.push("A1");
                break;
            case 11:
                // Regla de reducción T2 -> id
                simbolos.pop();
                estados.pop();
                simbolos.push("T2");
                break;
            case 12:
                // Regla de reducción T1 -> id T2
                simbolos.pop();
                simbolos.pop();
                estados.pop();
                estados.pop();
                simbolos.push("T1");
                break;
            case 13:
                // Regla de reducción T -> T, T1
                simbolos.pop();
                simbolos.pop();
                simbolos.pop();
                estados.pop();
                estados.pop();
                estados.pop();
                simbolos.push("T2");
                break;
            case 1000:
                hayErrores=true;
        }

        // Lógica de GOTO
        //+1 al obtener el indice para saltar la columna de estado
        //-1 para moverse de acuerdo a los indices de java 0 = primer elemento
        int nuevoEstado = IR_A[estados.peek() - 1][obtenerIndiceNoTerminal(regla)];
        estados.push(nuevoEstado);
    }

    private int obtenerIndiceNoTerminal(int regla) {
        String noTerminal = obtenerNoTerminalPorRegla(regla);
        Integer indice = NO_TERMINALES_MAPA.get(noTerminal);

        if (indice == null) {
            throw new IllegalArgumentException("No se encontró el índice para el no terminal: " + noTerminal);
        }

        return indice;
    }

    private int obtenerIndiceSimbolo(String simbolo) {
        Integer indice = SIMBOLOS_MAPA.get(simbolo);

        if (indice == null) {
            throw new IllegalArgumentException("No se encontró el índice para el símbolo: " + simbolo);
        }

        return indice;
    }

    private String obtenerNoTerminalPorRegla(int regla) {
        switch (regla) {
            case 1:
                return "A";
            case 2:
                return "D";
            case 3:
                return "A2";
            case 4:
                return "T";
            case 5:
                return "P";
            case 6:
                return "A1";
            case 7:
                return "A2";
            case 8:
                return "T";
            case 9:
                return "T1";
            case 10:
                return "T2";
            case 11:
                return "T2";
            default:
                throw new IllegalArgumentException("Regla no válida");
        }
    }

    private String obtenerSimboloEntrada() {
        if (i < tokens.size()) {
            if (tokens.get(i).tipo.equals(TipoToken.IDENTIFICADOR)) {
                return "id";
            } else {
                return tokens.get(i).lexema;
            }
        }
        return "$";
    }

    private void obtenerSiguienteSimbolo() {
        if (i < tokens.size()-1) {
            i++;
            preanalisis = tokens.get(i);
        }
    }
}
