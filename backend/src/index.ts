import express from 'express';
import authRoutes from './routes/auth'; // Import auth routes
import adminRoutes from './routes/admin'; // Import admin routes
import productRoutes from './routes/product'; // Import product routes
import saleRoutes from './routes/sale'; // Import sale routes

const app = express();
const port = process.env.PORT || 3000;

// Middleware to parse JSON bodies
app.use(express.json());

// Health check endpoint
app.get('/health', (req: express.Request, res: express.Response) => {
  res.status(200).send('Server is healthy');
});

// Auth routes
app.use('/api/auth', authRoutes);

// Admin management routes
app.use('/api/admins', adminRoutes); // All routes in adminRoutes are already protected

// Product management routes
app.use('/api/products', productRoutes); // All routes in productRoutes are already protected

// Sales management routes
app.use('/api/sales', saleRoutes); // All routes in saleRoutes are already protected

app.listen(port, () => {
  console.log(`Server is running on http://localhost:${port}`);
});
