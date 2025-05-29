import { Pool, QueryResult } from 'pg';

// Connection parameters should ideally be sourced from environment variables
// For example:
// const dbHost = process.env.DB_HOST || 'localhost';
// const dbPort = parseInt(process.env.DB_PORT || '5432', 10);
// const dbUser = process.env.DB_USER || 'your_db_user';
// const dbPassword = process.env.DB_PASSWORD || 'your_db_password';
// const dbName = process.env.DB_NAME || 'your_db_name';

const pool = new Pool({
  // Replace with your actual connection details or environment variables
  host: process.env.DB_HOST || 'localhost',
  port: parseInt(process.env.DB_PORT || '5432', 10),
  user: process.env.DB_USER || 'postgres', // Default user for PostgreSQL
  password: process.env.DB_PASSWORD || 'password', // Replace with a secure password
  database: process.env.DB_NAME || 'cafeteria_db', // Replace with your database name
  max: 20, // Max number of clients in the pool
  idleTimeoutMillis: 30000, // How long a client is allowed to remain idle before being closed
  connectionTimeoutMillis: 2000, // How long to wait for a connection from the pool
});

pool.on('connect', (client) => {
  console.log('New client connected to the PostgreSQL database');
  // You can set session-specific parameters here if needed
  // client.query('SET DATESTYLE = iso, mdy');
});

pool.on('error', (err, client) => {
  console.error('Unexpected error on idle client', err);
  // It's important to handle errors here, otherwise they might go unnoticed
  // Depending on the error, you might want to exit the process or attempt to reconnect
  process.exit(-1); // Example: exit the process on a critical pool error
});

/**
 * Executes a SQL query using a client from the connection pool.
 * @param text The SQL query string or a query config object.
 * @param params Optional array of parameters for parameterized queries.
 * @returns A Promise that resolves with the query result.
 */
export const query = async <T = any>(text: string, params?: any[]): Promise<QueryResult<T>> => {
  const start = Date.now();
  try {
    const client = await pool.connect();
    try {
      const res = await client.query<T>(text, params);
      const duration = Date.now() - start;
      console.log('Executed query', { text, duration, rows: res.rowCount });
      return res;
    } finally {
      client.release(); // Release the client back to the pool
    }
  } catch (error) {
    console.error('Error executing query', { text, error });
    throw error;
  }
};

// Graceful shutdown
// It's good practice to close the pool when the application is shutting down
const gracefulShutdown = async () => {
  console.log('Shutting down database pool...');
  try {
    await pool.end();
    console.log('Database pool closed successfully.');
    process.exit(0);
  } catch (error) {
    console.error('Error closing database pool:', error);
    process.exit(1);
  }
};

// Listen for termination signals
process.on('SIGINT', gracefulShutdown);
process.on('SIGTERM', gracefulShutdown);

export default pool;

// Example usage (not part of the connection module itself, just for demonstration):
/*
import { query } from './connection';

async function getUsers() {
  try {
    const result = await query('SELECT * FROM administradores');
    console.log(result.rows);
  } catch (err) {
    console.error('Failed to fetch users:', err);
  }
}

// getUsers();
*/
