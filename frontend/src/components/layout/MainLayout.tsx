import React from 'react';
import { Outlet } from 'react-router-dom';
import Navbar from './Navbar';
import Sidebar from './Sidebar';

const MainLayout: React.FC = () => {
  return (
    <> {/* Using fragment as the outer element is fine */}
      <Navbar />
      <div className="flex min-h-screen"> {/* Ensure full height */}
        <div className="fixed inset-y-0 left-0 z-40 pt-16 hidden md:flex md:w-64 flex-col bg-background border-r"> {/* Sidebar container */}
          <Sidebar />
        </div>
        <main className="flex-1 p-6 pt-20 md:ml-64"> {/* Main content: pt-20 for navbar (h-16 + p-4), ml-64 for sidebar */}
          <Outlet />
        </main>
      </div>
    </>
  );
};

export default MainLayout;
