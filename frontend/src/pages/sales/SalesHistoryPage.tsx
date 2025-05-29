import React, { useEffect, useState, useCallback } from 'react';
import {
  getSales as apiGetSalesHistory, // Renaming for clarity if needed, or use as is
  getAllAdmins,
} from '@/services/api';
import { VentaDisplayDTO } from '@/types/VentaDisplayDTO'; // Corrected import
import { UserPublic } from '@/types/user';
import { Button } from '@/components/ui/button';
import {
  Table,
  TableBody,
  TableCell,
  TableHead,
  TableHeader,
  TableRow,
  TableCaption,
} from '@/components/ui/table';
import {
  Pagination,
  PaginationContent,
  PaginationItem,
  PaginationLink,
  PaginationNext,
  PaginationPrevious,
} from "@/components/ui/pagination";
import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from "@/components/ui/select";
import {
  Popover,
  PopoverTrigger,
  PopoverContent,
} from "@/components/ui/popover";
import { Calendar } from "@/components/ui/calendar";
import { Dialog, DialogContent, DialogHeader, DialogTitle, DialogDescription, DialogFooter } from "@/components/ui/dialog";
import { CalendarIcon, SearchIcon, XCircleIcon } from 'lucide-react';
import { format, isValid } from 'date-fns';
import { toast } from "sonner";
import { debounce } from 'lodash';

const ITEMS_PER_PAGE = 10;

