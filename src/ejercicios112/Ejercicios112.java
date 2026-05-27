package ejercicios112;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Scanner;

/**
 *
 * @author alvin
 */
public class Ejercicios112 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3307/NBA";
        String user = "root";
        String pass = "";
        try (Connection conn = DriverManager.getConnection(url, user, pass);//
                //   PreparedStatement pstmt = conn.prepareStatement("INSERT INTO NBA "
                //        + "(`nombre`, `poder`, `Procedencia`, `Activo`) VALUES (?, ?, ?, ?)");//
                //     PreparedStatement pstmtUpdate = conn.prepareStatement("UPDATE personajesdb SET nombre=?, poder=?, Procedencia=?, Activo=?"); //
                 Statement stmtJugador = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);//
                 ResultSet rsJugador = stmtJugador.executeQuery("SELECT * FROM  jugadores"); //
                 Statement stmtEstadisticas = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE); //
                 ResultSet rsEstadisticas = stmtEstadisticas.executeQuery("SELECT * FROM  estadisticas"); Statement stmtPartidos = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE); //
                 ResultSet rsPartidos = stmtPartidos.executeQuery("SELECT * FROM  partidos");) { //
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("La conexión se ha establecido");

            System.out.println("\n===========================");

            //Jugadores mas altos y bajos de la NBA
            Scanner reader = new Scanner(System.in);
            HashMap<String, Double> jugadorAlto = new HashMap<>();
            HashMap<String, Double> jugadorBajo = new HashMap<>();
            String conversionAltura = "";
            double alturaCms = 0;

            while (rsJugador.next()) {

                conversionAltura = rsJugador.getString(4);
                String[] arrayConversionAltura = conversionAltura.split("-");
                alturaCms = (double) Math.round((Integer.parseInt(arrayConversionAltura[0]) * 30.48) + ((Integer.parseInt(arrayConversionAltura[1]) * 2.54)));

                jugadorAlto.put(rsJugador.getString(2), alturaCms);
                jugadorBajo.put(rsJugador.getString(2), alturaCms);
            }

            double alturaComparacionMasAltos = 0;
            double alturaComparacionMasBajos = 100000;
            String nombre = "";
            HashMap<String, Double> topAltos = new HashMap<>();
            HashMap<String, Double> topBajos = new HashMap<>();
            for (String h : jugadorAlto.keySet()) {
                if (jugadorAlto.get(h) >= alturaComparacionMasAltos) {
                    alturaComparacionMasAltos = jugadorAlto.get(h);
                }

            }
            for (String h : jugadorBajo.keySet()) {

                if (jugadorBajo.get(h) <= alturaComparacionMasBajos) {
                    alturaComparacionMasBajos = jugadorBajo.get(h);
                }
            }
            //Ultimo bucle para sacar los jugadores mas altos y bajos 
            for (String h : jugadorAlto.keySet()) {
                if (jugadorAlto.get(h) == alturaComparacionMasAltos) {
                    topAltos.put(h, jugadorAlto.get(h));
                }
            }
            for (String h : jugadorBajo.keySet()) {
                if (jugadorBajo.get(h) == alturaComparacionMasBajos) {
                    topBajos.put(h, jugadorBajo.get(h));
                }
            }
            System.out.println("Jugadores o jugador mas altos de la NBA : ");
            for (String h : topAltos.keySet()) {
                System.out.println(h + " " + topAltos.get(h) + " cms\n");
            }
            System.out.println("Jugadores o jugador mas bajos de la NBA : ");
            for (String h : topBajos.keySet()) {
                System.out.println(h + " " + topBajos.get(h) + " cms\n");
            }

            //Jugadores de los lakers
            ArrayList<String> jugadoresLakers = new ArrayList<>();
            rsJugador.beforeFirst();
            while (rsJugador.next()) {

                rsJugador.getString(7);
                if (rsJugador.getString(7).equalsIgnoreCase("Lakers")) {
                    jugadoresLakers.add(rsJugador.getString(2));
                }

            }
            System.out.println("Jugadores de los Lakers:");
            for (String lakers : jugadoresLakers) {

                System.out.println(lakers);

            }
            //Jugadores con mas puntos por partido
            System.out.println("");
            double puntosMasAltos = 0;
            int id = 0;
            HashMap<String, Integer> jugadorMasPuntos = new HashMap<>();
            HashMap<Integer, Double> puntos = new HashMap<>();

            rsEstadisticas.beforeFirst();
            while (rsEstadisticas.next()) {
                if (puntos.containsKey(rsEstadisticas.getInt(2))) {

                } else {
                    puntos.put(rsEstadisticas.getInt(2), rsEstadisticas.getDouble(3));
                }
            }
            for (int punto : puntos.keySet()) {
                if (puntos.get(punto) > puntosMasAltos) {
                    puntosMasAltos = puntos.get(punto); //Puntos Mas altos de la liga
                    id = punto;//ID DEL JUGADOR CON MAS PUNTOS
                }
            }

            rsJugador.beforeFirst();
            while (rsJugador.next()) {
                if (rsJugador.getInt(1) == id) {
                    nombre = rsJugador.getString(2);
                    break;
                }
            }

            System.out.println("Jugador con mas puntos en la liga");
            System.out.println("Nombre: " + nombre + " puntos: " + puntosMasAltos);

            //Jugador cuyo apellido empieza por R
            HashMap<String, String> jugadoresEmpiezanJ = new HashMap<>();

            rsJugador.beforeFirst();
            while (rsJugador.next()) {
                String separar = rsJugador.getString(2);
                String[] nombreSeparado = separar.split(" ");
                if (nombreSeparado.length >= 2) { //Sin esta condicion salta exception porque alomejor no se separan por espacios
                    if (nombreSeparado[1].toUpperCase().charAt(0) == 'J') {
                        jugadoresEmpiezanJ.put(nombreSeparado[0], nombreSeparado[1]);
                    }
                }

            }
            for (String n : jugadoresEmpiezanJ.keySet()) {
                System.out.println("Nombre: " + n + " Apellido: " + jugadoresEmpiezanJ.get(n));

            }
            System.out.println("");
            String pais = "Spain";
            //Jugadores Españoles y listados de sus nombres y equipos
            rsJugador.beforeFirst();
            ArrayList<String> jugadorSpain = new ArrayList<>();
            while (rsJugador.next()) {
                if (pais.equalsIgnoreCase(rsJugador.getString(3))) {
                    jugadorSpain.add("Nombre: " + rsJugador.getString(2) + " Procedencia: " + rsJugador.getString(3) + " Equipo: " + rsJugador.getString(7));
                }

            }
            System.out.println("Jugadores Españoles :");
            for (String n : jugadorSpain) {
                System.out.println(n);
            }

            //Numero de puntos mas alto de cualquier equipo en un partido local o visitante
            rsPartidos.beforeFirst();
            HashMap<String, Integer> partidosLocal = new HashMap<>();
            HashMap<String, Integer> partidosVisitante = new HashMap<>();
            while (rsPartidos.next()) {

                partidosLocal.put(rsPartidos.getString(2), rsPartidos.getInt(4));
                partidosVisitante.put(rsPartidos.getString(3), rsPartidos.getInt(5));
            }
            int puntosLocal = 0;
            int puntosVisitante = 0;
            String nombreLocal = "";
            String nombreVisitante = "";

            for (String n : partidosLocal.keySet()) {
                if (partidosLocal.get(n) > puntosLocal) {
                    puntosLocal = partidosLocal.get(n);
                    nombreLocal = n;
                }
            }
            for (String n : partidosVisitante.keySet()) {
                if (partidosVisitante.get(n) > puntosVisitante) {
                    puntosVisitante = partidosVisitante.get(n);
                    nombreVisitante = n;
                }
            }
            System.out.println("");
            System.out.println("El equipo con mas puntos anotados en un solo partido es: ");
            if (puntosVisitante < puntosLocal) {
                System.out.println("Equipo: " + nombreLocal + " Puntos: " + puntosLocal);
            } else if (puntosVisitante == puntosLocal) {
                System.out.println("Equipo: " + nombreLocal + " Puntos: " + puntosLocal);
                System.out.println("Equipo: " + nombreVisitante + " Puntos: " + puntosVisitante);
            } else {
                System.out.println("Equipo: " + nombreVisitante + " Puntos: " + puntosVisitante);
            }

        } catch (Exception e) {
            System.out.println("Error: " + e);
        }

    }

}
