import java.util.Random;
import java.util.Scanner;

/**
 Picross (Nonogram)
 */
public class Tablero {

    // Declaración de arreglos principales

    // Tamaño fijo del tablero
    public static final int N = 5;

    // Para guardar la solución seleccionada
    private int[][] solucion;

    // Para guardar el estado actual del jugador
    private int[][] jugador;

    // Para guardar las pistas (hasta 3 bloques por fila/columna)
    private int[][] pistasFilas;
    private int[][] pistasColumnas;

    /**
     Tableros solución.
     Decidí poner 6 tableros diferentes y elegir uno al azar al empezar
     */
    private static final int[][][] SOLUCIONES = {

        {
            {1,1,0,1,0},
            {0,1,1,0,1},
            {1,0,1,1,1},
            {1,1,0,0,1},
            {0,1,1,0,0}
        },

        {
            {1,0,1,1,0},
            {1,1,1,0,0},
            {0,1,0,1,1},
            {1,0,1,0,1},
            {0,1,1,1,0}
        },

        {
            {0,1,1,1,0},
            {1,1,0,1,0},
            {1,0,1,1,0},
            {0,1,0,1,1},
            {1,1,0,0,1}
        },

        {
            {1,1,0,0,1},
            {0,1,1,1,0},
            {1,0,1,0,1},
            {1,1,0,1,0},
            {0,1,1,0,1}
        },

        {
            {0,1,0,1,1},
            {1,1,0,1,0},
            {1,0,1,1,0},
            {0,1,1,0,1},
            {1,1,1,0,0}
        },

        {
            {1,0,1,0,1},
            {0,1,1,1,0},
            {1,1,0,1,0},
            {0,1,0,1,1},
            {1,0,1,1,0}
        }
    };

    /**
     * Inicio del juego:
     * - se elige un tablero al azar
     * - se crea el tablero del jugador vacío
     * - se generan las pistas
     */
    public Tablero() {
        solucion = seleccionarSolucionAleatoria();
        jugador = new int[N][N];              
        pistasFilas = new int[N][3];
        pistasColumnas = new int[N][3];
        generarPistas();
    }

    // Selecciona un tablero al azar
    private int[][] seleccionarSolucionAleatoria() {
        Random r = new Random();
        int indice = r.nextInt(SOLUCIONES.length);
        return copiarMatriz(SOLUCIONES[indice]);
    }

    // Copia una matriz para no modificar el arreglo fijo accidentalmente
    private int[][] copiarMatriz(int[][] original) {
        int[][] copia = new int[N][N];
        for (int f = 0; f < N; f++) {
            for (int c = 0; c < N; c++) {
                copia[f][c] = original[f][c];
            }
        }
        return copia;
    }

    // Generación de pistas, usando las soluciones ya establecidas arriba
    private void generarPistas() {

        // Pistas de filas
        for (int f = 0; f < N; f++) {
            pistasFilas[f] = contarBloquesAlineados(solucion[f]);
        }

        // Pistas de columnas
        for (int c = 0; c < N; c++) {
            int[] columna = new int[N];
            for (int f = 0; f < N; f++) {
                columna[f] = solucion[f][c];
            }
            pistasColumnas[c] = contarBloquesAlineados(columna);
        }
    }

    /**
     * Cuenta los bloques consecutivos de 1's.
     * Devuelve un arreglo de 3 posiciones alineando el tablero.
     */
    private int[] contarBloquesAlineados(int[] linea) {

        int[] temporal = new int[3];
        int contador = 0;
        int idx = 0;

        for (int i = 0; i < N; i++) {
            if (linea[i] == 1) {
                contador++;
            } else {
                if (contador > 0) {
                    if (idx < 3) {
                        temporal[idx] = contador;
                        idx++;
                    }
                    contador = 0;
                }
            }
        }

        if (contador > 0 && idx < 3) {
            temporal[idx] = contador;
            idx++;
        }

        int[] resultado = new int[3];
        int desplazamiento = 3 - idx;
        for (int i = 0; i < idx; i++) {
            resultado[i + desplazamiento] = temporal[i];
        }

        return resultado;
    }

