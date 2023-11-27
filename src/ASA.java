import java.util.List;
import java.util.Stack;

public class ASA implements Parser {

    private int i = 0;
    private boolean hayErrores = false;
    private Token preanalisis;
    private final List<Token> tokens;
    private final Stack<Integer> estados;
    private final Stack<Object> pilaValores;

    public ASA(List<Token> tokens) {
        this.tokens = tokens;
        this.preanalisis = this.tokens.get(i);
        this.estados = new Stack<>();
        this.pilaValores = new Stack<>();
        // Agrega el estado inicial a la pila de estados
        this.estados.push(1);
    }

    @Override
    public boolean parse() {
        while (true) {
            int estadoActual = this.estados.peek();
            Accion accion = obtenerAccion(estadoActual, preanalisis.tipo);
            if (accion == null) {
                // Manejar error
                System.out.println("Error en el análisis sintáctico");
                return false;
            }

            if (accion.tipo == TipoAccion.Desplazamiento) {
                // Realiza el desplazamiento y avanza al siguiente token
                this.estados.push(accion.siguienteEstado);
                this.pilaValores.push(preanalisis);
                i++;
                preanalisis = tokens.get(i);
            } else if (accion.tipo == TipoAccion.Reduccion) {
                // Realiza la reducción y actualiza el estado
                Reduccion reduccion = obtenerReduccion(accion.indiceReduccion);
                if (reduccion == null) {
                    // Manejar error
                    System.out.println("Error en el análisis sintáctico");
                    return false;
                }

                // Realiza la reducción y actualiza el estado
                for (int j = 0; j < reduccion.cantidad; j++) {
                    this.estados.pop();
                }

                int nuevoEstado = obtenerIrA(this.estados.peek(), reduccion.noTerminal);
                this.estados.push(nuevoEstado);
            } else if (accion.tipo == TipoAccion.Aceptacion) {
                // La cadena fue aceptada correctamente
                System.out.println("Consulta correcta");
                return true;
            }
        }
    }

    private Accion obtenerAccion(int estado, TipoToken tipoToken) {
        switch (estado) {
            case 1:
                if (tipoToken == TipoToken.SELECT) {
                    return new Accion(TipoAccion.Desplazamiento, 3, 0);
                } else {
                    // Manejar error
                    return null;
                }

            case 2:
                return new Accion(TipoAccion.Aceptacion, 0, 0);

            case 3:
                if (tipoToken == TipoToken.DISTINCT) {
                    return new Accion(TipoAccion.Desplazamiento, 7, 0);
                } else if (tipoToken == TipoToken.IDENTIFICADOR) {
                    return new Accion(TipoAccion.Desplazamiento, 10, 0);
                } else {
                    // Manejar error
                    return null;
                }

            case 4:
                if (tipoToken == TipoToken.COMA) {
                    return new Accion(TipoAccion.Desplazamiento, 5, 0);
                } else {
                    // Manejar error
                    return null;
                }

            case 5:
                if (tipoToken == TipoToken.IDENTIFICADOR) {
                    return new Accion(TipoAccion.Desplazamiento, 10, 0);
                } else {
                    // Manejar error
                    return null;
                }

            case 6:
                if (tipoToken == TipoToken.FROM) {
                    return new Accion(TipoAccion.Desplazamiento, 17, 0);
                } else {
                    // Manejar error
                    return null;
                }

            case 7:
                if (tipoToken == TipoToken.ASTERISCO) {
                    return new Accion(TipoAccion.Desplazamiento, 13, 0);
                } else if (tipoToken == TipoToken.IDENTIFICADOR) {
                    return new Accion(TipoAccion.Desplazamiento, 10, 0);
                } else {
                    // Manejar error
                    return null;
                }

            case 10:
                if (tipoToken == TipoToken.PUNTO) {
                    return new Accion(TipoAccion.Desplazamiento, 15, 0);
                }else if (tipoToken == TipoToken.EOF) {
                    return new Accion(TipoAccion.Desplazamiento, 11, 0);
                }  else {
                    // Manejar error
                    return null;
                }

            case 15:
                if (tipoToken == TipoToken.IDENTIFICADOR) {
                    return new Accion(TipoAccion.Desplazamiento, 16, 0);
                } else {
                    // Manejar error
                    return null;
                }

            case 17:
                if (tipoToken == TipoToken.IDENTIFICADOR) {
                    return new Accion(TipoAccion.Desplazamiento, 21, 0);
                } else {
                    // Manejar error
                    return null;
                }

            case 18:
                if (tipoToken == TipoToken.COMA) {
                    return new Accion(TipoAccion.Desplazamiento, 19, 0);
                } else {
                    // Manejar error
                    return null;
                }

            case 19:
                if (tipoToken == TipoToken.IDENTIFICADOR) {
                    return new Accion(TipoAccion.Desplazamiento, 21, 0);
                } else {
                    // Manejar error
                    return null;
                }
/*
            case 20:
                if (tipoToken == TipoToken.ASTERISCO || tipoToken == TipoToken.IDENTIFICADOR) {
                    return new Accion(TipoAccion.Reduccion, 4, 0);
                } else {
                    // Manejar error
                    return null;
                }
*/
            case 21:
                if (tipoToken == TipoToken.IDENTIFICADOR) {
                    return new Accion(TipoAccion.Desplazamiento, 24, 0);
                }else if (tipoToken == TipoToken.EOF) {
                    return new Accion(TipoAccion.Desplazamiento, 23, 0);
                }  else {
                    // Manejar error
                    return null;
                }

            default:
                // Manejar error
                return null;
        }
    }


