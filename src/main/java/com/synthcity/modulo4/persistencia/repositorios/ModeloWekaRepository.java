package com.synthcity.modulo4.persistencia.repositorios;

import com.synthcity.modulo4.persistencia.ConexionBD;
import com.synthcity.modulo4.persistencia.entidades.ModeloWekaEntidad;
import java.sql.*;

public class ModeloWekaRepository {

    public ModeloWekaEntidad guardar(ModeloWekaEntidad modelo) throws SQLException {
        String sql = "INSERT INTO modelo_weka (ruta_modelo, ruta_estructura, dataset_id, algoritmo) VALUES (?, ?, ?, ?)";
        Connection conn = ConexionBD.getConexion();

        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, modelo.getRutaModelo());
            ps.setString(2, modelo.getRutaEstructura());
            ps.setLong(3, modelo.getDatasetId());
            ps.setString(4, modelo.getAlgoritmo());
            ps.executeUpdate();

            try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    modelo.setId(generatedKeys.getLong(1));
                } else {
                    throw new SQLException("Error al guardar modelo_weka: No se obtuvo el ID autogenerado.");
                }
            }
        }
        return modelo;
    }

    public ModeloWekaEntidad buscarUltimoValido() throws SQLException {
        String sql = "SELECT id, ruta_modelo, ruta_estructura, dataset_id, algoritmo FROM modelo_weka ORDER BY id DESC LIMIT 1";
        Connection conn = ConexionBD.getConexion();

        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return new ModeloWekaEntidad(
                        rs.getLong("id"),
                        rs.getString("ruta_modelo"),
                        rs.getString("ruta_estructura"),
                        rs.getLong("dataset_id"),
                        rs.getString("algoritmo")
                );
            }
        }
        return null;
    }
}