    // Jugadas
    public void marcar(int fila, int columna) {
        jugador[fila][columna] = 1;
    }

    public void borrar(int fila, int columna) {
        jugador[fila][columna] = 0;
    }

    public boolean coordenadaValida(int fila, int columna) {
        return fila >= 0 && fila < N && columna >= 0 && columna < N;
    }

    // Valida la victoria 
    public boolean hayVictoria() {
        for (int f = 0; f < N; f++) {
            for (int c = 0; c < N; c++) {
                if (jugador[f][c] != solucion[f][c]) {
                    return false;
                }
            }
        }
        return true;
    }

    // Tablero y pistas
    public void mostrar() {

        final int ANCHO_CELDA = 4;
        final int ANCHO_PISTA = 3;
        final int ANCHO_ETIQUETA = 5;
        final int ANCHO_IZQUIERDO = (3 * ANCHO_PISTA) + 1 + ANCHO_ETIQUETA;

        // Pistas de columnas
        for (int nivel = 0; nivel < 3; nivel++) {
            System.out.printf("%" + ANCHO_IZQUIERDO + "s", "");
            for (int c = 0; c < N; c++) {
                int valor = pistasColumnas[c][nivel];
                String s = (valor == 0) ? "" : String.valueOf(valor);
                System.out.printf("%" + ANCHO_CELDA + "s", s);
            }
            System.out.println();
        }

        System.out.printf("%" + ANCHO_IZQUIERDO + "s", "");
        for (int c = 1; c <= N; c++) {
            System.out.printf("%" + ANCHO_CELDA + "s", "[" + c + "]");
        }
        System.out.println();

        // Separador
        System.out.printf("%" + ANCHO_IZQUIERDO + "s", "");
        for (int c = 0; c < N; c++) {
            System.out.printf("%" + ANCHO_CELDA + "s", "----");
        }
        System.out.println();

        // Filas
        for (int f = 0; f < N; f++) {

            // Pistas de fila
            for (int i = 0; i < 3; i++) {
                int valor = pistasFilas[f][i];
                String s = (valor == 0) ? "" : String.valueOf(valor);
                System.out.printf("%" + ANCHO_PISTA + "s", s);
            }

            System.out.printf(" %" + (ANCHO_ETIQUETA - 2) + "s", "[" + (f + 1) + "]");
            System.out.print(" |");

            for (int c = 0; c < N; c++) {
                String celda = (jugador[f][c] == 1) ? " X " : " . ";
                System.out.print(celda);
            }

            System.out.println();
        }
    }

    // En caso de escribir letras en lugar de numeros
    private static int leerEntero(Scanner sc, String mensaje) {
        while (true) {
            System.out.print(mensaje);
            if (sc.hasNextInt()) {
                return sc.nextInt();
            } else {
                System.out.println("Entrada inválida. Escribe un número.");
                sc.next();
            }
        }
    }

    // Main
    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        Tablero juego = new Tablero();

        System.out.println("=== PICROSS 5x5 ===");
        System.out.println("X = marcada | . = vacía");
        System.out.println("Pistas: cada número indica un bloque de X consecutivas.");
        System.out.println("Si hay dos números, hay al menos un '.' entre los bloques.");
        System.out.println();

        while (true) {

            juego.mostrar();
            System.out.println();

            if (juego.hayVictoria()) {
                System.out.println("¡Victoria! Has resuelto el rompecabezas.");
                break;
            }

            int fila = leerEntero(sc, "Fila (1-5): ");
            int columna = leerEntero(sc, "Columna (1-5): ");

            System.out.println("1) Marcar");
            System.out.println("2) Borrar");
            int accion = leerEntero(sc, "Elige (1-2): ");

            int f0 = fila - 1;
            int c0 = columna - 1;

            if (!juego.coordenadaValida(f0, c0)) {
                System.out.println("Coordenadas fuera del tablero.\n");
                continue;
            }

            if (accion == 1) {
                juego.marcar(f0, c0);
            } else if (accion == 2) {
                juego.borrar(f0, c0);
            } else {
                System.out.println("Acción inválida.\n");
                continue;
            }

            System.out.println();
        }

        sc.close();
    }
}







    










