import React from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { Button } from "@/components/ui/button";
import { useAuthStore } from '@/store/authStore';

const Navbar: React.FC = () => {
  const isAuthenticated = useAuthStore((state) => state.isAuthenticated());
  const user = useAuthStore((state) => state.user);
  const logout = useAuthStore((state) => state.logout);
  const navigate = useNavigate();

  const handleLogout = () => {
    logout();
    navigate('/login');
  };

  return (
    <nav className="fixed top-0 left-0 right-0 z-50 bg-primary text-primary-foreground p-4 shadow-md h-16">
      <div className="container mx-auto flex justify-between items-center">
        <Link to={isAuthenticated ? "/dashboard" : "/welcome"} className="text-xl font-bold">
          Cafeteria App
        </Link>
        <div className="flex items-center space-x-4">
          {isAuthenticated ? (
            <>
              <span className="text-sm">Hola, {user?.nombre_completo?.split(' ')[0]}</span> 
              <Button onClick={handleLogout} variant="ghost" size="sm">
                Cerrar Sesión
              </Button>
            </>
          ) : (
            <Button asChild variant="ghost" size="sm">
              <Link to="/login">Iniciar Sesión</Link>
            </Button>
          )}
        </div>
      </div>
    </nav>
  );
};

export default Navbar;
