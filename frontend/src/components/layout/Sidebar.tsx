import React from 'react';

const Sidebar: React.FC = () => {
  return (
    <aside className="w-64 bg-card p-4 shadow-lg hidden md:block"> {/* Hidden on small screens, shown on medium and up */}
      <h2 className="text-lg font-semibold mb-4">Navegación</h2>
      <ul className="space-y-2">
        <li><a href="/dashboard" className="text-muted-foreground hover:text-foreground">Dashboard</a></li>
        <li><a href="/products" className="text-muted-foreground hover:text-foreground">Productos</a></li>
        <li><a href="/sales" className="text-muted-foreground hover:text-foreground">Ventas</a></li>
        {/* More links can be added here */}
      </ul>
    </aside>
  );
};

export default Sidebar;
