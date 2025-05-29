import React, { useEffect, useState } from 'react';
import { getAllAdmins, deleteAdmin as apiDeleteAdmin } from '@/services/api';
import { UserPublic } from '@/types/user';
import { Button } from '@/components/ui/button';
import {
  Table,
  TableBody,
  TableCell,
  TableHead,
  TableHeader,
  TableRow,
} from '@/components/ui/table';
import {
  Dialog,
  DialogContent,
  DialogHeader,
  DialogTitle,
  DialogTrigger,
} from '@/components/ui/dialog';
import {
  AlertDialog,
  AlertDialogAction,
  AlertDialogCancel,
  AlertDialogContent,
  AlertDialogDescription,
  AlertDialogFooter,
  AlertDialogHeader,
  AlertDialogTitle,
  AlertDialogTrigger,
} from '@/components/ui/alert-dialog';
import { toast } from "sonner"; 
import AdminForm from '@/components/admin/AdminForm'; // Import AdminForm

const AdminManagementPage: React.FC = () => {
  const [admins, setAdmins] = useState<UserPublic[]>([]);
  const [isLoading, setIsLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);

  // States for handling dialogs
  const [isFormOpen, setIsFormOpen] = useState(false);
  const [editingAdmin, setEditingAdmin] = useState<UserPublic | null>(null);
  // const [adminToDelete, setAdminToDelete] = useState<UserPublic | null>(null);


  const fetchAdmins = async () => {
    setIsLoading(true);
    setError(null);
    try {
      const data = await getAllAdmins();
      setAdmins(data);
    } catch (err) {
      const errorMsg = err instanceof Error ? err.message : 'Failed to fetch admins';
      setError(errorMsg);
      toast.error(errorMsg);
    } finally {
      setIsLoading(false);
    }
  };

  useEffect(() => {
    fetchAdmins();
  }, []);

  const handleCreateNew = () => {
    setEditingAdmin(null);
    setIsFormOpen(true);
  };

  const handleEdit = (admin: UserPublic) => {
    setEditingAdmin(admin);
    setIsFormOpen(true);
  };
  
  const handleDelete = async (adminId: number) => {
    try {
      await apiDeleteAdmin(adminId);
      toast.success('Administrador eliminado/desactivado correctamente.');
      fetchAdmins(); // Refresh list
    } catch (err) {
      const errorMsg = err instanceof Error ? err.message : 'Error al eliminar administrador.';
      toast.error(errorMsg);
      // setError(errorMsg); // Optionally set page-level error
    }
  };
  
  const onFormSuccess = () => {
    setIsFormOpen(false);
    fetchAdmins();
  }

  if (isLoading) return <p>Cargando administradores...</p>;
  // Error display can be more prominent if needed
  // if (error) return <p className="text-destructive">Error: {error}</p>; 

  return (
    <div className="container mx-auto py-8">
      <div className="flex justify-between items-center mb-6">
        <h1 className="text-3xl font-bold">Gestión de Administradores</h1>
        <Button onClick={handleCreateNew}>Crear Nuevo Administrador</Button>
      </div>

      {error && <p className="text-destructive mb-4">Error: {error}</p>}


      {/* TODO: AdminForm Dialog will be triggered here */}
      {/* For now, a placeholder for where the Dialog with AdminForm would go */}
      <Dialog open={isFormOpen} onOpenChange={(isOpen) => {
        setIsFormOpen(isOpen);
        if (!isOpen) setEditingAdmin(null); // Clear editing admin when dialog closes
      }}>
        <DialogContent className="sm:max-w-[525px]"> {/* Adjust width as needed */}
          <DialogHeader>
            <DialogTitle>{editingAdmin ? 'Editar Administrador' : 'Crear Nuevo Administrador'}</DialogTitle>
          </DialogHeader>
          <AdminForm 
            initialData={editingAdmin} 
            onSuccess={onFormSuccess}
            onCancel={() => {
              setIsFormOpen(false);
              setEditingAdmin(null);
            }}
          />
        </DialogContent>
      </Dialog>

      <Table>
        <TableHeader>
          <TableRow>
            <TableHead>ID</TableHead>
            <TableHead>Nombre Completo</TableHead>
            <TableHead>Email</TableHead>
            <TableHead>Rol</TableHead>
            <TableHead>Activo</TableHead>
            <TableHead>Acciones</TableHead>
          </TableRow>
        </TableHeader>
        <TableBody>
          {admins.map((admin) => (
            <TableRow key={admin.id_administrador}>
              <TableCell>{admin.id_administrador}</TableCell>
              <TableCell>{admin.nombre_completo}</TableCell>
              <TableCell>{admin.correo_electronico}</TableCell>
              <TableCell>{admin.rol}</TableCell>
              <TableCell>{admin.activo ? 'Sí' : 'No'}</TableCell>
              <TableCell className="space-x-2">
                <Button variant="outline" size="sm" onClick={() => handleEdit(admin)}>
                  Editar
                </Button>
                <AlertDialog>
                  <AlertDialogTrigger asChild>
                    <Button variant="destructive" size="sm" disabled={admin.es_admin_maestro}> {/* Disable delete for master admin */}
                      Eliminar
                    </Button>
                  </AlertDialogTrigger>
                  <AlertDialogContent>
                    <AlertDialogHeader>
                      <AlertDialogTitle>¿Estás seguro?</AlertDialogTitle>
                      <AlertDialogDescription>
                        Esta acción {admin.activo ? "desactivará" : "activará"} al administrador "{admin.nombre_completo}".
                        No se puede deshacer.
                      </AlertDialogDescription>
                    </AlertDialogHeader>
                    <AlertDialogFooter>
                      <AlertDialogCancel>Cancelar</AlertDialogCancel>
                      <AlertDialogAction onClick={() => handleDelete(admin.id_administrador)}>
                        Confirmar
                      </AlertDialogAction>
                    </AlertDialogFooter>
                  </AlertDialogContent>
                </AlertDialog>
              </TableCell>
            </TableRow>
          ))}
        </TableBody>
      </Table>
      {/* Toaster for notifications from sonner */}
      {/* <Toaster richColors /> ... place this in App.tsx or MainLayout.tsx for global access */}
    </div>
  );
};

export default AdminManagementPage;