    private Reduccion obtenerReduccion(int indiceReduccion) {
        switch (indiceReduccion) {
         /*   case 1:
                return new Reduccion(NoTerminal.S_PRIMA, 1);

            case 2:
                return new Reduccion(NoTerminal.Q, 3);

            case 3:
                return new Reduccion(NoTerminal.D, 3);

            case 4:
                return new Reduccion(NoTerminal.P, 1);

            case 5:
                return new Reduccion(NoTerminal.A, 3);

            case 6:
                return new Reduccion(NoTerminal.A1, 2);

            case 7:
                return new Reduccion(NoTerminal.A, 1);
*/
            case 8:
                return new Reduccion(NoTerminal.A1, 1);

            case 9:
                return new Reduccion(NoTerminal.D, 1);

      //      case 10:
        //        return new Reduccion(NoTerminal.A1, 3);

            case 11:
                return new Reduccion(NoTerminal.A2, 3);

            case 12:
                return new Reduccion(NoTerminal.A2, 1);

            case 13:
                return new Reduccion(NoTerminal.T, 1);

            case 14:
                return new Reduccion(NoTerminal.A1, 1);

//            case 15:
 //               return new Reduccion(NoTerminal.A2, 3);

            case 16:
                return new Reduccion(NoTerminal.A2, 1);
/*
            case 17:
                return new Reduccion(NoTerminal.Q, 1);

            case 18:
                return new Reduccion(NoTerminal.T, 2);

            case 19:
                return new Reduccion(NoTerminal.T1, 3);
*/
            case 20:
                return new Reduccion(NoTerminal.T, 2);

/*            case 21:
                return new Reduccion(NoTerminal.T1, 1);
*/
            case 22:
                return new Reduccion(NoTerminal.T1, 2);

            case 23:
                return new Reduccion(NoTerminal.T2, 1);

            case 24:
                return new Reduccion(NoTerminal.T2, 1);

            default:
                // Manejar error
                return null;
        }
    }

    private int obtenerIrA(int estado, NoTerminal noTerminal) {
        switch (estado) {
            case 1:
                if (noTerminal == NoTerminal.Q) {
                    return 2;
                } else {
                    // Manejar error
                    return -1;
                }

            case 3:
                if (noTerminal == NoTerminal.D) {
                    return 6;
                } else if (noTerminal == NoTerminal.P) {
                    return 9;
                } else if (noTerminal == NoTerminal.A) {
                    return 4;
                } else if (noTerminal == NoTerminal.A1) {
                    return 8;
                } else {
                    // Manejar error
                    return -1;
                }

            case 7:
                if (noTerminal == NoTerminal.A) {
                    return 4;
                } else if (noTerminal == NoTerminal.A1) {
                    return 8;
                } else {
                    // Manejar error
                    return -1;
                }

            case 17:
                if (noTerminal == NoTerminal.T) {
                    return 18;
                } else if (noTerminal == NoTerminal.T1) {
                    return 12;
                } else {
                    // Manejar error
                    return -1;
                }

            case 19:
                if (noTerminal == NoTerminal.T1) {
                    return 20;
                } else {
                    // Manejar error
                    return -1;
                }

            case 21:
                if (noTerminal == NoTerminal.T2) {
                    return 22;
                } else {
                    // Manejar error
                    return -1;
                }

            default:
                // Manejar error
                return -1;
        }
    }


    private enum TipoAccion {
        Desplazamiento,
        Reduccion,
        Aceptacion
    }

    private static class Accion {
        TipoAccion tipo;
        int siguienteEstado;
        int indiceReduccion;

        Accion(TipoAccion tipo, int siguienteEstado, int indiceReduccion) {
            this.tipo = tipo;
            this.siguienteEstado = siguienteEstado;
            this.indiceReduccion = indiceReduccion;
        }
    }

    private static class Reduccion {
        NoTerminal noTerminal;
        int cantidad;

        Reduccion(NoTerminal noTerminal, int cantidad) {
            this.noTerminal = noTerminal;
            this.cantidad = cantidad;
        }
    }

    private enum NoTerminal {
        S_PRIMA, Q, D, P, A, A1, A2, T, T1, T2
    }
}
