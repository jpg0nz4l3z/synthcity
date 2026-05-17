package org.synthcity.modulo_4.persistencia.repositorios;

import org.synthcity.modulo_4.persistencia.ConexionBD;
import org.synthcity.modulo_4.persistencia.entidades.DatasetReferenciaEntidad;
import java.sql.*;

public class DatasetReferenciaRepository {

    public DatasetReferenciaEntidad guardar(DatasetReferenciaEntidad dataset) throws SQLException {
        String sql = "INSERT INTO dataset_referencia (ruta_csv, columnas, registros, objetivo) VALUES (?, ?, ?, ?)";
        Connection conn = ConexionBD.getConexion();

        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, dataset.getRutaCsv());
            ps.setInt(2, dataset.getColumnas());
            ps.setInt(3, dataset.getRegistros());
            ps.setString(4, dataset.getObjetivo());
            ps.executeUpdate();

            try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    dataset.setId(generatedKeys.getLong(1));
                } else {
                    throw new SQLException("Error al guardar dataset_referencia: No se obtuvo el ID autogenerado.");
                }
            }
        }
        return dataset;
    }

    public DatasetReferenciaEntidad buscarUltimoPorRuta(String rutaCsv) throws SQLException {
        String sql = "SELECT id, ruta_csv, columnas, registros, objetivo FROM dataset_referencia WHERE ruta_csv = ? ORDER BY id DESC LIMIT 1";
        Connection conn = ConexionBD.getConexion();

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, rutaCsv);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new DatasetReferenciaEntidad(
                            rs.getLong("id"),
                            rs.getString("ruta_csv"),
                            rs.getInt("columnas"),
                            rs.getInt("registros"),
                            rs.getString("objetivo")
                    );
                }
            }
        }
        return null;
    }
}