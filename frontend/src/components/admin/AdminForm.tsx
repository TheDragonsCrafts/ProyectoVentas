import React, { useState, useEffect } from 'react';
import { useForm, Controller } from 'react-hook-form';
import { zodResolver } from '@hookform/resolvers/zod';
import * as z from 'zod';
import { Button } from '@/components/ui/button';
import { Input } from '@/components/ui/input';
import { Label } from '@/components/ui/label';
import { Checkbox } from '@/components/ui/checkbox';
import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from '@/components/ui/select';
import { toast } from 'sonner';
import { UserPublic, AdminCreateDTO, AdminUpdateDTO, UserRole } from '@/types/user';
import { createAdmin, updateAdmin } from '@/services/api';

// Zod schema for validation
const adminFormSchema = z.object({
  nombre_completo: z.string().min(3, 'Nombre completo debe tener al menos 3 caracteres.'),
  correo_electronico: z.string().email('Email inválido.'),
  password_plana: z.string().optional(), // Optional for update
  confirm_password: z.string().optional(),
  rol: z.enum(['ESTANDAR', 'MAESTRO'], { required_error: 'Rol es requerido.' }),
  activo: z.boolean().default(true),
}).refine(data => {
  // If password is provided, confirm_password must match
  if (data.password_plana && data.password_plana !== data.confirm_password) {
    return false;
  }
  return true;
}, {
  message: 'Las contraseñas no coinciden.',
  path: ['confirm_password'], // Point error to confirm_password field
});

type AdminFormData = z.infer<typeof adminFormSchema>;

interface AdminFormProps {
  initialData?: UserPublic | null;
  onSuccess: ()_ => void; // Callback to refresh data and close dialog
  onCancel: ()_ => void; // Callback to close dialog
}

const AdminForm: React.FC<AdminFormProps> = ({ initialData, onSuccess, onCancel }) => {
  const isEditing = !!initialData;

  const { control, handleSubmit, register, reset, formState: { errors, isSubmitting } } = useForm<AdminFormData>({
    resolver: zodResolver(adminFormSchema),
    defaultValues: {
      nombre_completo: initialData?.nombre_completo || '',
      correo_electronico: initialData?.correo_electronico || '',
      password_plana: '',
      confirm_password: '',
      rol: initialData?.rol || 'ESTANDAR',
      activo: initialData?.activo === undefined ? true : initialData.activo,
    },
  });

  useEffect(() => {
    // Reset form if initialData changes (e.g., opening dialog for different admin)
    reset({
      nombre_completo: initialData?.nombre_completo || '',
      correo_electronico: initialData?.correo_electronico || '',
      password_plana: '',
      confirm_password: '',
      rol: initialData?.rol || 'ESTANDAR',
      activo: initialData?.activo === undefined ? true : initialData.activo,
    });
  }, [initialData, reset]);

  const onSubmit = async (data: AdminFormData) => {
    try {
      let response;
      const adminPayload: Partial<AdminCreateDTO | AdminUpdateDTO> = {
        nombre_completo: data.nombre_completo,
        correo_electronico: data.correo_electronico,
        rol: data.rol as UserRole, // Zod enum ensures it's UserRole
        activo: data.activo,
      };

      if (data.password_plana) {
        (adminPayload as AdminCreateDTO /* or AdminUpdateDTO */).password_plana = data.password_plana;
      }
      
      if (isEditing && initialData) {
        response = await updateAdmin(initialData.id_administrador, adminPayload as AdminUpdateDTO);
        toast.success('Administrador actualizado correctamente.');
      } else {
         if (!data.password_plana) {
            toast.error('Contraseña es requerida para crear un nuevo administrador.');
            return; // Should be caught by schema if made required for create
          }
        response = await createAdmin(adminPayload as AdminCreateDTO);
        toast.success('Administrador creado correctamente.');
      }
      onSuccess(); // Call parent's success handler
    } catch (error: any) {
      const errorMsg = error.response?.data?.message || error.message || 
                       (isEditing ? 'Error al actualizar administrador.' : 'Error al crear administrador.');
      toast.error(errorMsg);
    }
  };

  return (
    <form onSubmit={handleSubmit(onSubmit)} className="space-y-6">
      <div>
        <Label htmlFor="nombre_completo">Nombre Completo</Label>
        <Input id="nombre_completo" {...register('nombre_completo')} />
        {errors.nombre_completo && <p className="text-sm text-destructive mt-1">{errors.nombre_completo.message}</p>}
      </div>

      <div>
        <Label htmlFor="correo_electronico">Email</Label>
        <Input id="correo_electronico" type="email" {...register('correo_electronico')} />
        {errors.correo_electronico && <p className="text-sm text-destructive mt-1">{errors.correo_electronico.message}</p>}
      </div>

      <div>
        <Label htmlFor="password_plana">Contraseña {isEditing ? '(Dejar en blanco para no cambiar)' : ''}</Label>
        <Input id="password_plana" type="password" {...register('password_plana')} />
        {errors.password_plana && <p className="text-sm text-destructive mt-1">{errors.password_plana.message}</p>}
      </div>

      <div>
        <Label htmlFor="confirm_password">Confirmar Contraseña</Label>
        <Input id="confirm_password" type="password" {...register('confirm_password')} />
        {errors.confirm_password && <p className="text-sm text-destructive mt-1">{errors.confirm_password.message}</p>}
      </div>
      
      <div>
        <Label htmlFor="rol">Rol</Label>
        <Controller
          name="rol"
          control={control}
          render={({ field }) => (
            <Select onValueChange={field.onChange} defaultValue={field.value}>
              <SelectTrigger>
                <SelectValue placeholder="Seleccionar rol" />
              </SelectTrigger>
              <SelectContent>
                <SelectItem value="ESTANDAR">Estándar</SelectItem>
                <SelectItem value="MAESTRO">Maestro</SelectItem>
              </SelectContent>
            </Select>
          )}
        />
        {errors.rol && <p className="text-sm text-destructive mt-1">{errors.rol.message}</p>}
      </div>

      <div className="flex items-center space-x-2">
         <Controller
            name="activo"
            control={control}
            render={({ field }) => (
                 <Checkbox
                    id="activo"
                    checked={field.value}
                    onCheckedChange={field.onChange}
                />
            )}
        />
        <Label htmlFor="activo" className="font-normal">Activo</Label>
        {errors.activo && <p className="text-sm text-destructive mt-1">{errors.activo.message}</p>}
      </div>

      <div className="flex justify-end space-x-3 pt-4">
        <Button type="button" variant="outline" onClick={onCancel} disabled={isSubmitting}>
          Cancelar
        </Button>
        <Button type="submit" disabled={isSubmitting}>
          {isSubmitting ? (isEditing ? 'Guardando Cambios...' : 'Creando...' ) : (isEditing ? 'Guardar Cambios' : 'Crear Administrador')}
        </Button>
      </div>
    </form>
  );
};

export default AdminForm;
