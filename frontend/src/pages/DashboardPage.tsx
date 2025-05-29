import React from 'react';
import { useNavigate } from 'react-router-dom';
import { Card, CardHeader, CardTitle, CardDescription, CardContent } from '@/components/ui/card';
import { Button } from '@/components/ui/button';
import { useAuthStore } from '@/store/authStore';

const DashboardPage: React.FC = () => {
  const { user, logout } = useAuthStore((state) => ({ user: state.user, logout: state.logout }));
  const navigate = useNavigate();

  const handleLogout = () => {
    logout();
    navigate('/login');
  };

  return (
    <div>
      <div className="flex justify-between items-center mb-6">
        <h1 className="text-3xl font-bold">Dashboard</h1>
        <Button onClick={handleLogout} variant="outline">Cerrar Sesión</Button>
      </div>
      
      {user && (
        <div className="mb-6 p-4 bg-card rounded-lg shadow">
          <h2 className="text-2xl font-semibold">Bienvenido, {user.nombre_completo}!</h2>
          <p className="text-muted-foreground">Correo: {user.correo_electronico}</p>
          <p className="text-muted-foreground">Rol: {user.rol}</p>
        </div>
      )}

      <div className="grid gap-6 md:grid-cols-2 lg:grid-cols-3">
        <Card>
          <CardHeader>
            <CardTitle>Administradores</CardTitle>
            <CardDescription>Gestionar usuarios del sistema.</CardDescription>
          </CardHeader>
          <CardContent>
            <p>Contenido relacionado a administradores...</p>
            {/* Link to /admin/users or similar */}
          </CardContent>
        </Card>
        <Card>
          <CardHeader>
            <CardTitle>Productos</CardTitle>
            <CardDescription>Gestionar inventario de productos.</CardDescription>
          </CardHeader>
          <CardContent>
            <p>Contenido relacionado a productos...</p>
            {/* Link to /admin/products */}
          </CardContent>
        </Card>
        <Card>
          <CardHeader>
            <CardTitle>Ventas</CardTitle>
            <CardDescription>Ver historial y reportes de ventas.</CardDescription>
          </CardHeader>
          <CardContent>
            <p>Contenido relacionado a ventas...</p>
            {/* Link to /admin/sales */}
          </CardContent>
        </Card>
      </div>
    </div>
  );
};

export default DashboardPage;
