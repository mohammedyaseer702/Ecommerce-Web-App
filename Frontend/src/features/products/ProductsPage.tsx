import { useEffect, useState } from "react";
import { getAllProducts } from "./productsApi";
import type { Product } from "./types";
import ProductCard from "./ProductCard";

export default function ProductsPage() {
  const [products, setProducts] = useState<Product[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");

  const [search, setSearch] = useState("");
  const [filtered, setFiltered] = useState<Product[]>([]);

  useEffect(() => {
    const fetchProducts = async () => {
      try {
        const data = await getAllProducts();
        setProducts(data);
        setFiltered(data);
      } catch (err) {
        setError("Failed to load products");
      } finally {
        setLoading(false);
      }
    };

    fetchProducts();
  }, []);

  // 🔍 SEARCH FILTER (FRONTEND)
  useEffect(() => {
    const result = products.filter((p) =>
      p.name.toLowerCase().includes(search.toLowerCase())
    );
    setFiltered(result);
  }, [search, products]);

  if (loading) {
    return <div className="text-center">Loading products...</div>;
  }

  if (error) {
    return <div className="text-red-500">{error}</div>;
  }

  return (
    <div>
      <h1 className="text-2xl font-bold mb-6">Products</h1>

      {/* 🔍 SEARCH BAR */}
      <input
        type="text"
        placeholder="Search products..."
        className="border p-2 mb-6 w-full rounded-lg"
        value={search}
        onChange={(e) => setSearch(e.target.value)}
      />

      {filtered.length === 0 ? (
        <div className="text-center text-gray-500">
          No products found.
        </div>
      ) : (
        <div className="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 lg:grid-cols-4 gap-6">
          {filtered.map((product) => (
            <ProductCard key={product.id} product={product} />
          ))}
        </div>
      )}
    </div>
  );
}