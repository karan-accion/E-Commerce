import React, { useEffect, useState } from 'react'
import axios from 'axios'

export default function InventoryManagement() {
  const [products, setProducts] = useState([])
  const [form, setForm] = useState({ name: '', price: '', stockQuantity: 0 })
  const [message, setMessage] = useState('')

  useEffect(() => {
    fetchProducts()
  }, [])

  async function fetchProducts() {
    try {
      const res = await axios.get('/products')
      setProducts(res.data)
    } catch (e) {
      console.error(e)
      setMessage('Failed to load products')
    }
  }

  async function handleCreate(e) {
    e.preventDefault()
    try {
      const payload = { ...form, price: parseFloat(form.price) }
      await axios.post('/products', payload)
      setMessage('Product created')
      setForm({ name: '', price: '', stockQuantity: 0 })
      fetchProducts()
    } catch (err) {
      console.error(err)
      setMessage('Create failed')
    }
  }

  return (
    <div>
      <h2>Inventory Management</h2>
      <form onSubmit={handleCreate} className="card">
        <label>Name<input value={form.name} onChange={e => setForm({ ...form, name: e.target.value })} required /></label>
        <label>Price<input value={form.price} onChange={e => setForm({ ...form, price: e.target.value })} required /></label>
        <label>Stock<input type="number" value={form.stockQuantity} onChange={e => setForm({ ...form, stockQuantity: parseInt(e.target.value || '0') })} min="0" required /></label>
        <button type="submit">Create Product</button>
      </form>

      <div className="message">{message}</div>

      <ul>
        {products.map(p => (
          <li key={p.id}>{p.name} — ${p.price} — stock: {p.stockQuantity}</li>
        ))}
      </ul>
    </div>
  )
}
