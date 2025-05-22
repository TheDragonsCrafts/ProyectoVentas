# Database Optimization Notes

This document outlines potential database optimizations based on observed query patterns in the application.

## 1. Product Name Searches (`productos.nombre`)

*   **Observation:** The `productos.nombre` column is frequently queried using `LIKE '%patron%'` in `ProductoDatos.buscarPorNombre`. This type of query, with a leading wildcard, typically results in a full table scan because standard B-tree indexes cannot be efficiently used.

*   **Recommendation for `productos.nombre`:**
    *   **If searches can be restricted to matching from the beginning of the string (e.g., `patron%`):**
        Add a standard B-tree index:
        ```sql
        CREATE INDEX idx_productos_nombre ON productos(nombre);
        ```
        This will significantly speed up queries like `SELECT * FROM productos WHERE nombre LIKE 'patron%';`.

    *   **For efficient arbitrary substring searches (e.g., `%patron%`) on large tables:**
        Consider using a MySQL `FULLTEXT` index. This is more suitable for searching for words or phrases within text.
        ```sql
        CREATE FULLTEXT INDEX ft_productos_nombre ON productos(nombre);
        ```
        The query in `ProductoDatos.buscarPorNombre` would then need to be modified to use `MATCH() AGAINST()` syntax:
        ```java
        // Example modification in ProductoDatos.java
        // String sql = "SELECT * FROM productos WHERE MATCH(nombre) AGAINST(? IN BOOLEAN MODE)"; 
        // ps.setString(1, "*" + patron + "*"); // Adjust pattern for boolean mode if needed
        ```
        **Note:** `FULLTEXT` indexes have specific requirements and behaviors (e.g., minimum word length, stopword lists) and are supported by MyISAM and InnoDB (since MySQL 5.6). Performance and relevance of results can differ from `LIKE`.

## 2. Sales Data Filtering (`ventas.fecha_venta`, `ventas.estado`)

*   **Observation:** The `ventas` table has columns `fecha_venta` and `estado` which are likely to be used for filtering or sorting in future reports or queries (e.g., "show all sales from last month", "list all pending sales"). Currently, these columns are not indexed.

*   **Recommendation for `ventas.fecha_venta` and `ventas.estado`:**
    If queries that filter or sort by these columns become frequent or performance issues are noted, consider adding separate indexes:
    ```sql
    CREATE INDEX idx_ventas_fecha_venta ON ventas(fecha_venta);
    ```
    ```sql
    CREATE INDEX idx_ventas_estado ON ventas(estado);
    ```
    If queries often filter by both `fecha_venta` and `estado` simultaneously, a composite index might be beneficial, depending on query patterns and cardinality:
    ```sql
    -- Example: If filtering by state first, then by date range
    -- CREATE INDEX idx_ventas_estado_fecha ON ventas(estado, fecha_venta); 
    ```

## General Indexing Considerations:

*   **Write Performance:** Indexes improve read performance but can slow down write operations (INSERT, UPDATE, DELETE) as the indexes also need to be updated. Only add indexes that provide a tangible benefit to common and performance-critical queries.
*   **Cardinality:** Indexes are most effective on columns with high cardinality (many unique values). Indexing a boolean column with only two values might not always be as beneficial as indexing a column with many distinct values, unless the data is very skewed and queries select for the less common value.
*   **Query Analysis:** Use `EXPLAIN` on your SQL queries to understand how MySQL is executing them and whether indexes are being used effectively.
*   **Monitoring:** Monitor database performance regularly to identify slow queries and potential indexing needs.

These notes should serve as a starting point for database performance tuning as the application evolves.
