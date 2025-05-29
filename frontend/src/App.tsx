import {
  createBrowserRouter,
  RouterProvider,
  Navigate,
  Outlet, // Outlet might be used by ProtectedRoute itself
} from "react-router-dom";
import MainLayout from "./components/layout/MainLayout";
import LoginPage from "./pages/LoginPage";
import DashboardPage from "./pages/DashboardPage";
import ProtectedRoute from "./components/auth/ProtectedRoute"; // Import ProtectedRoute
import HomePage from "./pages/HomePage"; // Keep HomePage for a public landing page

const router = createBrowserRouter([
  {
    path: "/login",
    element: <LoginPage />,
  },
  {
    path: "/welcome", // Public landing page
    element: <HomePage />,
  },
  {
    element: <ProtectedRoute />, // Wraps all protected routes
    children: [
      {
        path: "/", // Default protected route
        element: <MainLayout />,
        children: [
          {
            index: true,
            element: <Navigate to="/dashboard" replace />, // Redirect / to /dashboard
          },
          {
            path: "dashboard",
            element: <DashboardPage />,
          },
          // Placeholder for other protected sections
          // { path: "products", element: <div>Productos (Protected)</div> },
          // { path: "sales", element: <div>Ventas (Protected)</div> },
          // { path: "admins", element: <div>Administradores (Protected)</div> },
        ],
      },
    ],
  },
  {
    path: "*", // Catch-all for not found routes
    // If authenticated, redirect to dashboard, else to welcome/login
    // For simplicity now, just redirect to /welcome or /dashboard based on a quick check,
    // or let ProtectedRoute handle unauthorized access to /
    element: <Navigate to="/welcome" replace />, 
  }
]);

function App() {
  return <RouterProvider router={router} />;
}

export default App;
