import React, { useEffect, useState } from 'react'
import axios from 'axios'

export default function UserManagement() {
  const [users, setUsers] = useState([])
  const [form, setForm] = useState({ name: '', email: '', password: '' })
  const [message, setMessage] = useState('')

  useEffect(() => {
    fetchUsers()
  }, [])

  async function fetchUsers() {
    try {
      const res = await axios.get('/users')
      setUsers(res.data)
    } catch (e) {
      console.error(e)
      setMessage('Failed to load users')
    }
  }

  async function handleCreate(e) {
    e.preventDefault()
    try {
      const res = await axios.post('/auth/register', form)
      setMessage('User created: ' + res.data.name)
      setForm({ name: '', email: '', password: '' })
      fetchUsers()
    } catch (err) {
      console.error(err)
      setMessage('Create failed')
    }
  }

  return (
    <div>
      <h2>User Management</h2>
      <form onSubmit={handleCreate} className="card">
        <label>Name<input value={form.name} onChange={e => setForm({ ...form, name: e.target.value })} required /></label>
        <label>Email<input value={form.email} onChange={e => setForm({ ...form, email: e.target.value })} required /></label>
        <label>Password<input type="password" value={form.password} onChange={e => setForm({ ...form, password: e.target.value })} required /></label>
        <button type="submit">Create User</button>
      </form>

      <div className="message">{message}</div>

      <ul>
        {users.map(u => (
          <li key={u.id}>{u.name} — {u.email}</li>
        ))}
      </ul>
    </div>
  )
}