const SalesHistoryPage: React.FC = () => {
  const [sales, setSales] = useState<VentaDisplayDTO[]>([]);
  const [totalSales, setTotalSales] = useState(0);
  const [admins, setAdmins] = useState<UserPublic[]>([]);
  
  const [isLoading, setIsLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);

  // Filter states
  const [filterAdminId, setFilterAdminId] = useState<string>(''); // Store as string for Select compatibility
  const [filterFechaInicio, setFilterFechaInicio] = useState<Date | undefined>(undefined);
  const [filterFechaFin, setFilterFechaFin] = useState<Date | undefined>(undefined);
  
  const [currentPage, setCurrentPage] = useState(1);

  // Detail view state
  const [selectedSale, setSelectedSale] = useState<VentaDisplayDTO | null>(null);
  const [isDetailModalOpen, setIsDetailModalOpen] = useState(false);

  const fetchSales = useCallback(async (page: number, adminId?: number, fechaInicio?: string, fechaFin?: string) => {
    setIsLoading(true);
    setError(null);
    try {
      const params = {
        limit: ITEMS_PER_PAGE,
        offset: (page - 1) * ITEMS_PER_PAGE,
        adminId: adminId || undefined,
        fechaInicio: fechaInicio || undefined,
        fechaFin: fechaFin || undefined,
      };
      // Assuming getSales returns { data: VentaDisplayDTO[], total: number }
      const response = await apiGetSalesHistory(params); 
      setSales(response.data); 
      setTotalSales(response.total);
    } catch (err) {
      const errorMsg = err instanceof Error ? err.message : 'Failed to fetch sales history';
      setError(errorMsg);
      toast.error(errorMsg);
    } finally {
      setIsLoading(false);
    }
  }, []);
  
  useEffect(() => {
    // Fetch admins for filter dropdown
    getAllAdmins()
      .then(setAdmins)
      .catch(() => toast.error("Error al cargar lista de administradores para el filtro."));
  }, []);

  useEffect(() => {
    const formattedFechaInicio = filterFechaInicio ? format(filterFechaInicio, 'yyyy-MM-dd') : undefined;
    const formattedFechaFin = filterFechaFin ? format(filterFechaFin, 'yyyy-MM-dd') : undefined;
    const numericAdminId = filterAdminId ? parseInt(filterAdminId) : undefined;
    fetchSales(currentPage, numericAdminId, formattedFechaInicio, formattedFechaFin);
  }, [currentPage, filterAdminId, filterFechaInicio, filterFechaFin, fetchSales]);

  const handleFilterSubmit = () => {
    setCurrentPage(1); // Reset to first page when filters are applied
    // useEffect will trigger fetchSales due to filter state change
    // To force re-fetch if states haven't changed but user clicks search:
    const formattedFechaInicio = filterFechaInicio ? format(filterFechaInicio, 'yyyy-MM-dd') : undefined;
    const formattedFechaFin = filterFechaFin ? format(filterFechaFin, 'yyyy-MM-dd') : undefined;
    const numericAdminId = filterAdminId ? parseInt(filterAdminId) : undefined;
    fetchSales(1, numericAdminId, formattedFechaInicio, formattedFechaFin);
  };

  const handleClearFilters = () => {
    setFilterAdminId('');
    setFilterFechaInicio(undefined);
    setFilterFechaFin(undefined);
    setCurrentPage(1); 
    // useEffect will trigger fetchSales
  };
  
  const totalPages = Math.ceil(totalSales / ITEMS_PER_PAGE);

  const handleViewDetails = (sale: VentaDisplayDTO) => {
    setSelectedSale(sale);
    setIsDetailModalOpen(true);
  };

  return (
    <div className="container mx-auto py-8">
      <h1 className="text-3xl font-bold mb-6">Historial de Ventas</h1>

      {/* Filter Controls */}
      <div className="mb-6 p-4 bg-card rounded-lg shadow grid grid-cols-1 md:grid-cols-4 gap-4 items-end">
        <div>
          <Label htmlFor="adminFilter">Filtrar por Vendedor</Label>
          <Select value={filterAdminId} onValueChange={setFilterAdminId}>
            <SelectTrigger id="adminFilter">
              <SelectValue placeholder="Todos los vendedores" />
            </SelectTrigger>
            <SelectContent>
              <SelectItem value="">Todos los vendedores</SelectItem>
              {admins.map(admin => (
                <SelectItem key={admin.id_administrador} value={String(admin.id_administrador)}>
                  {admin.nombre_completo}
                </SelectItem>
              ))}
            </SelectContent>
          </Select>
        </div>
        <div>
          <Label htmlFor="fechaInicio">Fecha Inicio</Label>
          <Popover>
            <PopoverTrigger asChild>
              <Button variant="outline" className="w-full justify-start text-left font-normal">
                <CalendarIcon className="mr-2 h-4 w-4" />
                {filterFechaInicio ? format(filterFechaInicio, "PPP") : <span>Seleccionar fecha</span>}
              </Button>
            </PopoverTrigger>
            <PopoverContent className="w-auto p-0"><Calendar mode="single" selected={filterFechaInicio} onSelect={setFilterFechaInicio} initialFocus /></PopoverContent>
          </Popover>
        </div>
        <div>
          <Label htmlFor="fechaFin">Fecha Fin</Label>
          <Popover>
            <PopoverTrigger asChild>
              <Button variant="outline" className="w-full justify-start text-left font-normal">
                <CalendarIcon className="mr-2 h-4 w-4" />
                {filterFechaFin ? format(filterFechaFin, "PPP") : <span>Seleccionar fecha</span>}
              </Button>
            </PopoverTrigger>
            <PopoverContent className="w-auto p-0"><Calendar mode="single" selected={filterFechaFin} onSelect={setFilterFechaFin} initialFocus /></PopoverContent>
          </Popover>
        </div>
        <div className="flex space-x-2">
          <Button onClick={handleFilterSubmit} className="w-full md:w-auto"><SearchIcon className="mr-2 h-4 w-4"/>Buscar</Button>
          <Button onClick={handleClearFilters} variant="outline" className="w-full md:w-auto"><XCircleIcon className="mr-2 h-4 w-4"/>Limpiar</Button>
        </div>
      </div>

      {isLoading && <p>Cargando historial de ventas...</p>}
      {error && <p className="text-destructive mb-4">Error: {error}</p>}

      <Table>
        <TableCaption>{totalSales === 0 && !isLoading ? "No se encontraron ventas." : `Total de ventas: ${totalSales}`}</TableCaption>
        <TableHeader>
          <TableRow>
            <TableHead>ID Venta</TableHead>
            <TableHead>Fecha</TableHead>
            <TableHead>Vendedor</TableHead>
            <TableHead className="text-right">Monto Total</TableHead>
            <TableHead>Estado</TableHead>
            <TableHead>Acciones</TableHead>
          </TableRow>
        </TableHeader>
        <TableBody>
          {sales.map((sale) => (
            <TableRow key={sale.id_venta}>
              <TableCell>#{sale.id_venta}</TableCell>
              <TableCell>{format(new Date(sale.fecha_venta), "dd/MM/yyyy HH:mm")}</TableCell>
              <TableCell>{sale.nombre_administrador_venta || 'N/A'}</TableCell>
              <TableCell className="text-right">${sale.total_venta.toFixed(2)}</TableCell>
              <TableCell>{sale.estado}</TableCell>
              <TableCell>
                <Button variant="outline" size="sm" onClick={() => handleViewDetails(sale)}>
                  Ver Detalles
                </Button>
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
            {[...Array(Math.min(totalPages, 5))].map((_, i) => { // Show limited page numbers
                let pageNum = i + 1;
                if (totalPages > 5 && currentPage > 3) {
                    pageNum = currentPage - 2 + i;
                    if (pageNum > totalPages) return null; // Don't render if pageNum exceeds total
                }
               return (
                <PaginationItem key={pageNum}>
                  <PaginationLink 
                    href="#" 
                    onClick={(e) => { e.preventDefault(); setCurrentPage(pageNum);}}
                    isActive={currentPage === pageNum}
                  >
                    {pageNum}
                  </PaginationLink>
                </PaginationItem>
               );
            })}
            {totalPages > 5 && currentPage < totalPages - 2 && <PaginationEllipsis />}
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

      {/* Sale Detail Modal */}
      <Dialog open={isDetailModalOpen} onOpenChange={setIsDetailModalOpen}>
        <DialogContent className="sm:max-w-2xl">
          <DialogHeader>
            <DialogTitle>Detalle de Venta #{selectedSale?.id_venta}</DialogTitle>
            {selectedSale && (
              <DialogDescription>
                Fecha: {format(new Date(selectedSale.fecha_venta), "dd/MM/yyyy HH:mm")} | 
                Vendedor: {selectedSale.nombre_administrador_venta || 'N/A'} | 
                Estado: {selectedSale.estado}
              </DialogDescription>
            )}
          </DialogHeader>
          {selectedSale && (
            <div>
              <Table className="mt-4">
                <TableHeader>
                  <TableRow>
                    <TableHead>ID Producto</TableHead>
                    <TableHead>Nombre Producto</TableHead>
                    <TableHead className="text-right">Cantidad</TableHead>
                    <TableHead className="text-right">Precio Unit.</TableHead>
                    <TableHead className="text-right">Subtotal</TableHead>
                  </TableRow>
                </TableHeader>
                <TableBody>
                  {selectedSale.detalles.map((detalle, index) => (
                    <TableRow key={index}> {/* Use index if id_detalle_venta is not available/unique here */}
                      <TableCell>{detalle.id_producto}</TableCell>
                      <TableCell>{detalle.nombre_producto}</TableCell>
                      <TableCell className="text-right">{detalle.cantidad}</TableCell>
                      <TableCell className="text-right">${detalle.precio_unitario_venta.toFixed(2)}</TableCell>
                      <TableCell className="text-right">${detalle.subtotal.toFixed(2)}</TableCell>
                    </TableRow>
                  ))}
                </TableBody>
                <TableFooter>
                  <TableRow>
                    <TableCell colSpan={4} className="text-right font-bold">Total Venta:</TableCell>
                    <TableCell className="text-right font-bold">${selectedSale.total_venta.toFixed(2)}</TableCell>
                  </TableRow>
                </TableFooter>
              </Table>
            </div>
          )}
          <DialogFooter className="mt-4">
            <Button variant="outline" onClick={() => setIsDetailModalOpen(false)}>Cerrar</Button>
          </DialogFooter>
        </DialogContent>
      </Dialog>
    </div>
  );
};

export default SalesHistoryPage;
