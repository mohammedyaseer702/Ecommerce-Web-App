import { useEffect, useState } from "react";
import AdminLayout from "./AdminLayout";
import toast from "react-hot-toast";
import {
  getAdminProducts,
  addProduct,
  updateProduct,
  deleteProduct,
} from "./adminApi";

export default function AdminProductsPage() {
  const [products, setProducts] = useState<any[]>([]);
  const [editing, setEditing] = useState<any>(null);

  const [form, setForm] = useState<any>({
    name: "",
    description: "",
    price: "",
    stock: "",
    category: "",
    imageUrl: "",
    active: true,
  });

  const fetchProducts = async () => {
    const data = await getAdminProducts();
    setProducts(data);
  };

  useEffect(() => {
    fetchProducts();
  }, []);

  // ✅ ADD
  const handleAdd = async (e: any) => {
    e.preventDefault();

    try {
      await addProduct({
        ...form,
        price: Number(form.price),
        stock: Number(form.stock),
      });

      toast.success("Product added");

      setForm({
        name: "",
        description: "",
        price: "",
        stock: "",
        category: "",
        imageUrl: "",
        active: true,
      });

      fetchProducts();
    } catch {
      toast.error("Error adding product");
    }
  };

  // ✅ DELETE (FIXED CONFIRMATION BELOW)
  const handleDelete = async (id: number) => {
    const confirmDelete = window.confirm("Are you sure to delete this product?");
    if (!confirmDelete) return;

    await deleteProduct(id);
    toast.success("Deleted");
    fetchProducts();
  };

  // ✅ UPDATE
  const handleUpdate = async () => {
    await updateProduct(editing.id, {
      ...editing,
      price: Number(editing.price),
      stock: Number(editing.stock),
    });

    toast.success("Updated");
    setEditing(null);
    fetchProducts();
  };

  return (
    <AdminLayout>

      <h1 className="text-3xl font-bold mb-6">Products</h1>

      {/* ✅ ADD FORM */}
      <form
        onSubmit={handleAdd}
        className="grid grid-cols-2 gap-4 bg-white p-6 rounded shadow mb-6"
      >
        <input placeholder="Name" className="border p-2" onChange={(e)=>setForm({...form,name:e.target.value})}/>
        <input placeholder="Price" className="border p-2" onChange={(e)=>setForm({...form,price:e.target.value})}/>
        <input placeholder="Stock" className="border p-2" onChange={(e)=>setForm({...form,stock:e.target.value})}/>
        <input placeholder="Category" className="border p-2" onChange={(e)=>setForm({...form,category:e.target.value})}/>
        <input placeholder="Image URL" className="border p-2 col-span-2" onChange={(e)=>setForm({...form,imageUrl:e.target.value})}/>
        <textarea placeholder="Description" className="border p-2 col-span-2" onChange={(e)=>setForm({...form,description:e.target.value})}/>
        <button className="col-span-2 bg-black text-white p-3 rounded">
          Add Product
        </button>
      </form>

      {/* ✅ TABLE */}
      <table className="w-full bg-white shadow">
        <thead>
          <tr className="bg-gray-200">
            <th>Image</th>
            <th>Name</th>
            <th>Price</th>
            <th>Stock</th>
            <th>Action</th>
          </tr>
        </thead>

        <tbody>
          {products.map((p) => (
            <tr key={p.id} className="text-center border-t">
              <td><img src={p.imageUrl} className="w-12 mx-auto"/></td>
              <td>{p.name}</td>
              <td>{p.price}</td>
              <td>{p.stock}</td>

              <td>
                <button
                  onClick={() => setEditing(p)}
                  className="bg-blue-500 text-white px-2 mr-2"
                >
                  Edit
                </button>

                <button
                  onClick={() => handleDelete(p.id)}
                  className="bg-red-500 text-white px-2"
                >
                  Delete
                </button>
              </td>
            </tr>
          ))}
        </tbody>
      </table>

      {/* ✅ EDIT MODAL */}
      {editing && (
        <div className="fixed inset-0 bg-black bg-opacity-50 flex justify-center items-center">
          <div className="bg-white p-6 space-y-3 rounded">

            <h2 className="text-xl font-bold">Edit Product</h2>

            <input value={editing.name} onChange={(e)=>setEditing({...editing,name:e.target.value})} className="border p-2 w-full"/>
            <input value={editing.price} onChange={(e)=>setEditing({...editing,price:e.target.value})} className="border p-2 w-full"/>
            <input value={editing.stock} onChange={(e)=>setEditing({...editing,stock:e.target.value})} className="border p-2 w-full"/>

            <div className="flex justify-between">
              <button onClick={handleUpdate} className="bg-green-500 text-white px-4 py-2 rounded">
                Save
              </button>

              <button onClick={()=>setEditing(null)} className="bg-gray-400 px-4 py-2 rounded">
                Cancel
              </button>
            </div>

          </div>
        </div>
      )}

    </AdminLayout>
  );
}