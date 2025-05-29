import React from 'react';
import { Link } from 'react-router-dom';
import { Button } from "@/components/ui/button"; // Using ShadCN button

const HomePage: React.FC = () => {
  return (
    <div className="container mx-auto text-center py-10">
      <h1 className="text-4xl font-bold mb-4">Bienvenido a la Cafetería</h1>
      <p className="text-lg text-muted-foreground mb-8">
        Gestiona tus productos, ventas y administradores fácilmente.
      </p>
      <div className="space-x-4">
        <Button asChild>
          <Link to="/login">Iniciar Sesión</Link>
        </Button>
        <Button variant="outline" asChild>
          <Link to="/dashboard">Ir al Dashboard (Demo)</Link>
        </Button>
      </div>
    </div>
  );
};

export default HomePage;
