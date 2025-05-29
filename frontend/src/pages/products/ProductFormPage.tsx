import React, { useEffect, useState } from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import { useForm, Controller } from 'react-hook-form';
import { zodResolver } from '@hookform/resolvers/zod';
import * as z from 'zod';
import { Button } from '@/components/ui/button';
import { Input } from '@/components/ui/input';
import { Label } from '@/components/ui/label';
import { Checkbox } from '@/components/ui/checkbox';
import { Textarea } from '@/components/ui/textarea'; // Need to add this component
import { Popover, PopoverTrigger, PopoverContent } from '@/components/ui/popover';
import { Calendar } from '@/components/ui/calendar';
import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from '@/components/ui/select';
import { CalendarIcon } from 'lucide-react';
import { format, parseISO } from 'date-fns'; // For date handling
import { toast } from 'sonner';
import { getProductById, createProduct, updateProduct } from '@/services/api';
import { Producto, ProductoCreateDTO, ProductoUpdateDTO, CategoriaProducto } from '@/types/Producto';

// Zod schema for validation
const productFormSchema = z.object({
  id_producto: z.number().int().positive("ID de producto debe ser un número positivo."),
  nombre_producto: z.string().min(3, "Nombre debe tener al menos 3 caracteres."),
  descripcion: z.string().optional(),
  precio: z.coerce.number().min(0, "Precio debe ser no negativo."),
  stock: z.coerce.number().int().min(0, "Cantidad (stock) debe ser un entero no negativo."),
  categoria: z.enum(['bebida_caliente', 'bebida_fria', 'panaderia', 'pasteleria', 'snacks', 'otros'], {
    required_error: "Categoría es requerida."
  }),
  activo: z.boolean().default(true),
  fecha_caducidad: z.date().optional().nullable(),
  imagen_url: z.string().url("URL de imagen inválida.").optional().or(z.literal('')),
});

type ProductFormData = z.infer<typeof productFormSchema>;

