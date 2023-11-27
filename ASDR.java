import java.util.List;
import java.util.Stack;

public class ASDR implements Parser {

    private int i = 0;
    private boolean hayErrores = false;
    private Token preanalisis;
    private final List<Token> tokens;
    private final Stack<Integer> estados;

    public ASDR(List<Token> tokens) {
        this.tokens = tokens;
        preanalisis = this.tokens.get(i);
        estados = new Stack<>();
        estados.push(0); // Estado inicial
    }

    @Override
    public boolean parse() {
        while (true) {
            int estadoActual = estados.peek();
            Accion accion = obtenerAccion(estadoActual, preanalisis.tipo);

            if (accion == null) {
                hayErrores = true;
                System.out.println("Error: no hay acción definida para el estado " + estadoActual + " y el token " + preanalisis.tipo);
                break;
            }

            if (accion instanceof Desplazamiento) {
                Desplazamiento desplazamiento = (Desplazamiento) accion;
                estados.push(desplazamiento.getNuevoEstado());
                i++;
                preanalisis = tokens.get(i);
            } else if (accion instanceof Reduccion) {
                Reduccion reduccion = (Reduccion) accion;
                for (int j = 0; j < reduccion.getCantidadPop(); j++) {
                    estados.pop();
                }
                int estadoAnterior = estados.peek();
                int nuevoEstado = obtenerIr_A(estadoAnterior, reduccion.getNoTerminal());
                estados.push(nuevoEstado);
            } else if (accion instanceof Aceptacion) {
                if (!hayErrores) {
                    System.out.println("Consulta correcta");
                    return true;
                } else {
                    System.out.println("Se encontraron errores");
                    return false;
                }
            }
        }

        return false;
    }

        private Accion obtenerAccion(int estado, TipoToken tipoToken) {
        switch (estado) {
            case 1:
                if (tipoToken == TipoToken.SELECT) {
                    return new Desplazamiento(3); // S3
                }
                break;
            case 2:
                return new Aceptacion(); // ACC
            case 3:
                if (tipoToken == TipoToken.SELECT) {
                    return new Desplazamiento(10); // S10
                } else if (tipoToken == TipoToken.DISTINCT) {
                    return new Desplazamiento(7); // S7
                }
                break;
            case 4:
                if (tipoToken == TipoToken.COMA) {
                    return new Desplazamiento(5);
                }
                break;
            case 5:
                if (tipoToken == TipoToken.IDENTIFICADOR){
                    return new Desplazamiento(10);
                }
                break;
            case 6:
                if (tipoToken == TipoToken.FROM) {
                    return new Desplazamiento(17);
                }
                break;
            case 7:
                if (tipoToken == TipoToken.IDENTIFICADOR) {
                    return new Desplazamiento(10);
                }else if (tipoToken == TipoToken.ASTERISCO){
                    return new Desplazamiento(13);
                }
                break;
            case 8:
                if (tipoToken == TipoToken.FROM) {
                    return new Desplazamiento(17);
                }
                break;
            case 9:
                if (tipoToken == TipoToken.FROM) {
                    return new Desplazamiento(17);
                }
                break;
            case 10:
                if (tipoToken == TipoToken.FROM) {
                    return new Desplazamiento(17);
                }
                break;
            case 11:
                if (tipoToken == TipoToken.SELECT) {
                    return new Desplazamiento(3); // S3
                }
                break;
            case 12:
                return new Aceptacion(); // ACC
            case 13:
                if (tipoToken == TipoToken.SELECT) {
                    return new Desplazamiento(10); // S10
                } else if (tipoToken == TipoToken.DISTINCT) {
                    return new Desplazamiento(7); // S7
                }
                break;
            case 14:
                if (tipoToken == TipoToken.COMA) {
                    return new Desplazamiento(5);
                }
                break;
            case 15:
                if (tipoToken == TipoToken.IDENTIFICADOR){
                    return new Desplazamiento(10);
                }
                break;
            case 16:
                if (tipoToken == TipoToken.FROM) {
                    return new Desplazamiento(17);
                }
                break;
            case 17:
                if (tipoToken == TipoToken.FROM) {
                    return new Desplazamiento(17);
                }
                break;
            case 18:
                if (tipoToken == TipoToken.FROM) {
                    return new Desplazamiento(17);
                }
                break;
            case 19:
                if (tipoToken == TipoToken.FROM) {
                    return new Desplazamiento(17);
                }
                break;
            case 20:
                if (tipoToken == TipoToken.FROM) {
                    return new Desplazamiento(17);
                }
                break;
            case 21:
                if (tipoToken == TipoToken.FROM) {
                    return new Desplazamiento(17);
                }
                break;
            case 22:
                if (tipoToken == TipoToken.FROM) {
                    return new Desplazamiento(17);
                }
                break;
            case 23:
                if (tipoToken == TipoToken.FROM) {
                    return new Desplazamiento(17);
                }
                break;
            case 24:
                if (tipoToken == TipoToken.FROM) {
                    return new Desplazamiento(17);
                }
                break;
        }
        return null; // No hay acción definida
    }

    private int obtenerIr_A(int estado, TipoToken noTerminal) {
        // Aquí debes implementar la lógica para obtener el estado Ir_A
        // según el estado actual y el no terminal.
        // Puedes usar la información proporcionada en la tabla de Ir_A.

        // Ejemplo simplificado:
        if (estado == 2 && noTerminal == TipoToken.Q) {
            return 3; // Transición al estado 3 por 'Q'
        } else {
            return -1; // No hay transición definida
        }
    }
}

// Clase Accion
abstract class Accion {
}

// Clase Desplazamiento
class Desplazamiento extends Accion {
    private final int nuevoEstado;

    Desplazamiento(int nuevoEstado) {
        this.nuevoEstado = nuevoEstado;
    }

    int getNuevoEstado() {
        return nuevoEstado;
    }
}

// Clase Reduccion
class Reduccion extends Accion {
    private final TipoToken noTerminal;
    private final int cantidadPop;

    Reduccion(TipoToken noTerminal, int cantidadPop) {
        this.noTerminal = noTerminal;
        this.cantidadPop = cantidadPop;
    }

    TipoToken getNoTerminal() {
        return noTerminal;
    }

    int getCantidadPop() {
        return cantidadPop;
    }
}

// Clase Aceptacion
class Aceptacion extends Accion {
    // Puedes agregar más información si es necesario
}

