-- Creación del espacio de trabajo
CREATE DATABASE IF NOT EXISTS synthcity;
USE synthcity;

-- 1. Tabla base: Ciudad
CREATE TABLE ciudad (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    filas INT NOT NULL,
    columnas INT NOT NULL
);

-- 2. Bloques componentes de la ciudad
CREATE TABLE bloque (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    ciudad_id BIGINT NOT NULL,
    tipo VARCHAR(50) NOT NULL,
    x INT NOT NULL,
    y INT NOT NULL,
    activo BOOLEAN NOT NULL,
    FOREIGN KEY (ciudad_id) REFERENCES ciudad(id) ON DELETE CASCADE
);

-- 3. Historial de simulaciones ejecutadas
CREATE TABLE simulacion (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    ciudad_id BIGINT NOT NULL,
    ciclos INT NOT NULL,
    fecha TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (ciudad_id) REFERENCES ciudad(id) ON DELETE CASCADE
);

-- 4. Ciclos iterativos individuales de cada simulación
CREATE TABLE ciclo (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    simulacion_id BIGINT NOT NULL,
    numero INT NOT NULL,
    estado TEXT NOT NULL, -- Almacena el estado serializado en formato JSON/Texto
    FOREIGN KEY (simulacion_id) REFERENCES simulacion(id) ON DELETE CASCADE
);

-- 5. Evaluaciones del estado de la simulación
CREATE TABLE evaluacion (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    simulacion_id BIGINT NOT NULL,
    nivel VARCHAR(50) NOT NULL,
    score DOUBLE NOT NULL,
    mensaje TEXT NOT NULL,
    FOREIGN KEY (simulacion_id) REFERENCES simulacion(id) ON DELETE CASCADE
);

-- 6. Dataset de referencia para el modelo predictivo
CREATE TABLE dataset_referencia (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    ruta_csv VARCHAR(255) NOT NULL,
    columnas INT NOT NULL,
    registros INT NOT NULL,
    objetivo VARCHAR(50) NOT NULL
);

-- 7. Modelos Weka entrenados y persistidos
CREATE TABLE modelo_weka (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    ruta_modelo VARCHAR(255) NOT NULL,
    ruta_estructura VARCHAR(255) NOT NULL,
    dataset_id BIGINT NOT NULL,
    algoritmo VARCHAR(50) NOT NULL,
    FOREIGN KEY (dataset_id) REFERENCES dataset_referencia(id)
);

-- 8. Predicciones heurísticas y de Machine Learning
CREATE TABLE prediccion (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    evaluacion_id BIGINT NOT NULL,
    tipo VARCHAR(50) NOT NULL, -- 'HEURISTICA' o 'ML'
    tendencia VARCHAR(50) NOT NULL, -- 'MEJORA', 'ESTABLE', 'DETERIORO'
    score DOUBLE NOT NULL,
    mensaje TEXT NOT NULL,
    modelo_id BIGINT NULL, -- Opcional si es heurística
    FOREIGN KEY (evaluacion_id) REFERENCES evaluacion(id) ON DELETE CASCADE,
    FOREIGN KEY (modelo_id) REFERENCES modelo_weka(id) ON DELETE SET NULL
);

-- 9. Rankings globales generados
CREATE TABLE ranking (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    contenido TEXT NOT NULL, -- Almacena la lista indexada y justificada
    fecha TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);