const ProductFormPage: React.FC = () => {
  const { id } = useParams<{ id?: string }>();
  const navigate = useNavigate();
  const isEditing = !!id;
  const [isLoading, setIsLoading] = useState(false);
  // For initial data loading in edit mode
  const [productData, setProductData] = useState<Producto | null>(null);


  const { control, handleSubmit, register, reset, setValue, formState: { errors, isSubmitting } } = useForm<ProductFormData>({
    resolver: zodResolver(productFormSchema),
    defaultValues: {
      id_producto: undefined, // Required, but no default for create
      nombre_producto: '',
      descripcion: '',
      precio: 0,
      stock: 0,
      categoria: undefined, // Required
      activo: true,
      fecha_caducidad: null,
      imagen_url: '',
    },
  });

  useEffect(() => {
    if (isEditing && id) {
      setIsLoading(true);
      getProductById(parseInt(id, 10))
        .then(data => {
          setProductData(data);
          reset({
            ...data,
            fecha_caducidad: data.fecha_caducidad ? parseISO(String(data.fecha_caducidad)) : null,
            // Ensure numeric fields are numbers if they come as strings from API
            precio: Number(data.precio),
            stock: Number(data.stock),
          });
        })
        .catch(err => {
          toast.error('Error al cargar producto: ' + err.message);
          navigate('/products');
        })
        .finally(() => setIsLoading(false));
    }
  }, [id, isEditing, navigate, reset]);

  const onSubmit = async (data: ProductFormData) => {
    try {
      const payload: Omit<ProductoCreateDTO | ProductoUpdateDTO, 'id_producto'> & { id_producto?: number } = {
        ...data,
        fecha_caducidad: data.fecha_caducidad ? format(data.fecha_caducidad, 'yyyy-MM-dd') : undefined,
      };
      
      if (isEditing) {
        delete payload.id_producto; // id_producto is not in UpdateDTO body, it's in URL
        await updateProduct(parseInt(id!, 10), payload as ProductoUpdateDTO);
        toast.success('Producto actualizado correctamente.');
      } else {
        payload.id_producto = data.id_producto; // Add it for create DTO
        await createProduct(payload as ProductoCreateDTO);
        toast.success('Producto creado correctamente.');
      }
      navigate('/products');
    } catch (error: any) {
      const errorMsg = error.response?.data?.message || error.message || 
                       (isEditing ? 'Error al actualizar producto.' : 'Error al crear producto.');
      toast.error(errorMsg);
    }
  };
  
  if (isLoading && isEditing) return <p>Cargando datos del producto...</p>;

  return (
    <div className="container mx-auto py-8 max-w-2xl">
      <h1 className="text-3xl font-bold mb-6">{isEditing ? 'Editar Producto' : 'Crear Nuevo Producto'}</h1>
      <form onSubmit={handleSubmit(onSubmit)} className="space-y-8 bg-card p-6 rounded-lg shadow-md">
        <div>
          <Label htmlFor="id_producto">ID Producto</Label>
          <Input 
            id="id_producto" 
            type="number"
            {...register('id_producto', { valueAsNumber: true })} 
            readOnly={isEditing} 
          />
          {errors.id_producto && <p className="text-sm text-destructive mt-1">{errors.id_producto.message}</p>}
        </div>

        <div>
          <Label htmlFor="nombre_producto">Nombre del Producto</Label>
          <Input id="nombre_producto" {...register('nombre_producto')} />
          {errors.nombre_producto && <p className="text-sm text-destructive mt-1">{errors.nombre_producto.message}</p>}
        </div>

        <div>
          <Label htmlFor="descripcion">Descripción</Label>
          <Textarea id="descripcion" {...register('descripcion')} />
          {errors.descripcion && <p className="text-sm text-destructive mt-1">{errors.descripcion.message}</p>}
        </div>
        
        <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
          <div>
            <Label htmlFor="precio">Precio</Label>
            <Input id="precio" type="number" step="0.01" {...register('precio')} />
            {errors.precio && <p className="text-sm text-destructive mt-1">{errors.precio.message}</p>}
          </div>
          <div>
            <Label htmlFor="stock">Cantidad (Stock)</Label>
            <Input id="stock" type="number" {...register('stock')} />
            {errors.stock && <p className="text-sm text-destructive mt-1">{errors.stock.message}</p>}
          </div>
        </div>

        <div>
          <Label htmlFor="categoria">Categoría</Label>
          <Controller
            name="categoria"
            control={control}
            render={({ field }) => (
              <Select onValueChange={field.onChange} defaultValue={field.value}>
                <SelectTrigger>
                  <SelectValue placeholder="Seleccionar categoría" />
                </SelectTrigger>
                <SelectContent>
                  <SelectItem value="bebida_caliente">Bebida Caliente</SelectItem>
                  <SelectItem value="bebida_fria">Bebida Fría</SelectItem>
                  <SelectItem value="panaderia">Panadería</SelectItem>
                  <SelectItem value="pasteleria">Pastelería</SelectItem>
                  <SelectItem value="snacks">Snacks</SelectItem>
                  <SelectItem value="otros">Otros</SelectItem>
                </SelectContent>
              </Select>
            )}
          />
          {errors.categoria && <p className="text-sm text-destructive mt-1">{errors.categoria.message}</p>}
        </div>
        
        <div>
          <Label htmlFor="fecha_caducidad">Fecha de Caducidad (Opcional)</Label>
          <Controller
            name="fecha_caducidad"
            control={control}
            render={({ field }) => (
              <Popover>
                <PopoverTrigger asChild>
                  <Button
                    variant={"outline"}
                    className={`w-full justify-start text-left font-normal ${!field.value && "text-muted-foreground"}`}
                  >
                    <CalendarIcon className="mr-2 h-4 w-4" />
                    {field.value ? format(field.value, "PPP") : <span>Seleccionar fecha</span>}
                  </Button>
                </PopoverTrigger>
                <PopoverContent className="w-auto p-0">
                  <Calendar
                    mode="single"
                    selected={field.value || undefined} // Pass undefined if null
                    onSelect={(date) => field.onChange(date || null)} // Ensure null is passed if date is cleared
                    initialFocus
                  />
                </PopoverContent>
              </Popover>
            )}
          />
          {errors.fecha_caducidad && <p className="text-sm text-destructive mt-1">{errors.fecha_caducidad.message}</p>}
        </div>

        <div>
          <Label htmlFor="imagen_url">URL de Imagen (Opcional)</Label>
          <Input id="imagen_url" {...register('imagen_url')} />
          {errors.imagen_url && <p className="text-sm text-destructive mt-1">{errors.imagen_url.message}</p>}
        </div>

        <div className="flex items-center space-x-2">
          <Controller
            name="activo"
            control={control}
            render={({ field }) => (
              <Checkbox id="activo" checked={field.value} onCheckedChange={field.onChange} />
            )}
          />
          <Label htmlFor="activo" className="font-normal">Activo</Label>
        </div>
        {errors.activo && <p className="text-sm text-destructive mt-1">{errors.activo.message}</p>}


        <div className="flex justify-end space-x-4 pt-4">
          <Button type="button" variant="outline" onClick={() => navigate('/products')} disabled={isSubmitting}>
            Cancelar
          </Button>
          <Button type="submit" disabled={isSubmitting}>
            {isSubmitting ? (isEditing ? 'Guardando...' : 'Creando...') : (isEditing ? 'Guardar Cambios' : 'Crear Producto')}
          </Button>
        </div>
      </form>
    </div>
  );
};

export default ProductFormPage;
