import {
  createBrowserRouter,
  RouterProvider,
  Navigate,
  Outlet, // Outlet might be used by ProtectedRoute itself
} from "react-router-dom";
import MainLayout from "./components/layout/MainLayout";
import LoginPage from "./pages/LoginPage";
import DashboardPage from "./pages/DashboardPage";
import ProtectedRoute from "./components/auth/ProtectedRoute";
import MasterAdminRoute from "./components/auth/MasterAdminRoute"; 
import AdminManagementPage from "./pages/admin/AdminManagementPage"; 
import HomePage from "./pages/HomePage"; 
import ProductListPage from "./pages/products/ProductListPage"; 
import ProductFormPage from "./pages/products/ProductFormPage"; 
import SalesPage from "./pages/sales/SalesPage"; 
import SalesHistoryPage from "./pages/sales/SalesHistoryPage"; // Import SalesHistoryPage

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
          {
            path: "products",
            children: [
              { index: true, element: <ProductListPage /> },
              { path: "new", element: <ProductFormPage /> },
              { path: "edit/:id", element: <ProductFormPage /> },
            ]
          },
          {
            path: "admin/users", 
            element: <MasterAdminRoute />, 
            children: [
              { index: true, element: <AdminManagementPage /> }
            ]
          },
          {
            path: "sales/new",
            element: <SalesPage />,
          },
          {
            path: "sales/history",
            element: <SalesHistoryPage />,
          }
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
