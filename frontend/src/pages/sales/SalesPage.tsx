import React, { useState, useEffect, useCallback } from 'react';
import { useNavigate } from 'react-router-dom';
import { Button } from '@/components/ui/button';
import { Input } from '@/components/ui/input';
import { Label } from '@/components/ui/label';
import {
  Table,
  TableBody,
  TableCell,
  TableHead,
  TableHeader,
  TableRow,
  TableFooter,
} from '@/components/ui/table';
import {
  Popover,
  PopoverTrigger,
  PopoverContent,
} from '@/components/ui/popover';
import {
  Command,
  CommandInput,
  CommandEmpty,
  CommandGroup,
  CommandItem,
  CommandList,
} from '@/components/ui/command';
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
import { Check, ChevronsUpDown, Trash2, Edit3, PlusCircle } from 'lucide-react';
import { toast } from 'sonner';
import { getProducts, createSale } from '@/services/api';
import { Producto } from '@/types/Producto';
import { SaleItemLine } from '@/types/sales';
import { VentaCreateDTO } from '@/types/Venta'; // Use existing VentaCreateDTO
import { debounce } from 'lodash';

const SalesPage: React.FC = () => {
  const navigate = useNavigate();

  // Product Search/Selection State
  const [productSearchQuery, setProductSearchQuery] = useState('');
  const [searchedProducts, setSearchedProducts] = useState<Producto[]>([]);
  const [selectedProduct, setSelectedProduct] = useState<Producto | null>(null);
  const [quantity, setQuantity] = useState<number>(1);
  const [isComboboxOpen, setIsComboboxOpen] = useState(false);

  // Current Sale State
  const [currentSaleItems, setCurrentSaleItems] = useState<SaleItemLine[]>([]);
  const [totalAmount, setTotalAmount] = useState(0);
  
  // Editing item state
  const [editingItem, setEditingItem] = useState<SaleItemLine | null>(null);
  const [editQuantity, setEditQuantity] = useState<number>(0);


  const debouncedProductSearch = useCallback(
    debounce(async (search: string) => {
      if (search.length < 2) {
        setSearchedProducts([]);
        return;
      }
      try {
        const response = await getProducts({ activo: true, nombre: search, limit: 10 });
        setSearchedProducts(response.data);
      } catch (error) {
        console.error("Error fetching products for search:", error);
        setSearchedProducts([]);
      }
    }, 300),
    []
  );

  useEffect(() => {
    debouncedProductSearch(productSearchQuery);
  }, [productSearchQuery, debouncedProductSearch]);

  useEffect(() => {
    const newTotal = currentSaleItems.reduce((sum, item) => sum + item.subtotal, 0);
    setTotalAmount(newTotal);
  }, [currentSaleItems]);

  const handleAddProductToSale = () => {
    if (!selectedProduct) {
      toast.error("Por favor, seleccione un producto.");
      return;
    }
    if (quantity <= 0) {
      toast.error("La cantidad debe ser mayor que cero.");
      return;
    }
    if (selectedProduct.stock !== null && selectedProduct.stock !== undefined && quantity > selectedProduct.stock) {
      toast.error(`Stock insuficiente para ${selectedProduct.nombre_producto}. Disponible: ${selectedProduct.stock}`);
      return;
    }
     if (selectedProduct.fecha_caducidad && new Date(selectedProduct.fecha_caducidad) < new Date()) {
      toast.error(`Producto '${selectedProduct.nombre_producto}' ha expirado.`);
      return;
    }


    const existingItemIndex = currentSaleItems.findIndex(item => item.producto.id_producto === selectedProduct.id_producto);

    if (existingItemIndex > -1) {
      // Product already in sale, update quantity
      const updatedItems = [...currentSaleItems];
      const existingItem = updatedItems[existingItemIndex];
      const newQuantity = existingItem.cantidad + quantity;

      if (selectedProduct.stock !== null && selectedProduct.stock !== undefined && newQuantity > selectedProduct.stock) {
        toast.error(`Stock insuficiente para ${selectedProduct.nombre_producto}. Total solicitado: ${newQuantity}, Disponible: ${selectedProduct.stock}`);
        return;
      }
      existingItem.cantidad = newQuantity;
      existingItem.subtotal = existingItem.producto.precio * newQuantity;
      setCurrentSaleItems(updatedItems);
    } else {
      // Add new product to sale
      setCurrentSaleItems(prevItems => [
        ...prevItems,
        {
          producto: selectedProduct,
          cantidad: quantity,
          subtotal: selectedProduct.precio * quantity,
        },
      ]);
    }
    // Reset selection
    setSelectedProduct(null);
    setProductSearchQuery('');
    setQuantity(1);
    setSearchedProducts([]);
  };
  
  const handleUpdateItemQuantity = (productId: number, newQuantity: number) => {
    if (newQuantity <= 0) {
      toast.error("La cantidad debe ser positiva. Para eliminar, use el botón de eliminar.");
      return;
    }
    setCurrentSaleItems(items => items.map(item => {
      if (item.producto.id_producto === productId) {
        if (item.producto.stock !== null && item.producto.stock !== undefined && newQuantity > item.producto.stock) {
          toast.error(`Stock insuficiente para ${item.producto.nombre_producto}. Disponible: ${item.producto.stock}`);
          return item; // Return original item if update fails
        }
        return { ...item, cantidad: newQuantity, subtotal: item.producto.precio * newQuantity };
      }
      return item;
    }));
    setEditingItem(null); // Close edit mode
  };

  const handleRemoveItem = (productId: number) => {
    setCurrentSaleItems(prevItems => prevItems.filter(item => item.producto.id_producto !== productId));
  };

  const handleCancelSale = () => {
    setCurrentSaleItems([]);
    setTotalAmount(0);
    setSelectedProduct(null);
    setProductSearchQuery('');
    setQuantity(1);
    toast.info("Venta cancelada.");
  };

  const handleSubmitSale = async () => {
    if (currentSaleItems.length === 0) {
      toast.error("No hay productos en la venta actual.");
      return;
    }

    const saleDetailsDTO = currentSaleItems.map(item => ({
      id_producto: item.producto.id_producto,
      cantidad: item.cantidad,
    }));
    
    // VentaCreateDTO from types/Venta.ts expects { detalles: SaleDetailCreateDTO[] }
    // where SaleDetailCreateDTO is { id_producto: number, cantidad: number }
    const salePayload: VentaCreateDTO = {
      detalles: saleDetailsDTO,
      // metodo_pago was removed from DTO
    };

    try {
      const createdSale = await createSale(salePayload);
      toast.success(`Venta #${createdSale.id_venta} registrada exitosamente!`);
      handleCancelSale(); // Clear current sale
      // Optionally navigate or show summary
    } catch (error: any) {
      const errorMsg = error.response?.data?.message || error.message || "Error al registrar la venta.";
      toast.error(errorMsg);
    }
  };

  return (
    <div className="container mx-auto py-8">
      <h1 className="text-3xl font-bold mb-6">Registrar Nueva Venta</h1>

      {/* Product Selection Area */}
      <div className="grid md:grid-cols-3 gap-6 mb-6 items-start">
        <div className="md:col-span-2 space-y-4">
          <Label>Buscar Producto</Label>
          <Popover open={isComboboxOpen} onOpenChange={setIsComboboxOpen}>
            <PopoverTrigger asChild>
              <Button
                variant="outline"
                role="combobox"
                aria-expanded={isComboboxOpen}
                className="w-full justify-between"
              >
                {selectedProduct
                  ? `${selectedProduct.nombre_producto} (Stock: ${selectedProduct.stock ?? 'N/A'})`
                  : "Seleccionar producto..."}
                <ChevronsUpDown className="ml-2 h-4 w-4 shrink-0 opacity-50" />
              </Button>
            </PopoverTrigger>
            <PopoverContent className="w-[--radix-popover-trigger-width] p-0">
              <Command>
                <CommandInput 
                  placeholder="Buscar producto por nombre..."
                  value={productSearchQuery}
                  onValueChange={setProductSearchQuery}
                />
                <CommandList>
                  <CommandEmpty>
                    {productSearchQuery.length < 2 ? "Escriba al menos 2 caracteres para buscar." : "No se encontraron productos."}
                  </CommandEmpty>
                  <CommandGroup>
                    {searchedProducts.map((product) => (
                      <CommandItem
                        key={product.id_producto}
                        value={`${product.nombre_producto} (ID: ${product.id_producto})`}
                        onSelect={() => {
                          setSelectedProduct(product);
                          setIsComboboxOpen(false);
                        }}
                        disabled={!product.activo || (product.stock !== null && product.stock !== undefined && product.stock <= 0)}
                      >
                        <Check className={`mr-2 h-4 w-4 ${selectedProduct?.id_producto === product.id_producto ? "opacity-100" : "opacity-0"}`} />
                        {product.nombre_producto} (Stock: {product.stock ?? 'N/A'}, Precio: ${product.precio.toFixed(2)})
                        {!product.activo && " (Inactivo)"}
                        {(product.stock !== null && product.stock !== undefined && product.stock <= 0) && " (Agotado)"}
                      </CommandItem>
                    ))}
                  </CommandGroup>
                </CommandList>
              </Command>
            </PopoverContent>
          </Popover>
        </div>
        <div className="space-y-4">
          <Label htmlFor="quantity">Cantidad</Label>
          <Input
            id="quantity"
            type="number"
            min="1"
            value={quantity}
            onChange={(e) => setQuantity(Math.max(1, parseInt(e.target.value, 10) || 1))}
            disabled={!selectedProduct}
          />
        </div>
        <Button onClick={handleAddProductToSale} disabled={!selectedProduct || quantity <=0} className="md:col-span-3">
          <PlusCircle className="mr-2 h-4 w-4" /> Agregar Producto a la Venta
        </Button>
      </div>

      {/* Current Sale Items Table */}
      <h2 className="text-2xl font-semibold mb-4">Productos en Venta Actual</h2>
      {currentSaleItems.length === 0 ? (
        <p className="text-muted-foreground">No hay productos agregados a la venta.</p>
      ) : (
        <Card>
        <Table>
          <TableHeader>
            <TableRow>
              <TableHead>Producto</TableHead>
              <TableHead className="text-right">Precio Unit.</TableHead>
              <TableHead className="text-center">Cantidad</TableHead>
              <TableHead className="text-right">Subtotal</TableHead>
              <TableHead className="text-center">Acciones</TableHead>
            </TableRow>
          </TableHeader>
          <TableBody>
            {currentSaleItems.map((item) => (
              <TableRow key={item.producto.id_producto}>
                <TableCell>{item.producto.nombre_producto}</TableCell>
                <TableCell className="text-right">${item.producto.precio.toFixed(2)}</TableCell>
                <TableCell className="text-center">
                  {editingItem?.producto.id_producto === item.producto.id_producto ? (
                    <div className="flex items-center justify-center gap-1">
                       <Input 
                          type="number" 
                          value={editQuantity} 
                          onChange={(e) => setEditQuantity(parseInt(e.target.value,10))}
                          className="w-16 h-8 text-center"
                          min="1"
                          max={item.producto.stock ?? undefined}
                        />
                        <Button size="icon" variant="ghost" className="h-8 w-8" onClick={() => handleUpdateItemQuantity(item.producto.id_producto, editQuantity)}>
                            <Check className="h-4 w-4 text-green-500"/>
                        </Button>
                        <Button size="icon" variant="ghost" className="h-8 w-8" onClick={() => setEditingItem(null)}>
                            <ChevronsUpDown className="h-4 w-4"/> {/* Represents cancel edit icon */}
                        </Button>
                    </div>
                  ) : (
                    <span>{item.cantidad}</span>
                  )}
                </TableCell>
                <TableCell className="text-right">${item.subtotal.toFixed(2)}</TableCell>
                <TableCell className="text-center space-x-1">
                  <Button variant="outline" size="icon" className="h-8 w-8" onClick={() => { setEditingItem(item); setEditQuantity(item.cantidad);}}>
                    <Edit3 className="h-4 w-4" />
                  </Button>
                  <Button variant="destructive" size="icon" className="h-8 w-8" onClick={() => handleRemoveItem(item.producto.id_producto)}>
                    <Trash2 className="h-4 w-4" />
                  </Button>
                </TableCell>
              </TableRow>
            ))}
          </TableBody>
          <TableFooter>
            <TableRow>
              <TableCell colSpan={3} className="text-right font-bold">Total</TableCell>
              <TableCell className="text-right font-bold">${totalAmount.toFixed(2)}</TableCell>
              <TableCell></TableCell>
            </TableRow>
          </TableFooter>
        </Table>
        </Card>
      )}

      {/* Action Buttons */}
      <div className="mt-8 flex justify-end space-x-4">
        <AlertDialog>
          <AlertDialogTrigger asChild>
            <Button variant="outline" disabled={currentSaleItems.length === 0}>Cancelar Venta</Button>
          </AlertDialogTrigger>
          <AlertDialogContent>
            <AlertDialogHeader><AlertDialogTitle>¿Cancelar Venta?</AlertDialogTitle></AlertDialogHeader>
            <AlertDialogDescription>
              Esto limpiará todos los productos de la venta actual. Esta acción no se puede deshacer.
            </AlertDialogDescription>
            <AlertDialogFooter>
              <AlertDialogCancel>No</AlertDialogCancel>
              <AlertDialogAction onClick={handleCancelSale}>Sí, Cancelar Venta</AlertDialogAction>
            </AlertDialogFooter>
          </AlertDialogContent>
        </AlertDialog>
        <Button onClick={handleSubmitSale} disabled={currentSaleItems.length === 0}>
          Registrar Venta
        </Button>
      </div>
    </div>
  );
};

export default SalesPage;
