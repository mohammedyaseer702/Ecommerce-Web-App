import { useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import api from "../../api/axios";
import { useCart } from "../cart/CartContext";
import toast from "react-hot-toast";

export default function ProductDetailsPage() {
  const { id } = useParams();
  const [product, setProduct] = useState<any>(null);
  const [loading, setLoading] = useState(true);
  const [qty, setQty] = useState(1);
  const [rating, setRating] = useState(5);
  const [comment, setComment] = useState("");
  const [related, setRelated] = useState<any[]>([]);
  const [reviews, setReviews] = useState<any[]>([]);

  const { addToCart } = useCart();

  // ================= LOAD PRODUCT =================
  useEffect(() => {
    if (!id) return;

    api.get(`/products/${id}`)
      .then(res => setProduct(res.data.data))
      .catch(() => toast.error("Failed to load product"))
      .finally(() => setLoading(false));
  }, [id]);

  // ================= LOAD REVIEWS =================
  useEffect(() => {
    if (!id) return;

    api.get(`/reviews/${id}`)
      .then(res => setReviews(res.data))
      .catch(() => {});
  }, [id]);

  // ================= RELATED PRODUCTS =================
  useEffect(() => {
    if (!product) return;

    api.get("/products/public")
      .then(res => {
        const all = res.data.data.content;

        const filtered = all.filter((p: any) =>
          p.category === product.category &&
          p.id !== product.id
        );

        setRelated(filtered.slice(0, 4));
      });
  }, [product]);

  // ================= AVERAGE RATING =================
  const averageRating =
    reviews.length === 0
      ? 0
      : reviews.reduce((sum, r) => sum + r.rating, 0) / reviews.length;

  // ================= STAR COMPONENT =================
  const Star = ({ fill = 0 }: any) => (
    <svg viewBox="0 0 24 24" className="w-5 h-5">
      <defs>
        <linearGradient id={`grad-${fill}`}>
          <stop offset={`${fill * 100}%`} stopColor="#facc15" />
          <stop offset={`${fill * 100}%`} stopColor="#e5e7eb" />
        </linearGradient>
      </defs>
      <path
        fill={`url(#grad-${fill})`}
        d="M12 17.27L18.18 21l-1.64-7.03L22 
           9.24l-7.19-.61L12 2 9.19 
           8.63 2 9.24l5.46 4.73L5.82 
           21z"
      />
    </svg>
  );

  const renderStars = (rating: number) => {
    return (
      <div className="flex items-center gap-1">
        {[1, 2, 3, 4, 5].map((i) => {
          const fill =
            rating >= i ? 1 : rating >= i - 0.5 ? 0.5 : 0;
          return <Star key={i} fill={fill} />;
        })}
      </div>
    );
  };

  // ================= ADD TO CART =================
  const handleAddToCart = async () => {
    try {
      await addToCart({ ...product, quantity: qty });
      toast.success("Added to cart 🛒");
    } catch (err: any) {
      toast.error(err?.response?.data?.message || "Failed to add");
    }
  };

  // ================= BUY NOW =================
  const handleBuyNow = async () => {
    const token = localStorage.getItem("token");

    if (!token) {
      window.location.href = "/login";
      return;
    }

    try {
      await addToCart({ ...product, quantity: qty });
      window.location.href = "/cart";
    } catch {
      toast.error("Failed to proceed");
    }
  };

  // ================= SUBMIT REVIEW =================
  const submitReview = async () => {
    try {
      await api.post("/reviews", {
        productId: product.id,
        rating: rating,
        comment: comment
      });

      setComment("");
      setRating(5);

      const res = await api.get(`/reviews/${product.id}`);
      setReviews(res.data);

      toast.success("Review added ⭐");
    } catch {
      toast.error("Failed to submit review");
    }
  };

  if (loading) return <p className="text-center">Loading...</p>;
  if (!product) return <p className="text-center">Product not found</p>;

  return (
    <div className="p-8 bg-gray-50 min-h-screen">

      {/* PRODUCT */}
      <div className="max-w-6xl mx-auto bg-white shadow-lg rounded-2xl p-6 grid md:grid-cols-2 gap-10">

        <img
          src={product.imageUrl}
          alt={product.name}
          className="w-full max-h-[400px] object-contain rounded-xl border"
        />

        <div className="space-y-5">

          <h1 className="text-4xl font-bold">{product.name}</h1>

          {/* ⭐ AVERAGE RATING */}
          <div className="flex items-center gap-2">
            {renderStars(averageRating)}
            <span className="text-gray-600 text-sm">
              {averageRating.toFixed(1)} ({reviews.length} reviews)
            </span>
          </div>

          <p className="text-gray-500">{product.description}</p>

          <div className="text-3xl font-bold text-green-600">
            ₹{product.price}
          </div>

          <div className={`font-semibold ${
            product.stock > 0 ? "text-green-600" : "text-red-500"
          }`}>
            {product.stock > 0
              ? `✔ In Stock (${product.stock})`
              : "❌ Out of Stock"}
          </div>

          <input
            type="number"
            min={1}
            value={qty}
            onChange={(e) => setQty(Number(e.target.value))}
            className="border p-2 w-20 rounded"
          />

          <div className="flex gap-4">

            <button
              onClick={handleAddToCart}
              className="flex-1 bg-black text-white py-3 rounded-xl"
            >
              Add to Cart
            </button>

            <button
              onClick={handleBuyNow}
              className="flex-1 bg-green-600 text-white py-3 rounded-xl"
            >
              Buy Now
            </button>

          </div>
        </div>
      </div>

      {/* REVIEWS */}
      <div className="max-w-6xl mx-auto mt-10 grid md:grid-cols-2 gap-8">

        <div className="bg-white p-6 rounded-xl shadow">
          <h2 className="text-xl font-semibold mb-4">Customer Reviews</h2>

          {reviews.map((r: any, i: number) => (
            <div key={i} className="border-b py-4">

              {/* USER + VERIFIED */}
              <div className="flex items-center justify-between">
                <p className="font-semibold">
                  {r.user?.name || "User"}
                </p>
                <span className="text-green-600 text-xs font-semibold">
                  ✔ Verified Purchase
                </span>
              </div>

              {/* DATE */}
              <p className="text-xs text-gray-400">
                {r.createdAt
                  ? new Date(r.createdAt).toLocaleDateString()
                  : ""}
              </p>

              {/* STARS */}
              <div className="mt-1">
                {renderStars(r.rating)}
              </div>

              {/* COMMENT */}
              <p className="mt-2 text-gray-700">{r.comment}</p>


{/* 👍 👎 ACTIONS */}
    <div className="flex gap-4 mt-3 text-sm">

      <button
        onClick={async () => {
          await api.put(`/reviews/${r.id}/like`);
          setReviews(prev =>
            prev.map(item =>
              item.id === r.id
                ? { ...item, likes: (item.likes || 0) + 1 }
                : item
            )
          );
        }}
        className="flex items-center gap-1 text-gray-600 hover:text-green-600"
      >
        👍 {r.likes || 0}
      </button>

      <button
        onClick={async () => {
          await api.put(`/reviews/${r.id}/dislike`);
          setReviews(prev =>
            prev.map(item =>
              item.id === r.id
                ? { ...item, dislikes: (item.dislikes || 0) + 1 }
                : item
            )
          );
        }}
        className="flex items-center gap-1 text-gray-600 hover:text-red-500"
      >
        👎 {r.dislikes || 0}
      </button>

    </div>



            </div>
          ))}
        </div>

        {/* ADD REVIEW */}
        <div className="bg-white p-6 rounded-xl shadow">
          <h2 className="text-lg font-semibold mb-3">Add Review</h2>

          <input
            type="number"
            min={1}
            max={5}
            value={rating}
            onChange={(e) => setRating(Number(e.target.value))}
            className="border p-2 w-full mb-3 rounded"
          />

          <textarea
            value={comment}
            onChange={(e) => setComment(e.target.value)}
            className="border p-2 w-full mb-3 rounded"
          />

          <button
            onClick={submitReview}
            className="w-full bg-black text-white py-2 rounded"
          >
            Submit Review
          </button>
        </div>
      </div>

      {/* RELATED */}
      <div className="max-w-6xl mx-auto mt-10">
        <h2 className="text-xl font-bold mb-4">Related Products</h2>

        <div className="grid grid-cols-2 md:grid-cols-4 gap-4">
          {related.map((p: any) => (
            <div
              key={p.id}
              onClick={() => window.location.href = `/products/${p.id}`}
              className="cursor-pointer bg-white p-3 rounded shadow"
            >
              <img src={p.imageUrl} className="h-32 w-full object-cover" />
              <p className="text-sm mt-2">{p.name}</p>
              <p className="text-green-600 font-bold">₹{p.price}</p>
            </div>
          ))}
        </div>
      </div>

    </div>
  );
}