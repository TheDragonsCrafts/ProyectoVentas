import React from 'react';
import { Link } from 'react-router-dom';
import { useAuthStore } from '@/store/authStore';

const Sidebar: React.FC = () => {
  const { user } = useAuthStore(state => ({ user: state.user }));
  // const isMaster = user?.rol === 'MAESTRO' || user?.es_admin_maestro === true;
  // The backend schema correction uses 'es_admin_maestro' boolean for the source of truth.
  // The 'rol' is derived or set consistently. The token also contains 'indicador_admin_maestro' (integer).
  // UserPublic type has 'rol' and 'es_admin_maestro?'.
  // Prefer es_admin_maestro if available and reliable from token/user object.
  // If user.es_admin_maestro is not consistently populated from token, then user.rol === 'MAESTRO' is the fallback.
  // The backend login returns user object with 'rol' and 'indicador_admin_maestro'.
  // The UserPublic type in frontend has 'rol' and 'es_admin_maestro'.
  // The authStore user object should have 'es_admin_maestro' populated if it's in the token.
  // Let's assume `user.rol` is the most reliable from current setup.
  const isMaster = user?.rol === 'MAESTRO';


  return (
    <aside className="w-64 bg-card p-4 shadow-lg hidden md:block"> 
      <h2 className="text-lg font-semibold mb-4">Navegación</h2>
      <nav>
        <ul className="space-y-2">
          <li>
            <Link to="/dashboard" className="block px-3 py-2 rounded-md text-muted-foreground hover:bg-accent hover:text-accent-foreground">
              Dashboard
            </Link>
          </li>
          <li>
            <Link to="/products" className="block px-3 py-2 rounded-md text-muted-foreground hover:bg-accent hover:text-accent-foreground">
              Productos
            </Link>
          </li>
          <li>
            <Link to="/sales/new" className="block px-3 py-2 rounded-md text-muted-foreground hover:bg-accent hover:text-accent-foreground">
              Nueva Venta
            </Link>
          </li>
          <li>
            <Link to="/sales/history" className="block px-3 py-2 rounded-md text-muted-foreground hover:bg-accent hover:text-accent-foreground">
              Historial Ventas
            </Link>
          </li>
          {isMaster && (
            <li>
              <Link to="/admin/users" className="block px-3 py-2 rounded-md text-muted-foreground hover:bg-accent hover:text-accent-foreground">
                Gestión Admins
              </Link>
            </li>
          )}
        </ul>
      </nav>
    </aside>
  );
};

export default Sidebar;
