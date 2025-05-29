import React from 'react';
import { Outlet } from 'react-router-dom';
import Navbar from './Navbar';
import Sidebar from './Sidebar';
import { Toaster } from '@/components/ui/sonner'; // Import Toaster

const MainLayout: React.FC = () => {
  return (
    <> 
      <Navbar />
      <div className="flex min-h-screen"> 
        <div className="fixed inset-y-0 left-0 z-40 pt-16 hidden md:flex md:w-64 flex-col bg-background border-r"> 
          <Sidebar />
        </div>
        <main className="flex-1 p-6 pt-20 md:ml-64"> 
          <Outlet />
        </main>
      </div>
      <Toaster richColors position="top-right" /> {/* Add Toaster here */}
    </>
  );
};

export default MainLayout;
