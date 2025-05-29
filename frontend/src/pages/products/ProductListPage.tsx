import React, { useEffect, useState, useCallback } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { getProducts, deleteProduct as apiDeleteProduct } from '@/services/api';
import { Producto } from '@/types/Producto';
import { Button } from '@/components/ui/button';
import { Input } from '@/components/ui/input';
import { Switch } from '@/components/ui/switch';
import { Label } from '@/components/ui/label';
import {
  Table,
  TableBody,
  TableCell,
  TableHead,
  TableHeader,
  TableRow,
} from '@/components/ui/table';
import {
  Pagination,
  PaginationContent,
  PaginationEllipsis,
  PaginationItem,
  PaginationLink,
  PaginationNext,
  PaginationPrevious,
} from "@/components/ui/pagination";
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
import { debounce } from 'lodash'; // For debouncing search input

const ITEMS_PER_PAGE = 10;

const ProductListPage: React.FC = () => {
  const [products, setProducts] = useState<Producto[]>([]);
  const [totalProducts, setTotalProducts] = useState(0);
  const [isLoading, setIsLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);

  const [searchTerm, setSearchTerm] = useState('');
  const [showActive, setShowActive] = useState<boolean | undefined>(true); // true, false, or undefined for all
  const [currentPage, setCurrentPage] = useState(1);
  
  const navigate = useNavigate();

  const fetchProducts = useCallback(async (page: number, search: string, active?: boolean) => {
    setIsLoading(true);
    setError(null);
    try {
      const params = {
        limit: ITEMS_PER_PAGE,
        offset: (page - 1) * ITEMS_PER_PAGE,
        nombre: search || undefined,
        activo: active,
      };
      const response = await getProducts(params);
      setProducts(response.data); // Assuming API returns { data: [], total: X }
      setTotalProducts(response.total);
    } catch (err) {
      const errorMsg = err instanceof Error ? err.message : 'Failed to fetch products';
      setError(errorMsg);
      toast.error(errorMsg);
    } finally {
      setIsLoading(false);
    }
  }, []);

  // Debounced search
  const debouncedFetchProducts = useCallback(debounce(fetchProducts, 500), [fetchProducts]);

  useEffect(() => {
    debouncedFetchProducts(currentPage, searchTerm, showActive);
  }, [currentPage, searchTerm, showActive, debouncedFetchProducts]);

  const handleSearchChange = (event: React.ChangeEvent<HTMLInputElement>) => {
    setSearchTerm(event.target.value);
    setCurrentPage(1); // Reset to first page on new search
  };

  const handleActiveToggle = (checked: boolean) => {
    setShowActive(checked);
    setCurrentPage(1);
  };
  
  const totalPages = Math.ceil(totalProducts / ITEMS_PER_PAGE);

  const handleDelete = async (productId: number) => {
    try {
      await apiDeleteProduct(productId);
      toast.success('Producto desactivado correctamente.');
      fetchProducts(currentPage, searchTerm, showActive); // Refresh list
    } catch (err) {
      const errorMsg = err instanceof Error ? err.message : 'Error al desactivar producto.';
      toast.error(errorMsg);
    }
  };

  return (
    <div className="container mx-auto py-8">
      <div className="flex flex-col sm:flex-row justify-between items-center mb-6 gap-4">
        <h1 className="text-3xl font-bold">Inventario de Productos</h1>
        <Button asChild>
          <Link to="/products/new">Crear Producto</Link>
        </Button>
      </div>

      <div className="flex flex-col sm:flex-row justify-between items-center mb-4 gap-4">
        <Input
          type="text"
          placeholder="Buscar por nombre..."
          value={searchTerm}
          onChange={handleSearchChange}
          className="max-w-sm"
        />
        <div className="flex items-center space-x-2">
          <Switch
            id="active-toggle"
            checked={showActive === true} // Control Switch based on true explicitly
            onCheckedChange={handleActiveToggle}
          />
          <Label htmlFor="active-toggle">Mostrar solo activos</Label>
        </div>
      </div>

      {isLoading && <p>Cargando productos...</p>}
      {error && <p className="text-destructive mb-4">Error: {error}</p>}

      <Table>
        <TableHeader>
          <TableRow>
            <TableHead>ID</TableHead>
            <TableHead>Nombre</TableHead>
            <TableHead className="hidden md:table-cell">Descripción</TableHead>
            <TableHead>Precio</TableHead>
            <TableHead className="hidden sm:table-cell">Cantidad</TableHead>
            <TableHead className="hidden md:table-cell">Caducidad</TableHead>
            <TableHead>Activo</TableHead>
            <TableHead>Acciones</TableHead>
          </TableRow>
        </TableHeader>
        <TableBody>
          {products.map((product) => (
            <TableRow key={product.id_producto}>
              <TableCell>{product.id_producto}</TableCell>
              <TableCell>{product.nombre_producto}</TableCell>
              <TableCell className="hidden md:table-cell truncate max-w-xs">{product.descripcion}</TableCell>
              <TableCell>${product.precio.toFixed(2)}</TableCell>
              <TableCell className="hidden sm:table-cell">{product.stock}</TableCell>
              <TableCell className="hidden md:table-cell">
                {product.fecha_caducidad ? new Date(product.fecha_caducidad).toLocaleDateString() : 'N/A'}
              </TableCell>
              <TableCell>{product.activo ? 'Sí' : 'No'}</TableCell>
              <TableCell className="space-x-2 whitespace-nowrap">
                <Button variant="outline" size="sm" onClick={() => navigate(`/products/edit/${product.id_producto}`)}>
                  Editar
                </Button>
                <AlertDialog>
                  <AlertDialogTrigger asChild>
                    <Button variant="destructive" size="sm">
                      {product.activo ? 'Desactivar' : 'Activar'}
                    </Button>
                  </AlertDialogTrigger>
                  <AlertDialogContent>
                    <AlertDialogHeader>
                      <AlertDialogTitle>¿Estás seguro?</AlertDialogTitle>
                      <AlertDialogDescription>
                        Esta acción marcará el producto como '{product.activo ? 'inactivo' : 'activo'}'.
                      </AlertDialogDescription>
                    </AlertDialogHeader>
                    <AlertDialogFooter>
                      <AlertDialogCancel>Cancelar</AlertDialogCancel>
                      <AlertDialogAction onClick={() => handleDelete(product.id_producto)}>
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
      
      {totalPages > 1 && (
        <Pagination className="mt-6">
          <PaginationContent>
            <PaginationItem>
              <PaginationPrevious 
                href="#" 
                onClick={(e) => { e.preventDefault(); if(currentPage > 1) setCurrentPage(currentPage - 1);}}
                className={currentPage === 1 ? "pointer-events-none opacity-50" : undefined}
              />
            </PaginationItem>
            {/* Simplified pagination links - ideally generate these dynamically */}
            {[...Array(totalPages)].map((_, i) => (
              <PaginationItem key={i}>
                <PaginationLink 
                  href="#" 
                  onClick={(e) => { e.preventDefault(); setCurrentPage(i + 1);}}
                  isActive={currentPage === i + 1}
                >
                  {i + 1}
                </PaginationLink>
              </PaginationItem>
            ))}
             {/* Consider adding PaginationEllipsis if many pages */}
            <PaginationItem>
              <PaginationNext 
                href="#" 
                onClick={(e) => { e.preventDefault(); if(currentPage < totalPages) setCurrentPage(currentPage + 1);}}
                className={currentPage === totalPages ? "pointer-events-none opacity-50" : undefined}
              />
            </PaginationItem>
          </PaginationContent>
        </Pagination>
      )}
    </div>
  );
};

export default ProductListPage;
