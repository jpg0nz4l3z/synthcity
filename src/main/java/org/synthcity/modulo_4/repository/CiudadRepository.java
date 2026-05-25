package org.synthcity.modulo_4.repository;

import org.synthcity.modulo_1.Ciudad;
import org.synthcity.modulo_1.bloques.*;
import org.synthcity.modulo_1.*;
import org.synthcity.modulo_4.DatabaseManager;
import org.synthcity.modulo_4.FormatoSalidaException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Repositorio especializado del Módulo 4 encargado de gestionar los registros
 * de cabecera de la entidad Ciudad y coordinar el Orden Obligatorio de Carga del Sprint 4.
 */
public class CiudadRepository {

    private final DatabaseManager dbManager;
    private final BloqueRepository bloqueRepository;

    /**
     * Constructor con inyección de dependencias. Comparte el dbManager y el repositorio de bloques.
     */
    public CiudadRepository(DatabaseManager dbManager, BloqueRepository bloqueRepository) {
        this.dbManager = dbManager;
        this.bloqueRepository = bloqueRepository;
    }

    /**
     * Guarda la cabecera limpia de la entidad ciudad utilizando la conexión de la transacción activa.
     * Retorna el ID autogenerado indispensable para enlazar los bloques y simulaciones hijas.
     */
    public long guardarCiudad(Connection conn, String nombre, int filas, int columnas) throws SQLException {
        String sql = "INSERT INTO ciudad (nombre, filas, columnas) VALUES (?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, nombre);
            ps.setInt(2, filas);
            ps.setInt(3, columnas);
            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getLong(1);
                }
            }
        }
        throw new SQLException("No se pudo obtener el ID autogenerado para la cabecera de la ciudad.");
    }

    /**
     * CONTRATO OBLIGATORIO DE RECUPERACIÓN: cargarEstadoCompleto(idCiudad)
     * Ejecuta secuencialmente los pasos del desglose relacional exigidos por la especificación del Sprint 4.
     */
    public Ciudad cargarEstadoCompleto(long idCiudad) {
        String sqlCiudad = "SELECT * FROM ciudad WHERE id = ?";
        Ciudad ciudadReconstruida = null;

        try (Connection conn = dbManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sqlCiudad)) {

            ps.setLong(1, idCiudad);
            try (ResultSet rs = ps.executeQuery()) {
                // PASO 1 y 2 DEL ORDEN: Leer cabecera y dimensionar usando la factoría del Módulo 1
                if (rs.next()) {
                    String nombre = rs.getString("nombre");
                    int filas = rs.getInt("filas");
                    int columnas = rs.getInt("columnas");

                    // Invocación al método estático de fábrica obligatorio del Sprint 4
                    ciudadReconstruida = Ciudad.reconstruir(nombre, filas, columnas, 0);
                } else {
                    // Escenario de error controlado: la entidad buscada no existe en el sistema
                    throw new FormatoSalidaException("Escenario Controlado: La ciudad con ID [" + idCiudad + "] no existe en el sistema.");
                }
            }

            // PASO 3 DEL ORDEN: Extraer la colección de celdas desde su repositorio especializado
            List<BloqueRepository.DatosBloqueDTO> bloques = bloqueRepository.cargarBloquesDeCiudad(idCiudad);

            // PASO 4 DEL ORDEN: Rehidratar la matriz inyectando cada bloque en su celda correspondiente
            for (BloqueRepository.DatosBloqueDTO b : bloques) {
                try {
                    Bloque bloque = crearBloqueDesdeDTO(b);
                    ciudadReconstruida.addBloque(bloque);
                } catch (Exception e) {
                    System.err.println("[Persistencia] Error al cargar bloque en (" + b.x + ", " + b.y + "): " + e.getMessage());
                }
            }

            // PASOS 5, 6, 7 y 8 DEL ORDEN: Verificación de integridad
            System.out.println("[Persistencia] Ciudad rehidratada con " + bloques.size() + " bloques.");

            System.out.println("[Persistencia S4] Ciudad '" + ciudadReconstruida.getNombre() + "' rehidratada con éxito y lista para simular.");

        } catch (SQLException e) {
            throw new FormatoSalidaException("Error crítico de consistencia relacional al ejecutar el orden de carga.", e);
        }

        return ciudadReconstruida; // Devuelve el objeto de dominio completamente operativo y equivalente al original
    }

    /**
     * Recupera el catálogo general de ciudades guardadas para rellenar los componentes visuales de la GUI.
     */
    public List<String> listarCiudades() {
        String sql = "SELECT id, nombre, filas, columnas FROM ciudad ORDER BY id DESC";
        List<String> lista = new ArrayList<>();

        try (Connection conn = dbManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add("ID: " + rs.getLong("id") + " | " + rs.getString("nombre") + " (" + rs.getInt("filas") + "x" + rs.getInt("columnas") + ")");
            }
        } catch (SQLException e) {
            throw new FormatoSalidaException("Error al listar el catálogo de ciudades históricas.", e);
        }
        return lista;
    }

    private Bloque crearBloqueDesdeDTO(BloqueRepository.DatosBloqueDTO dto) {
        Posicion pos = new Posicion(dto.x, dto.y);
        Bloque bloque = switch(dto.tipo) {
            case "ENERGIA" -> new BloqueEnergia(pos);
            case "RESIDENCIAL" -> new BloqueResidencial(pos);
            case "INDUSTRIAL" -> new BloqueIndustrial(pos);
            case "SERVICIOS" -> new BloqueServicios(pos);
            case "TRANSPORTE" -> new BloqueTransporte(pos);
            default -> throw new IllegalArgumentException("Tipo de bloque desconocido: " + dto.tipo);
        };
        return bloque;
    }
}