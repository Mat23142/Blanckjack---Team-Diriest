import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

/**
 * Clase principal que CONTIENE todas las demás clases del modelo de dominio
 * como clases anidadas. Esto es para Sprints 1-4 (consola).
 */
public class Juego {

    // --- Atributos de la clase Juego ---
    private Baraja baraja;
    private Jugador jugador;
    private Crupier crupier;
    private Scanner scanner;
    private List<Apuesta> apuestasActivas; // Para Sprint 4
    private int apuestaPrincipal; // Para Sprint 3
    }
    // --- SPRINT 1: ENUMS Y CLASES DE DOMINIO FUNDAMENTALES (ANIDADAS) ---

    /**
     * Enum para los Palos de la baraja.
     */
    public enum Palo {
        CORAZONES, DIAMANTES, PICAS, TREBOLES
    }

    /**
     * Enum para los Rangos, almacena su valor.
     */
    public enum Rango {
        AS(11), DOS(2), TRES(3), CUATRO(4), CINCO(5), SEIS(6), SIETE(7),
        OCHO(8), NUEVE(9), DIEZ(10), JOTA(10), REINA(10), REY(10);

        private final int valor;
        Rango(int valor) { this.valor = valor; }
        public int getValor() { return valor; }
    }

    /**
     * Clase de datos para una Carta individual.
     */
    public static class Carta {
        private final Palo palo;
        private final Rango rango;

        public Carta(Palo palo, Rango rango) {
            this.palo = palo;
            this.rango = rango;
        }
        public Rango getRango() { return rango; }
        public Palo getPalo() { return palo; }
        @Override
        public String toString() { return rango + " de " + palo; }
    }

    /**
     * Clase para la Mano de un participante.
     * Contiene la lógica clave del valor dual del As[cite: 64, 150].
     */
    public static class Mano {
        private List<Carta> cartas = new ArrayList<>();

        public void agregarCarta(Carta carta) { cartas.add(carta); }
        public void limpiar() { cartas.clear(); }
        public List<Carta> getCartas() { return cartas; }

        public int getValor() {
            int valorTotal = 0;
            int numAses = 0;
            for (Carta carta : cartas) {
                valorTotal += carta.getRango().getValor();
                if (carta.getRango() == Rango.AS) numAses++;
            }
            // Lógica del As
            while (valorTotal > 21 && numAses > 0) {
                valorTotal -= 10;
                numAses--;
            }
            return valorTotal;
        }

        public boolean esBlackjack() { return cartas.size() == 2 && getValor() == 21; }
        public boolean sePaso() { return getValor() > 21; }
        @Override
        public String toString() { return cartas.toString() + " (Valor: " + getValor() + ")"; }
    }

    /**
     * Clase para la Baraja, usa múltiples mazos[cite: 58].
     */
    public static class Baraja {
        private List<Carta> cartas = new ArrayList<>();

        public Baraja(int numMazos) {
            // TODO: Llenar la lista 'cartas' con 'numMazos'
            // (un bucle por numMazos, bucles anidados por Palo y Rango)
            for (int i = 0; i < numMazos; i++) {
                for (Palo p : Palo.values()) {
                    for (Rango r : Rango.values()) {
                        cartas.add(new Carta(p, r));
                    }
                }
            }
        }
        public void barajar() { Collections.shuffle(cartas); }
        public Carta repartirCarta() { return cartas.remove(0); }
    }

    // --- SPRINT 2: CLASES DE PARTICIPANTES (ANIDADAS) ---

    /**
     * Clase abstracta para comportamiento común[cite: 186].
     */
    public abstract static class Participante {
        protected Mano mano = new Mano();
        public Mano getMano() { return mano; }
        public void recibirCarta(Carta carta) { mano.agregarCarta(carta); }
        public void limpiarMano() { mano.limpiar(); }
        public int getValorMano() { return mano.getValor(); }
        public boolean sePaso() { return mano.sePaso(); }
    }

    /**
     * Clase para el Jugador.
     */
    public static class Jugador extends Participante {
        // TODO SPRINT 3: Añadir 'private int saldo;'
        privat int saldo;
        public Jugador() { super(); this.saldo = 1000; } // Saldo inicial ejemplo
        // TODO SPRINT 3: Añadir métodos para gestionar saldo
        public int getSaldo() { return saldo; }
        public void ajustarSaldo(int delta) { this.saldo += delta; }
        public boolean puedeApostar(int monto) { return monto > 0 && monto <= saldo; }
    }

    /**
     * Clase para el Crupier, con su lógica fija[cite: 74, 161].
     */
    public static class Crupier extends Participante {
        public Crupier() { super(); }
        public Carta getCartaVisible() { return mano.getCartas().get(0); }

        public void jugarTurno(Baraja baraja) {
            System.out.println("Turno del Crupier. Mano: " + mano);
            while (getValorMano() < 17) { // Regla: plantarse con 17 o más
                System.out.println("Crupier pide carta...");
                recibirCarta(baraja.repartirCarta());
                System.out.println("Mano del Crupier: " + mano);
            }
            System.out.println("Crupier se planta.");
        }
    }

    // --- SPRINT 4: SISTEMA DE APUESTAS MODULAR (INTERFAZ Y CLASES ANIDADAS) ---

    /**
     * Interfaz para el diseño modular de apuestas[cite: 30].
     */
    public interface Apuesta {
        boolean evaluar(Mano manoJugador, Carta cartaVisibleCrupier, Mano manoCrupierCompleta);
        int calcularPago();
        void setMonto(int monto);
        int getMonto();
    }

    // TODO: Implementar las clases ApuestaSeguro, ApuestaParesPerfectos, Apuesta21mas3
    // Ejemplo de ApuestaSeguro [cite: 85, 86]
    public static class ApuestaSeguro implements Apuesta {
        private int monto;
        @Override
        public boolean evaluar(Mano manoJugador, Carta cartaVisibleCrupier, Mano manoCrupierCompleta) {
            return manoCrupierCompleta.esBlackjack();
        }
        @Override
        public int calcularPago() { return monto * 3; /* Pago 2:1 */ }
        @Override
        public void setMonto(int monto) { this.monto = monto; }
        @Override
        public int getMonto() { return monto; }
    }
    
    // TODO: Implementar ApuestaParesPerfectos [cite: 87, 88]
    // TODO: Implementar Apuesta21mas3 [cite: 89, 90]


    // --- LÓGICA PRINCIPAL DEL JUEGO (Sprints 2 y 3) ---

    public Juego() {
        this.baraja = new Baraja(6); // 6 mazos [cite: 58]
        this.jugador = new Jugador();
        this.crupier = new Crupier();
        this.scanner = new Scanner(System.in);
        this.apuestasActivas = new ArrayList<>(); // Sprint 4
    }

    public void iniciar() {
        baraja.barajar();
        while (true) {
            jugarRonda();
            System.out.println("¿Jugar otra ronda? (s/n)");
            if (scanner.nextLine().equalsIgnoreCase("n")) break;
        }
        System.out.println("Gracias por jugar.");
    }

    private void jugarRonda() {
        jugador.limpiarMano();
        crupier.limpiarMano();
        apuestasActivas.clear();
        apuestaPrincipal = 0;

        // TODO SPRINT 3: Pedir apuesta principal
        System.out.println("Tu saldo actual es: " + jugador.getSaldo());
        while (true) {
            System.out.println("Ingresa tu apuesta principal:");
            String linea = scanner.nextLine();
            try{
                int monto = Integer.parseInt(linea.trim());
                if (!jugador.puedeApostar(monto)){
                    System.out.println("Apuesta inválida. Intenta de nuevo.");
                    continue;
                }
                apuestaPrincipal = monto;
                jugador.ajustarSaldo(-monto);
                break;
            } catch (NumberFormatException e){
                System.out.println("Entrada inválida. Ingresa un número.");
            }
        }
        // Reparto inicial
        jugador.recibirCarta(baraja.repartirCarta());
        crupier.recibirCarta(baraja.repartirCarta());
        jugador.recibirCarta(baraja.repartirCarta());
        crupier.recibirCarta(baraja.repartirCarta());

        System.out.println("Mano del Jugador: " + jugador.getMano());
        System.out.println("Carta visible del Crupier: " + crupier.getCartaVisible());

        // TODO SPRINT 4: Ofrecer Apuestas Especiales (Seguro, 21+3, Pares)
        // if (crupier.getCartaVisible().getRango() == Rango.AS) { ... ofrecer seguro ... }
        // ... pedir apuesta 21+3 ...
        // ... pedir apuesta Pares Perfectos ...
        //Ofrecer seguro
        if (crupier.getCartaVisible().getRango() == Rango.AS) {
            System.out.println("¿Deseas hacer una apuesta de seguro? (s/n)");
            String respuesta = scanner.nextLine();
            if (respuesta.equalsIgnoreCase("s")) {
                ApuestaSeguro apuestaSeguro = new ApuestaSeguro();
                while (true) {
                    System.out.println("Ingresa el monto para la apuesta de seguro (máximo " + (apuestaPrincipal / 2) + "):");
                    String linea = scanner.nextLine();
                    try {
                        int montoSeguro = Integer.parseInt(linea.trim());
                        if (montoSeguro > (apuestaPrincipal / 2) || !jugador.puedeApostar(montoSeguro)) {
                            System.out.println("Apuesta inválida. Intenta de nuevo.");
                            continue;
                        }
                        apuestaSeguro.setMonto(montoSeguro);
                        jugador.ajustarSaldo(-montoSeguro);
                        apuestasActivas.add(apuestaSeguro);
                        break;
                    } catch (NumberFormatException e) {
                        System.out.println("Entrada inválida. Ingresa un número.");
                    }
                }
            }
            System.out.println("Deseas apostar a Pares Perfectos? (s/n)");
            String lineaPares = scanner.nextLine().trim();
            try {
                int montoPares = Integer.parseInt(lineaPares);
                if (montoPares > 0 && jugador.puedeApostar(montoPares)) {
                    ApuestaParesPerfectos apuestaPares = new ApuestaParesPerfectos(){
                    private int monto;
                    @Override
                    public boolean evaluar(Mano manoJugador, Carta cartaVisibleCrupier, Mano manoCrupierCompleta) {
                        List<Carta> cartas = manoJugador.getCartas();
                        if (cartas.size() < 2) return false;
                        return cartas.get(0).getRango() == cartas.get(1).getRango() &&
                            cartas.get(0).getPalo() == cartas.get(1).getPalo();
                    }
                    @Override public int calcularPago() { return monto * 25; /* Pago 25:1 */ }
                    @Override public void setMonto(int monto) { this.monto = monto; }
                    @Override public int getMonto() { return monto; }
                };
                    apuestaPares.setMonto(montoPares);
                    jugador.ajustarSaldo(-montoPares);
                    apuestasActivas.add(apuestaPares);
                    System.out.println("Apuesta a Pares Perfectos realizada por: " + montoPares);
                } else if (montoPares > 0) {
                    System.out.println("Apuesta inválida. No se realizó la apuesta a Pares Perfectos.");
                }
            } catch (NumberFormatException ignired) {
                System.out.println("No se realizó la apuesta a Pares Perfectos.");
        }

        System.out.println("¿Deseas apostar a 21+3? (s/n)");
        String linea21mas3 = scanner.nextLine().trim();
        try {
            int monto21mas3 = Integer.parseInt(linea21mas3);
            if (monto21mas3 > 0 && jugador.puedeApostar(monto21mas3)) {
                Apuesta21mas3 apuesta21mas3 = new Apuesta21mas3(){
                private int monto;
                @Override
                public void setMonto(int monto) { this.monto = monto; }
                @Override   public int getMonto() { return monto; }
                @Override
                public boolean evaluar(Mano manoJugador, Carta cartaVisibleCrupier, Mano manoCrupierCompleta) {
                    List<Carta> cartas = manoJugador.getCartas();
                    if (cartas.size() < 2) return false;
                    Carta cartaCrupier = cartaVisibleCrupier;
                    Rango r1 = cartas.get(0).getRango();
                    Rango r2 = cartas.get(1).getRango();
                    Rango r3 = cartaCrupier.getRango();
                    // Verificar combinaciones
                    boolean esTrio = (r1 == r2) && (r2 == r3);
                    boolean esEscalera = (Math.abs(r1.getValor() - r2.getValor()) == 1 &&
                                        Math.abs(r2.getValor() - r3.getValor()) == 1) ||
                                        (Math.abs(r1.getValor() - r3.getValor()) == 1 &&
                                        Math.abs(r3.getValor() - r2.getValor()) == 1) ||
                                        (Math.abs(r2.getValor() - r1.getValor()) == 1 &&
                                        Math.abs(r1.getValor() - r3.getValor()) == 1);
                    boolean esColor = (cartas.get(0).getPalo() == cartas.get(1).getPalo()) &&
                                    (cartas.get(1).getPalo() == cartaCrupier.getPalo());
                    return esTrio || esEscalera || esColor;
                }
                @Override public int calcularPago() { return monto * 9; /* Pago 9:1 */ }
            };
                apuesta21mas3.setMonto(monto21mas3);
                jugador.ajustarSaldo(-monto21mas3);
                apuestasActivas.add(apuesta21mas3);
                System.out.println("Apuesta a 21+3 realizada por: " + monto21mas3);
            } else if (monto21mas3 > 0) {
                System.out.println("Apuesta inválida. No se realizó la apuesta a 21+3.");
            }
        } catch (NumberFormatException ignired) {
            System.out.println("No se realizó la apuesta a 21+3.");
        }
        // Turno del Jugador
        turnoJugador();

        // Turno del Crupier
        if (!jugador.sePaso()) {
            crupier.jugarTurno(baraja);
        }
        
        // Determinar ganador
        determinarGanador();
    }

    private void turnoJugador() {
        while (true) {
            System.out.println("¿Qué deseas hacer? (1: Pedir, 2: Plantarse)");
            // TODO SPRINT 3: Añadir opciones 3: Doblar, 4: Dividir [cite: 71, 72]
            
            String opcion = scanner.nextLine();
            if (opcion.equals("1")) { // Pedir (Hit) [cite: 69]
                jugador.recibirCarta(baraja.repartirCarta());
                System.out.println("Mano del Jugador: " + jugador.getMano());
                if (jugador.sePaso()) {
                    System.out.println("¡Te has pasado!");
                    break;
                }
            } else if (opcion.equals("2")) { // Plantarse (Stand) [cite: 70]
                System.out.println("Jugador se planta.");
                break;
            } 
        }
    }
    
    private void determinarGanador() {
        System.out.println("Mano final Jugador: " + jugador.getMano());
        System.out.println("Mano final Crupier: " + crupier.getMano());
        int valorJugador = jugador.getValorMano();
        int valorCrupier = crupier.getValorMano();
        // TODO SPRINT 3: Implementar lógica de comparación y pagos (1:1 o 3:2) [cite: 75, 79]
        if (jugador.sePaso()) {
            System.out.println("Crupier gana.");
        } else if (crupier.sePaso()) {
            System.out.println("¡Jugador gana!");
            jugador.ajustarSaldo(apuestaPrincipal * 2); // Pagar al jugador
        } else if (jugador.getMano().esBlackjack()) {
            if (crupier.getMano().esBlackjack()) {
                System.out.println("Empate con Blackjack.");
                // Devolver apuesta al jugador
            } else {
                System.out.println("¡Jugador gana con Blackjack!");
                jugador.ajustarSaldo((int)(apuestaPrincipal * 2.5)); // Pagar 3:2
            }
        } else { 
            if (valorJugador > valorCrupier) {
                System.out.println("¡Jugador gana!");
                jugador.ajustarSaldo(apuestaPrincipal * 2); // Pagar al jugador
            } else if (valorJugador == valorCrupier) {
                System.out.println("Empate.");
                jugador.ajustarSaldo(apuestaPrincipal); // Devolver apuesta 
            } else {
                System.out.println("Jugador pierde.");
            }
        }
        System.out.println("Saldo actual del Jugador: " + jugador.getSaldo());
        // TODO SPRINT 4: Evaluar y pagar apuestas especiales
        for (Apuesta apuesta : apuestasActivas) {
            boolean gano = apuesta.evaluar(jugador.getMano(), crupier.getCartaVisible(), crupier.getMano());
            if (gano) {
                int pago = apuesta.calcularPago();
                System.out.println("¡Apuesta especial gana! Pago: " + pago);
                jugador.ajustarSaldo(pago);
            } else {
                System.out.println("Apuesta especial pierde.");
            }
        }
    }

    /**
     * Punto de entrada para la aplicación de consola.
     */
    public static void main(String[] args) {
        Juego miJuego = new Juego();
        miJuego.iniciar();
    }
}