package com.synthcity.modulo4.persistencia.repositorios;

import com.synthcity.modulo4.persistencia.ConexionBD;
import com.synthcity.modulo4.persistencia.entidades.CiudadEntidad;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CiudadRepository {

    public CiudadEntidad guardar(CiudadEntidad ciudad) throws SQLException {
        String sql = "INSERT INTO ciudad (nombre, filas, columnas) VALUES (?, ?, ?)";
        Connection conn = ConexionBD.getConexion();

        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, ciudad.getNombre());
            ps.setInt(2, ciudad.getFilas());
            ps.setInt(3, ciudad.getColumnas());
            ps.executeUpdate();

            try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    ciudad.setId(generatedKeys.getLong(1));
                } else {
                    throw new SQLException("Error al guardar ciudad: No se obtuvo el ID generado.");
                }
            }
        }
        return ciudad;
    }

    public CiudadEntidad buscarPorId(Long id) throws SQLException {
        String sql = "SELECT id, nombre, filas, columnas FROM ciudad WHERE id = ?";
        Connection conn = ConexionBD.getConexion();

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new CiudadEntidad(
                            rs.getLong("id"),
                            rs.getString("nombre"),
                            rs.getInt("filas"),
                            rs.getInt("columnas")
                    );
                }
            }
        }
        return null;
    }

    public List<CiudadEntidad> buscarTodas() throws SQLException {
        List<CiudadEntidad> ciudades = new ArrayList<>();
        String sql = "SELECT id, nombre, filas, columnas FROM ciudad";
        Connection conn = ConexionBD.getConexion();

        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                ciudades.add(new CiudadEntidad(
                        rs.getLong("id"),
                        rs.getString("nombre"),
                        rs.getInt("filas"),
                        rs.getInt("columnas")
                ));
            }
        }
        return ciudades;
    }

    public void eliminar(Long id) throws SQLException {
        String sql = "DELETE FROM ciudad WHERE id = ?";
        Connection conn = ConexionBD.getConexion();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            ps.executeUpdate();
        }
    }
}